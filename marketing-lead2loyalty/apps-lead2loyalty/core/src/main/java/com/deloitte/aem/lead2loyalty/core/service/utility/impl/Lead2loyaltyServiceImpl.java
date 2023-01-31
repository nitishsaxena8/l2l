package com.deloitte.aem.lead2loyalty.core.service.utility.impl;

import com.deloitte.aem.lead2loyalty.core.service.utility.Lead2loyaltyService;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * The Class Lead2loyaltyServiceImpl.
 */
@Component(service = Lead2loyaltyService.class, immediate = true)
public class Lead2loyaltyServiceImpl implements Lead2loyaltyService {

    /**
     * The Constant LOG.
     */
    private static final Logger LOG = LoggerFactory.getLogger(Lead2loyaltyServiceImpl.class);

    @Reference
    ResourceResolverFactory resourceResolverFactory;

    /**
     * @return serviceResolver
     */
    public ResourceResolver getServiceResolver() {
        Map<String, Object> param = new HashMap<>();
        param.put(ResourceResolverFactory.USER, "loyalty-user");
        param.put(ResourceResolverFactory.SUBSERVICE, "loyaltyService");
        LOG.debug("param map is {} ", param);
        try {
            return resourceResolverFactory.getServiceResourceResolver(param);
        } catch (LoginException e) {
            LOG.error("LoginException doImport method {}", e.getMessage());
        }
        return null;
    }

}
