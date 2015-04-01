/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.util.validation;

import org.topcased.gpm.ui.component.client.container.field.AbstractGpmField;
import org.topcased.gpm.ui.component.client.container.field.multivalued.GpmMultivaluedField;
import org.topcased.gpm.ui.component.client.util.validation.IRule;

/**
 * Abstract class that handle <code>null</code> and <code>empty</code> value for
 * the field's value to check.
 * 
 * @author mkargbo
 */
public abstract class AbstractRule implements IRule {

    /**
     * {@inheritDoc}
     * <p>
     * Test the field's value: no result if the value is 'null' or 'empty'.
     * </p>
     * 
     * @see org.topcased.gpm.ui.component.client.util.validation.IRule#check(org.topcased.gpm.ui.component.client.container.field.AbstractGpmField)
     */
    @Override
    public String check(AbstractGpmField<?> pField) {
        final String lResult;
        if (pField.isEnabled()) {
            if (pField instanceof GpmMultivaluedField) {
                lResult = handleMultivalued(pField);
            }
            else {
                lResult = handleSimple(pField);
            }
        }
        else {
            lResult = null;
        }
        return lResult;
    }

    /**
     * Test the simple field
     * 
     * @param pField
     *            Field to test
     * @return Null if the field does not respect the rule, an error message
     *         otherwise.
     */
    private String handleSimple(AbstractGpmField<?> pField) {
        final String lValue = pField.getAsString();
        final String lResult;
        if (lValue != null && !lValue.isEmpty()) {
            lResult = doCheck(pField, lValue);
        }
        else {
            lResult = null;
        }
        return lResult;
    }

    /**
     * Test multivalued field
     * 
     * @param pField
     *            Field to test
     * @return Null if the field does not respect the rule, an error message
     *         otherwise.
     */
    @SuppressWarnings("unchecked")
	private String handleMultivalued(AbstractGpmField<?> pField) {
        StringBuilder lResult = new StringBuilder();
        GpmMultivaluedField<AbstractGpmField<?>> lMultivaluedField =
                (GpmMultivaluedField<AbstractGpmField<?>>) pField;
        for (AbstractGpmField<?> lGpmField : lMultivaluedField.getFieldList()) {
            final String lValue = lGpmField.getAsString();
            final String lFieldResult;
            if (lValue != null && !lValue.isEmpty()) {
                lFieldResult = doCheck(lGpmField, lValue);
            }
            else {
                lFieldResult = null;
            }
            if (lFieldResult != null && !lFieldResult.isEmpty()) {
                lResult.append(lFieldResult);
            }
        }

        if (lResult.length() > 0) {
            return lResult.toString();
        }
        else {
            return null;
        }
    }

    /**
     * Check the value according to the implementation constraints.
     * 
     * @param pField
     *            Field containing the value
     * @param pValue
     *            Value to check
     * @return True if the constraint is respected, false otherwise
     */
    protected abstract String doCheck(final AbstractGpmField<?> pField,
            final String pValue);

}
