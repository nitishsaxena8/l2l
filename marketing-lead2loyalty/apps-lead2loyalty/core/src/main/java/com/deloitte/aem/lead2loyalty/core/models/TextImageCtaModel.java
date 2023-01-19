package com.deloitte.aem.lead2loyalty.core.models;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.ExporterOption;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.adobe.cq.export.json.ExporterConstants;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

	public void setCtaModelList(List<CtaModel> ctaModelList) {
		this.ctaModelList = ctaModelList;
	}
}
