package com.xperience.aem.xpbootstrap.core.impl.models.molecules.v1;

import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import junitx.util.PrivateAccessor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.util.ReflectionUtils;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class MleTitleImplTest {


    @InjectMocks
    private MleTitleImpl mleTitle;

    @Test
    void testSetup() throws NoSuchFieldException {
        PrivateAccessor.setField(mleTitle,"atmTitleText","atmTitleText");
        PrivateAccessor.setField(mleTitle,"atmTitleLevel","atmTitleLevel");

        assertEquals(mleTitle.getAtmTitleLevel(),"atmTitleLevel","atmTitleLevel");
        assertEquals(mleTitle.getAtmTitleText(),"atmTitleText","atmTitleText");
        mleTitle.initModel();



    }

}