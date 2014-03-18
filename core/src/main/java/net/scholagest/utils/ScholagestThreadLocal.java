package net.scholagest.utils;

import net.scholagest.old.database.ITransaction;

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

    private static final ThreadLocal<String> sessionIdLocal = new ThreadLocal<String>() {
        @Override
        protected String initialValue() {
            return null;
        }
    };

    public static String getRequestId() {
        return requestIdLocal.get();
    }

    public static void setRequestId(final String requestId) {
        requestIdLocal.set(requestId);
    }

    public static ITransaction getTransaction() {
        return transactionLocal.get();
    }

    public static void setTransaction(final ITransaction transaction) {
        transactionLocal.set(transaction);
    }

    public static Subject getSubject() {
        return subjectLocal.get();
    }

    public static void setSubject(final Subject subject) {
        subjectLocal.set(subject);
    }

    public static String getSessionId() {
        return sessionIdLocal.get();
    }

    public static void setSessionId(final String sessionId) {
        sessionIdLocal.set(sessionId);
    }
}
