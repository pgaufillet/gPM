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
 * Interface implemented by all field data classes.
 * 
 * @author llatil
 */
public interface FieldData {
    /**
     * Get the field name
     * 
     * @return Field name
     */
    String getName();

    /**
     * Get the field label key
     * 
     * @return Field label key
     */
    String getLabelKey();

    /**
     * Get the description of the field
     * 
     * @return Description string
     */
    String getDescription();

    /**
     * Specify if this field is mandatory.
     * 
     * @return True if the field muist have a value.
     */
    boolean isMandatory();

    /**
     * Specify if this field is confidential.
     * 
     * @return True if the field is confidential.
     */
    boolean isConfidential();

    /**
     * Specify if the field value can be changed
     * 
     * @return True if the field value can be changed
     */
    boolean isUpdatable();

    /**
     * Check if this field contains several values (multiple lines)
     * 
     * @return True if field is multiline.
     */
    boolean isMultiLine();

    /**
     * Check if this field contains sub-fields.
     * 
     * @return True if this field has sub-fields.
     */
    boolean isMultiField();
}
