package com.deloitte.aem.lead2loyalty.core.models;

import com.day.cq.commons.jcr.JcrConstants;
import com.deloitte.aem.lead2loyalty.core.beans.ProductsBean;
import com.deloitte.aem.lead2loyalty.core.util.ServiceUtils;
import com.deloitte.aem.lead2loyalty.core.util.WebUtils;
import org.apache.commons.collections4.CollectionUtils;
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

import javax.annotation.PostConstruct;
import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Value;
import javax.servlet.http.Cookie;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Model(adaptables = { SlingHttpServletRequest.class,
		Resource.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class BookmarksModel {

	@SlingObject
	private ResourceResolver resourceResolver;

	@OSGiService
	private SlingSettingsService settingsService;

	@Self
	private SlingHttpServletRequest request;

	String name;

	private List<ProductsBean> bookmarks;

	@PostConstruct
	protected void init() {
		try {
			bookmarks = new ArrayList<>();

			String emailId = WebUtils.getSpecificCookie(request, "userEmail");
			String userPath = "/var/lead2loyalty-users/" + emailId;
			Node userNode = Objects.requireNonNull(resourceResolver.getResource(userPath)).adaptTo(Node.class);
			if (userNode != null && userNode.hasProperty("bookmarks")) {
				name = userNode.getProperty("FirstName").getString();
				Property favoritesProperty = userNode.getProperty("bookmarks");
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
					bookmarks.add(productsBean);
				}
			}
		} catch (RepositoryException e) {
			// Handle exception
		}
	}

	public List<ProductsBean> getBookmarks() {
		if (CollectionUtils.isNotEmpty(bookmarks)) {
			return bookmarks;
		}
		return new ArrayList<>();
	}

	public String getName() {
		return name;
	}

}