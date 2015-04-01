/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien LOUISY (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.values.filter;

/**
 * The Enum FilterFieldValues.
 * 
 * @author jlouisy
 */
public enum FilterFieldValue {

    /** Not specified value. */
    NOT_SPECIFIED("$NOT_SPECIFIED");

    /** The value. */
    private final String value;

    /**
     * Constructs a new filter field value.
     * 
     * @param pValue
     *            the value
     */
    private FilterFieldValue(String pValue) {
        value = pValue;
    }

    /**
     * Gets the value.
     * 
     * @return the value
     */
    public String getValue() {
        return value;
    }
}