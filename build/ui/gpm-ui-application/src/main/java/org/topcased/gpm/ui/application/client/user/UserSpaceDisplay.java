/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.user;

import org.topcased.gpm.ui.application.client.common.tab.TabLayoutDisplay;

import com.google.gwt.event.dom.client.HasClickHandlers;

/**
 * Display interface for the UserSpaceView.
 * 
 * @author tpanuel
 */
public interface UserSpaceDisplay extends
        TabLayoutDisplay<ProductWorkspaceDisplay> {
    /**
     * Get button to add tab : temporary for test.
     * 
     * @return The click handler.
     */
    public HasClickHandlers getAddTabButton();
}