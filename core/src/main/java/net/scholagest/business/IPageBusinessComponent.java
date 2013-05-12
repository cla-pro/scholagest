package net.scholagest.business;

import java.util.Set;

import net.scholagest.database.ITransaction;
import net.scholagest.objects.PageObject;

public interface IPageBusinessComponent {
    public Set<PageObject> getAllPages(String requestId, ITransaction transaction) throws Exception;
}
