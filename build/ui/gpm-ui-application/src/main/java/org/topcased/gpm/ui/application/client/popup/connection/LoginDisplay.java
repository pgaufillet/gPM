/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.popup.connection;

import org.topcased.gpm.ui.application.client.popup.PopupDisplay;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.HasValue;

/**
 * AuthenticationDisplay <h4>Provides</h4>
 * <ul>
 * <li>Login value</li>
 * <li>Password value</li>
 * <li>Validation click handler</li>
 * <li>Setting the login error message.</li>
 * </ul>
 * 
 * @author mkargbo
 */
public interface LoginDisplay extends PopupDisplay {
    /**
     * Get login value
     * 
     * @return Value of the login
     */
    public HasValue<String> getLogin();

    /**
     * Get password value
     * 
     * @return Value of the password
     */
    public HasValue<String> getPassword();

    /**
     * Get validate click handle
     * 
     * @return Handler of the validate button
     */
    public HasClickHandlers getAuthenticationButton();

    /**
     * Display the error message.
     */
    public void displayLoginError();

    /**
     * Hide the error message
     */
    public void hideLoginError();
}