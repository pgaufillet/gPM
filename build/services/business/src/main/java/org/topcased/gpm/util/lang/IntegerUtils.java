/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.util.lang;

/**
 * CollectionUtils
 * 
 *@author nveillet
 */
public class IntegerUtils {

    /**
     * Compare two Integers objects. The result is true if the Integer objects
     * contains the same int value or if the two objects are null.
     * 
     * @param pValue1
     *            The first Integer
     * @param pValue2
     *            The second Integer
     * @return true if the integer values are the same; false otherwise.
     */
    public static boolean equals(Integer pValue1, Integer pValue2) {

        if (pValue1 == null && pValue2 == null) {
            return true;
        }

        if (pValue1 == null) {
            return false;
        }

        if (pValue2 == null && pValue1 != null) {
            return false;
        }

        if (!pValue1.equals(pValue2)) {
            return false;
        }
        return true;
    }
}
