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
 * UserLoginComparator : compare users to sort by login.
 * 
 * @author jlouisy
 */
public class UserLoginComparator implements Comparator<UiUser> {

    private static UserLoginComparator staticInstance;

    /**
     * Get singleton instance.
     * 
     * @return instance of comparator.
     */
    public static UserLoginComparator getInstance() {
        if (staticInstance == null) {
            staticInstance = new UserLoginComparator();
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
        return pUser1.getLogin().toLowerCase().compareTo(
                pUser2.getLogin().toLowerCase());
    }

}
