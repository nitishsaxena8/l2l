package com.deloitte.aem.lead2loyalty.core.models;

import com.day.cq.wcm.api.Page;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 * Model Mapping the Current Page Resource
 * 
 * @author pandu
 *
 */
@Model(adaptables = { Resource.class,
		SlingHttpServletRequest.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class DataLayerModel {  

	private static final Logger LOGGER = LoggerFactory.getLogger(DataLayerModel.class);

	/**
	 *  template type
	 */
	private String templateType;

	private String pageName;

	@ValueMapValue(name = "jcr:title")
	private String pageTitle;

	/**
	 *  Current page type
	 */
	@ValueMapValue
	private String pageType;

	/**
	 *  type
	 */
	@ValueMapValue
	private String type;

	/**
	 * current page content type
	 */
	@ValueMapValue
	private String contentType;

	/**
	 *  Current page channel
	 */
	@ValueMapValue
	private String channel;

	/**
	 *   Current page category.
	 */
	@ValueMapValue
	private String category;

	/**
	 *   Current page category1.
	 */
	@ValueMapValue
	private String category1;

	/**
	 *   Current page category2.
	 */
	@ValueMapValue
	private String category2;

	/** The current page. */
	@Inject
	private Page currentPage;

	/**
	 * Gets invoked after completion of all injections.
	 * 
	 */
	@PostConstruct
	protected void init() {
		LOGGER.debug("Entering DataLayerModel");

		for (int i = 0 ; i < currentPage.getDepth() - 1 ; i++) {
			if (i == 0) {
				//pageName = currentPage.getAbsoluteParent(i + 2).getName();
			} else {
				//this.pageName = pageName + ":" + currentPage.getAbsoluteParent(i + 2).getName();
			}
		}

		if(null != currentPage && null != currentPage.getTemplate()) {
			templateType = currentPage.getTemplate().getName();
		} else {
			templateType = StringUtils.EMPTY;
		}
		LOGGER.info("Current Page Belongs to {} template type",getTemplateType());
		LOGGER.debug("Exiting DataLayerModel");
	}

	/**
	 * @return current page template type.
	 */
	public String getTemplateType() {
		return templateType;
	}

	/**
	 * @return current page name.
	 */
	public String getPageName() {
		return pageName; 
	}

	/**
	 * @return current page type.
	 */
	public String getPageType() {
		return pageType;
	}

	/**
	 * @return type of the page.
	 */
	public String getType() {
		return type;
	}

	/**
	 * @return current page content type.
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * @return current page channel.
	 */
	public String getChannel() {
		return channel;
	}

	/**
	 * @return current page category.
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * @return current page category 1.
	 */
	public String getCategory1() {
		return category1;
	}

	/**
	 * @return current page category 2.
	 */
	public String getCategory2() {
		return category2;
	}

	public String getPageTitle() {
		return pageTitle;
	}
}