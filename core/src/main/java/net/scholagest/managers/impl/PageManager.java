package net.scholagest.managers.impl;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import net.scholagest.database.DatabaseException;
import net.scholagest.database.ITransaction;
import net.scholagest.managers.IPageManager;
import net.scholagest.managers.ontology.OntologyManager;
import net.scholagest.managers.ontology.types.DBSet;
import net.scholagest.objects.PageObject;

import com.google.inject.Inject;

public class PageManager extends ObjectManager implements IPageManager {
    @Inject
    public PageManager(OntologyManager ontologyManager) {
        super(ontologyManager);
    }

    @Override
    public PageObject createPage(String requestId, ITransaction transaction, String pageName, String pagePath, Set<String> roles) throws Exception {
        PageObject pageObject = createPageObject(transaction, pageName, pagePath, roles);

        persistObject(requestId, transaction, pageObject);
        transaction.insert(CoreNamespace.pageBase, pageName, pageObject.getKey(), null);

        return pageObject;
    }

    private PageObject createPageObject(ITransaction transaction, String pageName, String pagePath, Set<String> roles) throws DatabaseException {
        PageObject pageObject = new PageObject(UUID.randomUUID().toString());

        pageObject.setPath(pagePath);

        DBSet roleSet = new DBSet(transaction, UUID.randomUUID().toString());
        for (String role : roles) {
            roleSet.add(role);
        }
        pageObject.setRoles(roleSet);

        return pageObject;
    }

    @Override
    public Set<PageObject> getAllPages(String requestId, ITransaction transaction) throws Exception {
        Set<PageObject> pages = new HashSet<>();

        for (String pageName : transaction.getColumns(CoreNamespace.pageBase)) {
            String pageKey = (String) transaction.get(CoreNamespace.pageBase, pageName, null);
            PageObject pageObject = new PageObject(pageKey);

            pageObject.setProperties(getAllObjectProperties(requestId, transaction, pageKey));

            pages.add(pageObject);
        }

        return pages;
    }
}
