package com.xperience.aem.xpatoms.core.models;

import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.ExporterOption;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(adaptables = {
	    Resource.class
	}, resourceType = "xpatoms/components/molecules/btn-group", defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
	@Exporter(name = "jackson", extensions = "json", options = {
	    @ExporterOption(name = "MapperFeature.SORT_PROPERTIES_ALPHABETICALLY", value = "true"),
	    @ExporterOption(name = "SerializationFeature.WRITE_DATES_AS_TIMESTAMPS", value = "false")
	})
public class ButtonGroupModel {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Inject
	@Via("resource")
    private Resource btnGrp;

	public Resource getBtnGrp() {
		return btnGrp;
	}

}
