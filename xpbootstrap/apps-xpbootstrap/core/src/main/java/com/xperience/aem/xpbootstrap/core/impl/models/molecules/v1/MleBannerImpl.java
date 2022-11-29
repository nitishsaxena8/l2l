package com.xperience.aem.xpbootstrap.core.impl.models.molecules.v1;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.xperience.aem.xpbootstrap.core.component.internal.AbstractComponentImpl;
import com.xperience.aem.xpbootstrap.core.impl.models.AbstractCompImpl;
import com.xperience.aem.xpbootstrap.core.models.molecules.MleBanner;
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
	    adapters = {MleBanner.class, ComponentExporter.class},
	    resourceType = MleBannerImpl.RESOURCE_TYPE,
	    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
	)
	@Exporter(
	    name = ExporterConstants.SLING_MODEL_EXPORTER_NAME,
	    extensions = ExporterConstants.SLING_MODEL_EXTENSION
	)
public class MleBannerImpl extends AbstractCompImpl implements MleBanner {
	
	public static final String RESOURCE_TYPE = "xpbootstrap/components/master/molecules/mleBanner/v1/mleBanner";

	private final Logger LOG = LoggerFactory.getLogger(getClass());
	
	@Self
    private SlingHttpServletRequest request;
	
	@ValueMapValue
	private boolean animation;
	
	@ValueMapValue
	private String gradientType;
	
	@ValueMapValue
	private String atmPictureDesktopImgFileReference;
	
	@ValueMapValue
	private String atmPictureAltText;
	
	@ValueMapValue
	private String atmPictureMobileImgFileReference;
	
	@ValueMapValue
	private String gradientPosition;
	
	@ValueMapValue
	private String atmTitleText;
	
	@ValueMapValue
	private String atmTitleLevel;
	
	@ValueMapValue
	private String atmTextHtml;
	
	@ValueMapValue
	private String atmCtaLabel;
	
	@ValueMapValue
	private String atmCtaLink;
	
	@ValueMapValue
	private String atmCtaTarget;
	
	@ValueMapValue
	private String atmCtaType;
	
	@ValueMapValue
	private String atmCtaSize;
	
	@ValueMapValue
	private String atmCtaContext;
	
	@ValueMapValue
	private String atmCtaRole;
	
	public boolean isAnimation() {
		return animation;
	}


	@Override
	public String getGradientType() {
		return gradientType;
	}


	@Override
	public String getAtmPictureDesktopImgFileReference() {
		return atmPictureDesktopImgFileReference;
	}


	@Override
	public String getAtmPictureAltText() {
		return atmPictureAltText;
	}


	@Override
	public String getAtmPictureMobileImgFileReference() {
		return atmPictureMobileImgFileReference;
	}


	@Override
	public String getGradientPosition() {
		return gradientPosition;
	}


	@Override
	public String getAtmTitleText() {
		return atmTitleText;
	}


	@Override
	public String getAtmTitleLevel() {
		return atmTitleLevel;
	}


	@Override
	public String getAtmTextHtml() {
		return atmTextHtml;
	}


	@Override
	public String getAtmCtaLabel() {
		return atmCtaLabel;
	}


	@Override
	public String getAtmCtaLink() {
		return atmCtaLink;
	}


	@Override
	public String getAtmCtaTarget() {
		return atmCtaTarget;
	}


	@Override
	public String getAtmCtaType() {
		return atmCtaType;
	}


	@Override
	public String getAtmCtaSize() {
		return atmCtaSize;
	}


	@Override
	public String getAtmCtaContext() {
		return atmCtaContext;
	}


	@Override
	public String getAtmCtaRole() {
		return atmCtaRole;
	}


	@PostConstruct
    private void initModel() {
        LOG.info(" --- MleBannerImpl initialized ---");
    }

	@Override
	public String getExportedType() {
		return request.getResource().getResourceType();
	}
}
