/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: XXX (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.util;

/**
 * Utils for UI.
 * 
 * @author mkargbo
 */
public final class UIUtils {

    /**
     * Parse string value to test if its a integer value
     * 
     * @param pInteger
     *            Value to parse
     * @return True if the value is an integer, false otherwise.
     */
    public static boolean isInteger(final String pInteger) {
        boolean lResult;
        try {
            Integer.parseInt(pInteger);
            lResult = true;
        }
        catch (NumberFormatException e) {
            lResult = false;
        }
        return lResult;
    }

    /**
     * Parse string value to test if its a integer value
     * 
     * @param pInteger
     *            Value to parse
     * @return True if the value is an integer, false otherwise.
     */
    public static boolean isPositiveInteger(final String pInteger) {
        boolean lResult;
        try {
            int lInt = Integer.parseInt(pInteger);
            lResult = lInt >= 0;
        }
        catch (NumberFormatException e) {
            lResult = false;
        }
        return lResult;
    }

    /**
     * Parse string value to test if its a real (double) value
     * 
     * @param pDouble
     *            Value to parse
     * @return True if the value is a double, false otherwise
     */
    public static boolean isDouble(final String pDouble) {
        boolean lResult;
        try {
            Double.parseDouble(pDouble);
            lResult = true;
        }
        catch (NumberFormatException e) {
            lResult = false;
        }
        return lResult;
    }
}
