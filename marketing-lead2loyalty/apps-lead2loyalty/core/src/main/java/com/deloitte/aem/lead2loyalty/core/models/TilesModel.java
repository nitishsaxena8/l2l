package com.deloitte.aem.lead2loyalty.core.models;

import com.adobe.cq.export.json.ExporterConstants;
import com.day.cq.commons.jcr.JcrConstants;
import com.deloitte.aem.lead2loyalty.core.util.ServiceUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.ExporterOption;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.settings.SlingSettingsService;

import javax.annotation.PostConstruct;
import java.util.*;

@Model(adaptables = { Resource.class, SlingHttpServletRequest.class},
        adapters = TilesModel.class,
        resourceType = "lead2loyalty/components/content/organisms/tiles",
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)

@Exporter(selector = "modal", name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION,
        options = {
                @ExporterOption(name = "MapperFeature.SORT_PROPERTIES_ALPHABETICALLY", value = "true"),
                @ExporterOption(name = "SerializationFeature.WRITE_DATES_AS_TIMESTAMPS", value="false")
        })

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TilesModel {
	
	@ValueMapValue
	private String tileHeading;
	
	@ValueMapValue
	private String ctaLabel;

	@ValueMapValue
	private String ctaLink;

	@ValueMapValue
	private String ctaTarget;

    @ValueMapValue
    private String authorType;
	
	@SlingObject
	private ResourceResolver resourceResolver;

	@OSGiService
	private SlingSettingsService settingsService;

    @SlingObject
    Resource  currentResource;

    @SlingObject
    SlingHttpServletRequest request;

    private List<TileItem> tileListItems;

    private List<TileItem> tilePathListItems;

    private List<TileItem> tileListItemsList=new ArrayList<TileItem>();

    @PostConstruct
    protected void init() {
        if (Objects.requireNonNull(authorType).equals("manual")) {
            Resource tileListItems = currentResource.getChild("tileListItems");
            if (tileListItems != null) {
                Iterator<Resource> tileListItemsItr = tileListItems.listChildren();
                while (tileListItemsItr.hasNext()) {
                    Resource tileListItemsRes = tileListItemsItr.next();
                    TileItem tileListItem = tileListItemsRes.adaptTo(TileItem.class);
                    tileListItemsList.add(tileListItem);
                }
            }
            setTileListItems(tileListItemsList);
        }
        else if (Objects.requireNonNull(authorType).equals("path")) {
            Resource tilePathListItems = currentResource.getChild("tilePathListItems");
            if (tilePathListItems != null) {
                Iterator<Resource> tilePathListItemsItr = tilePathListItems.listChildren();
                while (tilePathListItemsItr.hasNext()) {
                    Resource tilePathListItemsRes = tilePathListItemsItr.next();
                    TileItem tileListItem = tilePathListItemsRes.adaptTo(TileItem.class);
                    String pathValue = Objects.requireNonNull(tileListItem).getProductPathValue();
                    Resource pageResource = Objects.requireNonNull(resourceResolver.getResource(pathValue));
                    Resource jcrResource = Objects.requireNonNull(pageResource).getChild(JcrConstants.JCR_CONTENT);

                    ValueMap jcrProperties = Objects.requireNonNull(jcrResource).getValueMap();
                    tileListItem.setProductTitle(jcrProperties.get(JcrConstants.JCR_TITLE, String.class));
                    tileListItem.setProductDescription(jcrProperties.get(JcrConstants.JCR_DESCRIPTION, String.class));
                    tileListItem.setProductScrollId(pageResource.getName());
                    ValueMap imageProperties = Objects.requireNonNull(jcrResource.getChild("image")).getValueMap();
                    tileListItem.setProductImageSource(imageProperties.get("fileReference", String.class));

                    tileListItemsList.add(tileListItem);
                }
            }
            setTilePathListItems(tileListItemsList);
        }
    }

    public List<TileItem> getTileListItems() {
        return new ArrayList<> (tileListItems);
    }

    public void setTileListItems(List<TileItem> tileListItems) {
        tileListItems = new ArrayList<>(tileListItems);
        this.tileListItems = Collections.unmodifiableList(tileListItems);
    }

    public List<TileItem> getTilePathListItems() {
        return new ArrayList<> (tilePathListItems);
    }

    public void setTilePathListItems(List<TileItem> tileListItems) {
        tileListItems = new ArrayList<>(tileListItems);
        this.tilePathListItems = Collections.unmodifiableList(tileListItems);
    }

    public String getAuthorType() {
        return authorType;
    }

    public String getTileHeading() {
		return tileHeading;
	}
    
    public String getCtaLabel() {
		return ctaLabel;
	}
    
    public String getCtaLink() {
    	return ServiceUtils.getLink(resourceResolver, ctaLink, settingsService);
	}
    
    public String getCtaTarget() {
		return ctaTarget;
	}
    
}
