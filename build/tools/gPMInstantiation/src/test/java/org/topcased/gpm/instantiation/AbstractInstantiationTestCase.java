/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.instantiation;

import java.util.LinkedList;
import java.util.List;

import junit.framework.TestCase;
import net.sf.ehcache.CacheManager;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.HibernateTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.topcased.gpm.business.ContextLocator;
import org.topcased.gpm.business.ServiceLocator;
import org.topcased.gpm.business.authorization.service.AuthorizationService;
import org.topcased.gpm.instantiation.options.InstantiateOptions;
import org.topcased.gpm.tests.TestUtils;

/**
 * AbstractInstantiationTestCase Base elements definition for Instantiation
 * tests. PROCESS_NAME = InstantiationTest;
 * 
 * @author mkargbo
 */
public class AbstractInstantiationTestCase extends TestCase {

    private static final Logger LOGGER =
            Logger.getLogger(AbstractInstantiationTestCase.class);

    private static final String BASIC_INSTANCE_FILE = "BasicInstance.xml";

    protected String processName = "InstantiationTest";

    protected String adminLogin = "admin";

    protected String adminPwd = "admin";

    protected String adminRoleName = "admin";

    protected String adminUserToken;

    protected String adminRoleToken;

    private String additionalOption = StringUtils.EMPTY;

    protected ServiceLocator serviceLocator;

    protected AuthorizationService authorizationService;

    /**
     * Specify if the test should commit its transaction (instead of rollback
     * it). Normally tests MUST NOT commit their changes in DB.
     */
    private boolean commitTransaction = false;

    /** The Spring transaction status. */
    private TransactionStatus transactionStatus;

    /** The Spring transaction manager. */
    private HibernateTransactionManager transactionMgr;

    /** Flag used to check if the database has been created. */
    protected static boolean staticDatabaseCreated = false;

    /**
     * {@inheritDoc}
     * 
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        setupDatabase();

        createTransaction();

        authorizationService = serviceLocator.getAuthorizationService();

        adminUserToken = authorizationService.login(adminLogin, adminPwd);
        adminRoleToken =
                authorizationService.selectRole(adminUserToken, adminRoleName,
                /* getProductName() */null, processName);
    }

    public ServiceLocator getServiceLocator() {
        return serviceLocator;
    }

    public void setServiceLocator(ServiceLocator pServiceLocator) {
        serviceLocator = pServiceLocator;
    }

    public String getAdditionalOption() {
        return additionalOption;
    }

    public void setAdditionalOption(String pAdditionalOption) {
        additionalOption = pAdditionalOption;
    }

    private void setupDatabase() {
        if (!staticDatabaseCreated) {
            Instantiate.dropDataBase("dropTable.properties");

            LOGGER.info("--== Recreate schema ==--");
            TestUtils.recreateDatabaseSchema();

            // Close current Hibernate session
            LOGGER.info("--== Close current session (if open) ==--");
            TestUtils.closeHibernateSession();

            LOGGER.info("Database structure initialization ...");

            Instantiate.createAdminUser(adminLogin);

            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Test process instantiation ...");
            }
            InstantiateOptions lInstOpts = new InstantiateOptions();

            lInstOpts.setUser(adminLogin, adminPwd);
            lInstOpts.setProcessName(processName);
            //Skip auto generate schema
            lInstOpts.setAutoGenerateDynamicDb(false);

            lInstOpts.setFilename(BASIC_INSTANCE_FILE);
            Instantiate.createInstance(lInstOpts);

            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("--== ResetInstance Finished ==--");
            }

            staticDatabaseCreated = true;
        }
    }

    /**
     * Instantiates the given file.<br />
     * Use the command line (@see {@link Instantiate#main(String[])}
     * <ul>
     * <li>the user login
     * <li>The user password
     * <li>The process name
     * <li>The additional options (if exists)
     * </ul>
     * 
     * @param pFileName
     *            File to instantiate.
     */
    protected void instantiate(String pFileName) {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Test process instantiation ...");
        }
        Instantiate.main(getInstantiationCommandLine(pFileName));
    }

    private String[] getInstantiationCommandLine(String pFileName) {
        List<String> lCommand = new LinkedList<String>();
        lCommand.add("-U");
        lCommand.add(adminLogin);
        lCommand.add("-P");
        lCommand.add(adminPwd);
        lCommand.add("-N");
        lCommand.add(processName);
        lCommand.add("-f");
        lCommand.add(pFileName);
        //Skip dynamic schema for tests
        lCommand.add("-TU");

        if (StringUtils.isNotBlank(additionalOption)) {
            lCommand.add("-o");
            lCommand.add(additionalOption);
        }

        return lCommand.toArray(new String[lCommand.size()]);
    }

    /**
     * Create a transaction used in a TU execution.
     * <p>
     * The goal here is to provide an already created transaction for the
     * transaction mgr used by the business services. This transaction is then
     * re-used (<i>Propagation.REQUIRED</i>) in all services calls.
     * <p>
     * Finally the transaction is rollbacked in the teardown() method to prevent
     * any DB changes.
     */
    protected void createTransaction() {
        // Get the service locator (needed to force an initialization of
        // the business services).
        serviceLocator = ServiceLocator.instance();
        ApplicationContext lApplicationContext = ContextLocator.getContext();

        transactionMgr =
                (HibernateTransactionManager) lApplicationContext.getBean("transactionManager");

        transactionMgr.setRollbackOnCommitFailure(true);
        transactionMgr.setGlobalRollbackOnParticipationFailure(true);

        DefaultTransactionDefinition lTransactionOptions =
                new DefaultTransactionAttribute(
                        TransactionDefinition.PROPAGATION_REQUIRED);
        // lTransactionOptions.setIsolationLevel(TransactionDefinition.ISOLATION_READ_UNCOMMITTED);

        transactionStatus = transactionMgr.getTransaction(lTransactionOptions);
        if (!commitTransaction) {
            transactionStatus.setRollbackOnly();
        }
    }

    /**
     * This method is used to flush the various caches used in the gPM services
     * (in order to keep each unit test completely independant).
     */
    protected static void flushCache() {
        ApplicationContext lApplicationContext = ContextLocator.getContext();

        CacheManager lCacheMgr =
                (CacheManager) lApplicationContext.getBean("cacheManager");
        lCacheMgr.clearAll();
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("All business layer caches flushed.");
        }
    }

    /**
     * This is an overriding method.
     * 
     * @throws Exception
     *             Exception.
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        if (!commitTransaction) {
            // Rollback the transaction used for the test.
            // This MUST be done before the logout() call, because this
            // transaction may have been tagged as 'global rollback'
            // (in an error case test), and the logout() call will
            // try to tag the transaction as committed (and Spring does
            // not allow a transaction to be both committed and rollbacked !)
            transactionMgr.rollback(transactionStatus);

            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Transaction rolled back.");
            }
        }
        else {
            transactionMgr.commit(transactionStatus);
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Transaction committed.");
            }
        }

        flushCache();
    }
}
