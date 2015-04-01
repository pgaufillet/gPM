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

import java.util.Collection;

/**
 * CollectionUtils
 * 
 *@author nveillet
 */
public class CollectionUtils {

    /**
     * Compare two collections. returns true if both collections contain the
     * same elements or if the two objects are null.
     * 
     * @param pCollection1
     *            The first collection
     * @param pCollection2
     *            The second collection
     * @return true if contain same elements; false otherwise.
     */
    public static boolean equals(Collection<?> pCollection1,
            Collection<?> pCollection2) {

        if (pCollection1 == null && pCollection2 == null) {
            return true;
        }

        if (pCollection1 == null) {
            return false;
        }

        if (pCollection2 == null && pCollection1 != null) {
            return false;
        }

        if (!pCollection1.containsAll(pCollection2)
                || !pCollection2.containsAll(pCollection1)) {
            return false;
        }
        return true;
    }
}
