package com.xperience.aem.xpbootstrap.core.component.internal;

import org.apache.commons.lang3.StringUtils;

public enum Heading {

    H1("h1"),
    H2("h2"),
    H3("h3"),
    H4("h4"),
    H5("h5"),
    H6("h6");

    private String element;

    Heading(String element) {
        this.element = element;
    }

    public static Heading getHeading(String value) {
        for (Heading heading : values()) {
            if (StringUtils.equalsIgnoreCase(heading.element, value)) {
                return heading;
            }
        }
        return null;
    }

    public String getElement() {
        return element;
    }

}
