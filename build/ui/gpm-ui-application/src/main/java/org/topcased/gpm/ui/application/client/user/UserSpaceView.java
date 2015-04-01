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

import org.topcased.gpm.ui.application.client.common.tab.TabLayoutView;

/**
 * View for the user space that contain a set of product workspace.
 * 
 * @author tpanuel
 */
public class UserSpaceView extends TabLayoutView<ProductWorkspaceDisplay>
        implements UserSpaceDisplay {
    /**
     * Create an user space view.
     */
    public UserSpaceView() {
        super(true);
    }
}