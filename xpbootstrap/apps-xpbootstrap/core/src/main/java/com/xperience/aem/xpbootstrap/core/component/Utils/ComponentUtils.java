package com.xperience.aem.xpbootstrap.core.component.Utils;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.Template;
import com.day.cq.wcm.api.components.ComponentContext;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import se.eris.notnull.NotNull;
import se.eris.notnull.Nullable;

import java.util.Optional;


/**
 * Utility helper functions for components.
 */
public final class ComponentUtils {

    /**
     * Name of the node holding the context aware configurations below /conf/{site-name};
     */
    public static final String NN_SLING_CONFIGS = "sling:configs";

    /**
     * Name of the node holding the properties of the featured image of the page.
     *
     * @since com.adobe.cq.wcm.core.components.models 12.21.0
     */
   private static String NN_PAGE_FEATURED_IMAGE = "cq:featuredimage";
    /**
     * Name of the separator character used between prefix and hash when generating an ID, e.g. image-5c7e0ef90d
     */
    public static final String ID_SEPARATOR = "-";

    /**
     * Length of the ID hash.
     */
    private static final int ID_HASH_LENGTH = 10;

    /**
     * Private constructor to prevent instantiation of utility class.
     */
    private ComponentUtils() {
        // NOOP
    }



    /**
     * See {@link #getId(Resource, Page, String, ComponentContext)}.
     *
     * @param resource The resource for which to get or generate an ID.
     * @param currentPage The current request page.
     * @param componentContext The current component context.
     * @return The ID property value for the specified resource, or a generated ID if not set.
     */
    @NotNull
    public static String getId(@NotNull final Resource resource,
                               @Nullable final Page currentPage,
                               @Nullable final ComponentContext componentContext) {
        return getId(resource, currentPage, null, componentContext);
    }

    /**
     * Get the ID property value if set (using {@link #getPropertyId(Resource)},
     * otherwise generate a new ID (using {@link #generateId(Resource, Page, String, ComponentContext)}.
     *
     * @param resource The resource for which to get or generate an ID.
     * @param currentPage The current request page.
     * @param resourceCallerPath The path of the page or template resource that references this component.
     * @param componentContext The current component context.
     * @return The ID property value for the specified resource, or a generated ID if not set.
     */
    @NotNull
    public static String getId(@NotNull final Resource resource,
                               @Nullable final Page currentPage,
                               @Nullable final String resourceCallerPath,
                               @Nullable final ComponentContext componentContext) {
        return ComponentUtils.getPropertyId(resource)
                .orElseGet(() -> ComponentUtils.generateId(resource, currentPage, resourceCallerPath, componentContext));
    }

    /**
     * Get the ID value from the &quot; &quot; property.
     *
     * @param resource The resource for which to get the ID property value.
     * @return The ID property value if set, empty optional if not.
     */
    private static Optional<String> getPropertyId(@NotNull final Resource resource) {
        return Optional.ofNullable(resource.getValueMap().get("id", String.class))
                .filter(StringUtils::isNotEmpty)
                .map(StringUtils::trim)
                .map(StringUtils::normalizeSpace)
                .map(val -> StringUtils.replace(val, " ", ID_SEPARATOR));
    }

    /**
     * Returns an auto generated component ID.
     *
     * The ID is the first {@value ComponentUtils#ID_HASH_LENGTH} characters of an SHA-1 hexadecimal hash of the component path,
     * prefixed with the component name. Example: title-810f3af321
     *
     * If the component is referenced, the path is taken to be a concatenation of the component path,
     * with the path of the first parent context resource that exists on the page or in the template.
     * This ensures the ID is unique if the same component is referenced multiple times on the same page or template.
     *
     * Collision
     * ---------
     * c = expected collisions
     * c ~= (i^2)/(2m) - where i is the number of items and m is the number of possibilities for each item.
     * m = 16^n - for a hexadecimal string, where n is the number of characters.
     *
     * For i = 1000 components with the same name, and n = 10 characters:
     *
     * c ~= (1000^2)/(2*(16^10))
     * c ~= 0.00000045
     *
     * @return the auto generated component ID
     */
    @NotNull
    private static String generateId(@NotNull final Resource resource,
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

        return ComponentUtils.generateId(prefix, path);
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

    /**
     * Returns the resource holding the properties of the featured image of the page.
     *
     * @param page the page
     * @return the featured image resource
     */
    @Nullable
    public static Resource getFeaturedImage(@NotNull Page page) {
        return page.getContentResource(NN_PAGE_FEATURED_IMAGE);
    }
}
