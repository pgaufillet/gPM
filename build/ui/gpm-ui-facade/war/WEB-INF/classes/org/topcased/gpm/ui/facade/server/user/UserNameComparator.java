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

import org.topcased.gpm.ui.facade.shared.administration.user.UiUser;

/**
 * UserNameComparator : compare users to sort by Name - Forename.
 * 
 * @author jlouisy
 */
public class UserNameComparator implements Comparator<UiUser> {

    private static UserNameComparator staticInstance;

    /**
     * Get singleton instance.
     * 
     * @return instance of comparator.
     */
    public static UserNameComparator getInstance() {
        if (staticInstance == null) {
            staticInstance = new UserNameComparator();
        }
        return staticInstance;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    @Override
    public int compare(UiUser pUser1, UiUser pUser2) {

        String lUser1NameForeName = pUser1.getName().toLowerCase();
        if (pUser1.getForename() != null) {
            lUser1NameForeName += pUser1.getForename().toLowerCase();
        }

        String lUser2NameForeName = pUser2.getName().toLowerCase();
        if (pUser2.getForename() != null) {
            lUser2NameForeName += pUser2.getForename().toLowerCase();
        }

        return lUser1NameForeName.compareTo(lUser2NameForeName);
    }
}
