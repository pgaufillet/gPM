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

import org.topcased.gpm.ui.facade.shared.administration.user.UiUser;
import org.topcased.gpm.ui.facade.shared.administration.user.UiUserAffectation;
import org.topcased.gpm.ui.facade.shared.administration.user.UiUserLists;

/**
 * Get user for management result.
 * 
 * @author jlouisy
 */
public class GetUserResult extends AbstractUserResult {

    private static final long serialVersionUID = -2996829566735222575L;

    private UiUser user;

    private UiUserAffectation affectation;

    /**
     * Empty constructor for serialization.
     */
    public GetUserResult() {
    }

    /**
     * Create GetUserResult with values.
     * 
     * @param pUserLists
     *            the users lists.
     * @param pUser
     *            the user.
     * @param pAffectation
     *            the affectation.
     */
    public GetUserResult(UiUserLists pUserLists, UiUser pUser,
            UiUserAffectation pAffectation) {
        super(pUserLists);
        user = pUser;
        affectation = pAffectation;
    }

    public UiUser getUser() {
        return user;
    }

    public UiUserAffectation getAffectation() {
        return affectation;
    }

}
