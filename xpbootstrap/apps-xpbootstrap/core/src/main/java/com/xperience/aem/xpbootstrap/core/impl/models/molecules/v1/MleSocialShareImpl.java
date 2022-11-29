package com.xperience.aem.xpbootstrap.core.impl.models.molecules.v1;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xperience.aem.xpbootstrap.core.component.internal.AbstractComponentImpl;
import com.xperience.aem.xpbootstrap.core.models.molecules.MleSocialShare;

@Model(adaptables = {
		Resource.class }, adapters = MleSocialShare.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL, resourceType = MleSocialShareImpl.RESOURCE_TYPE)
public class MleSocialShareImpl extends AbstractComponentImpl implements MleSocialShare {

	public static final String RESOURCE_TYPE = "xpbootstrap/components/master/molecules/mleSocialShare/v1/mleSocialShare";
	private static final Logger logger = LoggerFactory.getLogger(MleSocialShareImpl.class);

	@Inject
	private String enableFacebook;

	@Inject
	private String enableTwitter;

	@Inject
	private String enableLinkedIn;
	
	@Inject 
	private String enableCopyUrl;
	
	@Inject
	private String pluginUrl;
	
	@Override
	public String getEnableCopyUrl() {
        return enableCopyUrl;
    }

    @Override
	public String getEnableFacebook() {
		return enableFacebook;
	}

	@Override
	public String getEnableTwitter() {
		return enableTwitter;
	}

	@Override
	public String getEnableLinkedIn() {
		return enableLinkedIn;
	}

    @Override
    public String getPluginUrl() {
        return pluginUrl;
    }
}
