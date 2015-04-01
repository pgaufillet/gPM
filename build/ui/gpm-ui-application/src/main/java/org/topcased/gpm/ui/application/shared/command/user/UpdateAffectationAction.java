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
import org.topcased.gpm.ui.facade.shared.administration.user.UiUserAffectation;

/**
 * Update affectation action.
 * 
 * @author jlouisy
 */
public class UpdateAffectationAction extends
        AbstractCommandAction<GetUserResult> {

    private static final long serialVersionUID = 169010420331415491L;

    private String login;

    private UiUserAffectation affectation;

    /**
     * create action
     */
    public UpdateAffectationAction() {
        super();
    }

    /**
     * create action with values
     * 
     * @param pUserLogin
     *            login
     * @param pAffectation
     *            selected affectations.
     */
    public UpdateAffectationAction(String pUserLogin,
            UiUserAffectation pAffectation) {
        super();
        login = pUserLogin;
        affectation = pAffectation;
    }

    public UiUserAffectation getAffectation() {
        return affectation;
    }

    public String getLogin() {
        return login;
    }
}
