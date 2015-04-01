/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.user.detail;

import org.topcased.gpm.ui.application.client.common.container.sheet.SheetDisplay;
import org.topcased.gpm.ui.application.client.common.workspace.detail.DetailView;

/**
 * View for a set of sheets displayed on the user space.
 * 
 * @author tpanuel
 */
public class SheetDetailView extends DetailView<SheetDisplay> implements
        SheetDetailDisplay {
    /**
     * Create a detail view for sheets.
     */
    public SheetDetailView() {
        super();
    }
}