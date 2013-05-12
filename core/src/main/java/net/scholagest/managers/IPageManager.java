package net.scholagest.managers;

import java.util.Set;

import net.scholagest.database.ITransaction;
import net.scholagest.objects.PageObject;

public interface IPageManager {
    public PageObject createPage(String requestId, ITransaction transaction, String pageName, String pagePath, Set<String> roles) throws Exception;

    Set<PageObject> getAllPages(String requestId, ITransaction transaction) throws Exception;
}
