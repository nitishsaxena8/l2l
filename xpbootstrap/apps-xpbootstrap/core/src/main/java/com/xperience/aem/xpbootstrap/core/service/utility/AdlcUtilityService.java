package com.xperience.aem.xpbootstrap.core.service.utility;

import org.apache.http.message.BasicNameValuePair;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolver;

import java.util.List;
import java.util.Map;

public interface AdlcUtilityService {

    public String getUrlParams(List<BasicNameValuePair> params);

    public boolean isInternalPageLink(String link, ResourceResolver resourceResolver);

    public String formatURL(String url, ResourceResolver resourceResolver, SlingHttpServletRequest request, Map<String, String> internalDomains);

    public boolean isInternalDomain(Map<String, String> internalDomains, String domainName);

    public String formatMappedURL(String url, ResourceResolver resourceResolver, SlingHttpServletRequest request, Map<String, String> internalDomains);

    public String externalizeUri(String url, ResourceResolver resourceResolver, SlingHttpServletRequest request, Map<String, String> internalDomains, String externalizerKey);

    public String extractDomainNameFromRequest(SlingHttpServletRequest request);

    public void addToRequestAttributeSet(SlingHttpServletRequest request, String setName, String value);

}
