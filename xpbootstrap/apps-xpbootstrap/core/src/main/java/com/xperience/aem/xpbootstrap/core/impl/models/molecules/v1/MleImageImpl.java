package com.xperience.aem.xpbootstrap.core.impl.models.molecules.v1;

import javax.annotation.PostConstruct;

import com.xperience.aem.xpbootstrap.core.component.internal.AbstractComponentImpl;
import com.xperience.aem.xpbootstrap.core.impl.models.AbstractCompImpl;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.xperience.aem.xpbootstrap.core.models.molecules.MleImage;

@Model(
	    adaptables = SlingHttpServletRequest.class,
	    adapters = {MleImage.class, ComponentExporter.class},
	    resourceType = MleImageImpl.RESOURCE_TYPE,
	    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
	)
	@Exporter(
	    name = ExporterConstants.SLING_MODEL_EXPORTER_NAME,
	    extensions = ExporterConstants.SLING_MODEL_EXTENSION
	)
public class MleImageImpl extends AbstractCompImpl implements MleImage {
	
	public static final String RESOURCE_TYPE = "xpbootstrap/components/master/molecules/mleImage/v1/mleImage";

	private final Logger LOG = LoggerFactory.getLogger(getClass());
	
	@Self
    private SlingHttpServletRequest request;
	

	@ValueMapValue
	private String atmIconSrcFileReference;
	
	@ValueMapValue
	private String atmIconAltText;
	
	
	@PostConstruct
    protected void initModel() {
        LOG.info(" --- MleImageImpl initialized ---");
    }

	@Override
	public String getExportedType() {
		return request.getResource().getResourceType();
	}

	@Override
	public String getAtmIconSrcFileReference() {
		return atmIconSrcFileReference;
	}

	@Override
	public String getAtmIconAltText() {
		return atmIconAltText;
	}

	
}
