package net.scholagest.business.impl;

import java.util.Set;

import net.scholagest.business.IPageBusinessComponent;
import net.scholagest.database.ITransaction;
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
    public Set<PageObject> getAllPages(String requestId, ITransaction transaction) throws Exception {
        return pageManager.getAllPages(requestId, transaction);
    }
}
