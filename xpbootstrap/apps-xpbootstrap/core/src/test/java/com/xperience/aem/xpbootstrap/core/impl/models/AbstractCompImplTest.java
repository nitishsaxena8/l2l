package com.xperience.aem.xpbootstrap.core.impl.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.Template;
import com.day.cq.wcm.api.components.Component;
import com.day.cq.wcm.api.components.ComponentContext;

import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import junitx.util.PrivateAccessor;

@ExtendWith({ AemContextExtension.class, MockitoExtension.class })
public class AbstractCompImplTest {

	@InjectMocks
	private AbstractCompImpl abstractComp;

	@Mock
	private ComponentContext componentContext;

	@Mock
	Page currentPage;

	@Mock
	Component component;

	@Mock
	SlingHttpServletRequest request;

	@Mock
	Resource resource;

	@Mock
	PageManager pageManager;

	@Mock
	Template template;

	String resourceCallerPath = "/content/adlc-component-library/us/en/home/some.html";

	@BeforeEach
	public void setUpComp() throws NoSuchFieldException {
		PrivateAccessor.setField(abstractComp, "marginTop", "marginTop");
		PrivateAccessor.setField(abstractComp, "mobileMarginTop", "mobileMarginTop");
		PrivateAccessor.setField(abstractComp, "marginBottom", "marginBottom");
		PrivateAccessor.setField(abstractComp, "mobileMarginBottom", "mobileMarginBottom");

	}

	@Test
	void testAllMargin() {
		assertNotNull(abstractComp.getMarginTop());
		assertEquals("marginTop", abstractComp.getMarginTop());
		assertNotNull(abstractComp.getMobileMarginTop());
		assertEquals("mobileMarginTop", abstractComp.getMobileMarginTop());
		assertNotNull(abstractComp.getMarginBottom());
		assertEquals("marginBottom", abstractComp.getMarginBottom());
		assertNotNull(abstractComp.getMobileMarginBottom());
		assertEquals("mobileMarginBottom", abstractComp.getMobileMarginBottom());
	}

	@Test
	void testGetId() {
		when(request.getAttribute(any())).thenReturn("someResourcePath");
		when(resource.getPath()).thenReturn("someResourcePath");
		when(currentPage.getPageManager()).thenReturn(pageManager);
		when(pageManager.getContainingPage(eq(resource))).thenReturn(currentPage);
		when(currentPage.getTemplate()).thenReturn(template);
		when(currentPage.getPath()).thenReturn("somePath").thenReturn("somePath");
		when(template.getPath()).thenReturn("somePath");
		assertNotNull(abstractComp.getId());
		assertEquals("-112e2d1dc5", abstractComp.getId());
	}

}
