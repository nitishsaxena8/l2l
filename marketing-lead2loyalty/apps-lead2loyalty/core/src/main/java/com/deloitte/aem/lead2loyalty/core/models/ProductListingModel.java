package com.deloitte.aem.lead2loyalty.core.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

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

import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagConstants;
import com.day.cq.tagging.TagManager;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageFilter;
import com.day.cq.wcm.api.PageManager;
import com.deloitte.aem.lead2loyalty.core.beans.ProductsBean;
import org.apache.sling.settings.SlingSettingsService;

@Model(adaptables = { SlingHttpServletRequest.class,
		Resource.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ProductListingModel {

	@SlingObject
	private ResourceResolver resourceResolver;

	@ValueMapValue
	private String rootPagePath;

	private List<ProductsBean> productList;
	private List<ProductsBean> accordionCategoryList;

	private Map<String, List<ProductsBean>> accordionMap;
	private Map<String, String> pageNameMap;

	@OSGiService
	private SlingSettingsService settingsService;

	@PostConstruct
	protected void init() {

		PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
		Page parentPage = pageManager.getPage(rootPagePath);
		TagManager tagManager = resourceResolver.adaptTo(TagManager.class);

		if (parentPage != null) {
			int rootPageDepth = parentPage.getDepth();
			Iterator<Page> rootPageIterator = parentPage.listChildren(new PageFilter(), true);
			productList = new ArrayList<>();

			while (rootPageIterator.hasNext()) {
				Page childPage = rootPageIterator.next();

				String parentTitle = childPage.getParent().getTitle();
				String parentName = childPage.getParent().getName();
				String parentPath = childPage.getParent().getPath();

				ValueMap pageProperties = childPage.getProperties();

				String path = childPage.getPath();
				int childPageDepth = childPage.getDepth();
				ProductsBean productsBean = new ProductsBean();

				List<String> category = new ArrayList<>();

				// Excluding Accordion Pages using if condition
				if (childPageDepth > (rootPageDepth + 1)) {

					if (pageProperties.get(TagConstants.PN_TAGS, String[].class) != null) {
						String[] categoryTags = pageProperties.get(TagConstants.PN_TAGS, String[].class);
						for (String tag : categoryTags) {
							Tag categoryTag = tagManager.resolve(tag);
							category.add(categoryTag.getTitle());
						}

						productsBean.setTags(category);
					}

					productsBean.setTitle(pageProperties.get(JcrConstants.JCR_TITLE, String.class) != null
							? pageProperties.get(JcrConstants.JCR_TITLE, String.class)
							: StringUtils.EMPTY);

					if (StringUtils.isNotBlank(path) && path.contains("/")) {
						productsBean.setName(path.substring(path.lastIndexOf("/") + 1));
					}
					productsBean.setDescription(pageProperties.get(JcrConstants.JCR_DESCRIPTION, String.class) != null
							? pageProperties.get(JcrConstants.JCR_DESCRIPTION, String.class)
							: StringUtils.EMPTY);

					productsBean.setImage(pageProperties.get("image", String.class) != null
							? pageProperties.get("image", String.class)
							: StringUtils.EMPTY);

					productsBean.setDepth(childPageDepth);

					productsBean.setPath(ServiceUtils.getLink(resourceResolver,
							childPage.getPath(), settingsService));

					productsBean.setParentTitle(parentTitle != null ? parentTitle : StringUtils.EMPTY);
					productsBean.setParentName(parentName != null ? parentName : StringUtils.EMPTY);
					productsBean.setParentPath(parentPath != null ? parentPath : StringUtils.EMPTY);

					productList.add(productsBean);
				}

			}

			/**** Category Accordion Title Page Start ****/

			Iterator<Page> pageIterator = parentPage.listChildren(new PageFilter(), false);
			accordionMap = new LinkedHashMap<>();
			pageNameMap = new HashMap<>();
			while (pageIterator.hasNext()) {
				Page childPage = pageIterator.next();
				ProductsBean childBean = new ProductsBean();

				if (childPage != null) {
					childBean.setTitle(childPage.getTitle());
					childBean.setDepth(childPage.getDepth());
					childBean.setPath(childPage.getPath());

					Iterator<Page> gChildPageIterator = childPage.listChildren(new PageFilter(), false);
					accordionCategoryList = new ArrayList<>();
					while (gChildPageIterator.hasNext()) {
						Page gChildPage = gChildPageIterator.next();
						ProductsBean gChaildBean = new ProductsBean();

						if (gChildPage != null) {

							ValueMap vm = gChildPage.getProperties();

							gChaildBean.setTitle(gChildPage.getTitle());
							gChaildBean.setDepth(gChaildBean.getDepth());
							gChaildBean.setPath(gChaildBean.getPath());
							gChaildBean.setDescription(vm.get(JcrConstants.JCR_DESCRIPTION, String.class) != null
									? vm.get(JcrConstants.JCR_DESCRIPTION, String.class)
									: StringUtils.EMPTY);
						}

						accordionCategoryList.add(gChaildBean);
					}

					accordionMap.put(childPage.getTitle(), accordionCategoryList);
					pageNameMap.put(childPage.getTitle(), childPage.getName());

				}
			}

			/**** Category Accordion Title Page End ****/

		}

	}

	public List<ProductsBean> getProductList() {
		if (CollectionUtils.isNotEmpty(productList)) {
			return productList;
		}
		return new ArrayList<>();
	}

	public Map<String, List<ProductsBean>> getAccordionMap() {
		return accordionMap;
	}
	
	public Map<String, String> getPageNameMap() {
		return pageNameMap;
	}

}
