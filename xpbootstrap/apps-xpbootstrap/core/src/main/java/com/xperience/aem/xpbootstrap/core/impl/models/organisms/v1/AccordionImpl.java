package com.xperience.aem.xpbootstrap.core.impl.models.organisms.v1;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ContainerExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.day.cq.wcm.api.designer.Style;
import com.xperience.aem.xpbootstrap.core.component.Utils.ComponentData;
import com.xperience.aem.xpbootstrap.core.component.internal.Heading;
import com.xperience.aem.xpbootstrap.core.component.internal.PanelContainerImpl;
import com.xperience.aem.xpbootstrap.core.component.models.Component;
import com.xperience.aem.xpbootstrap.core.models.organisms.Accordion;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import se.eris.notnull.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * V1 Accordion model implementation.
 */
@Model(
        adaptables = SlingHttpServletRequest.class,
        adapters = { Accordion.class, ComponentExporter.class, ContainerExporter.class },
        resourceType = AccordionImpl.RESOURCE_TYPE, defaultInjectionStrategy= DefaultInjectionStrategy.OPTIONAL
)
@Exporter(
        name = ExporterConstants.SLING_MODEL_EXPORTER_NAME,
        extensions = ExporterConstants.SLING_MODEL_EXTENSION
)
public class AccordionImpl extends PanelContainerImpl implements Accordion {

    /**
     * Resource type.
     */
    public final static String RESOURCE_TYPE = "xpbootstrap/components/master/organisms/accordion/v1/accordion";

    /**
     * Flag indicating if single expansion is enabled.
     */
    @ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
    private boolean singleExpansion;

    /**
     * Array of expanded items.
     */
    @ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
    @Nullable
    private String[] expandedItems;

    /**
     * The heading element.
     */
    @ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
    @Nullable
    private String headingElement;

    /**
     * The current style for this component.
     */
    @ScriptVariable
    private Style currentStyle;

    /**
     * The cached node names of the expanded items for which there is a valid matching child resource.
     */
    private String[] expandedItemNames;

    /**
     * The cached model IDs of the expanded items for which there is a valid matching child resource.
     */
    private String[] expandedItemIds;

    /**
     * The {@link com.xperience.aem.xpbootstrap.core.component.internal.Heading} object for the HTML element
     * to use for accordion headers.
     */
    private Heading heading;

    @Override
    public boolean isSingleExpansion() {
        return singleExpansion;
    }

    @Override
    public String[] getExpandedItems() {
        if (expandedItemNames == null) {
            this.expandedItemNames = Optional.ofNullable(this.expandedItems)
                    .map(Arrays::stream)
                    .orElse(Stream.empty())
                    .filter(Objects::nonNull)
                    .filter(item -> Objects.nonNull(resource.getChild(item)))
                    .toArray(String[]::new);
        }
        return Arrays.copyOf(expandedItemNames, expandedItemNames.length);
    }

    @Override
    public String getHeadingElement() {
        if (heading == null) {
            heading = Heading.getHeading(headingElement);
            if (heading == null) {
                heading = Heading.getHeading(currentStyle.get(PN_DESIGN_HEADING_ELEMENT, String.class));
            }
        }
        if (heading != null) {
            return heading.getElement();
        }
        return null;
    }

    /*
     * DataLayerProvider implementation of field getters
     */

    @Override
    public String[] getDataLayerShownItems() {
        if (expandedItems == null) {
            return new String[0];
        }

        if (expandedItemIds == null) {
            List<String> expandedItemsName = Arrays.asList(expandedItems);

            expandedItemIds = this.getItems().stream()
                    .filter(item -> expandedItemsName.contains(item.getName()))
                    .map(Component::getData)
                    .filter(Objects::nonNull)
                    .map(ComponentData::getId)
                    .toArray(String[]::new);
        }

        return Arrays.copyOf(expandedItemIds, expandedItemIds.length);
    }
}
