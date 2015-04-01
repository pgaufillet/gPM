/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.util.validation;

import static org.topcased.gpm.ui.component.client.resources.i18n.Ui18n.MESSAGES;

import org.topcased.gpm.ui.component.client.container.field.AbstractGpmField;

/**
 * Rule implementation for mandatory field. <h3>Constraint</h3>
 * <ul>
 * <li>Value is not null.</li>
 * <li>Value is not empty</li>
 * </ul>
 * 
 * @author nveillet
 */
public class PasswordRule extends AbstractRule {

    private AbstractGpmField<?> originalField;

    /**
     * Constructor
     * 
     * @param pOriginalField
     *            the original password field
     */
    public PasswordRule(AbstractGpmField<?> pOriginalField) {
        originalField = pOriginalField;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.util.validation.AbstractRule#doCheck(org.topcased.gpm.ui.component.client.container.field.AbstractGpmField,
     *      java.lang.String)
     */
    @Override
    protected String doCheck(AbstractGpmField<?> pField, String pValue) {
        if (!pValue.equals(originalField.getAsString())) {
            return new String(MESSAGES.fieldErrorPassword(
                    pField.getTranslatedFieldName(),
                    originalField.getTranslatedFieldName()));
        }
        else {
            return null;
        }
    }
}
