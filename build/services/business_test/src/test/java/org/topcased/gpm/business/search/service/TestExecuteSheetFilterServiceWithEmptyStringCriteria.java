/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Magali Franchet (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.search.service;

import java.util.Collection;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.collections.IteratorUtils;
import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.fields.FieldTypes;
import org.topcased.gpm.business.search.criterias.CriteriaFieldData;
import org.topcased.gpm.business.search.criterias.FilterData;
import org.topcased.gpm.business.search.criterias.FilterTypeData;
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
 * TestExecuteSheetFilterServiceWithEmptyStringCriteria : Test the execution of
 * filter with empty string criteria
 * 
 * @author Magali Franchet
 */
public class TestExecuteSheetFilterServiceWithEmptyStringCriteria extends
        AbstractBusinessServiceTestCase {

    /** The Search Service. */
    protected SearchService searchService;

    /** the filter name */
    protected static final String FILTER_NAME = "my_filter";

    /** the criteria suffix */
    protected static final String CRITERIA_SUFFIX = "_CRIT";

    /** the sorting suffix */
    protected static final String SORTING_SUFFIX = "_SORTER";

    /** the summary suffix */
    protected static final String SUMMARY_SUFFIX = "_SUMMARY";

    /** the sheet types to filter */
    protected static final String[] SHEET_TYPES =
            { GpmTestValues.SHEET_TYPE_CAT, GpmTestValues.SHEET_TYPE_PRICE };

    /** the user login for filter constraint : all users */
    protected static final String ALL_USERS = null;

    /** the product name for filter constraint : all products */
    protected static final String ALL_PRODUCTS = null;

    /** the properties of the first criterion field */
    protected static final String[] FIRST_CRITERIA_FIELD =
            { "CAT_description", "Pet description",
             FieldTypes.SIMPLE_STRING_FIELD };

    /** the properties of the second criterion field */
    protected static final String[] SECOND_CRITERIA_FIELD =
            { "CAT_color", GpmTestValues.CATEGORY_COLOR,
             FieldTypes.CHOICE_FIELD };

    /** the value for the first criterion */
    protected static final String VALUE_FOR_FIRST_CRITERIA =
            GpmTestValues.CATEGORY_COLOR_VALUE_BLACK;

    /** the value for the first criterion */
    protected static final String VALUE_FOR_SECOND_CRITERIA = "%cat%";

    /** result sorting data */
    protected static final String[] FIRST_RESULT_SORTING_DATA =
            { "CAT_ref", "Ref", FieldTypes.SIMPLE_STRING_FIELD, Operators.ASC };

    /** data for first result summary field */
    protected static final String[] FIRST_RESULT_SUMMARY_DATA =
            { "CAT_ref", "Ref", "Reference" };

    /** data for second result summary field */
    protected static final String[] SECOND_RESULT_SUMMARY_DATA =
            { "CAT_color", GpmTestValues.CATEGORY_COLOR };

    /** data for third result summary field */
    protected static final String[] THIRD_RESULT_SUMMARY_DATA =
            { "CAT_furlength", "Fur Length" };

    /** data for fourth result summary field */
    protected static final String[] FOURTH_RESULT_SUMMARY_DATA =
            { "CAT_description", "Pet description", "Description" };

    /** max number of results to display */
    protected static final int MAX_RESULT = 20;

    /** first result to display */
    protected static final int FIRST_RESULT = 0;

    /** sheet references that should be in result */
    protected static final String[] IN_RESULTS = { "Cat3" };

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.service.TestExecuteSheetFilterService#testNormalCase()
     */
    @SuppressWarnings("unchecked")
	public void testNormalCase() {
        searchService = serviceLocator.getSearchService();

        // Get the sheet type id
        CacheableSheetType lType =
                sheetService.getCacheableSheetTypeByName(adminRoleToken,
                        getProcessName(), SHEET_TYPES[0],
                        CacheProperties.IMMUTABLE);
        String lSheetTypeId = lType.getId();
        assertNotNull("Sheet type " + SHEET_TYPES[0] + " not found.",
                lSheetTypeId);

        // Create the Executable Filter Data to save
        ExecutableFilterData lExecutableFilterData =
                createExecutableFilterData(lSheetTypeId);

        assertNotNull("ExecutableFilterData is null", lExecutableFilterData);

        // main test
        FilterResultIterator<SheetSummaryData> lResult =
                searchService.executeFilter(
                        adminRoleToken,
                        lExecutableFilterData,
                        new FilterVisibilityConstraintData(ALL_USERS,
                                getProcessName(), ALL_PRODUCTS),
                        new FilterQueryConfigurator(0, MAX_RESULT, FIRST_RESULT));
        Collection<SheetSummaryData> lSheetSummaryDatas = IteratorUtils.toList(lResult);
        assertNotNull(lSheetSummaryDatas);
        assertTrue(lSheetSummaryDatas.size() >= 1);

        Collection<String> lNames = new Vector<String>();
        for (SheetSummaryData lSheetSummaryData : lSheetSummaryDatas) {
            lNames.add(lSheetSummaryData.getSheetReference());
        }
        assertTrue(
                "The query does not return the sheet " + IN_RESULTS[0] + ".",
                lNames.contains(IN_RESULTS[0]));

    }

    /**
     * Create the executable filter data
     * 
     * @param pSheetTypeId
     *            The sheet type id
     * @return The created executable filter data
     */
    protected ExecutableFilterData createExecutableFilterData(
            String pSheetTypeId) {

        String[] lFieldsContainerIds = { pSheetTypeId };

        FilterVisibilityConstraintData lFilterVisibilityConstraintData =
                new FilterVisibilityConstraintData(ALL_USERS, getProcessName(),
                        ALL_PRODUCTS);

        Map<String, UsableFieldData> lUsableFieldsMap =
                searchService.getUsableFields(adminRoleToken,
                        lFieldsContainerIds, getProcessName());

        // Create the usable field data
        UsableFieldData lUstring =
                lUsableFieldsMap.get(FIRST_CRITERIA_FIELD[0]);

        // Create empty criteria
        CriteriaFieldData lCriteriaFieldData =
                new CriteriaFieldData(Operators.LIKE, Boolean.TRUE, null,
                        lUstring);

        // Create the filter data from the criteria data, the fieldsContainer,
        // the filter name and the visibility constraints
        FilterData lFilterData =
                new FilterData(null, FILTER_NAME + CRITERIA_SUFFIX, false,
                        lFieldsContainerIds, FilterTypeData.SHEET,
                        lCriteriaFieldData, lFilterVisibilityConstraintData);

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

        return lExecutableFilterData;
    }

}
