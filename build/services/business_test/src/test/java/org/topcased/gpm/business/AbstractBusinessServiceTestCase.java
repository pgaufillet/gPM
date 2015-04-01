/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Szadel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Properties;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;
import net.sf.ehcache.CacheManager;

import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.HibernateTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.topcased.gpm.business.authorization.impl.filter.FilterAccessManager;
import org.topcased.gpm.business.authorization.service.AuthorizationService;
import org.topcased.gpm.business.fields.service.FieldsService;
import org.topcased.gpm.business.fieldscontainer.service.FieldsContainerService;
import org.topcased.gpm.business.link.service.LinkService;
import org.topcased.gpm.business.revision.service.RevisionService;
import org.topcased.gpm.business.search.impl.query.FilterQueryConfigurator;
import org.topcased.gpm.business.search.impl.query.FilterResultIterator;
import org.topcased.gpm.business.search.service.ExecutableFilterData;
import org.topcased.gpm.business.search.service.FilterVisibilityConstraintData;
import org.topcased.gpm.business.sheet.service.SheetService;
import org.topcased.gpm.business.sheet.service.SheetSummaryData;
import org.topcased.gpm.instantiation.Instantiate;
import org.topcased.gpm.instantiation.options.InstantiateOptions;
import org.topcased.gpm.resetinstance.ResetInstance;

/**
 * Abstract class for all service tests. It initializes a new database content
 * and logs in the user.
 * <p>
 * See gPM test framework for details about products, sheet types...
 * 
 * @author tszadel
 */
public abstract class AbstractBusinessServiceTestCase extends TestCase {

    /** The name of the file where we store the time took by Unit Tests. */
    protected String perfLogFilename = "tu_perf.csv";

    /**
     * The name of the property containing the resource file name used by the
     * perf measurement.
     */
    protected String maxElapsedTimeResourceName = "gPM.benchmark-file";

    /** The date formatter. */
    private final DateFormat dateFormatter =
            new SimpleDateFormat("dd/MM/yyyy HH:mm");

    /** Login parameters to use for the 'admin' user. */
    protected static final String[] ADMIN_LOGIN =
            { GpmTestValues.USER_ADMIN, GpmTestValues.USER_ADMIN };

    /** The 'admin' role name. */
    private static final String ADMIN_ROLE_NAME = GpmTestValues.USER_ADMIN;

    /** Login parameters to use for the 'user2' user. */
    protected static final String[] USER2_LOGIN =
            { GpmTestValues.USER_USER2, "pwd2" };

    /** The 'normal' role name. */
    private static final String USER2_ROLE_NAME = "notadmin";

    /** The default type product name. */
    private static final String DEFAULT_PRODUCT_TYPE_NAME = "Store";

    /** The default product name. */
    protected static final String DEFAULT_PRODUCT_NAME =
            GpmTestValues.PRODUCT_BERNARD_STORE_NAME;

    /** The default business process name. */
    protected static final String DEFAULT_PROCESS_NAME =
            GpmTestValues.PROCESS_NAME;

    /** Invalid identifier to tests error cases. */
    protected static final String INVALID_CONTAINER_ID = "invalid_id";

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

    /** Fields container service. */
    protected FieldsContainerService fieldsContainerService;

    /** Revision service. */
    protected RevisionService revisionService;

    /** Administrator user session token. */
    protected String adminUserToken;

    /** Administrator role session token. */
    protected String adminRoleToken;

    /** 'normal' user session token (user2). */
    protected String normalUserToken;

    /** 'normal' role session token. (user2 with role USER2_ROLE_NAME) */
    protected String normalRoleToken;

    /** The startup time. */
    private long startupTime = -1;

    /** The test time. */
    protected long testTime = -1;

    /** The name of the test method. */
    private String testMethodName = null;

    /** The max elapsed time given to the test to execute. */
    private long maxElapsedTime = -1;

    /**
     * Specify if the test should commit its transaction (instead of rollback
     * it). Normally tests MUST NOT commit their changes in DB.
     */
    private boolean commitTransaction = false;

    /** The Spring transaction status. */
    private TransactionStatus transactionStatus;

    /** The Spring transaction manager. */
    private HibernateTransactionManager transactionMgr;

    /** Execution time tolerance for the test (percentage). */
    private static final double EXEC_TIME_TOLERANCE = 20;

    /**
     * Constructs a new test case with default behavior.
     * <p>
     * The transaction used during the execution of this test case is
     * automatically rollbacked at the end of the test.
     */
    public AbstractBusinessServiceTestCase() {
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
    public AbstractBusinessServiceTestCase(boolean pCommitTransaction) {
        super();
        commitTransaction = pCommitTransaction;

        if (Boolean.parseBoolean(System.getProperty("gPM.disable-tests-db-init"))) {
            staticDatabaseCreated = true;
        }
    }

    /**
     * Starts the time measurement. Suppose that the method is called
     * <i>testNormalCase</i>
     */
    protected void startTimer() {
        startTimer("testNormalCase");
    }

    /**
     * Starts the time measurement.
     * 
     * @param pTestMethodName
     *            The name of the test method.
     */
    protected void startTimer(String pTestMethodName) {
        testMethodName = pTestMethodName;
        // We get the max elapsed time
        maxElapsedTime = getMethodMaxElapsedTime();

        // Now, we can really start the timer
        startupTime = System.currentTimeMillis();
    }

    /**
     * Stops the timer.
     */
    protected void stopTimer() {
        if (startupTime < 0) {
            testTime = -1;
            return;
        }

        testTime = System.currentTimeMillis() - startupTime;

        if (maxElapsedTime > 0) {
            // Check execution duration of the test.
            // Bail out in error if the exec time exceeds the expected value
            long lMaxTime =
                    maxElapsedTime
                            + Math.round(maxElapsedTime * EXEC_TIME_TOLERANCE
                                    / 100);
            if (lMaxTime <= testTime) {
                fail(testMethodName + ": Execution time exceed. Max allowed : "
                        + maxElapsedTime + " ms (+" + EXEC_TIME_TOLERANCE
                        + "% = " + lMaxTime + " ms), elapsed : " + testTime
                        + " ms.");
            }
        }
    }

    /**
     * Returns the maximum time allowed to the test.
     * 
     * @return The maximum time allowed to the test or -1 if not defined.
     */
    private long getMethodMaxElapsedTime() {
        if (testMethodName == null || testMethodName.equals("")) {
            return -1;
        }
        try {
            String lFileName = System.getProperty(maxElapsedTimeResourceName);

            if (lFileName == null) {
                return -1;
            }
            InputStream lIn = new FileInputStream(lFileName);

            Properties lProps = new Properties();
            lProps.load(lIn);
            lIn.close();

            // We look for the methodName
            String lMethodName =
                    getClass().getSimpleName() + "." + testMethodName;
            String lValue = lProps.getProperty(lMethodName);
            if (lValue == null) {

            }
            else {
                return Long.parseLong(lValue);
            }
        }
        catch (Exception e) {

        }
        return -1;
    }

    /**
     * Stores the measures.
     */
    protected void storeTime() {
        try {
            if (testTime < 0) {
                return; // Perf not tested
            }

            File lPerfFile = new File(perfLogFilename);

            BufferedWriter lWriter =
                    new BufferedWriter(new FileWriter(lPerfFile, true));
            lWriter.write(dateFormatter.format(new Date()) + ","
                    + getClass().getSimpleName() + "," + testMethodName + ","
                    + testTime + "\n");
            lWriter.flush();
            lWriter.close();
        }
        catch (Exception e) {

        }
    }

    /**
     * Creation of the database.
     * <p>
     * The database is actually created only once per execution.
     */
    protected synchronized void setupDatabase() {
        if (!staticDatabaseCreated) {

            // Reset the test database before running the tests.
            ResetInstance lResetInstance = new ResetInstance();
            lResetInstance.execute("tests_db.properties");
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

        createTransaction();

        authorizationService = serviceLocator.getAuthorizationService();
        sheetService = serviceLocator.getSheetService();
        linkService = serviceLocator.getLinkService();
        fieldsService = serviceLocator.getFieldsService();
        fieldsContainerService = serviceLocator.getFieldsContainerService();
        revisionService = serviceLocator.getRevisionService();

        adminUserToken =
                authorizationService.login(ADMIN_LOGIN[0], ADMIN_LOGIN[1]);
        adminRoleToken =
                authorizationService.selectRole(adminUserToken,
                        ADMIN_ROLE_NAME,
                        /* getProductName() */null, getProcessName());

        normalUserToken =
                authorizationService.login(USER2_LOGIN[0], USER2_LOGIN[1]);
        normalRoleToken =
                authorizationService.selectRole(normalUserToken,
                        USER2_ROLE_NAME, getProductName(), getProcessName());
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
        storeTime();

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
        }
        else {
            transactionMgr.commit(transactionStatus);
        }

        clearCache();

        authorizationService.logout(adminUserToken);
    }

    /**
     * This method is used to flush the various caches used in the gPM services
     * (in order to keep each unit test completely independant).
     */
    protected static void clearCache() {
        final ApplicationContext lApplicationContext =
                ContextLocator.getContext();

        ((CacheManager) lApplicationContext.getBean("cacheManager")).clearAll();
        ((FilterAccessManager) lApplicationContext.getBean("filterAccessManager")).depreciateAll();
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
     * Returns the 'normal user' login parameters.
     * 
     * @return A String array containing both user login and its password.
     */
    protected static String[] getNormalUserLogin() {
        return USER2_LOGIN;
    }

    /**
     * Returns the name of the 'normal user' role.
     * 
     * @return Name of the 'normal user' role.
     */
    protected static String getNormalUserRoleName() {
        return USER2_ROLE_NAME;
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

        lOpts.setUser(GpmTestValues.USER_ADMIN, GpmTestValues.USER_ADMIN);
        lOpts.setProcessName(pProcessName);
        lOpts.setFilename(pFilePath);
        //Skip auto generate schema
        lOpts.setAutoGenerateDynamicDb(false);
        Instantiate.createInstance(lOpts);
    }

    /**
     * Assert that the two arrays have the same content, but not necessarily in
     * the same order.
     * 
     * @param pMessage
     *            Message to display when assertion is incorrect
     * @param pExpectedArray
     *            Expected array
     * @param pActualArray
     *            Actual array
     */
    public static void assertEqualsUnordered(String pMessage,
            Object[] pExpectedArray, Object[] pActualArray) {
        String lErrorMsg = StringUtils.EMPTY;
        if (pMessage != null && pMessage.length() != 0) {
            lErrorMsg = pMessage + "\n";
        }

        assertEquals(lErrorMsg + "The array has not the expected length.",
                pExpectedArray.length, pActualArray.length);

        for (Object lActual : pActualArray) {
            if (!ArrayUtils.contains(pExpectedArray, lActual)) {
                lErrorMsg +=
                        "Actual value '" + lActual
                                + "' not present in the expected values.";
                throw new AssertionFailedError(lErrorMsg);
            }
        }
    }

    /**
     * Assert that the two arrays have the same content, but not necessarily in
     * the same order.
     * 
     * @param pExpectedArray
     *            Expected array
     * @param pActualArray
     *            Actual array
     */
    public static void assertEqualsUnordered(Object[] pExpectedArray,
            Object[] pActualArray) {
        assertEqualsUnordered(null, pExpectedArray, pActualArray);
    }

    /**
     * Assert that the two arrays have the same content, in the same order.
     * 
     * @param pMessage
     *            Message to display when assertion is incorrect
     * @param pExpectedArray
     *            Expected array
     * @param pActualArray
     *            Actual array
     */
    public static void assertEqualsOrdered(String pMessage,
            Object[] pExpectedArray, Object[] pActualArray) {
        String lErrorMsg = StringUtils.EMPTY;
        if (pMessage != null && pMessage.length() != 0) {
            lErrorMsg = pMessage + "\n";
        }

        assertEquals(lErrorMsg + "The array has not the expected length",
                pExpectedArray.length, pActualArray.length);

        int i = 0;
        for (Object lActual : pActualArray) {
            assertEquals(lErrorMsg + "Element #" + i
                    + " in the array has not the expected value",
                    pExpectedArray[i++], lActual);
        }
    }

    /**
     * Assert that the two arrays have the same content, in the same order.
     * 
     * @param pExpectedArray
     *            Expected array
     * @param pActualArray
     *            Actual array
     */
    public static void assertEqualsOrdered(Object[] pExpectedArray,
            Object[] pActualArray) {
        assertEqualsOrdered(null, pExpectedArray, pActualArray);
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
     * Gets the fields container service.
     * 
     * @return the fields container service
     */
    public final FieldsContainerService getFieldsContainerService() {
        return fieldsContainerService;
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
     * Execute an executable filter data on sheets with a userLogin in a product
     * and a business process, with a limited number of results
     * 
     * @param pRoleToken
     *            the role token
     * @param pBusinessProcessName
     *            the business process name
     * @param pProductName
     *            the product name
     * @param pUserLogin
     *            the user login
     * @param pExecutableFilterData
     *            the executable filter data
     * @return the collection of sheet summary data
     */
    @SuppressWarnings("unchecked")
	protected Collection<SheetSummaryData> executeSheetFilter(
            String pRoleToken, String pBusinessProcessName,
            String pProductName, String pUserLogin,
            ExecutableFilterData pExecutableFilterData) {
        FilterResultIterator<SheetSummaryData> lResult =
                serviceLocator.getSearchService().executeFilter(
                        pRoleToken,
                        pExecutableFilterData,
                        new FilterVisibilityConstraintData(pUserLogin,
                                pBusinessProcessName, pProductName),
                        new FilterQueryConfigurator());
        return IteratorUtils.toList(lResult);
    }
}
