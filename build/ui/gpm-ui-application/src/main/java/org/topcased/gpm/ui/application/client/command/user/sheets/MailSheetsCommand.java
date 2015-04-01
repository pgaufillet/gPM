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

import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.ui.application.client.command.AbstractCommand;
import org.topcased.gpm.ui.application.client.command.validation.SheetListingViewValidator;
import org.topcased.gpm.ui.application.client.event.GlobalEvent;
import org.topcased.gpm.ui.application.shared.command.mail.GetMailingInfoAction;

import com.google.gwt.user.client.Command;
import com.google.inject.Inject;

/**
 * A command to send a list of sheets by mail.
 * 
 * @author tpanuel
 */
public class MailSheetsCommand extends AbstractCommand implements Command {
    /**
     * Create an MailSheetsCommand.
     * 
     * @param pEventBus
     *            The bus event.
     */
    @Inject
    public MailSheetsCommand(final EventBus pEventBus) {
        super(pEventBus);
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.client.Command#execute()
     */
    @Override
    public void execute() {
        fireEvent(GlobalEvent.MAIL_SHEETS.getType(), new GetMailingInfoAction(
                getCurrentProductWorkspaceName(), getCurrentSheetIds()),
                SheetListingViewValidator.getInstance());
    }
}