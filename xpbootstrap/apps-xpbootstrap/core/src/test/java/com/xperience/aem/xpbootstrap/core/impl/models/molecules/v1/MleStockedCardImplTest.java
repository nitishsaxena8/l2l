package com.xperience.aem.xpbootstrap.core.impl.models.molecules.v1;

import io.wcm.testing.mock.aem.junit5.AemContext;
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

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class MleStockedCardImplTest {

    @InjectMocks
    private MleStockedCardImpl mleStockedCard;

    @Mock
    SlingHttpServletRequest request;

    private AemContext context;

    @Mock
    Resource prCta;

    @Mock
    Resource secCta;

    @BeforeEach
    void setUp() throws NoSuchFieldException {
        context.addModelsForClasses(MleStockedCardImpl.class);
        request = context.request();
        PrivateAccessor.setField(mleStockedCard, "atmIconSrcFileReference", "some/path");
        PrivateAccessor.setField(mleStockedCard, "atmIconAltText", "atmIconAltText");
        PrivateAccessor.setField(mleStockedCard, "atmCardTitle", "atmCardTitle");
        PrivateAccessor.setField(mleStockedCard, "atmCardSubtitle", "atmCardSubtitle");
        PrivateAccessor.setField(mleStockedCard, "atmCardDescription", "atmCardDescription");
        PrivateAccessor.setField(mleStockedCard,"atmCardPrimaryCta",prCta);
        PrivateAccessor.setField(mleStockedCard,"atmCardSecondaryCta",secCta);
        PrivateAccessor.setField(mleStockedCard,"cardHeader","cardHeader");
        PrivateAccessor.setField(mleStockedCard,"cardFooter","cardFooter");
    }

    @Test
    void test() {
        assertNotNull(mleStockedCard.getAtmCardDescription());
        assertNotNull(mleStockedCard.getAtmCardSubtitle());
        assertNotNull(mleStockedCard.getAtmCardTitle());
        assertNotNull(mleStockedCard.getAtmIconAltText());
        assertNotNull(mleStockedCard.getAtmIconSrcFileReference());
        assertEquals(mleStockedCard.getAtmCardPrimaryCta(),prCta);
        assertEquals(mleStockedCard.getAtmCardSecondaryCta(),secCta);
        assertEquals(mleStockedCard.getCardHeader(),"cardHeader");
        assertEquals(mleStockedCard.getCardFooter(),"cardFooter");
        mleStockedCard.initModel();
    }
}