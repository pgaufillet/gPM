/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien EBALLARD (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.command.popup.userprofile;

import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.ui.application.client.Application;
import org.topcased.gpm.ui.application.client.command.AbstractCommand;
import org.topcased.gpm.ui.application.client.command.popup.ClosePopupAction;
import org.topcased.gpm.ui.application.client.command.validation.UpdateUserProfileViewValidator;
import org.topcased.gpm.ui.application.client.event.GlobalEvent;
import org.topcased.gpm.ui.application.client.popup.userprofile.UserProfilePresenter;
import org.topcased.gpm.ui.application.shared.command.user.UpdateUserProfileAction;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.inject.Inject;

/**
 * A command to update the user affectation.
 * 
 * @author jeballar
 */
public class UpdateUserProfileCommand extends AbstractCommand implements
        ClickHandler {

    /**
     * Create an UpdateAffectationCommand.
     * 
     * @param pEventBus
     *            The bus event.
     */
    @Inject
    public UpdateUserProfileCommand(EventBus pEventBus) {
        super(pEventBus);
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.event.dom.client.ClickHandler#onClick(com.google.gwt.event.dom.client.ClickEvent)
     */
    @Override
    public void onClick(ClickEvent pEvent) {
        final UserProfilePresenter lPresenter =
                Application.INJECTOR.getUserProfilePresenter();

        if (lPresenter.isEditable()) {
            fireEvent(GlobalEvent.SAVE_USER_PROFILE.getType(),
                    new UpdateUserProfileAction(lPresenter.getUser()),
                    UpdateUserProfileViewValidator.getInstance(),
                    GlobalEvent.CLOSE_USER_PROFILE_POPUP.getType(),
                    new ClosePopupAction());
        }
        else {
            fireEvent(GlobalEvent.CLOSE_USER_PROFILE_POPUP.getType(),
                    new ClosePopupAction());
        }
    }
}
