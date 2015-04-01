/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.popup.message;

import org.topcased.gpm.ui.application.client.popup.PopupDisplay;

import com.google.gwt.event.dom.client.HasClickHandlers;

/**
 * Display interface for the InfoMessageView.
 * 
 * @author tpanuel
 */
public interface InfoMessageDisplay extends PopupDisplay {
    /**
     * Set the info message.
     * 
     * @param pInfoMessage
     *            The info message.
     */
    public void setInfoMessage(String pInfoMessage);

    /**
     * Get the OK button.
     * 
     * @return The OK button.
     */
    public HasClickHandlers getOkButton();
}