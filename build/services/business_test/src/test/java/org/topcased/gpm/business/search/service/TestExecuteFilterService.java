/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.search.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.IteratorUtils;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.authorization.impl.filter.FilterAccessContraint;
import org.topcased.gpm.business.authorization.impl.filter.FilterExecutionReport;
import org.topcased.gpm.business.exception.AuthorizationException;
import org.topcased.gpm.business.fields.SummaryData;
import org.topcased.gpm.business.search.impl.fields.UsableTypeData;
import org.topcased.gpm.business.search.impl.query.FilterQueryConfigurator;
import org.topcased.gpm.business.search.impl.query.FilterResultIterator;
import org.topcased.gpm.business.sheet.service.SheetSummaryData;

/**
 * TestExecuteFilterService
 * 
 * @author nveillet
 */
public class TestExecuteFilterService extends FiltersCreationUtils {

    private static final String INSTANCE_FILE =
            "search/TestFilterAccessNonExecutable.xml";

    private static final String INSTANCE_FILE_MULTI_LEVEL =
            "search/TestFilterAccessNonExecutableMultiLevel.xml";

    private static final String INSTANCE_FILE_ADDITIONAL_CONSTRAINTS =
            "search/TestAdditionalConstraints.xml";

    private static final String FILTER_NAME_WITH_CONFIDENTIAL_TYPE =
            GpmTestValues.INSTANCE_FILTER_NAMES[8];

    private static final String FILTER_NAME_WITH_CONFIDENTIAL_FIELD =
            GpmTestValues.INSTANCE_FILTER_NAMES[1];

    private static final String FILTER_NAME_MULTI_LEVEL =
            GpmTestValues.INSTANCE_FILTER_NAMES[0];

    private static final String FILTER_NAME_ADDITIONAL_CONSTRAINTS =
            "Filter with additional constraints";

    private SearchService searchService;

    private static final String[] VIEWER_ROLE_LOGIN =
            { GpmTestValues.USER_USER5, "pwd5" };

    private static final String VIEWER_ROLE = GpmTestValues.VIEWER_ROLE;

    private String viewerRoleToken;

    private static final String[] ADMINISTRATOR_ROLE_LOGIN =
            { GpmTestValues.USER_ADMIN_INSTANCE, "pwd" };

    private static final String ADMINISTRATOR_ROLE = "administrator";

    private String administratorRoleToken;

    /** The additional constraint name */
    private static final String ADDITIONAL_CONSTRAINT_NAME =
            "ConfidentialOldDogs";

    /** The additional constraint applicated role */
    private static final String ADDITIONAL_CONSTRAINT_ROLE = "notadmin";

    /** The additional constraint applicated type */
    private static final String ADDITIONAL_CONSTRAINT_TYPE =
            GpmTestValues.SHEET_TYPE_DOG;

    /** The non executable product list for filter with constraint */
    private static final List<String> ADDITIONAL_CONSTRAINT_NON_EXE_PRODUCTS;
    static {
        ADDITIONAL_CONSTRAINT_NON_EXE_PRODUCTS = new ArrayList<String>();
        ADDITIONAL_CONSTRAINT_NON_EXE_PRODUCTS.add(GpmTestValues.PRODUCT_NAMES[1]);
        ADDITIONAL_CONSTRAINT_NON_EXE_PRODUCTS.add(GpmTestValues.PRODUCT_NAMES[2]);
        ADDITIONAL_CONSTRAINT_NON_EXE_PRODUCTS.add(GpmTestValues.PRODUCT_NAMES[3]);
        ADDITIONAL_CONSTRAINT_NON_EXE_PRODUCTS.add(GpmTestValues.PRODUCT_NAMES[6]);
    }

    /** The sheet references for filter with constraint. */
    private static final List<String> ADDITIONAL_CONSTRAINT_RESULTS;
    static {
        ADDITIONAL_CONSTRAINT_RESULTS = new ArrayList<String>();
        ADDITIONAL_CONSTRAINT_RESULTS.add("Medor");
        ADDITIONAL_CONSTRAINT_RESULTS.add("Lassie");
    }

    /**
     * Set the viewer role token. {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.AbstractBusinessServiceTestCase#setUp()
     */
    public void setUp() {
        super.setUp();
        searchService = serviceLocator.getSearchService();

        // Login viewer user
        String lUserToken =
                authorizationService.login(VIEWER_ROLE_LOGIN[0],
                        VIEWER_ROLE_LOGIN[1]);
        viewerRoleToken =
                authorizationService.selectRole(lUserToken, VIEWER_ROLE,
                        getProductName(), getProcessName());

        // Login other admin user
        lUserToken =
                authorizationService.login(ADMINISTRATOR_ROLE_LOGIN[0],
                        ADMINISTRATOR_ROLE_LOGIN[1]);
        administratorRoleToken =
                authorizationService.selectRole(lUserToken, ADMINISTRATOR_ROLE,
                        getProductName(), getProcessName());
    }

    /**
     * Test the execution filter when the user don't have access on type
     */
    public void testWithConfidentialType() {
        // Instantiate filter access
        instantiate(getProcessName(), INSTANCE_FILE);

        // Get filter
        ExecutableFilterData lExecutableFilterData =
                searchService.getExecutableFilterByName(viewerRoleToken,
                        getProcessName(), null, null,
                        FILTER_NAME_WITH_CONFIDENTIAL_TYPE);

        assertNotNull("The filter " + FILTER_NAME_WITH_CONFIDENTIAL_TYPE
                + " not exist.", lExecutableFilterData);

        assertFalse("The filter " + FILTER_NAME_WITH_CONFIDENTIAL_TYPE
                + " is executable.", lExecutableFilterData.isExecutable());

        // execute filter
        try {
            searchService.executeFilter(viewerRoleToken, lExecutableFilterData,
                    lExecutableFilterData.getFilterVisibilityConstraintData(),
                    new FilterQueryConfigurator());
            fail("The filter " + FILTER_NAME_WITH_CONFIDENTIAL_TYPE
                    + " has been executed.");
        }
        catch (AuthorizationException e) {
            // OK
        }
    }

    /**
     * Test the execution filter when the user don't have access on field
     */
    public void testWithConfidentialField() {
        // Instanciate filter access
        instantiate(getProcessName(), INSTANCE_FILE);

        // Get filter
        ExecutableFilterData lExecutableFilterData =
                searchService.getExecutableFilterByName(viewerRoleToken,
                        getProcessName(), null, null,
                        FILTER_NAME_WITH_CONFIDENTIAL_TYPE);

        assertNotNull("The filter " + FILTER_NAME_WITH_CONFIDENTIAL_FIELD
                + " not exist.", lExecutableFilterData);

        assertFalse("The filter " + FILTER_NAME_WITH_CONFIDENTIAL_FIELD
                + " is executable.", lExecutableFilterData.isExecutable());

        // execute filter
        try {
            searchService.executeFilter(viewerRoleToken, lExecutableFilterData,
                    lExecutableFilterData.getFilterVisibilityConstraintData(),
                    new FilterQueryConfigurator());
            fail("The filter " + FILTER_NAME_WITH_CONFIDENTIAL_FIELD
                    + " has been executed.");
        }
        catch (AuthorizationException e) {
            // OK
        }
    }

    /**
     * Test the execution multi-level filter when the user don't have access on
     * link type
     */
    public void testMultiLevelWithConfidentialLinkType() {
        // Instanciate filter access
        instantiate(getProcessName(), INSTANCE_FILE_MULTI_LEVEL);

        // Get filter
        ExecutableFilterData lExecutableFilterData =
                searchService.getExecutableFilterByName(normalRoleToken,
                        getProcessName(), null, null, FILTER_NAME_MULTI_LEVEL);

        assertNotNull("The filter " + FILTER_NAME_MULTI_LEVEL + " not exist.",
                lExecutableFilterData);

        assertFalse(
                "The filter " + FILTER_NAME_MULTI_LEVEL + " is executable.",
                lExecutableFilterData.isExecutable());

        // execute filter
        try {
            searchService.executeFilter(normalRoleToken, lExecutableFilterData,
                    lExecutableFilterData.getFilterVisibilityConstraintData(),
                    new FilterQueryConfigurator());
            fail("The filter " + FILTER_NAME_MULTI_LEVEL
                    + " has been executed.");
        }
        catch (AuthorizationException e) {
            // OK
        }

    }

    /**
     * Test the execution multi-level filter when the user don't have access on
     * linked sheet type
     */
    public void testMultiLevelWithConfidentialLinkedSheetType() {
        // Instanciate filter access
        instantiate(getProcessName(), INSTANCE_FILE_MULTI_LEVEL);

        // Get filter
        ExecutableFilterData lExecutableFilterData =
                searchService.getExecutableFilterByName(viewerRoleToken,
                        getProcessName(), null, null, FILTER_NAME_MULTI_LEVEL);

        assertNotNull("The filter " + FILTER_NAME_MULTI_LEVEL + " not exist.",
                lExecutableFilterData);

        assertFalse(
                "The filter " + FILTER_NAME_MULTI_LEVEL + " is executable.",
                lExecutableFilterData.isExecutable());

        // execute filter
        try {
            searchService.executeFilter(viewerRoleToken, lExecutableFilterData,
                    lExecutableFilterData.getFilterVisibilityConstraintData(),
                    new FilterQueryConfigurator());
            fail("The filter " + FILTER_NAME_MULTI_LEVEL
                    + " has been executed.");
        }
        catch (AuthorizationException e) {
            // OK
        }

    }

    /**
     * Test the execution multi-level filter when the user don't have access on
     * linked sheet field
     */
    public void testMultiLevelWithConfidentialLinkedSheetField() {
        // Instanciate filter access
        instantiate(getProcessName(), INSTANCE_FILE_MULTI_LEVEL);

        // Get filter
        ExecutableFilterData lExecutableFilterData =
                searchService.getExecutableFilterByName(administratorRoleToken,
                        getProcessName(), null, null, FILTER_NAME_MULTI_LEVEL);

        assertNotNull("The filter " + FILTER_NAME_MULTI_LEVEL + " not exist.",
                lExecutableFilterData);

        assertFalse(
                "The filter " + FILTER_NAME_MULTI_LEVEL + " is executable.",
                lExecutableFilterData.isExecutable());

        // execute filter
        try {
            searchService.executeFilter(administratorRoleToken,
                    lExecutableFilterData,
                    lExecutableFilterData.getFilterVisibilityConstraintData(),
                    new FilterQueryConfigurator());
            fail("The filter " + FILTER_NAME_MULTI_LEVEL
                    + " has been executed.");
        }
        catch (AuthorizationException e) {
            // OK
        }
    }

    /**
     * Test the edition of the report for execution with additional constraint
     */
    public void testExecutionReportWithAdditionalConstraints() {
        // Instantiate filter access
        instantiate(getProcessName(), INSTANCE_FILE_ADDITIONAL_CONSTRAINTS);

        ExecutableFilterData lExecutableFilterData =
                searchService.getExecutableFilterByName(normalRoleToken,
                        getProcessName(), null, null,
                        FILTER_NAME_ADDITIONAL_CONSTRAINTS);

        assertNotNull("The filter " + FILTER_NAME_ADDITIONAL_CONSTRAINTS
                + " not exist.", lExecutableFilterData);

        assertTrue("The filter " + FILTER_NAME_ADDITIONAL_CONSTRAINTS
                + " is not executable.", lExecutableFilterData.isExecutable());

        FilterResultIterator<SummaryData> lFilterResultIterator =
                searchService.executeFilter(
                        normalRoleToken,

                        lExecutableFilterData,
                        lExecutableFilterData.getFilterVisibilityConstraintData(),
                        new FilterQueryConfigurator());

        FilterExecutionReport lFilterExecutionReport =
                lFilterResultIterator.getExecutionReport();

        // Check the executable product
        assertEquals("The executable product list not contains 1 role.", 1,
                lFilterExecutionReport.getExecutableProducts().size());
        assertNotNull("The executable product list not contains '"
                + ADDITIONAL_CONSTRAINT_ROLE + "' role.",
                lFilterExecutionReport.getExecutableProducts().get(
                        ADDITIONAL_CONSTRAINT_ROLE));
        assertEquals("The executable product list not contains 1 product.", 1,
                lFilterExecutionReport.getExecutableProducts().get(
                        ADDITIONAL_CONSTRAINT_ROLE).size());
        assertTrue("The executable product is not " + getProductName(),
                lFilterExecutionReport.getExecutableProducts().get(
                        ADDITIONAL_CONSTRAINT_ROLE).contains(getProductName()));

        // Check the non executable product
        assertEquals(
                "The non executable product list not contains 4 products.", 4,
                lFilterExecutionReport.getNonExecutableProducts().size());
        assertTrue(
                "The non executable product list not contains good products.",
                lFilterExecutionReport.getNonExecutableProducts().containsAll(
                        ADDITIONAL_CONSTRAINT_NON_EXE_PRODUCTS));

        // Check the applicated additional constraints
        assertEquals("The additional constraints list not contains 1 type.", 1,
                lFilterExecutionReport.getAdditionalConstraints().size());

        UsableTypeData lUsableTypeData =
                lFilterExecutionReport.getAdditionalConstraints().keySet().iterator().next();
        assertEquals("The additional constraints list not contains '"
                + ADDITIONAL_CONSTRAINT_TYPE + "' type.",
                ADDITIONAL_CONSTRAINT_TYPE, lUsableTypeData.getFullName());

        assertEquals("The additional constraints list not contains 1 role.", 1,
                lFilterExecutionReport.getAdditionalConstraints().get(
                        lUsableTypeData).size());
        assertNotNull("The additional constraints list not contains '"
                + ADDITIONAL_CONSTRAINT_ROLE + "' role.",
                lFilterExecutionReport.getAdditionalConstraints().get(
                        lUsableTypeData).get(ADDITIONAL_CONSTRAINT_ROLE));
        assertEquals(
                "The additional constraints list not contains 1 constraint.",
                1, lFilterExecutionReport.getAdditionalConstraints().get(
                        lUsableTypeData).get(ADDITIONAL_CONSTRAINT_ROLE).size());

        FilterAccessContraint lFilterAccessContraint =
                lFilterExecutionReport.getAdditionalConstraints().get(
                        lUsableTypeData).get(ADDITIONAL_CONSTRAINT_ROLE).iterator().next();
        assertEquals("The additional constraints list not contains '"
                + ADDITIONAL_CONSTRAINT_NAME + "' constraint.",
                ADDITIONAL_CONSTRAINT_NAME, lFilterAccessContraint.getName());
    }

    /**
     * Test the results for execution with additional constraint
     */
    @SuppressWarnings("unchecked")
    public void testExecutionResultWithAdditionalConstraints() {
        // Instanciate filter access
        instantiate(getProcessName(), INSTANCE_FILE_ADDITIONAL_CONSTRAINTS);

        ExecutableFilterData lExecutableFilterData =
                searchService.getExecutableFilterByName(normalRoleToken,
                        getProcessName(), null, null,
                        FILTER_NAME_ADDITIONAL_CONSTRAINTS);

        assertNotNull("The filter " + FILTER_NAME_ADDITIONAL_CONSTRAINTS
                + " not exist.", lExecutableFilterData);

        assertTrue("The filter " + FILTER_NAME_ADDITIONAL_CONSTRAINTS
                + " is not executable.", lExecutableFilterData.isExecutable());

        FilterResultIterator<SheetSummaryData> lFilterResultIterator =
                searchService.executeFilter(
                        normalRoleToken,
                        lExecutableFilterData,
                        lExecutableFilterData.getFilterVisibilityConstraintData(),
                        new FilterQueryConfigurator());

        List<SheetSummaryData> lResultList =
                IteratorUtils.toList(lFilterResultIterator);

        assertEquals("The result list not contains 2 sheets.", 2,
                lResultList.size());

        for (SheetSummaryData lSheetSummaryData : lResultList) {
            assertTrue(
                    "The sheet '" + lSheetSummaryData.getSheetReference()
                            + "' should not be in the filter results.",
                    ADDITIONAL_CONSTRAINT_RESULTS.contains(lSheetSummaryData.getSheetReference()));

        }
    }
}