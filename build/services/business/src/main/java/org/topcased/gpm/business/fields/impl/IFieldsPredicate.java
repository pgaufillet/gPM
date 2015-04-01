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

import org.eclipse.jdt.core.dom.FieldAccess;
import org.topcased.gpm.business.serialization.data.Field;

/**
 * Fields predicate interface.
 * <p>
 * A fields predicate if the field access property:
 * <ul>
 * <li>Mandatory</li>
 * <li>Updatable</li>
 * <li>Exportable</li>
 * <li>Confidential</li>
 * </ul>
 * Defines in {@link FieldAccess}
 * </p>
 * <p>
 * FielsPredicate provides one method to know if the field respect the predicate
 * value.
 * </p>
 * <p>
 * Implementing classes must be creating with FieldsPredicatFactory.
 * 
 * @see {@link FieldAccess}
 * @see {@link FieldsPredicateFactory}
 * @author mkargbo
 */
public interface IFieldsPredicate {

    /**
     * Test the fields predicate value.
     * 
     * @param pField
     *            Field to test
     * @param pPredicatValue
     *            Expected predicate value. Can be null.
     * @return True if the field predicate value is the same as expected
     *         predicate value, false otherwise. <br />
     *         Returns true if the expected predicate value is null.
     */
    public boolean isEnabled(final Field pField, final Boolean pPredicatValue);

    /**
     * Test the fields predicate value.
     * 
     * @param pField
     *            Field to test
     * @param pPredicateValue
     *            Expected predicate value. Can be null.
     * @return True if the field predicate value is the same as expected
     *         predicate value, false otherwise. <br />
     *         Returns true if the expected predicate value is null.
     */
    public boolean isEnabled(final org.topcased.gpm.domain.fields.Field pField,
            final Boolean pPredicateValue);
}
