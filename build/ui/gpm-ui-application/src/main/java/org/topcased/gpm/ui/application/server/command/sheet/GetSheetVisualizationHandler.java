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
import org.topcased.gpm.business.util.Translation;
import org.topcased.gpm.ui.application.server.command.AbstractCommandWithMenuActionHandler;
import org.topcased.gpm.ui.application.shared.command.authorization.ConnectResult;
import org.topcased.gpm.ui.application.shared.command.sheet.GetSheetResult;
import org.topcased.gpm.ui.application.shared.command.sheet.GetSheetVisualizationAction;
import org.topcased.gpm.ui.application.shared.command.sheet.GetSheetVisualizationResult;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.shared.action.UiAction;
import org.topcased.gpm.ui.facade.shared.container.sheet.UiSheet;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * GetSheetVisualizationHandler
 * 
 * @author nveillet
 */
public class GetSheetVisualizationHandler
        extends
        AbstractCommandWithMenuActionHandler<GetSheetVisualizationAction, GetSheetResult> {

    /**
     * Create the GetSheetVisualizationHandler
     * 
     * @param pHttpSession
     *            The http session
     */
    @Inject
    public GetSheetVisualizationHandler(Provider<HttpSession> pHttpSession) {
        super(pHttpSession);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#execute(net.customware.gwt.dispatch.shared.Action,
     *      net.customware.gwt.dispatch.server.ExecutionContext)
     */
    @Override
    public GetSheetVisualizationResult execute(
            GetSheetVisualizationAction pAction, ExecutionContext pContext)
        throws ActionException {

        String lSheetId = pAction.getSheetId();
        UiSession lSession = getSession(pAction.getProductName());

        // Sheet
        UiSheet lSheet =
                getFacadeLocator().getSheetFacade().getSheet(lSession,
                        lSheetId, DisplayMode.VISUALIZATION);

        // Links
        List<Translation> lLinkGroups =
                getFacadeLocator().getLinkFacade().getLinkGroups(lSession,
                        lSheetId);

        // Actions
        Map<String, UiAction> lActions =
                getSheetActions(lSession, lSheet, DisplayMode.VISUALIZATION);

        // Extended actions
        List<UiAction> lExtendedActions =
                getFacadeLocator().getExtendedActionFacade().getAvailableExtendedActions(
                        lSession, lSheet.getTypeId(), DisplayMode.VISUALIZATION);

        // Merge actions and apply access controls
        mergeActions(lSession, lActions, lExtendedActions, null, lSheet);

        // Connect result
        ConnectResult lConnectResult =
                getConnectResult(pAction.getProductName(),
                        lSheet.getProductName(), pContext);

        return new GetSheetVisualizationResult(lSheet, lActions,
                lExtendedActions, lLinkGroups, lConnectResult, null);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#getActionType()
     */
    @Override
    public Class<GetSheetVisualizationAction> getActionType() {
        return GetSheetVisualizationAction.class;
    }
}
