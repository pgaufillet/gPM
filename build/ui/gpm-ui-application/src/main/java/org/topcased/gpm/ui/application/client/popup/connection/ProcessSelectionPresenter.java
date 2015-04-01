/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.popup.connection;

import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.ui.application.client.command.connection.SelectProcessCommand;
import org.topcased.gpm.ui.application.client.command.popup.ClosePopupAction;
import org.topcased.gpm.ui.application.client.event.GlobalEvent;
import org.topcased.gpm.ui.application.client.popup.PopupPresenter;
import org.topcased.gpm.ui.application.shared.command.authorization.SelectProcessResult;

import com.google.inject.Inject;

/**
 * The presenter for the ProcessSelectionView.
 * 
 * @author nveillet
 */
public class ProcessSelectionPresenter extends
        PopupPresenter<ProcessSelectionDisplay> {

    private final SelectProcessCommand selectProcessCommand;

    /**
     * Create a presenter for the ProcessSelectionView.
     * 
     * @param pDisplay
     *            The display interface.
     * @param pEventBus
     *            The event bus.
     * @param pSelectProcessCommand
     *            The select process command.
     */
    @Inject
    public ProcessSelectionPresenter(final ProcessSelectionDisplay pDisplay,
            final EventBus pEventBus,
            final SelectProcessCommand pSelectProcessCommand) {
        super(pDisplay, pEventBus);
        selectProcessCommand = pSelectProcessCommand;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.PopupPresenter#getClosePopupEvent()
     */
    @Override
    protected GlobalEvent<ClosePopupAction> getClosePopupEvent() {
        return GlobalEvent.CLOSE_SELECT_PROCESS_POPUP;
    }

    /**
     * Initialize the popup
     * 
     * @param pResult
     *            The select process result.
     */
    public void init(final SelectProcessResult pResult) {
        getDisplay().clear();
        // Set available process
        getDisplay().setProcess(pResult.getProcessNames());
        // Set the select button handler
        getDisplay().setSelectButtonHandler(selectProcessCommand);
    }
}
