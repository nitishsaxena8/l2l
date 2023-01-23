package com.deloitte.aem.lead2loyalty.core.servlets;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import java.io.IOException;

@Component(
	service=Servlet.class,
	property={
		Constants.SERVICE_DESCRIPTION + "=Custom Servlet",
		"sling.servlet.methods=" + HttpConstants.METHOD_POST,
		"sling.servlet.paths=" + "/bin/user"
	}
)
public class UserServlet extends SlingAllMethodsServlet {

	private static final long serialVersionUID = 1L;

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	protected void doPost(final SlingHttpServletRequest req,
		final SlingHttpServletResponse resp) throws IOException {
		logger.debug("Entering UserServlet >>>>>>>");
		String dataXml = req.getParameter("dataXml");
		logger.debug("User Data = {}",dataXml);
		try {
			JSONObject data = new JSONObject(dataXml);
			if (data.has("afData") && data.getJSONObject("afData").has("afBoundData") &&
					data.getJSONObject("afData").getJSONObject("afBoundData").has("data") ) {
				JSONObject userObject = data.getJSONObject("afData").getJSONObject("afBoundData").getJSONObject("data");
				logger.error(userObject.toString());
			}
		} catch (JSONException e) {
			logger.error("Error in UserServlet class");
		}

		resp.setStatus(SlingHttpServletResponse.SC_OK);
		resp.setContentType("application/json;charset=UTF-8");
		resp.getWriter().print("{\"response message\" : \" Servlet Called\"}");
		logger.error("Entering UserServlet >>>>>>>");
	}
}