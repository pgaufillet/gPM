/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.command.extended;

import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.ui.application.client.command.AbstractCommand;
import org.topcased.gpm.ui.application.client.event.GlobalEvent;
import org.topcased.gpm.ui.application.shared.command.extendedaction.ExecuteExtendedActionAction;
import org.topcased.gpm.ui.application.shared.util.ExtendedActionType;

import com.google.gwt.user.client.Command;
import com.google.inject.Inject;

/**
 * A command to execute extended action.
 * 
 * @author tpanuel
 */
public class ExecuteExtendedActionCommand extends AbstractCommand implements
        Command {
    private String actionName;

    private String extensionContainerId;

    private ExtendedActionType type;
    
    private String confirmationMessage;

    /**
     * Create an ExecuteExtendedActionCommand.
     * 
     * @param pEventBus
     *            The bus event.
     */
    @Inject
    public ExecuteExtendedActionCommand(final EventBus pEventBus) {
        super(pEventBus);
    }

    /**
     * Get the action name.
     * 
     * @return The action name.
     */
    public String getActionName() {
        return actionName;
    }

    /**
     * Set the action name.
     * 
     * @param pActionName
     *            The action name.
     */
    public void setActionName(final String pActionName) {
        actionName = pActionName;
    }

    /**
     * Get the extension container id.
     * 
     * @return The extension container id.
     */
    public String getExtensionContainerId() {
        return extensionContainerId;
    }

    /**
     * Set the extension container id.
     * 
     * @param pExtensionContainerId
     *            the new container id
     */
    public void setExtensionContainerId(final String pExtensionContainerId) {
        extensionContainerId = pExtensionContainerId;
    }

    /**
     * Get the type of extended action.
     * 
     * @return The type of extended action.
     */
    public ExtendedActionType getType() {
        return type;
    }

    /**
     * Set the type of extended action.
     * 
     * @param pType
     *            The type of extended action.
     */
    public void setType(final ExtendedActionType pType) {
        type = pType;
    }

    public String getConfirmationMessage() {
        return confirmationMessage;
    }

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
        final ExecuteExtendedActionAction lAction =
                new ExecuteExtendedActionAction(
                        getCurrentProductWorkspaceName(), actionName,
                        extensionContainerId);

        switch (type) {
            case SHEET_VISUALIZATION:
                lAction.setSheetId(getCurrentSheetId());
                break;
            case SHEET_CREATION:
            case SHEET_EDITION:
                lAction.setSheetId(getCurrentSheetId());
                lAction.setCurrentSheetUpdatedFields(getCurrentUpdatedSheetFields());
                // Add current updated link fields in the action
                lAction.setCurrentLinksUpdatedFields(getCurrentUpdatedLinkFields());
                break;
            case SHEETS:
                lAction.setSheetIds(getCurrentSheetIds());
                lAction.setFilterId(getCurrentSheetTableFilterId());
                break;
            case GLOBAL:
            default:
                // Nothing to do
                break;
        }

        lAction.setExtendedActionType(type);

        // Upload updated attached files in edition or creation mode 
        // so the extended actions can use them
        if (ExtendedActionType.SHEET_CREATION.equals(type)
                || ExtendedActionType.SHEET_EDITION.equals(type)) {            
            uploadFileAndFireEvent(
                    GlobalEvent.EXECUTE_EXTENDED_ACTION.getType(), lAction,
                    null, getCurrentSheetUploadRegisters(), confirmationMessage);
        }
        else {
            fireEvent(GlobalEvent.EXECUTE_EXTENDED_ACTION.getType(), lAction,
                    confirmationMessage);
        }

    }
}