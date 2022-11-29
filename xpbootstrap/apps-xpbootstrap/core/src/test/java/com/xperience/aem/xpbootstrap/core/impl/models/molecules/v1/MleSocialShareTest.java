package com.xperience.aem.xpbootstrap.core.impl.models.molecules.v1;

import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import junitx.util.PrivateAccessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class MleSocialShareTest {

    @InjectMocks
    private MleSocialShareImpl mleSocialShare;


    @BeforeEach
    void setUp() throws NoSuchFieldException {
        PrivateAccessor.setField(mleSocialShare,"enableFacebook","true");
        PrivateAccessor.setField(mleSocialShare,"enableTwitter", "false");
        PrivateAccessor.setField(mleSocialShare,"enableLinkedIn","true");
        PrivateAccessor.setField(mleSocialShare,"pluginUrl","pluginUrl");
        PrivateAccessor.setField(mleSocialShare,"enableCopyUrl","enableCopyUrl");
    }

    @Test
    void testSocialShare()
    {
        assertNotNull(mleSocialShare.getEnableCopyUrl());
        assertNotNull(mleSocialShare.getEnableFacebook());
        assertNotNull(mleSocialShare.getEnableTwitter());
        assertNotNull(mleSocialShare.getEnableLinkedIn());
        assertEquals(mleSocialShare.getPluginUrl(),"pluginUrl");
    }
}