/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien EBALLARD (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.popup.userprofile;

import java.util.List;

import org.topcased.gpm.ui.application.client.popup.PopupDisplay;
import org.topcased.gpm.ui.facade.shared.administration.user.UiUser;

import com.google.gwt.event.dom.client.ClickHandler;

/**
 * Display interface for the UserAffectationView.
 * 
 * @author jeballar
 */
public interface UserProfileDisplay extends PopupDisplay {

    /**
     * Get the user
     * 
     * @return the user
     */
    public UiUser getUser();

    /**
     * Set the user
     * 
     * @param pUser
     *            the user
     */
    public void setUser(UiUser pUser);

    /**
     * Set the save button handler.
     * 
     * @param pHandler
     *            The handler.
     */
    public void setValidateButtonHandler(ClickHandler pHandler);

    /**
     * The UI displays fields or only text for the values if the use is able to
     * edit its profile or not
     * 
     * @param pEditable
     *            true if the user must be able to edit its profile, else false
     */
    public void setEditable(boolean pEditable);

    /**
     * Set the list of available languages for the user to select
     * 
     * @param pLanguages
     *            list of available languages
     */
    public void setAvailableLanguages(List<String> pLanguages);

    /**
     * Clear the fields of the popup
     */
    public void reset();

    /**
     * Show the password area or not (true by default)
     * 
     * @param pEditablePassword
     *            true to show the area, else false
     */
    public void showPasswordArea(boolean pEditablePassword);

    /**
     * Validate the fields of the Display
     * 
     * @return A non-null message if an error was detected
     */
    public String validate();
}
