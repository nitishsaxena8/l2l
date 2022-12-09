package com.deloitte.aem.lead2loyalty.core.models;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;

import java.util.List;

import javax.inject.Named;

@Model(adaptables = { SlingHttpServletRequest.class,
		Resource.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class HeaderModel {

	public String getLinkTitle() {
		return linkTitle;
	}

	public String getLinkUrl() {
		return linkUrl;
	}

	String getOpenInNewWindow() {
		return openInNewWindow;
	}

	String getRowLabel() {
		return rowLabel;
	}

	@ValueMapValue
	@Named("tr_linkTitle")
	private String linkTitle;

	@ValueMapValue
	private String linkUrl;

	@ValueMapValue
	private String openInNewWindow;

	@ValueMapValue
	private String rowLabel;

	@ChildResource(name = "secondaryLinks")
	public List<SecondaryListModel > secondaryLinks;

	public List<SecondaryListModel > getSecondaryLinks() {
		return secondaryLinks;
	}

}
