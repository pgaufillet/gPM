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

import org.topcased.gpm.business.scalar.ScalarValueData;
import org.topcased.gpm.business.scalar.StringValueData;
import org.topcased.gpm.business.search.criterias.impl.Operators;

/**
 * Test <code>Attached</code> field
 * 
 * @author mkargbo
 */
public class TestAttachedField extends AbstractFieldTestCase {
    private static final String EXPECTED_CRITERION_VALUE = "file_02.jpg";

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
        return new StringValueData(EXPECTED_CRITERION_VALUE);
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
        return new String[] { "sameSheetType_02", "sameSheetType_01" };
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.service.multicontainer.AbstractFieldTest#getFieldLabelKey()
     */
    @Override
    protected String getFieldLabelKey() {
        return "attachedField";
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
}