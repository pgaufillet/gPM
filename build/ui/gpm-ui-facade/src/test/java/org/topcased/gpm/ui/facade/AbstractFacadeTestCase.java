/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;
import net.sf.ehcache.CacheManager;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.HibernateTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.topcased.gpm.business.authorization.impl.filter.FilterAccessManager;
import org.topcased.gpm.business.link.service.LinkService;
import org.topcased.gpm.business.product.service.ProductService;
import org.topcased.gpm.business.search.service.SearchService;
import org.topcased.gpm.business.sheet.service.SheetService;
import org.topcased.gpm.instantiation.Instantiate;
import org.topcased.gpm.instantiation.options.InstantiateOptions;
import org.topcased.gpm.resetinstance.ResetInstance;
import org.topcased.gpm.ui.facade.server.FacadeLocator;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.server.authorization.UiUserSession;
import org.topcased.gpm.ui.facade.shared.filter.summary.UiFilterSummaries;
import org.topcased.gpm.ui.facade.shared.filter.summary.UiFilterSummary;

/**
 * Abstract class for all facade tests. It initializes a new database content
 * and logs in the user.
 * 
 * @author nveillet
 */
public class AbstractFacadeTestCase extends TestCase {

    /** Login parameters to use for the 'admin' user. */
    protected static final String[] ADMIN_LOGIN = { "admin", "admin" };

    /** The 'admin' role name. */
    private static final String ADMIN_ROLE_NAME = "admin";

    /** The date formatter. */
    private final DateFormat dateFormatter =
            new SimpleDateFormat("dd/MM/yyyy HH:mm");

    /** The default business process name. */
    protected static final String DEFAULT_PROCESS_NAME = "TestInstance";

    /** The default product name. */
    protected static final String DEFAULT_PRODUCT_NAME = "ROOT_PRODUCT";

    /** The default type product name. */
    private static final String DEFAULT_PRODUCT_TYPE_NAME = "PRODUCT";

    /**
     * Execution time difference tolerance for the test (percentage) : Warning
     * if test execution time is more than 30% less than expected time.
     */
    private static final double EXEC_TIME_DIFFERENCE_TOLERANCE = 30;

    /** Execution time tolerance for the test (percentage). */
    private static final double EXEC_TIME_TOLERANCE = 20;

    /** Logger. */
    protected static final Logger LOGGER =
            Logger.getLogger(AbstractFacadeTestCase.class);

    /** Flag used to check if the database has been created. */
    private static boolean staticDatabaseCreated = false;

    /** Login parameters to use for the 'user' user. */
    protected static final String[] USER_LOGIN = { "user", "user" };

    /** The 'user' role name. */
    private static final String USER_ROLE_NAME = "user";

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
     * This method is used to flush the various caches used in the gPM services
     * (in order to keep each unit test completely independant).
     */
    protected static void clearCache() {
        final ApplicationContext lApplicationContext =
                FacadeLocator.getContext();

        ((CacheManager) lApplicationContext.getBean("cacheManager")).clearAll();
        ((FilterAccessManager) lApplicationContext.getBean("filterAccessManager")).depreciateAll();

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("All business layer caches flushed.");
        }
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
     * Returns the name of the process.
     * 
     * @return The name of the process.
     */
    public static String getProcessName() {
        return DEFAULT_PROCESS_NAME;
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
     * Returns the name of the product type.
     * 
     * @return The name of the product type.
     */
    public static String getProductTypeName() {
        return DEFAULT_PRODUCT_TYPE_NAME;
    }

    /**
     * Returns the name of the 'user' role.
     * 
     * @return Name of the 'user' role.
     */
    protected static String getUserRoleName() {
        return USER_ROLE_NAME;
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

    /** Administrator user session. */
    protected UiUserSession adminUserSession;

    /**
     * Specify if the test should commit its transaction (instead of rollback
     * it). Normally tests MUST NOT commit their changes in DB.
     */
    private boolean commitTransaction = false;

    private FacadeLocator facadeLocator;

    /** The max elapsed time given to the test to execute. */
    private long maxElapsedTime = -1;

    /**
     * The name of the property containing the resource file name used by the
     * perf measurement.
     */
    protected String maxElapsedTimeResourceName = "gPM.benchmark-file";

    /** The name of the file where we store the time took by Unit Tests. */
    protected String perfLogFilename = "tu_facade_perf.csv";

    /** The startup time. */
    private long startupTime = -1;

    /** The name of the test method. */
    private String testMethodName = null;

    /** The test time. */
    protected long testTime = -1;

    /** The Spring transaction manager. */
    private HibernateTransactionManager transactionMgr;

    /** The Spring transaction status. */
    private TransactionStatus transactionStatus;

    /**
     * Constructs a new test case with default behavior.
     * <p>
     * The transaction used during the execution of this test case is
     * automatically rollbacked at the end of the test.
     */
    public AbstractFacadeTestCase() {
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
    public AbstractFacadeTestCase(boolean pCommitTransaction) {
        super();
        commitTransaction = pCommitTransaction;

        if (Boolean.parseBoolean(System.getProperty("gPM.disable-tests-db-init"))) {
            staticDatabaseCreated = true;
        }
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
        transactionMgr =
                (HibernateTransactionManager) FacadeLocator.getContext().getBean(
                        "transactionManager");

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
     * Gets the user session.
     * 
     * @return the user session
     */
    public final UiUserSession getAdminUserSession() {
        return adminUserSession;
    }

    /**
     * get facadeLocator
     * 
     * @return the facadeLocator
     */
    public FacadeLocator getFacadeLocator() {
        return facadeLocator;
    }

    /**
     * Get filters summaries as list
     * 
     * @param pFilterSummaries
     *            filters summaries
     * @return the filters summaries list
     */
    protected List<UiFilterSummary> getFilterSummariesAsList(
            UiFilterSummaries pFilterSummaries) {

        ArrayList<UiFilterSummary> lFilterSummaries =
                new ArrayList<UiFilterSummary>();

        for (UiFilterSummary lFilterSummary : pFilterSummaries.getTableProcessFilters()) {
            lFilterSummaries.add(lFilterSummary);
        }
        for (UiFilterSummary lFilterSummary : pFilterSummaries.getTableProductFilters()) {
            lFilterSummaries.add(lFilterSummary);
        }
        for (UiFilterSummary lFilterSummary : pFilterSummaries.getTableUserFilters()) {
            lFilterSummaries.add(lFilterSummary);
        }
        for (UiFilterSummary lFilterSummary : pFilterSummaries.getTreeFilters()) {
            lFilterSummaries.add(lFilterSummary);
        }

        return lFilterSummaries;
    }

    protected LinkService getLinkService() {
        return (LinkService) FacadeLocator.getContext().getBean(
                "linkServiceImpl");
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
                LOGGER.warn("No max elapsed time defined for method "
                        + lMethodName);
            }
            else {
                return Long.parseLong(lValue);
            }
        }
        catch (Exception e) {
            LOGGER.error(e);
        }
        return -1;
    }

    protected ProductService getProductService() {
        return (ProductService) FacadeLocator.getContext().getBean(
                "productServiceImpl");
    }

    protected SearchService getSearchService() {
        return (SearchService) FacadeLocator.getContext().getBean(
                "searchServiceImpl");
    }

    protected SheetService getSheetService() {
        return (SheetService) FacadeLocator.getContext().getBean(
                "sheetServiceImpl");
    }

    /**
     * Get the test user login and password
     * 
     * @return the test user login [0] and password [1]
     */
    protected String[] getUserLogin() {
        return USER_LOGIN;
    }

    /**
     * Login a user
     * 
     * @return Returns the UiUserSession object of the logged user
     */
    protected UiUserSession loginAsUser() {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Logging into the system as USER...");
        }
        UiUserSession lUserUserSession =
                facadeLocator.getAuthorizationFacade().login(USER_LOGIN[0],
                        USER_LOGIN[1]);
        lUserUserSession.setProcessName(getProcessName());

        UiSession lUserSession =
                facadeLocator.getAuthorizationFacade().connect(
                        lUserUserSession, getProductName(), getUserRoleName());
        lUserUserSession.addSession(lUserSession.getProductName(), lUserSession);

        String lHighProduct =
                facadeLocator.getAuthorizationFacade().getProductWithHighRole(
                        lUserUserSession);

        lUserSession =
                facadeLocator.getAuthorizationFacade().connect(
                        lUserUserSession,
                        lHighProduct,
                        facadeLocator.getAuthorizationFacade().getDefaultRole(
                                lUserUserSession, lHighProduct));
        lUserUserSession.setDefaultGlobalSession(lUserSession);

        return lUserUserSession;
    }

    protected void logoutAsUser(UiUserSession pUserSession) {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("loggin out USER role...");
        }
        facadeLocator.getAuthorizationFacade().logout(pUserSession);
    }

    /**
     * Custom setup method.
     * 
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() {
        facadeLocator = FacadeLocator.instance();
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

        adminUserSession =
                facadeLocator.getAuthorizationFacade().login(ADMIN_LOGIN[0],
                        ADMIN_LOGIN[1]);
        adminUserSession.setProcessName(getProcessName());

        // Add product session for admin
        UiSession lAdminSession =
                facadeLocator.getAuthorizationFacade().connect(
                        adminUserSession, getProductName(), getAdminRoleName());
        adminUserSession.addSession(lAdminSession.getProductName(),
                lAdminSession);

        // Add default  session for admin
        String lHighProduct =
                facadeLocator.getAuthorizationFacade().getProductWithHighRole(
                        adminUserSession);
        lAdminSession =
                facadeLocator.getAuthorizationFacade().connect(
                        adminUserSession,
                        lHighProduct,
                        facadeLocator.getAuthorizationFacade().getDefaultRole(
                                adminUserSession, lHighProduct));
        adminUserSession.setDefaultGlobalSession(lAdminSession);

        // Add default product session for admin
        lAdminSession =
                facadeLocator.getAuthorizationFacade().connect(
                        adminUserSession,
                        getProductName(),
                        facadeLocator.getAuthorizationFacade().getDefaultRole(
                                adminUserSession, getProductName()));
        adminUserSession.addDefaultSession(getProductName(), lAdminSession);
    }

    /**
     * Creation of the database.
     * <p>
     * The database is actually created only once per execution.
     */
    protected synchronized void setupDatabase() {
        if (!staticDatabaseCreated) {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Resetting TestInstance Database...");
            }

            // Reset the test database before running the tests.
            ResetInstance lResetInstance = new ResetInstance();
            lResetInstance.execute("tests_facade.properties");

            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("... Database created");
            }

            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Resetting TestInstanceBis Database...");
            }

            // Reset the test database before running the tests.
            Instantiate.main(new String[] { "-P", ADMIN_LOGIN[1], "-N",
                                           "TestInstanceBis", "-f",
                                           "metadata/producttypes/test-instance_product_types.xml" });
            Instantiate.main(new String[] { "-P", ADMIN_LOGIN[1] });
            Instantiate.main(new String[] { "-P", ADMIN_LOGIN[1], "-N",
                                           "TestInstanceBis", "-f",
                                           "data/products/test-instance_products.xml" });

            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("... Database created");
            }
        }
        staticDatabaseCreated = true;
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
            LOGGER.error("Timer not started");
            testTime = -1;
            return;
        }

        testTime = System.currentTimeMillis() - startupTime;
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Test Time : " + testTime + " ms");
        }

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
            long lMinTime =
                    maxElapsedTime
                            - Math.round(maxElapsedTime
                                    * EXEC_TIME_DIFFERENCE_TOLERANCE / 100);
            if (testTime <= lMinTime) {
                LOGGER.warn(testMethodName
                        + ": Max execution time not up to date ("
                        + maxElapsedTime + " ms.): more than "
                        + EXEC_TIME_DIFFERENCE_TOLERANCE
                        + "% difference with current execution time ("
                        + testTime + " ms.)");
            }
        }
    }

    /**
     * Stores the measures.
     */
    protected void storeTime() {
        try {
            if (testTime < 0) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Perf not tested");
                }
                return; // Perf not tested
            }

            File lPerfFile = new File(perfLogFilename);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Store results into "
                        + lPerfFile.getCanonicalPath());
            }

            BufferedWriter lWriter =
                    new BufferedWriter(new FileWriter(lPerfFile, true));
            lWriter.write(dateFormatter.format(new Date()) + ","
                    + getClass().getSimpleName() + "," + testMethodName + ","
                    + testTime + "\n");
            lWriter.flush();
            lWriter.close();
        }
        catch (Exception e) {
            LOGGER.error("Unable to store perf !");
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

        clearCache();

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Logging out...");
        }

        facadeLocator.getAuthorizationFacade().logout(adminUserSession);
        adminUserSession = null;
    }
}
