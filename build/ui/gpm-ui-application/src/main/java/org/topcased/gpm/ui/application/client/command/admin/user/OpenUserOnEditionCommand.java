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

import org.topcased.gpm.ui.application.client.command.AbstractCommand;
import org.topcased.gpm.ui.application.client.event.GlobalEvent;
import org.topcased.gpm.ui.application.shared.command.user.GetUserAction;
import org.topcased.gpm.ui.component.client.button.GpmTextButton;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.inject.Inject;

/**
 * A command to open a user on edition mode.
 * 
 * @author nveillet
 */
public class OpenUserOnEditionCommand extends AbstractCommand implements
        ClickHandler {

    /**
     * Create an OpenUserOnEditionCommand.
     * 
     * @param pEventBus
     *            The bus event.
     */
    @Inject
    public OpenUserOnEditionCommand(EventBus pEventBus) {
        super(pEventBus);
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.event.dom.client.ClickHandler#onClick(com.google.gwt.event.dom.client.ClickEvent)
     */
    @Override
    public void onClick(ClickEvent pEvent) {
        String lLogin = ((GpmTextButton) pEvent.getSource()).getId();

        fireEvent(GlobalEvent.LOAD_USER.getType(), new GetUserAction(lLogin));
    }
}
