package com.deloitte.aem.lead2loyalty.core.models;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import com.day.cq.commons.jcr.JcrConstants;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.ExporterOption;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.adobe.cq.export.json.ExporterConstants;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Model(adaptables = { SlingHttpServletRequest.class,
		Resource.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)

@Exporter(selector = "modal", name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION, options = {
		@ExporterOption(name = "MapperFeature.SORT_PROPERTIES_ALPHABETICALLY", value = "true"),
		@ExporterOption(name = "SerializationFeature.WRITE_DATES_AS_TIMESTAMPS", value = "false") })

public class TextImageCtaModel {

	@ValueMapValue
	private String title;

	@ValueMapValue
	private String atmTextHtml;

	@ValueMapValue
	private String fileReference;
	
	@ValueMapValue
	private String imageAlt;

	@ValueMapValue
	private String imgAlignment;
	
	@ValueMapValue
	private String productCategory;

	@ValueMapValue
	private String productScrollId;

	@ValueMapValue
	private String pagePath;

	@ValueMapValue
	private String authorType;

	@SlingObject
	private ResourceResolver resourceResolver;

	@Inject
	@Via("resource")
	private Resource ctaButton;

	List<CtaModel> ctaModelList = new ArrayList<>();

	@PostConstruct
	protected void init() {

		if (ctaButton != null){
			Iterator<Resource> ctaResourceItr = ctaButton.getChildren().iterator();
			while (ctaResourceItr.hasNext()) {
				Resource ctaResource = ctaResourceItr.next();
				CtaModel ctaModel = ctaResource.adaptTo(CtaModel.class);
				ctaModel.setProductCategory(productCategory);
				ctaModelList.add(ctaModel);
			}
		}
		if (Objects.requireNonNull(authorType).equals("path")) {
			if (!productCategory.isEmpty() && !pagePath.isEmpty()) {
				Resource pageResource = Objects.requireNonNull(resourceResolver.getResource(pagePath));
				Resource jcrResource = Objects.requireNonNull(pageResource).getChild(JcrConstants.JCR_CONTENT);

				ValueMap jcrProperties = Objects.requireNonNull(jcrResource).getValueMap();
				setTitle(jcrProperties.get(JcrConstants.JCR_TITLE, String.class));
				setAtmTextHtml(jcrProperties.get(JcrConstants.JCR_DESCRIPTION, String.class));
				setProductScrollId(pageResource.getName());
				setImageAlt(pageResource.getName());
				ValueMap imageProperties = Objects.requireNonNull(jcrResource.getChild("image")).getValueMap();
				setFileReference(imageProperties.get("fileReference", String.class));
			}
		}

	}

	public Resource getCtaButton() {
		return ctaButton;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return atmTextHtml;
	}

	public String getImage() {
		return fileReference;
	}
	
	public String getImageAlt() {
		return imageAlt;
	}

	public String getImgAlignment() {
		return imgAlignment;
	}

	public List<CtaModel> getCtaModelList() {
		return ctaModelList;
	}

	public String getProductScrollId() {
		return productScrollId;
	}

	public void setCtaModelList(List<CtaModel> ctaModelList) {
		this.ctaModelList = ctaModelList;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setAtmTextHtml(String atmTextHtml) {
		this.atmTextHtml = atmTextHtml;
	}

	public void setFileReference(String fileReference) {
		this.fileReference = fileReference;
	}

	public void setImageAlt(String imageAlt) {
		this.imageAlt = imageAlt;
	}

	public void setProductScrollId(String productScrollId) {
		this.productScrollId = productScrollId;
	}
}
