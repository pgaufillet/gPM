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
import org.topcased.gpm.business.search.result.sorter.ResultSortingData;
import org.topcased.gpm.business.search.result.sorter.SortingFieldData;
import org.topcased.gpm.business.search.result.summary.ResultSummaryData;
import org.topcased.gpm.business.sheet.impl.CacheableSheetType;
import org.topcased.gpm.domain.search.FilterUsage;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * FiltersCreation
 * 
 * @author mfranche
 */
public class FiltersCreationUtils extends AbstractBusinessServiceTestCase {

    /** the user login for filter constraint : all users */
    private static final String ALL_USERS = null;

    /** the product name for filter constraint : all products */
    private static final String ALL_PRODUCTS = null;

    /** the instance filter name */
    private static final String INSTANCE_FILTER_NAME = "instance_filter";

    /** the sheet type to filter */
    private static final String SHEET_TYPE = GpmTestValues.SHEET_TYPE_CAT;

    /** the properties of the first criterion field */
    private static final String[] FIRST_CRITERIA_FIELD =
            { "CAT_description", "Pet description",
             FieldTypes.SIMPLE_STRING_FIELD };

    /** the properties of the second criterion field */
    private static final String[] SECOND_CRITERIA_FIELD =
            { "CAT_color", GpmTestValues.CATEGORY_COLOR,
             FieldTypes.CHOICE_FIELD };

    /** the filter description */
    private static final String FILTER_DESCR = "my_filter_description";

    /** the value for the first criterion */
    private static final String VALUE_FOR_FIRST_CRITERIA = "%BLACK%";

    /** the value for the first criterion */
    private static final String VALUE_FOR_SECOND_CRITERIA = "%cat%";

    /** the criteria suffix */
    private static final String CRITERIA_SUFFIX = "_CRIT";

    /** result sorting data */
    private static final String[] FIRST_RESULT_SORTING_DATA =
            { "CAT_ref", "Ref", FieldTypes.SIMPLE_STRING_FIELD, Operators.ASC };

    /** the sorting suffix */
    private static final String SORTING_SUFFIX = "_SORTER";

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

    /** the summary suffix */
    private static final String SUMMARY_SUFFIX = "_SUMMARY";

    /** the product filter name */
    private static final String PRODUCT_FILTER_NAME = "product_filter";

    /** the user on which user filter is set */
    private static final String USER_FILTER_LOGIN = GpmTestValues.USER_ADMIN;

    /** the user filter name */
    private static final String USER_FILTER_NAME = "user_filter";

    /**
     * Create executable filter data visible for all instance
     * 
     * @return created executable filter data
     */
    protected ExecutableFilterData createInstanceExecutableFilterData() {
        FilterVisibilityConstraintData lFilterVisibilityConstraintData =
                new FilterVisibilityConstraintData(ALL_USERS, getProcessName(),
                        ALL_PRODUCTS);

        return createGeneralFilterData(lFilterVisibilityConstraintData,
                INSTANCE_FILTER_NAME);
    }

    /**
     * Create executable filter data visible for product
     * 
     * @return created executable filter data
     */
    protected ExecutableFilterData createProductExecutableFilterData() {
        FilterVisibilityConstraintData lFilterVisibilityConstraintData =
                new FilterVisibilityConstraintData(ALL_USERS, getProcessName(),
                        GpmTestValues.PRODUCT1_NAME);

        return createGeneralFilterData(lFilterVisibilityConstraintData,
                PRODUCT_FILTER_NAME);
    }

    /**
     * Create executable filter data visible for current user
     * 
     * @return created executable filter data
     */
    protected ExecutableFilterData createUserExecutableFilterData() {
        FilterVisibilityConstraintData lFilterVisibilityConstraintData =
                new FilterVisibilityConstraintData(USER_FILTER_LOGIN,
                        getProcessName(), ALL_PRODUCTS);

        return createGeneralFilterData(lFilterVisibilityConstraintData,
                USER_FILTER_NAME);
    }

    /**
     * Create a general executable filter data
     * 
     * @param pFilterVisibilityConstraintData
     *            the constraint data
     * @param pFilterName
     *            the filter name
     * @return the created executable filter data
     */
    protected ExecutableFilterData createGeneralFilterData(
            FilterVisibilityConstraintData pFilterVisibilityConstraintData,
            String pFilterName) {
        CacheableSheetType lTypeData =
                serviceLocator.getSheetService().getCacheableSheetTypeByName(
                        adminRoleToken, getProcessName(), SHEET_TYPE,
                        CacheProperties.IMMUTABLE);
        String lSheetTypeId = lTypeData.getId();
        assertNotNull("Sheet type " + SHEET_TYPE + " not found.", lSheetTypeId);

        // Create the Executable Filter Data to save
        String[] lFieldsContainerIds = { lSheetTypeId };

        // Create the usable field data
        UsableFieldData lUstring = new UsableFieldData();
        lUstring.setFieldName(FIRST_CRITERIA_FIELD[0]);
        lUstring.setFieldType(FieldType.valueOf(FIRST_CRITERIA_FIELD[2]));

        UsableFieldData lUchoice = new UsableFieldData();
        lUchoice.setFieldName(SECOND_CRITERIA_FIELD[0]);
        lUchoice.setFieldType(FieldType.valueOf(SECOND_CRITERIA_FIELD[2]));

        FilterData lFilterData =
                createFilterData(pFilterVisibilityConstraintData,
                        lFieldsContainerIds, lUchoice, lUstring, pFilterName);

        ResultSortingData lResultSortingData =
                createResultSortingData(pFilterVisibilityConstraintData,
                        lFieldsContainerIds, lUchoice, lUstring, pFilterName);

        ResultSummaryData lResultSummaryData =
                createResultSummaryData(pFilterVisibilityConstraintData,
                        lFieldsContainerIds, pFilterName);

        // creation of the executable filter data from the result summary data,
        // the result sorting data, and the filter data
        return new ExecutableFilterData(null, pFilterName, FILTER_DESCR,
                FilterUsage.BOTH_VIEWS.getValue(), false, lResultSummaryData,
                lResultSortingData, lFilterData,
                pFilterVisibilityConstraintData, null);
    }

    /**
     * Create filter data
     * 
     * @param pFilterVisibilityContraintData
     *            constraint to set in filter data
     * @param pFieldsContainerIds
     *            field ids on which filter is created
     * @param pUChoice
     *            usable field data
     * @param pUString
     *            usable field data
     * @param pFilterName
     *            the filter name
     * @return created filter data
     */
    protected FilterData createFilterData(
            FilterVisibilityConstraintData pFilterVisibilityContraintData,
            String[] pFieldsContainerIds, UsableFieldData pUChoice,
            UsableFieldData pUString, String pFilterName) {
        // Create the 2 criteria
        CriteriaData[] lCrits = new CriteriaData[2];
        lCrits[0] =
                new CriteriaFieldData(Operators.LIKE, Boolean.TRUE,
                        new StringValueData(VALUE_FOR_FIRST_CRITERIA), pUChoice);
        lCrits[1] =
                new CriteriaFieldData(Operators.LIKE, Boolean.TRUE,
                        new StringValueData(VALUE_FOR_SECOND_CRITERIA),
                        pUString);

        // Create the Operation data (AND between the two criteria)
        OperationData lCriteriaData = new OperationData(Operators.AND, lCrits);

        // Create the filter data from the criteria data, the fieldsContainer,
        // the filter name and the visibility constraints
        return new FilterData(null, pFilterName + CRITERIA_SUFFIX, false,
                pFieldsContainerIds, FilterTypeData.SHEET, lCriteriaData,
                pFilterVisibilityContraintData);
    }

    /**
     * Create result sorting data
     * 
     * @param pFilterVisibilityConstraintData
     *            constraint to be set in result sorting data
     * @param pFieldsContainerIds
     *            field ids on which filter is created
     * @param pUChoice
     *            usable field data
     * @param pUString
     *            usable field data
     * @param pFilterName
     *            the filter name
     * @return created result sorting data
     */
    protected ResultSortingData createResultSortingData(
            FilterVisibilityConstraintData pFilterVisibilityConstraintData,
            String[] pFieldsContainerIds, UsableFieldData pUChoice,
            UsableFieldData pUString, String pFilterName) {
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
        lSortingFieldDatas[1].setUsableFieldData(pUChoice);
        lSortingFieldDatas[1].setOrder(Operators.DESC);

        lSortingFieldDatas[2] = new SortingFieldData();
        lSortingFieldDatas[2].setUsableFieldData(pUString);
        lSortingFieldDatas[2].setOrder(Operators.ASC);

        // creation of the result sorting data from the sorting field data
        return new ResultSortingData(null, pFilterName + SORTING_SUFFIX,
                pFieldsContainerIds, lSortingFieldDatas,
                pFilterVisibilityConstraintData);
    }

    /**
     * Create result summary data
     * 
     * @param pFilterVisibilityConstraintData
     *            constraint to be set in result summary
     * @param pFieldsContainerIds
     *            field ids on which filter is created
     * @param pFilterName
     *            the filter name
     * @return created result summary data
     */
    protected ResultSummaryData createResultSummaryData(
            FilterVisibilityConstraintData pFilterVisibilityConstraintData,
            String[] pFieldsContainerIds, String pFilterName) {
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
        return new ResultSummaryData(null, pFilterName + SUMMARY_SUFFIX,
                pFieldsContainerIds, lUsableFieldDatas,
                pFilterVisibilityConstraintData);
    }
}
