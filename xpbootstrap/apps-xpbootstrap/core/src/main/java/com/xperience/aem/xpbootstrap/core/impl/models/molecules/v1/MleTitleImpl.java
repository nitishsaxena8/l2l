package com.xperience.aem.xpbootstrap.core.impl.models.molecules.v1;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.xperience.aem.xpbootstrap.core.component.internal.AbstractComponentImpl;
import com.xperience.aem.xpbootstrap.core.impl.models.AbstractCompImpl;
import com.xperience.aem.xpbootstrap.core.models.molecules.MleTitle;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;

@Model(
	    adaptables = SlingHttpServletRequest.class,
	    adapters = {MleTitle.class, ComponentExporter.class},
	    resourceType = MleTitleImpl.RESOURCE_TYPE,
	    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
	)
	@Exporter(
	    name = ExporterConstants.SLING_MODEL_EXPORTER_NAME,
	    extensions = ExporterConstants.SLING_MODEL_EXTENSION
	)
public class MleTitleImpl extends AbstractCompImpl implements MleTitle {
	
	public static final String RESOURCE_TYPE = "xpbootstrap/components/master/molecules/mleTitle/v1/mleTitle";

	private final Logger LOG = LoggerFactory.getLogger(getClass());
	
	@Self
    private SlingHttpServletRequest request;
	
	@ValueMapValue
	private String atmTitleText;

	@ValueMapValue
	private String atmTitleLevel;
		
	@PostConstruct
    protected void initModel() {
        LOG.info(" --- MleTitleImpl initialized ---");
    }

	@Override
	public String getExportedType() {
		return request.getResource().getResourceType();
	}

	@Override
	public String getAtmTitleText() {
		return atmTitleText;
	}

	@Override
	public String getAtmTitleLevel() {
		return atmTitleLevel;
	}

	
}
