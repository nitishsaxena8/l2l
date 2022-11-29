package com.xperience.aem.xpbootstrap.core.component.internal;


import com.day.cq.wcm.api.NameConstants;
import com.xperience.aem.xpbootstrap.core.component.internal.AbstractComponentImpl;
import com.xperience.aem.xpbootstrap.core.component.models.Component;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Model;


/**
 * Default component model implementation.
 *
 * This component model implementations allows resource whose component does not have a model, or whose model
 * does not list {@link Component} as an adapter, to be adapted to {@link Component}.
 */
@Model(adaptables = SlingHttpServletRequest.class, adapters = Component.class, resourceType = NameConstants.NT_COMPONENT)
public final class ComponentImpl extends AbstractComponentImpl {
}

