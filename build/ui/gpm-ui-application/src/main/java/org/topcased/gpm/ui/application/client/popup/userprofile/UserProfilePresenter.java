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

import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.ui.application.client.command.popup.ClosePopupAction;
import org.topcased.gpm.ui.application.client.command.popup.userprofile.UpdateUserProfileCommand;
import org.topcased.gpm.ui.application.client.event.GlobalEvent;
import org.topcased.gpm.ui.application.client.popup.PopupPresenter;
import org.topcased.gpm.ui.facade.shared.administration.user.UiUser;

import com.google.inject.Inject;

/**
 * The presenter for the UserProfileView.
 * 
 * @author jeballar
 */
public class UserProfilePresenter extends PopupPresenter<UserProfileDisplay> {

    private final UpdateUserProfileCommand updateUser;

    private String language;

    private boolean editable = true;

    /**
     * Create a presenter for the UserProfileView.
     * 
     * @param pDisplay
     *            The display interface.
     * @param pEventBus
     *            The event bus.
     * @param pUserUpdate
     *            The update user command.
     */
    @Inject
    public UserProfilePresenter(UserProfileDisplay pDisplay,
            EventBus pEventBus, UpdateUserProfileCommand pUserUpdate) {
        super(pDisplay, pEventBus);
        updateUser = pUserUpdate;
    }

    /**
     * Get the user
     * 
     * @return the user
     */
    public UiUser getUser() {
        return getDisplay().getUser();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.PopupPresenter#getClosePopupEvent()
     */
    @Override
    protected GlobalEvent<ClosePopupAction> getClosePopupEvent() {
        return GlobalEvent.CLOSE_USER_PROFILE_POPUP;
    }

    /**
     * Initialize the popup form.
     * 
     * @param pUser
     *            The user.
     * @param pEditable
     *            If the user can edit its profile or not
     * @param pEditablePassword
     *            If the user can edit its password
     * @param pLanguages
     *            List of languages the user can select
     */
    public void init(final UiUser pUser, boolean pEditable,
            boolean pEditablePassword, List<String> pLanguages) {
        getDisplay().reset();
        getDisplay().setEditable(pEditable);
        getDisplay().setAvailableLanguages(pLanguages);
        getDisplay().setUser(pUser);
        editable = pEditable;
        language = pUser.getLanguage();
        // Set the save button handler
        getDisplay().setValidateButtonHandler(updateUser);
        getDisplay().showPasswordArea(pEditablePassword);
        getDisplay().center();
    }

    /**
     * Indicate if the language in argument is different from the previous user
     * language
     * 
     * @param pLanguage
     *            language to check
     * @return true if the language has changed, else false
     */
    public boolean userChangedLanguage(String pLanguage) {
        return !language.equals(pLanguage);
    }

    /**
     * Indicate if the user has the right to edit its profile or not
     * 
     * @return true if the user has the right to edit its profile, else false
     */
    public boolean isEditable() {
        return editable;
    }

    /**
     * Validate the fields of the Display
     * 
     * @return A non-null message if an error was detected
     */
    public String validate() {
        return getDisplay().validate();
    }
}
