/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.shared.command.user;

import java.util.List;

import net.customware.gwt.dispatch.shared.Result;

import org.topcased.gpm.ui.facade.shared.administration.user.UiUser;
import org.topcased.gpm.ui.facade.shared.administration.user.UiUserLists;

/**
 * AbstractUserResult
 * 
 * @author nveillet
 */
public class AbstractUserResult implements Result {

    /** serialVersionUID */
    private static final long serialVersionUID = 7143720887204674102L;

    private List<UiUser> userListSortedByLogin;

    private List<UiUser> userListSortedByName;

    /**
     * Empty constructor for serialization.
     */
    protected AbstractUserResult() {
    }

    /**
     * Create AbstractUserResult with values.
     * 
     * @param pUserLists
     *            the user lists
     */
    protected AbstractUserResult(UiUserLists pUserLists) {
        userListSortedByLogin = pUserLists.getUserListSortedByLogin();
        userListSortedByName = pUserLists.getUserListSortedByName();
    }

    public List<UiUser> getUserListSortedByLogin() {
        return userListSortedByLogin;
    }

    public List<UiUser> getUserListSortedByName() {
        return userListSortedByName;
    }

}
