/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.shared.command.sheet;

import java.util.List;
import java.util.Map;

import org.topcased.gpm.business.util.DisplayMode;
import org.topcased.gpm.ui.application.shared.command.authorization.ConnectResult;
import org.topcased.gpm.ui.application.shared.command.container.GetContainerResult;
import org.topcased.gpm.ui.application.shared.exception.UIAttachmentException;
import org.topcased.gpm.ui.facade.shared.action.UiAction;
import org.topcased.gpm.ui.facade.shared.container.sheet.UiSheet;

/**
 * GetSheetResult
 * 
 * @author nveillet
 */
public abstract class GetSheetResult extends GetContainerResult<UiSheet> {

    /** serialVersionUID */
    private static final long serialVersionUID = -7016146197954560243L;

    protected List<UIAttachmentException> attachedFieldsInError;

    private List<UiAction> extendedActions;

    private ConnectResult connectResult;

    private boolean confirmationRequired = false;

    /**
     * Empty constructor for serialization.
     */
    protected GetSheetResult() {
        super();
    }

    /**
     * Create GetSheetResult with values
     * 
     * @param pSheet
     *            the sheet
     * @param pDisplayMode
     *            the display mode
     * @param pActions
     *            the actions
     * @param pExtendedActions
     *            the extended actions
     * @param pConnectResult
     *            the connect result*
     * @param lUIAttachedFieldsInError
     *            The Attached fields in error
     */
    protected GetSheetResult(UiSheet pSheet, DisplayMode pDisplayMode,
            Map<String, UiAction> pActions, List<UiAction> pExtendedActions,
            ConnectResult pConnectResult,
            List<UIAttachmentException> lUIAttachedFieldsInError) {
        super(pSheet, pDisplayMode, pActions);
        attachedFieldsInError = lUIAttachedFieldsInError;
        extendedActions = pExtendedActions;
        connectResult = pConnectResult;
    }

    /**
     * Create GetSheetResult with values
     * 
     * @param pSheet
     *            the sheet
     * @param pDisplayMode
     *            the display mode
     * @param pActions
     *            the actions
     * @param pExtendedActions
     *            the extended actions
     * @param pConnectResult
     *            the connect result
     * @param pIsConfirmationRequired
     *            is a confirmation is required due to a lock on the sheet
     * @param pAttachedFieldsInError
     *            The Attached fields in error
     */
    public GetSheetResult(UiSheet pSheet, DisplayMode pDisplayMode,
            Map<String, UiAction> pActions, List<UiAction> pExtendedActions,
            ConnectResult pConnectResult, boolean pIsConfirmationRequired,
            List<UIAttachmentException> pAttachedFieldsInError) {
        super(pSheet, pDisplayMode, pActions);
        extendedActions = pExtendedActions;
        connectResult = pConnectResult;
        confirmationRequired = pIsConfirmationRequired;
        attachedFieldsInError = pAttachedFieldsInError;
    }

    /**
     * get extended actions
     * 
     * @return the extended actions
     */
    public List<UiAction> getExtendedActions() {
        return extendedActions;
    }

    /**
     * get connect result
     * 
     * @return the connect result
     */
    public ConnectResult getConnectResult() {
        return connectResult;
    }

    /**
     * set the connect result
     * 
     * @param pConnectResult
     *            the connect result to set
     */
    public void setConnectResult(ConnectResult pConnectResult) {
        connectResult = pConnectResult;
    }

    public boolean isConfirmationRequired() {
        return confirmationRequired;
    }

    public void setConfirmationRequired(boolean pConfirmationRequired) {
        this.confirmationRequired = pConfirmationRequired;
    }

    public List<UIAttachmentException> getAttachedFieldsInError() {
        return attachedFieldsInError;
    }

}
