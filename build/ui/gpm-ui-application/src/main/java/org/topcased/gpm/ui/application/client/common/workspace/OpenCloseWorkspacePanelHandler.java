/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.common.workspace;

import org.topcased.gpm.ui.application.client.command.OpenCloseWorkspacePanelAction;
import org.topcased.gpm.ui.application.client.event.ActionEventHandler;

/**
 * Handler used to open or close workspace panel.
 * 
 * @author tpanuel
 */
public class OpenCloseWorkspacePanelHandler extends
        ActionEventHandler<OpenCloseWorkspacePanelAction> {
    private final WorkspaceDisplay display;

    /**
     * Create an OpenCloseWorkspacePanelHandler.
     * 
     * @param pDisplay
     *            The display interface.
     */
    public OpenCloseWorkspacePanelHandler(final WorkspaceDisplay pDisplay) {
        display = pDisplay;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.client.rpc.AsyncCallback#onSuccess(java.lang.Object)
     */
    @Override
    public void execute(final OpenCloseWorkspacePanelAction pResult) {
        switch (pResult.getPanelType()) {
            case NAVIGATION:
                switch (pResult.getPanelStatus()) {
                    case MINIMIZE:
                        display.minimizeNavigationPanel();
                        break;
                    case RESTORE:
                        display.restoreNavigationPanel();
                        break;
                    default:
                        // Do nothing
                }
                break;
            case LISTING:
                switch (pResult.getPanelStatus()) {
                    case MINIMIZE:
                        display.minimizeListingPanel();
                        break;
                    case MAXIMIZE_RESTORE:
                        display.maximizeOrRestoreListingPanel();
                        break;
                    case RESTORE:
                        display.restoreListingPanel();
                        break;
                    default:
                        // Do nothing
                }
                break;
            case DETAIL:
                switch (pResult.getPanelStatus()) {
                    case MINIMIZE:
                        display.minimizeDetailPanel();
                        break;
                    case MAXIMIZE_RESTORE:
                        display.maximizeOrRestoreDetailPanel();
                        break;
                    case RESTORE:
                        display.restoreDetailPanel();
                        break;
                    case HIDE:
                        display.hideDetailPanel();
                        break;
                    case SHOW:
                        display.showDetailPanel();
                        break;
                    case MAKEVISIBLE:
                        display.makeDetailPanelVisible();
                        break;
                    default:
                        // Do nothing
                }
                break;
            default:
                // Do nothing
        }
    }
}