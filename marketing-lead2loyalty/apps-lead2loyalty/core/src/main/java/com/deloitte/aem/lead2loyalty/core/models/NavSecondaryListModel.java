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

import javax.inject.Named;


@Model(adaptables = { SlingHttpServletRequest.class,
		Resource.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class NavSecondaryListModel {
	
	@ValueMapValue
	@Named("tr_secondarylinkTitle")
	private String secondarylinkTitle;

	@ValueMapValue
	private String secondarylinkUrl;

	@ValueMapValue
	private String openInNewWindowsecondary;

	@SlingObject
	private ResourceResolver resourceResolver;

	@OSGiService
	private SlingSettingsService settingsService;

	public String getSecondarylinkTitle() {
		return secondarylinkTitle;
	}

	public void setSecondarylinkTitle(String secondarylinkTitle) {
		this.secondarylinkTitle = secondarylinkTitle;
	}

	public String getSecondarylinkUrl() {
		return ServiceUtils.getLink(resourceResolver, secondarylinkUrl, settingsService);
	}

	public String getSecondarylinkUrlPath() {
		return secondarylinkUrl;
	}

	public void setSecondarylinkUrl(String secondarylinkUrl) {
		this.secondarylinkUrl = secondarylinkUrl;
	}

	public String getOpenInNewWindowsecondary() {
		return openInNewWindowsecondary;
	}

	public void setOpenInNewWindowsecondary(String openInNewWindowsecondary) {
		this.openInNewWindowsecondary = openInNewWindowsecondary;
	}

	

}
