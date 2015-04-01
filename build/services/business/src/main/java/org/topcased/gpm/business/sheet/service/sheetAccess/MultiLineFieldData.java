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

import java.util.List;

/**
 * @author llatil
 */
public interface MultiLineFieldData extends FieldData {

    /**
     * Get the number of lines
     * 
     * @return Number of lines
     */
    int size();

    /**
     * Get a field from a given index
     * 
     * @param pIndex
     *            Index of the field
     * @return Field
     */
    FieldData get(int pIndex);

    /**
     * Get the list of fields.
     * 
     * @return List of fields
     */
    List<FieldData> getFields();

    /**
     * Add a new element. Once added, the element can be accessed through the
     * get() method (using the element index).
     * 
     * @return Index of the added element.
     * @see get(int)
     */
    int addLine();

    /**
     * Remove an element from this list
     * 
     * @param pIndex
     *            Index of the element to remove
     */
    void removeLine(int pIndex);
}
