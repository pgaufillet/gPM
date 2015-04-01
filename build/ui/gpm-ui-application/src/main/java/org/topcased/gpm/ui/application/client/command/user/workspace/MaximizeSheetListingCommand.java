/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.command.user.workspace;

import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.ui.application.client.command.AbstractCommand;
import org.topcased.gpm.ui.application.client.command.OpenCloseWorkspacePanelAction;
import org.topcased.gpm.ui.application.client.common.workspace.WorkspacePanelAction;
import org.topcased.gpm.ui.application.client.common.workspace.WorkspacePanelType;
import org.topcased.gpm.ui.application.client.event.LocalEvent;

import com.google.gwt.user.client.Command;
import com.google.inject.Inject;

/**
 * A command to maximize the listing panel.
 * 
 * @author tpanuel
 */
public class MaximizeSheetListingCommand extends AbstractCommand implements
        Command {
    /**
     * Create an MaximizeListingCommand.
     * 
     * @param pEventBus
     *            The event bus.
     */
    @Inject
    public MaximizeSheetListingCommand(final EventBus pEventBus) {
        super(pEventBus);
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.client.Command#execute()
     */
    @Override
    public void execute() {
        fireEvent(
                LocalEvent.OPEN_CLOSE_SHEET_WORKSPACE.getType(getCurrentProductWorkspaceName()),
                new OpenCloseWorkspacePanelAction(WorkspacePanelType.LISTING,
                        WorkspacePanelAction.MAXIMIZE_RESTORE));
    }
}