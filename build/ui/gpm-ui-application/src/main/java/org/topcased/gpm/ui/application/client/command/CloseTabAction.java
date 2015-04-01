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

import org.topcased.gpm.ui.application.client.event.EmptyAction;

/**
 * The action that close a tab.
 * 
 * @author tpanuel
 */
public class CloseTabAction extends EmptyAction<CloseTabAction> {

    private static final long serialVersionUID = 5953761542016771823L;

    private final boolean sendServer;

    private final String tabId;

    /**
     * Create a close tab action.
     * 
     * @param pTabId
     *            The tab id.
     * @param pSendServer
     *            If information must be send to server
     */
    public CloseTabAction(final String pTabId, final boolean pSendServer) {
        tabId = pTabId;
        sendServer = pSendServer;
    }

    /**
     * Get the tab id.
     * 
     * @return The tab id.
     */
    public String getTabId() {
        return tabId;
    }

    /**
     * get if information must be send to server
     * 
     * @return If information must be send to server
     */
    public boolean isSendServer() {
        return sendServer;
    }
}