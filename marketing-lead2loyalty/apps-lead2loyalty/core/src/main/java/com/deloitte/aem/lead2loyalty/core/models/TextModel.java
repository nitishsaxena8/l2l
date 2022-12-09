package com.deloitte.aem.lead2loyalty.core.models;

import java.util.List;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;

@Model(adaptables = { SlingHttpServletRequest.class,
		Resource.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class TextModel {
	
	@ChildResource(name = "utilityLinks")
	public List<HeaderModel> utilityLinks;

	public List<HeaderModel> getUtilityLinks() {
		return utilityLinks;
	}

}
