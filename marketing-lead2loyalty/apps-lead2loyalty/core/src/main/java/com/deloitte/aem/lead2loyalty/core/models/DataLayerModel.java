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
import javax.jcr.Node;
import javax.jcr.RepositoryException;

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

	@ValueMapValue
	private String image;

	private String action;

	/** The current page. */
	@Inject
	private Page currentPage;

	@OSGiService
	private SlingSettingsService settingsService;

	private String productID;

	private String parentPageTitle;

	private String headerFragmentPath;

	private String footerFragmentPath;

	@SlingObject
	private ResourceResolver resourceResolver;

	/**
	 * Gets invoked after completion of all injections.
	 *
	 */
	@PostConstruct
	protected void init() throws RepositoryException {
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

		productID = pageProperties.get("productID", String.class);
		parentPageTitle = currentPage.getParent().getTitle();
		pagePath = currentPage.getPath();

		JSONObject productJson = new JSONObject();

		try {
			productJson.put("productId","");
			productJson.put("productDescription","");
			productJson.put("productTitle","");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		headerFragmentPath = resourceResolver.getResource(currentPage.getAbsoluteParent(3).getPath() +"/home-page/jcr:content").adaptTo(Node.class).getProperty("headerFragmentPath").getValue().getString();
		footerFragmentPath = resourceResolver.getResource(currentPage.getAbsoluteParent(3).getPath() +"/home-page/jcr:content").adaptTo(Node.class).getProperty("footerFragmentPath").getValue().getString();

		LOGGER.debug("Exiting DataLayerModel");
	}

	/**
	 * @return current page name.
	 */
	public String getPageName() {
		return pageName;
	}

	public String getPagePath() {
		return ServiceUtils.getExternalizeContentLink(resourceResolver, pagePath);
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

	public String getImage() {
		return ServiceUtils.getExternalizeAssetLink(resourceResolver, image);
	}

	public String getHeaderFragmentPath() {
		return headerFragmentPath;
	}

	public String getFooterFragmentPath() {
		return footerFragmentPath;
	}
}