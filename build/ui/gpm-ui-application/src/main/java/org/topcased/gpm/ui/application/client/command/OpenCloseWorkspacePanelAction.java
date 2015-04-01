/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.command;

import org.topcased.gpm.ui.application.client.common.workspace.WorkspacePanelAction;
import org.topcased.gpm.ui.application.client.common.workspace.WorkspacePanelType;
import org.topcased.gpm.ui.application.client.event.EmptyAction;

/**
 * Empty action for open or close a panel of a workspace.
 * 
 * @author tpanuel
 */
public class OpenCloseWorkspacePanelAction extends
        EmptyAction<OpenCloseWorkspacePanelAction> {
    private static final long serialVersionUID = 6706201541037938074L;

    private final WorkspacePanelType panelType;

    private final WorkspacePanelAction panelStatus;

    /**
     * Create an OpenCloseWorkspacePanelAction.
     * 
     * @param pPanelType
     *            The panel type.
     * @param pPanelStatus
     *            The panel status.
     */
    public OpenCloseWorkspacePanelAction(final WorkspacePanelType pPanelType,
            final WorkspacePanelAction pPanelStatus) {
        panelType = pPanelType;
        panelStatus = pPanelStatus;
    }

    /**
     * Get the panel type.
     * 
     * @return The panel type.
     */
    public WorkspacePanelType getPanelType() {
        return panelType;
    }

    /**
     * Get the panel status.
     * 
     * @return The panel status.
     */
    public WorkspacePanelAction getPanelStatus() {
        return panelStatus;
    }
}