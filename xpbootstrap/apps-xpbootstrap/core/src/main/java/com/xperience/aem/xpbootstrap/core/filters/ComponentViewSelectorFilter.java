package com.xperience.aem.xpbootstrap.core.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.request.RequestPathInfo;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.servlets.annotations.SlingServletFilter;
import org.apache.sling.servlets.annotations.SlingServletFilterScope;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
@SlingServletFilter(
     scope = {SlingServletFilterScope.COMPONENT}, // REQUEST, INCLUDE, FORWARD, ERROR, COMPONENT (REQUEST, INCLUDE, COMPONENT)
     pattern = "/content/.*",
     methods = {"GET","HEAD"}
)
public class ComponentViewSelectorFilter implements Filter {
	
    private static final Logger LOG = LoggerFactory.getLogger(ComponentViewSelectorFilter.class);
    
    private static final String VIEW_ATTRIBUTE_NAME = "cmpview";
    
    private static final String HTML_EXTENSION = ".html";
    
    private static final String REQUEST_VIEW_SELECTOR_NAME = "request.variation.selector.name";


	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		final SlingHttpServletResponse slingResponse = (SlingHttpServletResponse) response;
        final SlingHttpServletRequest slingRequest = (SlingHttpServletRequest) request;
        final Resource resource = slingRequest.getResource();
        if(removeVariationRequestAttr(slingRequest)) {
        	chain.doFilter(request, response);
        } else {            
            String viewPath = getViewPath(resource,slingRequest.getRequestPathInfo());
            if(StringUtils.isNotEmpty(viewPath)) {
            	LOG.info(">> Included a request with new selectors >> " + viewPath);
        		RequestDispatcher requestDispatcher = slingRequest.getRequestDispatcher(viewPath);      		
        		slingRequest.setAttribute(REQUEST_VIEW_SELECTOR_NAME, VIEW_ATTRIBUTE_NAME);
        		requestDispatcher.include(slingRequest, slingResponse);
        		return;
        	}
            chain.doFilter(request, response);
        }        
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
	
	private String getViewPath(Resource resource, RequestPathInfo requestPathInfo) {
		if(null == resource) {
			return null;
		}
		ValueMap vm = resource.getValueMap();
    	String cmpview = vm.get(VIEW_ATTRIBUTE_NAME, String.class);
    	if(StringUtils.isNotEmpty(cmpview)) {
    		String extension = requestPathInfo.getExtension();
    		if(StringUtils.isNotEmpty(extension) && !"html".equalsIgnoreCase(extension)) {
    			return null;
    		} else {
    			return new StringBuilder(resource.getPath()).append(".").append(cmpview).append(HTML_EXTENSION).toString();
    		}    		
    	}
    	return null;
	}
	
	private boolean removeVariationRequestAttr(SlingHttpServletRequest slingRequest) {
		if(null != slingRequest.getAttribute(REQUEST_VIEW_SELECTOR_NAME)) {
			LOG.info(" >> Variation attribute exists ");
			slingRequest.removeAttribute(REQUEST_VIEW_SELECTOR_NAME);
			return true;
		}
		return false;
	}

}
