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
 * Interface implemente by all classes containing fields.
 * 
 * @author llatil
 */
public interface FieldDataContainer {

    /**
     * Get the list of all fields stored in this container.
     * 
     * @return List of FieldData used to handle the fields of this container
     */
    List<FieldData> getFields();

    /**
     * Get the list of all field label keys stored in this container.
     * 
     * @return List of field keys.
     */
    List<String> getFieldKeys();

    /**
     * Get a field from its name
     * 
     * @param pKey
     *            Label key of the field
     * @return Data of the field.
     */
    FieldData getField(String pKey);
}
