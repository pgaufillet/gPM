/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.domain.dynamic.container.field.simple;

import org.topcased.gpm.domain.dictionary.CategoryValue;
import org.topcased.gpm.domain.fields.ChoiceField;

/**
 * Describe dynamic mapping used to represent a Choice Field
 * 
 * @author tpanuel
 */
public class ChoiceFieldDescriptor extends ManyToOneFieldDescriptor {
    /**
     * Describe the data model of a choice field
     * 
     * @param pField
     *            The choice field
     */
    public ChoiceFieldDescriptor(ChoiceField pField) {
        super(pField, CategoryValue.class, CategoryValue.class, false);
    }
}