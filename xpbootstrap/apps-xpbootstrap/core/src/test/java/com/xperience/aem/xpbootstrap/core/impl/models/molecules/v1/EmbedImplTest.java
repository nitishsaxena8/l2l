package com.xperience.aem.xpbootstrap.core.impl.models.molecules.v1;

import com.day.cq.wcm.api.designer.Style;
import com.xperience.aem.xpbootstrap.core.component.Utils.UrlProcessor;
import com.xperience.aem.xpbootstrap.core.models.molecules.Embed;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import junitx.util.PrivateAccessor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class EmbedImplTest {

    @InjectMocks
    private EmbedImpl embed;

    @Mock
    private Style style;

    @Mock
    UrlProcessor urlProcessor;


    private AemContext context;

    @Mock
    private UrlProcessor.Result result;

    private static final String BASE = "/embed";
    private static final String CONTENT_ROOT = "/content";
    private static final String ROOT_PAGE = "/content/embed";
    private static final String GRID = ROOT_PAGE + "/jcr:content/root/responsivegrid";
    private static final String EMBED_1 = "/embed1";
    private static final String EMBED_2 = "/embed2";
    private static final String EMBED_3 = "/embed3";
    private static final String PATH_EMBED_1 = GRID + EMBED_1;
    private static final String PATH_EMBED_2 = GRID + EMBED_2;
    private static final String PATH_EMBED_3 = GRID + EMBED_3;
    private static final String EMBED_PATH = "/content/xpbootstrap/us/en/space/jcr:content/root/container/mleembed";


    @Test
    void testUrl() throws NoSuchFieldException {
        PrivateAccessor.setField(embed, "type", "url");
        PrivateAccessor.setField(embed, "url", "url");
        Mockito.when(urlProcessor.process("url")).thenReturn(result);
        List<UrlProcessor> urlProcessorList = new ArrayList<>();
        urlProcessorList.add(urlProcessor);
        PrivateAccessor.setField(embed, "urlProcessors", urlProcessorList);
        Mockito.when(style.get(Embed.PN_DESIGN_URL_DISABLED, false)).thenReturn(false);
        Mockito.when(style.get(Embed.PN_DESIGN_HTML_DISABLED, false)).thenReturn(true);
        Mockito.when(style.get(Embed.PN_DESIGN_EMBEDDABLES_DISABLED, false)).thenReturn(true);
        embed.initModel();
        assertEquals(Embed.Type.URL, embed.getType());
        assertNull(embed.getHtml());
        assertNull(embed.getEmbeddableResourceType());
        assertEquals("url", embed.getUrl());
        assertNotNull(embed.getResult());
    }

    @Test
    void testHtml() throws NoSuchFieldException {
        PrivateAccessor.setField(embed, "type", "html");
        PrivateAccessor.setField(embed, "html", "<div>Html<div>");
        Mockito.when(style.get(Embed.PN_DESIGN_URL_DISABLED, false)).thenReturn(true);
        Mockito.when(style.get(Embed.PN_DESIGN_HTML_DISABLED, false)).thenReturn(false);
        Mockito.when(style.get(Embed.PN_DESIGN_EMBEDDABLES_DISABLED, false)).thenReturn(true);
        embed.initModel();
        assertNull(embed.getUrl());
        assertNull(embed.getEmbeddableResourceType());
        assertEquals("<div>Html<div>", embed.getHtml());
    }


}
