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

import java.util.List;

import org.topcased.gpm.ui.application.client.popup.PopupDisplay;

import com.google.gwt.event.dom.client.HasClickHandlers;

/**
 * Display interface for the ErrorMessageView.
 * 
 * @author tpanuel
 */
public interface ErrorMessageDisplay extends PopupDisplay {
    /**
     * Set the error message.
     * 
     * @param pErrorMessage
     *            The error message.
     */
    public void setErrorMessage(String pErrorMessage);

    /**
     * Set the stack trace.
     * 
     * @param pStackTrace
     *            The stack trace.
     */
    public void setStackTrace(List<String> pStackTrace);

    /**
     * Get the OK button.
     * 
     * @return The OK button.
     */
    public HasClickHandlers getOkButton();
}