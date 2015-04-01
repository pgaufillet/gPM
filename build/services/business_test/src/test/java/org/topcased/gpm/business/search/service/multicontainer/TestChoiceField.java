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
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.scalar.ScalarValueData;
import org.topcased.gpm.business.scalar.StringValueData;
import org.topcased.gpm.business.search.criterias.impl.Operators;
import org.topcased.gpm.business.search.impl.query.FilterResultIterator;
import org.topcased.gpm.business.search.service.ExecutableFilterData;
import org.topcased.gpm.business.sheet.service.SheetSummaryData;
import org.topcased.gpm.business.values.field.virtual.VirtualFieldType;

/**
 * Test <code>Choice</code> field.
 * <p>
 * Test choice with default order and defined order.
 * </p>
 * 
 * @author mkargbo
 */
public class TestChoiceField extends AbstractFieldTestCase {

    private static final String CRITERION_VALUE =
            GpmTestValues.CATEGORY_USAGE_VALUE_POLICE;

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.service.multicontainer.AbstractFieldTest#getCriterionCount()
     */
    @Override
    protected int getCriterionCount() {
        return 1;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.service.multicontainer.AbstractFieldTest#getCriterionValue()
     */
    @Override
    protected ScalarValueData getCriterionValue() {
        return new StringValueData(CRITERION_VALUE);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.service.multicontainer.AbstractFieldTest#getResultCount()
     */
    @Override
    protected int getResultCount() {
        return 2;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.service.multicontainer.AbstractFieldTest#getSortCount()
     */
    @Override
    protected int getSortCount() {
        return 2;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.service.multicontainer.AbstractFieldTest#getSortOrder()
     */
    @Override
    protected String getSortOrder() {
        return Operators.DESC;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.service.multicontainer.AbstractFieldTest#getExpectedCriterionResult()
     */
    @Override
    protected String getExpectedCriterionResult() {
        return "sameSheetType_02";
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.service.multicontainer.AbstractFieldTest#getExpectedResultOrder()
     */
    @Override
    protected String[] getExpectedResultOrder() {
        return new String[] { "sameSheetType_01", "sameSheetType_02" };
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.service.multicontainer.AbstractFieldTest#getFieldLabelKey()
     */
    @Override
    protected String getFieldLabelKey() {
        return "choiceField";
    }

    protected String[] getExpectedResultDefinedOrder() {
        return new String[] { "sameSheetType_02", "sameSheetType_01" };
    }

    private String getSortDefinedOrder() {
        return Operators.DEF_DESC;
    }

    /**
     * Test using field as criterion, result and sorted field with defined
     * order.
     */
    public void testAsCriterionResultDefinedSort() {
        List<String> lDisplayField = new ArrayList<String>(1);
        lDisplayField.add(VirtualFieldType.$SHEET_TYPE.getValue());
        lDisplayField.add(getResultLabelKey());
        ExecutableFilterData lFilter =
                createFilter(getCriterionLabelKey(), getCriterionValue(),
                        getCriterionOperator(), lDisplayField,
                        getSortLabelKey(), getSortDefinedOrder());
        FilterResultIterator<SheetSummaryData> lResults =
                searchService.executeFilter(normalRoleToken, lFilter,
                        filterVisibilityConstraint, filterQueryConfigurator);
        int lCount = 0;
        while (lResults.hasNext()) {
            SheetSummaryData lResult = lResults.next();
            assertEquals(getExpectedCriterionResult(),
                    lResult.getFieldSummaryDatas()[0].getValue());
            assertTrue(StringUtils.isNotBlank(lResult.getFieldSummaryDatas()[1].getValue()));
            lCount++;
        }
        assertEquals("Results number error", getCriterionCount(), lCount);
    }

    /**
     * Test using field as criterion and sorted field with defined order.
     */
    public void testAsCriterionDefinedSort() {
        List<String> lDisplayField = new ArrayList<String>(1);
        lDisplayField.add(VirtualFieldType.$SHEET_TYPE.getValue());
        ExecutableFilterData lFilter =
                createFilter(getCriterionLabelKey(), getCriterionValue(),
                        getCriterionOperator(), lDisplayField,
                        getSortLabelKey(), getSortDefinedOrder());
        FilterResultIterator<SheetSummaryData> lResults =
                searchService.executeFilter(normalRoleToken, lFilter,
                        filterVisibilityConstraint, filterQueryConfigurator);
        int lCount = 0;
        while (lResults.hasNext()) {
            SheetSummaryData lResult = lResults.next();
            assertEquals(getExpectedCriterionResult(),
                    lResult.getFieldSummaryDatas()[0].getValue());
            lCount++;
        }
        assertEquals("Results number error", getCriterionCount(), lCount);
    }

    /**
     * Test using field as result and sorted field. with defined order.
     */
    public void testAsResultDefinedSort() {
        List<String> lDisplayField = new ArrayList<String>(1);
        lDisplayField.add(VirtualFieldType.$SHEET_TYPE.getValue());
        lDisplayField.add(getResultLabelKey());
        ExecutableFilterData lFilter =
                createFilter(StringUtils.EMPTY, null, StringUtils.EMPTY,
                        lDisplayField, getSortLabelKey(), getSortDefinedOrder());
        FilterResultIterator<SheetSummaryData> lResults =
                searchService.executeFilter(normalRoleToken, lFilter,
                        filterVisibilityConstraint, filterQueryConfigurator);
        int lCount = 0;
        while (lResults.hasNext()) {
            SheetSummaryData lResult = lResults.next();
            assertEquals(getExpectedResultDefinedOrder()[lCount],
                    lResult.getFieldSummaryDatas()[0].getValue());
            assertTrue(StringUtils.isNotBlank(lResult.getFieldSummaryDatas()[1].getValue()));
            lCount++;
        }
        assertEquals("Results number error", getResultCount(), lCount);
    }

    /**
     * Test using field as sorted field with defined order.
     */
    public void testAsDefinedSort() {
        List<String> lDisplayField = new ArrayList<String>(1);
        lDisplayField.add(VirtualFieldType.$SHEET_TYPE.getValue());
        ExecutableFilterData lFilter =
                createFilter(StringUtils.EMPTY, null, StringUtils.EMPTY,
                        lDisplayField, getSortLabelKey(), getSortDefinedOrder());
        FilterResultIterator<SheetSummaryData> lResults =
                searchService.executeFilter(normalRoleToken, lFilter,
                        filterVisibilityConstraint, filterQueryConfigurator);
        int lCount = 0;
        while (lResults.hasNext()) {
            SheetSummaryData lResult = lResults.next();
            assertEquals(getExpectedResultDefinedOrder()[lCount],
                    lResult.getFieldSummaryDatas()[0].getValue());
            lCount++;
        }
        assertEquals("Results number error", getSortCount(), lCount);
    }

}