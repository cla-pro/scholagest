package net.scholagest.managers;

import java.util.Set;

import net.scholagest.objects.PageObject;

public interface IPageManager {
    public PageObject createPage(String pageName, String pagePath, Set<String> roles) throws Exception;

    Set<PageObject> getAllPages() throws Exception;
}
