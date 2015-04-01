/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.popup.extended;

import java.util.List;

import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.business.values.field.BusinessFieldGroup;
import org.topcased.gpm.ui.application.client.command.popup.ClosePopupAction;
import org.topcased.gpm.ui.application.client.command.popup.extended.ExecuteExtendedActionWithInputDataCommand;
import org.topcased.gpm.ui.application.client.common.container.UiFieldManager;
import org.topcased.gpm.ui.application.client.event.GlobalEvent;
import org.topcased.gpm.ui.application.client.popup.PopupPresenter;
import org.topcased.gpm.ui.application.shared.command.extendedaction.GetInputDataResult;
import org.topcased.gpm.ui.application.shared.util.ExtendedActionType;
import org.topcased.gpm.ui.component.client.container.field.GpmUploadFileRegister;
import org.topcased.gpm.ui.facade.shared.container.field.UiField;
import org.topcased.gpm.ui.facade.shared.container.inputdata.UiInputData;

import com.google.inject.Inject;

/**
 * The presenter for the InputDataView.
 * 
 * @author tpanuel
 */
public class InputDataPresenter extends PopupPresenter<InputDataDisplay> {
    private String extendedActionName;

    private ExtendedActionType extendedActionType;

    private String extensionContainerId;

    private final UiFieldManager inputDataFieldManager;

    private String inputDataId;

    private String productName;

    /**
     * Create a presenter for the InputDataView.
     * 
     * @param pDisplay
     *            The display interface.
     * @param pEventBus
     *            The event bus.
     * @param pExecuteCommand
     *            extended action execute command
     */
    @Inject
    public InputDataPresenter(final InputDataDisplay pDisplay,
            final EventBus pEventBus,
            final ExecuteExtendedActionWithInputDataCommand pExecuteCommand) {
        super(pDisplay, pEventBus);
        inputDataFieldManager = new UiFieldManager();
        // Set the execute button handler
        getDisplay().setExecuteButtonHandler(pExecuteCommand);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.PopupPresenter#getClosePopupEvent()
     */
    @Override
    protected GlobalEvent<ClosePopupAction> getClosePopupEvent() {
        return GlobalEvent.CLOSE_INPUT_DATA_POPUP;
    }

    /**
     * Get the extended action name.
     * 
     * @return The extended action name.
     */
    public String getExtendedActionName() {
        return extendedActionName;
    }

    /**
     * get extended action type
     * 
     * @return the extended action type
     */
    public ExtendedActionType getExtendedActionType() {
        return extendedActionType;
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
     * Get the the input data field manager.
     * 
     * @return The input data field manager.
     */
    public UiFieldManager getInputDataFieldManager() {
        return inputDataFieldManager;
    }

    /**
     * Get the input data id.
     * 
     * @return The input data id.
     */
    public String getInputDataId() {
        return inputDataId;
    }

    /**
     * Get the product name.
     * 
     * @return The product name.
     */
    public String getProductName() {
        return productName;
    }

    /**
     * Get the updated fields.
     * 
     * @return The update fields.
     */
    public List<UiField> getUpdatedFields() {
        return inputDataFieldManager.getUpdatedFields();
    }

    /**
     * Get the upload registers.
     * 
     * @return The upload registers.
     */
    public List<GpmUploadFileRegister> getUploadRegisters() {
        return inputDataFieldManager.getUploadRegisters();
    }

    /**
     * Initialize the input data popup.
     * 
     * @param pResult
     *            The input data.
     * @param pProductName
     *            The product name.
     */
    public void initInputDataPopup(final GetInputDataResult pResult,
            final String pProductName) {
        final UiInputData lInputData = pResult.getInputData();

        // Display popup title
        getDisplay().setHeaderText(lInputData.getTypeName());
        // Display fields
        getDisplay().clearGroups();
        inputDataFieldManager.init(getDisplay().getInputDataFieldSet());
        for (BusinessFieldGroup lGroup : lInputData.getFieldGroups()) {
            getDisplay().addFieldGroup(
                    lGroup.getGroupName(),
                    inputDataFieldManager.buildGroup(lInputData,
                            lGroup.getFieldNames(), null), lGroup.isOpen());
        }
        // Keep extended action infos
        productName = pProductName;
        extendedActionName = pResult.getExtendedActionName();
        extensionContainerId = pResult.getExtensionContainerId();
        inputDataId = pResult.getInputData().getId();
        extendedActionType = pResult.getExtendedActionType();
    }
}