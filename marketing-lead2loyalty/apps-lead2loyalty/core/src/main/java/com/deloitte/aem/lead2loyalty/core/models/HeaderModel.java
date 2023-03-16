package com.deloitte.aem.lead2loyalty.core.models;

import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageFilter;
import com.day.cq.wcm.api.PageManager;
import com.deloitte.aem.lead2loyalty.core.beans.TextAndUrlBean;
import com.deloitte.aem.lead2loyalty.core.util.ServiceUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.settings.SlingSettingsService;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import javax.annotation.PostConstruct;
import javax.inject.Named;

@Model(adaptables = { SlingHttpServletRequest.class,
		Resource.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class HeaderModel {

	//secondary Nav Links
	private List<NavPrimaryListModel> secondaryHeaderLinks;

	@ChildResource(name = "secondaryFooterLinks")
	public List<NavSecondaryListModel> secondaryFooterLinks;

	@SlingObject
	private ResourceResolver resourceResolver;

	@OSGiService
	private SlingSettingsService settingsService;

	@ValueMapValue
	@Named("tr_linkTitle")
	private String linkTitle;

	@ValueMapValue
	private String linkUrl;

	@ValueMapValue
	private String openInNewWindow;

	@ValueMapValue
	private String showChildPages;

	@PostConstruct
	protected void init() {
		if(null!=showChildPages && showChildPages.equals("true"))
			assignHeaderNav();
		else
			showChildPages="false";
	}

	public void assignHeaderNav(){

		PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
		Page parentPage = Objects.requireNonNull(pageManager).getPage(linkUrl);
		if (parentPage != null) {

			//to set secondary links
			Iterator<Page> rootPageIterator = parentPage.listChildren(new PageFilter(), false);
			secondaryHeaderLinks = new ArrayList<>();
			while (rootPageIterator.hasNext()) {
				Page childPage = rootPageIterator.next();
				ValueMap childPageProperties = childPage.getProperties();
				if ("true".equals(childPageProperties.get("showInNav", String.class)) )	{
					NavPrimaryListModel childPageInfo = new NavPrimaryListModel();
					childPageInfo.setText(childPageProperties.get(JcrConstants.JCR_TITLE, ""));
					childPageInfo.setUrl(ServiceUtils.getLink(resourceResolver, childPage.getPath(), settingsService) + "#" + childPage.getName());
					String categoryParam = settingsService.getRunModes().contains("author") ? "&category="+childPage.getName() : "?category="+childPage.getName();
					childPageInfo.setSecondLevelMoreLink(ServiceUtils.getLink(resourceResolver, parentPage.getPath(), settingsService) + categoryParam);

					//to set tertiary links
					Iterator<Page> childPageIterator = childPage.listChildren(new PageFilter(), false);
					List<TextAndUrlBean> tertiaryLinks=new ArrayList<>();
					while (childPageIterator.hasNext()) {
						Page grandchildPage = childPageIterator.next();
						ValueMap grandchildPageProperties = grandchildPage.getProperties();
						if ("true".equals(grandchildPageProperties.get("showInNav", String.class)) )	{
							TextAndUrlBean grandchildPageInfo = new TextAndUrlBean();
							grandchildPageInfo.setText(grandchildPageProperties.get(JcrConstants.JCR_TITLE, ""));
							grandchildPageInfo.setUrl(ServiceUtils.getLink(resourceResolver, grandchildPage.getPath(), settingsService));

							tertiaryLinks.add(grandchildPageInfo);
						}
					}

					childPageInfo.setTertiaryLinks(tertiaryLinks);
					secondaryHeaderLinks.add(childPageInfo);
				}
			}
		}
	}


	public String getLinkTitle() {
		return linkTitle;
	}

	public String getLinkUrl() {
		return ServiceUtils.getLink(resourceResolver, linkUrl, settingsService);
	}

	String getOpenInNewWindow() {
		return openInNewWindow;
	}

	public String getShowChildPages() {
		return showChildPages;
	}

	public List<NavSecondaryListModel> getSecondaryFooterLinks() {
		return secondaryFooterLinks;
	}

	public List<NavPrimaryListModel> getSecondaryHeaderLinks() {
		return secondaryHeaderLinks;
	}

}
