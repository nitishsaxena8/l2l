package com.xperience.aem.xpbootstrap.core.impl.models.molecules.v1;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.lenient;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.xperience.aem.xpbootstrap.core.impl.models.molecules.v1.MleBannerImpl;

import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import junitx.util.PrivateAccessor;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
public class MleBannerTest {

    @InjectMocks
    private MleBannerImpl mleBanner;

    @Mock
    SlingHttpServletRequest request;

    @Mock
    Resource resource;

    @BeforeEach
    void testAllValues() throws NoSuchFieldException {
        PrivateAccessor.setField(mleBanner,"animation",true);
        PrivateAccessor.setField(mleBanner,"gradientType","gradientType");
        PrivateAccessor.setField(mleBanner,"atmPictureDesktopImgFileReference","some/path");
        PrivateAccessor.setField(mleBanner,"atmPictureAltText","atmPictureAltText");
        PrivateAccessor.setField(mleBanner,"atmPictureMobileImgFileReference","some/image/path");
        PrivateAccessor.setField(mleBanner,"gradientPosition","gradientPosition");
        PrivateAccessor.setField(mleBanner,"atmTitleText","atmTitleText");
        PrivateAccessor.setField(mleBanner,"atmTitleLevel","atmTitleLevel");
        PrivateAccessor.setField(mleBanner,"atmTextHtml","atmTextHtml");
        PrivateAccessor.setField(mleBanner,"atmCtaLink","atmCtaLink");
        PrivateAccessor.setField(mleBanner,"atmCtaLabel","atmCtaLabel");
        PrivateAccessor.setField(mleBanner,"atmCtaTarget","atmCtaTarget");
        PrivateAccessor.setField(mleBanner,"atmCtaType","atmCtaType");
        PrivateAccessor.setField(mleBanner,"atmCtaSize","atmCtaSize");
        PrivateAccessor.setField(mleBanner,"atmCtaContext","atmCtaContext");
        PrivateAccessor.setField(mleBanner,"atmCtaRole","atmCtaRole");
        lenient().when(request.getResource()).thenReturn(resource);
        lenient().when(resource.getResourceType()).thenReturn("resourceType");
    }

    @Test
    void SetupTest()
    {
        assertNotNull(mleBanner.getAtmCtaContext());
        assertNotNull(mleBanner.getAtmCtaLabel());
        assertNotNull(mleBanner.getAtmCtaLink());
        assertNotNull(mleBanner.getAtmCtaRole());
        assertNotNull(mleBanner.getAtmCtaSize());
        assertNotNull(mleBanner.getAtmCtaTarget());
        assertNotNull(mleBanner.getAtmCtaType());
        assertNotNull(mleBanner.getAtmPictureAltText());
        assertNotNull(mleBanner.getAtmPictureDesktopImgFileReference());
        assertNotNull(mleBanner.getAtmPictureDesktopImgFileReference());
        assertNotNull(mleBanner.getAtmPictureMobileImgFileReference());
        assertNotNull(mleBanner.getAtmTextHtml());
        assertNotNull(mleBanner.getAtmTitleLevel());
        assertNotNull(mleBanner.getAtmTitleText());
        assertNotNull(mleBanner.getGradientPosition());
        assertNotNull(mleBanner.getGradientType());
        assertEquals(mleBanner.isAnimation(),true);



    }


}
