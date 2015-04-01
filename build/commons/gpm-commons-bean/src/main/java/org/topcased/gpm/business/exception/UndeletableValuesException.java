/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin), Thomas Szadel
 * (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.exception;

import java.util.Collection;

/**
 * Exception raised when trying to delete a value still referenced elsewhere.
 * 
 * @author llatil
 */
public class UndeletableValuesException extends BusinessException {
    /**
     * Automatically generated ID.
     */
    private static final long serialVersionUID = 9011151831614186121L;

    /** Default user message */
    private static final String DEFAULT_UNDELETABLEVALUES_ERROR_MSG =
            "Some values cannot be deleted : ";

    /**
     * Construct a UndeletableValues exception
     * 
     * @param pValues
     *            List of non deletable values.
     */
    public UndeletableValuesException(Collection<String> pValues) {
        values = pValues;
        setUserMessage(DEFAULT_UNDELETABLEVALUES_ERROR_MSG
                + computeValuesString());
    }

    /**
     * Get the message of the exception. This message includes the name of all
     * non deletable values.
     * 
     * @return Exception message.
     */
    public String getMessage() {
        StringBuilder lBuffer;
        lBuffer =
                new StringBuilder(
                        "Category values cannot be deleted because used in container.");
        lBuffer.append(" [");
        lBuffer.append(computeValuesString());
        lBuffer.append("]");
        return lBuffer.toString();
    }

    /**
     * Get the list of non removable values.
     * 
     * @return List of non removable values.
     */
    public Collection<String> getValuesList() {
        return values;
    }

    /**
     * Creates the values list String for user display
     * 
     * @return String containing values separated by "," .
     */
    private String computeValuesString() {
        StringBuilder lBuffer = new StringBuilder();
        boolean lFirst = true;
        for (String lValue : values) {
            if (!lFirst) {
                lBuffer.append(", ");
            }
            lFirst = false;
            lBuffer.append(lValue);
        }

        return lBuffer.toString();
    }

    private Collection<String> values;
}
