package com.xperience.aem.xpbootstrap.core.impl.models.molecules.v1.embed;

import com.xperience.aem.xpbootstrap.core.component.Utils.UrlProcessor;
import org.apache.commons.lang3.StringUtils;
import org.osgi.service.component.annotations.Component;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component(
        service = UrlProcessor.class
)
public class PinterestUrlProcessor implements UrlProcessor {

    protected static final String NAME = "pinterest";

    protected static final String PIN_ID = "pinId";

    protected static final String SCHEME = "https://www\\.pinterest\\.com/pin/(\\d+)/";

    private Pattern pattern = Pattern.compile(SCHEME);

    @Override
    public Result process(String url) {
        if (StringUtils.isNotEmpty(url)) {
            Matcher matcher = pattern.matcher(url);
            if (matcher.matches()) {
                return new UrlProcessorResultImpl(
                        NAME,
                        new HashMap<String, Object>() {{
                            put(PIN_ID, matcher.group(1));
                        }});
            }
        }
        return null;
    }
}
