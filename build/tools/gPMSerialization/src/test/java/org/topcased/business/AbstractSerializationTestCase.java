/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.business;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;
import net.sf.ehcache.CacheManager;

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
import org.topcased.gpm.business.fields.service.FieldsService;
import org.topcased.gpm.business.link.service.LinkService;
import org.topcased.gpm.business.revision.service.RevisionService;
import org.topcased.gpm.business.serialization.converter.XMLConverter;
import org.topcased.gpm.business.sheet.service.SheetService;
import org.topcased.gpm.instantiation.Instantiate;
import org.topcased.gpm.instantiation.options.InstantiateOptions;
import org.topcased.gpm.resetinstance.ResetInstance;

import com.thoughtworks.xstream.io.HierarchicalStreamReader;

/**
 * Abstract class for all service tests. It initializes a new database content
 * and logs in the user.
 * <p>
 * See gPM test framework for details about products, sheet types...
 * 
 * @author nveillet
 */
public abstract class AbstractSerializationTestCase extends TestCase {

    /** Logger. */
    private static final Logger LOGGER =
            Logger.getLogger(AbstractSerializationTestCase.class);

    /** The name of the file where we store the time took by Unit Tests. */
    protected String perfLogFilename = "tu_perf.csv";

    /** The folder of migration */
    protected final static String MIGRATION_BASE_DIRECTORY =
            "src" + File.separator + "test" + File.separator + "resources"
                    + File.separator + "export";

    /**
     * The name of the property containing the resource file name used by the
     * perf measurement.
     */
    protected String maxElapsedTimeResourceName = "gPM.benchmark-file";

    /** Login parameters to use for the 'admin' user. */
    protected static final String[] ADMIN_LOGIN = { "admin", "admin" };

    /** The 'admin' role name. */
    private static final String ADMIN_ROLE_NAME = "admin";

    /** The default type product name. */
    private static final String DEFAULT_PRODUCT_TYPE_NAME = "Store";

    /** The default product name. */
    protected static final String DEFAULT_PRODUCT_NAME = "Bernard's store";

    /** The default business process name. */
    protected static final String DEFAULT_PROCESS_NAME = "PET STORE";

    /** Flag used to check if the database has been created. */
    private static boolean staticDatabaseCreated = false;

    /** The service locator. */
    protected ServiceLocator serviceLocator;

    /** Authorization service. */
    protected AuthorizationService authorizationService;

    /** Sheet service. */
    protected SheetService sheetService;

    /** Link service. */
    protected LinkService linkService;

    /** Fields service. */
    protected FieldsService fieldsService;

    /** Revision service. */
    protected RevisionService revisionService;

    /** Administrator user session token. */
    protected String adminUserToken;

    /** Administrator role session token. */
    protected String adminRoleToken;

    /**
     * Specify if the test should commit its transaction (instead of rollback
     * it). Normally tests MUST NOT commit their changes in DB.
     */
    private boolean commitTransaction = false;

    /** The Spring transaction status. */
    private TransactionStatus transactionStatus;

    /** The Spring transaction manager. */
    private HibernateTransactionManager transactionMgr;

    /**
     * The supported export tag
     */
    private static Set<String> staticSupportedTags = new HashSet<String>();
    static {
        staticSupportedTags.add("products");
        staticSupportedTags.add("productLinks");
        staticSupportedTags.add("sheets");
        staticSupportedTags.add("sheetLinks");
        staticSupportedTags.add("users");
        staticSupportedTags.add("userRoles");
        staticSupportedTags.add("filters");
        staticSupportedTags.add("categories");
        staticSupportedTags.add("environments");
    }

    /**
     * The supported export tag container
     */
    private static Set<String> staticSupportedContainerTags =
            new HashSet<String>();
    static {
        staticSupportedContainerTags.add("dictionary");
    }

    /**
     * Constructs a new test case with default behavior.
     * <p>
     * The transaction used during the execution of this test case is
     * automatically rollbacked at the end of the test.
     */
    public AbstractSerializationTestCase() {
        this(false);
    }

    /**
     * Constructs a new test case with specified transaction behavior.
     * 
     * @param pCommitTransaction
     *            Specify if the transaction must be committed or not. Please
     *            note that tests SHOULD NOT commit their changes in DB (unless
     *            they have some <b>very good</b> reasons to do it).
     */
    public AbstractSerializationTestCase(boolean pCommitTransaction) {
        super();
        commitTransaction = pCommitTransaction;

        if (Boolean.parseBoolean(System.getProperty("gPM.disable-tests-db-init"))) {
            staticDatabaseCreated = true;
        }
    }

    /**
     * Creation of the database.
     * <p>
     * The database is actually created only once per execution.
     */
    private void setupDatabase() {
        if (!staticDatabaseCreated) {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Resetting Database...");
            }

            // Reset the test database before running the tests.
            ResetInstance lResetInstance = new ResetInstance();
            lResetInstance.execute("tests_db.properties");

            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("... Database created");
            }
        }
        staticDatabaseCreated = true;
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
     * Custom setup method.
     * 
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() {
        setupDatabase();

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Logging into the system...");
        }

        createTransaction();

        String[] lLogin = getAdminLogin();

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("\tLogin : " + lLogin[0]);
            LOGGER.debug("\tPassword : " + lLogin[1]);
            LOGGER.debug("\tRole : " + getAdminRoleName());
            LOGGER.debug("\tProduct Name : " + getProductName());
            LOGGER.debug("\tProcess Name : " + getProcessName());
        }

        authorizationService = serviceLocator.getAuthorizationService();
        sheetService = serviceLocator.getSheetService();
        linkService = serviceLocator.getLinkService();
        fieldsService = serviceLocator.getFieldsService();
        revisionService = serviceLocator.getRevisionService();

        adminUserToken =
                authorizationService.login(ADMIN_LOGIN[0], ADMIN_LOGIN[1]);
        adminRoleToken =
                authorizationService.selectRole(adminUserToken,
                        ADMIN_ROLE_NAME,
                        /* getProductName() */null, getProcessName());
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
            // Do flush before rollback for check database integrity
            transactionMgr.getSessionFactory().getCurrentSession().flush();

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
            commit();
        }

        clearCache();

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Logging out...");
        }
        authorizationService.logout(adminUserToken);
    }

    /**
     * This method is used to flush the various caches used in the gPM services
     * (in order to keep each unit test completely independant).
     */
    protected static void clearCache() {
        ApplicationContext lApplicationContext = ContextLocator.getContext();

        CacheManager lCacheMgr =
                (CacheManager) lApplicationContext.getBean("cacheManager");
        lCacheMgr.clearAll();

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("All business layer caches flushed.");
        }
    }

    /**
     * Returns the 'admin' login parameters.
     * 
     * @return A String array containing both user login and its password.
     */
    protected static String[] getAdminLogin() {
        return ADMIN_LOGIN;
    }

    /**
     * Returns the name of the 'admin' role.
     * 
     * @return Name of the 'admin' role.
     */
    protected static String getAdminRoleName() {
        return ADMIN_ROLE_NAME;
    }

    /**
     * Returns the name of the product type.
     * 
     * @return The name of the product type.
     */
    public static String getProductTypeName() {
        return DEFAULT_PRODUCT_TYPE_NAME;
    }

    /**
     * Returns the name of the product.
     * 
     * @return The name of the product.
     */
    public static String getProductName() {
        return DEFAULT_PRODUCT_NAME;
    }

    /**
     * Returns the name of the process.
     * 
     * @return The name of the process.
     */
    public static String getProcessName() {
        return DEFAULT_PROCESS_NAME;
    }

    /**
     * instantiate.
     * 
     * @param pProcessName
     *            the process to instantiate
     * @param pFilePath
     *            the input XML file
     */
    protected static void instantiate(String pProcessName, String pFilePath) {
        InstantiateOptions lOpts = new InstantiateOptions();

        lOpts.setUser("admin", "admin");
        lOpts.setProcessName(pProcessName);
        lOpts.setFilename(pFilePath);
        //Skip auto generate schema
        lOpts.setAutoGenerateDynamicDb(false);
        Instantiate.createInstance(lOpts);
    }

    /**
     * Stop the test, and exit as a failure.
     * <p>
     * The exception causing the failure is reported on stderr.
     * 
     * @param pCause
     *            Exception resulting in the test failure.
     */
    public static void fail(Throwable pCause) {
        System.err.println("------------------\n" + "Exception "
                + pCause.getClass().getName() + ": " + pCause.getMessage()
                + "\n------------------");
        printStack(pCause);
        if (pCause.getCause() != null) {
            Throwable lCause = pCause.getCause();
            System.err.println("------------------\n" + "Caused by exception "
                    + lCause.getClass().getName() + ": " + lCause.getMessage()
                    + "\n------------------");
            printStack(lCause);
        }
        fail();
    }

    /**
     * Print an exception stack trace on stderr.
     * 
     * @param pExc
     *            Exception content to use to dump stack
     */
    private static void printStack(Throwable pExc) {
        StackTraceElement[] lStack = pExc.getStackTrace();
        for (StackTraceElement lTraceElem : lStack) {
            System.err.println(lTraceElem.toString());
        }
    }

    /**
     * Gets the service locator.
     * 
     * @return the service locator
     */
    public final ServiceLocator getServiceLocator() {
        return serviceLocator;
    }

    /**
     * Gets the authorization service.
     * 
     * @return the authorization service
     */
    public final AuthorizationService getAuthorizationService() {
        return authorizationService;
    }

    /**
     * Gets the sheet service.
     * 
     * @return the sheet service
     */
    public final SheetService getSheetService() {
        return sheetService;
    }

    /**
     * Gets the link service.
     * 
     * @return the link service
     */
    public final LinkService getLinkService() {
        return linkService;
    }

    /**
     * Gets the fields service.
     * 
     * @return the fields service
     */
    public final FieldsService getFieldsService() {
        return fieldsService;
    }

    /**
     * Gets the revision service.
     * 
     * @return the revision service
     */
    public final RevisionService getRevisionService() {
        return revisionService;
    }

    /**
     * Gets the user token.
     * 
     * @return the user token
     */
    public final String getAdminUserToken() {
        return adminUserToken;
    }

    /**
     * Gets the role token.
     * 
     * @return the role token
     */
    public final String getAdminRoleToken() {
        return adminRoleToken;
    }

    /**
     * Commit transaction
     */
    protected void commit() {
        transactionMgr.commit(transactionStatus);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Transaction committed.");
        }
    }

    /**
     * Get object list on a file
     * 
     * @param pConverter
     *            the XML converter
     * @param pHierarchicalReader
     *            the hierarchical reader
     * @return object list
     * @throws IOException
     *             IO exception
     * @throws ClassNotFoundException
     *             ClassNotFound exception
     */
    protected List<Object> getObjectList(XMLConverter pConverter,
            HierarchicalStreamReader pHierarchicalReader) throws IOException,
        ClassNotFoundException {

        List<Object> lList = new ArrayList<Object>();

        while (pHierarchicalReader.hasMoreChildren()) {
            pHierarchicalReader.moveDown();
            String lCurrentNodeName = pHierarchicalReader.getNodeName();

            if (staticSupportedTags.contains(lCurrentNodeName)) {
                ObjectInputStream lOis = pConverter.createObjectInputStream();
                try {
                    while (true) {
                        Object lObject = lOis.readObject();
                        lList.add(lObject);
                    }
                }
                catch (EOFException e) {
                    // End of the object stream
                }
            }
            else if (staticSupportedContainerTags.contains(lCurrentNodeName)) {
                lList.addAll(getObjectList(pConverter, pHierarchicalReader));
            }

            pHierarchicalReader.moveUp();
        }

        return lList;
    }

    /**
     * Delete all file hierarchy
     * 
     * @param pFile
     *            the file to delete
     */
    protected void deleteFile(File pFile) {
        if (pFile.isDirectory()) {
            for (File lChild : pFile.listFiles()) {
                deleteFile(lChild);
            }
        }
        pFile.delete();
    }
}
