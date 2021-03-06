/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.command.popup.filter;

import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.ui.application.client.command.AbstractCommand;
import org.topcased.gpm.ui.application.client.command.popup.ClosePopupAction;
import org.topcased.gpm.ui.application.client.command.validation.FilterPopupViewValidator;
import org.topcased.gpm.ui.application.client.event.GlobalEvent;
import org.topcased.gpm.ui.application.client.popup.filter.FilterPopupPresenter;
import org.topcased.gpm.ui.application.shared.command.link.DeleteSheetLinkAction;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.inject.Inject;

/**
 * A command to delete links on a sheet from the filter popup.
 * 
 * @author tpanuel
 */
public class DeleteSheetLinkPopupCommand extends AbstractCommand implements
        ClickHandler {
    /**
     * Create an DeleteSheetLinkPopupCommand.
     * 
     * @param pEventBus
     *            The bus event.
     */
    @Inject
    public DeleteSheetLinkPopupCommand(final EventBus pEventBus) {
        super(pEventBus);
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.event.dom.client.ClickHandler#onClick(com.google.gwt.event.dom.client.ClickEvent)
     */
    @Override
    public void onClick(final ClickEvent pEvent) {
        final FilterPopupPresenter lFilterPopupPresenter =
                getPopupManager().getFilterPopupPresenter();
        final String lProductName = getCurrentProductWorkspaceName();

        fireEvent(GlobalEvent.LOAD_SHEET.getType(), new DeleteSheetLinkAction(
                lProductName, lFilterPopupPresenter.getTypeName(),
                getCurrentSheetId(),
                lFilterPopupPresenter.getSelectedElementIds()),
                FilterPopupViewValidator.getInstance(),
                GlobalEvent.CLOSE_FILTER_POPUP.getType(),
                new ClosePopupAction(),
                "Are you sure you want to delete this(these) sheet link(s) ?");
    }
}