package net.scholagest.business.impl;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;

import net.scholagest.business.IPageBusinessComponent;
import net.scholagest.managers.IPageManager;
import net.scholagest.objects.PageObject;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class PageBusinessComponentTest {
    private IPageManager pageManager;

    private IPageBusinessComponent testee;

    @Before
    public void setup() {
        pageManager = Mockito.mock(IPageManager.class);

        testee = new PageBusinessComponent(pageManager);
    }

    @Test
    public void testGetAllPages() throws Exception {
        Set<PageObject> pages = new HashSet<>();

        Mockito.when(pageManager.getAllPages()).thenReturn(pages);

        Set<PageObject> allPages = testee.getAllPages();
        assertEquals(pages, allPages);
    }
}
