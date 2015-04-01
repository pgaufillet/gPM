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
 * Rule implementation for limited test size of field. <h3>Constraint</h3>
 * <p>
 * Value's length is not greater than expected size.
 * </p>
 * 
 * @author mkargbo
 */
public class SizeRule extends AbstractRule {
    private int size;

    /**
     * Constructs a size rule
     * 
     * @param pSize
     *            Expected size of the field's value
     */
    protected SizeRule(int pSize) {
        size = pSize;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.util.validation.AbstractRule#doCheck(org.topcased.gpm.ui.component.client.container.field.AbstractGpmField,
     *      java.lang.String)
     */
    @Override
    protected String doCheck(AbstractGpmField<?> pField, String pValue) {
        if (size != -1 && pValue.length() > size) {
            return new String(MESSAGES.fieldErrorMaxsize(
                    pField.getTranslatedFieldName(), size));
        }
        else {
            return null;
        }
    }
}