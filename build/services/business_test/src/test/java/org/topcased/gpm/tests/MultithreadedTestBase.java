/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin), Sébastien René
 * (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.tests;

import java.util.HashMap;

import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.HibernateTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.ContextLocator;
import org.topcased.gpm.business.ServiceLocator;
import org.topcased.gpm.business.authorization.service.AuthorizationService;

/**
 * @author llatil
 */
public class MultithreadedTestBase extends AbstractBusinessServiceTestCase {

    /**
     * Global variable shared by all threads used to indicate a failed test.
     */
    private boolean failure = false;

    /**
     * Associate the exception cause with each threads (to know the exception
     * raised by each thread).
     */
    private HashMap<String, Exception> exceptionMap =
            new HashMap<String, Exception>();

    protected void runThreadedTests(ThreadedTest[] pTests) {

        Thread[] lThreads = new Thread[pTests.length];
        int i = 0;

        // Start the threads.
        for (ThreadedTest lTest : pTests) {
            lThreads[i] = new Thread(lTest);
            lThreads[i].start();
            ++i;
        }

        // Wait for completion of all of them.
        for (Thread lThread : lThreads) {
            try {
                lThread.join();
            }
            catch (InterruptedException e) {
            }
        }
    }

    /**
     * Set a global test failure indicator
     * 
     * @param pException
     *            The exception causing the failure
     * @param pThreadId
     *            The thread id
     */
    protected synchronized void setTestFailed(String pThreadId,
            Exception pException) {
        failure = true;
        exceptionMap.put(pThreadId, pException);
    }

    protected synchronized boolean isFailed() {
        return failure;
    }

    protected void logExceptions() {
        fail("Session error while executing several threads.");
    }

    /**
     * Base class for the threaded tests.
     * 
     * @author llatil
     */
    abstract class ThreadedTest implements Runnable {

        protected String threadId;

        /** The service locator. */
        protected ServiceLocator serviceLocatorLocal;

        /** The authorization service. */
        protected AuthorizationService authorizationServiceLocal;

        /** The Spring transaction manager */
        private HibernateTransactionManager transactionMgrLocal;

        /** The Spring transaction status */
        private TransactionStatus transactionStatusLocal;

        /** The user token. */
        protected String userTokenLocal;

        /** The Role session token. */
        protected String roleTokenLocal;

        /**
         * ThreadedTest ctor
         */
        public ThreadedTest(String pThreadId) {
            threadId = pThreadId;
        }

        /**
         * {@inheritDoc}
         * 
         * @see java.lang.Runnable#run()
         */
        public final void run() {
            try {
                setupTest();
                runTest();
            }
            catch (Exception lExl) {
                setTestFailed(threadId, lExl);
            }
            finally {
                teardownTest();
            }
        }

        /**
         * Must be implemented in derived classes.
         */
        abstract void runTest();

        protected void setupTest() {
            // Get the service locator (needed to force an initialization of
            // the business services).
            serviceLocatorLocal = ServiceLocator.instance();
            ApplicationContext lApplicationContext =
                    ContextLocator.getContext();

            transactionMgrLocal =
                    (HibernateTransactionManager) lApplicationContext.getBean("transactionManager");

            transactionMgrLocal.setRollbackOnCommitFailure(true);
            transactionMgrLocal.setGlobalRollbackOnParticipationFailure(true);

            transactionStatusLocal =
                    transactionMgrLocal.getTransaction(new DefaultTransactionAttribute(
                            TransactionDefinition.PROPAGATION_REQUIRES_NEW));

            transactionStatusLocal.setRollbackOnly();

            String[] lLogin = getAdminLogin();

            authorizationServiceLocal =
                    serviceLocatorLocal.getAuthorizationService();
            userTokenLocal =
                    authorizationServiceLocal.login(lLogin[0], lLogin[1]);
            roleTokenLocal =
                    authorizationServiceLocal.selectRole(userTokenLocal,
                            getAdminRoleName(), getProductName(),
                            getProcessName());
        }

        protected void teardownTest() {
            // close the test
            if (null != transactionStatusLocal) {
                transactionMgrLocal.rollback(transactionStatusLocal);
            }

            if (null != userTokenLocal) {
                authorizationServiceLocal.logout(userTokenLocal);
            }
        }
    }
}
