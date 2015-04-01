/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Anne Haugommard(Atos Origin), Michael Kargbo
 * (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.search.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.exception.AuthorizationException;
import org.topcased.gpm.business.fields.FieldType;
import org.topcased.gpm.business.fields.FieldTypes;
import org.topcased.gpm.business.fields.FieldsContainerType;
import org.topcased.gpm.business.link.impl.LinkDirection;
import org.topcased.gpm.business.scalar.ScalarValueData;
import org.topcased.gpm.business.scalar.StringValueData;
import org.topcased.gpm.business.search.criterias.CriteriaData;
import org.topcased.gpm.business.search.criterias.CriteriaFieldData;
import org.topcased.gpm.business.search.criterias.FilterData;
import org.topcased.gpm.business.search.criterias.FilterTypeData;
import org.topcased.gpm.business.search.criterias.OperationData;
import org.topcased.gpm.business.search.criterias.impl.FilterFieldsContainerInfo;
import org.topcased.gpm.business.search.criterias.impl.Operators;
import org.topcased.gpm.business.search.impl.SearchServiceTestUtils;
import org.topcased.gpm.business.search.result.sorter.ResultSortingData;
import org.topcased.gpm.business.search.result.sorter.SortingFieldData;
import org.topcased.gpm.business.search.result.summary.ResultSummaryData;
import org.topcased.gpm.business.sheet.impl.CacheableSheetType;
import org.topcased.gpm.domain.search.FilterUsage;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * Tests the method <CODE>updateExecutableFilter<CODE> of the Search Service.
 * 
 * @author ahaugomm
 */
public class TestUpdateExecutableFilterService extends FiltersCreationUtils {

    /** The Search Service. */
    private SearchService searchService;

    /** the filter name */
    private static final String FILTER_NAME = "my_filter";

    /** the filter name */
    private static final String FILTER_DESCRIPTION = "my_filter_description";

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
            { "CAT_description", "Pet Description",
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

    /** the properties for the fourth virtual field criterion */
    private static final String FIELDS_CONTAINER_NAME_CAT_PRICE =
            GpmTestValues.SHEETLINK_CAT_PRICE;

    private static final String[] FOURTH_VIRTUAL_CRITERIA_FIELD =
            { "CAT_PRICE_author", "Link-Cat-Price.Justification.Author",
             FieldTypes.SIMPLE_STRING_FIELD, Operators.LIKE };

    private static final ScalarValueData[] VALUES_FOR_VIRTUAL_FIELDS =
            { new StringValueData("%") };

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
     * test the method in a normal way
     */
    public void testNormalCase() {
        // Get the sheet type id
        CacheableSheetType lType =
                sheetService.getCacheableSheetTypeByName(adminRoleToken,
                        getProcessName(), SHEET_TYPE, CacheProperties.IMMUTABLE);
        String lSheetTypeId = lType.getId();
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
        final int lLength = 2;
        SortingFieldData[] lSortingFieldDatas = new SortingFieldData[lLength];

        lSortingFieldDatas[0] = new SortingFieldData();
        lSortingFieldDatas[0].setUsableFieldData(new UsableFieldData());
        lSortingFieldDatas[0].getUsableFieldData().setFieldName(
                FIRST_RESULT_SORTING_DATA[0]);
        lSortingFieldDatas[0].getUsableFieldData().setFieldType(
                FieldType.valueOf(FIRST_RESULT_SORTING_DATA[2]));
        lSortingFieldDatas[0].setOrder(FIRST_RESULT_SORTING_DATA[3]);

        lSortingFieldDatas[1] = new SortingFieldData();
        lSortingFieldDatas[1].setUsableFieldData(lUstring);
        lSortingFieldDatas[1].setOrder(Operators.ASC);

        // creation of the result sorting data from the sorting field data
        ResultSortingData lResultSortingData =
                new ResultSortingData(null, FILTER_NAME + SORTING_SUFFIX,
                        lFieldsContainerIds, lSortingFieldDatas,
                        lFilterVisibilityConstraintData);

        // creation of the filter result field data
        final int lSize = 3;
        Map<String, UsableFieldData> lAvailableFields =
                searchService.getUsableFields(adminRoleToken,
                        lFieldsContainerIds, getProcessName());

        UsableFieldData[] lUsableFieldDatas = new UsableFieldData[lSize];
        lUsableFieldDatas[0] = lAvailableFields.get("CAT_ref");
        lUsableFieldDatas[1] = lAvailableFields.get("CAT_furlength");
        lUsableFieldDatas[2] = lAvailableFields.get("CAT_description");

        // creation of the result summary data from the filter result field data
        ResultSummaryData lResultSummaryData =
                new ResultSummaryData(null, FILTER_NAME + SUMMARY_SUFFIX,
                        lFieldsContainerIds, lUsableFieldDatas,
                        lFilterVisibilityConstraintData);

        // creation of the executable filter data from the result summary data,
        // the result sorting data, and the filter data
        ExecutableFilterData lExecutableFilterData =
                new ExecutableFilterData(null, FILTER_NAME, FILTER_DESCRIPTION,
                        FilterUsage.BOTH_VIEWS.getValue(), false,
                        lResultSummaryData, lResultSortingData, lFilterData,
                        lFilterVisibilityConstraintData, null);

        // Create the executable filter in the database
        String lId =
                searchService.createExecutableFilter(adminRoleToken,
                        lExecutableFilterData);

        assertNotNull("createExecutableFilter returns a null Id", lId);

        lExecutableFilterData =
                searchService.getExecutableFilter(adminRoleToken, lId);

        assertNotNull("Executable filter not found ", lExecutableFilterData);

        //Set new criteria
        CriteriaData lNewCriteriaData =
                createNewCriteria(lExecutableFilterData.getFilterData().getCriteriaData());
        lExecutableFilterData.getFilterData().setCriteriaData(lNewCriteriaData);

        lUsableFieldDatas[0] = lAvailableFields.get("CAT_ref");
        lUsableFieldDatas[1] = lAvailableFields.get("CAT_color");
        lUsableFieldDatas[2] = lAvailableFields.get("CAT_furlength");
        lUsableFieldDatas[lSize - 1] = lAvailableFields.get("CAT_description");

        SortingFieldData[] lSortingFieldDatas2 = new SortingFieldData[1];

        lSortingFieldDatas2[0] = new SortingFieldData();
        lSortingFieldDatas2[0].setUsableFieldData(lAvailableFields.get("CAT_ref"));
        lSortingFieldDatas2[0].setOrder(Operators.ASC);

        lExecutableFilterData.getResultSortingData().setSortingFieldDatas(
                lSortingFieldDatas2);
        lExecutableFilterData.setFilterVisibilityConstraintData(new FilterVisibilityConstraintData(
                GpmTestValues.USER_ADMIN, GpmTestValues.PROCESS_NAME,
                GpmTestValues.PRODUCT_HAPPY_MOUSE_NAME));

        UsableFieldData[] lUsableFieldDatas2 = new UsableFieldData[2];

        lUsableFieldDatas2[0] = lAvailableFields.get("CAT_ref");

        lUsableFieldDatas2[1] = lAvailableFields.get("CAT_description");

        lExecutableFilterData.getResultSummaryData().setUsableFieldDatas(
                lUsableFieldDatas2);

        lExecutableFilterData.setDescription("new description");

        // main test
        startTimer();
        searchService.updateExecutableFilter(adminRoleToken,
                lExecutableFilterData);
        stopTimer();

        ExecutableFilterData lNewExecutableFilterData =
                searchService.getExecutableFilter(adminRoleToken,
                        lExecutableFilterData.getId());

        assertTrue(SearchServiceTestUtils.areEqual(lExecutableFilterData,
                lNewExecutableFilterData));
    }

    private CriteriaData createNewCriteria(CriteriaData pOldCriteria) {
        if (pOldCriteria instanceof OperationData) {
            OperationData lOldCriteria = (OperationData) pOldCriteria;
            CriteriaData[] lCriteria =
                    new CriteriaData[lOldCriteria.getCriteriaDatas().length + 1];

            int i = 0;
            for (CriteriaData lCriteriaData : lOldCriteria.getCriteriaDatas()) {
                lCriteria[i] = lCriteriaData;
                i++;
            }

            //Fields container identifiers
            String lFieldsContainerCatPriceId =
                    getFieldsContainerService().getFieldsContainerId(
                            adminRoleToken, FIELDS_CONTAINER_NAME_CAT_PRICE);

            FilterFieldsContainerInfo lCatPriceFilterFieldsContainerInfo =
                    new FilterFieldsContainerInfo();
            lCatPriceFilterFieldsContainerInfo.setId(lFieldsContainerCatPriceId);
            lCatPriceFilterFieldsContainerInfo.setLabelKey(FIELDS_CONTAINER_NAME_CAT_PRICE);
            lCatPriceFilterFieldsContainerInfo.setType(FieldsContainerType.LINK);
            lCatPriceFilterFieldsContainerInfo.setLinkDirection(LinkDirection.DESTINATION);

            //The link
            UsableFieldData lVirtual = new UsableFieldData();
            lVirtual.setFieldName(FOURTH_VIRTUAL_CRITERIA_FIELD[0]);
            lVirtual.setFieldType(FieldType.valueOf(FOURTH_VIRTUAL_CRITERIA_FIELD[2]));
            lVirtual.setLevel(1);
            List<FilterFieldsContainerInfo> lFieldsContainerHierarchy =
                    new ArrayList<FilterFieldsContainerInfo>(1);
            lFieldsContainerHierarchy.add(lCatPriceFilterFieldsContainerInfo);
            lVirtual.setFieldsContainerHierarchy(lFieldsContainerHierarchy);

            lCriteria[i] =
                    new CriteriaFieldData(FOURTH_VIRTUAL_CRITERIA_FIELD[3],
                            Boolean.TRUE, VALUES_FOR_VIRTUAL_FIELDS[0],
                            lVirtual);
            // Create the Operation data (AND between the two criteria)
            OperationData lCriteriaData =
                    new OperationData(Operators.AND, lCriteria);
            return lCriteriaData;
        }
        else {
            return pOldCriteria;
        }
    }

    /**
     * Test to update an instance filter with global admin role : update is
     * authorized
     */
    public void testUpdateInstanceFilterWithAdminRoleCase() {
        searchService = serviceLocator.getSearchService();
        authorizationService = serviceLocator.getAuthorizationService();

        // create Instance filter
        ExecutableFilterData lInstanceFilterData =
                createInstanceExecutableFilterData();

        String lFilterId =
                searchService.createExecutableFilter(adminRoleToken,
                        lInstanceFilterData);

        lInstanceFilterData =
                searchService.getExecutableFilter(adminRoleToken, lFilterId);

        try {
            searchService.updateExecutableFilter(adminRoleToken,
                    lInstanceFilterData);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            fail("An exception should not have been thrown.");
        }
    }

    /**
     * Test to update an instance filter with admin access on global instance :
     * update is authorized
     */
    public void testUpdateInstanceFilterWithAdminInstanceAccessCase() {
        searchService = serviceLocator.getSearchService();
        authorizationService = serviceLocator.getAuthorizationService();

        // Login with Admin Instance
        String lAdminInstanceUserToken =
                authorizationService.login(
                        GpmTestValues.ADMIN_INSTANCE_LOGIN_PWD[0],
                        GpmTestValues.ADMIN_INSTANCE_LOGIN_PWD[1]);
        String lAdminInstanceRoleToken =
                authorizationService.selectRole(lAdminInstanceUserToken,
                        GpmTestValues.ADMINISTRATOR_ROLE, getProductName(),
                        getProcessName());

        // create Instance filter
        ExecutableFilterData lInstanceFilterData =
                createInstanceExecutableFilterData();

        String lFilterId =
                searchService.createExecutableFilter(adminRoleToken,
                        lInstanceFilterData);

        lInstanceFilterData =
                searchService.getExecutableFilter(adminRoleToken, lFilterId);

        try {
            searchService.updateExecutableFilter(lAdminInstanceRoleToken,
                    lInstanceFilterData);
        }
        catch (Exception ex) {
            fail("An exception should not have been thrown.");
        }
    }

    /**
     * Test to update an instance filter with admin access on product : update
     * is not authorized
     */
    public void testUpdateInstanceFilterWithProductAccessCase() {
        searchService = serviceLocator.getSearchService();
        authorizationService = serviceLocator.getAuthorizationService();

        // Login with Product Instance
        String lProductInstanceUserToken =
                authorizationService.login(
                        GpmTestValues.ADMIN_PRODUCT_LOGIN_PWD[0],
                        GpmTestValues.ADMIN_PRODUCT_LOGIN_PWD[1]);
        String lProductInstanceRoleToken =
                authorizationService.selectRole(lProductInstanceUserToken,
                        GpmTestValues.ADMINISTRATOR_ROLE,
                        GpmTestValues.PRODUCT1_NAME, getProcessName());

        // create Instance filter
        ExecutableFilterData lInstanceFilterData =
                createInstanceExecutableFilterData();

        String lFilterId =
                searchService.createExecutableFilter(adminRoleToken,
                        lInstanceFilterData);

        lInstanceFilterData =
                searchService.getExecutableFilter(adminRoleToken, lFilterId);

        try {
            searchService.updateExecutableFilter(lProductInstanceRoleToken,
                    lInstanceFilterData);
            fail("The exception has not been thrown.");
        }
        catch (AuthorizationException lAuthorizationException) {
            // ok
        }
    }

    /**
     * Test to update an instance filter with no admin access : update is not
     * authorized
     */
    public void testUpdateInstanceFilterWithNoAdminAccessCase() {
        searchService = serviceLocator.getSearchService();
        authorizationService = serviceLocator.getAuthorizationService();

        // Login with classical role
        String lUserToken =
                authorizationService.login(GpmTestValues.NO_ADMIN_LOGIN_PWD[0],
                        GpmTestValues.NO_ADMIN_LOGIN_PWD[1]);
        String lRoleToken =
                authorizationService.selectRole(lUserToken,
                        GpmTestValues.VIEWER_ROLE, getProductName(),
                        getProcessName());

        // create Instance filter
        ExecutableFilterData lInstanceFilterData =
                createInstanceExecutableFilterData();

        String lFilterId =
                searchService.createExecutableFilter(adminRoleToken,
                        lInstanceFilterData);

        lInstanceFilterData =
                searchService.getExecutableFilter(adminRoleToken, lFilterId);

        try {
            searchService.updateExecutableFilter(lRoleToken,
                    lInstanceFilterData);
            fail("The exception has not been thrown.");
        }
        catch (AuthorizationException lAuthorizationException) {
            // ok
        }
    }

    /**
     * Test to update a product filter with global admin role : update is
     * authorized
     */
    public void testUpdateProductFilterWithAdminRoleCase() {
        searchService = serviceLocator.getSearchService();
        authorizationService = serviceLocator.getAuthorizationService();

        // create Product filter
        ExecutableFilterData lProductFilterData =
                createProductExecutableFilterData();

        String lFilterId =
                searchService.createExecutableFilter(adminRoleToken,
                        lProductFilterData);

        lProductFilterData =
                searchService.getExecutableFilter(adminRoleToken, lFilterId);

        try {
            searchService.updateExecutableFilter(adminRoleToken,
                    lProductFilterData);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            fail("An exception should not have been thrown.");
        }
    }

    /**
     * Test to update a product filter with admin access on global instance :
     * update is authorized
     */
    public void testUpdateProductFilterWithAdminInstanceAccessCase() {
        searchService = serviceLocator.getSearchService();
        authorizationService = serviceLocator.getAuthorizationService();

        // Login with Admin Instance
        String lAdminInstanceUserToken =
                authorizationService.login(
                        GpmTestValues.ADMIN_INSTANCE_LOGIN_PWD[0],
                        GpmTestValues.ADMIN_INSTANCE_LOGIN_PWD[1]);
        String lAdminInstanceRoleToken =
                authorizationService.selectRole(lAdminInstanceUserToken,
                        GpmTestValues.ADMINISTRATOR_ROLE, getProductName(),
                        getProcessName());

        // create Instance filter
        ExecutableFilterData lProductFilterData =
                createProductExecutableFilterData();

        String lFilterId =
                searchService.createExecutableFilter(adminRoleToken,
                        lProductFilterData);

        lProductFilterData =
                searchService.getExecutableFilter(adminRoleToken, lFilterId);

        try {
            searchService.updateExecutableFilter(lAdminInstanceRoleToken,
                    lProductFilterData);
        }
        catch (Exception ex) {
            fail("An exception should not have been thrown.");
        }
    }

    /**
     * Test to update a product filter with admin access on product : update is
     * authorized
     */
    public void testUpdateProductFilterWithProductAccessCase() {
        searchService = serviceLocator.getSearchService();
        authorizationService = serviceLocator.getAuthorizationService();

        // Login with Product Instance
        String lProductInstanceUserToken =
                authorizationService.login(
                        GpmTestValues.ADMIN_PRODUCT_LOGIN_PWD[0],
                        GpmTestValues.ADMIN_PRODUCT_LOGIN_PWD[1]);
        String lProductInstanceRoleToken =
                authorizationService.selectRole(lProductInstanceUserToken,
                        GpmTestValues.ADMINISTRATOR_ROLE,
                        GpmTestValues.PRODUCT1_NAME, getProcessName());

        // create Product filter
        ExecutableFilterData lProductFilterData =
                createProductExecutableFilterData();

        String lFilterId =
                searchService.createExecutableFilter(adminRoleToken,
                        lProductFilterData);

        lProductFilterData =
                searchService.getExecutableFilter(adminRoleToken, lFilterId);

        try {
            searchService.updateExecutableFilter(lProductInstanceRoleToken,
                    lProductFilterData);
        }
        catch (Exception ex) {
            fail("An exception should not have been thrown.");
        }
    }

    /**
     * Test to update a product filter with no admin access : update is not
     * authorized
     */
    public void testUpdateProductFilterWithNoAdminAccessCase() {
        searchService = serviceLocator.getSearchService();
        authorizationService = serviceLocator.getAuthorizationService();

        // Login with classical role
        String lUserToken =
                authorizationService.login(GpmTestValues.NO_ADMIN_LOGIN_PWD[0],
                        GpmTestValues.NO_ADMIN_LOGIN_PWD[1]);
        String lRoleToken =
                authorizationService.selectRole(lUserToken,
                        GpmTestValues.VIEWER_ROLE, getProductName(),
                        getProcessName());

        // create Product filter
        ExecutableFilterData lProductFilterData =
                createProductExecutableFilterData();

        String lFilterId =
                searchService.createExecutableFilter(adminRoleToken,
                        lProductFilterData);

        lProductFilterData =
                searchService.getExecutableFilter(adminRoleToken, lFilterId);

        try {
            searchService.updateExecutableFilter(lRoleToken, lProductFilterData);
            fail("The exception has not been thrown.");
        }
        catch (AuthorizationException lAuthorizationException) {
            // ok
        }
        catch (Throwable e) {
            fail("The exception thrown is not an AuthorizationException.");
        }
    }

    /**
     * Test the update filter when the user don't have access on visibility
     * 'Process'
     */
    public void testWithConfidentialVisibilityProcess() {
        searchService = serviceLocator.getSearchService();

        // Instanciate filter access
        instantiate(getProcessName(), INSTANCE_FILE);

        // Get filter
        ExecutableFilterData lExecutableFilterData =
                searchService.getExecutableFilterByName(normalRoleToken,
                        getProcessName(), null, null, FILTER_NAME_PROCESS);

        assertNotNull("The filter " + FILTER_NAME_PROCESS + " not exist.",
                lExecutableFilterData);

        // update filter
        try {
            searchService.updateExecutableFilter(normalRoleToken,
                    lExecutableFilterData);
        }
        catch (AuthorizationException e) {
            fail("The filter " + FILTER_NAME_PROCESS + " has not been updated.");
        }
    }

    /**
     * Test the update filter when the user don't have access on visibility
     * 'Product'
     */
    public void testWithConfidentialVisibilityProduct() {
        searchService = serviceLocator.getSearchService();

        // Instanciate filter access
        instantiate(getProcessName(), INSTANCE_FILE);

        // Get filter
        ExecutableFilterData lExecutableFilterData =
                searchService.getExecutableFilterByName(normalRoleToken,
                        getProcessName(), getProductName(), null,
                        FILTER_NAME_PRODUCT);

        assertNotNull("The filter " + FILTER_NAME_PRODUCT + " not exist.",
                lExecutableFilterData);

        // update filter
        try {
            searchService.updateExecutableFilter(normalRoleToken,
                    lExecutableFilterData);
        }
        catch (AuthorizationException e) {
            fail("The filter " + FILTER_NAME_PRODUCT + " has not been updated.");
        }
    }

    /**
     * Test the update filter when the user don't have access on visibility
     * 'User'
     */
    public void testWithConfidentialVisibilityUser() {
        searchService = serviceLocator.getSearchService();

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

        // update filter
        try {
            searchService.updateExecutableFilter(normalRoleToken,
                    lExecutableFilterData);
            fail("The filter " + FILTER_NAME + " has been updated.");
        }
        catch (AuthorizationException e) {
            // OK
        }
    }

    /**
     * Test the update filter when the user don't have access on type
     */
    public void testWithConfidentialType() {
        searchService = serviceLocator.getSearchService();

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
                + " is updatable.", lExecutableFilterData.isEditable());

        // update filter
        try {
            searchService.updateExecutableFilter(viewerRoleToken,
                    lExecutableFilterData);
            fail("The filter " + FILTER_NAME_WITH_CONFIDENTIAL_TYPE
                    + " has been updated.");
        }
        catch (AuthorizationException e) {
            // OK
        }
    }

    /**
     * Test the update filter when the user don't have access on field
     */
    public void testWithConfidentialField() {
        searchService = serviceLocator.getSearchService();

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
                + " is updatable.", lExecutableFilterData.isEditable());

        // update filter
        try {
            searchService.updateExecutableFilter(viewerRoleToken,
                    lExecutableFilterData);
            fail("The filter " + FILTER_NAME_WITH_CONFIDENTIAL_FIELD
                    + " has been updated.");
        }
        catch (AuthorizationException e) {
            // OK
        }
    }
}
