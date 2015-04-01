/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.util.comparator;

import org.apache.commons.lang.StringUtils;

/**
 * Comparator : for example a VARCHAR empty is a VARCHAR null
 * 
 * @author tpanuel
 */
public class DataBaseValuesComparator {
    /**
     * Compare two string supposing that the data base saves VARCHAR EMPTY like
     * VARCHAR NULL
     * 
     * @param pString1
     *            The first string value
     * @param pString2
     *            The second string value
     * @return IF the string value are the same
     */
    public final static boolean equals(String pString1, String pString2) {
        final String lString1;
        final String lString2;

        // pString1 or EMPTY
        if (pString1 == null) {
            lString1 = StringUtils.EMPTY;
        }
        else {
            lString1 = pString1;
        }
        // pString2 or EMPTY
        if (pString2 == null) {
            lString2 = StringUtils.EMPTY;
        }
        else {
            lString2 = pString2;
        }

        return lString1.equals(lString2);
    }
}
