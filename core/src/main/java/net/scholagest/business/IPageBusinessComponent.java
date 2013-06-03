package net.scholagest.business;

import java.util.Set;

import net.scholagest.objects.PageObject;

public interface IPageBusinessComponent {
    public Set<PageObject> getAllPages() throws Exception;
}
