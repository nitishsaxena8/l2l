package com.deloitte.aem.lead2loyalty.core.models;

import com.day.cq.wcm.api.Page;
import com.deloitte.aem.lead2loyalty.core.util.ServiceUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.settings.SlingSettingsService;
import org.json.JSONException;
import org.json.JSONObject;
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

	private String action;

	/** The current page. */
	@Inject
	private Page currentPage;

	@OSGiService
	private SlingSettingsService settingsService;

	private String productID;

	private String parentPageTitle;

	@SlingObject
	private ResourceResolver resourceResolver;

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

		ValueMap pageProperties = currentPage.getProperties();

		String templatePath = pageProperties.get("cq:template", String.class);
		if (StringUtils.isNotEmpty(templatePath)) {
			String[] path = templatePath.split("/");
			action = path[path.length - 1].replaceAll("-", " ");
		}

		//String title = pageProperties.get(JcrConstants.JCR_TITLE, String.class);
		productID = pageProperties.get("productID", String.class);
		parentPageTitle = currentPage.getParent().getTitle();
		pagePath = ServiceUtils.getLink(resourceResolver, currentPage.getPath(), settingsService);

		JSONObject productJson = new JSONObject();

		try {
			productJson.put("productId","");
			productJson.put("productDescription","");
			productJson.put("productTitle","");
		} catch (JSONException e) {
			e.printStackTrace();
		}



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

	public String getAction() {
		return action;
	}

	public String getProductID() {
		return productID;
	}

	public String getParentPageTitle() {
		return parentPageTitle;
	}
}