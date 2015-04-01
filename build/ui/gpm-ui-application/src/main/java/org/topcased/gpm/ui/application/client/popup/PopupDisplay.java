/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.popup;

import net.customware.gwt.presenter.client.widget.WidgetDisplay;

import com.google.gwt.event.dom.client.ClickHandler;

/**
 * Display interface for the popup.
 * 
 * @author tpanuel
 */
public interface PopupDisplay extends WidgetDisplay {
    /**
     * Show and center the popup.
     */
    public void center();

    /**
     * Hide the popup.
     */
    public void hide();

    /**
     * Set the popup title.
     * 
     * @param pPopupTitle
     *            The popup title.
     */
    public void setHeaderText(final String pPopupTitle);

    /**
     * Test if the popup is shown.
     * 
     * @return If the popup is shown.
     */
    public boolean isShowing();

    /**
     * Add a click handler on the close button.
     * 
     * @param pHandler
     *            The click handler.
     */
    public void addCloseButtonHandler(final ClickHandler pHandler);
}