/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.command.user.filter;

import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.business.util.FilterType;
import org.topcased.gpm.ui.application.client.command.AbstractCommand;
import org.topcased.gpm.ui.application.client.event.GlobalEvent;
import org.topcased.gpm.ui.application.shared.command.filter.edit.SelectContainerAction;

import com.google.gwt.user.client.Command;
import com.google.inject.Inject;

/**
 * A command to create a new filter.
 * 
 * @author tpanuel
 */
public class AddSheetFilterCommand extends AbstractCommand implements Command {
    /**
     * Create an AddFilterCommand.
     * 
     * @param pEventBus
     *            The bus event.
     */
    @Inject
    public AddSheetFilterCommand(final EventBus pEventBus) {
        super(pEventBus);
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.client.Command#execute()
     */
    @Override
    public void execute() {
        fireEvent(GlobalEvent.NEW_SHEET_FILTER.getType(),
                new SelectContainerAction(getCurrentProductWorkspaceName(),
                        FilterType.SHEET));
    }
}