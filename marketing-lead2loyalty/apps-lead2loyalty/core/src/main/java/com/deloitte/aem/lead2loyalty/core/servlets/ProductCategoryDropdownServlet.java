package com.deloitte.aem.lead2loyalty.core.servlets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.Servlet;

import org.apache.commons.collections4.iterators.TransformIterator;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceMetadata;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.granite.ui.components.ds.DataSource;
import com.adobe.granite.ui.components.ds.SimpleDataSource;
import com.adobe.granite.ui.components.ds.ValueMapResource;
import com.day.crx.JcrConstants;

@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "= Product Category Dynamic Drop Down",
		"sling.servlet.resourceTypes=" + "/apps/dropDownListing" })
public class ProductCategoryDropdownServlet extends SlingAllMethodsServlet {

	private static final long serialVersionUID = 1L;
	private static Logger LOGGER = LoggerFactory.getLogger(ProductCategoryDropdownServlet.class);

	private static final String ROOT_PATH = "/content/lead2loyalty/language-masters/en/home-page/products";

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) {
		try {
			ResourceResolver resourceResolver = request.getResourceResolver();
			List<KeyValue> dropDownList = new ArrayList<>();
			// Resource pathResource = request.getResource();
			// String rootPath =
			// pathResource.getChild("datasource").getValueMap().get("rootPath",String.class);
			// String rootPath = ROOT_PATH;
			if (StringUtils.isNotEmpty(ROOT_PATH)) {
				Resource resource = request.getResourceResolver().getResource(ROOT_PATH);
				if (resource != null) {
					Iterator<Resource> iterator = resource.listChildren();
					List<Resource> list = new ArrayList<>();
					iterator.forEachRemaining(list::add);
					list.forEach(res -> {
						// ValueMap valueMap = res.getValueMap();
						String key = res.getName();
						String value = res.getName();
						if (key.equals(JcrConstants.JCR_CONTENT)) {
							key = "None";
							value = StringUtils.EMPTY;
						}

						dropDownList.add(new KeyValue(value, key));
					});
				}}
			DataSource ds = new SimpleDataSource(new TransformIterator<>(dropDownList.iterator(), input -> {
				KeyValue keyValue = (KeyValue) input;
				ValueMap vm = new ValueMapDecorator(new HashMap<>());
				vm.put("value", keyValue.key);
				vm.put("text", keyValue.value);
				return new ValueMapResource(resourceResolver, new ResourceMetadata(), JcrConstants.NT_UNSTRUCTURED, vm);
			}));
			request.setAttribute(DataSource.class.getName(), ds);

		} catch (Exception e) {
			LOGGER.error("Error in Get Drop Down Values", e);
		}
	}

	private class KeyValue {

		/**
		 * key property.
		 */
		private String key;
		/**
		 * value property.
		 */
		private String value;

		/**
		 * constructor instance intance.
		 *
		 * @param newKey   -
		 * @param newValue -
		 */
		private KeyValue(final String newKey, final String newValue) {
			this.key = newKey;
			this.value = newValue;
		}
	}
}
