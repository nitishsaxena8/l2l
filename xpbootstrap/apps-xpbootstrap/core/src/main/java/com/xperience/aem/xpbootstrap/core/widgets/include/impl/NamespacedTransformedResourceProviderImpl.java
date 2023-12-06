package com.xperience.aem.xpbootstrap.core.widgets.include.impl;

import com.adobe.granite.ui.components.ExpressionResolver;
import com.xperience.aem.xpbootstrap.core.widgets.NamespacedTransformedResourceProvider;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;


@Component(service = NamespacedTransformedResourceProvider.class, immediate = true, configurationPolicy = ConfigurationPolicy.OPTIONAL)
@Designate(ocd = NamespacedTransformedResourceProviderImpl.Config.class)
public class NamespacedTransformedResourceProviderImpl implements NamespacedTransformedResourceProvider {
    
    @ObjectClassDefinition(
            name = "XP Bootstrap - Namespaced Transformed Resource Provider(include widget)",
            description = "Transforms a resource underlying children with a namespace. Used for granite dialog snippets to be included in a granular way.")
    public @interface Config {
        @AttributeDefinition(
                name = "Properties",
                description = "Properties that should be namespaced"
        )
        String[] properties() default {"name", "fileNameParameter", "fileReferenceParameter"};
    }
    
    @Reference
    private ExpressionResolver expressionResolver;
    
    private String[] namespacedProperties;
    
    @Activate
    @Modified 
    public void init(Config config) {
        this.namespacedProperties = config.properties();
    }
    
    @Override
    public Resource transformResourceWithNameSpacing(SlingHttpServletRequest request, Resource targetResource) {
        return new NamespaceResourceWrapper(targetResource, expressionResolver, request, namespacedProperties);
    }
    
}
