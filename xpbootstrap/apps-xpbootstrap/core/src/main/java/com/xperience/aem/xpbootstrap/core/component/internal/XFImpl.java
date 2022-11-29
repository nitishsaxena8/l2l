package com.xperience.aem.xpbootstrap.core.component.internal;


import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ContainerExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.components.Component;
import com.day.cq.wcm.api.components.ComponentEditConfig;
import com.day.cq.wcm.api.components.VirtualComponent;
import com.day.cq.wcm.api.designer.ComponentStyle;
import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.xperience.aem.xpbootstrap.core.component.Utils.ContentFragmentUtils;
import com.xperience.aem.xpbootstrap.core.component.models.XF;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.factory.ModelFactory;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Experience Fragment model implementation.
 */
@Model(adaptables = SlingHttpServletRequest.class,
        adapters = {XF.class, ComponentExporter.class, ContainerExporter.class},
        resourceType = {XFImpl.RESOURCE_TYPE_V1, XFImpl.RESOURCE_TYPE_V2})
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class XFImpl implements XF, Component {

    /**
     * The experience fragment component resource type.
     */
    public static final String RESOURCE_TYPE_V1 = "core/wcm/components/experiencefragment/v1/experiencefragment";
    public static final String RESOURCE_TYPE_V2 = "core/wcm/components/experiencefragment/v2/experiencefragment";

    /**
     * Class name to be applied if the XF is empty or not configured.
     */
    private static final String CSS_EMPTY_CLASS = "empty";

    /**
     * Class name to be applied to all experience fragments.
     */
    private static final String CSS_BASE_CLASS = "aem-xf";
    /**
     * The current resource.
     */
    @SlingObject
    protected Resource resource;
    /**
     * The current request.
     */
    @Self
    private SlingHttpServletRequest request;
    @Self
    private XFDataImpl data;
    /**
     * The model factory service.
     */
    @OSGiService
    private ModelFactory modelFactory;
    /**
     * Name of the experience fragment.
     */
    private String name;

    /**
     * Class names of the responsive grid.
     */
    private String classNames;

    /**
     * Child columns of the responsive grid.
     */
    private LinkedHashMap<String, ComponentExporter> children;


    @Override
    @Nullable
    public String getLocalizedFragmentVariationPath() {
        return data.getLocalizedFragmentVariationPath();
    }

    @Override
    public String getPath() {
        return null;
    }

    @Override
    @JsonIgnore
    @Nullable
    public String getName() {
        if (this.name == null) {
            this.name = Optional.ofNullable(this.request.getResourceResolver().adaptTo(PageManager.class))
                    .flatMap(pm -> Optional.ofNullable(this.getLocalizedFragmentVariationPath())
                            .map(pm::getContainingPage))
                    .map(Page::getParent)
                    .map(Page::getName)
                    .orElse(null);
        }
        return this.name;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @NotNull
    @Override
    public String getExportedType() {
        return this.request.getResource().getResourceType();
    }

    @NotNull
    @Override
    public Map<String, ? extends ComponentExporter> getExportedItems() {
        return this.getChildren();
    }

    @NotNull
    @Override
    public String[] getExportedItemsOrder() {
        return this.getChildren().keySet().toArray(new String[0]);
    }

    @Override
    @JsonProperty("classNames")
    @NotNull
    public String getCssClassNames() {
        if (this.classNames == null) {
            this.classNames = Stream.of(
                    CSS_BASE_CLASS,
                    this.getExportedItems().isEmpty() ? CSS_EMPTY_CLASS : "",
                    this.request.getResource().getValueMap().get(ComponentStyle.PN_CSS_CLASS, "")
            ).filter(StringUtils::isNotBlank).collect(Collectors.joining(" "));
        }
        return classNames;
    }

    @Override
    @JsonInclude
    public boolean isConfigured() {
        return StringUtils.isNotEmpty(this.getLocalizedFragmentVariationPath()) && !this.getExportedItems().isEmpty();
    }

    /**
     * Gets an ordered map of all children resources of the experience fragment with the resource name as the key
     * and the corresponding {@link ComponentExporter} model as the value.
     *
     * @return Ordered map of resource names to {@link ComponentExporter} models.
     */
    private LinkedHashMap<String, ComponentExporter> getChildren() {
        if (this.children == null) {
            this.children = Optional.ofNullable(this.getLocalizedFragmentVariationPath())
                    .filter(StringUtils::isNotBlank)
                    .map(this.resource.getResourceResolver()::getResource)
                    .map(Resource::listChildren)
                    .map(it -> ContentFragmentUtils.getComponentExporters(it, this.modelFactory, this.request, this.resource))
                    .orElseGet(LinkedHashMap::new);
        }
        return this.children;
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
}
