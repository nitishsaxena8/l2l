package com.xperience.aem.xpbootstrap.core.impl.models.molecules.v1;

import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import junitx.util.PrivateAccessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class MleSocialMediaLinksModelTest {

    @InjectMocks
    private MleSocialMediaLinksModelImpl mleSocialMediaLinksModel;

    @BeforeEach
    void setUp() throws NoSuchFieldException {

        PrivateAccessor.setField(mleSocialMediaLinksModel, "atmTitleText", "atmTitleText");
        PrivateAccessor.setField(mleSocialMediaLinksModel, "atmTitleLevel", "atmTitleLevel");
        PrivateAccessor.setField(mleSocialMediaLinksModel, "target", "newTab");
    }

    @Test
    void testSocialMediaLink() {

        assertNotNull(mleSocialMediaLinksModel.getTarget());
        assertNotNull(mleSocialMediaLinksModel.getAtmTitleLevel());
        assertNotNull(mleSocialMediaLinksModel.getAtmTitleText());
        mleSocialMediaLinksModel.initModel();
    }

    @Test
    void testIconTwitter() throws NoSuchFieldException {
        PrivateAccessor.setField(mleSocialMediaLinksModel, "pathLink", "https://twitter.com/i/flow/login");
        assertNotNull(mleSocialMediaLinksModel.getPathLink());
        assertNotNull(mleSocialMediaLinksModel.getIcon());
    }

    @Test
    void testIconFacebook() throws NoSuchFieldException {
        PrivateAccessor.setField(mleSocialMediaLinksModel, "pathLink", "https://facebook.com/i/flow/login");
        assertNotNull(mleSocialMediaLinksModel.getPathLink());
        assertNotNull(mleSocialMediaLinksModel.getIcon());
    }

    @Test
    void testIconYoutube() throws NoSuchFieldException {
        PrivateAccessor.setField(mleSocialMediaLinksModel, "pathLink", "https://youtube.com/i/flow/login");
        assertNotNull(mleSocialMediaLinksModel.getPathLink());
        assertNotNull(mleSocialMediaLinksModel.getIcon());
    }

    @Test
    void testIconDefault() throws NoSuchFieldException {
        PrivateAccessor.setField(mleSocialMediaLinksModel, "pathLink", "https://somePath.com/i/flow/login");
        assertNotNull(mleSocialMediaLinksModel.getPathLink());
        assertNotNull(mleSocialMediaLinksModel.getIcon());
    }
}