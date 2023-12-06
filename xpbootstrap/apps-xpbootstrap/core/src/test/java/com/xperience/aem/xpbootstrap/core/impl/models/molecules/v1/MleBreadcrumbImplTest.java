package com.xperience.aem.xpbootstrap.core.impl.models.molecules.v1;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.Template;
import com.day.cq.wcm.api.components.Component;
import com.day.cq.wcm.api.components.ComponentContext;
import com.day.cq.wcm.api.designer.Style;
import com.xperience.aem.xpbootstrap.core.component.Utils.ContentFragmentUtils;
import com.xperience.aem.xpbootstrap.core.component.Utils.Link;
import com.xperience.aem.xpbootstrap.core.component.Utils.LinkBuilder;
import com.xperience.aem.xpbootstrap.core.component.Utils.LinkManager;
import com.xperience.aem.xpbootstrap.core.component.internal.BreadcrumbItemImpl;
import com.xperience.aem.xpbootstrap.core.component.models.NavigationItem;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import junitx.util.PrivateAccessor;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.checkerframework.common.value.qual.MinLen;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static com.xperience.aem.xpbootstrap.core.impl.models.molecules.v1.MleBreadcrumbImpl.*;
import static com.xperience.aem.xpbootstrap.core.models.molecules.MleBreadcrumb.PN_SHOW_HIDDEN;
import static com.xperience.aem.xpbootstrap.core.models.molecules.MleBreadcrumb.PN_START_LEVEL;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.lenient;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class MleBreadcrumbImplTest {


    @InjectMocks
    private MleBreadcrumbImpl mleBreadcrumb;

    @Mock
    Style style;

    @Mock
    PageManager pageManager;


    @Mock
    LinkBuilder linkBuilder;

    @Mock
    Link link;
    @Mock
    Page resourcePage;
    @Mock
    SlingHttpServletRequest request;
    @Mock
    Page page;

    @Mock
    Template template;

    private int startLevel =2;
    private int currentLevel =3;

    @Mock
    Page somePage;

    @Mock
    Resource contentResource;

    @Mock
    LinkManager linkManager;

    @Mock
    ValueMap properties;

    @Mock
    ComponentContext componentContext;

    @Mock
    private  Object obj;
    @Mock
    NavigationItem navigationItem;
    private List<NavigationItem> navigationItemList;

    @Mock
    Page currentPage;

    @Mock
    Component component;

    @BeforeEach
    void setUp() throws NoSuchFieldException {
        PrivateAccessor.setField(mleBreadcrumb,"startLevel",2);
        lenient().when(style.get(PN_START_LEVEL, PROP_START_LEVEL_DEFAULT)).thenReturn(2);
        lenient().when(properties.get(PN_START_LEVEL,2)).thenReturn(startLevel);
        lenient().when(style.get(PN_SHOW_HIDDEN, PROP_SHOW_HIDDEN_DEFAULT)).thenReturn(false);
        lenient().when(properties.get(PN_SHOW_HIDDEN,false)).thenReturn(false);
        lenient().when(style.get(PN_HIDE_CURRENT, PROP_HIDE_CURRENT_DEFAULT)).thenReturn(true);
        lenient().when(properties.get(PN_HIDE_CURRENT,true)).thenReturn(true);
        lenient().when(currentPage.getDepth()).thenReturn(currentLevel);
        lenient().when(currentPage.getAbsoluteParent(startLevel)).thenReturn(somePage);
        lenient().when(somePage.getContentResource()).thenReturn(contentResource);
        lenient().when(contentResource.getResourceType()).thenReturn("breadcrumb/jcr:content");
        lenient().when((String)request.getAttribute(ContentFragmentUtils.ATTR_RESOURCE_CALLER_PATH)).thenReturn("content/xpbootstrap/some/component");
        lenient().when(currentPage.getPageManager()).thenReturn(pageManager);
        lenient().when(contentResource.getPath()).thenReturn("conf/xpbootstrap/base/jcr:content");
        lenient().when(pageManager.getContainingPage(contentResource)).thenReturn(resourcePage);
        lenient().when(currentPage.getTemplate()).thenReturn(template);
        lenient().when(linkManager.get(somePage)).thenReturn(linkBuilder);
        lenient().when(linkBuilder.build()).thenReturn(link);
        lenient().when(resourcePage.getPath()).thenReturn("some/comp/path");
        lenient().when(currentPage.getPath()).thenReturn("some/comp/path");
        lenient().when(template.getPath()).thenReturn("conf/xpbootstrap/base");
    }

    @Test
    void testInit()
    {
      mleBreadcrumb.initModel();
      assertNotNull(mleBreadcrumb.checkIfNotHidden(page));
    }

    @Test
    void testCreateItems()
    {
        navigationItemList = new ArrayList<>();
        lenient().when(currentPage.getDepth()).thenReturn(currentLevel);
        Mockito.when(currentPage.getAbsoluteParent(startLevel)).thenReturn(somePage);
        assertNotNull(mleBreadcrumb.getItems());
    }

    @Test
    void testConst()
    {
        navigationItemList = new ArrayList<>();
        navigationItemList.add(navigationItem);
        navigationItemList.add(navigationItem);
        navigationItemList.add(navigationItem);
        mleBreadcrumb.newBreadcrumbItem(somePage,false,linkManager,1,navigationItemList,"currentPage",component);
        assertNotNull(mleBreadcrumb.newBreadcrumbItem(somePage,false,linkManager,1,navigationItemList,"currentPage",component));
    }
}