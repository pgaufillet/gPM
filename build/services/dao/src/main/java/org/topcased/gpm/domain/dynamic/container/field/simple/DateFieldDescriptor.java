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

import java.util.Date;

import org.topcased.gpm.domain.fields.SimpleField;

/**
 * Describe dynamic mapping used to represent a Date Simple Field
 * 
 * @author tpanuel
 */
public class DateFieldDescriptor extends SimpleFieldDescriptor {
    /**
     * Describe the data model of a date simple field
     * 
     * @param pField
     *            The simple field
     */
    public DateFieldDescriptor(SimpleField pField) {
        super(pField, Date.class);
    }
}
