/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.admin.user;

import org.topcased.gpm.ui.application.client.admin.user.listing.UserListingDisplay;
import org.topcased.gpm.ui.application.client.common.workspace.WorkspaceDisplay;
import org.topcased.gpm.ui.component.client.layout.IResizableLayoutPanel;

/**
 * Display interface for the UserAdminView.
 * 
 * @author tpanuel
 */
public interface UserAdminDisplay extends WorkspaceDisplay {

    /**
     * Set the two sub views.
     * 
     * @param pUserListingDisplay
     *            The listing view.
     * @param pUserDetailDisplay
     *            The detail view.
     */
    public void setContent(UserListingDisplay pUserListingDisplay,
            IResizableLayoutPanel pUserDetailDisplay);

    /**
     * Set the detail sub views.
     * 
     * @param pUserDetailDisplay
     *            The detail view.
     */
    public void setDetailContent(IResizableLayoutPanel pUserDetailDisplay);
}