/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.command.user.sheet;

import static org.topcased.gpm.ui.component.client.resources.i18n.Ui18n.MESSAGES;
import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.ui.application.client.command.AbstractCommand;
import org.topcased.gpm.ui.application.client.command.CloseTabAction;
import org.topcased.gpm.ui.application.client.event.LocalEvent;
import org.topcased.gpm.ui.application.shared.command.sheet.DeleteSheetAction;

import com.google.gwt.user.client.Command;
import com.google.inject.Inject;

/**
 * A command to delete a sheet.
 * 
 * @author tpanuel
 */
public class DeleteSheetCommand extends AbstractCommand implements Command {
    /**
     * Create an DeleteSheetCommand.
     * 
     * @param pEventBus
     *            The bus event.
     */
    @Inject
    public DeleteSheetCommand(final EventBus pEventBus) {
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
                LocalEvent.DELETE_SHEET.getType(getCurrentProductWorkspaceName()),
                new DeleteSheetAction(getCurrentProductWorkspaceName(),
                        getCurrentSheetId()),
                LocalEvent.CLOSE_SHEET.getType(getCurrentProductWorkspaceName()),
                new CloseTabAction(getCurrentSheetId(), false),
                MESSAGES.confirmationSheetDeletion(1));
    }
}
