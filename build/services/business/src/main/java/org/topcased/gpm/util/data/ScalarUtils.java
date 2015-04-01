/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Lesser Gnu
 * Public License (LGPL) which accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Yvan Ntsama (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.util.data;

import org.topcased.gpm.business.scalar.ScalarValueData;

/**
 * ScalarUtils: class to help handling ScalarValueData objects
 * 
 * @author yntsama
 */
public class ScalarUtils {

    /**
     * Compares two ScalarValueData objects by their values.
     * 
     * @param pScalar1
     *            the scalar value data #1
     * @param pScalar2
     *            the scalar value data #2
     * @return true, if parameters have the same value attribute
     */
    public static boolean equalsScalar(ScalarValueData pScalar1,
            ScalarValueData pScalar2) {

        // Ensures the two values belong to the same Java class
        if (pScalar1.getClass() != pScalar2.getClass()) {
            return false;
        }
        return pScalar1.getValue().equals(pScalar2.getValue());
    }
}