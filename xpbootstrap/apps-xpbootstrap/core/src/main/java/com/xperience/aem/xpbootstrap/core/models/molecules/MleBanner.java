package com.xperience.aem.xpbootstrap.core.models.molecules;

import com.adobe.cq.export.json.ComponentExporter;
import com.xperience.aem.xpbootstrap.core.models.atoms.AtmCta;
import com.xperience.aem.xpbootstrap.core.models.atoms.AtmPicture;
import com.xperience.aem.xpbootstrap.core.models.atoms.AtmSpace;
import com.xperience.aem.xpbootstrap.core.models.atoms.AtmText;
import com.xperience.aem.xpbootstrap.core.models.atoms.AtmTitle;

public interface MleBanner extends AtmSpace, AtmTitle, AtmText, AtmPicture, AtmCta, ComponentExporter {
	
	boolean isAnimation();
	
	String getGradientType();

	String getGradientPosition();

}
