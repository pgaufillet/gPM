/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available 
 * under the terms of the Lesser Gnu Public License (LGPL)which 
 * accompanies this distribution, and is available 
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.sheet.service.sheetAccess;

/**
 * @author llatil
 */
public class SimpleFieldDataAccess extends FieldDataAccess implements
        SimpleFieldData {

    /**
     * Constructs a new accesss object for simple fields
     * 
     * @param pFieldData
     *            Data of the field.
     */
    public SimpleFieldDataAccess(
            final org.topcased.gpm.business.fields.FieldData pFieldData) {
        super(pFieldData);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.sheetAccess.SimpleFieldData#getValue()
     */
    public String getValue() {
        String[] lValues = fieldData.getValues().getValues();
        if (lValues.length == 0) {
            return null;
        }
        // else
        return fieldData.getValues().getValues()[0];
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.sheetAccess.SimpleFieldData#setValue(java.lang.String)
     */
    public void setValue(final String pValue) {
        String[] lValues = fieldData.getValues().getValues();
        if (lValues.length == 0) {
            fieldData.getValues().setValues(new String[] { pValue });
        }
        else {
            fieldData.getValues().getValues()[0] = pValue;
        }
    }

    /**
     * Get the type name of the field.
     * 
     * @return Type name
     */
    public String getTypeName() {
        return fieldData.getFieldType();
    }
}
