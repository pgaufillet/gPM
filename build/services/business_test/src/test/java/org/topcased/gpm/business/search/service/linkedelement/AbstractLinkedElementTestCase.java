/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas Veillet (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.search.service.linkedelement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.fields.FieldSummaryData;
import org.topcased.gpm.business.fields.FieldsContainerType;
import org.topcased.gpm.business.link.impl.LinkDirection;
import org.topcased.gpm.business.scalar.ScalarValueData;
import org.topcased.gpm.business.search.criterias.CriteriaData;
import org.topcased.gpm.business.search.criterias.CriteriaFieldData;
import org.topcased.gpm.business.search.criterias.FilterData;
import org.topcased.gpm.business.search.criterias.FilterTypeData;
import org.topcased.gpm.business.search.criterias.OperationData;
import org.topcased.gpm.business.search.criterias.impl.FilterFieldsContainerInfo;
import org.topcased.gpm.business.search.criterias.impl.Operators;
import org.topcased.gpm.business.search.impl.query.FilterQueryConfigurator;
import org.topcased.gpm.business.search.impl.query.FilterResultIterator;
import org.topcased.gpm.business.search.result.sorter.ResultSortingData;
import org.topcased.gpm.business.search.result.sorter.SortingFieldData;
import org.topcased.gpm.business.search.result.summary.ResultSummaryData;
import org.topcased.gpm.business.search.service.ExecutableFilterData;
import org.topcased.gpm.business.search.service.FilterProductScope;
import org.topcased.gpm.business.search.service.FilterVisibilityConstraintData;
import org.topcased.gpm.business.search.service.SearchService;
import org.topcased.gpm.business.search.service.UsableFieldData;
import org.topcased.gpm.business.sheet.service.SheetSummaryData;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * Test helper for fields.
 * <p>
 * Base for testing fields on linked elements.<br/>
 * Test field as : (4 tests)
 * </p>
 * <ul>
 * <li>Criterion</li>
 * <li>Result</li>
 * <li>Sorted field</li>
 * <li>Criterion, result and sorted field</li>
 * </ul>
 * <h3>Instance test usage</h3> Use the sheet's types <code>Cat</code> and
 * <code>Price</code>. <br/>
 * Use the link's type <code>CAT_PRICE</code>.
 * 
 * @author nveillet
 */
public abstract class AbstractLinkedElementTestCase extends
        AbstractBusinessServiceTestCase {

    private static final String SEPARATOR = "|";

    private static String staticLinkType =
            GpmTestValues.SHEETLINK_SHEETTYPE1_CAT;

    private static String staticSheetType01 = GpmTestValues.SHEET_TYPE_CAT;

    private static String staticSheetType02 =
            GpmTestValues.SHEET_TYPE_SHEETTYPE1;

    protected FilterQueryConfigurator filterQueryConfigurator;

    protected FilterVisibilityConstraintData filterVisibilityConstraint;

    protected SearchService searchService;

    /**
     * Assertion for field as criterion test.
     * 
     * @param pResult
     *            Result of the filter
     * @param pResultNumber
     *            the result number
     */
    protected void assertCriterion(final SheetSummaryData pResult,
            final int pResultNumber) {
        for (int i = 0; i < pResult.getFieldSummaryDatas().length; i++) {
            assertEquals(getExpectedCriterionResult(pResultNumber).get(i),
                    pResult.getFieldSummaryDatas()[i].getValue());
        }
    }

    /**
     * Assertion for field as criterion, result and sort test.
     * 
     * @param pResult
     *            Result of the filter
     * @param pResultNumber
     *            the result number
     */
    protected void assertCriterionDisplaySort(final SheetSummaryData pResult,
            final int pResultNumber) {
        for (int i = 0; i < pResult.getFieldSummaryDatas().length; i++) {
            assertEquals(
                    getExpectedCriterionDisplaySortResult(pResultNumber).get(i),
                    pResult.getFieldSummaryDatas()[i].getValue());
        }
    }

    /**
     * Assertion for field as result and sort test.
     * 
     * @param pResult
     *            Result of the filter
     * @param pResultNumber
     *            the result number
     */
    protected void assertSort(final SheetSummaryData pResult,
            final int pResultNumber) {
        for (int i = 0; i < pResult.getFieldSummaryDatas().length; i++) {
            assertEquals(getExpectedSortResult(pResultNumber).get(i),
                    pResult.getFieldSummaryDatas()[i].getValue());
        }
    }

    /**
     * Creates a multi-container filter with optionally criteria, results and
     * sorted fields.
     * 
     * @param pCriteria
     *            Criteria labels key
     * @param pCriteriaValue
     *            Criteria values
     * @param pCriteriaOperator
     *            Criteria operators
     * @param pDisplayFields
     *            Results for fields label key
     * @param pSortedFields
     *            Sorted fields label key
     * @param pOrder
     *            Order operator
     * @return Filter
     */
    protected ExecutableFilterData createFilter(final List<String> pCriteria,
            final List<ScalarValueData> pCriteriaValue,
            final List<String> pCriteriaOperator,
            final List<String> pDisplayFields,
            final List<String> pSortedFields, final List<String> pOrder) {

        Map<String, String> lTypes = new HashMap<String, String>();

        String lTypeId =
                sheetService.getCacheableSheetTypeByName(adminRoleToken,
                        staticSheetType01, CacheProperties.IMMUTABLE).getId();
        assertNotNull("Sheet type " + staticSheetType01 + " not found.",
                lTypeId);
        lTypes.put(staticSheetType01, lTypeId);

        lTypeId =
                sheetService.getCacheableSheetTypeByName(adminRoleToken,
                        staticSheetType02, CacheProperties.IMMUTABLE).getId();
        assertNotNull("Sheet type " + staticSheetType02 + " not found.",
                lTypeId);
        lTypes.put(staticSheetType02, lTypeId);

        lTypeId =
                linkService.getLinkTypeByName(adminRoleToken, staticLinkType,
                        CacheProperties.IMMUTABLE).getId();
        assertNotNull("Link type " + staticLinkType + " not found.", lTypeId);
        lTypes.put(staticLinkType, lTypeId);

        Collection<String> lTypesId = new HashSet<String>();
        lTypesId.add(lTypes.get(staticSheetType01));

        ExecutableFilterData lExecutableFilter = new ExecutableFilterData();
        lExecutableFilter.setFilterVisibilityConstraintData(filterVisibilityConstraint);
        FilterData lFilter = new FilterData();
        lFilter.setType(FilterTypeData.SHEET);
        lFilter.setFieldsContainerIds(lTypesId.toArray(new String[1]));

        // Scope
        FilterProductScope[] lFilterProductScopes = new FilterProductScope[2];
        lFilterProductScopes[0] =
                new FilterProductScope(GpmTestValues.PRODUCT1_NAME, false);
        lFilterProductScopes[1] =
                new FilterProductScope(GpmTestValues.PRODUCT_PRODUCT2, false);
        lExecutableFilter.setFilterProductScopes(lFilterProductScopes);

        //Criteria
        if (pCriteria != null) {

            OperationData lOperationData =
                    new OperationData(Operators.AND,
                            new CriteriaData[pCriteria.size()]);

            for (int i = 0; i < pCriteria.size(); i++) {

                String[] lCriterionField =
                        pCriteria.get(i).split("\\" + SEPARATOR);

                UsableFieldData lCriterionUsableField =
                        searchService.getUsableField(
                                getProcessName(),
                                lCriterionField[lCriterionField.length - 1],
                                Arrays.asList(lTypes.get(lCriterionField[lCriterionField.length - 2])));
                setFieldContainerhierarchy(lCriterionUsableField,
                        lCriterionField, lTypes);

                CriteriaFieldData lCriterion = new CriteriaFieldData();
                lCriterion.setOperator(pCriteriaOperator.get(i));
                lCriterion.setUsableFieldData(lCriterionUsableField);
                lCriterion.setScalarValueData(pCriteriaValue.get(i));
                lCriterion.setCaseSensitive(false);
                lOperationData.getCriteriaDatas()[i] = lCriterion;
            }
            lFilter.setCriteriaData(lOperationData);
        }

        //Result
        if (!pDisplayFields.isEmpty()) {
            final List<UsableFieldData> lResultFields =
                    new ArrayList<UsableFieldData>(pDisplayFields.size());
            for (String lField : pDisplayFields) {

                String[] lDisplayField = lField.split("\\" + SEPARATOR);

                UsableFieldData lDisplayedUsableField =
                        searchService.getUsableField(
                                getProcessName(),
                                lDisplayField[lDisplayField.length - 1],
                                Arrays.asList(lTypes.get(lDisplayField[lDisplayField.length - 2])));
                setFieldContainerhierarchy(lDisplayedUsableField,
                        lDisplayField, lTypes);

                lResultFields.add(lDisplayedUsableField);
            }
            ResultSummaryData lResultSummary = new ResultSummaryData();
            lResultSummary.setFilterVisibilityConstraintData(filterVisibilityConstraint);
            lResultSummary.setUsableFieldDatas(lResultFields.toArray(new UsableFieldData[0]));
            lExecutableFilter.setResultSummaryData(lResultSummary);

        }

        //Sort
        if (pSortedFields != null) {

            ArrayList<SortingFieldData> lSortingFieldsData =
                    new ArrayList<SortingFieldData>();

            for (int i = 0; i < pSortedFields.size(); i++) {

                String[] lSortedField =
                        pSortedFields.get(i).split("\\" + SEPARATOR);

                UsableFieldData lSortedUsableField =
                        searchService.getUsableField(
                                getProcessName(),
                                lSortedField[lSortedField.length - 1],
                                Arrays.asList(lTypes.get(lSortedField[lSortedField.length - 2])));
                setFieldContainerhierarchy(lSortedUsableField, lSortedField,
                        lTypes);

                SortingFieldData lSortingField = new SortingFieldData();
                lSortingField.setOrder(pOrder.get(i));
                lSortingField.setUsableFieldData(lSortedUsableField);
                lSortingFieldsData.add(lSortingField);
            }
            ResultSortingData lResultSorting = new ResultSortingData();
            lResultSorting.setFilterVisibilityConstraintData(filterVisibilityConstraint);
            lResultSorting.setSortingFieldDatas(lSortingFieldsData.toArray(new SortingFieldData[lSortingFieldsData.size()]));
            lExecutableFilter.setResultSortingData(lResultSorting);
        }
        lExecutableFilter.setFilterData(lFilter);
        return lExecutableFilter;
    }

    /**
     * Get the list of field label key for the criteria.
     * 
     * @return Criteria labels key
     */
    private List<String> getCriteriaLabelKey() {
        List<String> lCriteria = new ArrayList<String>();
        lCriteria.add(staticSheetType01 + SEPARATOR + getFieldLabelKey());
        lCriteria.add(staticSheetType01 + SEPARATOR + staticLinkType
                + SEPARATOR + staticSheetType02 + SEPARATOR
                + getFieldLabelKey());
        return lCriteria;
    }

    /**
     * Get the list of criterion operator.
     * 
     * @return Criterion operator list.
     */
    private List<String> getCriteriaOperator() {
        List<String> lCriteriaOperator = new ArrayList<String>();
        lCriteriaOperator.add(getCriterionOperator());
        lCriteriaOperator.add(getCriterionOperator());
        return lCriteriaOperator;
    }

    /**
     * Get the list of criterion value.
     * 
     * @return Criterion value list.
     */
    private List<ScalarValueData> getCriteriaValue() {
        List<ScalarValueData> lCriteriaValue = new ArrayList<ScalarValueData>();
        lCriteriaValue.add(getCriterionValue());
        lCriteriaValue.add(getCriterionValue());
        return lCriteriaValue;
    }

    /**
     * Get the results count for criterion test
     * 
     * @return Result count
     */
    protected abstract int getCriterionCount();

    /**
     * Get the criterion operator.
     * 
     * @return Criterion operator.
     */
    protected abstract String getCriterionOperator();

    /**
     * Get the criterion value.
     * 
     * @return Criterion value.
     */
    protected abstract ScalarValueData getCriterionValue();

    /**
     * Expected results for criterion, display and sorting test.
     * 
     * @param pResultNumber
     *            the result number
     * @return The unique result of criterion, display and sorting test.
     */
    protected abstract List<String> getExpectedCriterionDisplaySortResult(
            int pResultNumber);

    /**
     * Expected results for criterion test.
     * 
     * @param pResultNumber
     *            the result number
     * @return The unique result of criterion test.
     */
    protected abstract List<String> getExpectedCriterionResult(int pResultNumber);

    /**
     * Expected results for display test. Order of the elements does not matter.
     * 
     * @return expected results.
     */
    protected abstract List<List<String>> getExpectedDisplayResults();

    /**
     * Expected ordered results for sort test.
     * 
     * @param pResultNumber
     *            the result number
     * @return Ordered results.
     */
    protected abstract List<String> getExpectedSortResult(int pResultNumber);

    /**
     * Label key of the field to test
     * 
     * @return Label key of the field.
     */
    protected abstract String getFieldLabelKey();

    /**
     * Get the results count for result test
     * 
     * @return Result count
     */
    protected abstract int getDisplayCount();

    /**
     * Get the list of field label key for result
     * 
     * @return Result label key list
     */
    private List<String> getResultsLabelKey() {
        List<String> lDisplayField = new ArrayList<String>(2);
        lDisplayField.add(staticSheetType01 + SEPARATOR + getFieldLabelKey());
        lDisplayField.add(staticSheetType01 + SEPARATOR + staticLinkType
                + SEPARATOR + staticSheetType02 + SEPARATOR
                + getFieldLabelKey());
        return lDisplayField;
    }

    /**
     * Get the list of field label key for the sorted fields
     * 
     * @return Sorted field's label key list
     */
    private List<String> getSortedFieldsLabelKey() {
        List<String> lSortedField = new ArrayList<String>();
        lSortedField.add(staticSheetType01 + SEPARATOR + getFieldLabelKey());
        lSortedField.add(staticSheetType01 + SEPARATOR + staticLinkType
                + SEPARATOR + staticSheetType02 + SEPARATOR
                + getFieldLabelKey());
        return lSortedField;
    }

    /**
     * Get the sorted field order
     * 
     * @return Sorted field's order
     */
    protected abstract String getSortOrder();

    /**
     * @return
     */
    private List<String> getSortOrders() {
        List<String> lOrder = new ArrayList<String>();
        lOrder.add(getSortOrder());
        lOrder.add(getSortOrder());
        return lOrder;
    }

    /**
     * @param pUsableFieldData
     * @param pCriterionField
     * @param pTypes
     */
    private void setFieldContainerhierarchy(UsableFieldData pUsableFieldData,
            String[] pCriterionField, Map<String, String> pTypes) {

        String lId = "";
        pUsableFieldData.setFieldsContainerHierarchy(new ArrayList<FilterFieldsContainerInfo>());

        for (int i = 1; i < pCriterionField.length - 1; i++) {
            FieldsContainerType lFieldContainerType = null;
            LinkDirection lLinkDirection = null;
            if (staticLinkType.equals(pCriterionField[i])) {
                lFieldContainerType = FieldsContainerType.LINK;
                lLinkDirection = LinkDirection.ORIGIN;
            }
            else if (staticSheetType02.equals(pCriterionField[i])) {
                lFieldContainerType = FieldsContainerType.SHEET;
                lLinkDirection = LinkDirection.UNDEFINED;
            }

            String lTypeId = pTypes.get(pCriterionField[i]);
            lId += lTypeId + SEPARATOR;

            FilterFieldsContainerInfo lFieldsContainerInfo =
                    new FilterFieldsContainerInfo(lTypeId, pCriterionField[i],
                            lFieldContainerType, lLinkDirection);
            pUsableFieldData.getFieldsContainerHierarchy().add(
                    lFieldsContainerInfo);
        }

        lId += pUsableFieldData.getFieldName();

        pUsableFieldData.setId(lId);
    }

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
     * Test using field as criterion.
     */
    public void testAsCriterion() {
        List<String> lDisplayField = getResultsLabelKey();

        ExecutableFilterData lFilter =
                createFilter(getCriteriaLabelKey(), getCriteriaValue(),
                        getCriteriaOperator(), lDisplayField, null, null);
        FilterResultIterator<SheetSummaryData> lResults =
                searchService.executeFilter(adminRoleToken, lFilter,
                        filterVisibilityConstraint, filterQueryConfigurator);
        int lCount = 0;
        while (lResults.hasNext()) {
            assertCriterion(lResults.next(), lCount);
            lCount++;
        }
        assertEquals("Results number error", getCriterionCount(), lCount);
    }

    /**
     * Test using field as criterion, result and sorted field.
     */
    public void testAsCriterionDisplaySort() {
        List<String> lDisplayField = getResultsLabelKey();

        ExecutableFilterData lFilter =
                createFilter(getCriteriaLabelKey(), getCriteriaValue(),
                        getCriteriaOperator(), lDisplayField,
                        getSortedFieldsLabelKey(), getSortOrders());
        FilterResultIterator<SheetSummaryData> lResults =
                searchService.executeFilter(adminRoleToken, lFilter,
                        filterVisibilityConstraint, filterQueryConfigurator);
        int lCount = 0;
        while (lResults.hasNext()) {
            assertCriterionDisplaySort(lResults.next(), lCount);
            lCount++;
        }
        assertEquals("Results number error", getCriterionCount(), lCount);
    }

    /**
     * Test using field as result.
     */
    public void testAsDisplay() {
        List<String> lDisplayField = getResultsLabelKey();

        ExecutableFilterData lFilter =
                createFilter(null, null, null, lDisplayField, null, null);
        FilterResultIterator<SheetSummaryData> lResults =
                searchService.executeFilter(adminRoleToken, lFilter,
                        filterVisibilityConstraint, filterQueryConfigurator);

        List<List<String>> lExpectedResults =
                new ArrayList<List<String>>(getExpectedDisplayResults());
        while (lResults.hasNext()) {
            FieldSummaryData[] lDatas = lResults.next().getFieldSummaryDatas();
            List<String> lResult = new ArrayList<String>();
            for (int i = 0; i < lDatas.length; i++) {
                lResult.add(lDatas[i].getValue());
            }
            assertTrue("Check that result is expected : " + lResult,
                    lExpectedResults.remove(lResult));
        }
        assertEquals("Check the number of results left to match", 0,
                lExpectedResults.size());
    }

    /**
     * Test using field as result and sorted field.
     */
    public void testAsSort() {
        List<String> lDisplayField = getResultsLabelKey();

        ExecutableFilterData lFilter =
                createFilter(null, null, null, lDisplayField,
                        getSortedFieldsLabelKey(), getSortOrders());
        FilterResultIterator<SheetSummaryData> lResults =
                searchService.executeFilter(adminRoleToken, lFilter,
                        filterVisibilityConstraint, filterQueryConfigurator);
        int lCount = 0;
        while (lResults.hasNext()) {
            assertSort(lResults.next(), lCount);
            lCount++;
        }
        assertEquals("Results number error", getDisplayCount(), lCount);
    }
}