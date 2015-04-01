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

import static org.topcased.gpm.ui.component.client.resources.i18n.Ui18n.MESSAGES;

import org.topcased.gpm.ui.component.client.container.field.AbstractGpmField;

/**
 * Rule implementation for e-mail field. <h3>Constraint</h3>
 * <p>
 * Value is an e-mail address.
 * </p>
 * 
 * @author mkargbo
 */
public class EmailRule extends AbstractRule {

    private static final String EMAIL_REGEXP;

    static {
        EMAIL_REGEXP = "^[A-Za-z0-9\\._-]+@[A-Za-z0-9\\.-]+\\.[A-Za-z]{2,4}$";
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.util.validation.AbstractRule#doCheck(org.topcased.gpm.ui.component.client.container.field.AbstractGpmField,
     *      java.lang.String)
     */
    @Override
    protected String doCheck(AbstractGpmField<?> pField, String pValue) {
        if (!pValue.matches(EMAIL_REGEXP)) {
            return new String(
                    MESSAGES.fieldErrorEmail(pField.getTranslatedFieldName()));
        }
        else {
            return null;
        }
    }
}