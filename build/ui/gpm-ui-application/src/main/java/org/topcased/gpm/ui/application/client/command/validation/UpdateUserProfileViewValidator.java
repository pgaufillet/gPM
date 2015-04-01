/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien EBALLARD (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.command.validation;

import net.customware.gwt.dispatch.shared.Action;

import org.topcased.gpm.ui.application.client.Application;
import org.topcased.gpm.ui.application.client.popup.userprofile.UserProfilePresenter;

/**
 * Validate the user edition detail view : validate all the fields.
 * 
 * @author jeballar
 */
public final class UpdateUserProfileViewValidator implements ViewValidator {

    private final static UpdateUserProfileViewValidator INSTANCE =
            new UpdateUserProfileViewValidator();

    /**
     * Private constructor for singleton.
     */
    private UpdateUserProfileViewValidator() {

    }

    /**
     * Get the single instance.
     * 
     * @return The single instance.
     */
    public final static UpdateUserProfileViewValidator getInstance() {
        return INSTANCE;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.command.validation.ViewValidator#validate(net.customware.gwt.dispatch.shared.Action)
     */
    @Override
    public String validate(final Action<?> pAction) {
        UserProfilePresenter lUserProfilePresenter =
                Application.INJECTOR.getUserProfilePresenter();

        final StringBuilder lValidMessage = new StringBuilder();

        String lMessage = lUserProfilePresenter.validate();
        if (lMessage != null && !lMessage.isEmpty()) {
            lValidMessage.append(lMessage);
        }

        return lValidMessage.toString().trim();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.command.validation.ViewValidator#isError()
     */
    @Override
    public boolean isError() {
        return true;
    }
}