package com.deloitte.aem.lead2loyalty.core.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.Servlet;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.ContentType;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageFilter;
import com.day.cq.wcm.api.PageManager;
import com.deloitte.aem.lead2loyalty.core.beans.ProductsBean;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Component(service = Servlet.class)
@SlingServletResourceTypes(resourceTypes = "services/products/json", methods = HttpConstants.METHOD_GET, extensions = "json")
@ServiceDescription("Products Json Servlet")
public class ProductsJsonServlet extends SlingAllMethodsServlet {

	private static final long serialVersionUID = 7859688600832568906L;
	private static final Logger LOG = LoggerFactory.getLogger(ProductsJsonServlet.class);

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
		response.setContentType(ContentType.APPLICATION_JSON.getMimeType());
		response.setHeader("Dispatcher", "no-cache");
		String rootPage = request.getParameter("rootPage");

		JsonObject jsonObj = new JsonObject();
		JsonObject jsonObj2 = new JsonObject();
		ResourceResolver resourceResolver = request.getResourceResolver();
		PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
		Page parentPage = pageManager.getPage(rootPage);

		TagManager tagManager = resourceResolver.adaptTo(TagManager.class);

		int rootPageDepth = parentPage.getDepth();

		if (parentPage != null) {
			Iterator<Page> rootPageIterator = parentPage.listChildren(new PageFilter(), true);
			List<ProductsBean> productsList = new ArrayList<>();
			List<ProductsBean> productsList2 = new ArrayList<>();

			while (rootPageIterator.hasNext()) {
				Page childPage = rootPageIterator.next();

				String parentTitle = childPage.getParent().getTitle();
				String parentPath = childPage.getParent().getPath();

				ValueMap pageProperties = childPage.getProperties();

				String path = childPage.getPath();
				int childPageDepth = childPage.getDepth();
				ProductsBean productsBean = new ProductsBean();

				List<String> category = new ArrayList<>();

				//Excluding Accordion Pages using if condition
				if (childPageDepth > (rootPageDepth + 1)) {

					if (pageProperties.get("cq:tags", String[].class) != null) {
						String[] categoryTags = pageProperties.get("cq:tags", String[].class);
						for (String tag : categoryTags) {
							Tag categoryTag = tagManager.resolve(tag);
							category.add(categoryTag.getTitle());
						}

						productsBean.setTags(category);
					}

					productsBean.setTitle(pageProperties.get("jcr:title", String.class) != null
							? pageProperties.get("jcr:title", String.class)
							: StringUtils.EMPTY);

					if (StringUtils.isNotBlank(path) && path.contains("/")) {
						productsBean.setName(path.substring(path.lastIndexOf("/") + 1));
					}
					productsBean.setDescription(pageProperties.get("jcr:description", String.class) != null
							? pageProperties.get("jcr:description", String.class)
							: StringUtils.EMPTY);

					productsBean.setImage(pageProperties.get("image", String.class) != null
							? pageProperties.get("image", String.class)
							: StringUtils.EMPTY);

					productsBean.setDepth(childPageDepth);
					productsBean.setPath(childPage.getPath());

					productsBean.setParentTitle(parentTitle != null ? parentTitle : StringUtils.EMPTY);
					productsBean.setParentPath(parentPath != null ? parentPath : StringUtils.EMPTY);

					productsList.add(productsBean);
				}

			}

			/**** Category Accordion Title Page Start ****/
			Iterator<Page> pageIterator = parentPage.listChildren();
			while (pageIterator.hasNext()) {
				Page childPage = pageIterator.next();
				ProductsBean productsBean2 = new ProductsBean();

				if (childPage != null) {
					productsBean2.setTitle(childPage.getTitle());
					productsBean2.setDepth(childPage.getDepth());
					productsBean2.setPath(childPage.getPath());

					productsList2.add(productsBean2);
				}
			}
			/**** Category Accordion Title Page End ****/

			try {
				JsonArray finalJsonArray = new JsonArray();
				JsonArray jsonArray = new Gson().toJsonTree(productsList).getAsJsonArray();
				JsonArray jsonArray2 = new Gson().toJsonTree(productsList2).getAsJsonArray();
				
				jsonObj.add("products", jsonArray);
				finalJsonArray.add(jsonObj);
				jsonObj2.add("category", jsonArray2);
				finalJsonArray.add(jsonObj2);

				response.getWriter().write(finalJsonArray.toString());
			} catch (Exception e) {
				LOG.error("Exception in ProductsJson {}", e);
			}
		}
	}
}