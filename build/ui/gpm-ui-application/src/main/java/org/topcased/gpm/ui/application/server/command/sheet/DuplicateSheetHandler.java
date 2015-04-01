/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.server.command.sheet;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import org.topcased.gpm.business.util.DisplayMode;
import org.topcased.gpm.ui.application.server.command.AbstractCommandWithMenuActionHandler;
import org.topcased.gpm.ui.application.shared.command.sheet.DuplicateSheetAction;
import org.topcased.gpm.ui.application.shared.command.sheet.GetSheetCreationResult;
import org.topcased.gpm.ui.application.shared.command.sheet.GetSheetResult;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.shared.action.UiAction;
import org.topcased.gpm.ui.facade.shared.container.sheet.UiSheet;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * DuplicateSheetHandler
 * 
 * @author nveillet
 */
public class DuplicateSheetHandler
        extends
        AbstractCommandWithMenuActionHandler<DuplicateSheetAction, GetSheetResult> {

    /**
     * Create the DuplicateSheetHandler
     * 
     * @param pHttpSession
     *            The http session
     */
    @Inject
    public DuplicateSheetHandler(Provider<HttpSession> pHttpSession) {
        super(pHttpSession);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#execute(net.customware.gwt.dispatch.shared.Action,
     *      net.customware.gwt.dispatch.server.ExecutionContext)
     */
    @Override
    public GetSheetResult execute(DuplicateSheetAction pAction,
            ExecutionContext pContext) throws ActionException {

        UiSession lSession = getSession(pAction.getProductName());

        // Sheet
        UiSheet lSheet =
                getFacadeLocator().getSheetFacade().duplicateSheet(lSession,
                        pAction.getSheetId());

        // Actions
        Map<String, UiAction> lActions =
                getSheetActions(lSession, lSheet, DisplayMode.CREATION);

        // Extended actions
        List<UiAction> lExtendedActions =
                getFacadeLocator().getExtendedActionFacade().getAvailableExtendedActions(
                        lSession, lSheet.getTypeId(), DisplayMode.CREATION);

        // Merge actions and apply access controls
        mergeActions(lSession, lActions, lExtendedActions, null, lSheet);

        return new GetSheetCreationResult(lSheet, lActions, lExtendedActions,
                null, null);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#getActionType()
     */
    @Override
    public Class<DuplicateSheetAction> getActionType() {
        return DuplicateSheetAction.class;
    }

}
