/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.command.admin.user;

import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.ui.application.client.Application;
import org.topcased.gpm.ui.application.client.admin.user.detail.UserEditionDetailPresenter;
import org.topcased.gpm.ui.application.client.command.AbstractCommand;
import org.topcased.gpm.ui.application.client.event.GlobalEvent;
import org.topcased.gpm.ui.application.shared.command.user.GetAffectationAction;
import org.topcased.gpm.ui.facade.shared.administration.user.UiUser;

import com.google.gwt.user.client.Command;
import com.google.inject.Inject;

/**
 * A command to get user affectation.
 * 
 * @author nveillet
 */
public class GetUserAffectationCommand extends AbstractCommand implements
        Command {

    /**
     * Create an OpenUserCommand.
     * 
     * @param pEventBus
     *            The bus event.
     */
    @Inject
    public GetUserAffectationCommand(EventBus pEventBus) {
        super(pEventBus);
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.client.Command#execute()
     */
    @Override
    public void execute() {
        UserEditionDetailPresenter lEditionPresenter =
                Application.INJECTOR.getAdminPresenter().getUserAdmin().getEditionDetail();
        UiUser lUser = lEditionPresenter.getUser();

        fireEvent(GlobalEvent.OPEN_USER_AFFECTATION_POPUP.getType(),
                new GetAffectationAction(lUser.getLogin()));
    }
}
