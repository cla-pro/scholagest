package net.scholagest.old.business;

import java.util.Set;

import net.scholagest.old.objects.PageObject;

public interface IPageBusinessComponent {
    public Set<PageObject> getAllPages() throws Exception;
}
