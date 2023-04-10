package com.deloitte.aem.lead2loyalty.core.servlets;

import com.deloitte.aem.lead2loyalty.core.constants.ApplicationConstants;
import com.deloitte.aem.lead2loyalty.core.service.utility.Lead2loyaltyService;
import com.deloitte.aem.lead2loyalty.core.util.WebUtils;
import org.apache.commons.io.IOUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceUtil;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.json.JSONObject;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.*;
import javax.servlet.Servlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component(
        service= Servlet.class,
        property={
                Constants.SERVICE_DESCRIPTION + "=Bookmarks Servlet",
                "sling.servlet.methods=GET",
                "sling.servlet.paths=" + "/bin/manageBookmarks",
                "sling.servlet.extensions=" + "json"
        }
)
public class BookmarkServlet extends SlingAllMethodsServlet {
    private static final long serialVersionUID = 1L;
    private final transient Logger logger = LoggerFactory.getLogger(getClass());
    @Reference
    private transient Lead2loyaltyService lead2loyaltyService;
    @Override
    protected void doPost(final SlingHttpServletRequest request,
                          final SlingHttpServletResponse response) throws IOException {

        String requestBody = IOUtils.toString(request.getReader());
        ResourceResolver resourceResolver = lead2loyaltyService.getServiceResolver();
        Session session = resourceResolver.adaptTo(Session.class);

        try{
            JSONObject json = WebUtils.getRequestParam(requestBody);
            String email = json.get("email").toString();
            String pagePath = json.get("pagePath").toString();
            Node varNode = session.getNode(ApplicationConstants.VAR_NODE_PATH);
            if (varNode.hasNode("lead2loyalty-users/" + email) && !ResourceUtil.isNonExistingResource(resourceResolver.getResource(pagePath))) {
                Node node = resourceResolver.getResource(ApplicationConstants.LOYALTY_USER_PATH + email).adaptTo(Node.class);
                if(json.get("action").toString().equalsIgnoreCase("add")) {
                    addAsBookmark(node, session, response, pagePath);
                } else {
                    removeFromBookmark(node, session, response, pagePath);
                }
            }
        } catch(Exception e) {
            logger.error("Exception");
        }
    }

    private void addAsBookmark(Node node, Session session, SlingHttpServletResponse response, String pagePath) throws RepositoryException, IOException {

        PrintWriter out = response.getWriter();
        ValueFactory valueFactory = session.getValueFactory();
        Value value = valueFactory.createValue(pagePath);

        if(node.hasProperty(ApplicationConstants.BOOKMARKS_PROPERTY)) {
            Value[] valueArray = node.getProperty(ApplicationConstants.BOOKMARKS_PROPERTY).getValues();
            List<Value> valueList = new ArrayList<>(Arrays.asList(valueArray));
            if(!valueList.contains(value)){
                valueList.add(value);
                node.setProperty(ApplicationConstants.BOOKMARKS_PROPERTY, valueList.toArray(new Value[0]));
                session.save();
                out.println("Page bookmarked!!!");
            } else {
                out.println("Page is already bookmarked.");
            }
        } else {
            List<Value> valueList = new ArrayList<>();
            valueList.add(value);
            node.setProperty(ApplicationConstants.BOOKMARKS_PROPERTY, valueList.toArray(new Value[0]));
            session.save();
            out.println("Page bookmarked!!!");
        }
    }

    private void removeFromBookmark(Node node, Session session, SlingHttpServletResponse response, String pagePath) throws RepositoryException, IOException {

        PrintWriter out = response.getWriter();
        ValueFactory valueFactory = session.getValueFactory();
        Value value = valueFactory.createValue(pagePath);
        if(node.hasProperty(ApplicationConstants.BOOKMARKS_PROPERTY)) {
            Value[] valueArray = node.getProperty(ApplicationConstants.BOOKMARKS_PROPERTY).getValues();
            List<Value> valueList = new ArrayList<>(Arrays.asList(valueArray));
            if(valueList.contains(value)) {
                valueList.remove(value);
                node.setProperty(ApplicationConstants.BOOKMARKS_PROPERTY, valueList.toArray(new Value[0]));
                session.save();
                out.println("Page removed from bookmark list.");
            } else {
                out.println("Page doesn't exits in bookmark list");
            }
        }
    }
}
