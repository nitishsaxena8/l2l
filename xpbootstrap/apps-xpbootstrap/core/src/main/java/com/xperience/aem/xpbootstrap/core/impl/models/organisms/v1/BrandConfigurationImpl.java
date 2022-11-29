package com.xperience.aem.xpbootstrap.core.impl.models.organisms.v1;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.day.cq.commons.jcr.JcrConstants;
import com.xperience.aem.xpbootstrap.core.beans.BrandConfigItem;
import com.xperience.aem.xpbootstrap.core.impl.models.AbstractCompImpl;
import com.xperience.aem.xpbootstrap.core.models.organisms.BrandConfiguration;
import com.xperience.aem.xpbootstrap.core.util.ADLCUtility;

@Model(adaptables = SlingHttpServletRequest.class, 
	   adapters = {BrandConfiguration.class, ComponentExporter.class},
	   defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(
	    name = ExporterConstants.SLING_MODEL_EXPORTER_NAME,
	    extensions = ExporterConstants.SLING_MODEL_EXTENSION
	)
public class BrandConfigurationImpl extends AbstractCompImpl implements BrandConfiguration {

	private static final Logger LOGGER = LoggerFactory.getLogger(BrandConfigurationImpl.class);
	
	protected static final String RESOURCE_TYPE = "/apps/xpbootstrap/components/master/admin-console/brandConfig";
	
	private static final String ROOT_CONFIG_PATH = "/conf/xpbootstrap/adminconsole";
	
	private List<BrandConfigItem> brandConfigItems;
	
	@OSGiService
	private ResourceResolverFactory resourceResolverFactory;

	@PostConstruct
	protected void initializeModel() {
		LOGGER.info("BrandConfigurationImpl :: init");
		this.brandConfigItems = getBrandConfigPages();
	}

	private List<BrandConfigItem> getBrandConfigPages() {
		ResourceResolver resolver = null;
		List<BrandConfigItem> configPageList = new ArrayList<BrandConfigItem>();
		try {
			resolver = ADLCUtility.getResourceResolver(resourceResolverFactory, ADLCUtility.ADLC_SUBSERVICE);
			if (resolver != null) {
				final Resource rootConfigResource = resolver.getResource(ROOT_CONFIG_PATH);
				
				if (rootConfigResource != null) {
					
					final Iterator<Resource> resourceIterator = rootConfigResource.listChildren();
					 while(resourceIterator.hasNext()) {
						final Resource brandConfigResource =  resourceIterator.next();
						final BrandConfigItem brandConfigItem = new BrandConfigItem();
						brandConfigItem.setName(getBrandConfigPageName(resolver, brandConfigResource));
						brandConfigItem.setTitle(getBrandConfigPageTitle(resolver, brandConfigResource));
						brandConfigItem.setPath(brandConfigResource.getPath());
						configPageList.add(brandConfigItem);
					 }
					
				}
			}
		} catch (final LoginException exception) {
			LOGGER.error("Error occured : {}", exception.getLocalizedMessage());
		} finally {
			ADLCUtility.closeResourceResolver(resolver);
		}
		LOGGER.info("configPageList size : {}", configPageList.size());
		return configPageList;

	}
	
	private String getBrandConfigPageTitle(final ResourceResolver resolver, final Resource brandConfigResource) {
		final ValueMap valueMap = getValueMapFromResource(resolver, brandConfigResource);
		if(valueMap != null && valueMap.containsKey(JcrConstants.JCR_TITLE)) {
			return valueMap.get(JcrConstants.JCR_TITLE, String.class);
		}
		return null;
	}

	private String getBrandConfigPageName(final ResourceResolver resolver, final Resource childResource) {
		final ValueMap valueMap = getValueMapFromResource(resolver, childResource);
		if(valueMap != null && valueMap.containsKey(JcrConstants.JCR_NAME)) {
			return valueMap.get(JcrConstants.JCR_NAME, String.class);
		}
		return null;
	}

	private ValueMap getValueMapFromResource(final ResourceResolver resolver, final Resource brandConfigResource) {
		final String contentPath = brandConfigResource.getPath() + "/" + JcrConstants.JCR_CONTENT;
		final Resource content = resolver.getResource(contentPath);
		final ValueMap valueMap = content.getValueMap();
		return valueMap;
	}

	@Override
	@SuppressWarnings("squid:S2384")
	public List<BrandConfigItem> getBrandConfigItems() {
		return this.brandConfigItems;
	}
}
