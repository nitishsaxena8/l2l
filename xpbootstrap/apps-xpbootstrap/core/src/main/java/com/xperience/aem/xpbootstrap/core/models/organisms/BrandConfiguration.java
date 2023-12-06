package com.xperience.aem.xpbootstrap.core.models.organisms;

import java.util.List;

import com.xperience.aem.xpbootstrap.core.beans.BrandConfigItem;
import com.xperience.aem.xpbootstrap.core.component.Utils.Container;

public interface BrandConfiguration extends Container{
	
	public List<BrandConfigItem> getBrandConfigItems();
		
}
