/***************************************************************
 * Copyright (c) 2011 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: phtsaan  (Atos)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.util.validation;

import static org.topcased.gpm.ui.component.client.resources.i18n.Ui18n.MESSAGES;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.topcased.gpm.ui.component.client.container.field.AbstractGpmField;
import org.topcased.gpm.ui.component.client.container.field.multivalued.GpmMultivaluedField;
import org.topcased.gpm.ui.component.client.util.validation.IRule;

/**
 * Rule implementation for duplicate value . <h3>Constraint</h3>
 * <p>
 * Value is duplicated.
 * </p>
 * 
 * @author phtsaan
 */
public class DuplicateValueRule implements IRule {

    private Set<String> valuesSet;

    /**
     * {@inheritDoc} Check duplicate values
     * 
     * @see org.topcased.gpm.ui.application.client.util.validation.AbstractRule#doCheck(org.topcased.gpm.ui.component.client.container.field.AbstractGpmField,
     *      java.lang.String)
     */
    @Override
    public String check(AbstractGpmField<?> pField) {
        valuesSet = new HashSet<String>();
        if (pField instanceof GpmMultivaluedField<?>) {
            GpmMultivaluedField<?> lGpmMultiple =
                    (GpmMultivaluedField<?>) pField;
            Iterator<?> lIterator = lGpmMultiple.iterator();

            while (lIterator.hasNext()) {
                String lCurrentValue =
                        ((AbstractGpmField<?>) lIterator.next()).getAsString();
                if (!valuesSet.add(lCurrentValue)) {
                    valuesSet.clear();
                    return new String(MESSAGES.errorDuplicateValue());
                }
            }
        }
        valuesSet.clear();
        return null;
    }
}
