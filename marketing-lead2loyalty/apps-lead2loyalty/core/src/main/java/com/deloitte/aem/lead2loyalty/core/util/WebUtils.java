package com.deloitte.aem.lead2loyalty.core.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class WebUtils {
    private static final Logger logger = LoggerFactory.getLogger(WebUtils.class);

    public static JSONObject getRequestParam(String  requestBody) {
        JSONObject json = null;
        try {
            json = new JSONObject(requestBody);
        } catch(Exception e) {
            logger.error("Exception");
        }
        return json;
    }

    /**
     * Gets the specific cookie.
     *
     * @param request    the request
     * @param cookieName the cookie name
     * @return the specific cookie
     */
    public static String getSpecificCookie(SlingHttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        String defaultValue = StringUtils.EMPTY;
        if (null != cookies) {
            for (Cookie cookie : cookies) {
                try {
                    if (cookieName.equals(cookie.getName()))
                        return URLDecoder.decode((cookie.getValue()), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    logger.warn("getSpecificCookie UnsupportedEncodingException: {0}", e);
                }
            }
        }
        logger.debug("Specific Cookie : {}", defaultValue);
        return (defaultValue);
    }
}
