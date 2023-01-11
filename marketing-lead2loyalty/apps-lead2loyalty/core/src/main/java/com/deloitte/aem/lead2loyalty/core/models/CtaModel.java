package com.deloitte.aem.lead2loyalty.core.models;

import com.deloitte.aem.lead2loyalty.core.util.ServiceUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.settings.SlingSettingsService;


@Model(adaptables = { SlingHttpServletRequest.class,
		Resource.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class CtaModel {

	@ValueMapValue
	private String atmCtaLabel;

	@ValueMapValue
	private String atmCtaLink;

	@ValueMapValue
	private String atmCtaTarget;

	@ValueMapValue
	private String atmCtaType;

	@SlingObject
	private ResourceResolver resourceResolver;

	@OSGiService
	private SlingSettingsService settingsService;

	public String getAtmCtaLabel() {
		return atmCtaLabel;
	}

	public void setAtmCtaLabel(String atmCtaLabel) {
		this.atmCtaLabel = atmCtaLabel;
	}

	public String getAtmCtaLink() {
		return ServiceUtils.getLink(resourceResolver, atmCtaLink, settingsService);
	}

	public void setAtmCtaLink(String atmCtaLink) {
		this.atmCtaLink = atmCtaLink;
	}

	public String getAtmCtaTarget() {
		return atmCtaTarget;
	}

	public void setAtmCtaTarget(String atmCtaTarget) {
		this.atmCtaTarget = atmCtaTarget;
	}

	public String getAtmCtaType() {
		return atmCtaType;
	}

	public void setAtmCtaType(String atmCtaType) {
		this.atmCtaType = atmCtaType;
	}
}
