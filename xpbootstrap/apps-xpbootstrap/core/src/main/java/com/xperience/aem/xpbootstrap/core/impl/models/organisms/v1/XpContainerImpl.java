package com.xperience.aem.xpbootstrap.core.impl.models.organisms.v1;

import javax.annotation.PostConstruct;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.xperience.aem.xpbootstrap.core.models.organisms.XpContainer;

@Model(
	    adaptables = SlingHttpServletRequest.class,
	    adapters = {XpContainer.class, ComponentExporter.class},
	    resourceType = XpContainerImpl.RESOURCE_TYPE
	)
	@Exporter(
	    name = ExporterConstants.SLING_MODEL_EXPORTER_NAME,
	    extensions = ExporterConstants.SLING_MODEL_EXTENSION
	)
public class XpContainerImpl implements XpContainer{

	public static final String RESOURCE_TYPE = "xpbootstrap/components/master/organisms/xp-container/v1/xp-container";

	private final Logger LOG = LoggerFactory.getLogger(getClass());
	
	@Self
    private SlingHttpServletRequest request;
	
	@Override
	public String getExportedType() {
		return null;
	}
	
	@PostConstruct
    private void initModel() {
        LOG.info(" --- XpContainerImpl initialized ---");
    }

}
