package com.xperience.aem.xpbootstrap.core.service.utility.impl;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Map;

import javax.net.ssl.SSLContext;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.MethodNotSupportedException;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;
import org.apache.sling.api.SlingHttpServletResponse;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xperience.aem.xpbootstrap.core.beans.APIResponse;
import com.xperience.aem.xpbootstrap.core.beans.BaseRequest;
import com.xperience.aem.xpbootstrap.core.service.utility.RestClient;
import com.xperience.aem.xpbootstrap.core.service.utility.configs.RestClientConfigurations;
import com.xperience.aem.xpbootstrap.core.util.RestServiceConstants;
import com.xperience.aem.xpbootstrap.core.util.ADLCUtility;

/**
 * The Class RestServiceImpl. The Rest Client uses
 * PoolingHttpClientConnectionManager to create the ClosableHttpClient THe
 * Connection is set with max connections with 200
 */
@Component(service = RestClient.class, immediate = true)
@Designate(ocd = RestClientConfigurations.class)
public class RestClientImpl implements RestClient {

    /**
     * The Constant START_REQUEST.
     */
    private static final String START_REQUEST = "**************************** Start Request ********************* ";

    /**
     * The Constant TOTAL_TIME_TAKEN_FOR_API_REQUEST_MS.
     */
    private static final String TOTAL_TIME_TAKEN_FOR_API_REQUEST_MS = "Total Time Taken for API Request - {} ms";

    /**
     * The Constant END_REQUEST.
     */
    private static final String END_REQUEST = "**************************** End Request *********************";

    /**
     * The Constant EXCEPTION_OCCURRED_IN_REST_CLIENT.
     */
    private static final String EXCEPTION_OCCURRED_IN_REST_CLIENT = "Exception occurred in Rest Client ";

    /**
     * The Constant EXIT_SEND_REQUEST_WITH_BODY.
     */
    private static final String EXIT_SEND_REQUEST_WITH_BODY = "Exit sendRequestWithBody";

    /**
     * The Constant LOG.
     */
    private static final Logger LOG = LoggerFactory.getLogger(RestClientImpl.class);

    /**
     * The http client.
     */
    private CloseableHttpClient httpClient;

    /**
     * The cm.
     */
    private PoolingHttpClientConnectionManager cm;

    /**
     * Gets the response data.
     *
     * @param httpResponse the http response
     * @return the response data
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static final APIResponse getResponse(HttpResponse httpResponse) throws IOException {
        APIResponse response = new APIResponse();
        String responseData = StringUtils.EMPTY;
        if (httpResponse != null) {
            LOG.debug("Inside httpResponse Not Null");
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            LOG.debug("Status code : {}", statusCode);
            response.setStatusCode(statusCode);
            if (statusCode != SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR
                    && statusCode != SlingHttpServletResponse.SC_NOT_FOUND && null != httpResponse.getEntity()) {
                responseData = EntityUtils.toString(httpResponse.getEntity());
                LOG.debug("Response : {}", responseData);
            }
        }
        response.setResponse(responseData);
        return response;
    }

    @Override
    public final APIResponse sendRequest(final BaseRequest request, final Map<String, String> headerMap) throws IOException, MethodNotSupportedException {

        long lStartTime = ADLCUtility.getStartTime();
        LOG.debug(START_REQUEST);
        APIResponse response;
        HttpPost postMethod = null;
        HttpGet getMethod = null;
        HttpPatch patchMethod = null;
        HttpDelete deleteMethod = null;
        try {

            if ("POST".equals(request.gethttpMethodType().toString())) {
                StringEntity requestEntity = new StringEntity(request.getData(), ContentType.APPLICATION_JSON);
                LOG.debug("POST request with URL : {}, and request : {}", request.getUrl(), request.getData());
                postMethod = new HttpPost(request.getUrl());
                postMethod.setEntity(requestEntity);
                setHeader(postMethod, headerMap);
                response = getResponse(httpClient.execute(postMethod));
            } else if ("PATCH".equals(request.gethttpMethodType().toString())) {
                StringEntity requestEntity = new StringEntity(request.getData(), ContentType.APPLICATION_JSON);
                LOG.debug("PATCH request with URL : {}, and request : {}", request.getUrl(), request.getData());
                patchMethod = new HttpPatch(request.getUrl());
                patchMethod.setEntity(requestEntity);
                setHeader(patchMethod, headerMap);
                response = getResponse(httpClient.execute(patchMethod));
            } else if ("GET".equals(request.gethttpMethodType().toString())) {
                LOG.debug("GET request with URL : {}, and request : {}", request.getUrl(), request.getData());
                getMethod = new HttpGet(request.getUrl());
                setHeader(getMethod, headerMap);
                response = getResponse(httpClient.execute(getMethod));
			} else if ("DELETE".equals(request.gethttpMethodType().toString())) {
				LOG.debug("DETETE request with URL : {}, and request : {}", request.getUrl(), request.getData());
				deleteMethod = new HttpDelete(request.getUrl());
				setHeader(deleteMethod, headerMap);
				response = getResponse(httpClient.execute(deleteMethod));
			} else {
                throw new MethodNotSupportedException("Requested method is not supported");
            }
            LOG.debug(TOTAL_TIME_TAKEN_FOR_API_REQUEST_MS, ADLCUtility.logMethodTime(lStartTime));
            LOG.debug(END_REQUEST);
        } finally {
            releaseConnection(postMethod);
            releaseConnection(patchMethod);
            releaseConnection(getMethod);
            releaseConnection(deleteMethod);
        }

        LOG.trace(EXIT_SEND_REQUEST_WITH_BODY);
        return response;
    }

    /**
     * Sets the get header.
     *
     * @param httpMethod the Http method
     * @param headerMap  the headers map
     */
    private void setHeader(HttpRequestBase httpMethod, Map<String, String> headerMap) {
        if (headerMap != null && !headerMap.isEmpty()) {
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                httpMethod.addHeader(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * Releases Http connection
     *
     * @param httpMethod the Http method
     */
    private void releaseConnection(HttpRequestBase httpMethod) {

        if (null != httpMethod) {
            httpMethod.releaseConnection();
        }
    }

    /**
     * Activate.
     */
    @Activate
    public void activate(RestClientConfigurations configService) {
        LOG.debug("RestClientImpl Activated");

        final int requestTimeout = configService.getRequestTimeOut();
        final int socketTimeout = configService.getSocketTimeOut();
        final int connectTimeout = configService.getConnectionTimeOut();

        try {
            SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(new TrustStrategy() {
                @Override
                public boolean isTrusted(java.security.cert.X509Certificate[] chain, String authType)
                        throws CertificateException {
                    return true;
                }
            }).build();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, new String[]{"TLSv1.2"},
                    null, SSLConnectionSocketFactory.getDefaultHostnameVerifier());

            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register(RestServiceConstants.HTTPS, sslsf)
                    .register(RestServiceConstants.HTTP, new PlainConnectionSocketFactory()).build();
            // NOSONAR
            cm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            cm.setMaxTotal(500);
            // Increase default max connection per route to 20
            cm.setDefaultMaxPerRoute(100);

            RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.IGNORE_COOKIES)
                    .setSocketTimeout(socketTimeout * 1000).setConnectTimeout(connectTimeout * 1000)
                    .setConnectionRequestTimeout(requestTimeout * 1000).build();

            // Build the client.
            httpClient = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).setConnectionManager(cm)
                    .build();

        } catch (KeyManagementException e) {
            LOG.error("KeyManagementException", e);
        } catch (NoSuchAlgorithmException e) {
            LOG.error("NoSuchAlgorithmException", e);
        } catch (KeyStoreException e) {
            LOG.error("KeyStoreException", e);
        }
        LOG.debug("Exit RestClientImpl Activate");
    }

    /**
     * Deactivate.
     */
    @Deactivate
    public void deactivate() {
        LOG.debug("RestClientImpl Deactivated");
        try {
            if (null != httpClient) {
                httpClient.close();
            }
            if (null != cm) {
                cm.close();
            }
        } catch (IOException e) {
            LOG.error("IOException during closing of httpClient", e);
        }
    }

}
