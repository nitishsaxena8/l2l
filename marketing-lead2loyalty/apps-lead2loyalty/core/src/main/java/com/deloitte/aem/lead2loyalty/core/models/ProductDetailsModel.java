package com.deloitte.aem.lead2loyalty.core.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

@Model(adaptables = { SlingHttpServletRequest.class,
		Resource.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)

@Exporter(selector = "modal", name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION, options = {
		@ExporterOption(name = "MapperFeature.SORT_PROPERTIES_ALPHABETICALLY", value = "true"),
		@ExporterOption(name = "SerializationFeature.WRITE_DATES_AS_TIMESTAMPS", value = "false") })

public class ProductDetailsModel {

	@ValueMapValue
	private String subtitle;

	@ValueMapValue
	private String title;

	@ValueMapValue
	private String productid;

	@ValueMapValue
	private String pricingTitle;

	@ValueMapValue
	private String pricingDesc;

	@ValueMapValue
	private String pricingCtaLabel;

	@ValueMapValue
	private String pricingCtaLink;

	@ValueMapValue
	private String pricingCtaTarget;

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

	List<CtaModel> ctaModelList = new ArrayList<>();

	@PostConstruct
	protected void init() {

		if (ctaButton != null) {
			Iterator<Resource> ctaResourceItr = ctaButton.getChildren().iterator();
			while (ctaResourceItr.hasNext()) {
				Resource ctaResource = ctaResourceItr.next();
				CtaModel ctaModel = ctaResource.adaptTo(CtaModel.class);
				ctaModelList.add(ctaModel);
			}
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

	public String getPricingTitle() {
		return pricingTitle;
	}

	public String getPricingDesc() {
		return pricingDesc;
	}

	public String getPricingCtaLabel() {
		return pricingCtaLabel;
	}

	public String getPricingCtaLink() {
		return pricingCtaLink;
	}

	public String getPricingCtaTarget() {
		return pricingCtaTarget;
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

	public List<CtaModel> getCtaModelList() {
		return ctaModelList;
	}

	public void setCtaModelList(List<CtaModel> ctaModelList) {
		this.ctaModelList = ctaModelList;
	}
}
