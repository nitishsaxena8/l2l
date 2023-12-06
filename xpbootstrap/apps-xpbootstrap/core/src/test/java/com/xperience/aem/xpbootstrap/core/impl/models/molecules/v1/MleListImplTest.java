package com.xperience.aem.xpbootstrap.core.impl.models.molecules.v1;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.designer.Style;
import com.xperience.aem.xpbootstrap.core.component.Utils.LinkManager;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.xperience.aem.xpbootstrap.core.impl.models.molecules.v1.MleListImpl.*;
import static com.xperience.aem.xpbootstrap.core.models.molecules.MleList.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class MleListImplTest {

    @InjectMocks
    private MleListImpl mleList;

    @Mock
    ValueMap properties;

    @Mock
    SlingHttpServletRequest request;

    @Mock
    Style currentStyle;

    @Mock
    Page currentPage;

    @Mock
    Resource resource;

    @Mock
    LinkManager linkManager;

    @BeforeEach
    void setup()
    {
        lenient().when(currentStyle.get(PN_SHOW_DESCRIPTION, SHOW_DESCRIPTION_DEFAULT)).thenReturn(true);
        lenient().when(properties.get(PN_SHOW_DESCRIPTION,true)).thenReturn(true);
        lenient().when(currentStyle.get(PN_SHOW_MODIFICATION_DATE, SHOW_MODIFICATION_DATE_DEFAULT)).thenReturn(true);
        lenient().when(properties.get(PN_SHOW_MODIFICATION_DATE,true)).thenReturn(true);
        lenient().when(currentStyle.get(PN_LINK_ITEMS, LINK_ITEMS_DEFAULT)).thenReturn(true);
        lenient().when(properties.get(PN_LINK_ITEMS,true)).thenReturn(true);
        lenient().when(currentStyle.get(PN_DATE_FORMAT, DATE_FORMAT_DEFAULT)).thenReturn("mm-dd-yyyy");
        lenient().when(properties.get(PN_DATE_FORMAT,"mm-dd-yyyy")).thenReturn("mm-dd-yyyy");
        lenient().when(currentStyle.get(PN_DISPLAY_ITEM_AS_TEASER, DISPLAY_ITEM_AS_TEASER_DEFAULT)).thenReturn(true);
        lenient().when(properties.get(PN_DISPLAY_ITEM_AS_TEASER,true)).thenReturn(true);
        lenient().when(currentStyle.get(PN_NUMBER_OF_CARDS_IN_ROW, PN_NUMBER_OF_CARDS_IN_ROW_DEFAULT)).thenReturn("4");
        lenient().when(properties.get(PN_NUMBER_OF_CARDS_IN_ROW,"4")).thenReturn("4");

    }

    @Test
    void testInit()
    {
        mleList.initModel();
    }
}