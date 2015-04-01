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

import org.topcased.gpm.common.fields.FieldsPredicateCondition;

/**
 * Defines a factory that given to application a implementing classes of
 * IFieldsPredicate interface according to the field property.
 * <p>
 * All new implementation of IFieldsPredicate interface have to be registered in
 * this class.
 * 
 * @author mkargbo
 */
public class FieldsPredicateFactory {

    /**
     * Constructs an IFieldsPredicate instance of an known implementing classes
     * according to the given field property.
     * 
     * @param pFieldProperty
     *            Field property. Can be null.
     * @return An implementation of IFieldsPredicate. If field property is null,
     *         returns the default implementation.
     * @throws IllegalArgumentException
     *             If the field property is not null and doesn't have an
     *             implementation of IFieldPredicate interface.
     */
    public static IFieldsPredicate getInstance(
            final FieldsPredicateCondition pFieldProperty)
        throws IllegalArgumentException {
        if (pFieldProperty == null) {
            return new DefaultPredicate();
        }
        // else
        switch (pFieldProperty) {
            case CONFIDENTIAL:
                return new ConfidentialPredicate();
            case POINTER_FIELD:
                return new PointerFieldPredicate();
            default:
                throw new IllegalArgumentException("The property '"
                        + pFieldProperty + "' has no predicate class");
        }
    }
}
