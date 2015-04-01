/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin)
 ******************************************************************/

package org.topcased.gpm.business.authorization.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.topcased.gpm.domain.accesscontrol.AccessControl;

/**
 * AuthorizationUtils
 * <p>
 * Various utility methods used for Authorization service.
 * 
 * @author llatil
 */
public final class AuthorizationUtils {
    /**
     * This array is the priority order of the access control The first item is
     * the one with the lower priority, to higher priority is at the end P as
     * Product, S as State, R as Role, T as Type
     */
    private static final List<String> PRIORITYLIST =
            Arrays.asList(new String[] { "", "T", "R", "P", "RT", "ST", "PT",
                                        "PR", "SRT", "PRT", "PST", "PSRT" });

    /**
     * Private ctor to prevent construction of this class
     */
    private AuthorizationUtils() {
    }

    // Sort the applicable access controls  (cannot do it reliably with DB queries)
    private static final Comparator<AccessControl> AC_COMPARATOR =
            new Comparator<AccessControl>() {
                public final int compare(AccessControl pO1, AccessControl pO2) {
                    String lO1Key = "";
                    String lO2Key = "";
                    //Build the key with the accessControl attributes
                    if (pO1.getProductControl() != null) {
                        lO1Key = lO1Key + "P";
                    }
                    if (pO2.getProductControl() != null) {
                        lO2Key = lO2Key + "P";
                    }
                    if (pO1.getStateControl() != null) {
                        lO1Key = lO1Key + "S";
                    }
                    if (pO2.getStateControl() != null) {
                        lO2Key = lO2Key + "S";
                    }
                    if (pO1.getRoleControl() != null) {
                        lO1Key = lO1Key + "R";
                    }
                    if (pO2.getRoleControl() != null) {
                        lO2Key = lO2Key + "R";
                    }
                    if (pO1.getTypeControl() != null) {
                        lO1Key = lO1Key + "T";
                    }
                    if (pO2.getTypeControl() != null) {
                        lO2Key = lO2Key + "T";
                    }
                    //compare O1 with O2 in order to have the order of this AccessControl
                    return Integer.valueOf(PRIORITYLIST.indexOf(lO1Key)).compareTo(
                            Integer.valueOf(PRIORITYLIST.indexOf(lO2Key)));

                }
            };

    /**
     * Sort a collection of applicable access controls.
     * <P>
     * The returned collection is sorted from the least applicable control to
     * the most applicable one.
     * 
     * @param pApplicables
     *            List of applicable access controls
     * @return Sorted access controls
     */
    static <T extends AccessControl> Collection<T> getSortedAccessControls(
            Collection<T> pApplicables) {
        SortedSet<T> lSortedFac = new TreeSet<T>(AC_COMPARATOR);
        lSortedFac.addAll(pApplicables);
        return lSortedFac;
    }
}
