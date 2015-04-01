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

import org.topcased.gpm.ui.application.client.util.UIUtils;
import org.topcased.gpm.ui.component.client.container.field.AbstractGpmField;

/**
 * Rule implementation for integer field. <h3>Constraint</h3>
 * <p>
 * Value is an integer.
 * </p>
 * 
 * @author mkargbo
 */
public class IntegerRule extends AbstractRule {

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.util.validation.AbstractRule#doCheck(org.topcased.gpm.ui.component.client.container.field.AbstractGpmField,
     *      java.lang.String)
     */
    @Override
    protected String doCheck(AbstractGpmField<?> pField, String pValue) {
        if (!UIUtils.isInteger(pValue)) {
            return new String(
                    MESSAGES.fieldErrorInteger(pField.getTranslatedFieldName()));
        }
        else {
            return null;
        }
    }
}
