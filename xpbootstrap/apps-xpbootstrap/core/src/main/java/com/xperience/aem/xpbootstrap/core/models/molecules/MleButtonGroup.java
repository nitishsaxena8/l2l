package com.xperience.aem.xpbootstrap.core.models.molecules;

import org.apache.sling.api.resource.Resource;

import com.adobe.cq.export.json.ComponentExporter;
import com.xperience.aem.xpbootstrap.core.models.atoms.AtmSpace;

public interface MleButtonGroup extends ComponentExporter, AtmSpace {

	public Resource getBtnGrp();

}
