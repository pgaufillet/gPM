/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.command.user.sheets;

import static org.topcased.gpm.ui.component.client.resources.i18n.Ui18n.MESSAGES;
import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.ui.application.client.command.AbstractCommand;
import org.topcased.gpm.ui.application.client.command.CloseTabAction;
import org.topcased.gpm.ui.application.client.command.validation.SheetListingViewValidator;
import org.topcased.gpm.ui.application.client.event.ActionEvent;
import org.topcased.gpm.ui.application.client.event.LocalEvent;
import org.topcased.gpm.ui.application.client.util.CollectionUtil;
import org.topcased.gpm.ui.application.shared.command.sheet.DeleteSheetsAction;

import com.google.gwt.user.client.Command;
import com.google.inject.Inject;

/**
 * A command to delete a list of sheets selected on the listing view.
 * 
 * @author tpanuel
 */
public class DeleteSheetsCommand extends AbstractCommand implements Command {
    /**
     * Create an DeleteSheetsCommand.
     * 
     * @param pEventBus
     *            The bus event.
     */
    @Inject
    public DeleteSheetsCommand(final EventBus pEventBus) {
        super(pEventBus);
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.client.Command#execute()
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void execute() {
        // The filter is executed again because sheets have been deleted
        final ActionEvent lMainAction =
                new ActionEvent(
                        LocalEvent.EXECUTE_SHEET_TABLE_FILTER.getType(getCurrentProductWorkspaceName()),
                        new DeleteSheetsAction(
                                getCurrentProductWorkspaceName(),
                                getCurrentSheetIds(),
                                getCurrentSheetTableFilterId()),
                        CollectionUtil.singleton(SheetListingViewValidator.getInstance()));
        ActionEvent lPreviousAction = lMainAction;

        // Close all the selected sheet on a same
        for (String lDeletedSheetId : getCurrentSheetIds()) {
            final ActionEvent lCloseAction =
                    new ActionEvent(
                            LocalEvent.CLOSE_SHEET.getType(getCurrentProductWorkspaceName()),
                            new CloseTabAction(lDeletedSheetId, false));

            lPreviousAction.setNextEvent(lCloseAction);
            lPreviousAction = lCloseAction;
        }

        // A confirmation is need
        lMainAction.setConfirmationMessage(MESSAGES.confirmationSheetDeletion(getCurrentSheetIds().size()));
        eventBus.fireEvent(lMainAction);

    }
}