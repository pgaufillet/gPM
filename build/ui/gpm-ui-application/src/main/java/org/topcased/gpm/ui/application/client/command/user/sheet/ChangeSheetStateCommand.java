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
import org.topcased.gpm.ui.application.shared.command.sheet.ChangeStateAction;
import org.topcased.gpm.ui.component.client.resources.i18n.Ui18n;

import com.google.gwt.user.client.Command;
import com.google.inject.Inject;

/**
 * A command to perform a transition on a sheet.
 * 
 * @author tpanuel
 */
public class ChangeSheetStateCommand extends AbstractCommand implements Command {
    private String transitionName;

    private String confirmationMessage;

    /**
     * Create an ChangeSheetStateCommand.
     * 
     * @param pEventBus
     *            The bus event.
     */
    @Inject
    public ChangeSheetStateCommand(final EventBus pEventBus) {
        super(pEventBus);
    }

    /**
     * Get the transition name.
     * 
     * @return The transition name.
     */
    public String getTransitionName() {
        return transitionName;
    }

    /**
     * Set the transition name.
     * 
     * @param pTransitionName
     *            The transition name.
     */
    public void setTransitionName(final String pTransitionName) {
        transitionName = pTransitionName;
    }

    /**
     * Set the confirmation message
     * 
     * @param confirmationMessage
     *            the confirmation message
     */
    public void setConfirmationMessage(String pConfirmationMessage) {
        this.confirmationMessage = pConfirmationMessage;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.client.Command#execute()
     */
    @Override
    public void execute() {
        // If sheet was not modified, do not ask confirmation
        if (isCurrentSheetModified()) {
            fireEvent(GlobalEvent.LOAD_SHEET.getType(), new ChangeStateAction(
                    getCurrentProductWorkspaceName(), getCurrentSheetId(),
                    transitionName), confirmationMessage);
        }
        else {
            fireEvent(GlobalEvent.LOAD_SHEET.getType(), new ChangeStateAction(
                    getCurrentProductWorkspaceName(), getCurrentSheetId(),
                    transitionName), Ui18n.MESSAGES.confirmationChangeState());
        }
    }
}