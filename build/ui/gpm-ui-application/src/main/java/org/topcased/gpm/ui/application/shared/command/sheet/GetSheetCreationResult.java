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
import org.topcased.gpm.ui.application.shared.exception.UIAttachmentException;
import org.topcased.gpm.ui.facade.shared.action.UiAction;
import org.topcased.gpm.ui.facade.shared.container.sheet.UiSheet;

/**
 * GetSheetCreationResult
 * 
 * @author nveillet
 */
public class GetSheetCreationResult extends GetSheetResult {

    /** serialVersionUID */
    private static final long serialVersionUID = 1039496868883116021L;

    /**
     * Empty constructor for serialization.
     */
    public GetSheetCreationResult() {
    }

    /**
     * Create GetSheetCreationResult with values
     * 
     * @param pSheet
     *            the sheet
     * @param pActions
     *            the actions
     * @param pExtendedActions
     *            the extended actions
     * @param pConnectResult
     *            the connect result
     * @param pAttachedFieldsInError
     *            The Attached fields in error
     */
    public GetSheetCreationResult(UiSheet pSheet,
            Map<String, UiAction> pActions, List<UiAction> pExtendedActions,
            ConnectResult pConnectResult,
            List<UIAttachmentException> pAttachedFieldsInError) {
        super(pSheet, DisplayMode.CREATION, pActions, pExtendedActions,
                pConnectResult, pAttachedFieldsInError);
    }
}
