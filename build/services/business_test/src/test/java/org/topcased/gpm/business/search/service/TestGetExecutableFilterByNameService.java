/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Anne Haugommard(Atos Origin), 
 *  Vincent Hemery (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.search.service;

import java.util.HashMap;
import java.util.Map;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.fields.FieldType;
import org.topcased.gpm.business.fields.FieldTypes;
import org.topcased.gpm.business.scalar.StringValueData;
import org.topcased.gpm.business.search.criterias.CriteriaData;
import org.topcased.gpm.business.search.criterias.CriteriaFieldData;
import org.topcased.gpm.business.search.criterias.FilterData;
import org.topcased.gpm.business.search.criterias.FilterTypeData;
import org.topcased.gpm.business.search.criterias.OperationData;
import org.topcased.gpm.business.search.criterias.impl.Operators;
import org.topcased.gpm.business.search.impl.SearchServiceTestUtils;
import org.topcased.gpm.business.search.result.sorter.ResultSortingData;
import org.topcased.gpm.business.search.result.sorter.SortingFieldData;
import org.topcased.gpm.business.search.result.summary.ResultSummaryData;
import org.topcased.gpm.business.sheet.impl.CacheableSheetType;
import org.topcased.gpm.domain.search.FilterUsage;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * Tests the method <CODE>getExecutableFilterByName<CODE> of the Search
 * Service.
 * 
 * @author ahaugomm
 */
public class TestGetExecutableFilterByNameService extends
        AbstractBusinessServiceTestCase {

    /** The Search Service. */
    private SearchService searchService;

    /** the product name */
    private static final String PRODUCT_NAME =
            GpmTestValues.PRODUCT_HAPPY_MOUSE_NAME;

    /** the filter name */
    private static final String FILTER_NAME = "my_filter";

    /** the summary suffix */
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

    /** Name of filters with same name and different scopes */
    private static final String FILTER_WITH_SAME_NAME =
            GpmTestValues.FILTER_TEST_FILTER_WITH_SAME_NAME;

    /**
     * Name of the only field label key for filters with same name, for each
     * visibility scope
     */
    private static final Map<FilterScope, String> FILTER_WITH_SAME_NAME_FIELD =
            new HashMap<FilterScope, String>(3);
    static {
        FILTER_WITH_SAME_NAME_FIELD.put(FilterScope.USER_FILTER,
                "CAT_description");
        FILTER_WITH_SAME_NAME_FIELD.put(FilterScope.PRODUCT_FILTER,
                "$PRODUCT_NAME");
        FILTER_WITH_SAME_NAME_FIELD.put(FilterScope.INSTANCE_FILTER,
                "$SHEET_REFERENCE");
    }

    /**
     * Tests the method in a normal way.
     */
    public void testNormalCase() {
        searchService = serviceLocator.getSearchService();

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
        final int lSize = 4;
        Map<String, UsableFieldData> lAvailableFields =
                searchService.getUsableFields(adminRoleToken,
                        lFieldsContainerIds, getProcessName());

        UsableFieldData[] lUsableFieldDatas = new UsableFieldData[lSize];
        lUsableFieldDatas[0] = lAvailableFields.get("CAT_ref");
        lUsableFieldDatas[1] = lAvailableFields.get("CAT_color");
        lUsableFieldDatas[2] = lAvailableFields.get("CAT_furlength");
        lUsableFieldDatas[lSize - 1] = lAvailableFields.get("CAT_description");

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

        // Create the filter in the database
        String lId =
                searchService.createExecutableFilter(adminRoleToken,
                        lExecutableFilterData);

        assertNotNull("createExecutableFilter returns a null Id", lId);

        // Main test
        startTimer();
        ExecutableFilterData lNewExecutableFilterData =
                this.searchService.getExecutableFilterByName(adminRoleToken,
                        getProcessName(), null, null, FILTER_NAME);
        stopTimer();

        assertNotNull("The executable filter " + FILTER_NAME
                + " does not exist in the DB.", lNewExecutableFilterData);
        assertTrue("The executable filter " + FILTER_NAME
                + " does not correspond to the initial filter saved",
                SearchServiceTestUtils.areEqual(lExecutableFilterData,
                        lNewExecutableFilterData));

    }

    /**
     * Tests the method with different elements with the same name and different
     * scopes.
     * 
     * @author vhemery
     */
    public void testSameNameCase() {
        searchService = serviceLocator.getSearchService();

        // Main test
        // instance's filter
        startTimer();
        ExecutableFilterData lExecutableFilterData =
                this.searchService.getExecutableFilterByName(adminRoleToken,
                        getProcessName(), null, null, FILTER_WITH_SAME_NAME);
        stopTimer();

        assertNotNull("The instance's executable filter "
                + FILTER_WITH_SAME_NAME + " does not exist in the DB.",
                lExecutableFilterData);
        assertTrue(
                "The instance's executable filter " + FILTER_WITH_SAME_NAME
                        + " does not correspond to the expected one.",
                lExecutableFilterData.getResultSummaryData().getUsableFieldDatas().length > 0);
        assertEquals(
                "The instance's executable filter " + FILTER_WITH_SAME_NAME
                        + " does not correspond to the expected one.",
                FILTER_WITH_SAME_NAME_FIELD.get(FilterScope.INSTANCE_FILTER),
                lExecutableFilterData.getResultSummaryData().getUsableFieldDatas()[0].getFieldName());

        // product's filter
        startTimer();
        lExecutableFilterData =
                this.searchService.getExecutableFilterByName(adminRoleToken,
                        getProcessName(), PRODUCT_NAME, null,
                        FILTER_WITH_SAME_NAME);
        stopTimer();

        assertNotNull("The product's executable filter "
                + FILTER_WITH_SAME_NAME + " does not exist in the DB.",
                lExecutableFilterData);
        assertTrue(
                "The product's executable filter " + FILTER_WITH_SAME_NAME
                        + " does not correspond to the expected one.",
                lExecutableFilterData.getResultSummaryData().getUsableFieldDatas().length > 0);
        assertEquals(
                "The product's executable filter " + FILTER_WITH_SAME_NAME
                        + " does not correspond to the expected one.",
                FILTER_WITH_SAME_NAME_FIELD.get(FilterScope.PRODUCT_FILTER),
                lExecutableFilterData.getResultSummaryData().getUsableFieldDatas()[0].getFieldName());

        // user's filter
        startTimer();
        lExecutableFilterData =
                this.searchService.getExecutableFilterByName(adminRoleToken,
                        getProcessName(), null, ADMIN_LOGIN[0],
                        FILTER_WITH_SAME_NAME);
        stopTimer();

        assertNotNull("The user's executable filter " + FILTER_WITH_SAME_NAME
                + " does not exist in the DB.", lExecutableFilterData);
        assertTrue(
                "The user's executable filter " + FILTER_WITH_SAME_NAME
                        + " does not correspond to the expected one.",
                lExecutableFilterData.getResultSummaryData().getUsableFieldDatas().length > 0);
        assertEquals(
                "The user's executable filter " + FILTER_WITH_SAME_NAME
                        + " does not correspond to the expected one.",
                FILTER_WITH_SAME_NAME_FIELD.get(FilterScope.USER_FILTER),
                lExecutableFilterData.getResultSummaryData().getUsableFieldDatas()[0].getFieldName());
    }
}
