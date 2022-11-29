package com.xperience.aem.xpbootstrap.core.impl.models.component.library;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.RequestAttribute;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.xperience.aem.xpbootstrap.core.component.internal.AbstractComponentImpl;
import com.xperience.aem.xpbootstrap.core.models.component.library.ReadMeDescription;
import com.xperience.aem.xpbootstrap.core.service.utility.ReadmeService;

@Model(
	    adaptables = SlingHttpServletRequest.class,
	    adapters = {ReadMeDescription.class, ComponentExporter.class},
	    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
	)
	@Exporter(
	    name = ExporterConstants.SLING_MODEL_EXPORTER_NAME,
	    extensions = ExporterConstants.SLING_MODEL_EXTENSION
	)
public class ReadMeDescriptionImpl extends AbstractComponentImpl implements ReadMeDescription {

	private static final Logger LOGGER = LoggerFactory.getLogger(ReadMeDescriptionImpl.class);
	private String htmlMarkup = StringUtils.EMPTY;
	 
	@OSGiService
	protected ReadmeService readmeService;
	
	@Inject
	@RequestAttribute
	private String resourcePath;
	
	@PostConstruct
    protected void initModel() {
		//final String resourcePath = properties.get("resourcePath", String.class);
		LOGGER.info("ReadMeDescription :: resourcePath - {}", resourcePath);
		htmlMarkup = readmeService.getReadmeContent(resourcePath);
		LOGGER.debug("HTML markup : {}", htmlMarkup);
	}

	@Override
	public String getHtmlMarkup() {
		return htmlMarkup;
	}

}
