/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.command.popup.user;

import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.ui.application.client.Application;
import org.topcased.gpm.ui.application.client.admin.user.detail.UserEditionDetailPresenter;
import org.topcased.gpm.ui.application.client.command.AbstractCommand;
import org.topcased.gpm.ui.application.client.command.popup.ClosePopupAction;
import org.topcased.gpm.ui.application.client.event.GlobalEvent;
import org.topcased.gpm.ui.application.client.popup.user.UserAffectationPresenter;
import org.topcased.gpm.ui.application.shared.command.user.UpdateAffectationAction;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.inject.Inject;

/**
 * A command to update the user affectation.
 * 
 * @author nveillet
 */
public class UpdateAffectationCommand extends AbstractCommand implements
        ClickHandler {

    /**
     * Create an UpdateAffectationCommand.
     * 
     * @param pEventBus
     *            The bus event.
     */
    @Inject
    public UpdateAffectationCommand(EventBus pEventBus) {
        super(pEventBus);
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.event.dom.client.ClickHandler#onClick(com.google.gwt.event.dom.client.ClickEvent)
     */
    @Override
    public void onClick(ClickEvent pEvent) {
        final UserEditionDetailPresenter lEditionPresenter =
                Application.INJECTOR.getAdminPresenter().getUserAdmin().getEditionDetail();
        final UserAffectationPresenter lUserAffectationPresenter =
                getPopupManager().getUserAffectationPresenter();

        fireEvent(GlobalEvent.LOAD_USER.getType(), new UpdateAffectationAction(
                lEditionPresenter.getUser().getLogin(),
                lUserAffectationPresenter.getAffectation()),
                GlobalEvent.CLOSE_USER_AFFECTATION_POPUP.getType(),
                new ClosePopupAction());
    }
}
