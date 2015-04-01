/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin), Thomas Szadel
 * (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.exception;

import org.apache.commons.lang.StringUtils;

/**
 * Exception thrown when an invalid value is assigned to a field.
 * 
 * @author llatil
 */
public class InvalidFieldValueException extends BusinessException {

    /** Generated UID. */
    private static final long serialVersionUID = -5696664020261331622L;

    /**
     * Create a new invalid field value exception.
     * 
     * @param pSheetTypeName
     *            Name of the sheet type
     * @param pFieldName
     *            Name of the field
     * @param pValue
     *            Invalid value
     */
    public InvalidFieldValueException(String pSheetTypeName, String pFieldName,
            String pValue) {
        super("InvalidFieldValueException / Invalid value '" + pValue
                + "' for field '" + pFieldName + "' in type '" + pSheetTypeName
                + "'", "Invalid value '" + pValue + "' for field '"
                + pFieldName + "' in type '" + pSheetTypeName + "'");

        fieldName = pFieldName;
        sheetTypeName = pSheetTypeName;
        value = pValue;
    }

    /**
     * Create a new invalid field value exception.
     * 
     * @param pFieldName
     *            Name of the field
     * @param pValue
     *            Invalid value
     */
    public InvalidFieldValueException(String pFieldName, String pValue) {
        super("Invalid value '" + pValue + "' for field '" + pFieldName + "'",
                "Invalid value '" + pValue + "' for field '" + pFieldName + "'");

        fieldName = pFieldName;
        value = pValue;
    }

    /**
     * Get the name of the field set with an incorrect value.
     * 
     * @return Name of the field
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * Get the sheet type name of the field.
     * 
     * @return Name of the sheet type
     */
    public String getSheetTypeName() {
        if (null != sheetTypeName) {
            return sheetTypeName;
        }
        // else
        return StringUtils.EMPTY;
    }

    /**
     * Get the invalid value.
     * 
     * @return Value
     */
    public String getValue() {
        return value;
    }

    /** The field name. */
    protected String fieldName;

    /** The value. */
    protected String value;

    /** The sheet type name. */
    protected String sheetTypeName;
}
