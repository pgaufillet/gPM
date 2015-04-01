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
public interface SimpleFieldData extends FieldData {

    /**
     * Get the value of the field
     * 
     * @return Current field value
     */
    String getValue();

    /**
     * Change the value of the field
     * 
     * @param pValue
     *            New value to set
     */
    void setValue(String pValue);

    /**
     * Get the type name of the field
     * 
     * @return Type name of the field
     */
    String getTypeName();
}
