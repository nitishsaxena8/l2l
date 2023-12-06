package com.xperience.aem.xpbootstrap.core.component.commons;

import com.day.cq.wcm.api.components.Component;
import com.day.cq.wcm.api.components.ComponentManager;
import com.day.cq.wcm.msm.api.LiveRelationshipManager;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.request.RequestPathInfo;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static com.day.cq.wcm.api.NameConstants.NN_ICON_PNG;
import static com.xperience.aem.xpbootstrap.core.component.internal.PanelContainerItemImpl.PN_PANEL_TITLE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class EditorTest {

    @InjectMocks
    Editor editor;

    @Mock
    Resource png;


    @Mock
    ValueMap valueMap;

    @Mock
    Resource res;
    @Mock
    LiveRelationshipManager mgr;
    @Mock
    SlingHttpServletRequest request;

    @Mock
    RequestPathInfo requestPathInfo;

    @Mock
    Resource container;

    @Mock
    Item item;

    @Mock
    ResourceResolver resourceResolver;

    @Mock
    ValueMap vm;

    String containerPath = "some/path";

    @Mock
    ComponentManager componentManager;

    @Mock
    Resource childResource;
    protected String iconName = "iconName";
    List<Resource> childResourceList;

    @Mock
    Component component;
    String PN_ICON = "cq:icon";

    @BeforeEach
    void setUp() {
        lenient().when(request.getRequestPathInfo()).thenReturn(requestPathInfo);
        lenient().when(requestPathInfo.getSuffix()).thenReturn(containerPath);
        lenient().when(request.getResourceResolver()).thenReturn(resourceResolver);
        Mockito.when(resourceResolver.getResource(containerPath)).thenReturn(container);
        lenient().when(resourceResolver.adaptTo(ComponentManager.class)).thenReturn(componentManager);
        childResourceList = new ArrayList<>();
        childResourceList.add(childResource);
        lenient().when(container.getChildren()).thenReturn(childResourceList);
        lenient().when(componentManager.getComponentOfResource(childResource)).thenReturn(component);
        lenient().when(childResource.getValueMap()).thenReturn(vm);
        lenient().when(vm.get(PN_PANEL_TITLE, String.class)).thenReturn("someValue");
        lenient().when(resourceResolver.adaptTo(LiveRelationshipManager.class)).thenReturn(mgr);
        lenient().when(component.adaptTo(Resource.class)).thenReturn(res);
        lenient().when(res.getValueMap()).thenReturn( valueMap);
        Mockito.when(valueMap.get(PN_ICON, String.class)).thenReturn(iconName);

    }
    @Test
    void testReadChildren()
    {

        editor.initModel();
        assertNotNull(editor.getContainer());
        assertNotNull(editor.getItems());
    }


}