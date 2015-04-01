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
import org.topcased.gpm.ui.application.client.event.EmptyAction;
import org.topcased.gpm.ui.application.client.event.GlobalEvent;

import com.google.gwt.user.client.Command;
import com.google.inject.Inject;

/**
 * A command to open a user in creation mode.
 * 
 * @author nveillet
 */
public class OpenUserOnCreationCommand extends AbstractCommand implements
        Command {

    /**
     * Create an OpenUserOnCreationCommand.
     * 
     * @param pEventBus
     *            The bus event.
     */
    @Inject
    public OpenUserOnCreationCommand(EventBus pEventBus) {
        super(pEventBus);
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.client.Command#execute()
     */
    @SuppressWarnings("rawtypes")
	@Override
    public void execute() {
        fireEvent(GlobalEvent.LOAD_NEW_USER.getType(),
                new EmptyAction<EmptyAction>());
    }
}
