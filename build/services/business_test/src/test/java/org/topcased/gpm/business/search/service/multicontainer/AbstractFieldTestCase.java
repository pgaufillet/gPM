/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.search.service.multicontainer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.scalar.ScalarValueData;
import org.topcased.gpm.business.search.criterias.CriteriaFieldData;
import org.topcased.gpm.business.search.criterias.FilterData;
import org.topcased.gpm.business.search.criterias.FilterTypeData;
import org.topcased.gpm.business.search.criterias.impl.Operators;
import org.topcased.gpm.business.search.impl.query.FilterQueryConfigurator;
import org.topcased.gpm.business.search.impl.query.FilterResultIterator;
import org.topcased.gpm.business.search.result.sorter.ResultSortingData;
import org.topcased.gpm.business.search.result.sorter.SortingFieldData;
import org.topcased.gpm.business.search.result.summary.ResultSummaryData;
import org.topcased.gpm.business.search.service.ExecutableFilterData;
import org.topcased.gpm.business.search.service.FilterVisibilityConstraintData;
import org.topcased.gpm.business.search.service.SearchService;
import org.topcased.gpm.business.search.service.UsableFieldData;
import org.topcased.gpm.business.sheet.service.SheetSummaryData;
import org.topcased.gpm.business.values.field.virtual.VirtualFieldType;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * Test helper for fields.
 * <p>
 * Base for testing fields for multi-container filters.<br/>
 * Test field as : (7 tests)
 * </p>
 * <ul>
 * <li>Criterion</li>
 * <li>Result</li>
 * <li>Sorted field</li>
 * <li>Criterion and result</li>
 * <li>Criterion and sorted field</li>
 * <li>Result and sorted field</li>
 * <li>Criterion, result and sorted field</li>
 * </ul>
 * <p>
 * There is always <code>$SHEET_TYPE</code> as result.
 * </p>
 * <h3>Instance test usage</h3> Use the sheet's types
 * <code>sameSheetType_01</code> and <code>sameSheetType_02</code>
 * 
 * @author mkargbo
 */
public abstract class AbstractFieldTestCase extends
        AbstractBusinessServiceTestCase {
    private static String staticSheetType01 = "sameSheetType_01";

    private static String staticSheetType02 = "sameSheetType_02";

    protected SearchService searchService;

    protected FilterVisibilityConstraintData filterVisibilityConstraint;

    protected FilterQueryConfigurator filterQueryConfigurator;

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.AbstractBusinessServiceTestCase#setUp()
     */
    protected void setUp() {
        super.setUp();
        searchService = serviceLocator.getSearchService();
        filterVisibilityConstraint =
                new FilterVisibilityConstraintData(StringUtils.EMPTY,
                        getProcessName(), StringUtils.EMPTY);
        filterQueryConfigurator = new FilterQueryConfigurator();
    }

    /**
     * Creates a multi-container filter with optionally criteria, results and
     * sorted fields.
     * 
     * @param pCriterion
     *            Criterion label key
     * @param pCriterionValue
     *            Criterion value
     * @param pCriterionOperator
     *            Criterion operator
     * @param pDisplayFields
     *            Results for fields label key
     * @param pSortedField
     *            Sorted fields label key
     * @param pOrder
     *            Order operator
     * @return Filter
     */
    protected ExecutableFilterData createFilter(final String pCriterion,
            final ScalarValueData pCriterionValue,
            final String pCriterionOperator, final List<String> pDisplayFields,
            final String pSortedField, final String pOrder) {

        String lSheetTypeId =
                sheetService.getCacheableSheetTypeByName(adminRoleToken,
                        staticSheetType01, CacheProperties.IMMUTABLE).getId();
        assertNotNull("Sheet type " + staticSheetType01 + " not found.",
                lSheetTypeId);
        String lSheetTypeId2 =
                sheetService.getCacheableSheetTypeByName(adminRoleToken,
                        staticSheetType02, CacheProperties.IMMUTABLE).getId();
        assertNotNull("Sheet type " + staticSheetType02 + " not found.",
                lSheetTypeId);

        Collection<String> lTypeId = new HashSet<String>();
        lTypeId.add(lSheetTypeId);
        lTypeId.add(lSheetTypeId2);

        ExecutableFilterData lExecutableFilter = new ExecutableFilterData();
        lExecutableFilter.setFilterVisibilityConstraintData(filterVisibilityConstraint);
        FilterData lFilter = new FilterData();
        lFilter.setType(FilterTypeData.SHEET);
        lFilter.setFieldsContainerIds(lTypeId.toArray(new String[2]));

        //Criteria
        if (StringUtils.isNotBlank(pCriterion)) {
            UsableFieldData lCriterionUsableField =
                    searchService.getUsableField(getProcessName(), pCriterion,
                            lTypeId);
            CriteriaFieldData lCriterion = new CriteriaFieldData();
            lCriterion.setOperator(pCriterionOperator);
            lCriterion.setUsableFieldData(lCriterionUsableField);
            lCriterion.setScalarValueData(pCriterionValue);
            lFilter.setCriteriaData(lCriterion);
        }

        //Result
        if (!pDisplayFields.isEmpty()) {
            final List<UsableFieldData> lResultFields =
                    new ArrayList<UsableFieldData>(pDisplayFields.size());
            for (String lField : pDisplayFields) {
                UsableFieldData lDisplayedUsableField =
                        searchService.getUsableField(getProcessName(), lField,
                                lTypeId);
                lResultFields.add(lDisplayedUsableField);
            }
            ResultSummaryData lResultSummary = new ResultSummaryData();
            lResultSummary.setFilterVisibilityConstraintData(filterVisibilityConstraint);
            lResultSummary.setUsableFieldDatas(lResultFields.toArray(new UsableFieldData[0]));
            lExecutableFilter.setResultSummaryData(lResultSummary);

        }

        //Sort
        if (StringUtils.isNotBlank(pSortedField)) {
            UsableFieldData lSortedUsableField =
                    searchService.getUsableField(getProcessName(),
                            pSortedField, lTypeId);
            SortingFieldData lSortingField = new SortingFieldData();
            lSortingField.setOrder(pOrder);
            lSortingField.setUsableFieldData(lSortedUsableField);
            ResultSortingData lResultSorting = new ResultSortingData();
            lResultSorting.setFilterVisibilityConstraintData(filterVisibilityConstraint);
            lResultSorting.setSortingFieldDatas(new SortingFieldData[] { lSortingField });
            lExecutableFilter.setResultSortingData(lResultSorting);
        }
        lExecutableFilter.setFilterData(lFilter);
        return lExecutableFilter;
    }

    /**
     * Test using field as criterion.
     */
    public void testAsCriterion() {
        List<String> lDisplayField = new ArrayList<String>(1);
        lDisplayField.add(VirtualFieldType.$SHEET_TYPE.getValue());
        ExecutableFilterData lFilter =
                createFilter(getCriterionLabelKey(), getCriterionValue(),
                        getCriterionOperator(), lDisplayField,
                        StringUtils.EMPTY, StringUtils.EMPTY);
        FilterResultIterator<SheetSummaryData> lResults =
                searchService.executeFilter(normalRoleToken, lFilter,
                        filterVisibilityConstraint, filterQueryConfigurator);
        int lCount = 0;
        while (lResults.hasNext()) {
            assertCriterion(lResults.next());
            lCount++;
        }
        assertEquals("Results number error", getCriterionCount(), lCount);
    }

    /**
     * Test using field as result.
     */
    public void testAsResult() {
        List<String> lDisplayField = new ArrayList<String>(2);
        lDisplayField.add(VirtualFieldType.$SHEET_TYPE.getValue());
        lDisplayField.add(getResultLabelKey());
        ExecutableFilterData lFilter =
                createFilter(StringUtils.EMPTY, null, StringUtils.EMPTY,
                        lDisplayField, StringUtils.EMPTY, StringUtils.EMPTY);
        FilterResultIterator<SheetSummaryData> lResults =
                searchService.executeFilter(normalRoleToken, lFilter,
                        filterVisibilityConstraint, filterQueryConfigurator);
        int lCount = 0;
        while (lResults.hasNext()) {
            assertResult(lResults.next());
            lCount++;
        }
        assertEquals("Results number error", getResultCount(), lCount);
    }

    /**
     * Test using field as sorted field.
     */
    public void testAsSort() {
        List<String> lDisplayField = new ArrayList<String>(1);
        lDisplayField.add(VirtualFieldType.$SHEET_TYPE.getValue());
        ExecutableFilterData lFilter =
                createFilter(StringUtils.EMPTY, null, StringUtils.EMPTY,
                        lDisplayField, getSortLabelKey(), getSortOrder());
        FilterResultIterator<SheetSummaryData> lResults =
                searchService.executeFilter(normalRoleToken, lFilter,
                        filterVisibilityConstraint, filterQueryConfigurator);
        int lCount = 0;
        while (lResults.hasNext()) {
            assertSort(lResults.next(), lCount);
            lCount++;
        }
        assertEquals("Results number error", getSortCount(), lCount);
    }

    /**
     * Test using field as criterion and result.
     */
    public void testAsCriterionResult() {
        List<String> lDisplayField = new ArrayList<String>(1);
        lDisplayField.add(VirtualFieldType.$SHEET_TYPE.getValue());
        lDisplayField.add(getResultLabelKey());
        ExecutableFilterData lFilter =
                createFilter(getCriterionLabelKey(), getCriterionValue(),
                        getCriterionOperator(), lDisplayField,
                        StringUtils.EMPTY, StringUtils.EMPTY);
        FilterResultIterator<SheetSummaryData> lResults =
                searchService.executeFilter(normalRoleToken, lFilter,
                        filterVisibilityConstraint, filterQueryConfigurator);
        int lCount = 0;
        while (lResults.hasNext()) {
            assertCriterionResult(lResults.next());
            lCount++;
        }
        assertEquals("Results number error", getCriterionCount(), lCount);
    }

    /**
     * Test using field as criterion and sorted field.
     */
    public void testAsCriterionSort() {
        List<String> lDisplayField = new ArrayList<String>(1);
        lDisplayField.add(VirtualFieldType.$SHEET_TYPE.getValue());
        ExecutableFilterData lFilter =
                createFilter(getCriterionLabelKey(), getCriterionValue(),
                        getCriterionOperator(), lDisplayField,
                        getSortLabelKey(), getSortOrder());
        FilterResultIterator<SheetSummaryData> lResults =
                searchService.executeFilter(normalRoleToken, lFilter,
                        filterVisibilityConstraint, filterQueryConfigurator);
        int lCount = 0;
        while (lResults.hasNext()) {
            assertCriterionSort(lResults.next());
            lCount++;
        }
        assertEquals("Results number error", getCriterionCount(), lCount);
    }

    /**
     * Test using field as result and sorted field.
     */
    public void testAsResultSort() {
        List<String> lDisplayField = new ArrayList<String>(1);
        lDisplayField.add(VirtualFieldType.$SHEET_TYPE.getValue());
        lDisplayField.add(getResultLabelKey());
        ExecutableFilterData lFilter =
                createFilter(StringUtils.EMPTY, null, StringUtils.EMPTY,
                        lDisplayField, getSortLabelKey(), getSortOrder());
        FilterResultIterator<SheetSummaryData> lResults =
                searchService.executeFilter(normalRoleToken, lFilter,
                        filterVisibilityConstraint, filterQueryConfigurator);
        int lCount = 0;
        while (lResults.hasNext()) {
            assertResultSort(lResults.next(), lCount);
            lCount++;
        }
        assertEquals("Results number error", getResultCount(), lCount);
    }

    /**
     * Test using field as criterion, result and sorted field.
     */
    public void testAsCriterionResultSort() {
        List<String> lDisplayField = new ArrayList<String>(1);
        lDisplayField.add(VirtualFieldType.$SHEET_TYPE.getValue());
        lDisplayField.add(getResultLabelKey());
        ExecutableFilterData lFilter =
                createFilter(getCriterionLabelKey(), getCriterionValue(),
                        getCriterionOperator(), lDisplayField,
                        getSortLabelKey(), getSortOrder());
        FilterResultIterator<SheetSummaryData> lResults =
                searchService.executeFilter(normalRoleToken, lFilter,
                        filterVisibilityConstraint, filterQueryConfigurator);
        int lCount = 0;
        while (lResults.hasNext()) {
            assertCriterionResultSort(lResults.next());
            lCount++;
        }
        assertEquals("Results number error", getCriterionCount(), lCount);
    }

    /**
     * Get the field label key for the criterion.
     * 
     * @return Criterion label key
     */
    protected String getCriterionLabelKey() {
        return getFieldLabelKey();
    }

    protected String getCriterionOperator() {
        return Operators.EQ;
    }

    /**
     * Get the criterion value.
     * 
     * @return Criterion value.
     */
    protected abstract ScalarValueData getCriterionValue();

    /**
     * Get the field label key for result
     * 
     * @return Result label key
     */
    protected String getResultLabelKey() {
        return getFieldLabelKey();
    }

    /**
     * Get the field label key for the sorted field
     * 
     * @return Sorted field's label key
     */
    protected String getSortLabelKey() {
        return getFieldLabelKey();
    }

    /**
     * Get the sorted field order
     * 
     * @return Sorted field's order
     */
    protected abstract String getSortOrder();

    /**
     * Get the results count for criterion test
     * 
     * @return Result count
     */
    protected abstract int getCriterionCount();

    /**
     * Get the results count for result test
     * 
     * @return Result count
     */
    protected abstract int getResultCount();

    /**
     * Get the results count for sort test
     * 
     * @return Result count
     */
    protected abstract int getSortCount();

    /**
     * Label key of the field to test
     * 
     * @return Label key of the field.
     */
    protected abstract String getFieldLabelKey();

    /**
     * Expected results for criterion test.
     * 
     * @return The unique result of criterion test.
     */
    protected abstract String getExpectedCriterionResult();

    /**
     * Expected ordered results for sort test.
     * 
     * @return Ordered results.
     */
    protected abstract String[] getExpectedResultOrder();

    /**
     * Assertion for field as result test.
     * 
     * @param pResult
     *            Result of the filter
     * @param pObject
     *            Other arguments
     */
    protected void assertResult(final SheetSummaryData pResult,
            final Object... pObject) {
        assertTrue(StringUtils.isNotBlank(pResult.getFieldSummaryDatas()[1].getValue()));
    }

    /**
     * Assertion for field as sort test.
     * 
     * @param pResult
     *            Result of the filter
     * @param pObject
     *            Other arguments
     */
    protected void assertSort(final SheetSummaryData pResult,
            final Object... pObject) {
        int lCount = (Integer) pObject[0];
        assertEquals(getExpectedResultOrder()[lCount],
                pResult.getFieldSummaryDatas()[0].getValue());
    }

    /**
     * Assertion for field as criterion test.
     * 
     * @param pResult
     *            Result of the filter
     * @param pObject
     *            Other arguments
     */
    protected void assertCriterion(final SheetSummaryData pResult,
            final Object... pObject) {
        assertEquals(getExpectedCriterionResult(),
                pResult.getFieldSummaryDatas()[0].getValue());
    }

    /**
     * Assertion for field as criterion and result test.
     * 
     * @param pResult
     *            Result of the filter
     * @param pObject
     *            Other arguments
     */
    protected void assertCriterionResult(final SheetSummaryData pResult,
            final Object... pObject) {
        assertTrue(StringUtils.isNotBlank(pResult.getFieldSummaryDatas()[1].getValue()));
    }

    /**
     * Assertion for field as criterion and sort test.
     * 
     * @param pResult
     *            Result of the filter
     * @param pObject
     *            Other arguments
     */
    protected void assertCriterionSort(final SheetSummaryData pResult,
            final Object... pObject) {
        assertEquals(getExpectedCriterionResult(),
                pResult.getFieldSummaryDatas()[0].getValue());
    }

    /**
     * Assertion for field as result and sort test.
     * 
     * @param pResult
     *            Result of the filter
     * @param pObject
     *            Other arguments
     */
    protected void assertResultSort(final SheetSummaryData pResult,
            final Object... pObject) {
        int lCount = (Integer) pObject[0];
        assertEquals(getExpectedResultOrder()[lCount],
                pResult.getFieldSummaryDatas()[0].getValue());
        assertTrue(StringUtils.isNotBlank(pResult.getFieldSummaryDatas()[1].getValue()));
    }

    /**
     * Assertion for field as criterion, result and sort test.
     * 
     * @param pResult
     *            Result of the filter
     * @param pObject
     *            Other arguments
     */
    protected void assertCriterionResultSort(final SheetSummaryData pResult,
            final Object... pObject) {
        assertEquals(getExpectedCriterionResult(),
                pResult.getFieldSummaryDatas()[0].getValue());
        assertTrue(StringUtils.isNotBlank(pResult.getFieldSummaryDatas()[1].getValue()));
    }

    public static void setSheetType01(String pSheetType01) {
        staticSheetType01 = pSheetType01;
    }

    public static void setSheetType02(String pSheetType02) {
        staticSheetType02 = pSheetType02;
    }
}