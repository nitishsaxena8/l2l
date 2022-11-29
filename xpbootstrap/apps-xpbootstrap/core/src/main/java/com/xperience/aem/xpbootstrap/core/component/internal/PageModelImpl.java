package com.xperience.aem.xpbootstrap.core.component.internal;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ContainerExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.adobe.cq.export.json.SlingModelFilter;
import com.day.cq.tagging.Tag;
import com.day.cq.wcm.api.NameConstants;
import com.day.cq.wcm.api.Template;
import com.day.cq.wcm.api.components.Component;
import com.day.cq.wcm.api.components.ComponentEditConfig;
import com.day.cq.wcm.api.components.VirtualComponent;
import com.day.cq.wcm.api.designer.Design;
import com.day.cq.wcm.api.designer.Designer;
import com.day.cq.wcm.api.designer.Style;
import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xperience.aem.xpbootstrap.core.component.Utils.Utils;
import com.xperience.aem.xpbootstrap.core.component.models.PageModel;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.factory.ModelFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.*;

@Model(adaptables = SlingHttpServletRequest.class, adapters = {PageModel.class,
        ContainerExporter.class}, resourceType = PageModelImpl.RESOURCE_TYPE)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class PageModelImpl implements PageModel, Component { 

    public static final String PN_REDIRECT_TARGET = "cq:redirectTarget";
    protected static final String RESOURCE_TYPE = "/apps/xpbootstrap/components/master/organisms/page/v2/page";
    protected static final String DEFAULT_TEMPLATE_EDITOR_CLIENTLIB = "wcm.foundation.components.parsys.allowedcomponents";
    protected static final String PN_CLIENTLIBS = "clientlibs";
    protected static final String PN_BRANDSLUG = "brandSlug";
    protected static final String BARND_CONFIG_PAGE_PATH = "brandConfigPagePath";
    protected static final String HEADER_XF_PATH = "headerXfPath";
    protected static final String FOOTER_XF_PATH = "footerXfPath";
    protected static final String BRAND_SPECIFIC_CLIENT_LIBS = "brandClientlibs";
    protected static final String BRAND_ID = "brandId";
    /**
     * Style property name to load custom Javascript libraries asynchronously.
     */
    protected static final String PN_CLIENTLIBS_ASYNC = "clientlibsAsync";
    @ScriptVariable
    protected com.day.cq.wcm.api.Page currentPage;
    @ScriptVariable
    protected ValueMap pageProperties;
    @ScriptVariable
    @JsonIgnore
    protected Design currentDesign;
    @ScriptVariable(injectionStrategy = InjectionStrategy.OPTIONAL)
    @JsonIgnore
    @Nullable
    protected Style currentStyle;
    @ScriptVariable
    @JsonIgnore
    protected ResourceResolver resolver;
    protected String[] keywords = new String[0];
    protected String designPath;
    protected String staticDesignPath;
    protected String title;
    protected String description;
    protected String brandSlug;
    protected String[] clientLibCategories = new String[0];
    protected Calendar lastModifiedDate;
    protected String templateName;
    protected String brandConfigPagePath;
    @JsonIgnore
    protected Map<String, String> favicons = new HashMap<>();
    @Self
    SlingHttpServletRequest request;
    @Inject
    private ModelFactory modelFactory;
    @Inject
    private SlingModelFilter slingModelFilter;
    private Map<String, ComponentExporter> childModels = null;
    private String resourceType;
    private Set<String> resourceTypes;
    
    

    @PostConstruct
    protected void initModel() {
        title = currentPage.getTitle();
        description = currentPage.getDescription();
        if (StringUtils.isBlank(title)) {
            title = currentPage.getName();
        }
        Tag[] tags = currentPage.getTags();
        keywords = new String[tags.length];
        int index = 0;
        for (Tag tag : tags) {
            keywords[index++] = tag.getTitle(currentPage.getLanguage(false));
        }
        if (currentDesign != null) {
            String designPath = currentDesign.getPath();
            if (!Designer.DEFAULT_DESIGN_PATH.equals(designPath)) {
                this.designPath = designPath;
                if (resolver.getResource(designPath + "/static.css") != null) {
                    staticDesignPath = designPath + "/static.css";
                }
                loadFavicons(designPath);
            }
        }
        templateName = extractTemplateName();
        brandSlug = Utils.getInheritedValue(currentPage, PN_BRANDSLUG);
        brandConfigPagePath = Utils.getBrandConfigPageValue(currentPage, BARND_CONFIG_PAGE_PATH);
        populateClientlibCategories();
    }

    protected String extractTemplateName() {
        String templateName = null;
        String templatePath = pageProperties.get(NameConstants.PN_TEMPLATE, String.class);
        if (StringUtils.isNotEmpty(templatePath)) {
            int i = templatePath.lastIndexOf('/');
            if (i > 0) {
                templateName = templatePath.substring(i + 1);
            }
        }
        return templateName;
    }

    @Override
    public String getLanguage() {
        return currentPage == null ? Locale.getDefault().toLanguageTag()
                : currentPage.getLanguage(false).toLanguageTag();
    }

    @Override
    public Calendar getLastModifiedDate() {
        if (lastModifiedDate == null) {
            lastModifiedDate = pageProperties.get(NameConstants.PN_PAGE_LAST_MOD, Calendar.class);
        }
        return lastModifiedDate;
    }

    @Override
    @JsonIgnore
    public String[] getKeywords() {
        return Arrays.copyOf(keywords, keywords.length);
    }

    @Override
    public String getDesignPath() {
        return designPath;
    }

    @Override
    public String getStaticDesignPath() {
        return staticDesignPath;
    }

    @Override
    @JsonIgnore
    @Deprecated
    public Map<String, String> getFavicons() {
        return favicons;
    }

    @Override
    public String getPath() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getBrandSlug() {
        return brandSlug;
    }

    @Override
    public String getTemplateName() {
        return templateName;
    }

    @Override
    @JsonIgnore
    public String[] getClientLibCategories() {
        return Arrays.copyOf(clientLibCategories, clientLibCategories.length);
    }

    @Override
    @JsonIgnore
    @SuppressWarnings("squid:S2384")
    public Set<String> getComponentsResourceTypes() {
        if (resourceTypes == null) {
            resourceTypes = Utils.getPageResourceTypes(currentPage, request, modelFactory);
        }
        return resourceTypes;
    }

    @NotNull
    @Override
    public Map<String, ? extends ComponentExporter> getExportedItems() {
        if (childModels == null) {
            childModels = getChildModels(request, ComponentExporter.class);
        }

        return childModels;
    }

    @NotNull
    @Override
    public String[] getExportedItemsOrder() {
        Map<String, ? extends ComponentExporter> models = getExportedItems();

        if (models.isEmpty()) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }

        return models.keySet().toArray(ArrayUtils.EMPTY_STRING_ARRAY);

    }

    @NotNull
    @Override
    public String getExportedType() {
        if (StringUtils.isEmpty(resourceType)) {
            resourceType = pageProperties.get(ResourceResolver.PROPERTY_RESOURCE_TYPE, String.class);
            if (StringUtils.isEmpty(resourceType)) {
                resourceType = currentPage.getContentResource().getResourceType();
            }
        }
        return resourceType;
    }

    /**
     * Returns a map (resource name => Sling Model class) of the given resource
     * children's Sling Models that can be adapted to {@link T}.
     *
     * @param slingRequest the current request
     * @param modelClass   the Sling Model class to be adapted to
     * @return a map (resource name => Sling Model class) of the given resource
     * children's Sling Models that can be adapted to {@link T}
     */
    @NotNull
    private <T> Map<String, T> getChildModels(@NotNull SlingHttpServletRequest slingRequest,
                                              @NotNull Class<T> modelClass) {
        Map<String, T> itemWrappers = new LinkedHashMap<>();

        for (final Resource child : slingModelFilter.filterChildResources(request.getResource().getChildren())) {
            itemWrappers.put(child.getName(), modelFactory.getModelFromWrappedRequest(slingRequest, child, modelClass));
        }

        return itemWrappers;
    }

    protected void loadFavicons(String designPath) {
        favicons.put(PN_FAVICON_ICO, getFaviconPath(designPath, FN_FAVICON_ICO));
        favicons.put(PN_FAVICON_PNG, getFaviconPath(designPath, FN_FAVICON_PNG));
        favicons.put(PN_TOUCH_ICON_120, getFaviconPath(designPath, FN_TOUCH_ICON_120));
        favicons.put(PN_TOUCH_ICON_152, getFaviconPath(designPath, FN_TOUCH_ICON_152));
        favicons.put(PN_TOUCH_ICON_60, getFaviconPath(designPath, FN_TOUCH_ICON_60));
        favicons.put(PN_TOUCH_ICON_76, getFaviconPath(designPath, FN_TOUCH_ICON_76));
    }

    protected String getFaviconPath(String designPath, String faviconName) {
        String path = designPath + "/" + faviconName;
        if (resolver.getResource(path) == null) {
            return null;
        }
        return path;
    }

    protected void populateClientlibCategories() {
        List<String> categories = new ArrayList<>();
        Template template = currentPage.getTemplate();
        if (template != null && template.hasStructureSupport()) {
            Resource templateResource = template.adaptTo(Resource.class);
            if (templateResource != null) {
                addDefaultTemplateEditorClientLib(templateResource, categories);
                addPolicyClientLibs(categories);
            }
        }
        addBrandSpecificClientLibs(categories);
        clientLibCategories = categories.toArray(new String[0]);
    }

	protected void addDefaultTemplateEditorClientLib(Resource templateResource, List<String> categories) {
        if (currentPage.getPath().startsWith(templateResource.getPath())) {
            categories.add(DEFAULT_TEMPLATE_EDITOR_CLIENTLIB);
        }
    }

    protected void addPolicyClientLibs(List<String> categories) {
        if (currentStyle != null) {
            Collections.addAll(categories, currentStyle.get(PN_CLIENTLIBS, ArrayUtils.EMPTY_STRING_ARRAY));
        }
    }

    @Override
    public boolean isAccessible() {
        return false;
    }

    @Override
    public String getCellName() {
        return null;
    }

    @Override
    public boolean isEditable() {
        return false;
    }

    @Override
    public boolean isDesignable() {
        return false;
    }

    @Override
    public boolean isContainer() {
        return false;
    }

    @Override
    public boolean isAnalyzable() {
        return false;
    }

    @Override
    public boolean noDecoration() {
        return false;
    }

    @Override
    public String getDialogPath() {
        return null;
    }

    @Override
    public String getDesignDialogPath() {
        return null;
    }

    @Override
    public String getIconPath() {
        return null;
    }

    @Override
    public String getThumbnailPath() {
        return null;
    }

    @Override
    public String getComponentGroup() {
        return null;
    }

    @Override
    public ValueMap getProperties() {
        return null;
    }

    @Override
    public ComponentEditConfig getDeclaredEditConfig() {
        return null;
    }

    @Override
    public ComponentEditConfig getDeclaredChildEditConfig() {
        return null;
    }

    @Override
    public ComponentEditConfig getEditConfig() {
        return null;
    }

    @Override
    public ComponentEditConfig getChildEditConfig() {
        return null;
    }

    @Override
    public ComponentEditConfig getDesignEditConfig(String s) {
        return null;
    }

    @Override
    public Map<String, String> getHtmlTagAttributes() {
        return null;
    }

    @Override
    public Component getSuperComponent() {
        return null;
    }

    @Override
    public String getResourceType() {
        return null;
    }

    @Override
    public Resource getLocalResource(String s) {
        return null;
    }

    @Override
    public Collection<VirtualComponent> getVirtualComponents() {
        return null;
    }

    @Override
    public String getDefaultView() {
        return null;
    }

    @Override
    public String getTemplatePath() {
        return null;
    }

    @Override
    public String[] getInfoProviders() {
        return new String[0];
    }

    @Override
    public <AdapterType> AdapterType adaptTo(Class<AdapterType> aClass) {
        return null;
    }

    @Override
    @JsonIgnore
    public boolean isClientlibsAsync() {
        if (currentStyle != null) {
            return currentStyle.get(PN_CLIENTLIBS_ASYNC, false);
        }
        return false;
    }

    @Override
    public String getHeaderXfPath() {
		return Utils.getPropertyFromResource(resolver, brandConfigPagePath, HEADER_XF_PATH).toString();
    	
    }
    
    @Override
    public String getFooterXfPath() {
		return Utils.getPropertyFromResource(resolver, brandConfigPagePath, FOOTER_XF_PATH).toString();
    }
    
    @Override
    public String getBrandId() {
    	return Utils.getPropertyFromResource(resolver, brandConfigPagePath, BRAND_ID).toString();
    }
    
	private void addBrandSpecificClientLibs(final List<String> categories) {
		final String[] brandClientLibs = (String[]) Utils.getPropertyFromResource(resolver, brandConfigPagePath,
				BRAND_SPECIFIC_CLIENT_LIBS);
		if (brandClientLibs != null && brandClientLibs.length > 0) {
			categories.addAll(Arrays.asList(brandClientLibs));
		}
	}

}