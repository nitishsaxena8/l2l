package com.xperience.aem.xpbootstrap.core.models.molecules;

import java.util.Collection;

import org.osgi.annotation.versioning.ConsumerType;

import com.adobe.cq.export.json.ComponentExporter;
import com.xperience.aem.xpbootstrap.core.component.models.NavigationItem;
import com.xperience.aem.xpbootstrap.core.models.atoms.AtmSpace;

@ConsumerType
public interface MleBreadcrumb extends ComponentExporter, AtmSpace {
   
    String PN_SHOW_HIDDEN = "showHidden";

    String PN_HIDE_CURRENT = "hideCurrent";

    String PN_START_LEVEL = "startLevel";

    default Collection<NavigationItem> getItems() {
        return null;
    }

}
