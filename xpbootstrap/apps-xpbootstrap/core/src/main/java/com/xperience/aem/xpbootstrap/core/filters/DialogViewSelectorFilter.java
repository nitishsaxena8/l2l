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
     scope = {SlingServletFilterScope.REQUEST}, // REQUEST, INCLUDE, FORWARD, ERROR, COMPONENT (REQUEST, INCLUDE, COMPONENT)
     pattern = "/mnt/override/.*",
     methods = {"GET","HEAD"}
)
public class DialogViewSelectorFilter implements Filter {
	
    private static final Logger LOG = LoggerFactory.getLogger(DialogViewSelectorFilter.class);
    
    private static final String VIEW_ATTRIBUTE_NAME = "cmpview";
    
    private static final String HTML_EXTENSION = ".html";
    

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {		
		final SlingHttpServletResponse slingResponse = (SlingHttpServletResponse) response;
        final SlingHttpServletRequest slingRequest = (SlingHttpServletRequest) request;
        RequestPathInfo requestPathInfo = slingRequest.getRequestPathInfo();
        final Resource suffixResource = requestPathInfo.getSuffixResource();
        String viewName = getView(suffixResource);
        if(StringUtils.isNotEmpty(viewName)) {
        	String dialogPath = getDialogPath(requestPathInfo.getResourcePath(),viewName);
        	if(StringUtils.isNotEmpty(dialogPath) && null != slingRequest.getResourceResolver().getResource(dialogPath)) {
        		String dispatchPath = dialogPath + HTML_EXTENSION + suffixResource.getPath();
        		LOG.info(" >> Dialog Variation filter dispatch dialog Path >> " + dispatchPath);
        		RequestDispatcher requestDispatcher = slingRequest.getRequestDispatcher(dispatchPath);      		
        		requestDispatcher.include(slingRequest, slingResponse);
        		return;
        	}
        }
        chain.doFilter(request, response);   
	}
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
	
	private String getDialogPath(String dialogResourcePath, String viewName) {
		if(StringUtils.isNotEmpty(dialogResourcePath)) {
			return dialogResourcePath.replace("cq:dialog", viewName + "/cq:dialog");
		}
		return null;
	}

	
	private String getView(Resource resource) {
		if(null == resource) {
			return null;
		}
		ValueMap vm = resource.getValueMap();
    	return vm.get(VIEW_ATTRIBUTE_NAME, String.class);
	}
	
	

}
