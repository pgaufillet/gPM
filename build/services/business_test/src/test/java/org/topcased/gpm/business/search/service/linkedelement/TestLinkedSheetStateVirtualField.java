/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.search.service.linkedelement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.topcased.gpm.business.scalar.ScalarValueData;
import org.topcased.gpm.business.scalar.StringValueData;
import org.topcased.gpm.business.search.criterias.impl.Operators;
import org.topcased.gpm.business.values.field.virtual.VirtualFieldType;

/**
 * Test <code>$SHEET_STATE</code> virtual field on linked fields.
 * 
 * @author nveillet
 */
public class TestLinkedSheetStateVirtualField extends
        AbstractLinkedElementTestCase {

    private final static List<List<String>> CRITERION_DISPLAY_SORT_RESULT =
            new ArrayList<List<String>>();

    private final static List<List<String>> CRITERION_RESULT =
            new ArrayList<List<String>>();

    private final static List<List<String>> DISPLAY_RESULT =
            new ArrayList<List<String>>();

    private final static List<List<String>> SORT_RESULT =
            new ArrayList<List<String>>();

    static {
        DISPLAY_RESULT.add(Arrays.asList("Deleted", null));
        DISPLAY_RESULT.add(Arrays.asList("Open", "Start"));

        CRITERION_RESULT.add(Arrays.asList("Open", "Start"));

        SORT_RESULT.add(Arrays.asList("Open", "Start"));
        SORT_RESULT.add(Arrays.asList("Deleted", null));

        CRITERION_DISPLAY_SORT_RESULT.add(Arrays.asList("Open", "Start"));
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.service.linkedelement.AbstractLinkedElementTestCase#getCriterionCount()
     */
    @Override
    protected int getCriterionCount() {
        return CRITERION_RESULT.size();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.service.linkedelement.AbstractLinkedElementTestCase#getCriterionOperator()
     */
    @Override
    protected String getCriterionOperator() {
        return Operators.LIKE;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.service.linkedelement.AbstractLinkedElementTestCase#getCriterionValue()
     */
    @Override
    protected ScalarValueData getCriterionValue() {
        return new StringValueData("%");
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.service.linkedelement.AbstractLinkedElementTestCase#getDisplayCount()
     */
    @Override
    protected int getDisplayCount() {
        return DISPLAY_RESULT.size();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.service.linkedelement.AbstractLinkedElementTestCase#getExpectedCriterionDisplaySortResult(int)
     */
    @Override
    protected List<String> getExpectedCriterionDisplaySortResult(
            int pResultNumber) {
        return CRITERION_DISPLAY_SORT_RESULT.get(pResultNumber);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.service.linkedelement.AbstractLinkedElementTestCase#getExpectedCriterionResult(int)
     */
    @Override
    protected List<String> getExpectedCriterionResult(int pResultNumber) {
        return CRITERION_RESULT.get(pResultNumber);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.service.linkedelement.AbstractLinkedElementTestCase#getExpectedDisplayResult()
     */
    @Override
    protected List<List<String>> getExpectedDisplayResults() {
        return DISPLAY_RESULT;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.service.linkedelement.AbstractLinkedElementTestCase#getExpectedSortResult(int)
     */
    @Override
    protected List<String> getExpectedSortResult(int pResultNumber) {
        return SORT_RESULT.get(pResultNumber);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.service.linkedelement.AbstractLinkedElementTestCase#getFieldLabelKey()
     */
    @Override
    protected String getFieldLabelKey() {
        return VirtualFieldType.$SHEET_STATE.getValue();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.service.linkedelement.AbstractLinkedElementTestCase#getSortOrder()
     */
    @Override
    protected String getSortOrder() {
        return Operators.DESC;
    }
}
