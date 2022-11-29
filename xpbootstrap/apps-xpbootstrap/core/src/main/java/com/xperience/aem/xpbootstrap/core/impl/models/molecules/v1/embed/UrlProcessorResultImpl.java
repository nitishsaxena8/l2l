package com.xperience.aem.xpbootstrap.core.impl.models.molecules.v1.embed;

import com.xperience.aem.xpbootstrap.core.component.Utils.UrlProcessor;

import java.util.Map;

public class UrlProcessorResultImpl implements UrlProcessor.Result {

    String processor;
    Map<String, Object> options;

    public UrlProcessorResultImpl(String processor, Map<String, Object> options) {
        this.processor = processor;
        this.options = options;
    }

    @Override
    public String getProcessor() {
        return processor;
    }

    @Override
    public Map<String, Object> getOptions() {
        return options;
    }
}
