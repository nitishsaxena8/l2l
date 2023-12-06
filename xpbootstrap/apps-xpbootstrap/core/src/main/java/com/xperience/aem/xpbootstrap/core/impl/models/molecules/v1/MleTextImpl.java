package com.xperience.aem.xpbootstrap.core.impl.models.molecules.v1;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.xperience.aem.xpbootstrap.core.component.internal.AbstractComponentImpl;
import com.xperience.aem.xpbootstrap.core.impl.models.AbstractCompImpl;
import com.xperience.aem.xpbootstrap.core.models.molecules.MleText;
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
	    adapters = {MleText.class, ComponentExporter.class},
	    resourceType = MleTextImpl.RESOURCE_TYPE,
	    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
	)
	@Exporter(
	    name = ExporterConstants.SLING_MODEL_EXPORTER_NAME,
	    extensions = ExporterConstants.SLING_MODEL_EXTENSION
	)
public class MleTextImpl extends AbstractCompImpl implements MleText {
	
	public static final String RESOURCE_TYPE = "xpbootstrap/components/master/molecules/mleText/v1/mleText";

	private final Logger LOG = LoggerFactory.getLogger(getClass());
	
	@Self
    private SlingHttpServletRequest request;
	
	@ValueMapValue
	private String atmTextHtml;

		
	@PostConstruct
	protected void initModel() {
        LOG.info(" --- MleTextImpl initialized ---");
    }

	@Override
	public String getExportedType() {
		return request.getResource().getResourceType();
	}

	@Override
	public String getAtmTextHtml() {
		return atmTextHtml;
	}

	
}
