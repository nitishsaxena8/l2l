package com.xperience.aem.xpbootstrap.core.service.utility;

import org.apache.sling.api.resource.ResourceResolver;

public interface DynamicMediaService {
      public String getDMAssetUrl(String assetPath, String imagePreset, String cropProfile, String queryParams,
			ResourceResolver resourceResolver, boolean isTargetPreview);
}