package com.deloitte.aem.lead2loyalty.core.models;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import javax.inject.Named;


@Model(adaptables = { SlingHttpServletRequest.class,
		Resource.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class SecondaryListModel {
	
	@ValueMapValue
	@Named("tr_secondarylinkTitle")
	private String secondarylinkTitle;

	@ValueMapValue
	private String secondarylinkUrl;

	@ValueMapValue
	private String openInNewWindowsecondary;

	public String getSecondarylinkTitle() {
		return secondarylinkTitle;
	}

	public void setSecondarylinkTitle(String secondarylinkTitle) {
		this.secondarylinkTitle = secondarylinkTitle;
	}

	public String getSecondarylinkUrl() {
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
