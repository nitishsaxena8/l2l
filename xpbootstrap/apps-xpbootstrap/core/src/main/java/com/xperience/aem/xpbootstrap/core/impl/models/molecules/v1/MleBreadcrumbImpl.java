package com.xperience.aem.xpbootstrap.core.impl.models.molecules.v1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.components.Component;
import com.day.cq.wcm.api.designer.Style;
import com.drew.lang.annotations.NotNull;
import com.xperience.aem.xpbootstrap.core.component.Utils.LinkManager;
import com.xperience.aem.xpbootstrap.core.component.internal.BreadcrumbItemImpl;
import com.xperience.aem.xpbootstrap.core.component.models.NavigationItem;
import com.xperience.aem.xpbootstrap.core.impl.models.AbstractCompImpl;
import com.xperience.aem.xpbootstrap.core.models.molecules.MleBreadcrumb;

@Model(adaptables = SlingHttpServletRequest.class,
       adapters = {MleBreadcrumb.class, ComponentExporter.class},
       resourceType = MleBreadcrumbImpl.RESOURCE_TYPE_V1)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class MleBreadcrumbImpl extends AbstractCompImpl implements MleBreadcrumb {

    protected static final String RESOURCE_TYPE_V1 = "xpbootstrap/components/master/molecules/mleBreadcrumb/v1/mleBreadcrumb";

    protected static final boolean PROP_SHOW_HIDDEN_DEFAULT = false;
    protected static final boolean PROP_HIDE_CURRENT_DEFAULT = false;
    protected static final int PROP_START_LEVEL_DEFAULT = 2;

    @ScriptVariable
    private ValueMap properties;

    @ScriptVariable
    private Style currentStyle;

    @ScriptVariable
    private Page currentPage;

    @Self
    protected LinkManager linkManager;

    private boolean showHidden;
    private boolean hideCurrent;
    private int startLevel;
    private List<NavigationItem> items;

    @PostConstruct
    protected void initModel() {
        startLevel = properties.get(PN_START_LEVEL, currentStyle.get(PN_START_LEVEL, PROP_START_LEVEL_DEFAULT));
        showHidden = properties.get(PN_SHOW_HIDDEN, currentStyle.get(PN_SHOW_HIDDEN, PROP_SHOW_HIDDEN_DEFAULT));
        hideCurrent = properties.get(PN_HIDE_CURRENT, currentStyle.get(PN_HIDE_CURRENT, PROP_HIDE_CURRENT_DEFAULT));
    }

    @Override
    public Collection<NavigationItem> getItems() {
        if (items == null) {
            items = createItems();
        }
        return Collections.unmodifiableList(items);
    }

    @NotNull
    @Override
    public String getExportedType() {
        return request.getResource().getResourceType();
    }

    private List<NavigationItem> createItems() {
        List<NavigationItem> items = new ArrayList<>();
        int currentLevel = currentPage.getDepth();
        while (startLevel < currentLevel) {
            Page page = currentPage.getAbsoluteParent(startLevel);
            if (page != null && page.getContentResource() != null) {
                boolean isActivePage = page.equals(currentPage);
                if (isActivePage && hideCurrent) {
                    break;
                }
                if (checkIfNotHidden(page)) {
                    NavigationItem navigationItem = newBreadcrumbItem(page, isActivePage, linkManager, currentLevel,
                            Collections.emptyList(), getId(), component);
                    items.add(navigationItem);
                }
            }
            startLevel++;
        }
        return items;
    }

    protected NavigationItem newBreadcrumbItem(Page page, boolean active, @NotNull LinkManager linkManager, int level, List<NavigationItem> children, String parentId, Component component) {
        return new BreadcrumbItemImpl(page, active, linkManager, level, children, parentId, component);
    }

    protected boolean checkIfNotHidden(Page page) {
        return !page.isHideInNav() || showHidden;
    }
}