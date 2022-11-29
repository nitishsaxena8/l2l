package com.xperience.aem.xpbootstrap.core.impl.models.molecules.v1;

import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import junitx.util.PrivateAccessor;
import org.apache.sling.api.SlingHttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class MleImageImplTest {

    @InjectMocks
    MleImageImpl mleImage;

    @Mock
    SlingHttpServletRequest request;
    @BeforeEach
    void setUp() throws NoSuchFieldException {

        PrivateAccessor.setField(mleImage,"atmIconSrcFileReference","/content/dam/some/path");
        PrivateAccessor.setField(mleImage,"atmIconAltText","atmIconAltText");
    }

    @Test
    void testImage()
    {
    	mleImage.initModel();
        assertNotNull(mleImage.getAtmIconSrcFileReference());
        assertNotNull(mleImage.getAtmIconAltText());
        
    }
}