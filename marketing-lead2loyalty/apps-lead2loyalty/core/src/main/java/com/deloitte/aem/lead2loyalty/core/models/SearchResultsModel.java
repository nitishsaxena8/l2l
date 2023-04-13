package com.deloitte.aem.lead2loyalty.core.models;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.jcr.Session;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Model(adaptables = { SlingHttpServletRequest.class,
		Resource.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class SearchResultsModel {

	@SlingObject
	private ResourceResolver resourceResolver;

	private String pageTitlesJson;

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@PostConstruct
	protected void init() {
		QueryBuilder builder = resourceResolver.adaptTo(QueryBuilder.class);
		Session session = resourceResolver.adaptTo(Session.class);
		List<String> titles = new ArrayList<>();

		try {
			// Build the query
			Map<String, String> predicate = new HashMap<>();
				predicate.put("type", "cq:Page");
				predicate.put("path", "/content/lead2loyalty/language-masters/en");
				predicate.put("property", "jcr:content/jcr:title");
				predicate.put("property.operation", "exists");
				predicate.put("p.limit", "-1");
			Query query = builder.createQuery(PredicateGroup.create(predicate), session);
			SearchResult searchResult = query.getResult();

			// Process the results
			for(Hit hit : searchResult.getHits()) {
				ValueMap properties = hit.getProperties();
				String title = properties.get("jcr:title", String.class);
				titles.add(title);
			}

			// Serialize the result to JSON
			ObjectMapper mapper = new ObjectMapper();
			pageTitlesJson = mapper.writeValueAsString(titles);
		}
		catch(Exception e) {
			logger.error("Exception");
		}
	}

	public String getPageTitlesJson() {
		return pageTitlesJson;
	}
}