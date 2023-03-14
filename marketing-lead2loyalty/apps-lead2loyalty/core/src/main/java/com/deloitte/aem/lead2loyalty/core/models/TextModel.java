package com.deloitte.aem.lead2loyalty.core.models;

import java.util.List;

import com.deloitte.aem.lead2loyalty.core.util.ServiceUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.settings.SlingSettingsService;

@Model(adaptables = { SlingHttpServletRequest.class,
		Resource.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class TextModel {

	//primary Nav Links
	@ChildResource(name = "utilityLinks")
	public List<HeaderModel> utilityLinks;

	@ValueMapValue
	private String logoTitle;

	@ValueMapValue
	private String logoLink;

	@ValueMapValue
	private String signUpPageUrl;

	@SlingObject
	private ResourceResolver resourceResolver;

	@OSGiService
	private SlingSettingsService settingsService;

	public List<HeaderModel> getUtilityLinks() {
		return utilityLinks;
	}

	public String getLogoTitle() {
		return logoTitle;
	}

	public String getLogoLink() {
		return ServiceUtils.getLink(resourceResolver, logoLink, settingsService);
	}

	public String getSignUpPageUrl() {
		return ServiceUtils.getLink(resourceResolver, signUpPageUrl, settingsService);
	}
}
