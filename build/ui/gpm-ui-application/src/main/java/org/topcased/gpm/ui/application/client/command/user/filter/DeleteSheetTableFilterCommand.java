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
import org.topcased.gpm.ui.component.client.button.GpmImageButton;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.inject.Inject;

/**
 * A command to delete a filter.
 * 
 * @author tpanuel
 */
public class DeleteSheetTableFilterCommand extends AbstractCommand implements
        ClickHandler {
    /**
     * Create a DeleteFilterCommand.
     * 
     * @param pEventBus
     *            The bus event.
     */
    @Inject
    public DeleteSheetTableFilterCommand(final EventBus pEventBus) {
        super(pEventBus);
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.event.dom.client.ClickHandler#onClick(com.google.gwt.event.dom.client.ClickEvent)
     */
    @Override
    public void onClick(final ClickEvent pEvent) {
        final String lProductName = getCurrentProductWorkspaceName();
        final String lFilterId = ((GpmImageButton) pEvent.getSource()).getId();

        fireEvent(LocalEvent.DELETE_SHEET_FILTER.getType(lProductName),
                new DeleteFilterAction(lProductName, FilterType.SHEET,
                        lFilterId), MESSAGES.confirmationFilterDeletion());
    }
}