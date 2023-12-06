package com.xperience.aem.xpbootstrap.core.impl.models.molecules.v1;

import com.xperience.aem.xpbootstrap.core.models.atoms.AtmTitle;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

@Model(
        adaptables = {Resource.class},
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)

public class MleSocialMediaLinksModelImpl implements AtmTitle {
    private final Logger LOG = LoggerFactory.getLogger(MleSocialMediaLinksModelImpl.class);


    @Inject
    private String pathLink;

    @Inject
    private String target;

    @Inject
    private String atmTitleText;

    @Inject
    private String atmTitleLevel;

    String icon = StringUtils.EMPTY;


    protected String getTitle() {

        if (pathLink.contains("twitter"))
            return "twitter";
        else if (pathLink.contains("youtube"))
            return "youtube";
        else if (pathLink.contains("facebook"))
            return "facebook";
        return "link";
    }

    @PostConstruct
    protected void initModel() {
        LOG.info(" --- MleSocialMediaLinks initialized ---{}", getPathLink());
    }

    public String getTarget() {
        return target;
    }


    public String getIcon() {
        icon = getTitle();

        String iconClass = "";
        switch (icon) {
            case "twitter":
                iconClass = "bi bi-twitter";
                break;
            case "youtube":
                iconClass = "bi bi-youtube";
                break;
            case "facebook":
                iconClass = "bi bi-facebook";
                break;
            default:
                iconClass = "bi bi-share";
                break;

        }
        return iconClass;

    }

    public String getPathLink() {
        return pathLink;
    }

    @Override
    public String getAtmTitleText() {
        return atmTitleText;
    }

    @Override
    public String getAtmTitleLevel() {
        return atmTitleLevel;
    }
}
