package com.xperience.aem.xpbootstrap.core.impl.models.molecules.v1;

import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import junitx.util.PrivateAccessor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
public class MleTextImplTest {

    @InjectMocks
    private MleTextImpl mleText;
    @Test
    void testSetup() throws NoSuchFieldException {
        PrivateAccessor.setField(mleText,"atmTextHtml","atmTextHtml");
        assertEquals(mleText.getAtmTextHtml(),"atmTextHtml");
        mleText.initModel();
    }
}