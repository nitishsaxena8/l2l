package com.xperience.aem.xpbootstrap.core.service.utility.impl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.jackrabbit.commons.JcrUtils;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xperience.aem.xpbootstrap.core.service.utility.ReadmeService;
import com.xperience.aem.xpbootstrap.core.util.ADLCUtility;

@Component(service = ReadmeService.class, immediate = true)
public class ReadmeServiceImpl implements ReadmeService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ReadmeServiceImpl.class);
	private static final String README_MD = "README.md";
	private static final String APPS = "/apps/";

	@Reference
	private ResourceResolverFactory resourceResolverFactory;

	public String getReadmeContent(final String componentPath) {

		try (ResourceResolver resourceResolver = ADLCUtility.getResourceResolver(resourceResolverFactory,
				ADLCUtility.ADLC_SUBSERVICE)) {
			final Resource componentResource = resourceResolver.getResource(APPS + componentPath);

			if (null == componentResource) {
				LOGGER.warn("Component under path -> [{}] does not exist", componentPath);
				return StringUtils.EMPTY;
			}
			//check if current resource has README file
			Resource readMeResource = componentResource.getChild(README_MD);
			final String superResourceType = componentResource.getResourceSuperType();
			
			if (readMeResource == null && StringUtils.isNotBlank(superResourceType)) {
				// check if super resource has README file
				final Resource superResource = resourceResolver.getResource(APPS + superResourceType);
				if (superResource != null) {
					readMeResource = superResource.getChild(README_MD);
				}
			}
			
			if (readMeResource == null) {
				LOGGER.error(
						"Readme.md resource is null, should be not null. Resource path = [{}]",
						componentResource.getPath());
				return null;
			}
			
			final Node mdFileResourceNode = readMeResource.adaptTo(Node.class);
			final InputStream inputStream = JcrUtils.readFile(mdFileResourceNode);
	        final String readmeText = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
			//final Parser PARSER = Parser.builder().build();
			//final HtmlRenderer HTML_RENDERER = HtmlRenderer.builder().build();
			//final org.commonmark.node.Node node = PARSER.parse(readmeText);
			return StringUtils.EMPTY;
			 
		} catch (RepositoryException | IOException | LoginException e) {
			LOGGER.error(e.getMessage(), e);
			return e.getMessage() + ". Please checkout logs";
		}
	}
}