/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien EBALLARD (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.shared.command.user;

import org.topcased.gpm.ui.application.shared.command.AbstractCommandAction;
import org.topcased.gpm.ui.facade.shared.administration.user.UiUser;

/**
 * Update user action.
 * 
 * @author jeballar
 */
public class UpdateUserProfileAction extends
        AbstractCommandAction<GetUserProfileResult> {

    private static final long serialVersionUID = 169010420331415491L;

    private UiUser user;

    /**
     * create action
     */
    public UpdateUserProfileAction() {
        super();
    }

    /**
     * create action with values
     * 
     * @param pUser
     *            the user to update
     */
    public UpdateUserProfileAction(UiUser pUser) {
        super();
        user = pUser;
    }

    /**
     * Get the user
     * 
     * @return the user
     */
    public UiUser getUser() {
        return user;
    }

    /**
     * Set the user
     * 
     * @param pUser
     *            the user
     */
    public void setUser(UiUser pUser) {
        user = pUser;
    }

}
