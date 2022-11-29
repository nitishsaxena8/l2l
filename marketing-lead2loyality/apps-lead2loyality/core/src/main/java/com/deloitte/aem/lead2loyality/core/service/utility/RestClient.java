package com.deloitte.aem.lead2loyality.core.service.utility;

import java.io.IOException;
import java.util.Map;

import org.apache.http.MethodNotSupportedException;

import com.deloitte.aem.lead2loyality.core.beans.BaseRequest;
import com.deloitte.aem.lead2loyality.core.beans.APIResponse;

/**
 * The Interface RestClient.
 */
public interface RestClient {

	/**
	 * Send request with body string.
	 *
	 * @param request   the request
	 * @param headerMap the header map
	 * @return the string
	 * @throws IOException 
	 * @throws MethodNotSupportedException 
	 */
	APIResponse sendRequest(final BaseRequest request, final Map<String, String> headerMap) throws IOException, MethodNotSupportedException;

}
