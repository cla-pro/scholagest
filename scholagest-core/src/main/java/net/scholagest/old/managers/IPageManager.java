package net.scholagest.old.managers;

import java.util.Set;

import net.scholagest.old.objects.PageObject;

public interface IPageManager {
    public PageObject createPage(String pageName, String pagePath, Set<String> roles);

    Set<PageObject> getAllPages();
}
