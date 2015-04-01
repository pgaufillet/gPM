/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.domain.dynamic.container.field;

import org.topcased.gpm.domain.dynamic.ColumnDescriptor;
import org.topcased.gpm.domain.dynamic.util.DynamicObjectNamesUtils;
import org.topcased.gpm.domain.fields.Field;

/**
 * Describe dynamic mapping used to represent a field
 * 
 * @author tpanuel
 */
public abstract class FieldDescriptor extends ColumnDescriptor {
    private final String fieldId;

    /**
     * Describe the data model of a field
     * 
     * @param pField
     *            The field
     * @param pClass
     *            The class of the object representing the field
     */
    public FieldDescriptor(Field pField, Class<?> pClass) {
        super(
                DynamicObjectNamesUtils.getInstance().initDynamicColumnName(
                        pField),
                pClass,
                DynamicObjectNamesUtils.getInstance().getDynamicColumnGetterName(
                        pField.getId()),
                DynamicObjectNamesUtils.getInstance().getDynamicColumnSetterName(
                        pField.getId()));
        fieldId = pField.getId();
    }

    /**
     * Get the id of the described field
     * 
     * @return The id of the described field
     */
    public String getFieldId() {
        return fieldId;
    }
}
