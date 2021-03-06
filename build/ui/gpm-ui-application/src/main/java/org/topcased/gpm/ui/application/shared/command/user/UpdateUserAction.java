/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien LOUISY (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.shared.command.user;

import org.topcased.gpm.ui.application.shared.command.AbstractCommandAction;
import org.topcased.gpm.ui.facade.shared.administration.user.UiUser;

/**
 * Update user action.
 * 
 * @author jlouisy
 */
public class UpdateUserAction extends AbstractCommandAction<GetUserResult> {

    private static final long serialVersionUID = 169010420331415491L;

    private UiUser user;

    /**
     * create action
     */
    public UpdateUserAction() {
        super();
    }

    /**
     * create action with values
     * 
     * @param pUser
     *            the user to update
     */
    public UpdateUserAction(UiUser pUser) {
        super();
        user = pUser;
    }

    public UiUser getUser() {
        return user;
    }

    public void setUser(UiUser pUser) {
        user = pUser;
    }

}
