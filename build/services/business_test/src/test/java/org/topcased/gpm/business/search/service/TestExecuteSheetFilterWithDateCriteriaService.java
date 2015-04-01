/***************************************************************
 * Copyright (c) 2011 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien LOUISY (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.search.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.collections.IteratorUtils;
import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.fields.FieldSummaryData;
import org.topcased.gpm.business.scalar.DateValueData;
import org.topcased.gpm.business.search.criterias.CriteriaData;
import org.topcased.gpm.business.search.criterias.CriteriaFieldData;
import org.topcased.gpm.business.search.criterias.FilterData;
import org.topcased.gpm.business.search.criterias.FilterTypeData;
import org.topcased.gpm.business.search.criterias.OperationData;
import org.topcased.gpm.business.search.criterias.impl.Operators;
import org.topcased.gpm.business.search.impl.query.FilterQueryConfigurator;
import org.topcased.gpm.business.search.result.summary.ResultSummaryData;
import org.topcased.gpm.business.sheet.service.SheetSummaryData;
import org.topcased.gpm.domain.search.FilterUsage;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * Tests the method <CODE>executeSheetFilter<CODE> of the Search Service.
 * 
 * @author jlouisy
 */
public class TestExecuteSheetFilterWithDateCriteriaService extends
        AbstractBusinessServiceTestCase {

    /** The Search Service. */
    protected SearchService searchService;

    protected static final int SIMPLE = 0;

    protected static final int MULTIVALUED = 1;

    /** the filter name */
    protected static final String FILTER_NAME = "my_filter";

    /** the criteria suffix */
    protected static final String CRITERIA_SUFFIX = "_CRIT";

    /** the sorting suffix */
    protected static final String SORTING_SUFFIX = "_SORTER";

    /** the summary suffix */
    protected static final String SUMMARY_SUFFIX = "_SUMMARY";

    /** the sheet types to filter */
    protected static final String SHEET_TYPE =
            GpmTestValues.SHEET_TYPE_TEST_DATE_DISPLAYHINT;

    /** the user login for filter constraint : all users */
    protected static final String ALL_USERS = null;

    /** the product name for filter constraint : all products */
    protected static final String ALL_PRODUCTS = null;

    protected static final String[] FIELDS_SIMPLE = { "NAME", "D1" };

    protected static final String[] FIELDS_MULTIVALUED = { "NAME", "D2" };

    protected static final String[] FIELDS_MULTIPLE = { "NAME", "DM1" };

    protected static final String[] FIELDS_MULTIPLE_MULTIVALUED =
            { "NAME", "DM2" };

    protected static final String[] ALL_FIELDS =
            { "NAME", "D1", "D2", "DM1", "DM2" };

    /** sheet references that should be in result */
    protected static final String[][] RESULTS_EQ =
            { { "REF_Sheet2" }, { "REF_Sheet2", "REF_Sheet2" } };

    protected static final String[][] RESULTS_NEQ =
            { { "REF_Sheet1", "REF_Sheet3", "REF_Sheet4" },
             { "REF_Sheet1", "REF_Sheet3", "REF_Sheet4" } };

    protected static final String[][] RESULTS_LT =
            { { "REF_Sheet1" }, { "REF_Sheet1" } };

    protected static final String[][] RESULTS_LE =
            { { "REF_Sheet1", "REF_Sheet2" },
             { "REF_Sheet1", "REF_Sheet2", "REF_Sheet2" } };

    protected static final String[][] RESULTS_GT =
            { { "REF_Sheet3" }, { "REF_Sheet3" } };

    protected static final String[][] RESULTS_GE =
            { { "REF_Sheet2", "REF_Sheet3" },
             { "REF_Sheet2", "REF_Sheet2", "REF_Sheet3" } };

    protected static final String[][] RESULTS_ALL_FIELDS =
            {
             { "REF_Sheet2", "REF_Sheet2", "REF_Sheet2", "REF_Sheet2" },
             { "REF_Sheet1", "REF_Sheet2", "REF_Sheet2", "REF_Sheet2",
              "REF_Sheet2", "REF_Sheet3" } };

    private final SimpleDateFormat dateFormatter =
            new SimpleDateFormat("yyyy-MM-dd");

    /** the value for the first criterion */
    protected static final String VALUE_FOR_CRITERIA = "2011-01-02";

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
     * Tests EQUAL.
     * 
     * @throws ParseException
     */
    public void testDateCriteriaEQ() throws ParseException {

        checkResults(RESULTS_EQ[SIMPLE], runTest(Operators.EQ, FIELDS_SIMPLE));
        checkResults(RESULTS_EQ[MULTIVALUED], runTest(Operators.EQ,
                FIELDS_MULTIVALUED));
        checkResults(RESULTS_EQ[SIMPLE], runTest(Operators.EQ, FIELDS_MULTIPLE));
        checkResults(RESULTS_EQ[MULTIVALUED], runTest(Operators.EQ,
                FIELDS_MULTIPLE_MULTIVALUED));
    }

    /**
     * Tests NOT EQUAL.
     * 
     * @throws ParseException
     */
    public void testDateCriteriaNEQ() throws ParseException {

        checkResults(RESULTS_NEQ[SIMPLE], runTest(Operators.NEQ, FIELDS_SIMPLE));
        checkResults(RESULTS_NEQ[MULTIVALUED], runTest(Operators.NEQ,
                FIELDS_MULTIVALUED));
        checkResults(RESULTS_NEQ[SIMPLE], runTest(Operators.NEQ,
                FIELDS_MULTIPLE));
        checkResults(RESULTS_NEQ[MULTIVALUED], runTest(Operators.NEQ,
                FIELDS_MULTIPLE_MULTIVALUED));
    }

    /**
     * Tests LESSER THAN.
     * 
     * @throws ParseException
     */
    public void testDateCriteriaLT() throws ParseException {

        checkResults(RESULTS_LT[SIMPLE], runTest(Operators.LT, FIELDS_SIMPLE));
        checkResults(RESULTS_LT[MULTIVALUED], runTest(Operators.LT,
                FIELDS_MULTIVALUED));
        checkResults(RESULTS_LT[SIMPLE], runTest(Operators.LT, FIELDS_MULTIPLE));
        checkResults(RESULTS_LT[MULTIVALUED], runTest(Operators.LT,
                FIELDS_MULTIPLE_MULTIVALUED));
    }

    /**
     * Tests LESSER OR EQUAL.
     * 
     * @throws ParseException
     */
    public void testDateCriteriaLE() throws ParseException {

        checkResults(RESULTS_LE[SIMPLE], runTest(Operators.LE, FIELDS_SIMPLE));
        checkResults(RESULTS_LE[MULTIVALUED], runTest(Operators.LE,
                FIELDS_MULTIVALUED));
        checkResults(RESULTS_LE[SIMPLE], runTest(Operators.LE, FIELDS_MULTIPLE));
        checkResults(RESULTS_LE[MULTIVALUED], runTest(Operators.LE,
                FIELDS_MULTIPLE_MULTIVALUED));
    }

    /**
     * Tests GREATER THAN.
     * 
     * @throws ParseException
     */
    public void testDateCriteriaGT() throws ParseException {

        checkResults(RESULTS_GT[SIMPLE], runTest(Operators.GT, FIELDS_SIMPLE));
        checkResults(RESULTS_GT[MULTIVALUED], runTest(Operators.GT,
                FIELDS_MULTIVALUED));
        checkResults(RESULTS_GT[SIMPLE], runTest(Operators.GT, FIELDS_MULTIPLE));
        checkResults(RESULTS_GT[MULTIVALUED], runTest(Operators.GT,
                FIELDS_MULTIPLE_MULTIVALUED));
    }

    /**
     * Tests GREATER OR EQUAL.
     * 
     * @throws ParseException
     */
    public void testDateCriteriaGE() throws ParseException {

        checkResults(RESULTS_GE[SIMPLE], runTest(Operators.GE, FIELDS_SIMPLE));
        checkResults(RESULTS_GE[MULTIVALUED], runTest(Operators.GE,
                FIELDS_MULTIVALUED));
        checkResults(RESULTS_GE[SIMPLE], runTest(Operators.GE, FIELDS_MULTIPLE));
        checkResults(RESULTS_GE[MULTIVALUED], runTest(Operators.GE,
                FIELDS_MULTIPLE_MULTIVALUED));
    }

    /**
     * Tests ALL_FIELDS.
     * 
     * @throws ParseException
     */
    public void testDateCriteriaAllFields() throws ParseException {

        checkResults(RESULTS_ALL_FIELDS[SIMPLE], runTest(Operators.AND,
                ALL_FIELDS));
        checkResults(RESULTS_ALL_FIELDS[MULTIVALUED], runTest(Operators.OR,
                ALL_FIELDS));
    }

    @SuppressWarnings("unchecked")
	private Collection<String> runTest(String pOperator, String[] pFields)
        throws ParseException {

        // Get the sheet type id
        String lSheetTypeId =
                serviceLocator.getSheetService().getCacheableSheetTypeByName(
                        adminRoleToken, SHEET_TYPE, CacheProperties.IMMUTABLE).getId();
        assertNotNull("Sheet type " + SHEET_TYPE + " not found.", lSheetTypeId);

        // Create the Executable Filter Data to save
        ExecutableFilterData lExecutableFilterData;
        if (Operators.AND.equals(pOperator) || Operators.OR.equals(pOperator)) {
            lExecutableFilterData =
                    createMultipleCriteriaExecutableFilterData(lSheetTypeId,
                            pOperator, pFields);
        }
        else {
            lExecutableFilterData =
                    createExecutableFilterData(lSheetTypeId, pOperator, pFields);
        }

        assertNotNull("ExecutableFilterData is null", lExecutableFilterData);

        // main test
        startTimer();
        List<SheetSummaryData> lSheetSummaryDatas =
                IteratorUtils.toList(searchService.executeFilter(
                        adminRoleToken,
                        lExecutableFilterData,
                        lExecutableFilterData.getFilterVisibilityConstraintData(),
                        new FilterQueryConfigurator()));
        stopTimer();

        Collection<String> lNames = new Vector<String>();
        for (SheetSummaryData lSheetSummaryData : lSheetSummaryDatas) {
            for (FieldSummaryData lFieldSummaryData : lSheetSummaryData.getFieldSummaryDatas()) {
                if (pFields[0].equals(lFieldSummaryData.getLabelKey())) {
                    lNames.add(lFieldSummaryData.getValue());
                }
            }
        }

        return lNames;
    }

    private void checkResults(String[] pExpected, Collection<String> pActual) {
        assertEquals(pExpected.length, pActual.size());
        for (String lExpectedSheet : pExpected) {
            assertTrue(pActual.contains(lExpectedSheet));
        }
    }

    /**
     * Create the executable filter data
     * 
     * @param pSheetTypeId
     *            The sheet type id
     * @param pOperator
     * @return The created executable filter data
     * @throws ParseException
     */
    protected ExecutableFilterData createExecutableFilterData(
            String pSheetTypeId, String pOperator, String[] pFields)
        throws ParseException {

        String[] lFieldsContainerIds = { pSheetTypeId };

        FilterVisibilityConstraintData lFilterVisibilityConstraintData =
                new FilterVisibilityConstraintData(ALL_USERS, getProcessName(),
                        ALL_PRODUCTS);

        Map<String, UsableFieldData> lUsableFieldsMap =
                searchService.getUsableFields(adminRoleToken,
                        lFieldsContainerIds, getProcessName());

        // Create the usable field data
        UsableFieldData lUsableDate = lUsableFieldsMap.get(pFields[1]);

        // Create the criteria
        CriteriaData lCriteria =
                new CriteriaFieldData(pOperator, Boolean.FALSE,
                        new DateValueData(
                                dateFormatter.parse(VALUE_FOR_CRITERIA)),
                        lUsableDate);

        // Create the filter data from the criteria data, the fieldsContainer,
        // the filter name and the visibility constraints
        FilterData lFilterData =
                new FilterData(null, FILTER_NAME + CRITERIA_SUFFIX, false,
                        lFieldsContainerIds, FilterTypeData.SHEET, lCriteria,
                        lFilterVisibilityConstraintData);

        // creation of the filter result field data
        final int lSize = pFields.length;
        UsableFieldData[] lUsableFieldDatas = new UsableFieldData[lSize];
        for (int i = 0; i < pFields.length; i++) {
            lUsableFieldDatas[i] = lUsableFieldsMap.get(pFields[i]);
        }

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
                        lResultSummaryData, null, lFilterData,
                        lFilterVisibilityConstraintData, null);

        return lExecutableFilterData;
    }

    /**
     * Create the executable filter data
     * 
     * @param pSheetTypeId
     *            The sheet type id
     * @param pOperator
     * @return The created executable filter data
     * @throws ParseException
     */
    protected ExecutableFilterData createMultipleCriteriaExecutableFilterData(
            String pSheetTypeId, String pOperator, String[] pFields)
        throws ParseException {

        String[] lFieldsContainerIds = { pSheetTypeId };

        FilterVisibilityConstraintData lFilterVisibilityConstraintData =
                new FilterVisibilityConstraintData(ALL_USERS, getProcessName(),
                        ALL_PRODUCTS);

        Map<String, UsableFieldData> lUsableFieldsMap =
                searchService.getUsableFields(adminRoleToken,
                        lFieldsContainerIds, getProcessName());

        CriteriaData[] lCrits = new CriteriaData[4];
        lCrits[0] =
                new CriteriaFieldData(Operators.EQ, Boolean.FALSE,
                        new DateValueData(
                                dateFormatter.parse(VALUE_FOR_CRITERIA)),
                        lUsableFieldsMap.get(pFields[1]));
        lCrits[1] =
                new CriteriaFieldData(Operators.EQ, Boolean.FALSE,
                        new DateValueData(
                                dateFormatter.parse(VALUE_FOR_CRITERIA)),
                        lUsableFieldsMap.get(pFields[2]));
        lCrits[2] =
                new CriteriaFieldData(Operators.LE, Boolean.FALSE,
                        new DateValueData(
                                dateFormatter.parse(VALUE_FOR_CRITERIA)),
                        lUsableFieldsMap.get(pFields[3]));
        lCrits[3] =
                new CriteriaFieldData(Operators.GE, Boolean.FALSE,
                        new DateValueData(
                                dateFormatter.parse(VALUE_FOR_CRITERIA)),
                        lUsableFieldsMap.get(pFields[4]));

        // Create the Operation data (AND between the two criteria)
        OperationData lCriteria = new OperationData(pOperator, lCrits);

        // Create the filter data from the criteria data, the fieldsContainer,
        // the filter name and the visibility constraints
        FilterData lFilterData =
                new FilterData(null, FILTER_NAME + CRITERIA_SUFFIX, false,
                        lFieldsContainerIds, FilterTypeData.SHEET, lCriteria,
                        lFilterVisibilityConstraintData);

        // creation of the filter result field data
        final int lSize = pFields.length;
        UsableFieldData[] lUsableFieldDatas = new UsableFieldData[lSize];
        for (int i = 0; i < pFields.length; i++) {
            lUsableFieldDatas[i] = lUsableFieldsMap.get(pFields[i]);
        }

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
                        lResultSummaryData, null, lFilterData,
                        lFilterVisibilityConstraintData, null);

        return lExecutableFilterData;
    }
}
