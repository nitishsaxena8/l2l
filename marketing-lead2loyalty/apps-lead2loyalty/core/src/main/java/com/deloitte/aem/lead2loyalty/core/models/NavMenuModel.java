package com.deloitte.aem.lead2loyalty.core.models;

import com.day.cq.commons.jcr.JcrConstants;
import com.deloitte.aem.lead2loyalty.core.util.ServiceUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceUtil;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.settings.SlingSettingsService;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Model(adaptables = { SlingHttpServletRequest.class,
		Resource.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class NavMenuModel {


	@OSGiService
	private SlingSettingsService settingsService;

	@SlingObject
	private ResourceResolver resourceResolver;

	private List<NavPrimaryListModel> firstLevelLinks;


	@PostConstruct
	protected void init() {

		Resource parentResource = resourceResolver.getResource("/content/lead2loyalty/language-masters/en/home-page/products");
		if (parentResource != null) {

			firstLevelLinks = new ArrayList<>();

			//to get category page resource
			Iterator<Resource> childResources = parentResource.listChildren();
			while (childResources.hasNext()) {
				Resource categoryResource = childResources.next();

				if (ResourceUtil.isNonExistingResource(categoryResource)) {
					continue;
				}
				else if ("cq:PageContent".equals(categoryResource.getValueMap().get(JcrConstants.JCR_PRIMARYTYPE, ""))) {
					continue;
				}

				ValueMap categoryValueMap = Objects.requireNonNull(categoryResource.getChild(JcrConstants.JCR_CONTENT)).getValueMap();
				if ("cq:PageContent".equals(categoryValueMap.get(JcrConstants.JCR_PRIMARYTYPE, "")) && "true".equals(categoryValueMap.get("showInNav", String.class)) )	{

					NavPrimaryListModel categoryPageInfo = new NavPrimaryListModel();
					categoryPageInfo.setText(categoryValueMap.get(JcrConstants.JCR_TITLE, ""));
					categoryPageInfo.setUrl(ServiceUtils.getLink(resourceResolver, categoryResource.getPath(), settingsService));

					//to set product page links
					List<NavSecondaryListModel> secondaryLinks=new ArrayList<>();
					Iterator<Resource> childResources2 = categoryResource.listChildren();
					while (childResources2.hasNext()) {
						Resource productResource = childResources2.next();

						if (ResourceUtil.isNonExistingResource(productResource)) {
							continue;
						}
						else if ("cq:PageContent".equals(productResource.getValueMap().get(JcrConstants.JCR_PRIMARYTYPE, ""))) {
							continue;
						}

						ValueMap productValueMap = Objects.requireNonNull(productResource.getChild(JcrConstants.JCR_CONTENT)).getValueMap();
						if ("cq:PageContent".equals(productValueMap.get(JcrConstants.JCR_PRIMARYTYPE, "")) && "true".equals(productValueMap.get("showInNav", String.class)) )	{

							NavSecondaryListModel productPageInfo = new NavSecondaryListModel();
							productPageInfo.setSecondarylinkTitle(productValueMap.get(JcrConstants.JCR_TITLE, ""));
							productPageInfo.setSecondarylinkUrl(ServiceUtils.getLink(resourceResolver, productResource.getPath(), settingsService));

							secondaryLinks.add(productPageInfo);
						}
					}

					categoryPageInfo.setSecondaryLinks(secondaryLinks);
					firstLevelLinks.add(categoryPageInfo);
				}
			}
		}

	}

	public List<NavPrimaryListModel> getFirstLevelLinks() {
		return firstLevelLinks;
	}

}
