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

import org.topcased.gpm.ui.facade.shared.administration.user.UiUserLists;

/**
 * Delete user result.
 * 
 * @author jlouisy
 */
public class DeleteUserResult extends AbstractUserResult {

    private static final long serialVersionUID = -2996829566735222575L;

    /**
     * Empty constructor for serialization.
     */
    public DeleteUserResult() {
    }

    /**
     * Create DeleteUserResult with values.
     * 
     * @param pUserLists
     *            the users lists.
     */
    public DeleteUserResult(UiUserLists pUserLists) {
        super(pUserLists);
    }
}
