package com.xperience.aem.xpbootstrap.core.models.molecules;

import com.xperience.aem.xpbootstrap.core.models.atoms.AtmSpace;

public interface MleSocialShare extends AtmSpace {

    public String getEnableFacebook();

    public String getEnableTwitter();

    public String getEnableLinkedIn();

    public String getPluginUrl();

    public String getEnableCopyUrl();
}
