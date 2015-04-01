/***************************************************************
 * Copyright (c) 2011 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thibault LANDRE (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.shared.administration.user;

import java.io.Serializable;
import java.util.List;

/**
 * Allow to get list of users sorted by some criterias
 * 
 * @author tlandre
 */
public class UiUserLists implements Serializable {

    private static final long serialVersionUID = 8154856279016438343L;

    private List<UiUser> userListSortedByLogin;

    private List<UiUser> userListSortedByName;

    /**
     * Create a new UserLists
     * 
     * @param pUserListSortedByLogin
     *            Users list sorted by login
     * @param pUserListSortedByName
     *            Users list sorted by name and forename
     */
    public UiUserLists(List<UiUser> pUserListSortedByLogin,
            List<UiUser> pUserListSortedByName) {
        userListSortedByLogin = pUserListSortedByLogin;
        userListSortedByName = pUserListSortedByName;
    }

    /**
     * Get the list of users sorted by login
     * 
     * @return the list of users sorted
     */
    public List<UiUser> getUserListSortedByLogin() {
        return userListSortedByLogin;
    }

    /**
     * Get the list of users sorted by name and forename.
     * 
     * @return the list of users sorted
     */
    public List<UiUser> getUserListSortedByName() {
        return userListSortedByName;
    }

}
