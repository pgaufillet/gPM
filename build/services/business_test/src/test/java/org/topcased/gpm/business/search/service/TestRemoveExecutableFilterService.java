/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Anne Haugommard(Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.search.service;

import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.exception.AuthorizationException;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.exception.InvalidIdentifierException;
import org.topcased.gpm.business.fields.FieldType;
import org.topcased.gpm.business.fields.FieldTypes;
import org.topcased.gpm.business.scalar.StringValueData;
import org.topcased.gpm.business.search.criterias.CriteriaData;
import org.topcased.gpm.business.search.criterias.CriteriaFieldData;
import org.topcased.gpm.business.search.criterias.FilterData;
import org.topcased.gpm.business.search.criterias.FilterTypeData;
import org.topcased.gpm.business.search.criterias.OperationData;
import org.topcased.gpm.business.search.criterias.impl.Operators;
import org.topcased.gpm.business.search.result.sorter.ResultSortingData;
import org.topcased.gpm.business.search.result.sorter.SortingFieldData;
import org.topcased.gpm.business.search.result.summary.ResultSummaryData;
import org.topcased.gpm.business.sheet.impl.CacheableSheetType;
import org.topcased.gpm.domain.search.FilterUsage;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * Tests the method <CODE>removeExecutableFilter<CODE> of the Search Service.
 * 
 * @author ahaugomm
 */
public class TestRemoveExecutableFilterService extends FiltersCreationUtils {

    /** The Search Service. */
    private SearchService searchService;

    /** the filter name */
    private static final String FILTER_NAME = "my_filter";

    /** the filter description */
    private static final String FILTER_DESCR = "my_filter_description";

    /** the criteria suffix */
    private static final String CRITERIA_SUFFIX = "_CRIT";

    /** the sorting suffix */
    private static final String SORTING_SUFFIX = "_SORTER";

    /** the summary suffix */
    private static final String SUMMARY_SUFFIX = "_SUMMARY";

    /** the sheet type to filter */
    private static final String SHEET_TYPE = GpmTestValues.SHEET_TYPE_CAT;

    /** the user login for filter constraint : all users */
    private static final String ALL_USERS = null;

    /** the product name for filter constraint : all products */
    private static final String ALL_PRODUCTS = null;

    /** the properties of the first criterion field */
    private static final String[] FIRST_CRITERIA_FIELD =
            { "CAT_description", "Pet description",
             FieldTypes.SIMPLE_STRING_FIELD };

    /** the properties of the second criterion field */
    private static final String[] SECOND_CRITERIA_FIELD =
            { "CAT_color", GpmTestValues.CATEGORY_COLOR,
             FieldTypes.CHOICE_FIELD };

    /** the value for the first criterion */
    private static final String VALUE_FOR_FIRST_CRITERIA = "%BLACK%";

    /** the value for the first criterion */
    private static final String VALUE_FOR_SECOND_CRITERIA = "%cat%";

    /** result sorting data */
    private static final String[] FIRST_RESULT_SORTING_DATA =
            { "CAT_ref", "Ref", FieldTypes.SIMPLE_STRING_FIELD, Operators.ASC };

    /** the properties of the first summary field */
    private static final String[] FIRST_SUMMARY_FIELD = { "CAT_ref", "Ref" };

    /** the properties of the second summary field */
    private static final String[] SECOND_SUMMARY_FIELD =
            { "CAT_color", GpmTestValues.CATEGORY_COLOR };

    /** the properties of the third summary field */
    private static final String[] THIRD_SUMMARY_FIELD =
            { "CAT_furlength", "Fur Length" };

    /** the properties of the last summary field */
    private static final String[] LAST_SUMMARY_FIELD =
            { "CAT_description", "Pet description" };

    private static final String INSTANCE_FILE =
            "search/TestFilterAccessNonEditable.xml";

    private static final String FILTER_NAME_WITH_CONFIDENTIAL_TYPE =
            GpmTestValues.INSTANCE_FILTER_NAMES[8];

    private static final String FILTER_NAME_WITH_CONFIDENTIAL_FIELD =
            GpmTestValues.INSTANCE_FILTER_NAMES[1];

    private static final String FILTER_NAME_PROCESS =
            GpmTestValues.INSTANCE_FILTER_NAMES[3];

    private static final String FILTER_NAME_PRODUCT =
            GpmTestValues.INSTANCE_FILTER_NAMES[4];

    private static final String[] VIEWER_ROLE_LOGIN =
            { GpmTestValues.USER_USER5, "pwd5" };

    private static final String VIEWER_ROLE = GpmTestValues.VIEWER_ROLE;

    private String viewerRoleToken;

    /**
     * Set the viewer role token. {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.AbstractBusinessServiceTestCase#setUp()
     */
    public void setUp() {
        super.setUp();
        searchService = serviceLocator.getSearchService();

        String lUserToken =
                authorizationService.login(VIEWER_ROLE_LOGIN[0],
                        VIEWER_ROLE_LOGIN[1]);
        viewerRoleToken =
                authorizationService.selectRole(lUserToken, VIEWER_ROLE,
                        getProductName(), getProcessName());
    }

    /**
     * Tests the method in a normal way.
     */
    public void testNormalCase() {

        ExecutableFilterData lExecutableFilterData =
                createExecutableFilterData();

        // Create the executable filter in the database
        String lId =
                searchService.createExecutableFilter(adminRoleToken,
                        lExecutableFilterData);

        assertNotNull("The executableFilter has not been created", lId);

        // main test
        this.searchService.removeExecutableFilter(adminRoleToken, lId);

        try {
            searchService.getExecutableFilter(adminRoleToken, lId);
            fail("an Exception should be thrown");
        }
        catch (GDMException e) {
        }
    }

    /**
     * Create executable filter data
     * 
     * @return created executable filter data
     */
    protected ExecutableFilterData createExecutableFilterData() {

        // Get the sheet type id
        CacheableSheetType lTypeData =
                serviceLocator.getSheetService().getCacheableSheetTypeByName(
                        adminRoleToken, getProcessName(), SHEET_TYPE,
                        CacheProperties.IMMUTABLE);
        String lSheetTypeId = lTypeData.getId();
        assertNotNull("Sheet type " + SHEET_TYPE + " not found.", lSheetTypeId);

        // Create the Executable Filter Data to save
        String[] lFieldsContainerIds = { lSheetTypeId };

        FilterVisibilityConstraintData lFilterVisibilityConstraintData =
                new FilterVisibilityConstraintData(ALL_USERS, getProcessName(),
                        ALL_PRODUCTS);

        // Create the usable field data
        UsableFieldData lUstring = new UsableFieldData();
        lUstring.setFieldName(FIRST_CRITERIA_FIELD[0]);
        lUstring.setFieldType(FieldType.valueOf(FIRST_CRITERIA_FIELD[2]));

        UsableFieldData lUchoice = new UsableFieldData();
        lUchoice.setFieldName(SECOND_CRITERIA_FIELD[0]);
        lUchoice.setFieldType(FieldType.valueOf(SECOND_CRITERIA_FIELD[2]));

        // Create the 2 criteria
        CriteriaData[] lCrits = new CriteriaData[2];
        lCrits[0] =
                new CriteriaFieldData(Operators.LIKE, Boolean.TRUE,
                        new StringValueData(VALUE_FOR_FIRST_CRITERIA), lUchoice);
        lCrits[1] =
                new CriteriaFieldData(Operators.LIKE, Boolean.TRUE,
                        new StringValueData(VALUE_FOR_SECOND_CRITERIA),
                        lUstring);

        // Create the Operation data (AND between the two criteria)
        OperationData lCriteriaData = new OperationData(Operators.AND, lCrits);

        // Create the filter data from the criteria data, the fieldsContainer,
        // the filter name and the visibility constraints
        FilterData lFilterData =
                new FilterData(null, FILTER_NAME + CRITERIA_SUFFIX, false,
                        lFieldsContainerIds, FilterTypeData.SHEET,
                        lCriteriaData, lFilterVisibilityConstraintData);

        // Create the sorting field data
        final int lLength = 3;
        SortingFieldData[] lSortingFieldDatas = new SortingFieldData[lLength];

        lSortingFieldDatas[0] = new SortingFieldData();
        lSortingFieldDatas[0].setUsableFieldData(new UsableFieldData());
        lSortingFieldDatas[0].getUsableFieldData().setFieldName(
                FIRST_RESULT_SORTING_DATA[0]);
        lSortingFieldDatas[0].getUsableFieldData().setFieldType(
                FieldType.valueOf(FIRST_RESULT_SORTING_DATA[2]));
        lSortingFieldDatas[0].setOrder(FIRST_RESULT_SORTING_DATA[3]);

        lSortingFieldDatas[1] = new SortingFieldData();
        lSortingFieldDatas[1].setUsableFieldData(lUchoice);
        lSortingFieldDatas[1].setOrder(Operators.DESC);

        lSortingFieldDatas[2] = new SortingFieldData();
        lSortingFieldDatas[2].setUsableFieldData(lUstring);
        lSortingFieldDatas[2].setOrder(Operators.ASC);

        // creation of the result sorting data from the sorting field data
        ResultSortingData lResultSortingData =
                new ResultSortingData(null, FILTER_NAME + SORTING_SUFFIX,
                        lFieldsContainerIds, lSortingFieldDatas,
                        lFilterVisibilityConstraintData);

        // creation of the filter result field data
        final int lSize = 4;
        UsableFieldData[] lUsableFieldDatas = new UsableFieldData[lSize];
        lUsableFieldDatas[0] = new UsableFieldData();
        lUsableFieldDatas[0].setFieldName(FIRST_SUMMARY_FIELD[0]);
        lUsableFieldDatas[1] = new UsableFieldData();
        lUsableFieldDatas[1].setFieldName(SECOND_SUMMARY_FIELD[0]);
        lUsableFieldDatas[2] = new UsableFieldData();
        lUsableFieldDatas[2].setFieldName(THIRD_SUMMARY_FIELD[0]);
        lUsableFieldDatas[lSize - 1] = new UsableFieldData();
        lUsableFieldDatas[lSize - 1].setFieldName(LAST_SUMMARY_FIELD[0]);
        // creation of the result summary data from the filter result field data
        ResultSummaryData lResultSummaryData =
                new ResultSummaryData(null, FILTER_NAME + SUMMARY_SUFFIX,
                        lFieldsContainerIds, lUsableFieldDatas,
                        lFilterVisibilityConstraintData);

        // creation of the executable filter data from the result summary data,
        // the result sorting data, and the filter data
        ExecutableFilterData lExecutableFilterData =
                new ExecutableFilterData(null, FILTER_NAME, FILTER_DESCR,
                        FilterUsage.BOTH_VIEWS.getValue(), false,
                        lResultSummaryData, lResultSortingData, lFilterData,
                        lFilterVisibilityConstraintData, null);

        return lExecutableFilterData;
    }

    /**
     * Tests the method with an invalid id
     */
    public void testInvalidIdCase() {
        try {
            searchService.removeExecutableFilter(adminRoleToken, "");
            fail("The exception has not been thrown.");
        }
        catch (InvalidIdentifierException lException) {
            // ok
        }
    }

    /**
     * Tests the method with a role who can't delete an instance filter
     */
    public void testNotDeletableCase() {
        authorizationService = serviceLocator.getAuthorizationService();

        // Create executable filter data structure
        ExecutableFilterData lExecutableFilterData =
                createExecutableFilterData();

        // Create the executable filter in the database
        String lId =
                searchService.createExecutableFilter(adminRoleToken,
                        lExecutableFilterData);

        assertNotNull("The executableFilter has not been created", lId);

        // main test
        try {
            searchService.removeExecutableFilter(normalRoleToken, lId);
            fail("An exception should have been thrown.");
        }
        catch (AuthorizationException ex) {
            // ok
        }
    }

    /**
     * Test the remove filter when the user don't have access on visibility
     * 'Process'
     */
    public void testWithConfidentialVisibilityProcess() {
        // Instanciate filter access
        instantiate(getProcessName(), INSTANCE_FILE);

        // Get filter
        ExecutableFilterData lExecutableFilterData =
                searchService.getExecutableFilterByName(normalRoleToken,
                        getProcessName(), null, null, FILTER_NAME_PROCESS);

        assertNotNull("The filter " + FILTER_NAME_PROCESS + " not exist.",
                lExecutableFilterData);

        // delete filter
        try {
            searchService.removeExecutableFilter(normalRoleToken,
                    lExecutableFilterData.getId());
        }
        catch (AuthorizationException e) {
            fail("The filter " + FILTER_NAME_PROCESS + " has not been deleted.");
        }
    }

    /**
     * Test the remove filter when the user don't have access on visibility
     * 'Product'
     */
    public void testWithConfidentialVisibilityProduct() {
        // Instanciate filter access
        instantiate(getProcessName(), INSTANCE_FILE);

        // Get filter
        ExecutableFilterData lExecutableFilterData =
                searchService.getExecutableFilterByName(normalRoleToken,
                        getProcessName(), getProductName(), null,
                        FILTER_NAME_PRODUCT);

        assertNotNull("The filter " + FILTER_NAME_PRODUCT + " not exist.",
                lExecutableFilterData);

        // delete filter
        try {
            searchService.removeExecutableFilter(normalRoleToken,
                    lExecutableFilterData.getId());
        }
        catch (AuthorizationException e) {
            fail("The filter " + FILTER_NAME_PRODUCT + " has not been deleted.");
        }
    }

    /**
     * Test the remove filter when the user don't have access on visibility
     * 'User'
     */
    public void testWithConfidentialVisibilityUser() {
        // Create user filter
        ExecutableFilterData lExecutableFilterData =
                createGeneralFilterData(new FilterVisibilityConstraintData(
                        USER2_LOGIN[0], getProcessName(), null), FILTER_NAME);

        searchService.createExecutableFilter(normalRoleToken,
                lExecutableFilterData);

        // Instanciate filter access
        instantiate(getProcessName(), INSTANCE_FILE);

        // Get filter
        lExecutableFilterData =
                searchService.getExecutableFilterByName(normalRoleToken,
                        getProcessName(), null, USER2_LOGIN[0], FILTER_NAME);

        assertNotNull("The filter " + FILTER_NAME + " not exist.",
                lExecutableFilterData);

        // delete filter
        try {
            searchService.removeExecutableFilter(normalRoleToken,
                    lExecutableFilterData.getId());
            fail("The filter " + FILTER_NAME + " has been deleted.");
        }
        catch (AuthorizationException e) {
            // OK
        }
    }

    /**
     * Test the remove filter when the user don't have access on type
     */
    public void testWithConfidentialType() {
        // Instanciate filter access
        instantiate(getProcessName(), INSTANCE_FILE);

        // Get filter
        ExecutableFilterData lExecutableFilterData =
                searchService.getExecutableFilterByName(viewerRoleToken,
                        getProcessName(), null, null,
                        FILTER_NAME_WITH_CONFIDENTIAL_TYPE);

        assertNotNull("The filter " + FILTER_NAME_WITH_CONFIDENTIAL_TYPE
                + " not exist.", lExecutableFilterData);

        assertFalse("The filter " + FILTER_NAME_WITH_CONFIDENTIAL_TYPE
                + " is deletable.", lExecutableFilterData.isEditable());

        // delete filter
        try {
            searchService.removeExecutableFilter(viewerRoleToken,
                    lExecutableFilterData.getId());
            fail("The filter " + FILTER_NAME_WITH_CONFIDENTIAL_TYPE
                    + " has been deleted.");
        }
        catch (AuthorizationException e) {
            // OK
        }
    }

    /**
     * Test the remove filter when the user don't have access on field
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
                + " is deletable.", lExecutableFilterData.isEditable());

        // delete filter
        try {
            searchService.removeExecutableFilter(viewerRoleToken,
                    lExecutableFilterData.getId());
            fail("The filter " + FILTER_NAME_WITH_CONFIDENTIAL_FIELD
                    + " has been deleted.");
        }
        catch (AuthorizationException e) {
            // OK
        }
    }
}
