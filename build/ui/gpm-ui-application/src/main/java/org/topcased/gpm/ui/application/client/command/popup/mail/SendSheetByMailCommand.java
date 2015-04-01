/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.command.popup.mail;

import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.ui.application.client.command.AbstractCommand;
import org.topcased.gpm.ui.application.client.command.popup.ClosePopupAction;
import org.topcased.gpm.ui.application.client.command.validation.SendMailViewValidator;
import org.topcased.gpm.ui.application.client.event.GlobalEvent;
import org.topcased.gpm.ui.application.client.popup.mail.SendMailDisplay;
import org.topcased.gpm.ui.application.client.util.CollectionUtil;
import org.topcased.gpm.ui.application.shared.command.mail.SendMailAction;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.inject.Inject;

/**
 * A command to send by mail the sheet displayed on the detail panel.
 * 
 * @author tpanuel
 */
public class SendSheetByMailCommand extends AbstractCommand implements
        ClickHandler {
    /**
     * Create an SendSheetByMailCommand.
     * 
     * @param pEventBus
     *            The bus event.
     */
    @Inject
    public SendSheetByMailCommand(final EventBus pEventBus) {
        super(pEventBus);
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.event.dom.client.ClickHandler#onClick(com.google.gwt.event.dom.client.ClickEvent)
     */
    @Override
    public void onClick(final ClickEvent pEvent) {
        final SendMailDisplay lMailForm =
                getPopupManager().getSendMailPresenter().getDisplay();

        fireEvent(GlobalEvent.SEND_MAIL.getType(), new SendMailAction(
                getCurrentProductWorkspaceName(), lMailForm.getDestination(),
                lMailForm.getSubject(), lMailForm.getBody(),
                CollectionUtil.singleton(getCurrentSheetId()),
                lMailForm.getReportModel()),
                SendMailViewValidator.getInstance(),
                GlobalEvent.CLOSE_SEND_MAIL_POPUP.getType(),
                new ClosePopupAction());
    }
}