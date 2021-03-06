/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.fields.impl;

import org.topcased.gpm.business.serialization.data.Field;

/**
 * Implementation for Confidential predicate.
 * 
 * @author mkargbo
 */
public class ConfidentialPredicate implements IFieldsPredicate {

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.fields.impl.IFieldsPredicate#isEnabled(org.topcased.gpm.business.serialization.data.Field,
     *      java.lang.Boolean)
     */
    public boolean isEnabled(Field pField, Boolean pPredicateValue) {
        if ((pPredicateValue == null)
                || (pField.getMultiple().equals(Boolean.TRUE))) {
            return true;
        }
        // else
        return (pField.isConfidential().equals(pPredicateValue));
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.fields.impl.IFieldsPredicate#isEnabled(org.topcased.gpm.business.serialization.data.Field,
     *      java.lang.Boolean)
     */
    public boolean isEnabled(org.topcased.gpm.domain.fields.Field pField,
            Boolean pPredicateValue) {
        Boolean lFieldAccessValue = pField.isConfidential();
        if (pPredicateValue == null) {
            return true;
        }
        // else
        return (lFieldAccessValue.equals(pPredicateValue));
    }
}
