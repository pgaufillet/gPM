/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.admin.dictionary;

import org.topcased.gpm.ui.application.client.admin.dictionary.navigation.DictionaryNavigationDisplay;
import org.topcased.gpm.ui.application.client.common.workspace.WorkspaceDisplay;
import org.topcased.gpm.ui.component.client.layout.IResizableLayoutPanel;

/**
 * Display interface for the DictionaryAdminView.
 * 
 * @author tpanuel
 */
public interface DictionaryAdminDisplay extends WorkspaceDisplay {

    /**
     * Set the two sub views.
     * 
     * @param pDictionaryNavigationDisplay
     *            The listing view.
     * @param pListingDisplay
     *            The listing view.
     * @param pDetailDisplay
     *            The detail view.
     */
    public void setContent(
            DictionaryNavigationDisplay pDictionaryNavigationDisplay,
            IResizableLayoutPanel pListingDisplay,
            IResizableLayoutPanel pDetailDisplay);

    /**
     * Set the detail sub view.
     * 
     * @param pDetailDisplay
     *            The detail view.
     */
    public void setDetailContent(IResizableLayoutPanel pDetailDisplay);
}