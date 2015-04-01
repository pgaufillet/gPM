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
 * @author Atos
 */
public class FieldDataFactory {

    /**
     * Create an actual FieldData implementation based on the content of the
     * field data
     * 
     * @param pFieldData
     *            Field content
     * @return FieldData access object.
     */
    public static FieldData create(
            final org.topcased.gpm.business.fields.FieldData pFieldData) {
        FieldData lResult = null;

        if (pFieldData.getFileValue() == null) {
            if (pFieldData.getFieldAvailableValueData() != null
                    && pFieldData.getFieldAvailableValueData().getValues().length > 0) {
                lResult = new ChoiceFieldDataAccess(pFieldData);
            }
            else {
                lResult = new SimpleFieldDataAccess(pFieldData);
            }
        }
        else {
            lResult = new FileFieldValueAccess(pFieldData);
        }

        return lResult;
    }
}
