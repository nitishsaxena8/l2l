package com.xperience.aem.xpbootstrap.core.impl.models.molecules.v1;

import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import junitx.util.PrivateAccessor;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class MleResponsiveImageImplTest {


    @InjectMocks
    private MleResponsiveImageImpl mleResponsiveImage;

    @Mock
    private SlingHttpServletRequest request;
    
    @Mock
    private Resource resource;

    @BeforeEach
    void setUp() throws NoSuchFieldException {
        PrivateAccessor.setField(mleResponsiveImage,"atmPictureDesktopImgFileReference","/content/dam/xpbootstrap/some/files");
        PrivateAccessor.setField(mleResponsiveImage,"atmPictureAltText","atmPictureAltText");
        PrivateAccessor.setField(mleResponsiveImage,"atmPictureMobileImgFileReference","/content/dam/xpbootstrap/some/files");
    }

	@Test
	void testImage() {
		when(request.getResource()).thenReturn(resource);
		when(resource.getResourceType()).thenReturn("resourceType");
		mleResponsiveImage.initModel();
		assertNotNull(mleResponsiveImage.getAtmPictureAltText());
		assertNotNull(mleResponsiveImage.getAtmPictureDesktopImgFileReference());
		assertNotNull(mleResponsiveImage.getAtmPictureMobileImgFileReference());
		assertNotNull(mleResponsiveImage.getExportedType());
		assertEquals("resourceType", mleResponsiveImage.getExportedType());

	}
}
