/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available 
 * under the terms of the Lesser Gnu Public License (LGPL)which 
 * accompanies this distribution, and is available 
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin), Sophian Idjellidaine (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.instantiation.fieldaccess;

/**
 * Maps SheetData.
 * 
 * @author nbousque
 */
public interface SheetAccess extends FieldCompositeAccess {

    /**
     * Get the identifier of the sheet in the DB.
     * 
     * @return the Id of the sheet.
     */
    public String getId();

    /**
     * Get process name
     * 
     * @return the process name of the sheet.
     */
    public String getProcessName();

    /**
     * Get product name
     * 
     * @return the productName of the sheet.
     */
    public String getProductName();

    /**
     * Get the functional reference of the sheet.
     * 
     * @return the reference of the sheet.
     */
    public org.topcased.gpm.instantiation.fieldaccess.MultipleFieldAccess getReference();

    /**
     * Get the 'stringified' reference of the sheet.
     * 
     * @return the String version of the reference.
     */
    public String getStringReference();

    /**
     * Get sheet version
     * 
     * @return the version of the sheet.
     */
    public int getVersion();

    /**
     * Get the name of sheet type.
     * 
     * @return the type of the sheet.
     */
    public String getSheetTypeName();
}