/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien LOUISY (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.server.user;

import java.util.Comparator;
import java.util.List;

/**
 * UserNameComparator : compare users to sort by Name - Forename.
 * 
 * @author jlouisy
 */
public class UserRoleComparator implements Comparator<String> {

    private static UserRoleComparator staticInstance;

    private static List<String> staticReference;

    /**
     * Get singleton instance.
     * 
     * @param pReference
     *            Reference list containing roles order.
     * @return instance of comparator.
     */
    public static UserRoleComparator getInstance(List<String> pReference) {
        if (staticInstance == null) {
            staticInstance = new UserRoleComparator();
        }
        staticReference = pReference;
        return staticInstance;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    @Override
    public int compare(String pRole1, String pRole2) {
        Integer lIndexOfRole1 = staticReference.indexOf(pRole1);
        Integer lIndexOfRole2 = staticReference.indexOf(pRole2);
        return lIndexOfRole1.compareTo(lIndexOfRole2);
    }

}
