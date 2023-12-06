package com.xperience.aem.xpbootstrap.core.models.atoms;

import org.apache.sling.api.resource.Resource;

public interface AtmCardBody {

	String getAtmCardTitle();
	
	String getAtmCardSubtitle();
	
	String getAtmCardDescription();
	
	Resource getAtmCardPrimaryCta();
	
	Resource getAtmCardSecondaryCta();
	
}
