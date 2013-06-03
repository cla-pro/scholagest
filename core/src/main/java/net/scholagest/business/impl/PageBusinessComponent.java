package net.scholagest.business.impl;

import java.util.Set;

import net.scholagest.business.IPageBusinessComponent;
import net.scholagest.managers.IPageManager;
import net.scholagest.objects.PageObject;

import com.google.inject.Inject;

public class PageBusinessComponent implements IPageBusinessComponent {
    private IPageManager pageManager;

    @Inject
    public PageBusinessComponent(IPageManager pageManager) {
        this.pageManager = pageManager;
    }

    @Override
    public Set<PageObject> getAllPages() throws Exception {
        return pageManager.getAllPages();
    }
}
