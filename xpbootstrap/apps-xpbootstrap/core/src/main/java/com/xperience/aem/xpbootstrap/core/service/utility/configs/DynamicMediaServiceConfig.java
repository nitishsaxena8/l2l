package com.xperience.aem.xpbootstrap.core.service.utility.configs;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "XP Bootstrap Utility -  Dynamic Media Config", description = "XP Bootstrap Utility -  Dynamic Media Config")
public @interface DynamicMediaServiceConfig {

	@AttributeDefinition(name = "Preview Server URL", description = "Preview Server URL", type = AttributeType.STRING)
	String previewServerUrl() default "https://s7test1.scene7.com/";
	
	@AttributeDefinition(name = "Default Image preset", description = "Default Image preset", type = AttributeType.STRING)
	String defaultImagepreset() default "defaultResponsive";
}
