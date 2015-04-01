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
 * Get user affectation popup action.
 * 
 * @author jlouisy
 */
public class GetAffectationAction extends
        AbstractCommandAction<GetAffectationResult> {

    private static final long serialVersionUID = 169010420331415491L;

    private String login;

    /**
     * create action
     */
    public GetAffectationAction() {
        super();
    }

    /**
     * create action with values
     * 
     * @param pLogin
     *            the user login
     */
    public GetAffectationAction(String pLogin) {
        super();
        login = pLogin;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String pLogin) {
        login = pLogin;
    }

}
