package com.deloitte.aem.lead2loyality.core.service.utility.configs;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name="XP Bootstrap : Rest Client Configurations", description="XP Bootstrap : Rest Client Config service")
public @interface RestClientConfigurations {

	 @AttributeDefinition(name = "Connection Timeout", description = "Connection timeout value in ms.", type = AttributeType.INTEGER)
     public int getConnectionTimeOut() default 0;
 
     @AttributeDefinition(name = "Socket Timeout", description = "Socket timeout value in ms.", type = AttributeType.INTEGER)
     public int getSocketTimeOut() default 0;
 
     @AttributeDefinition(name = "Request Timeout", description = "Request timeout value in ms.", type = AttributeType.INTEGER)
     public int getRequestTimeOut() default 0;
     
}
