/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.command.popup.extended;

import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.ui.application.client.command.AbstractCommand;
import org.topcased.gpm.ui.application.client.command.popup.ClosePopupAction;
import org.topcased.gpm.ui.application.client.command.validation.InputDataViewValidator;
import org.topcased.gpm.ui.application.client.event.GlobalEvent;
import org.topcased.gpm.ui.application.client.popup.extended.InputDataPresenter;
import org.topcased.gpm.ui.application.shared.command.extendedaction.ExecuteExtendedActionAction;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.inject.Inject;

/**
 * A command to execute extended action.
 * 
 * @author tpanuel
 */
public class ExecuteExtendedActionWithInputDataCommand extends AbstractCommand
        implements ClickHandler {
    /**
     * Create an ExecuteExtendedActionCommand.
     * 
     * @param pEventBus
     *            The bus event.
     */
    @Inject
    public ExecuteExtendedActionWithInputDataCommand(final EventBus pEventBus) {
        super(pEventBus);
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.event.dom.client.ClickHandler#onClick(com.google.gwt.event.dom.client.ClickEvent)
     */
    @Override
    public void onClick(final ClickEvent pEvent) {
        final InputDataPresenter lInputDataPresenter =
                getPopupManager().getInputDataPresenter();
        final ExecuteExtendedActionAction lAction =
                new ExecuteExtendedActionAction();

        lAction.setProductName(lInputDataPresenter.getProductName());
        lAction.setExtendedActionName(lInputDataPresenter.getExtendedActionName());
        lAction.setExtensionContainerId(lInputDataPresenter.getExtensionContainerId());
        lAction.setInputDataId(lInputDataPresenter.getInputDataId());
        lAction.setInputDataFields(lInputDataPresenter.getUpdatedFields());

        lAction.setExtendedActionType(lInputDataPresenter.getExtendedActionType());

        switch (lInputDataPresenter.getExtendedActionType()) {
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

        uploadFileAndFireEvent(GlobalEvent.CLOSE_INPUT_DATA_POPUP.getType(),
                new ClosePopupAction(), InputDataViewValidator.getInstance(),
                GlobalEvent.EXECUTE_EXTENDED_ACTION.getType(), lAction,
                lInputDataPresenter.getUploadRegisters());
    }
}