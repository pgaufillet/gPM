/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.admin;

import net.customware.gwt.presenter.client.widget.WidgetDisplay;

import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.Widget;

/**
 * Display interface for the AdminView.
 * 
 * @author tpanuel
 */
public interface AdminDisplay extends WidgetDisplay {
    /**
     * Set the tabs contains by the administration panel.
     * 
     * @param pProductPanel
     *            The panel for product management.
     * @param pUserPanel
     *            The panel for user management.
     * @param pDicoPanel
     *            The panel for dictionary management.
     */
    public void setTabs(Widget pProductPanel, Widget pUserPanel,
            Widget pDicoPanel);

    /**
     * Add handler when changing tab
     * 
     * @param pHandler
     *            the handler to add
     */
    public void addTabChangeHandler(SelectionHandler<Integer> pHandler);
}