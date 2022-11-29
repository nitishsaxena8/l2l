package com.xperience.aem.xpbootstrap.core.servlet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.Servlet;

import org.apache.commons.collections4.iterators.TransformIterator;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.request.RequestPathInfo;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceMetadata;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.granite.ui.components.ds.DataSource;
import com.adobe.granite.ui.components.ds.SimpleDataSource;
import com.adobe.granite.ui.components.ds.ValueMapResource;
import com.day.cq.commons.jcr.JcrConstants;

@Component(
        service = Servlet.class,
        property = {
                Constants.SERVICE_DESCRIPTION + "= View Selector dropdown",
                "sling.servlet.resourceTypes=" + "/apps/viewselector"
        })
public class ViewSelectorDataSourceServlet extends SlingSafeMethodsServlet {

	private static final long serialVersionUID = -1903908352391570211L;
	private static Logger LOGGER = LoggerFactory.getLogger(ViewSelectorDataSourceServlet.class);

    @Override
    protected void doGet(SlingHttpServletRequest request,
                         SlingHttpServletResponse response) {
    	ResourceResolver resourceResolver = request.getResourceResolver();
    	RequestPathInfo requestPathInfo = request.getRequestPathInfo();
    	Resource contentResource = requestPathInfo.getSuffixResource();
    	Set<KeyValue> dropDownSet = new HashSet<>();
    	String resourceType = contentResource.getResourceType();
    	generateDropdown(resourceResolver,resourceType,dropDownSet);
    	List<KeyValue> keyValueList = new ArrayList<>();
    	keyValueList.add(new KeyValue("Default",""));
    	keyValueList.addAll(dropDownSet);
        DataSource ds =
                new SimpleDataSource(
                        new TransformIterator<>(
                        		keyValueList.iterator(),
                                input -> {
                                    KeyValue keyValue = (KeyValue) input;
                                    ValueMap vm = new ValueMapDecorator(new HashMap<>());
                                    vm.put("value", keyValue.value);
                                    vm.put("text", keyValue.key);
                                    return new ValueMapResource(
                                            resourceResolver, new ResourceMetadata(),
                                            JcrConstants.NT_UNSTRUCTURED, vm);
                                }));
    	request.setAttribute(DataSource.class.getName(), ds);
    }
    
    private void generateDropdown(ResourceResolver resourceResolver,String resourceType,Set<KeyValue> dropDownSet) {
    	Resource appResource = resourceResolver.getResource("/apps/" + resourceType);
    	LOGGER.info(">> Resource under apps >> " + appResource);
    	if(null != appResource) {
    		Iterator<Resource> childIterator = appResource.listChildren();
        	while(childIterator.hasNext()) {
        		Resource childResource = childIterator.next();
        		ValueMap vm = childResource.getValueMap();
        		String cmpViewTitle = vm.get("cmpviewTitle", String.class);
        		if(StringUtils.isNotEmpty(cmpViewTitle)) {
        			String viewName = childResource.getName();
        			if(viewName.contains(".")) {
						viewName = viewName.substring(0, viewName.lastIndexOf("."));
        			}
        			//KeyValue keyValue = new KeyValue(cmpViewTitle, childResource.getName());
					KeyValue keyValue = new KeyValue(cmpViewTitle, viewName);
        			dropDownSet.add(keyValue);
        		}
        	}
        	ValueMap appResourceVm = appResource.getValueMap();
        	String resourceSuperType = appResourceVm.get("sling:resourceSuperType",String.class);
        	if(StringUtils.isNotEmpty(resourceSuperType)) {
        		generateDropdown(resourceResolver, resourceSuperType, dropDownSet);
        	}
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

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getEnclosingInstance().hashCode();
			result = prime * result + ((value == null) ? 0 : value.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			KeyValue other = (KeyValue) obj;
			if (!getEnclosingInstance().equals(other.getEnclosingInstance()))
				return false;
			if (value == null) {
				if (other.value != null)
					return false;
			} else if (!value.equals(other.value))
				return false;
			return true;
		}

		private ViewSelectorDataSourceServlet getEnclosingInstance() {
			return ViewSelectorDataSourceServlet.this;
		}

		@Override
		public String toString() {
			return "KeyValue [key=" + key + ", value=" + value + "]";
		}
		
		
        
    }
}

