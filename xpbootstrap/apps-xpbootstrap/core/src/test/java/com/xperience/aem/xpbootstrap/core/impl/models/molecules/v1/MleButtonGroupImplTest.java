package com.xperience.aem.xpbootstrap.core.impl.models.molecules.v1;

import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import junitx.util.PrivateAccessor;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class MleButtonGroupImplTest {

    @InjectMocks
    MleButtonGroupImpl mleButtonGroup;

    @Mock
    SlingHttpServletRequest request;

    @Mock
    Resource btnGrp;

    @Mock
    Resource someResource;

    @Test
    void testButtonGrp() throws NoSuchFieldException {
        PrivateAccessor.setField(mleButtonGroup,"btnGrp",btnGrp);
        mleButtonGroup.initModel();
        assertNotNull(mleButtonGroup.getBtnGrp());
    }

    @Test
    void testExportedType()
    {
        lenient().when(request.getResource()).thenReturn(someResource);
        lenient().when(someResource.getResourceType()).thenReturn("content/json");
        assertNotNull(mleButtonGroup.getExportedType());
    }
}