/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.popup.csv;

import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.ui.application.client.command.popup.ClosePopupAction;
import org.topcased.gpm.ui.application.client.command.popup.csv.LaunchCsvExportCommand;
import org.topcased.gpm.ui.application.client.event.GlobalEvent;
import org.topcased.gpm.ui.application.client.popup.PopupPresenter;

import com.google.inject.Inject;

/**
 * The presenter for the CsvOptionSelectionView.
 * 
 * @author tpanuel
 */
public class CsvOptionSelectionPresenter extends
        PopupPresenter<CsvOptionSelectionDisplay> {
    /**
     * Create a presenter for the CsvOptionSelectionView.
     * 
     * @param pDisplay
     *            The display interface.
     * @param pEventBus
     *            The event bus.
     * @param pExportCommand
     *            the export command
     */
    @Inject
    public CsvOptionSelectionPresenter(
            final CsvOptionSelectionDisplay pDisplay, final EventBus pEventBus,
            final LaunchCsvExportCommand pExportCommand) {
        super(pDisplay, pEventBus);
        getDisplay().addExportButtonHandler(pExportCommand);
    }

    /**
     * Reset the view.
     */
    public void reset() {
        getDisplay().reset();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.PopupPresenter#getClosePopupEvent()
     */
    @Override
    protected GlobalEvent<ClosePopupAction> getClosePopupEvent() {
        return GlobalEvent.CLOSE_CSV_POPUP;
    }
}