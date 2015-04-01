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

import net.customware.gwt.presenter.client.widget.WidgetDisplay;

import org.topcased.gpm.ui.component.client.button.GpmImageButton;
import org.topcased.gpm.ui.component.client.menu.GpmToolBar;

/**
 * Interface for workspace view.
 * 
 * @author tpanuel
 */
public interface WorkspaceDisplay extends WidgetDisplay {
    /**
     * Set the tool bar.
     * 
     * @param pToolBar
     *            The tool bar.
     */
    public void setToolBar(GpmToolBar pToolBar);

    /**
     * Set the three sub views minimized buttons.
     * 
     * @param pNavigationButton
     *            The navigation minimized button.
     * @param pListingButton
     *            The listing minimized button.
     * @param pDetailButton
     *            The detail minimized button.
     */
    public void setMinimizedContent(GpmImageButton pNavigationButton,
            GpmImageButton pListingButton, GpmImageButton pDetailButton);

    /**
     * Maximize or restore the listing panel depending on its status
     */
    public void maximizeOrRestoreListingPanel();

    /**
     * Maximize or restore the detail panel depending on its status
     */
    public void maximizeOrRestoreDetailPanel();

    /**
     * Minimize navigation panel
     */
    public void minimizeNavigationPanel();

    /**
     * Minimize the listing panel
     */
    public void minimizeListingPanel();

    /**
     * Minimize the detail panel
     */
    public void minimizeDetailPanel();

    /**
     * Restore navigation panel
     */
    public void restoreNavigationPanel();

    /**
     * Restore the listing panel
     */
    public void restoreListingPanel();

    /**
     * Restore the detail panel
     */
    public void restoreDetailPanel();

    /**
     * Completly hide the detail panel
     */
    public void hideDetailPanel();

    /**
     * Completly show the detail panel
     */
    public void showDetailPanel();

    /**
     * Make the detail panel visible (if minimized, expand, if hidden, show)
     */
    public void makeDetailPanelVisible();
}