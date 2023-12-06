package com.xperience.aem.xpbootstrap.core.impl.models.molecules.v1;

import com.day.cq.commons.DownloadResource;
import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.DamConstants;
import com.day.cq.dam.api.Rendition;
import com.day.cq.wcm.api.designer.Style;
import com.xperience.aem.xpbootstrap.core.component.Utils.Link;
import com.xperience.aem.xpbootstrap.core.component.Utils.LinkBuilder;
import com.xperience.aem.xpbootstrap.core.component.Utils.LinkManager;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import junitx.util.PrivateAccessor;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.commons.mime.MimeTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.jcr.Binary;
import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.RepositoryException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class MleDownloadTest {

    @InjectMocks
    private MleDownloadImpl mleDownload;

    @Mock
    SlingHttpServletRequest request;

    @Mock
    LinkManager linkManager;

    @Mock
    MimeTypeService mimeTypeService;

    String fileName = "fileName";

    @Mock
    ValueMap properties;

    @Mock
    Style currentStyle;

    @Mock
    Resource dwnldResource;

    @Mock
    Asset dwnldAsset;

    @Mock
    LinkBuilder linkBuilder;

    @Mock
    Resource downRes;

    @Mock
    DownloadResource downloadResource;

    @Mock
    ValueMap prop;

    @Mock
    Link link;

    @Mock
    Property property;

    @Mock
    Object object;
    @Mock
    Rendition rendition;
    String fileNames = "fileName";
    Long fileSize = 128717855L;

    @Mock
    Resource fileResource;

    @Mock
    Binary binary;

    String PN_DISPLAY_SIZE = "displaySize";
    String PN_DISPLAY_FORMAT = "displayFormat";

    @Mock
    Resource fileContent;

    @Mock
    Resource resource;

    @Mock
    Node node;

    String assetPath = "/content/dam/xpbootstrap/some/file";

    @Mock
    ResourceResolver resourceResolver;

    @BeforeEach
    void setUp() throws NoSuchFieldException {
        PrivateAccessor.setField(mleDownload, "atmTitleText", "atmTitleText");
        PrivateAccessor.setField(mleDownload, "actionText", "actionText");
        PrivateAccessor.setField(mleDownload, "atmTitleLevel", "atmTitleLevel");
    }

    @Test
    void testDownload() {
        assertNotNull(mleDownload.getActionText());
        assertNotNull(mleDownload.getAtmTitleText());
        assertNotNull(mleDownload.getAtmTitleLevel());


    }

    @Test
    void testDowloadAsset() {
        Mockito.when(properties.get(DownloadResource.PN_REFERENCE, String.class)).thenReturn("/content/dam/xpbootstrap/some/file");
        Mockito.when(currentStyle.get(PN_DISPLAY_SIZE, true)).thenReturn(true);
        Mockito.when(currentStyle.get(PN_DISPLAY_FORMAT, true)).thenReturn(true);
        Mockito.when(resourceResolver.getResource("/content/dam/xpbootstrap/some/file")).thenReturn(dwnldResource);
        Mockito.when(dwnldResource.adaptTo(Asset.class)).thenReturn(dwnldAsset);
        Mockito.when(dwnldAsset.getName()).thenReturn("file");
        Mockito.when(dwnldAsset.getMetadataValue(DamConstants.DC_FORMAT)).thenReturn("pdf");
        Mockito.when(mimeTypeService.getExtension("pdf")).thenReturn("pdf");
        Mockito.when(dwnldResource.getPath()).thenReturn(assetPath);
        Mockito.when(linkManager.get(anyString())).thenReturn(linkBuilder);
        Mockito.when(linkBuilder.build()).thenReturn(link);
        Mockito.when(dwnldAsset.getOriginal()).thenReturn(rendition);
        Mockito.when(rendition.getSize()).thenReturn(fileSize);
        mleDownload.initModel();
        assertNotNull(mleDownload.getSize());
        assertNotNull(mleDownload.getExtension());
        assertNotNull(mleDownload.getFilename());
        assertNotNull(mleDownload.getFormat());
        assertNotNull(mleDownload.displaySize());
        assertNotNull(mleDownload.getIcon());

    }

    @Test
    void testDownloadNonAsset() throws RepositoryException {
        Mockito.when(currentStyle.get(PN_DISPLAY_SIZE, true)).thenReturn(true);
        Mockito.when(currentStyle.get(PN_DISPLAY_FORMAT, true)).thenReturn(true);
        Mockito.when(resource.getChild(DownloadResource.NN_FILE)).thenReturn(fileResource);
        lenient().when(properties.get(DownloadResource.PN_FILE_NAME, String.class)).thenReturn(fileNames);
        Mockito.when(fileResource.getChild(JcrConstants.JCR_CONTENT)).thenReturn(fileContent);
        Mockito.when(fileContent.adaptTo(ValueMap.class)).thenReturn(prop);
        lenient().when(prop.get(JcrConstants.JCR_MIMETYPE, String.class)).thenReturn("format");
        lenient().when(mimeTypeService.getExtension("format")).thenReturn("extension");
        Mockito.when(linkManager.get(anyString())).thenReturn(linkBuilder);
        Mockito.when(linkBuilder.build()).thenReturn(link);
        lenient().when(fileContent.adaptTo(Node.class)).thenReturn(node);
        lenient().when(node.getProperty("jcr:data")).thenReturn(property);
        lenient().when(property.getBinary()).thenReturn(binary);
        lenient().when(binary.getSize()).thenReturn(1233L);
        mleDownload.initModel();
        assertNotNull(mleDownload.getTitle());

    }

}