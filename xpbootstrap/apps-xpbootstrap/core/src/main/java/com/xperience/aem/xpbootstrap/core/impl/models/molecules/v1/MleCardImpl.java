package com.xperience.aem.xpbootstrap.core.impl.models.molecules.v1;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import com.xperience.aem.xpbootstrap.core.component.internal.AbstractComponentImpl;
import com.xperience.aem.xpbootstrap.core.impl.models.AbstractCompImpl;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.xperience.aem.xpbootstrap.core.models.molecules.MleCard;

@Model(
	    adaptables = SlingHttpServletRequest.class,
	    adapters = {MleCard.class, ComponentExporter.class},
	    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
	)
	@Exporter(
	    name = ExporterConstants.SLING_MODEL_EXPORTER_NAME,
	    extensions = ExporterConstants.SLING_MODEL_EXTENSION
	)
public class MleCardImpl extends AbstractCompImpl implements MleCard {
	
	private final Logger LOG = LoggerFactory.getLogger(getClass());
	
	@Self
    private SlingHttpServletRequest request;
	
	@ValueMapValue
	private String atmIconSrcFileReference;

	@ValueMapValue
	private String atmIconAltText;
	
	@ValueMapValue
	private String atmCardTitle;
	
	@ValueMapValue
	private String atmCardSubtitle;
	
	@ValueMapValue
	private String atmCardDescription;
	
	@Inject
	@Via("resource")
    private Resource atmCardPrimaryCta;
	
	@Inject
	@Via("resource")
    private Resource atmCardSecondaryCta;
		
	@PostConstruct
    public void initModel() {
        LOG.info(" --- MleCardImpl initialized ---");

    }

	@Override
	public String getExportedType() {

		return request.getResource().getResourceType();
	}

	@Override
	public String getAtmCardTitle() {
		return atmCardTitle;
	}

	@Override
	public String getAtmCardSubtitle() {
		return atmCardSubtitle;
	}

	@Override
	public String getAtmCardDescription() {
		return atmCardDescription;
	}

	@Override
	public Resource getAtmCardPrimaryCta() {
		return atmCardPrimaryCta;
	}

	@Override
	public Resource getAtmCardSecondaryCta() {
		return atmCardSecondaryCta;
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
