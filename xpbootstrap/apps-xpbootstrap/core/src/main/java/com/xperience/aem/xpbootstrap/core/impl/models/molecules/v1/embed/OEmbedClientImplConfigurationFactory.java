package com.xperience.aem.xpbootstrap.core.impl.models.molecules.v1.embed;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.osgi.service.metatype.annotations.Option;

@Component(
        service = OEmbedClientImplConfigurationFactory.class
)
@Designate(
        ocd = OEmbedClientImplConfigurationFactory.Config.class,
        factory = true
)
public class OEmbedClientImplConfigurationFactory {

    private Config config;

    @ObjectClassDefinition(
            name = "Core Components oEmbed Client",
            description = "Configuration for defining oEmbed endpoints."
    )
    public @interface Config {
        @AttributeDefinition(
                name = "Provider Name",
                description = "Name of the oEmbed provider."
        )
        String provider();

        @AttributeDefinition(
                name = "Format",
                description = "Defines the format for the oEmbed response",
                options = {
                        @Option(
                                label = "JSON",
                                value = "json"),
                        @Option(
                                label = "XML",
                                value = "xml"
                        )
                }
        )
        String format();

        @AttributeDefinition(
                name = "API Endpoint",
                description = "Defines the URL where consumers may request representation for this provider."
        )
        String endpoint();

        @AttributeDefinition(
                name = "URL Scheme",
                description = "Describes which URLs provided by the service may have an embedded representation."
        )
        String[] scheme();

        @AttributeDefinition(
                name = "Unsafe Context",
                description = "Describes whether the provider response HTML is allowed to be displayed in an unsafe context."
        )
        boolean unsafeContext() default false;
    }

    @Activate
    @Modified
    void configure(Config config) {
        this.config = config;
    }

    public Config getConfig() {
        return config;
    }
}