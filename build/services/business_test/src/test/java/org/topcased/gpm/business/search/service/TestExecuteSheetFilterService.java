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
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.collections.IteratorUtils;
import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.fields.FieldType;
import org.topcased.gpm.business.fields.FieldTypes;
import org.topcased.gpm.business.scalar.BooleanValueData;
import org.topcased.gpm.business.scalar.DateValueData;
import org.topcased.gpm.business.scalar.IntegerValueData;
import org.topcased.gpm.business.scalar.RealValueData;
import org.topcased.gpm.business.scalar.StringValueData;
import org.topcased.gpm.business.search.criterias.CriteriaData;
import org.topcased.gpm.business.search.criterias.CriteriaFieldData;
import org.topcased.gpm.business.search.criterias.FilterData;
import org.topcased.gpm.business.search.criterias.FilterTypeData;
import org.topcased.gpm.business.search.criterias.OperationData;
import org.topcased.gpm.business.search.criterias.impl.FilterFieldsContainerInfo;
import org.topcased.gpm.business.search.criterias.impl.Operators;
import org.topcased.gpm.business.search.criterias.impl.VirtualFieldData;
import org.topcased.gpm.business.search.impl.query.FilterQueryConfigurator;
import org.topcased.gpm.business.search.impl.query.FilterResultIterator;
import org.topcased.gpm.business.search.result.sorter.ResultSortingData;
import org.topcased.gpm.business.search.result.sorter.SortingFieldData;
import org.topcased.gpm.business.search.result.summary.ResultSummaryData;
import org.topcased.gpm.business.sheet.impl.CacheableSheetType;
import org.topcased.gpm.business.sheet.service.SheetSummaryData;
import org.topcased.gpm.business.values.field.virtual.VirtualFieldType;
import org.topcased.gpm.domain.search.FilterUsage;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * Tests the method <CODE>executeSheetFilter<CODE> of the Search Service.
 * 
 * @author ahaugomm
 */
public class TestExecuteSheetFilterService extends
        AbstractBusinessServiceTestCase {

    /** The Search Service. */
    protected SearchService searchService;

    /** The product name. */
    //    private static final String PRODUCT_NAME = GpmTestValues.PRODUCT_HAPPY_MOUSE_NAME;

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
    protected static final String[] IN_RESULTS =
            { "Gros Minet", GpmTestValues.SHEET_REF_GARFIELD };

    private static final String[] USER4 =
            { GpmTestValues.USER_USER4, "pwd4", "notadmin" };

    private static final int LEVEL_3 = 3;

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.AbstractBusinessServiceTestCase#setUp()
     */
    protected void setUp() {
        super.setUp();
        searchService = serviceLocator.getSearchService();
        normalUserToken = authorizationService.login(USER4[0], USER4[1]);
        normalRoleToken =
                authorizationService.selectRole(normalUserToken, USER4[2],
                        getProductName(), getProcessName());
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
        ExecutableFilterData lExecutableFilterData =
                createExecutableFilterData(lSheetTypeId);

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

    /**
     * Create a list of CriteriaData from a llist of usableFieldData
     * 
     * @param pUsableFieldDatas
     *            the list of usable field datas
     * @return a list of corresponding criteria data
     */
    private List<CriteriaData> createCriteriaDatas(
            Map<String, UsableFieldData> pUsableFieldDatas) {
        List<CriteriaData> lCriteria = new Vector<CriteriaData>();

        for (UsableFieldData lUsableFieldData : pUsableFieldDatas.values()) {

            List<FilterFieldsContainerInfo> lHierarchy = null;
            if (lUsableFieldData instanceof VirtualFieldData) {
                lHierarchy =
                        ((VirtualFieldData) lUsableFieldData).getFieldsContainerHierarchy();
            }
            if (lHierarchy == null || lHierarchy.isEmpty()) {
                CriteriaFieldData lCriteriaData = null;
                switch (lUsableFieldData.getFieldType()) {
                    case SIMPLE_STRING_FIELD:
                        lCriteriaData =
                                new CriteriaFieldData(Operators.LIKE,
                                        Boolean.TRUE, new StringValueData("%"),
                                        lUsableFieldData);
                        break;
                    case ATTACHED_FIELD:
                        lCriteriaData =
                                new CriteriaFieldData(Operators.LIKE,
                                        Boolean.TRUE, new StringValueData("%"),
                                        lUsableFieldData);
                        break;
                    case CHOICE_FIELD:
                        lCriteriaData =
                                new CriteriaFieldData(Operators.NEQ,
                                        Boolean.TRUE, new StringValueData(
                                                "DEFAULT"), lUsableFieldData);
                        break;
                    case SIMPLE_BOOLEAN_FIELD:
                        lCriteriaData =
                                new CriteriaFieldData(Operators.EQ,
                                        Boolean.TRUE,
                                        new BooleanValueData(true),
                                        lUsableFieldData);
                        break;
                    case SIMPLE_INTEGER_FIELD:
                        lCriteriaData =
                                new CriteriaFieldData(Operators.GE,
                                        Boolean.TRUE, new IntegerValueData(0),
                                        lUsableFieldData);
                        break;
                    case SIMPLE_REAL_FIELD:
                        lCriteriaData =
                                new CriteriaFieldData(Operators.GE,
                                        Boolean.TRUE, new RealValueData(0.),
                                        lUsableFieldData);
                        break;
                    case SIMPLE_DATE_FIELD:
                        lCriteriaData =
                                new CriteriaFieldData(Operators.GE,
                                        Boolean.TRUE, new DateValueData(
                                                new Date(0)), lUsableFieldData);
                        break;
                    // Do nothing for an incorrect type. 
                    default:

                }

                if (lCriteriaData != null) {
                    lCriteria.add(lCriteriaData);
                }
            }
        }

        return lCriteria;
    }

    /**
     * Get the sheet garfield id
     * 
     * @param pList
     *            The SheetSummaryData list
     * @return The id for the garfield sheet
     */
    protected String getGarfieldSheetId(List<SheetSummaryData> pList) {
        boolean lFound = false;
        int i = 0;

        SheetSummaryData lSheetSummaryData = null;

        while (!lFound && i < pList.size()) {
            lSheetSummaryData = pList.get(i);
            if (lSheetSummaryData.getSheetReference().compareTo(
                    GpmTestValues.SHEET_REF_GARFIELD) == 0) {
                lFound = true;
            }
            else {
                i++;
            }
        }

        if (!lFound) {
            lSheetSummaryData = null;
        }

        if (lSheetSummaryData != null) {
            return lSheetSummaryData.getId();
        }
        // else
        return null;
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

        return lExecutableFilterData;
    }

    private static final String INSTANCE_FILE =
            "search/TestExecuteSheetFilterService.xml";

    private static final String FILTER_WITH_LEVEL_NAME = "FilterOnSeveralLevel";

    private static final String[] EXPECTED_RESULT_FOR_LEVEL =
            { "sheet_with_some_confidential_fields_01" };

    /**
     * Test filter execution (and creation via instantiation tool) with:
     * <ul>
     * <li>Criteria on several level
     * <li>Criteria on all field types
     * <li>Scope on 1 product and its sub-products and another product.
     * <li>Sheet reference (field) as result field
     * </ul>
     */
    public void testFilterOnLevelFields() {
        instantiate(getProcessName(), INSTANCE_FILE);

        ExecutableFilterData lExecutableFilterData =
                searchService.getExecutableFilterByName(normalRoleToken,
                        getProcessName(), null, null, FILTER_WITH_LEVEL_NAME);
        assertNotNull("ExecutableFilterData is null", lExecutableFilterData);

        // main test
        startTimer();
        Collection<SheetSummaryData> lSheetSummaryDatas =
                executeSheetFilter(normalRoleToken, getProcessName(), null,
                        null, lExecutableFilterData);
        stopTimer();

        // THE QUERY SHOULD RETURN AT LEAST 1 SHEET.
        assertEquals(lSheetSummaryDatas.size(), 1);
        Collection<String> lNames =
                new ArrayList<String>(lSheetSummaryDatas.size());
        for (SheetSummaryData lSheetSummaryData : lSheetSummaryDatas) {
            lNames.add(lSheetSummaryData.getSheetReference());
        }

        assertEqualsUnordered(EXPECTED_RESULT_FOR_LEVEL,
                lNames.toArray(new String[0]));
    }

    private static final String FILTER_WITH_LEVEL_PRODUCT_FIELDS_NAME =
            "FilterOnSeveralLevel_ProductFields";

    private static final String[] EXPECTED_RESULT_PRODUCT_FIELDS_FOR_LEVEL =
            { "sheet_with_some_confidential_fields_01",
             "sheet_with_some_confidential_fields_04" };

    /**
     * Test filter execution (and creation via instantiation tool) with:
     * <ul>
     * <li>No scope
     * <li>A product field as criterion
     * <li>Sheet reference as result field
     * </ul>
     */
    public void testFilterOnLevelFieldsWithProductFields() {
        instantiate(getProcessName(), INSTANCE_FILE);

        ExecutableFilterData lExecutableFilterData =
                searchService.getExecutableFilterByName(normalRoleToken,
                        getProcessName(), null, null,
                        FILTER_WITH_LEVEL_PRODUCT_FIELDS_NAME);
        assertNotNull("ExecutableFilterData is null", lExecutableFilterData);

        // main test
        startTimer();
        Collection<SheetSummaryData> lSheetSummaryDatas =
                executeSheetFilter(normalRoleToken, getProcessName(), null,
                        null, lExecutableFilterData);
        stopTimer();

        // THE QUERY SHOULD RETURN AT LEAST 2 SHEETS.
        assertTrue(lSheetSummaryDatas.size() == 2);
        Collection<String> lNames =
                new ArrayList<String>(lSheetSummaryDatas.size());
        for (SheetSummaryData lSheetSummaryData : lSheetSummaryDatas) {
            lNames.add(lSheetSummaryData.getSheetReference());
        }

        assertEqualsUnordered(EXPECTED_RESULT_PRODUCT_FIELDS_FOR_LEVEL,
                lNames.toArray(new String[0]));
    }

    /**
     * Tests a complete set of criteria with one container
     */
    public void testCompleteCriteriaWithOneContainer() {
        // Get the sheet type id
        CacheableSheetType lType =
                sheetService.getCacheableSheetTypeByName(adminRoleToken,
                        getProcessName(), SHEET_TYPES[1],
                        CacheProperties.IMMUTABLE);
        String lSheetTypeId = lType.getId();
        assertNotNull("Sheet type " + SHEET_TYPES[1] + " not found.",
                lSheetTypeId);
        String[] lIds = { lSheetTypeId };

        int lCurrentMaxFieldsDepth = searchService.getMaxFieldsDepth();
        searchService.setMaxFieldsDepth(1);
        Map<String, UsableFieldData> lUsableFieldDatas =
                serviceLocator.getSearchService().getUsableFields(
                        adminRoleToken, lIds, getProcessName());
        assertNotNull("No usable fields found for containers", lUsableFieldDatas);

        List<CriteriaData> lCriteria = createCriteriaDatas(lUsableFieldDatas);

        // create visibility constraint
        FilterVisibilityConstraintData lFilterVisibilityConstraint =
                new FilterVisibilityConstraintData(null, getProcessName(), null);

        // build other parts of filter data from
        // getUsableFieldsForOrderAndDisplay
        Collection<UsableFieldData> lResultFields =
                new ArrayList<UsableFieldData>();
        Collection<SortingFieldData> lSortingFields =
                new ArrayList<SortingFieldData>();

        for (UsableFieldData lUsableFieldDataForSummary : lUsableFieldDatas.values()) {
            List<FilterFieldsContainerInfo> lHierarchy =
                    lUsableFieldDataForSummary.getFieldsContainerHierarchy();
            VirtualFieldType lVirtualFieldType = null;
            if (lUsableFieldDataForSummary instanceof VirtualFieldData) {
                lVirtualFieldType =
                        ((VirtualFieldData) lUsableFieldDataForSummary).getVirtualFieldType();
            }

            if (VirtualFieldType.$PRODUCT_HIERARCHY.equals(lVirtualFieldType)
                    || (lHierarchy != null && !(lHierarchy.isEmpty()))) {
                //DO NOTHING
            }
            else if (FieldType.MULTIPLE_FIELD.equals(lUsableFieldDataForSummary.getFieldType())) {
                //DO NOTHING
            }
            else {
                lResultFields.add(lUsableFieldDataForSummary);
                lSortingFields.add(new SortingFieldData(null, Operators.ASC,
                        lUsableFieldDataForSummary));
            }
        }
        ResultSummaryData lResultSummaryData =
                new ResultSummaryData(null, "name_result", lIds,
                        lResultFields.toArray(new UsableFieldData[0]),
                        lFilterVisibilityConstraint);

        ResultSortingData lResultSortingData =
                new ResultSortingData(null, "name_sorter", lIds,
                        lSortingFields.toArray(new SortingFieldData[0]),
                        lFilterVisibilityConstraint);

        // Create Executable filter from CriteriaData, ResultSummaryData and
        // ResultSortingData

        ExecutableFilterData lExecutableFilterData =
                new ExecutableFilterData(
                        null,
                        "name",
                        "descr",
                        FilterUsage.BOTH_VIEWS.getValue(),
                        false,
                        lResultSummaryData,
                        lResultSortingData,
                        new FilterData(
                                null,
                                "filter_criteria",
                                false,
                                lIds,
                                FilterTypeData.SHEET,
                                new OperationData(Operators.AND,
                                        lCriteria.toArray(new CriteriaData[0])),
                                lFilterVisibilityConstraint),
                        lFilterVisibilityConstraint, null);

        searchService.executeFilter(
                adminRoleToken,
                lExecutableFilterData,
                new FilterVisibilityConstraintData(null, getProcessName(), null),
                new FilterQueryConfigurator(0, MAX_RESULT, FIRST_RESULT));

        searchService.setMaxFieldsDepth(lCurrentMaxFieldsDepth);
    }

    /**
     * Tests a complete set of criteria with one container only
     */
    public void testCompleteCriteriaWithMultipleContainers() {

        // Get the sheet type id
        CacheableSheetType lType =
                sheetService.getCacheableSheetTypeByName(adminRoleToken,
                        getProcessName(), SHEET_TYPES[0],
                        CacheProperties.IMMUTABLE);
        String lSheetTypeId = lType.getId();
        assertNotNull("Sheet type " + SHEET_TYPES[0] + " not found.",
                lSheetTypeId);

        // Get the sheet type id
        lType =
                sheetService.getCacheableSheetTypeByName(adminRoleToken,
                        getProcessName(), SHEET_TYPES[1],
                        CacheProperties.IMMUTABLE);
        String lSheetTypeId2 = lType.getId();
        assertNotNull("Sheet type " + SHEET_TYPES[1] + " not found.",
                lSheetTypeId);

        String[] lIds = { lSheetTypeId, lSheetTypeId2 };

        Map<String, UsableFieldData> lUsableFieldDatas =
                serviceLocator.getSearchService().getUsableFields(
                        adminRoleToken, lIds, getProcessName());

        assertNotNull("No usable fields found for containers", lUsableFieldDatas);

        List<CriteriaData> lCriteria = createCriteriaDatas(lUsableFieldDatas);

        // create visibility constraint
        FilterVisibilityConstraintData lFilterVisibilityConstraint =
                new FilterVisibilityConstraintData(null, getProcessName(), null);

        // build other parts of filter data from
        // getUsableFieldsForOrderAndDisplay

        Collection<UsableFieldData> lResultFields =
                new Vector<UsableFieldData>();
        Collection<SortingFieldData> lSortingFields =
                new Vector<SortingFieldData>();

        for (UsableFieldData lUsableFieldDataForSummary : lUsableFieldDatas.values()) {
            List<FilterFieldsContainerInfo> lHierarchy = null;
            VirtualFieldType lVirtualFieldType = null;
            if (lUsableFieldDataForSummary instanceof VirtualFieldData) {
                lVirtualFieldType =
                        ((VirtualFieldData) lUsableFieldDataForSummary).getVirtualFieldType();
                lHierarchy =
                        ((VirtualFieldData) lUsableFieldDataForSummary).getFieldsContainerHierarchy();
            }

            if (VirtualFieldType.$PRODUCT_HIERARCHY.equals(lVirtualFieldType)
                    || (lHierarchy != null && !(lHierarchy.isEmpty()))) {
                //DO NOTHING
            }
            else if (FieldType.MULTIPLE_FIELD.equals(lUsableFieldDataForSummary.getFieldType())) {
                //DO NOTHING
            }
            else {
                //                lResultFields.add(lUsableFieldDataForSummary);
                //                lSortingFields.add(new SortingFieldData(null, Operators.ASC,
                //                        lUsableFieldDataForSummary));
            }
        }
        ResultSummaryData lResultSummaryData =
                new ResultSummaryData(null, "name_result", lIds,
                        lResultFields.toArray(new UsableFieldData[0]),
                        lFilterVisibilityConstraint);

        ResultSortingData lResultSortingData =
                new ResultSortingData(null, "name_sorter", lIds,
                        lSortingFields.toArray(new SortingFieldData[0]),
                        lFilterVisibilityConstraint);

        // Create Executable filter from CriteriaData, ResultSummaryData and
        // ResultSortingData

        ExecutableFilterData lExecutableFilterData =
                new ExecutableFilterData(
                        null,
                        "name",
                        "descr",
                        FilterUsage.BOTH_VIEWS.getValue(),
                        false,
                        lResultSummaryData,
                        lResultSortingData,
                        new FilterData(
                                null,
                                "filter_criteria",
                                false,
                                lIds,
                                FilterTypeData.SHEET,
                                new OperationData(Operators.AND,
                                        lCriteria.toArray(new CriteriaData[0])),
                                lFilterVisibilityConstraint),
                        lFilterVisibilityConstraint, null);

        searchService.executeFilter(
                adminRoleToken,
                lExecutableFilterData,
                new FilterVisibilityConstraintData(null, getProcessName(), null),
                new FilterQueryConfigurator(0, MAX_RESULT, FIRST_RESULT));
    }

    private static final String FILTER_WITH_SHEET_STATE_LEVEL_NAME =
            GpmTestValues.FILTER_TYPE_VFD_SHEET_STATE_FILTER;

    private static final String[] EXPECTED_RESULT_FOR_SHEET_STATE_LEVEL =
            { "type_vfd_01_01", "type_vfd_01_02" };

    /**
     * Test filter execution (and creation via instantiation tool) with:
     * <ul>
     * <li>Criterion on several third level: $SHEET_STATE
     * </ul>
     */
    public void testFilterWithSheetStateLevel() {
        int lOldLevel = searchService.getMaxFieldsDepth();
        searchService.setMaxFieldsDepth(LEVEL_3);

        ExecutableFilterData lExecutableFilterData =
                searchService.getExecutableFilterByName(adminRoleToken,
                        getProcessName(), null, null,
                        FILTER_WITH_SHEET_STATE_LEVEL_NAME);
        assertNotNull("ExecutableFilterData is null", lExecutableFilterData);

        // main test
        Collection<SheetSummaryData> lSheetSummaryDatas =
                executeSheetFilter(adminRoleToken, getProcessName(), null,
                        null, lExecutableFilterData);

        // THE QUERY SHOULD ONLY RETURN 2 SHEETS
        // (The query returns more than 2 results, but only 2 are different).
        assertEquals(2, lSheetSummaryDatas.size());
        Collection<String> lNames =
                new ArrayList<String>(lSheetSummaryDatas.size());
        for (SheetSummaryData lSheetSummaryData : lSheetSummaryDatas) {
            lNames.add(lSheetSummaryData.getSheetReference());
        }

        assertEqualsUnordered(EXPECTED_RESULT_FOR_SHEET_STATE_LEVEL,
                lNames.toArray(new String[0]));
        searchService.setMaxFieldsDepth(lOldLevel);
    }

    private static final String FILTER_WITH_SHEET_TYPE_LEVEL_NAME =
            GpmTestValues.FILTER_TYPE_VFD_SHEET_TYPE_FILTER;

    private static final String[] EXPECTED_RESULT_FOR_SHEET_TYPE_LEVEL =
            { "type_vfd_02_01", "type_vfd_02_anotherProduct" };

    /**
     * Test filter execution (and creation via instantiation tool) with:
     * <ul>
     * <li>Criterion on several third level: $SHEET_TYPE
     * </ul>
     */
    public void testFilterWithSheetTypeLevel() {
        int lOldLevel = searchService.getMaxFieldsDepth();
        searchService.setMaxFieldsDepth(LEVEL_3);

        ExecutableFilterData lExecutableFilterData =
                searchService.getExecutableFilterByName(adminRoleToken,
                        getProcessName(), null, null,
                        FILTER_WITH_SHEET_TYPE_LEVEL_NAME);
        assertNotNull("ExecutableFilterData is null", lExecutableFilterData);

        // main test
        Collection<SheetSummaryData> lSheetSummaryDatas =
                executeSheetFilter(adminRoleToken, getProcessName(), null,
                        null, lExecutableFilterData);

        assertEquals(lSheetSummaryDatas.size(), 2);
        Collection<String> lNames =
                new ArrayList<String>(lSheetSummaryDatas.size());
        for (SheetSummaryData lSheetSummaryData : lSheetSummaryDatas) {
            lNames.add(lSheetSummaryData.getSheetReference());
        }

        assertEqualsUnordered(EXPECTED_RESULT_FOR_SHEET_TYPE_LEVEL,
                lNames.toArray(new String[0]));
        searchService.setMaxFieldsDepth(lOldLevel);
    }

    private static final String FILTER_WITH_SHEET_REFERENCE_LEVEL_NAME =
            GpmTestValues.FILTER_TYPE_VFD_SHEET_REFERENCE_FILTER;

    private static final String[] EXPECTED_RESULT_FOR_SHEET_REFERENCE_LEVEL =
            { "type_vfd_01_01", "type_vfd_01_02" };

    /**
     * Test filter execution (and creation via instantiation tool) with:
     * <ul>
     * <li>Criterion on several third level: $SHEET_REFERENCE
     * </ul>
     */
    public void testFilterWithSheetReferenceLevel() {
        int lOldLevel = searchService.getMaxFieldsDepth();
        searchService.setMaxFieldsDepth(LEVEL_3);
        ExecutableFilterData lExecutableFilterData =
                searchService.getExecutableFilterByName(adminRoleToken,
                        getProcessName(), null, null,
                        FILTER_WITH_SHEET_REFERENCE_LEVEL_NAME);
        assertNotNull("ExecutableFilterData is null", lExecutableFilterData);

        // main test
        Collection<SheetSummaryData> lSheetSummaryDatas =
                executeSheetFilter(adminRoleToken, getProcessName(), null,
                        null, lExecutableFilterData);

        assertEquals(lSheetSummaryDatas.size(), 2);
        Collection<String> lNames =
                new ArrayList<String>(lSheetSummaryDatas.size());
        for (SheetSummaryData lSheetSummaryData : lSheetSummaryDatas) {
            lNames.add(lSheetSummaryData.getSheetReference());
        }
        assertEqualsUnordered(EXPECTED_RESULT_FOR_SHEET_REFERENCE_LEVEL,
                lNames.toArray(new String[0]));
        searchService.setMaxFieldsDepth(lOldLevel);
    }
}
