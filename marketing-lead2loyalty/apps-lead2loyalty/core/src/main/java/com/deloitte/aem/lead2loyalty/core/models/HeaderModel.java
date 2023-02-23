package com.deloitte.aem.lead2loyalty.core.models;

import com.deloitte.aem.lead2loyalty.core.util.ServiceUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.settings.SlingSettingsService;

import java.util.List;

import javax.inject.Named;

@Model(adaptables = { SlingHttpServletRequest.class,
		Resource.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class HeaderModel {

	@SlingObject
	private ResourceResolver resourceResolver;

	@OSGiService
	private SlingSettingsService settingsService;

	@ValueMapValue
	@Named("tr_linkTitle")
	private String linkTitle;

	@ValueMapValue
	private String linkUrl;

	@ValueMapValue
	private String openInNewWindow;

	@ValueMapValue
	private String signUpPageUrl;

	@ValueMapValue
	private String rowLabel;

	@ChildResource(name = "secondaryLinks")
	public List<NavSecondaryListModel> secondaryLinks;

	public String getLinkTitle() {
		return linkTitle;
	}

	public String getLinkUrl() {
		return ServiceUtils.getLink(resourceResolver, linkUrl, settingsService);
	}

	String getOpenInNewWindow() {
		return openInNewWindow;
	}

	String getRowLabel() {
		return rowLabel;
	}

	public List<NavSecondaryListModel> getSecondaryLinks() {
		return secondaryLinks;
	}

	public String getSignUpPageUrl() {
		return ServiceUtils.getLink(resourceResolver, signUpPageUrl, settingsService);
	}
}
