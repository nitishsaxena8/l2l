package com.xperience.aem.xpatoms.core.models;

import javax.annotation.PostConstruct;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.ExporterOption;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(adaptables = {
	    Resource.class
	}, resourceType = "xpatoms/components/organisms/hero-banner", defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
	@Exporter(name = "jackson", extensions = "json", options = {
	    @ExporterOption(name = "MapperFeature.SORT_PROPERTIES_ALPHABETICALLY", value = "true"),
	    @ExporterOption(name = "SerializationFeature.WRITE_DATES_AS_TIMESTAMPS", value = "false")
	})
public class HeroBannerModel {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@ValueMapValue
    private String title;
	
	@ValueMapValue
    private String description;
	
	@ValueMapValue
    private String fileReference;
	
	@ValueMapValue
    private String imgAlt;
	
	@ValueMapValue
    private String linkHref;
	
	@ValueMapValue
    private String linkTitle;
	
	@ValueMapValue
    private String linkTarget;
	
	@ValueMapValue
    private String iconSrc;
	
	@ValueMapValue
    private String iconAlt;
	
	
	@PostConstruct
    protected void init() {
		logger.info("Hero Banner Initialized");
    }


	public String getTitle() {
		return title;
	}


	public String getDescription() {
		return description;
	}


	public String getFileReference() {
		return fileReference;
	}


	public String getImgAlt() {
		return imgAlt;
	}


	public String getLinkHref() {
		return linkHref;
	}


	public String getLinkTitle() {
		return linkTitle;
	}


	public String getLinkTarget() {
		return linkTarget;
	}


	public String getIconSrc() {
		return iconSrc;
	}


	public String getIconAlt() {
		return iconAlt;
	}
	
	
}
