package com.deloitte.aem.lead2loyalty.core.service.utility.impl;

import com.deloitte.aem.lead2loyalty.core.beans.APIResponse;
import com.deloitte.aem.lead2loyalty.core.beans.BaseRequest;
import com.deloitte.aem.lead2loyalty.core.service.utility.Lead2loyaltyService;
import com.deloitte.aem.lead2loyalty.core.service.utility.RestClient;
import com.deloitte.aem.lead2loyalty.core.service.utility.configs.RestClientConfigurations;
import com.deloitte.aem.lead2loyalty.core.util.RestServiceConstants;
import com.deloitte.aem.lead2loyalty.core.util.RestServiceUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.MethodNotSupportedException;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;

/**
 * The Class RestServiceImpl. The Rest Client uses
 * PoolingHttpClientConnectionManager to create the ClosableHttpClient THe
 * Connection is set with max connections with 200
 */
@Component(service = RestClient.class, immediate = true)
public class Lead2loyaltyServiceImpl implements Lead2loyaltyService {

    /**
     * The Constant LOG.
     */
    private static final Logger LOG = LoggerFactory.getLogger(RestClientImpl.class);

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
