package com.xperience.aem.xpbootstrap.core.widgets.include.impl;

import org.apache.commons.collections.MapUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.engine.EngineConstants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;

import javax.annotation.CheckForNull;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;


import java.io.IOException;

@Component(
        service = Filter.class,
        configurationPolicy = ConfigurationPolicy.OPTIONAL,
        property= {
                EngineConstants.SLING_FILTER_SCOPE+"="+ EngineConstants.FILTER_SCOPE_INCLUDE
        }
)
public class IncludeDecoratorFilterImpl implements Filter {

    static final String RESOURCE_TYPE = "xpbootstrap/components/master/widgets/include";

    /**
     * Request attribute of the include namespace
     */
    static final String REQ_ATTR_NAMESPACE = "XP_BOOTSTRAP_INCLUDE_NAMESPACE";
    /**
     * Parameters node name
     */
    static final String NN_PARAMETERS = "parameters";

    /**
     * Property name parameters
     */
    static final String PN_NAMESPACE = "namespace";

    /**
     * Prefix value parameters passed downwards
     */
    static final String PREFIX = "XP_BOOTSTRAP_INCLUDE_PREFIX_";
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // no-op
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {

        ValueMap parameters = ValueMap.EMPTY;

        if(servletRequest instanceof SlingHttpServletRequest){
            
            SlingHttpServletRequest request = (SlingHttpServletRequest) servletRequest;

            if(request.getResourceResolver().isResourceType(request.getResource(), RESOURCE_TYPE)){
                performFilter(request, servletResponse, chain, parameters);
                return;
            }
            
        }
    
        chain.doFilter(servletRequest, servletResponse);
    }

    private void performFilter(SlingHttpServletRequest request, ServletResponse servletResponse, FilterChain chain, ValueMap parameters) throws IOException, ServletException {
        @CheckForNull Resource parameterResource = request.getResource().getChild(NN_PARAMETERS);
        if(parameterResource != null){
            parameters = parameterResource.getValueMap();
        }

        ValueMap includeProperties = request.getResource().getValueMap();

        Object existingNamespace = request.getAttribute(REQ_ATTR_NAMESPACE);
        boolean hasExistingNamespace = existingNamespace != null;
        boolean hasNamespaceInInclude = includeProperties.containsKey(PN_NAMESPACE);

        if(MapUtils.isNotEmpty(parameters)){
            parameters.forEach((key, object) -> {
                request.setAttribute(PREFIX + key, object);
            });
        }

        if(hasNamespaceInInclude && hasExistingNamespace){
            request.setAttribute(REQ_ATTR_NAMESPACE, existingNamespace + "/" + includeProperties.get(PN_NAMESPACE, String.class));
        }else if(hasNamespaceInInclude){
            request.setAttribute(REQ_ATTR_NAMESPACE, includeProperties.get(PN_NAMESPACE, String.class));
        }

        chain.doFilter(request, servletResponse);

        if(MapUtils.isNotEmpty(parameters)){
            parameters.forEach((key, object) -> {
                request.removeAttribute(PREFIX + key);
            });
        }

        if(existingNamespace != null){
            request.setAttribute(REQ_ATTR_NAMESPACE, existingNamespace);
        }else{
            request.removeAttribute(REQ_ATTR_NAMESPACE);
        }
    }


    @Override
    public void destroy() {
        // no-op
    }
}
