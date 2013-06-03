package net.scholagest.utils;

import net.scholagest.database.ITransaction;

import org.apache.shiro.subject.Subject;

public class ScholagestThreadLocal {
    private static final ThreadLocal<String> requestIdLocal = new ThreadLocal<String>() {
        @Override
        protected String initialValue() {
            return "";
        }
    };

    private static final ThreadLocal<ITransaction> transactionLocal = new ThreadLocal<ITransaction>() {
        @Override
        protected ITransaction initialValue() {
            return null;
        }
    };

    private static final ThreadLocal<Subject> subjectLocal = new ThreadLocal<Subject>() {
        @Override
        protected Subject initialValue() {
            return null;
        }
    };

    public static String getRequestId() {
        return requestIdLocal.get();
    }

    public static void setRequestId(String requestId) {
        requestIdLocal.set(requestId);
    }

    public static ITransaction getTransaction() {
        return transactionLocal.get();
    }

    public static void setTransaction(ITransaction transaction) {
        transactionLocal.set(transaction);
    }

    public static Subject getSubject() {
        return subjectLocal.get();
    }

    public static void setSubject(Subject subject) {
        subjectLocal.set(subject);
    }
}
