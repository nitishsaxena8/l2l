package com.deloitte.aem.lead2loyalty.core.servlets;

import com.deloitte.aem.lead2loyalty.core.beans.Login;
import com.deloitte.aem.lead2loyalty.core.beans.User;
import com.deloitte.aem.lead2loyalty.core.util.Lead2loyaltyException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.*;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.version.VersionException;
import javax.servlet.Servlet;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Iterator;

@Component(
		service=Servlet.class,
		property={
				Constants.SERVICE_DESCRIPTION + "=Custom Servlet",
				"sling.servlet.methods=POST",
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

 		if(response == null) {
			resp.setStatus(SlingHttpServletResponse.SC_BAD_REQUEST);
			resp.getWriter().print("{\"response message\" : \" Servlet Called\"}");
		} else {
			resp.setStatus(SlingHttpServletResponse.SC_OK);
			resp.getWriter().print(response);
		}

		logger.error("Exiting UserServlet >>>>>>>");
	}

	private String signUpUser(String userStr, SlingHttpServletRequest req) {
		ObjectMapper responseObjectMapper = new ObjectMapper();
		try {
			JSONObject data = new JSONObject(userStr);
			if (data != null) {

				ResourceResolver resourceResolver = req.getResourceResolver();
				Session session = resourceResolver.adaptTo(Session.class);

				Node varNode = session.getNode("/var");

				Node loyaltyNode = null;
				if (varNode != null) {
					if(varNode.hasNode("lead2loyalty-users")) {
						loyaltyNode = varNode.getNode("lead2loyalty-users");
					} else {
						loyaltyNode = varNode.addNode("lead2loyalty-users");
					}
				}

				if (loyaltyNode.hasNode(data.getString("Email"))) {
					return responseObjectMapper.writeValueAsString(new Lead2loyaltyException("User already exist !!!", 1001));
				} else {
					Node userNode = loyaltyNode.addNode(data.getString("Email"));

					Iterator keys = data.keys();
					while (keys.hasNext()) {
						String key = (String) keys.next();
						String value = data.getString(key);

						userNode.setProperty(key, value);
					}

					if (data.has("Password")) {
						data.remove("Password");
					}

					session.save();
					session.refresh(true);

					return data.toString();
				}
			}
		} catch (JSONException e) {
			logger.error("JSONException Exception in UserServlet class {}", e);
		} catch (PathNotFoundException e) {
			logger.error("PathNotFoundException Exception in UserServlet class {}", e);
		} catch (ItemExistsException e) {
			logger.error("ItemExistsException Exception in UserServlet class {}", e);
		} catch (VersionException e) {
			logger.error("VersionException Exception in UserServlet class {}", e);
		} catch (ConstraintViolationException e) {
			logger.error("ConstraintViolationException Exception in UserServlet class {}", e);
		} catch (LockException e) {
			logger.error("LockException Exception in UserServlet class {}", e);
		} catch (RepositoryException e) {
			logger.error("RepositoryException Exception in UserServlet class {}", e);
		} catch (JsonProcessingException e) {
			logger.error("JsonProcessingException Exception in UserServlet class {}", e);
		}
		return null;
	}

	private String loginUser(SlingHttpServletRequest req) {
		ObjectMapper responseObjectMapper = new ObjectMapper();
		StringBuffer jb = new StringBuffer();
		String line = null;
		try {
			BufferedReader reader = req.getReader();
			while ((line = reader.readLine()) != null)
				jb.append(line);
		} catch (Exception e) {

		}

		JSONObject loginJSONObject = new JSONObject(jb);

		if (loginJSONObject != null && loginJSONObject.has("username") && loginJSONObject.has("password")) {

			ResourceResolver resourceResolver = req.getResourceResolver();
			Session session = resourceResolver.adaptTo(Session.class);

			try {
				Node varNode = session.getNode("/var");
				if (varNode.hasNode("lead2loyalty-users/" + loginJSONObject.getString("username"))) {
					Node userNode = varNode.getNode("lead2loyalty-users/" + loginJSONObject.getString("username"));
					Resource resource = resourceResolver.getResource(userNode.getPath());

					if (StringUtils.equalsIgnoreCase(loginJSONObject.getString("username"), resource.getValueMap().get("email", String.class)) &&
							StringUtils.equalsIgnoreCase(loginJSONObject.getString("password"), resource.getValueMap().get("password", String.class))) {
						User user = resource.adaptTo(User.class);

						return responseObjectMapper.writeValueAsString(user);
					} else {
						return responseObjectMapper.writeValueAsString(new Lead2loyaltyException("Wrong username and password, please try again !!!", 1001));
					}
				} else {
					return responseObjectMapper.writeValueAsString(new Lead2loyaltyException("Enter a valid username !!!", 1001));
				}
			} catch (RepositoryException e) {
				logger.error("RepositoryException Exception in UserServlet class {}", e);
			} catch (JsonProcessingException e) {
				logger.error("JsonProcessingException Exception in UserServlet class {}", e);
			} catch (JSONException e) {
				logger.error("JSONException Exception in UserServlet class {}", e);
			}
		} else {
			return signUpUser(jb.toString(), req);
		}
		return null;
	}
}