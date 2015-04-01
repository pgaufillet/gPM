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

import java.util.Collection;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.collections.IteratorUtils;
import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.fields.FieldTypes;
import org.topcased.gpm.business.scalar.StringValueData;
import org.topcased.gpm.business.search.criterias.CriteriaData;
import org.topcased.gpm.business.search.criterias.CriteriaFieldData;
import org.topcased.gpm.business.search.criterias.FilterData;
import org.topcased.gpm.business.search.criterias.FilterTypeData;
import org.topcased.gpm.business.search.criterias.OperationData;
import org.topcased.gpm.business.search.criterias.impl.Operators;
import org.topcased.gpm.business.search.impl.query.FilterQueryConfigurator;
import org.topcased.gpm.business.search.impl.query.FilterResultIterator;
import org.topcased.gpm.business.search.result.sorter.ResultSortingData;
import org.topcased.gpm.business.search.result.sorter.SortingFieldData;
import org.topcased.gpm.business.search.result.summary.ResultSummaryData;
import org.topcased.gpm.business.sheet.impl.CacheableSheetType;
import org.topcased.gpm.business.sheet.service.SheetSummaryData;
import org.topcased.gpm.domain.search.FilterUsage;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * Tests the method <CODE>executeSheetFilter<CODE> of the Search Service.
 * 
 * @author ahaugomm
 */
public class TestExecuteSheetFilterWithEmptyChoiceFieldService extends
        AbstractBusinessServiceTestCase {

    /** The Search Service. */
    private SearchService searchService;

    /** the filter name */
    private static final String FILTER_NAME = "my_filter";

    /** the criteria suffix */
    private static final String CRITERIA_SUFFIX = "_CRIT";

    /** the sorting suffix */
    private static final String SORTING_SUFFIX = "_SORTER";

    /** the summary suffix */
    private static final String SUMMARY_SUFFIX = "_SUMMARY";

    /** the sheet types to filter */
    private static final String[] SHEET_TYPES =
            { GpmTestValues.SHEET_TYPE_CAT, GpmTestValues.SHEET_TYPE_PRICE };

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
    private static final String VALUE_FOR_FIRST_CRITERIA =
            GpmTestValues.CATEGORY_COLOR_VALUE_BLACK;

    /** the value for the first criterion */
    private static final String VALUE_FOR_SECOND_CRITERIA = "%cat%";

    /** result sorting data */
    private static final String[] FIRST_RESULT_SORTING_DATA =
            { "CAT_ref", "Ref", FieldTypes.SIMPLE_STRING_FIELD, Operators.ASC };

    /** data for first result summary field */
    private static final String[] FIRST_RESULT_SUMMARY_DATA =
            { "CAT_ref", "Ref" };

    /** data for second result summary field */
    private static final String[] SECOND_RESULT_SUMMARY_DATA =
            { "CAT_color", GpmTestValues.CATEGORY_COLOR };

    /** data for third result summary field */
    private static final String[] THIRD_RESULT_SUMMARY_DATA =
            { "CAT_furlength", "Fur Length" };

    /** data for fourth result summary field */
    private static final String[] FOURTH_RESULT_SUMMARY_DATA =
            { "CAT_description", "Pet description" };

    /** max number of results to display */
    private static final int MAX_RESULT = 20;

    /** first result to display */
    private static final int FIRST_RESULT = 0;

    /** sheet references that should be in result */
    private static final String[] IN_RESULTS =
            { "Gros Minet", GpmTestValues.SHEET_REF_GARFIELD };

    //    /** max number of results to display */
    //    private static final int MAX = 20;
    //
    //    /** index of the first result to display */
    //    private static final int FIRST = 20;

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.AbstractBusinessServiceTestCase#setUp()
     */
    protected void setUp() {
        super.setUp();
        searchService = serviceLocator.getSearchService();
    }

    /**
     * Tests the method in a normal way.
     */
    @SuppressWarnings("unchecked")
	public void testNormalCase() {

        // Get the sheet type id
        CacheableSheetType lType =
                sheetService.getCacheableSheetTypeByName(adminRoleToken,
                        getProcessName(), SHEET_TYPES[0],
                        CacheProperties.IMMUTABLE);
        String lSheetTypeId = lType.getId();
        assertNotNull("Sheet type " + SHEET_TYPES[0] + " not found.",
                lSheetTypeId);

        // Create the Executable Filter Data to save
        String[] lFieldsContainerIds = { lSheetTypeId };

        FilterVisibilityConstraintData lFilterVisibilityConstraintData =
                new FilterVisibilityConstraintData(ALL_USERS, getProcessName(),
                        ALL_PRODUCTS);

        Map<String, UsableFieldData> lUsableFieldsMap =
                searchService.getUsableFields(adminRoleToken,
                        lFieldsContainerIds, getProcessName());

        // Create the usable field data
        UsableFieldData lUstring =
                lUsableFieldsMap.get(FIRST_CRITERIA_FIELD[0]);

        UsableFieldData lUchoice =
                lUsableFieldsMap.get(SECOND_CRITERIA_FIELD[0]);

        // Create the 2 criteria
        CriteriaData[] lCrits = new CriteriaData[2];
        lCrits[0] =
                new CriteriaFieldData(Operators.EQ, Boolean.TRUE,
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
        lSortingFieldDatas[0].setUsableFieldData(lUsableFieldsMap.get(FIRST_RESULT_SORTING_DATA[0]));
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
        UsableFieldData[] lUsableFieldDatas = new UsableFieldData[lSize];
        lUsableFieldDatas[0] =
                lUsableFieldsMap.get(FIRST_RESULT_SUMMARY_DATA[0]);
        lUsableFieldDatas[1] =
                lUsableFieldsMap.get(SECOND_RESULT_SUMMARY_DATA[0]);
        lUsableFieldDatas[2] =
                lUsableFieldsMap.get(THIRD_RESULT_SUMMARY_DATA[0]);
        lUsableFieldDatas[lSize - 1] =
                lUsableFieldsMap.get(FOURTH_RESULT_SUMMARY_DATA[0]);

        // creation of the result summary data from the filter result field data
        ResultSummaryData lResultSummaryData =
                new ResultSummaryData(null, FILTER_NAME + SUMMARY_SUFFIX,
                        lFieldsContainerIds, lUsableFieldDatas,
                        lFilterVisibilityConstraintData);

        // creation of the executable filter data from the result summary data,
        // the result sorting data, and the filter data
        ExecutableFilterData lExecutableFilterData =
                new ExecutableFilterData(null, FILTER_NAME, "",
                        FilterUsage.BOTH_VIEWS.getValue(), false,
                        lResultSummaryData, lResultSortingData, lFilterData,
                        lFilterVisibilityConstraintData, null);

        assertNotNull("ExecutableFilterData is null", lExecutableFilterData);

        // main test
        startTimer();
        FilterResultIterator<SheetSummaryData> lResult =
                searchService.executeFilter(
                        adminRoleToken,
                        lExecutableFilterData,
                        new FilterVisibilityConstraintData(ALL_USERS,
                                getProcessName(), ALL_PRODUCTS),
                        new FilterQueryConfigurator(0, MAX_RESULT, FIRST_RESULT));
        Collection<SheetSummaryData> lSheetSummaryDatas = IteratorUtils.toList(lResult);
        stopTimer();

        // THE QUERY SHOULD RETURN AT LEAST 2 PRODUCTS : the default product and
        // the product wwith name productName
        assertTrue(lSheetSummaryDatas.size() >= 2);
        Collection<String> lNames = new Vector<String>();
        for (SheetSummaryData lSheetSummaryData : lSheetSummaryDatas) {
            lNames.add(lSheetSummaryData.getSheetReference());
        }
        assertTrue(
                "The query does not return the sheet " + IN_RESULTS[0] + ".",
                lNames.contains(IN_RESULTS[0]));
        assertTrue(
                "The query does not return the sheet " + IN_RESULTS[1] + ".",
                lNames.contains(IN_RESULTS[1]));

    }

    //    /**
    //     * Tests a complete set of criteria with one container
    //     */
    //    public void testCompleteCriteriaWithOneContainer() {
    //
    //        // Get the sheet type id
    //        String lSheetTypeId = this.serviceLocator.getSheetService().getSheetTypeByName(
    //            adminRoleToken, getProcessName(), SHEET_TYPES[1]).getId();
    //        assertNotNull("Sheet type "
    //                + SHEET_TYPES[1] + " not found.", lSheetTypeId);
    //        String[] lIds = { lSheetTypeId };
    //
    //
    //        searchService.setMaxFieldsDepth(1);
    //
    //        Map<String, UsableFieldData> lUsableFieldDatas = serviceLocator.getSearchService().getUsableFieldsForCriteria(
    //            adminRoleToken, lIds, getProcessName());
    //
    //        assertNotNull("No usable fields found for containers :"
    //                + lIds.toString(), lUsableFieldDatas);
    //
    //        List<CriteriaData> lCriteria = createCriteriaDatas(lUsableFieldDatas);
    //
    //        // create visibility constraint
    //        FilterVisibilityConstraintData lFilterVisibilityConstraint = new FilterVisibilityConstraintData(
    //                null, getProcessName(), null);
    //
    //        // build other parts of filter data from
    //        // getUsableFieldsForOrderAndDisplay
    //
    //        Map<String, UsableFieldData> lUsableFieldDatasForSummary = serviceLocator.getSearchService().getUsableFieldsForOrderAndDisplay(
    //            adminRoleToken, lIds, getProcessName());
    //
    //
    //        Collection<UsableFieldData> lResultFields = new Vector<UsableFieldData>();
    //        Collection<SortingFieldData> lSortingFields = new Vector<SortingFieldData>();
    //
    //        for (UsableFieldData lUsableFieldDataForSummary : lUsableFieldDatasForSummary.values()) {
    //            if (!lUsableFieldDataForSummary.getFieldType().equals(
    //                    FieldTypes.MULTIPLE_FIELD)) {
    //                lResultFields.add(lUsableFieldDataForSummary);
    //                lSortingFields.add(new SortingFieldData(null, Operators.ASC,
    //                        lUsableFieldDataForSummary));
    //            }
    //        }
    //        ResultSummaryData lResultSummaryData = new ResultSummaryData(
    //                null, "name_result", lIds,
    //                lResultFields.toArray(new UsableFieldData[0]),
    //                lFilterVisibilityConstraint);
    //
    //        ResultSortingData lResultSortingData = new ResultSortingData(
    //                null, "name_sorter", lIds,
    //                lSortingFields.toArray(new SortingFieldData[0]),
    //                lFilterVisibilityConstraint);
    //
    //        // Create Executable filter from CriteriaData, ResultSummaryData and
    //        // ResultSortingData
    //
    //        ExecutableFilterData lExecutableFilterData = new ExecutableFilterData(
    //                null,
    //                "name",
    //                "descr",
    //                FilterUsage.BOTH_VIEWS.getValue(),
    //                false,
    //                lResultSummaryData,
    //                lResultSortingData,
    //                new FilterData(
    //                        null,
    //                        "filter_criteria",
    //                        false,
    //                        lIds,FilterTypeData.SHEET,
    //                        new OperationData(
    //                                Operators.AND,
    //                                lCriteria.toArray(new CriteriaData[0])),
    //                        lFilterVisibilityConstraint),
    //                lFilterVisibilityConstraint);
    //
    //        Collection<SheetSummaryData> lSheetSummaryDatas = serviceLocator.getSearchService().executeSheetFilter(
    //            adminRoleToken, MAX, FIRST, getProcessName(),
    //            getProductName(), getAdminLogin()[0], lExecutableFilterData);
    //    }
    //
    //    /**
    //     * Tests a complete set of criteria with one container only
    //     */
    //    public void testCompleteCriteriaWithMultipleContainers() {
    //
    //        // Get the sheet type id
    //        String lSheetTypeId = this.serviceLocator.getSheetService().getSheetTypeByName(
    //            adminRoleToken, getProcessName(), SHEET_TYPES[0]).getId();
    //        assertNotNull("Sheet type "
    //                + SHEET_TYPES[0] + " not found.", lSheetTypeId);
    //
    //        // Get the sheet type id
    //        String lSheetTypeId2 = this.serviceLocator.getSheetService().getSheetTypeByName(
    //            adminRoleToken, getProcessName(), SHEET_TYPES[1]).getId();
    //        assertNotNull("Sheet type "
    //                + SHEET_TYPES[1] + " not found.", lSheetTypeId);
    //
    //        String[] lIds = { lSheetTypeId, lSheetTypeId2 };
    //
    //        Map<String, UsableFieldData> lUsableFieldDatas = serviceLocator.getSearchService().getUsableFieldsForCriteria(
    //            adminRoleToken, lIds, getProcessName());
    //
    //        assertNotNull("No usable fields found for containers :"
    //                + lIds.toString(), lUsableFieldDatas);
    //
    //        List<CriteriaData> lCriteria = createCriteriaDatas(lUsableFieldDatas);
    //
    //        // create visibility constraint
    //        FilterVisibilityConstraintData lFilterVisibilityConstraint = new FilterVisibilityConstraintData(
    //                null, getProcessName(), null);
    //
    //        // build other parts of filter data from
    //        // getUsableFieldsForOrderAndDisplay
    //
    //        Map<String, UsableFieldData> lUsableFieldDatasForSummary = serviceLocator.getSearchService().getUsableFieldsForOrderAndDisplay(
    //            adminRoleToken, lIds, getProcessName());
    //
    //        Collection<UsableFieldData> lResultFields = new Vector<UsableFieldData>();
    //        Collection<SortingFieldData> lSortingFields = new Vector<SortingFieldData>();
    //
    //        for (UsableFieldData lUsableFieldDataForSummary : lUsableFieldDatasForSummary.values()) {
    //            if (!lUsableFieldDataForSummary.getFieldType().equals(
    //                    FieldTypes.MULTIPLE_FIELD)) {
    //                lResultFields.add(lUsableFieldDataForSummary);
    //                lSortingFields.add(new SortingFieldData(null, Operators.ASC,
    //                        lUsableFieldDataForSummary));
    //            }
    //        }
    //        ResultSummaryData lResultSummaryData = new ResultSummaryData(
    //                null, "name_result", lIds,
    //                lResultFields.toArray(new UsableFieldData[0]),
    //                lFilterVisibilityConstraint);
    //
    //        ResultSortingData lResultSortingData = new ResultSortingData(
    //                null, "name_sorter", lIds,
    //                lSortingFields.toArray(new SortingFieldData[0]),
    //                lFilterVisibilityConstraint);
    //
    //        // Create Executable filter from CriteriaData, ResultSummaryData and
    //        // ResultSortingData
    //
    //        ExecutableFilterData lExecutableFilterData = new ExecutableFilterData(
    //                null,
    //                "name",
    //                "descr",
    //                FilterUsage.BOTH_VIEWS.getValue(),
    //                false,
    //                lResultSummaryData,
    //                lResultSortingData,
    //                new FilterData(
    //                        null,
    //                        "filter_criteria",
    //                        false,
    //                        lIds,FilterTypeData.SHEET,
    //                        new OperationData(
    //                                Operators.AND,
    //                                lCriteria.toArray(new CriteriaData[0])),
    //                        lFilterVisibilityConstraint),
    //                lFilterVisibilityConstraint);
    //
    //        Collection<SheetSummaryData> lSheetSummaryDatas = serviceLocator.getSearchService().executeSheetFilter(
    //            adminRoleToken, MAX, FIRST, getProcessName(),
    //            getProductName(), getAdminLogin()[0], lExecutableFilterData);
    //
    //    }
    //
    //    /**
    //     * Create a list of CriteriaData from a llist of usableFieldData
    //     *
    //     * @param pUsableFieldDatas
    //     *            the list of usable field datas
    //     * @return a list of corresponding criteria data
    //     */
    //    private List<CriteriaData> createCriteriaDatas(
    //            Map<String, UsableFieldData> pUsableFieldDatas) {
    //        List<CriteriaData> lCriteria = new Vector<CriteriaData>();
    //
    //        for (UsableFieldData lUsableFieldData : pUsableFieldDatas.values()) {
    //            CriteriaFieldData lCriteriaData = null;
    //            if (lUsableFieldData.getFieldType().equals(
    //                FieldTypes.SIMPLE_STRING_FIELD)
    //                    || lUsableFieldData.getFieldType().equals(
    //                        FieldTypes.ATTACHED_FIELD)) {
    //                lCriteriaData = new CriteriaFieldData(Operators.LIKE,
    //                        new StringValueData("%"), lUsableFieldData);
    //            }
    //            else if (lUsableFieldData.getFieldType().equals(
    //                FieldTypes.CHOICE_FIELD)) {
    //                lCriteriaData = new CriteriaFieldData(Operators.NEQ,
    //                        new StringValueData("DEFAULT"),
    //                        lUsableFieldData);
    //            }
    //            else if (lUsableFieldData.getFieldType().equals(
    //                FieldTypes.SIMPLE_BOOLEAN_FIELD)) {
    //                lCriteriaData = new CriteriaFieldData(Operators.EQ,
    //                        new BooleanValueData(true), lUsableFieldData);
    //            }
    //            else if (lUsableFieldData.getFieldType().equals(
    //                FieldTypes.SIMPLE_INTEGER_FIELD)) {
    //                lCriteriaData = new CriteriaFieldData(Operators.GE,
    //                        new IntegerValueData(0), lUsableFieldData);
    //            }
    //            else if (lUsableFieldData.getFieldType().equals(
    //                FieldTypes.SIMPLE_REAL_FIELD)) {
    //                lCriteriaData = new CriteriaFieldData(Operators.GE,
    //                        new RealValueData(0), lUsableFieldData);
    //            }
    //            else if (lUsableFieldData.getFieldType().equals(
    //                FieldTypes.SIMPLE_DATE_FIELD)) {
    //                lCriteriaData = new CriteriaFieldData(Operators.GE,
    //                        new DateValueData(new Date(0)),
    //                        lUsableFieldData);
    //            }
    //            else if (lUsableFieldData.getFieldType().equals(
    //                FieldTypes.VIRTUAL_FIELD)) {
    //
    //                VirtualFieldData lVirtualFieldData = (VirtualFieldData)lUsableFieldData;
    //                if( VirtualFieldType.$PRODUCT_HIERARCHY.getValue().equals(lVirtualFieldData.getFieldName())){
    //                    lCriteriaData = new CriteriaFieldData(
    //                        Operators.EQ, new StringValueData("Bernard's store"),
    //                        lVirtualFieldData);
    //                }
    //                else if (lVirtualFieldData.getVirtualFieldType().equals(
    //                    FieldTypes.SIMPLE_STRING_FIELD)
    //                        || lVirtualFieldData.getVirtualFieldType().equals(
    //                            FieldTypes.CHOICE_FIELD)
    //                        || lVirtualFieldData.getVirtualFieldType().equals(
    //                            FieldTypes.ATTACHED_FIELD)) {
    //                    lCriteriaData = new CriteriaFieldData(
    //                            Operators.LIKE, new StringValueData("%"),
    //                            lUsableFieldData);
    //                }
    //
    //                else if (lVirtualFieldData.getVirtualFieldType().equals(
    //                    FieldTypes.SIMPLE_BOOLEAN_FIELD)) {
    //                    lCriteriaData = new CriteriaFieldData(
    //                            Operators.EQ, new BooleanValueData(true),
    //                            lUsableFieldData);
    //                }
    //                else if (lVirtualFieldData.getVirtualFieldType().equals(
    //                    FieldTypes.SIMPLE_INTEGER_FIELD)) {
    //                    lCriteriaData = new CriteriaFieldData(
    //                            Operators.GE, new IntegerValueData(0),
    //                            lUsableFieldData);
    //                }
    //                else if (lVirtualFieldData.getVirtualFieldType().equals(
    //                    FieldTypes.SIMPLE_REAL_FIELD)) {
    //                    lCriteriaData = new CriteriaFieldData(
    //                            Operators.GE, new RealValueData(0),
    //                            lUsableFieldData);
    //                }
    //                else if (lVirtualFieldData.getVirtualFieldType().equals(
    //                    FieldTypes.SIMPLE_DATE_FIELD)) {
    //                    lCriteriaData = new CriteriaFieldData(
    //                            Operators.GE, new DateValueData(new Date(
    //                                    0)), lUsableFieldData);
    //                }
    //            }
    //            if (lCriteriaData != null) {
    //                lCriteria.add(lCriteriaData);
    //            }
    //        }
    //
    //        return lCriteria;
    //    }
}
