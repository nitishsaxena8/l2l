package com.deloitte.aem.lead2loyalty.core.models;

import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageFilter;
import com.day.cq.wcm.api.PageManager;
import com.deloitte.aem.lead2loyalty.core.beans.ProductsBean;
import com.deloitte.aem.lead2loyalty.core.util.ServiceUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.settings.SlingSettingsService;

import javax.annotation.PostConstruct;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Model(adaptables = { SlingHttpServletRequest.class,
		Resource.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ArticleListingModel {

	@SlingObject
	private ResourceResolver resourceResolver;

	@OSGiService
	private SlingSettingsService settingsService;

	@ValueMapValue
	private String articleTitle;

	@ValueMapValue
	private String rootPagePath;

	private List<ProductsBean> articleList;

	@PostConstruct
	protected void init() {

		PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
		Page parentPage = Objects.requireNonNull(pageManager).getPage(rootPagePath);
		if (parentPage != null) {
			Iterator<Page> rootPageIterator = parentPage.listChildren(new PageFilter(), false);
			articleList = new ArrayList<>();

			while (rootPageIterator.hasNext()) {
				Page childPage = rootPageIterator.next();
				ValueMap pageProperties = childPage.getProperties();
				ProductsBean productsBean = new ProductsBean();

				productsBean.setTitle(pageProperties.get(JcrConstants.JCR_TITLE, String.class) != null
						? pageProperties.get(JcrConstants.JCR_TITLE, String.class)
						: StringUtils.EMPTY);
				productsBean.setPath(ServiceUtils.getLink(resourceResolver,
						childPage.getPath(), settingsService));
				try {
					Node articleNode = childPage.adaptTo(Node.class);
					Node heroBannerNode = Objects.requireNonNull(articleNode).getNode("jcr:content/root/container/container/hero_banner");
					String fileReference = Objects.requireNonNull(heroBannerNode).getProperty("fileReference").getString();
					productsBean.setImage(fileReference != null ? fileReference : StringUtils.EMPTY);
				} catch (RepositoryException e) {
					throw new RuntimeException(e);
				}

				articleList.add(productsBean);
			}
		}
	}

	public List<ProductsBean> getArticleList() {
		if (CollectionUtils.isNotEmpty(articleList)) {
			return articleList;
		}
		return new ArrayList<>();
	}

	public String getArticleTitle() {
		return articleTitle;
	}

	public void setArticleTitle(String articleTitle) {
		this.articleTitle = articleTitle;
	}
}
