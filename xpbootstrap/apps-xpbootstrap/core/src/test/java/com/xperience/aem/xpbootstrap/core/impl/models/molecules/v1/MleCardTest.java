/**
 * 
 */
package com.xperience.aem.xpbootstrap.core.impl.models.molecules.v1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.xperience.aem.xpbootstrap.core.impl.models.molecules.v1.MleCardImpl;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import junitx.util.PrivateAccessor;

/**
 * @author abhijekumar
 *
 */
@ExtendWith({AemContextExtension.class,MockitoExtension.class})
class MleCardTest {


    public  AemContext context ;

    @InjectMocks
    private  MleCardImpl mleCard;


    @Mock
    Resource prCta;

    @Mock
    Resource secCta;

    @Mock
    Resource someResource;

    @Mock
    public  SlingHttpServletRequest request;


    /**
     * @throws java.lang.Exception
     */
    @BeforeEach
     void setUpBeforeClass() throws Exception {
        context.addModelsForClasses(MleCardImpl.class);
        request = context.request();
        PrivateAccessor.setField(mleCard, "atmIconSrcFileReference", "some/path");
        PrivateAccessor.setField(mleCard, "atmIconAltText", "atmIconAltText");
        PrivateAccessor.setField(mleCard, "atmCardTitle", "atmCardTitle");
        PrivateAccessor.setField(mleCard, "atmCardSubtitle", "atmCardSubtitle"); 
        PrivateAccessor.setField(mleCard, "atmCardDescription", "atmCardDescription");
        PrivateAccessor.setField(mleCard,"atmCardPrimaryCta",prCta);
        PrivateAccessor.setField(mleCard,"atmCardSecondaryCta",secCta);




    }

    @Test
    void test() {
        assertNotNull(mleCard.getAtmCardDescription());
        assertNotNull(mleCard.getAtmCardSubtitle());
        assertNotNull(mleCard.getAtmCardTitle());
        assertNotNull(mleCard.getAtmIconAltText());
        assertNotNull(mleCard.getAtmIconSrcFileReference());
        assertEquals(mleCard.getAtmCardPrimaryCta(),prCta);
        assertEquals(mleCard.getAtmCardSecondaryCta(),secCta);
        mleCard.initModel();
    }

}
