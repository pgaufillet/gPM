/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.domain.accesscontrol;

import java.util.Comparator;

/**
 * Compare the filter access control to find the one with the highest priority.
 * 
 * @author tpanuel
 */
public class FilterAccessComparator implements Comparator<FilterAccess> {

    private final static int BEFORE = -1;

    private final static int EQUAL = 0;

    private final static int AFTER = 1;

    /**
     * {@inheritDoc}
     * 
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    public int compare(final FilterAccess pAccess1, final FilterAccess pAccess2) {
        int lResult = compareNull(pAccess1, pAccess2);

        // Priority :
        // - Field Name
        // - Type Name
        // - Visibility
        // - Role Name
        if (lResult == EQUAL) {
            lResult =
                    compareNull(pAccess1.getFieldName(),
                            pAccess2.getFieldName());
        }
        if (lResult == EQUAL) {
            lResult =
                    compareNull(pAccess1.getTypeName(), pAccess2.getTypeName());
        }
        if (lResult == EQUAL) {
            lResult =
                    compareNull(pAccess1.getVisibility(),
                            pAccess2.getVisibility());
        }
        if (lResult == EQUAL) {
            lResult =
                    compareNull(pAccess1.getRoleName(), pAccess2.getRoleName());
        }

        return lResult;
    }

    private int compareNull(final Object pObject1, final Object pObject2) {
        if (pObject1 != null && pObject2 == null) {
            return BEFORE;
        }
        else if (pObject1 == null && pObject2 != null) {
            return AFTER;
        }
        else {
            return EQUAL;
        }
    }
}