package com.deloitte.aem.lead2loyalty.core.servlets;

import com.deloitte.aem.lead2loyalty.core.beans.Login;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
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
import java.io.BufferedReader;
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

		String response = loginUser(req);

		resp.setContentType("application/json;charset=UTF-8");
		if(response == null) {
			resp.setStatus(SlingHttpServletResponse.SC_BAD_REQUEST);
			resp.getWriter().print("{\"response message\" : \" Servlet Called\"}");
		} else {
			resp.setStatus(SlingHttpServletResponse.SC_OK);
			resp.getWriter().print(response);
		}

		logger.error("Entering UserServlet >>>>>>>");
	}

	private String signUpUser(String dataXml) {
		try {
			JSONObject data = new JSONObject(dataXml);
			if (data.has("afData") && data.getJSONObject("afData").has("afBoundData") &&
					data.getJSONObject("afData").getJSONObject("afBoundData").has("data") ) {
				JSONObject userObject = data.getJSONObject("afData").getJSONObject("afBoundData").getJSONObject("data");
				logger.error(userObject.toString());

				if (userObject.has("password")) {
					userObject.remove("password");
				}
				return userObject.toString();
			}
		} catch (JSONException e) {
			logger.error("Error in UserServlet class");
		}
		return null;
	}

	private String loginUser(SlingHttpServletRequest req) {
		StringBuffer jb = new StringBuffer();
		String line = null;
		try {
			BufferedReader reader = req.getReader();
			while ((line = reader.readLine()) != null)
				jb.append(line);
		} catch (Exception e) {

		}

		ObjectMapper mapper = new ObjectMapper();
		Login login = null;
		try {
			login = mapper.readValue(jb.toString(), Login.class);
		} catch (IOException e) {
			logger.error("Error in UserServlet class login() {}", e);
		}
		if (StringUtils.isNotEmpty(login.getUsername()) && StringUtils.isNotEmpty(login.getPassword())) {
			if ("test@gmail.com".equalsIgnoreCase(login.getUsername()) && "test".equals(login.getPassword())) {
				return "{\"firstName\":\"John\",\"lastName\":\"carter\",\"website\":\"www.google.com\",\"role\":\"leadership\",\"phone\":\"1234567809\",\"organization\":\"Test\",\"updates\":\"true\",\"email\":\"test@gmail.com\"}";
			}
		} else {
			String dataXml = req.getParameter("dataXml");
			logger.debug("User Data = {}",dataXml);
			return signUpUser(dataXml);
		}
		return null;
	}
}