/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien Eballard (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.util.validation;

import static org.topcased.gpm.ui.component.client.resources.i18n.Ui18n.MESSAGES;

import org.topcased.gpm.ui.component.client.container.field.AbstractGpmField;
import org.topcased.gpm.ui.component.client.container.field.multivalued.AbstractGpmMultivaluedChoiceField;
import org.topcased.gpm.ui.component.client.container.field.multivalued.AbstractGpmMultivaluedField;
import org.topcased.gpm.ui.component.client.container.field.multivalued.GpmMultipleMultivaluedField;
import org.topcased.gpm.ui.component.client.util.validation.IRule;

/**
 * Rule implementation for mandatory field. <h3>Constraint</h3>
 * <ul>
 * <li>Value is not null.</li>
 * <li>Value is not empty</li>
 * </ul>
 * 
 * @author mkargbo
 */
public class MandatoryRule implements IRule {
    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.util.validation.IRule#check(org.topcased.gpm.ui.component.client.container.field.AbstractGpmField)
     */
    @SuppressWarnings("unchecked")
    @Override
    public String check(final AbstractGpmField<?> pField) {
        // For multiple multivalued fields
        if (pField instanceof GpmMultipleMultivaluedField) {
            return null; // No check
        }
        // For multivalued fields
        else if (pField instanceof AbstractGpmMultivaluedField
                && pField.isEnabled()) {
            AbstractGpmMultivaluedField<AbstractGpmField<?>, ?> lField =
                    (AbstractGpmMultivaluedField<AbstractGpmField<?>, ?>) pField;
            if (!(pField instanceof AbstractGpmMultivaluedChoiceField)) {
                // Not a multivalued choice field
                // Field must have at least one value
                if (lField.getFieldList().size() == 0) {
                    return MESSAGES.fieldErrorMandatory(lField.getTranslatedFieldName());
                }
                // No subfields must be empty
                for (AbstractGpmField<?> lSubField : lField.getFieldList()) {
                    if (lSubField.isEnabled() && lSubField.isEmpty()) {
                        return MESSAGES.fieldErrorMandatoryMultivalued(lField.getTranslatedFieldName());
                    }
                }
            }
        }
        // For other fields
        if (pField.isEnabled() && pField.isEmpty()) {
            return MESSAGES.fieldErrorMandatory(pField.getTranslatedFieldName());
        }
        else {
            return null;
        }
    }
}
