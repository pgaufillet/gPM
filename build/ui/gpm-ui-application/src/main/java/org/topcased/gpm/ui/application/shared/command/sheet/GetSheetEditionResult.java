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
import org.topcased.gpm.business.util.Translation;
import org.topcased.gpm.ui.application.shared.command.authorization.ConnectResult;
import org.topcased.gpm.ui.application.shared.exception.UIAttachmentException;
import org.topcased.gpm.ui.facade.shared.action.UiAction;
import org.topcased.gpm.ui.facade.shared.container.sheet.UiSheet;

/**
 * GetSheetEditionResult
 * 
 * @author nveillet
 */
public class GetSheetEditionResult extends GetSheetResult {

    /** serialVersionUID */
    private static final long serialVersionUID = -2880678992170284122L;

    private List<Translation> linkGroups;

    /**
     * Empty constructor for serialization.
     */
    public GetSheetEditionResult() {
    }

    /**
     * Create GetSheetEditionResult with values
     * 
     * @param pSheet
     *            the sheet
     * @param pActions
     *            the actions
     * @param pExtendedActions
     *            the extended actions
     * @param pLinkGroups
     *            the link groups
     * @param pConnectResult
     *            the connect result
     * @param lUIAttachedFieldsInError
     *            The Attached fields in error
     */
    public GetSheetEditionResult(UiSheet pSheet,
            Map<String, UiAction> pActions, List<UiAction> pExtendedActions,
            List<Translation> pLinkGroups, ConnectResult pConnectResult,
            List<UIAttachmentException> lUIAttachedFieldsInError) {
        super(pSheet, DisplayMode.EDITION, pActions, pExtendedActions,
                pConnectResult, lUIAttachedFieldsInError);
        linkGroups = pLinkGroups;
    }

    /**
     * get link groups
     * 
     * @return the link groups
     */
    public List<Translation> getLinkGroups() {
        return linkGroups;
    }
}