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

import static org.topcased.gpm.ui.component.client.resources.i18n.Ui18n.MESSAGES;
import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.business.util.FilterType;
import org.topcased.gpm.ui.application.client.command.AbstractCommand;
import org.topcased.gpm.ui.application.client.event.LocalEvent;
import org.topcased.gpm.ui.application.shared.command.filter.DeleteFilterAction;

import com.google.gwt.user.client.Command;
import com.google.inject.Inject;

/**
 * A command to delete a tree filter.
 * 
 * @author tpanuel
 */
public class DeleteSheetTreeFilterCommand extends AbstractCommand implements
        Command {
    /**
     * Create a DeleteTreeFilterCommand.
     * 
     * @param pEventBus
     *            The bus event.
     */
    @Inject
    public DeleteSheetTreeFilterCommand(final EventBus pEventBus) {
        super(pEventBus);
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.client.Command#execute()
     */
    @Override
    public void execute() {
        final String lProductName = getCurrentProductWorkspaceName();
        final String lFilterId = getCurrentSheetTreeFilterId();

        fireEvent(LocalEvent.DELETE_SHEET_FILTER.getType(lProductName),
                new DeleteFilterAction(lProductName, FilterType.SHEET,
                        lFilterId), MESSAGES.confirmationFilterDeletion());
    }
}