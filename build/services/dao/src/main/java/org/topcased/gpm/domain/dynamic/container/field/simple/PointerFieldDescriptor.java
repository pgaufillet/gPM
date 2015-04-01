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

import org.topcased.gpm.domain.fields.Field;
import org.topcased.gpm.domain.fields.PointerFieldValue;

/**
 * Describe dynamic mapping used to represent a Pointer Field
 * 
 * @author tpanuel
 */
public class PointerFieldDescriptor extends ManyToOneFieldDescriptor {
    /**
     * Describe the data model of a pointer field
     * 
     * @param pField
     *            The pointer field
     */
    public PointerFieldDescriptor(Field pField) {
        super(pField, PointerFieldValue.class, PointerFieldValue.class, true);
    }
}