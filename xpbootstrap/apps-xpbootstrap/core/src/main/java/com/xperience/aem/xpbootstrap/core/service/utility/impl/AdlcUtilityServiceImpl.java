package com.xperience.aem.xpbootstrap.core.service.utility.impl;

import com.day.cq.commons.Externalizer;
import com.day.cq.wcm.api.NameConstants;
import com.xperience.aem.xpbootstrap.core.service.utility.AdlcUtilityService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceUtil;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component(service = AdlcUtilityService.class, immediate = true)
public class AdlcUtilityServiceImpl implements AdlcUtilityService {


    public static final String SLASH_CONTENT = "/content";
    public static final String SLASH_CONTENT_SLASH_DAM = "/content/dam";
    public static final String SUFFIX_HTML = ".html";
    public static final String HTTPS_PROTOCOL = "https://";
    public static final String FORWARD_SLASH = "/";

    private AdlcUtilityServiceImpl() {
        super();
    }

    private static final Logger LOG = LoggerFactory.getLogger(AdlcUtilityServiceImpl.class);

    @Override
    public String getUrlParams(List<BasicNameValuePair> params) {
        if (!CollectionUtils.isEmpty(params)) {
            return URLEncodedUtils.format(params, StandardCharsets.UTF_8.name());
        }
        return StringUtils.EMPTY;
    }


    /**
     * Checks whether a url is an internal AEM Page link or not. Checks by resolving
     * the link to a resource and checks whether the resource is of type cq:Page.
     *
     * @param link
     * @param resourceResolver
     * @return true if the link resolves to a resource.
     */
    @Override
    public boolean isInternalPageLink(String link, ResourceResolver resourceResolver) {
        Resource resource = resourceResolver.resolve(link);
        return !ResourceUtil.isNonExistingResource(resource) && resource.isResourceType(NameConstants.NT_PAGE);
    }

    /**
     * Formats an internal URL.Checks if the url is internal or external and applies
     * the suffix accordingly after mapping the url using resource resolver. it will
     * also adds html suffix.
     *
     * @param url
     * @param resourceResolver
     * @return formated url.
     */
    @Override
    public String formatURL(String url, ResourceResolver resourceResolver, SlingHttpServletRequest request, Map<String, String> internalDomains) {
        if (StringUtils.isNotBlank(url)) {
            url = url.trim();
            if (url.startsWith(SLASH_CONTENT)) {
                if (!url.contains(SUFFIX_HTML) && !url.startsWith(SLASH_CONTENT_SLASH_DAM)) {
                    url = url + SUFFIX_HTML;
                }
                // Logic to handle links on internal load balancer.
                String domainName = request.getServerName();
                if (StringUtils.isNotBlank(domainName) && MapUtils.isNotEmpty(internalDomains) && internalDomains.containsKey(domainName)) {
                    url = internalDomains.get(domainName) + url;
                }
            } else if (url.startsWith("www.")) {
                url = HTTPS_PROTOCOL + url;
            }
        }
        return url;
    }

    @Override
    public boolean isInternalDomain(Map<String, String> internalDomains, String domainName) {
        if (StringUtils.isNotBlank(domainName) && MapUtils.isNotEmpty(internalDomains) && internalDomains.containsKey(domainName)) {
            LOG.info("Internal Domain");
            return true;
        }
        return false;
    }

    /**
     * maps the url using resolver mapping
     *
     * @param url
     * @param resourceResolver
     * @return mapped path
     */
    @Override
    public String formatMappedURL(String url, ResourceResolver resourceResolver, SlingHttpServletRequest request, Map<String, String> internalDomains) {
        if (StringUtils.isBlank(url)) {
            return url;
        }
        if (url.startsWith(SLASH_CONTENT)) {
            url = url.trim();
            if (!url.contains(SUFFIX_HTML) && !url.startsWith(SLASH_CONTENT_SLASH_DAM)) {
                url = url + SUFFIX_HTML;
            }
            String domainName = extractDomainNameFromRequest(request);
            if (StringUtils.isNotBlank(domainName) && MapUtils.isNotEmpty(internalDomains) && internalDomains.containsKey(domainName)) {
                LOG.info("Internal Domain");
                return internalDomains.get(domainName) + url;
            } else {
                return resourceResolver.map(request, url);
            }
        } else if (url.startsWith("www.")) {
            return HTTPS_PROTOCOL + url;
        } else {
            return url;
        }

    }

    @Override
    public String externalizeUri(String url, ResourceResolver resourceResolver, SlingHttpServletRequest request, Map<String, String> internalDomains, String externalizerKey) {
        String mappedUri = formatMappedURL(url, resourceResolver, request, internalDomains);
        if (StringUtils.startsWith(mappedUri, SLASH_CONTENT)) {
            Externalizer externalizer = resourceResolver.adaptTo(Externalizer.class);
            if (StringUtils.isNotBlank(externalizerKey)) {
                return externalizer.externalLink(request.getResourceResolver(),
                        externalizerKey, mappedUri);
            } else {
                LOG.debug(
                        "No externalizer domain configured, take into account current host header {} and current scheme {}",
                        request.getServerName(), request.getScheme());
                return externalizer
                        .absoluteLink(request, request.getScheme(), mappedUri);
            }
        } else if (!StringUtils.startsWith(mappedUri, HTTPS_PROTOCOL) && StringUtils.startsWith(mappedUri, FORWARD_SLASH)) {
            // Url Starts with slash & does not have domain name - assume the request came from current domain
            Externalizer externalizer = resourceResolver.adaptTo(Externalizer.class);
            return externalizer
                    .absoluteLink(request, request.getScheme(), mappedUri);
        }
        return mappedUri;
    }

    @Override
    public String extractDomainNameFromRequest(SlingHttpServletRequest request) {
        StringBuffer url = request.getRequestURL();
        String uri = request.getRequestURI();
        int idx = (((uri != null) && (uri.length() > 0)) ? url.indexOf(uri) : url.length());
        String host = url.substring(0, idx); //base url
        LOG.info("host Name >>> " + host);
        return host;
    }

    @Override
    public void addToRequestAttributeSet(SlingHttpServletRequest request, String setName, String value) {
        if (null == request.getAttribute(setName)) {
            Set<String> attributeSet = new HashSet<>();
            attributeSet.add(value);
            request.setAttribute(setName, attributeSet);
        } else {
            Set<String> attributeSet = (Set<String>) request.getAttribute(setName);
            attributeSet.add(value);
        }
    }
}
