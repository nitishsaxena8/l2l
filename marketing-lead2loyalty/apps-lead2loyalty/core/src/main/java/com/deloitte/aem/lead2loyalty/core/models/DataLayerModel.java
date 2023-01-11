package com.deloitte.aem.lead2loyalty.core.models;

import com.day.cq.wcm.api.Page;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.settings.SlingSettingsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 * Model Mapping the Current Page Resource
 *
 * @author imrankhan5
 *
 */
@Model(adaptables = { Resource.class,
		SlingHttpServletRequest.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class DataLayerModel {

	private static final Logger LOGGER = LoggerFactory.getLogger(DataLayerModel.class);

	private String pageName;

	@Self
	private SlingHttpServletRequest request;

	@ValueMapValue(name = "jcr:title")
	private String pageTitle;

	private String pagePath;

	/** The current page. */
	@Inject
	private Page currentPage;

	@OSGiService
	private SlingSettingsService settingsService;

	/**
	 * Gets invoked after completion of all injections.
	 *
	 */
	@PostConstruct
	protected void init() {
		LOGGER.debug("Entering DataLayerModel");

		for (int i = 0 ; i < currentPage.getDepth() - 2 ; i++) {
			if (i == 0) {
				this.pageName = currentPage.getAbsoluteParent(i + 2).getName();
			} else {
				this.pageName = pageName + ":" + currentPage.getAbsoluteParent(i + 2).getName();
			}
		}

		pagePath = currentPage.getPath();

		LOGGER.debug("Exiting DataLayerModel");
	}

	/**
	 * @return current page name.
	 */
	public String getPageName() {
		return pageName;
	}

	public String getPagePath() {
		return pagePath;
	}

	public String getPageTitle() {
		return pageTitle;
	}

}