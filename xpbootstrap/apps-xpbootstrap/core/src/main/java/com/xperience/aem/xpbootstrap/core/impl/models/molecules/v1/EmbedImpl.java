package com.xperience.aem.xpbootstrap.core.impl.models.molecules.v1;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.day.cq.wcm.api.designer.Style;
import com.xperience.aem.xpbootstrap.core.component.Utils.UrlProcessor;
import com.xperience.aem.xpbootstrap.core.component.internal.AbstractComponentImpl;
import com.xperience.aem.xpbootstrap.core.models.molecules.Embed;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import se.eris.notnull.NotNull;
import se.eris.notnull.Nullable;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

@Model(
        adaptables = SlingHttpServletRequest.class,
        adapters = { Embed.class, ComponentExporter.class },
        resourceType = {EmbedImpl.RESOURCE_TYPE_V1, EmbedImpl.RESOURCE_TYPE_V2}
)
@Exporter(
        name = ExporterConstants.SLING_MODEL_EXPORTER_NAME,
        extensions = ExporterConstants.SLING_MODEL_EXTENSION
)
public class EmbedImpl extends AbstractComponentImpl implements Embed {

    protected static final String RESOURCE_TYPE_V1 = "core/wcm/components/embed/v1/embed";
    protected static final String RESOURCE_TYPE_V2 = "core/wcm/components/embed/v2/embed";

    @ValueMapValue(name = PN_TYPE, injectionStrategy = InjectionStrategy.OPTIONAL)
    @Nullable
    private String type;

    @ValueMapValue(name = PN_URL, injectionStrategy = InjectionStrategy.OPTIONAL)
    @Nullable
    private String url;

    @ValueMapValue(name = PN_HTML, injectionStrategy = InjectionStrategy.OPTIONAL)
    @Nullable
    private String html;

    @ValueMapValue(name = PN_EMBEDDABLE_RESOURCE_TYPE, injectionStrategy = InjectionStrategy.OPTIONAL)
    @Nullable
    private String embeddableResourceType;

    @ScriptVariable(injectionStrategy = InjectionStrategy.OPTIONAL)
    @Nullable
    private Style currentStyle;

    @Inject @Optional
    private List<UrlProcessor> urlProcessors;

    @Inject
    private Resource resource;

    private Type embedType;
    private UrlProcessor.Result result;

    @PostConstruct
    void initModel() {
        embedType = Type.fromString(type);
        if (embedType == null || embedType != Type.URL) {
            url = null;
        }
        if (embedType == null || embedType != Type.HTML) {
            html = null;
        }
        if (embedType == null || embedType != Type.EMBEDDABLE) {
            embeddableResourceType = null;
        }
        if (currentStyle != null) {
            boolean urlDisabled = currentStyle.get(PN_DESIGN_URL_DISABLED, false);
            boolean htmlDisabled = currentStyle.get(PN_DESIGN_HTML_DISABLED, false);
            boolean embeddablesDisabled = currentStyle.get(PN_DESIGN_EMBEDDABLES_DISABLED, false);
            if (urlDisabled) {
                url = null;
            }
            if (htmlDisabled) {
                html = null;
            }
            if (embeddablesDisabled) {
                embeddableResourceType = null;
            }
        }
        if (StringUtils.isNotEmpty(url) && urlProcessors != null) {
            for (UrlProcessor urlProcessor : urlProcessors) {
                UrlProcessor.Result result = urlProcessor.process(url);
                if (result != null) {
                    this.result = result;
                    break;
                }
            }
        }
    }

    @Nullable
    @Override
    public Type getType() {
        return embedType;
    }

    @Nullable
    @Override
    public String getUrl() {
        return url;
    }

    @Nullable
    @Override
    public UrlProcessor.Result getResult() {
        return result;
    }

    @Nullable
    @Override
    public String getHtml() {
        return html;
    }

    @Nullable
    @Override
    public String getEmbeddableResourceType() {
        return embeddableResourceType;
    }

    @NotNull
    @Override
    public String getExportedType() {
        return resource.getResourceType();
    }
}
