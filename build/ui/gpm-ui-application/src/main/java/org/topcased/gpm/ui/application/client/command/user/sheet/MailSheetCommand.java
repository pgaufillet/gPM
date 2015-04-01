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

import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.ui.application.client.command.AbstractCommand;
import org.topcased.gpm.ui.application.client.event.GlobalEvent;
import org.topcased.gpm.ui.application.client.util.CollectionUtil;
import org.topcased.gpm.ui.application.shared.command.mail.GetMailingInfoAction;

import com.google.gwt.user.client.Command;
import com.google.inject.Inject;

/**
 * A command to send a sheet by mail.
 * 
 * @author tpanuel
 */
public class MailSheetCommand extends AbstractCommand implements Command {
    /**
     * Create an MailSheetCommand.
     * 
     * @param pEventBus
     *            The bus event.
     */
    @Inject
    public MailSheetCommand(final EventBus pEventBus) {
        super(pEventBus);
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.client.Command#execute()
     */
    @Override
    public void execute() {
        fireEvent(GlobalEvent.MAIL_SHEET.getType(), new GetMailingInfoAction(
                getCurrentProductWorkspaceName(),
                CollectionUtil.singleton(getCurrentSheetId())));
    }
}