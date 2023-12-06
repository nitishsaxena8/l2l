package com.xperience.aem.xpbootstrap.core.component.Utils;


import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.factory.ModelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.Template;
import com.day.cq.wcm.api.components.ComponentContext;
import com.day.cq.wcm.api.designer.Designer;
import com.day.cq.wcm.api.designer.Style;
import com.day.cq.wcm.foundation.AllowedComponentList;
import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.google.common.collect.ImmutableSet;
import com.xperience.aem.xpbootstrap.core.component.internal.PageModelImpl;
import com.xperience.aem.xpbootstrap.core.component.models.XF;


public class Utils {

    /**
     * Name of the subservice used to authenticate as in order to be able to read details about components and
     * client libraries.
     */
    public static final String COMPONENTS_SERVICE = "components-service";
    private static final Logger LOGGER = LoggerFactory.getLogger(Utils.class);
    private static final Set<String> INTERNAL_PARAMETER = ImmutableSet.of(
            ":formstart",
            "_charset_",
            ":redirect",
            ":cq_csrf_token"
    );

    private Utils() {
    }

    /**
     * Name of the separator character used between prefix and hash when generating an ID, e.g. image-5c7e0ef90d
     */
    public static final String ID_SEPARATOR = "-";

    /**
     * Length of the ID hash.
     */
    private static final int ID_HASH_LENGTH = 10;
    
    private static final String NN_PAGE_FEATURED_IMAGE = "cq:featuredimage";
    
    
    /**
     * Returns a set of resource types for components used to render a given page, including those
     * from the page template and embedded experience templates.
     *
     * @param page         the {@link Page}
     * @param request      the current request
     * @param modelFactory the {@link ModelFactory}
     * @return The set of resource types for components used to render a page.
     */
    @NotNull
    public static Set<String> getPageResourceTypes(@NotNull Page page, @NotNull SlingHttpServletRequest request, @NotNull ModelFactory modelFactory) {
        Set<String> resourceTypes = new HashSet<>();
        resourceTypes.addAll(getResourceTypes(page.getContentResource(), request, modelFactory));
        resourceTypes.addAll(getTemplateResourceTypes(page, request, modelFactory));
        return resourceTypes;
    }

    /**
     * Returns a set of resource types for components used to render a given resource,
     * including it's direct children
     *
     * @param resource     the resource
     * @param request      the current request
     * @param modelFactory the {@link ModelFactory}
     * @return a set of resource types for components used to render the resource
     */
    @NotNull
    public static Set<String> getResourceTypes(@NotNull Resource resource, @NotNull SlingHttpServletRequest request, @NotNull ModelFactory modelFactory) {
        Set<String> resourceTypes = new HashSet<>();
        resourceTypes.add(resource.getResourceType());
        resourceTypes.addAll(getXFResourceTypes(resource, request, modelFactory));
        for (Resource child : resource.getChildren()) {
            resourceTypes.addAll(getResourceTypes(child, request, modelFactory));
        }
        return resourceTypes;
    }

    /**
     * Returns a set of resource types for components included in the experience template
     *
     * @param resource     the resource, will be tested to see if it's an experience template
     * @param request      the current request
     * @param modelFactory the {@link ModelFactory}
     * @return a set of resource types for components included in the experience template
     */
    @NotNull
    public static Set<String> getXFResourceTypes(@NotNull Resource resource, @NotNull SlingHttpServletRequest request, @NotNull ModelFactory modelFactory) {
        XF experienceFragment = modelFactory.getModelFromWrappedRequest(request, resource, XF.class);
        if (experienceFragment != null) {
            String fragmentPath = experienceFragment.getLocalizedFragmentVariationPath();
            if (StringUtils.isNotEmpty(fragmentPath)) {
                Resource fragmentResource = resource.getResourceResolver().getResource(fragmentPath);
                if (fragmentResource != null) {
                    return getResourceTypes(fragmentResource, request, modelFactory);
                }
            }
        }
        return Collections.emptySet();
    }

    /**
     * Returns a set of resource types for components included in the page template
     *
     * @param page         the page
     * @param request      the current request
     * @param modelFactory the {@link ModelFactory}
     * @return a set of resource types for components included in the page template
     */
    @NotNull
    public static Set<String> getTemplateResourceTypes(@NotNull Page page, @NotNull SlingHttpServletRequest request, @NotNull ModelFactory modelFactory) {
        Template template = page.getTemplate();
        if (template != null) {
            String templatePath = template.getPath() + AllowedComponentList.STRUCTURE_JCR_CONTENT;
            Resource templateResource = request.getResourceResolver().getResource(templatePath);
            if (templateResource != null) {
                return getResourceTypes(templateResource, request, modelFactory);
            }
        }
        return Collections.emptySet();
    }

    /**
     * Returns all the super-types of a component defined by its resource type.
     *
     * @param resourceType     the resource type of the component.
     * @param resourceResolver the resource resolver.
     * @return a set of the inherited resource types.
     */
    @NotNull
    public static Set<String> getSuperTypes(@NotNull final String resourceType, @NotNull final ResourceResolver resourceResolver) {
        Set<String> superTypes = new HashSet<>();
        String superType = resourceType;
        while (superType != null) {
            superType = Optional.ofNullable(resourceResolver.getResource(superType))
                    .map(Resource::getResourceSuperType)
                    .filter(StringUtils::isNotEmpty)
                    .orElse(null);
            if (superType != null) {
                superTypes.add(superType);
            }
        }
        return superTypes;
    }


    /**
     * Get the inherited value of a property from the page content resource. Walk the content tree upwards until an override of that
     * property is specified.
     *
     * @param startPage    the page in the content tree to start looking for the requested property.
     * @param propertyName the name of the property which is inherited.
     * @return the inherited value of the property or empty string if the property is not specified in the content tree.
     */
    @NotNull
    public static String getInheritedValue(com.day.cq.wcm.api.Page startPage, String propertyName) {
        if (startPage == null) {
            return StringUtils.EMPTY;
        }

        com.day.cq.wcm.api.Page tmp = startPage;

        while (tmp != null && tmp.hasContent() && tmp.getDepth() > 1) {
            ValueMap props = tmp.getProperties();
            if (props != null) {
                boolean override = Boolean.parseBoolean(props.get(propertyName + "_override", String.class));
                if (override) {
                    return props.get(propertyName, StringUtils.EMPTY);
                }
                tmp = tmp.getParent();
            }
        }
        return StringUtils.EMPTY;
    }

    /**
     * Converts the input into a set of strings. The input can be either a {@link Collection}, an array or a CSV.
     *
     * @param input - the input
     * @return Set of strings from input
     */
    @NotNull
    public static Set<String> getStrings(@Nullable final Object input) {
        Set<String> strings = new LinkedHashSet<>();
        if (input != null) {
            Class clazz = input.getClass();
            if (Collection.class.isAssignableFrom(clazz)) {
                // Try to convert from a collection
                for (Object obj : (Collection) input) {
                    if (obj != null) {
                        strings.add(obj.toString());
                    }
                }
            } else if (Object[].class.isAssignableFrom(clazz)) {
                // Try to convert from an array
                for (Object obj : (Object[]) input) {
                    if (obj != null) {
                        strings.add(obj.toString());
                    }
                }
            } else if (String.class.isAssignableFrom(clazz)) {
                // Try to convert from a CSV string
                for (String str : ((String) input).split(",")) {
                    if (StringUtils.isNotBlank(str)) {
                        strings.add(str.trim());
                    }
                }
            }
        }
        return strings;
    }

    /**
     * Converts request parameters to a JSON object and filter AEM specific parameters out.
     *
     * @param request - the current {@link SlingHttpServletRequest}
     * @return JSON object of the request parameters
     */
//    public static JSONObject getJsonOfRequestParameters(SlingHttpServletRequest request) throws JSONException {
//        Set<String> formFieldNames = getFormFieldNames(request);
//        org.json.JSONObject jsonObj = new org.json.JSONObject();
//        Map<String, String[]> params = request.getParameterMap();
//
//        for (Map.Entry<String, String[]> entry : params.entrySet()) {
//            if (!INTERNAL_PARAMETER.contains(entry.getKey()) && formFieldNames.contains(entry.getKey())) {
//                String[] v = entry.getValue();
//                Object o = (v.length == 1) ? v[0] : v;
//                jsonObj.put(entry.getKey(), o);
//            }
//        }
//        return jsonObj;
//    }

    /**
     * Returns a set of form field names for the form specified in the request.
     *
     * @param request - the current {@link SlingHttpServletRequest}
     * @return Set of form field names
     */
//    public static Set<String> getFormFieldNames(SlingHttpServletRequest request) {
//        Set<String> formFieldNames = new LinkedHashSet<>();
//        collectFieldNames(request.getResource(), formFieldNames);
//        return formFieldNames;
//    }

//    public static void collectFieldNames(Resource resource, Set<String> fieldNames) {
//        if (resource != null) {
//            for (Resource child : resource.getChildren()) {
//                String name = child.getValueMap().get(Field.PN_NAME, String.class);
//                if (StringUtils.isNotEmpty(name)) {
//                    fieldNames.add(name);
//                }
//                collectFieldNames(child, fieldNames);
//            }
//        }
//    }

    /**
     * Returns the property from the given {@link Resource} if it exists and is convertible to the requested type. If not it tries to get
     * the property from the {@link Resource}'s {@link Style}.
     *
     * @param resource the {@link Resource}
     * @param property the property name
     * @param type     the class of the requested type
     * @param <T>      the type of the expected return value
     * @return the return value converted to the requested type, or null of not found in either of {@link Resource} properties or{@link Style}
     */
    public static <T> T getPropertyOrStyle(Resource resource, String property, Class<T> type) {
        ValueMap properties = resource.getValueMap();
        T value = properties.get(property, type);
        if (value == null) {
            Designer designer = resource.getResourceResolver().adaptTo(Designer.class);
            Style style = designer != null ? designer.getStyle(resource) : null;
            if (style != null) {
                value = style.get(property, type);
            }
        }
        return value;
    }

    /**
     * Wraps an image resource with the properties and child resources of the inherited featured image of either
     * the linked page or the page containing the resource.
     *
     * @param resource The image resource
     * @param linkManager The link manager
     * @param currentStyle The style of the image resource
     * @param currentPage The page containing the image resource
     * @return The wrapped image resource augmented with inherited properties and child resource if inheritance is enabled, the plain image resource otherwise.
     */
//    public static Resource getWrappedImageResourceWithInheritance(Resource resource, LinkManager linkManager, Style currentStyle, Page currentPage) {
//        if (resource == null) {
//            LOGGER.error("The resource is not defined");
//            return null;
//        }
//        if (linkManager == null) {
//            LOGGER.error("The link manager is not defined");
//            return null;
//        }
//
//        ValueMap properties = resource.getValueMap();
//        String fileReference = properties.get(DownloadResource.PN_REFERENCE, String.class);
//        Resource fileResource = resource.getChild(DownloadResource.NN_FILE);
//        boolean imageFromPageImage = properties.get(PN_IMAGE_FROM_PAGE_IMAGE, StringUtils.isEmpty(fileReference) && fileResource == null);
//        boolean altValueFromPageImage = properties.get(PN_ALT_VALUE_FROM_PAGE_IMAGE, imageFromPageImage);
//
//        if (imageFromPageImage) {
//            Resource inheritedResource = null;
//            String linkURL = properties.get(ImageResource.PN_LINK_URL, String.class);
//            boolean actionsEnabled = (currentStyle != null) ?
//                    !currentStyle.get(Teaser.PN_ACTIONS_DISABLED, !properties.get(Teaser.PN_ACTIONS_ENABLED, true)) :
//                    properties.get(Teaser.PN_ACTIONS_ENABLED, true);
//            Resource firstAction = Optional.of(resource).map(res -> res.getChild(Teaser.NN_ACTIONS)).map(actions -> actions.getChildren().iterator().next()).orElse(null);
//
//            if (StringUtils.isNotEmpty(linkURL)) {
//                // the inherited resource is the featured image of the linked page
//                Optional<Link> link = getOptionalLink(linkManager.get(resource).build());
//                inheritedResource = link
//                        .map(link1 -> (Page) link1.getReference())
//                        .map(ComponentUtils::getFeaturedImage)
//                        .orElse(null);
//            } else if (actionsEnabled && firstAction != null) {
//                // the inherited resource is the featured image of the first action's page (the resource is assumed to be a teaser)
//                inheritedResource = getOptionalLink(linkManager.get(firstAction).withLinkUrlPropertyName(Teaser.PN_ACTION_LINK).build())
//                        .map(link1 -> {
//                            if (getOptionalLink(link1).isPresent()) {
//                                Page linkedPage = (Page) link1.getReference();
//                                return Optional.ofNullable(linkedPage)
//                                        .map(ComponentUtils::getFeaturedImage)
//                                        .orElse(null);
//                            }
//                            return null;
//                        })
//                        .orElse(null);
//            } else {
//                // the inherited resource is the featured image of the current page
//                inheritedResource = Optional.ofNullable(currentPage)
//                        .map(page -> {
//                            Template template = page.getTemplate();
//                            // make sure the resource is part of the currentPage or of its template
//                            if (StringUtils.startsWith(resource.getPath(), currentPage.getPath())
//                                    || (template != null && StringUtils.startsWith(resource.getPath(), template.getPath()))) {
//                                return ComponentUtils.getFeaturedImage(currentPage);
//                            }
//                            return null;
//                        })
//                        .orElse(null);
//            }
//
//            Map<String, String> overriddenProperties = new HashMap<>();
//            Map<String, Resource> overriddenChildren = new HashMap<>();
//            String inheritedFileReference = null;
//            Resource inheritedFileResource = null;
//            String inheritedAlt = null;
//            String inheritedAltValueFromDAM = null;
//
//            if (inheritedResource != null) {
//                // Define the inherited properties
//                ValueMap inheritedProperties = inheritedResource.getValueMap();
//                inheritedFileReference = inheritedProperties.get(DownloadResource.PN_REFERENCE, String.class);
//                inheritedFileResource = inheritedResource.getChild(DownloadResource.NN_FILE);
//                inheritedAlt = inheritedProperties.get(ImageResource.PN_ALT, String.class);
//                inheritedAltValueFromDAM = inheritedProperties.get(PN_ALT_VALUE_FROM_DAM, String.class);
//            }
//            overriddenProperties.put(DownloadResource.PN_REFERENCE, inheritedFileReference);
//            overriddenChildren.put(DownloadResource.NN_FILE, inheritedFileResource);
//            // don't inherit the image title from the page image
//            overriddenProperties.put(PN_TITLE_VALUE_FROM_DAM, "false");
//            if (altValueFromPageImage) {
//                overriddenProperties.put(ImageResource.PN_ALT, inheritedAlt);
//                overriddenProperties.put(PN_ALT_VALUE_FROM_DAM, inheritedAltValueFromDAM);
//            } else {
//                overriddenProperties.put(PN_ALT_VALUE_FROM_DAM, "false");
//            }
//
//            return new CoreResourceWrapper(resource, resource.getResourceType(), null, overriddenProperties, overriddenChildren);
//
//        }
//        return resource;
//    }

    /**
     * Attempts to resolve the redirect chain starting from the given page, avoiding loops.
     *
     * @param page The starting {@link Page}
     * @return A pair of {@link Page} and {@link String} the redirect chain resolves to. The page can be the original page, if no redirect
     * target is defined or even {@code null} if the redirect chain does not resolve to a valid page, in this case one should use the right
     * part of the pair (the {@link String} redirect target).
     */
    @NotNull
    public static Pair<Page, String> resolveRedirects(@Nullable final Page page) {
        Page result = page;
        String redirectTarget = null;
        if (page != null && page.getPageManager() != null) {
            Set<String> redirectCandidates = new LinkedHashSet<>();
            redirectCandidates.add(page.getPath());
            while (result != null && StringUtils
                    .isNotEmpty((redirectTarget = result.getProperties().get(PageModelImpl.PN_REDIRECT_TARGET, String.class)))) {
                result = page.getPageManager().getPage(redirectTarget);
                if (result != null) {
                    if (!redirectCandidates.add(result.getPath())) {
                        LOGGER.warn("Detected redirect loop for the following pages: {}.", redirectCandidates);
                        break;
                    }
                }
            }
        }
        return new ImmutablePair<>(result, redirectTarget);
    }

    /**
     * Converts a link object into an Optional<Link> object.
     * This method is used to keep the logic based on the former internal link handler backwards compatible.
     *
     * @param link The {@link Link}
     * @return the Optional<Link> object
     */
    public static Optional<Link> getOptionalLink(Link link) {
        if (link == null) {
            return Optional.empty();
        }
        if (!link.isValid()) {
            return Optional.empty();
        }
        return Optional.of(link);
    }
    
    @NotNull
    public static String generateId(@NotNull final Resource resource,
                                    @Nullable final Page currentPage,
                                    @Nullable final String resourceCallerPath,
                                    @Nullable final ComponentContext componentContext) {
        String resourceType = resource.getResourceType();
        String prefix = StringUtils.substringAfterLast(resourceType, "/");
        String path = resource.getPath();
        if (currentPage != null && componentContext != null) {
            PageManager pageManager = currentPage.getPageManager();
            Page resourcePage = pageManager.getContainingPage(resource);
            Template template = currentPage.getTemplate();
            boolean inCurrentPage = (resourcePage != null && StringUtils.equals(resourcePage.getPath(), currentPage.getPath()));
            boolean inTemplate = (template != null && path.startsWith(template.getPath()));
            if (resourceCallerPath != null) {
                path = resourceCallerPath.concat(resource.getPath());
            } else if (!inCurrentPage && !inTemplate) {
                ComponentContext parentContext = componentContext.getParent();
                while (parentContext != null) {
                    Resource parentContextResource = parentContext.getResource();
                    if (parentContextResource != null) {
                        Page parentContextPage = pageManager.getContainingPage(parentContextResource);
                        inCurrentPage = (parentContextPage != null && StringUtils.equals(parentContextPage.getPath(), currentPage.getPath()));
                        inTemplate = (template != null && parentContextResource.getPath().startsWith(template.getPath()));
                        if (inCurrentPage || inTemplate) {
                            path = parentContextResource.getPath().concat(resource.getPath());
                            break;
                        }
                    }
                    parentContext = parentContext.getParent();
                }
            }

        }

        return Utils.generateId(prefix, path);
    }

    /**
     * Returns an ID based on the prefix, the ID_SEPARATOR and a hash of the path, e.g. image-5c7e0ef90d
     *
     * @param prefix the prefix for the ID
     * @param path   the resource path
     * @return the generated ID
     */
    @NotNull
    public static String generateId(@NotNull final String prefix, @NotNull final String path) {
        return StringUtils.join(prefix, ID_SEPARATOR, StringUtils.substring(DigestUtils.sha256Hex(path), 0, ID_HASH_LENGTH));
    }
    
    @Nullable
    public static Resource getFeaturedImage(@NotNull Page page) {
        return page.getContentResource(NN_PAGE_FEATURED_IMAGE);
    }
    

	@NotNull
	public static Object getPropertyFromResource(final ResourceResolver resolver, final String resourcePath,
			final String propertyName) {
		if (resolver != null) {

			final String contentPath = resourcePath + "/" + JcrConstants.JCR_CONTENT;
			final Resource content = resolver.getResource(contentPath);
			if (content != null) {
				final ValueMap valueMap = content.getValueMap();
				if (valueMap != null && valueMap.containsKey(propertyName)) {
					return valueMap.get(propertyName);
				}
			}
		}
		return null;
	}
	
	@NotNull
    public static String getBrandConfigPageValue(com.day.cq.wcm.api.Page startPage, String propertyName) {
        if (startPage == null) {
            return StringUtils.EMPTY;
        }
        
        com.day.cq.wcm.api.Page tmp = startPage;

		while (tmp != null && tmp.hasContent() && tmp.getDepth() > 1) {
			final ValueMap props = tmp.getProperties();
			if (props != null && StringUtils.isNotBlank(props.get(propertyName, StringUtils.EMPTY))) {
				return props.get(propertyName, StringUtils.EMPTY);
			} else {
				tmp = tmp.getParent();
			}

		}
        return StringUtils.EMPTY;
    }
}

