/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael KARGBO (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.command.connection;

import org.topcased.gpm.ui.application.client.event.EmptyAction;
import org.topcased.gpm.ui.application.shared.command.authorization.ConnectResult;

/**
 * Launch a workspace
 * 
 * @author mkargbo
 */
public class LaunchWorkspaceAction extends EmptyAction<LaunchWorkspaceAction> {

    /** serialVersionUID */
    private static final long serialVersionUID = -4839729466523229743L;

    private ConnectResult connectResult;

    /**
     * Default constructor
     */
    public LaunchWorkspaceAction() {

    }

    /**
     * Constructor with information to display
     * 
     * @param pConnectResult
     *            Connection informations
     */
    public LaunchWorkspaceAction(ConnectResult pConnectResult) {
        connectResult = pConnectResult;
    }

    public ConnectResult getConnectResult() {
        return connectResult;
    }

    public void setConnectResult(ConnectResult pConnectResult) {
        connectResult = pConnectResult;
    }

}
