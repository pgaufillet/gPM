/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Florian ROSIER (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.shared.util;

/**
 * StringUtils is an helper to manipulates string values.
 * 
 * @author frosier
 */
public class GpmStringUtils {

    /**
     * Get the string value empty if null.
     * 
     * @param pStringValue
     *            The string value to check if null.
     * @return An empty string if <code>value</code> is null, the
     *         <code>value</code> otherwise.
     */
    public static String getEmptyIfNull(final String pStringValue) {
        if (pStringValue == null || pStringValue.trim().equals("")) {
            return "";
        }
        else {
            return pStringValue;
        }
    }

    /**
     * Determines if the string value is empty or null.
     * 
     * @param pStringValue
     *            The string value to check if null or empty.
     * @return <code>true</code> if the value in parameter is empty or null.
     */
    public static boolean isEmptyOrNull(final String pStringValue) {
        return "".equals(getEmptyIfNull(pStringValue));
    }
}
