package com.xperience.aem.xpbootstrap.core.widgets;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.osgi.annotation.versioning.ProviderType;

/**
 * NamespacedTransformedResourceProvider
 * <p>
 * Transforms a resource underlying children with a namespace.
 * </p>
 */
@ProviderType
public interface NamespacedTransformedResourceProvider {
    
    /**
     * Transforms a resource underlying children with a namespace.
     * Children under the resource will have various properties (the ones configured under the service) prefixed with the namespace
     * @param request
     * @param targetResource
     * @return Wrapped resource
     */
    Resource transformResourceWithNameSpacing(SlingHttpServletRequest request, Resource targetResource);
    
}
