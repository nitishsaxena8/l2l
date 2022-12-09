package com.deloitte.aem.lead2loyalty.core.models;

import com.adobe.cq.export.json.ExporterConstants;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.ExporterOption;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@Model(adaptables = { Resource.class, SlingHttpServletRequest.class},
        resourceType = "lead2loyalty/components/content/organisms/tiles",
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)

@Exporter(selector = "modal", name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION,
        options = {
                @ExporterOption(name = "MapperFeature.SORT_PROPERTIES_ALPHABETICALLY", value = "true"),
                @ExporterOption(name = "SerializationFeature.WRITE_DATES_AS_TIMESTAMPS", value="false")
        })

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TilesModel {

    @SlingObject
    Resource  currentResource;

    @SlingObject
    SlingHttpServletRequest request;

    private List<TileItem> tileListItems;

    private List<TileItem> tileListItemsList=new ArrayList<TileItem>();

    @PostConstruct
    protected void init(){
        Resource  tileListItems=  currentResource.getChild("tileListItems");
        if(tileListItems!=null) {
            Iterator<Resource> tileListItemsItr = tileListItems.listChildren();
            while (tileListItemsItr.hasNext()) {
                Resource tileListItemsRes = tileListItemsItr.next();
                TileItem tileListItem = tileListItemsRes.adaptTo(TileItem.class);
                tileListItemsList.add(tileListItem);
            }
        }
        setTileListItems(tileListItemsList);
    }

    public List<TileItem> getTileListItems() {
        return new ArrayList<> (tileListItems);
    }

    public void setTileListItems(List<TileItem> tileListItems) {
        tileListItems = new ArrayList<>(tileListItems);
        this.tileListItems = Collections.unmodifiableList(tileListItems);
    }

}
