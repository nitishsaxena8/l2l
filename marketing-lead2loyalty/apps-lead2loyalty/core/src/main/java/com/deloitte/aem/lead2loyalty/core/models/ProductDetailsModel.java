package com.deloitte.aem.lead2loyalty.core.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.jcr.Node;
import javax.jcr.Value;

import com.day.cq.wcm.api.Page;
import com.deloitte.aem.lead2loyalty.core.util.WebUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceUtil;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.ExporterOption;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.adobe.cq.export.json.ExporterConstants;

@Model(adaptables = { SlingHttpServletRequest.class,
		Resource.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)

@Exporter(selector = "modal", name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION, options = {
		@ExporterOption(name = "MapperFeature.SORT_PROPERTIES_ALPHABETICALLY", value = "true"),
		@ExporterOption(name = "SerializationFeature.WRITE_DATES_AS_TIMESTAMPS", value = "false") })

public class ProductDetailsModel {

	@Inject
	private Page currentPage;

	@ValueMapValue
	private String subtitle;

	@ValueMapValue
	private String title;

	@ValueMapValue
	private String productid;

	@ValueMapValue
	private String atmTextHtml;

	@ValueMapValue
	private String shareCtaLabel;

	@ValueMapValue
	private String shareCtaLink;

	@ValueMapValue
	private String modalTitle;

	@ValueMapValue
	private String modalDesc;

	@ValueMapValue
	private String nameFieldLabel;

	@ValueMapValue
	private String emailIdFieldLabel;

	@ValueMapValue
	private String modalCtaLabel;

	@Inject
	@Via("resource")
	private Resource ctaButton;

	private String bookmarkClassName;
	@Self
	private SlingHttpServletRequest request;
	@SlingObject
	private ResourceResolver resourceResolver;

	@PostConstruct
	protected void init() {

		String emailId = WebUtils.getSpecificCookie(request, "userEmail");
		String userPath = "/var/lead2loyalty-users/" + emailId;
		Resource resource = resourceResolver.getResource(userPath);
		bookmarkClassName = "fa fa-bookmark-o";
		try{
			if(!ResourceUtil.isNonExistingResource(resource)) {
				Node node = resource.adaptTo(Node.class);
				if(node.hasProperty("bookmarks")) {
					Value[] valueArray = node.getProperty("bookmarks").getValues();
					List<Value> valueList = new ArrayList<>(Arrays.asList(valueArray));
					for(Value value : valueList){
						if(value.getString().equalsIgnoreCase(currentPage.getPath())){
							bookmarkClassName = "fa fa-bookmark";
							break;
						}
					}
				}
			}
		} catch(Exception e) {
			// Handle exception
		}

	}

	public Resource getCtaButton() {
		return ctaButton;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public String getTitle() {
		return title;
	}

	public String getProductId() {
		return productid;
	}

	public String getDescription() {
		return atmTextHtml;
	}

	public String getShareCtaLabel() {
		return shareCtaLabel;
	}

	public String getShareCtaLink() {
		return shareCtaLink;
	}

	public String getModalTitle() {
		return modalTitle;
	}

	public String getModalDesc() {
		return modalDesc;
	}

	public String getNameFieldLabel() {
		return nameFieldLabel;
	}

	public String getEmailIdFieldLabel() {
		return emailIdFieldLabel;
	}

	public String getModalCtaLabel() {
		return modalCtaLabel;
	}

	public String getBookmarkClassName() {
		return bookmarkClassName;
	}
}
