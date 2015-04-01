/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Anne Haugommard (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.fields.impl;

import org.topcased.gpm.business.serialization.data.Field;

/**
 * PointerFieldPredicate : check if a field is a pointer or not
 * 
 * @author ahaugomm
 */
public class PointerFieldPredicate implements IFieldsPredicate {

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.fields.impl.IFieldsPredicate#isEnabled(org.topcased.gpm.business.serialization.data.Field,
     *      java.lang.Boolean)
     */
    public boolean isEnabled(Field pField, Boolean pPredicateValue) {
        if (pPredicateValue == null) {
            return true;
        }
        // else
        return (pPredicateValue.booleanValue() == pField.isPointerField());
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.fields.impl.IFieldsPredicate#isEnabled(org.topcased.gpm.domain.fields.Field,
     *      java.lang.Boolean)
     */
    public boolean isEnabled(org.topcased.gpm.domain.fields.Field pField,
            Boolean pPredicatValue) {
        if (pPredicatValue == null) {
            return true;
        }
        // else
        return (pPredicatValue.booleanValue() == pField.isPointerField());
    }
}
