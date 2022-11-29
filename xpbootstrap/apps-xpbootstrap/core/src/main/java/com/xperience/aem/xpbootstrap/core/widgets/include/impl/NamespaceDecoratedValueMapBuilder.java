package com.xperience.aem.xpbootstrap.core.widgets.include.impl;

import static com.xperience.aem.xpbootstrap.core.widgets.include.impl.IncludeDecoratorFilterImpl.PREFIX;
import static com.xperience.aem.xpbootstrap.core.widgets.include.impl.IncludeDecoratorFilterImpl.REQ_ATTR_NAMESPACE;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.contains;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;
import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.substringAfter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class NamespaceDecoratedValueMapBuilder {
	
	private final static Logger logger = LoggerFactory.getLogger(NamespaceDecoratedValueMapBuilder.class);

    private final SlingHttpServletRequest request;

    private final Map<String,Object> copyMap;
    private final String[] namespacedProperties;

    static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("(\\$\\{\\{([a-zA-Z0-9]+?)(:(.+?))??\\}\\})+?");
    static final Pattern PLACEHOLDER_TYPE_HINTED_PATTERN = Pattern.compile("(.*)\\$\\{\\{(\\(([a-zA-Z]+)\\)){1}([a-zA-Z0-9]+)(:(.+))?\\}\\}(.*)?");
    
    public NamespaceDecoratedValueMapBuilder(SlingHttpServletRequest request, Resource resource, String[] namespacedProperties) {
        this.request = request;
        this.copyMap = new HashMap<>(resource.getValueMap());
        this.namespacedProperties = Optional.ofNullable(namespacedProperties)
                .map(array -> Arrays.copyOf(array, array.length))
                .orElse(new String[0]);
        logger.debug("namespacedProperties : {}", namespacedProperties.toString());
        this.applyDynamicVariables();
        this.applyNameSpacing();
    }

    private void applyNameSpacing() {
   	 logger.info("START - applyNameSpacing");
        if (request.getAttribute(REQ_ATTR_NAMESPACE) != null) {
            for (String namespacedProp : namespacedProperties) {
                if (copyMap.containsKey(namespacedProp)) {
					logger.debug("namespacedProp : {}", namespacedProp);
                    String originalValue = copyMap.get(namespacedProp).toString();
                    final String adjusted;

                    logger.debug("originalValue : {}", originalValue);
                    String namespace = request.getAttribute(REQ_ATTR_NAMESPACE).toString();
                    final boolean containsDotSlash = contains(originalValue, "./");
                    logger.debug("namespace : {}", namespace);
                    if (containsDotSlash) {
                        String extracted = substringAfter(originalValue, "./");
                        adjusted = "./" + namespace + "/" + extracted;
                    } else {
                        adjusted = namespace + "/" + originalValue;
                    }
                    logger.debug("adjusted : {}", adjusted);
                    this.copyMap.put(namespacedProp, adjusted);

                }
            }
        }
        logger.info("END - applyNameSpacing");

    }
    
    public ValueMap build(){
        return new ValueMapDecorator(copyMap);
    }
    
    private void applyDynamicVariables() {
    	 logger.info("START - applyDynamicVariables");
        for(Iterator<Map.Entry<String,Object>> iterator = this.copyMap.entrySet().iterator(); iterator.hasNext();){
    
            Map.Entry<String,Object> entry = iterator.next();
            
            if(entry.getValue() instanceof String) {
                final Object filtered = filter(entry.getValue().toString(), this.request);
                logger.debug("dynamic variable : {}", filtered.toString());
                copyMap.put(entry.getKey(), filtered);
            }
            
        }
   	 logger.info("END - applyDynamicVariables");
    }
    
    
    private Object filter(String value, SlingHttpServletRequest request) {
        Object filtered = applyTypeHintedPlaceHolders(value, request);
        
        if(filtered != null){
            return filtered;
        }
        
        return applyPlaceHolders(value, request);
    }
    
    private Object applyTypeHintedPlaceHolders(String value, SlingHttpServletRequest request) {
        Matcher matcher = PLACEHOLDER_TYPE_HINTED_PATTERN.matcher(value);
    
        if (matcher.find()) {
        
            String prefix = matcher.group(1);
            String typeHint = matcher.group(3);
            String paramKey = matcher.group(4);
            String defaultValue = matcher.group(6);
            String suffix = matcher.group(7);
    
            String requestParamValue = (request.getAttribute(PREFIX + paramKey) != null) ? request.getAttribute(PREFIX + paramKey).toString() : null;
            String chosenValue = defaultString(requestParamValue, defaultValue);
            String finalValue = defaultIfEmpty(prefix, EMPTY) + chosenValue + defaultIfEmpty(suffix, EMPTY);
        
            return isNotEmpty(typeHint) ? castTypeHintedValue(typeHint, finalValue) : finalValue;
        }
    
        return null;
    }

    private String applyPlaceHolders(String value, SlingHttpServletRequest request) {
        Matcher matcher = PLACEHOLDER_PATTERN.matcher(value);
        StringBuffer buffer = new StringBuffer();
        
        while (matcher.find()) {
            
            String paramKey = matcher.group(2);
            String defaultValue = matcher.group(4);
            
            String requestParamValue = (request.getAttribute(PREFIX + paramKey) != null) ? request.getAttribute(PREFIX + paramKey).toString() : null;
            String chosenValue = defaultString(requestParamValue, defaultValue);
            
            if(chosenValue == null){
                chosenValue = StringUtils.EMPTY;
            }
            
            matcher.appendReplacement(buffer, chosenValue);
            
        }
        
        matcher.appendTail(buffer);
        
        return buffer.toString();
    }
    
    private Object castTypeHintedValue(String typeHint, String chosenValue) {
    
        final Class<?> clazz;
    
        switch(typeHint.toLowerCase()){
            case "boolean":
                clazz = Boolean.class;
                break;
            case "long":
                clazz = Long.class;
                break;
            case "double":
                clazz = Double.class;
                break;
            default:
                clazz = String.class;
                break;
        }
        
        return TypeUtil.toObjectType(chosenValue, clazz);
    }
}
