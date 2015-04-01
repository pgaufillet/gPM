/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.common.tab;

import net.customware.gwt.presenter.client.widget.WidgetDisplay;

import org.topcased.gpm.ui.component.client.layout.IResizableLayoutPanel;

import com.google.gwt.event.dom.client.ClickHandler;

/**
 * Display interface for the tab layout.
 * 
 * @author tpanuel
 * @param <T>
 *            The type of tab element display.
 */
public interface TabLayoutDisplay<T extends TabElementDisplay> extends
        WidgetDisplay, IResizableLayoutPanel {
    /**
     * Add a tab.
     * 
     * @param pTabDisplay
     *            the tab to add.
     * @param pCloseHandler
     *            The handler that close the tab.
     */
    public void addTab(T pTabDisplay, ClickHandler pCloseHandler);

    /**
     * Remove a tab.
     * 
     * @param pTabId
     *            The tab id.
     */
    public void removeTab(String pTabId);

    /**
     * Select a tab.
     * 
     * @param pTabId
     *            The tab id.
     */
    public void selectTab(String pTabId);

    /**
     * Returns the currently displayed tab @return The currently displayed tab
     */
    public T getCurrentTab();
}