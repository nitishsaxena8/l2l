package com.deloitte.aem.lead2loyalty.core.models;

import com.deloitte.aem.lead2loyalty.core.util.ServiceUtils;

import org.apache.commons.lang3.StringUtils;
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

	private String productCategory = StringUtils.EMPTY;

	public String getAtmCtaLabel() {
		return atmCtaLabel;
	}

	public void setAtmCtaLabel(String atmCtaLabel) {
		this.atmCtaLabel = atmCtaLabel;
	}

	public String getAtmCtaLink() {
		String link = ServiceUtils.getLink(resourceResolver, atmCtaLink, settingsService);

		if (StringUtils.isNotEmpty(link) && StringUtils.isNotEmpty(productCategory)) {
			if (link.contains("?")) {
				return link.concat("&" + "category=" + productCategory);
			} else {
				return link.concat("?" + "category=" + productCategory);
			}
		}

		return link;
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

	public void setProductCategory(String productCategory) {
		this.productCategory = productCategory;
	}
}
