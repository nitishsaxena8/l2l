package com.xperience.aem.xpbootstrap.core.impl.models.molecules.v1;

import com.adobe.cq.export.json.ExporterConstants;
import com.xperience.aem.xpbootstrap.core.models.molecules.MleSocialMediaLinks;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

@Model( 
    adaptables = {Resource.class},
    adapters = {MleSocialMediaLinks.class},
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
    )
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME,
        extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class MleSocialMediaLinksImpl implements MleSocialMediaLinks{
    private final Logger LOG = LoggerFactory.getLogger(MleSocialMediaLinksImpl.class);

    @Inject
    private List<MleSocialMediaLinksModelImpl> links;

    @Override
    @SuppressWarnings("squid:S2384")
    public List<MleSocialMediaLinksModelImpl> getLinks() {
        return links;
    }



}