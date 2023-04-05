package com.deloitte.aem.lead2loyalty.core.models;

import com.day.cq.commons.jcr.JcrConstants;
import com.deloitte.aem.lead2loyalty.core.beans.ProductsBean;
import com.deloitte.aem.lead2loyalty.core.beans.SearchFilterBean;
import com.deloitte.aem.lead2loyalty.core.constants.ApplicationConstants;
import com.deloitte.aem.lead2loyalty.core.util.ServiceUtils;
import com.deloitte.aem.lead2loyalty.core.util.WebUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceUtil;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.settings.SlingSettingsService;

import javax.annotation.PostConstruct;
import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Value;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Model(adaptables = { SlingHttpServletRequest.class,
		Resource.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class BookmarksModel {

	@SlingObject
	private ResourceResolver resourceResolver;

	@OSGiService
	private SlingSettingsService settingsService;

	@Self
	private SlingHttpServletRequest request;

	private String name;

	private List<ProductsBean> bookmarks;

	private List<SearchFilterBean> filterBeanList;

	@PostConstruct
	protected void init() {
		try {
			bookmarks = new ArrayList<ProductsBean>();

			String emailId = WebUtils.getSpecificCookie(request, ApplicationConstants.USER_EMAIL_COOKIE);
			String userPath = ApplicationConstants.LOYALTY_USER_PATH + emailId;
			Resource resource = resourceResolver.getResource(userPath);

			if (!ResourceUtil.isNonExistingResource(resource) && resource.adaptTo(Node.class).hasProperty(ApplicationConstants.BOOKMARKS_PROPERTY)) {
				Node userNode = resource.adaptTo(Node.class);
				if(userNode.hasProperty(ApplicationConstants.BOOKMARKS_PROPERTY)) {
					name = userNode.getProperty("FirstName").getString();
					Property favoritesProperty = userNode.getProperty(ApplicationConstants.BOOKMARKS_PROPERTY);
					Value[] favoriteValues = favoritesProperty.getValues();
					for (Value favoriteValue : favoriteValues) {
						Resource pageResource = resourceResolver.getResource(favoriteValue.getString());
						Resource jcrResource = Objects.requireNonNull(pageResource).getChild(JcrConstants.JCR_CONTENT);
						ValueMap jcrProperties = Objects.requireNonNull(jcrResource).getValueMap();
						ProductsBean productsBean = new ProductsBean();
						productsBean.setPath(ServiceUtils.getLink(resourceResolver,
								favoriteValue.getString(), settingsService));
						productsBean.setTitle(jcrProperties.get(JcrConstants.JCR_TITLE, String.class) != null
								? jcrProperties.get(JcrConstants.JCR_TITLE, String.class)
								: StringUtils.EMPTY);
						Resource imageResource = jcrResource.getChild("image");
						ValueMap imageProperties = imageResource != null ? imageResource.getValueMap() : null;
						productsBean.setImage(imageProperties != null ? imageProperties.get("fileReference", String.class)
								: StringUtils.EMPTY);
						productsBean.setPageType(jcrProperties.get(ApplicationConstants.PAGE_TYPE_PROPERTY, String.class) != null
								? jcrProperties.get(ApplicationConstants.PAGE_TYPE_PROPERTY, String.class)
								: StringUtils.EMPTY);
						bookmarks.add(productsBean);
					}
					setFilterBeanList(getFilterList(bookmarks));
				}
			}
		} catch (RepositoryException e) {
			// Handle exception
		}
	}

	private List<SearchFilterBean> getFilterList(List<ProductsBean> productsBeanList) {
		List<SearchFilterBean> filterBeanList = new ArrayList<>();
		List<String> distinctFilterList = productsBeanList.stream()
				.map(ProductsBean::getPageType)
				.distinct()
				.collect(Collectors.toList());

		for(String filter : distinctFilterList) {
			long filterCount = productsBeanList.stream()
					.filter(c -> filter.equals(c.getPageType()))
					.count();
			filterBeanList.add(new SearchFilterBean(filter, filterCount));
		}
		return  filterBeanList;
	}

	public List<ProductsBean> getBookmarks() {
		if (CollectionUtils.isNotEmpty(bookmarks)) {
			return bookmarks;
		}
		return new ArrayList<ProductsBean>();
	}

	public String getName() {
		return name;
	}

	public List<SearchFilterBean> getFilterBeanList() {
		return filterBeanList;
	}

	public void setFilterBeanList(List<SearchFilterBean> filterBeanList) {
		this.filterBeanList = filterBeanList;
	}
}