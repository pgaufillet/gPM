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

/**
 * Get user for management action.
 * 
 * @author jeballar
 */
public class GetUserProfileAction extends
        AbstractCommandAction<GetUserProfileResult> {

    private static final long serialVersionUID = 169010420331415491L;

    /**
     * create action
     */
    public GetUserProfileAction() {
        super();
    }
}