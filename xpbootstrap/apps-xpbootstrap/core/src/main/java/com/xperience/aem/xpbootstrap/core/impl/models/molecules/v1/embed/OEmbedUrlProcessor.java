package com.xperience.aem.xpbootstrap.core.impl.models.molecules.v1.embed;



import com.xperience.aem.xpbootstrap.core.component.Utils.OEmbedClient;
import com.xperience.aem.xpbootstrap.core.component.Utils.OEmbedResponse;
import com.xperience.aem.xpbootstrap.core.component.Utils.UrlProcessor;
import org.apache.commons.lang3.StringUtils;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.HashMap;

@Component(
        service = UrlProcessor.class
)
public class OEmbedUrlProcessor implements UrlProcessor {

    protected static final String NAME = "oembed";

    @Reference
    protected OEmbedClient oEmbedClient;

    @Override
    public Result process(String url) {
        if (oEmbedClient == null || StringUtils.isEmpty(url)) {
            return null;
        }
        String provider = oEmbedClient.getProvider(url);
        if (StringUtils.isEmpty(provider)) {
            return null;
        }
        OEmbedResponse oEmbedResponse = oEmbedClient.getResponse(url);
        if (oEmbedResponse == null) {
            return null;
        }
        boolean unsafeContext = oEmbedClient.isUnsafeContext(url);
        return new UrlProcessorResultImpl(
                NAME,
                new HashMap<String, Object>() {{
                    put("provider", provider);
                    put("response", oEmbedResponse);
                    put("unsafeContext", unsafeContext);
                }});
    }
}