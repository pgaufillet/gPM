/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.popup.mail;

import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.ui.application.client.command.popup.ClosePopupAction;
import org.topcased.gpm.ui.application.client.command.popup.mail.SendSheetByMailCommand;
import org.topcased.gpm.ui.application.client.command.popup.mail.SendSheetsByMailCommand;
import org.topcased.gpm.ui.application.client.event.GlobalEvent;
import org.topcased.gpm.ui.application.client.popup.PopupPresenter;
import org.topcased.gpm.ui.application.shared.command.mail.GetMailingInfoResult;

import com.google.inject.Inject;

/**
 * The presenter for the SendMailView.
 * 
 * @author tpanuel
 */
public class SendMailPresenter extends PopupPresenter<SendMailDisplay> {
    private final SendSheetByMailCommand sendSheet;

    private final SendSheetsByMailCommand sendSheets;

    /**
     * Create a presenter for the SendMailView.
     * 
     * @param pDisplay
     *            The display interface.
     * @param pEventBus
     *            The event bus.
     * @param pSendSheetCommand
     *            The send sheet command.
     * @param pSendSheetsCommand
     *            The send sheets command.
     */
    @Inject
    public SendMailPresenter(final SendMailDisplay pDisplay,
            final EventBus pEventBus,
            final SendSheetByMailCommand pSendSheetCommand,
            final SendSheetsByMailCommand pSendSheetsCommand) {
        super(pDisplay, pEventBus);
        sendSheet = pSendSheetCommand;
        sendSheets = pSendSheetsCommand;
    }

    /**
     * Initialize the mail form.
     * 
     * @param pResult
     *            The result.
     * @param pMultivalued
     *            If several sheet has been selected from the listing panel.
     */
    public void init(final GetMailingInfoResult pResult,
            final boolean pMultivalued) {
        getDisplay().clear();
        // Set available mail address
        getDisplay().setAvailableMailAddress(pResult.getDestinationUsers());
        // Set attached sheet references
        getDisplay().setAttachedSheetReferences(pResult.getSheetReferences());
        // Set report models
        getDisplay().setReportModel(pResult.getReportModels());
        // Set the send button handler
        if (pMultivalued) {
            getDisplay().setSendButtonHandler(sendSheets);
        }
        else {
            getDisplay().setSendButtonHandler(sendSheet);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.PopupPresenter#getClosePopupEvent()
     */
    @Override
    protected GlobalEvent<ClosePopupAction> getClosePopupEvent() {
        return GlobalEvent.CLOSE_SEND_MAIL_POPUP;
    }
}