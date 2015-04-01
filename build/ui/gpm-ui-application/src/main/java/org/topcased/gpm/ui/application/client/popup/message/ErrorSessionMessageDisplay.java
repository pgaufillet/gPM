/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.popup.message;

import org.topcased.gpm.ui.application.client.popup.PopupDisplay;

import com.google.gwt.event.dom.client.HasClickHandlers;

/**
 * Display interface for the ErrorSessionMessageView.
 * 
 * @author nveillet
 */
public interface ErrorSessionMessageDisplay extends PopupDisplay {

    /**
     * Get the OK button.
     * 
     * @return The OK button.
     */
    public HasClickHandlers getLogoutButton();

    /**
     * Get the Cancel button.
     * 
     * @return The Cancel button.
     */
    public HasClickHandlers getCancelButton();
}