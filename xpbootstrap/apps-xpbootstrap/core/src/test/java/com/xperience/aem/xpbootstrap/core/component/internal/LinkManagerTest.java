package com.xperience.aem.xpbootstrap.core.component.internal;

import com.day.cq.dam.api.Asset;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.designer.Style;
import com.xperience.aem.xpbootstrap.core.component.Utils.LinkBuilder;
import com.xperience.aem.xpbootstrap.core.service.utility.PathProcessor;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import junitx.util.PrivateAccessor;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static com.xperience.aem.xpbootstrap.core.component.internal.LinkManagerImpl.PN_DISABLE_SHADOWING;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
public class LinkManagerTest {


    @InjectMocks
    private LinkManagerImpl linkManager;

    @Mock
    SlingHttpServletRequest request;

    @Mock
    ValueMap properties;

    @Mock
    Resource resource;

    String url = "https://somepath.com";

    @Mock
    Page page;

    @Mock
    Asset asset;

    @Mock
    Style style;

    @Mock
    PathProcessor pathProcessor;

    @Mock
    LinkBuilderImpl linkBuilder;

    @Captor
    ArgumentCaptor<LinkBuilder> linkBuilderArgumentCaptor;

    private boolean PROP_DISABLE_SHADOWING_DEFAULT = false;

    @BeforeEach
    void initValues() {
        Mockito.when(style.get(PN_DISABLE_SHADOWING, false)).thenReturn(false);
        Mockito.when(properties.get(PN_DISABLE_SHADOWING, false)).thenReturn(false);
    }


    @Test
    void testGetAsset() throws NoSuchFieldException {
        List<PathProcessor> pathProcessors = new ArrayList<>();
        pathProcessors.add(pathProcessor);


        PrivateAccessor.setField(linkManager, "pathProcessors", pathProcessors);
        linkManager.initModel();
        assertNotNull(linkManager.get(asset));

    }

    @Test
    void testGetPage() throws NoSuchFieldException {
        List<PathProcessor> pathProcessors = new ArrayList<>();
        pathProcessors.add(pathProcessor);
        PrivateAccessor.setField(linkManager, "pathProcessors", pathProcessors);
        linkManager.initModel();
        assertNotNull(linkManager.get(page));

    }

    @Test
    void testGetUrl() throws NoSuchFieldException {
        List<PathProcessor> pathProcessors = new ArrayList<>();
        pathProcessors.add(pathProcessor);
        PrivateAccessor.setField(linkManager, "pathProcessors", pathProcessors);
        linkManager.initModel();
        assertNotNull(linkManager.get(url));

    }

    @Test
    void testGetResource() throws NoSuchFieldException {
        List<PathProcessor> pathProcessors = new ArrayList<>();
        pathProcessors.add(pathProcessor);
        PrivateAccessor.setField(linkManager, "pathProcessors", pathProcessors);
        linkManager.initModel();
        assertNotNull(linkManager.get(resource));

    }

}