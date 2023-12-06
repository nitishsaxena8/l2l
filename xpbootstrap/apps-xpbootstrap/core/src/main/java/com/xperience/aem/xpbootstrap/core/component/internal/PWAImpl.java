package com.xperience.aem.xpbootstrap.core.component.internal;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.drew.lang.annotations.NotNull;
import com.xperience.aem.xpbootstrap.core.component.models.PWA;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;

import javax.annotation.PostConstruct;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Model(adaptables = Resource.class,
        adapters = {PWA.class})
public class PWAImpl implements PWA {

    static final String CONTENT_PATH = "/content/";

    private boolean isEnabled = false;
    private String manifestPath = "";
    private String serviceWorkerPath = "";
    private String themeColor = "";
    private String iconPath = "";

    @Self
    private Resource resource;

    @PostConstruct
    protected void initModel() {
        PageManager pageManager = resource.getResourceResolver().adaptTo(PageManager.class);
        if (pageManager == null) {
            return;
        }

        Page page = pageManager.getContainingPage(resource);
        while (page != null) {
            Resource contentResource = page.getContentResource();
            if (contentResource != null) {
                ValueMap valueMap = contentResource.getValueMap();
                Boolean isPWAEnabled = valueMap.get(PN_PWA_ENABLED, Boolean.class);
                if (isPWAEnabled != null && isPWAEnabled) {
                    this.isEnabled = true;
                    this.themeColor = colorToHex(valueMap.get(PN_PWA_THEME_COLOR, ""));
                    this.iconPath = valueMap.get(PN_PWA_ICON_PATH, "");
                    String startURL = valueMap.get(PN_PWA_START_URL, "");
                    this.manifestPath = replaceSuffix(startURL, MANIFEST_NAME);
                    String mappingName = page.getPath().replace(CONTENT_PATH, "").replace("/", ".");
                    this.serviceWorkerPath = "/" + mappingName + "sw.js";
                    break;
                }
            }
            page = page.getParent();
        }
    }

    @Override
    public boolean isEnabled() {
        return this.isEnabled;
    }

    @Override
    public String getThemeColor() {
        return this.themeColor;
    }

    @Override
    public String getIconPath() {
        return this.iconPath;
    }

    @Override
    public String getManifestPath() {
        return this.manifestPath;
    }

    @Override
    public String getServiceWorkerPath() {
        return this.serviceWorkerPath;
    }

    private String colorToHex(String color) {
        if (color == null || color.length() == 0) {
            return "";
        }

        if (color.startsWith("#")) {
            return color;
        }

        try {
            Pattern rgbPattern = Pattern.compile("^rgba? *\\( *([0-9]+), *([0-9]+), *([0-9]+),? *([01]|0\\.[0-9]*)? *\\)");
            Matcher rgbMatcher = rgbPattern.matcher(color);

            if (!rgbMatcher.matches()) {
                return "";
            }

            String r = Integer.toHexString(Integer.parseInt(rgbMatcher.group(1)));
            String g = Integer.toHexString(Integer.parseInt(rgbMatcher.group(2)));
            String b = Integer.toHexString(Integer.parseInt(rgbMatcher.group(3)));

            return "#" + (r.length() == 2 ? r : ("0" + r)) + (g.length() == 2 ? g : ("0" + g)) + (b.length() == 2 ? b : ("0" + b));
        } catch (NumberFormatException e) {
            return "";
        }
    }

    private String replaceSuffix(@NotNull String url, @NotNull String newSuffix) {
        int index = url.lastIndexOf(".");
        if (index == -1) {
            return url + (url.endsWith("/") ? newSuffix : ("/" + newSuffix));
        }

        return url.replace(url.substring(index, url.length()), ("/" + newSuffix));
    }
}