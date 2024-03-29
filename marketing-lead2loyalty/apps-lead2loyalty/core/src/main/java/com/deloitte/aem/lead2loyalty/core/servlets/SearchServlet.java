package com.deloitte.aem.lead2loyalty.core.servlets;

import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.deloitte.aem.lead2loyalty.core.beans.SearchFilterBean;
import com.deloitte.aem.lead2loyalty.core.beans.SearchResponseWrapper;
import com.deloitte.aem.lead2loyalty.core.beans.SearchResultBean;
import com.deloitte.aem.lead2loyalty.core.constants.ApplicationConstants;
import com.deloitte.aem.lead2loyalty.core.service.utility.Lead2loyaltyService;
import com.deloitte.aem.lead2loyalty.core.util.WebUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Session;
import javax.servlet.Servlet;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Component(
        service= Servlet.class,
        property={
                Constants.SERVICE_DESCRIPTION + "=Search Servlet",
                "sling.servlet.methods=GET",
                "sling.servlet.paths=" + "/bin/search",
                "sling.servlet.extensions=" + "json"
        }
)
public class SearchServlet extends SlingAllMethodsServlet {
    private static final long serialVersionUID = 1L;
    private final transient Logger logger = LoggerFactory.getLogger(getClass());
    @Reference
    private transient Lead2loyaltyService lead2loyaltyService;
    @Reference
    private transient QueryBuilder builder;
    private final int RESULTS_PER_PAGE = 10;
    @Override
    protected void doPost(final SlingHttpServletRequest request,
                          final SlingHttpServletResponse response) throws IOException {

        String requestBody = IOUtils.toString(request.getReader());
        ResourceResolver resourceResolver = lead2loyaltyService.getServiceResolver();
        Session session = resourceResolver.adaptTo(Session.class);

        ObjectMapper responseObjectMapper = new ObjectMapper();
        SearchResponseWrapper searchResponseWrapper = new SearchResponseWrapper();
        List<SearchResultBean> searchResultBeanList = new ArrayList<>();
        HashSet<String> pagePathHashSet = new HashSet<>();

        try {
            Map<String, String> predicate = createPredicate(requestBody);
            Query query = builder.createQuery(PredicateGroup.create(predicate), session);
            SearchResult searchResult = query.getResult();

            for(Hit hit : searchResult.getHits()) {
                Page page = resourceResolver.adaptTo(PageManager.class).getContainingPage(hit.getResource());
                pagePathHashSet.add(page.getPath());
            }
            searchResponseWrapper.setSearchResultCount(pagePathHashSet.size());
            searchResponseWrapper.setPageCount(pagePathHashSet.size()/RESULTS_PER_PAGE + 1);
            for(String pagePath : pagePathHashSet) {
                Page page = resourceResolver.getResource(pagePath).adaptTo(Page.class);
                searchResultBeanList.add(getSearchResultBean(page));
            }
            JSONObject json = WebUtils.getRequestParam(requestBody);
            String sortBy = json.get("sortBy").toString();
            if(sortBy.equals("Latest")) {
                searchResultBeanList.sort(Comparator.comparing(SearchResultBean::getPublishDate).reversed());
            }
            searchResultBeanList.forEach(searchResultBean -> searchResultBean.setPublishDateAsString(searchResultBean.getPublishDate() != null ? searchResultBean.getPublishDate().toString() : StringUtils.EMPTY));
            searchResponseWrapper.setSearchResultBeanList(searchResultBeanList);
            searchResponseWrapper.setSearchFilterBeanList(createSearchFilter(searchResultBeanList));
            searchResponseWrapper.setStatus(Boolean.TRUE);
            response.getWriter().println(responseObjectMapper.writeValueAsString(searchResponseWrapper));
        }
        catch(Exception e) {
            searchResponseWrapper.setStatus(Boolean.FALSE);
            response.getWriter().println(responseObjectMapper.writeValueAsString(searchResponseWrapper));
            logger.error("Exception");
        }
    }

    private SearchResultBean getSearchResultBean(Page page) {

        SearchResultBean searchResultBean = new SearchResultBean();

        if(page != null) {
            ValueMap properties = page.getProperties();
            searchResultBean.setPagePath(page.getPath());
            searchResultBean.setTitle(properties.get(JcrConstants.JCR_TITLE, String.class) != null
                    ? properties.get(JcrConstants.JCR_TITLE, String.class)
                    : StringUtils.EMPTY);
            searchResultBean.setDescription(properties.get(JcrConstants.JCR_DESCRIPTION, String.class) != null
                    ? properties.get(JcrConstants.JCR_DESCRIPTION, String.class)
                    : StringUtils.EMPTY);
            searchResultBean.setCategory(properties.get(ApplicationConstants.PAGE_TYPE_PROPERTY, String.class) != null
                    ? properties.get(ApplicationConstants.PAGE_TYPE_PROPERTY, String.class)
                    : "Misc.");
            searchResultBean.setKeywords(properties.get("keywords", String.class) != null
                    ? properties.get("keywords", String.class)
                    : StringUtils.EMPTY);
            if(page.getContentResource().getChild(ApplicationConstants.IMAGE_PROPERTY) != null) {
                ValueMap imageProperties = Objects.requireNonNull(page.getContentResource().getChild(ApplicationConstants.IMAGE_PROPERTY)).getValueMap();
                searchResultBean.setImagePath(imageProperties.get("fileReference", String.class) != null
                        ? imageProperties.get("fileReference", String.class)
                        : StringUtils.EMPTY);
            } else {
                searchResultBean.setImagePath(properties.get(ApplicationConstants.IMAGE_PROPERTY, String.class) != null
                        ? properties.get(ApplicationConstants.IMAGE_PROPERTY, String.class)
                        : StringUtils.EMPTY);
            }
            searchResultBean.setPublishDate(properties.get(ApplicationConstants.CQ_LAST_MODIFIED_PROPERTY, Date.class) != null
                    ? properties.get(ApplicationConstants.CQ_LAST_MODIFIED_PROPERTY, Date.class)
                    : null);
        }
        return  searchResultBean;
    }

    private Map<String, String> createPredicate(String requestBody) throws JSONException {

        Map<String, String> predicate = new HashMap<>();
        JSONObject json = WebUtils.getRequestParam(requestBody);

        predicate.put("path", json.get("rootPagePath").toString());
        predicate.put("type", "nt:unstructured");
        predicate.put("fulltext", json.get("query").toString());
        predicate.put("p.limit", "-1");

        return predicate;
    }

    private List<SearchFilterBean> createSearchFilter(List<SearchResultBean> searchResultBeanList) {

        List<SearchFilterBean> searchFilterBeanList = new ArrayList<>();
        List<String> distinctFilterList = searchResultBeanList.stream()
                .map(SearchResultBean::getCategory)
                .distinct()
                .collect(Collectors.toList());

        for(String filter : distinctFilterList) {
            long filterCount = searchResultBeanList.stream()
                    .filter(c -> filter.equals(c.getCategory()))
                    .count();
            searchFilterBeanList.add(new SearchFilterBean(filter, filterCount));
        }
        searchFilterBeanList.sort(Comparator.comparing(SearchFilterBean::getFilterCount).reversed());
        return searchFilterBeanList;
    }

}
