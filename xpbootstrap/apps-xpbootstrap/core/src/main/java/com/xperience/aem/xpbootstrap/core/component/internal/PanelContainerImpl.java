package com.xperience.aem.xpbootstrap.core.component.internal;

import com.adobe.cq.export.json.ComponentExporter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.xperience.aem.xpbootstrap.core.component.Utils.Container;
import com.xperience.aem.xpbootstrap.core.component.Utils.LinkManager;
import com.xperience.aem.xpbootstrap.core.component.models.ListItem;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.eris.notnull.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Abstract panel container model.
 */
public abstract class PanelContainerImpl extends AbstractContainerImpl implements Container {

	private static final Logger logger = LoggerFactory.getLogger(PanelContainerImpl.class);
    
    /**
     * The resource type.
     */
    public static final String RESOURCE_TYPE = "xpbootstrap/components/master/organisms/panelcontainer/v1/panelcontainer";


    @Self
    LinkManager linkManager;


    @Override
    @NotNull
    protected List<? extends ListItem> readItems() {
    	logger.info("reading items via PanelContainerImpl");
        return getChildren().stream()
                .map(res -> new PanelContainerItemImpl(linkManager, res, getId(), component, getCurrentPage()))
                .collect(Collectors.toList());
    }


    @Override
    protected Map<String, ComponentExporter> getItemModels(@NotNull final SlingHttpServletRequest request,
                                                           @NotNull final Class<ComponentExporter> modelClass) {
        Map<String, ComponentExporter> models = super.getItemModels(request, modelClass);
        models.entrySet().forEach(entry ->
                getItems().stream()
                        .filter(Objects::nonNull)
                        .filter(item -> StringUtils.isNotEmpty(item.getName()) && StringUtils.equals(item.getName(), entry.getKey()))
                        .findFirst()
                        .ifPresent(match -> entry.setValue(new JsonWrapper(entry.getValue(), match)))
        );
        return models;
    }

    /**
     * Wrapper class used to add specific properties of the container items to the JSON serialization of the underlying container item model
     */
    static class JsonWrapper implements ComponentExporter {

        /**
         * The wrapped ComponentExporter.
         */
        @NotNull
        private final ComponentExporter inner;

        /**
         * The panel title.
         */
        private final String panelTitle;

        /**
         * Construct the wrapper.
         *
         * @param inner The ComponentExporter to be wrapped.
         * @param item  The panel item.
         */
        JsonWrapper(@NotNull final ComponentExporter inner, @NotNull final ListItem item) {
            this.inner = inner;
            this.panelTitle = item.getTitle();
        }

        /**
         * Get the underlying ComponentExporter that is wrapped by this wrapper.
         *
         * @return the underlying container item model
         */
        @JsonUnwrapped
        @NotNull
        public ComponentExporter getInner() {
            return this.inner;
        }

        /**
         * Get the panel title.
         *
         * @return the container item title
         */
        @JsonProperty(PanelContainerItemImpl.PN_PANEL_TITLE)
        @JsonInclude()
        public String getPanelTitle() {
            return this.panelTitle;
        }

        @NotNull
        @Override
        @JsonIgnore
        public String getExportedType() {
            return this.inner.getExportedType();
        }
    }
}
