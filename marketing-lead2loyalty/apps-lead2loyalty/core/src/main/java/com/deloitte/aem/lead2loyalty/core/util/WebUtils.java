package com.deloitte.aem.lead2loyalty.core.util;

import org.apache.commons.io.IOUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebUtils {
    private static final Logger logger = LoggerFactory.getLogger(WebUtils.class);
    public static JSONObject getRequestParam(SlingHttpServletRequest request) {
        JSONObject json = null;
        try{
            String requestData = IOUtils.toString(request.getReader());
            json = new JSONObject(requestData);
        } catch(Exception e) {
            logger.error("Exception");
        }
        return json;
    }
}
