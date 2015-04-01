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

/**
 * Delete user action.
 * 
 * @author jlouisy
 */
public class DeleteUserAction extends AbstractCommandAction<DeleteUserResult> {

    private static final long serialVersionUID = 169010420331415491L;

    private String userLogin;

    /**
     * create action
     */
    public DeleteUserAction() {
        super();
    }

    /**
     * create action with values
     * 
     * @param pLogin
     *            the user login
     */
    public DeleteUserAction(String pLogin) {
        super();
        userLogin = pLogin;
    }

    public String getLogin() {
        return userLogin;
    }

    public void setLogin(String pLogin) {
        userLogin = pLogin;
    }

}
