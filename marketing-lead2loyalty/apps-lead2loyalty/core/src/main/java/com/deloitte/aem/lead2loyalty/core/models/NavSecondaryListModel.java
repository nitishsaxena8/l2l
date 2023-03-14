package com.deloitte.aem.lead2loyalty.core.models;
import com.deloitte.aem.lead2loyalty.core.util.ServiceUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.settings.SlingSettingsService;


@Model(adaptables = { SlingHttpServletRequest.class,
		Resource.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class NavSecondaryListModel {
	
	@ValueMapValue
	private String secondaryLinkTitle;

	@ValueMapValue
	private String secondaryLinkUrl;

	@ValueMapValue
	private String openInNewWindowSecondary;

	@SlingObject
	private ResourceResolver resourceResolver;

	@OSGiService
	private SlingSettingsService settingsService;

	public String getSecondaryLinkTitle() {
		return secondaryLinkTitle;
	}

	public void setSecondaryLinkTitle(String secondaryLinkTitle) {
		this.secondaryLinkTitle = secondaryLinkTitle;
	}

	public String getSecondaryLinkUrl() {
		return ServiceUtils.getLink(resourceResolver, secondaryLinkUrl, settingsService);
	}

	public String getSecondarylinkUrlPath() {
		return secondaryLinkUrl;
	}

	public void setSecondaryLinkUrl(String secondaryLinkUrl) {
		this.secondaryLinkUrl = secondaryLinkUrl;
	}

	public String getOpenInNewWindowSecondary() {
		return openInNewWindowSecondary;
	}

	public void setOpenInNewWindowSecondary(String openInNewWindowSecondary) {
		this.openInNewWindowSecondary = openInNewWindowSecondary;
	}

	

}
