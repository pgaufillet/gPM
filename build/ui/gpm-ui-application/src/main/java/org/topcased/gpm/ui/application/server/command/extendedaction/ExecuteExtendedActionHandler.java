/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.server.command.extendedaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import org.topcased.gpm.business.util.DisplayMode;
import org.topcased.gpm.business.util.FilterType;
import org.topcased.gpm.business.util.action.ActionName;
import org.topcased.gpm.ui.application.server.command.AbstractCommandWithMenuActionHandler;
import org.topcased.gpm.ui.application.shared.command.authorization.ConnectResult;
import org.topcased.gpm.ui.application.shared.command.extendedaction.AbstractExecuteExtendedActionResult;
import org.topcased.gpm.ui.application.shared.command.extendedaction.ExecuteExtendedActionAction;
import org.topcased.gpm.ui.application.shared.command.extendedaction.FilterExtendedActionResult;
import org.topcased.gpm.ui.application.shared.command.extendedaction.GetFileExtendedActionResult;
import org.topcased.gpm.ui.application.shared.command.extendedaction.GetInputDataResult;
import org.topcased.gpm.ui.application.shared.command.extendedaction.GetSheetsExtendedActionResult;
import org.topcased.gpm.ui.application.shared.command.extendedaction.MessageExtendedActionResult;
import org.topcased.gpm.ui.application.shared.command.filter.ExecuteTableFilterResult;
import org.topcased.gpm.ui.application.shared.command.sheet.GetSheetCreationAction;
import org.topcased.gpm.ui.application.shared.command.sheet.GetSheetCreationResult;
import org.topcased.gpm.ui.application.shared.command.sheet.GetSheetEditionAction;
import org.topcased.gpm.ui.application.shared.command.sheet.GetSheetResult;
import org.topcased.gpm.ui.application.shared.command.sheet.GetSheetVisualizationAction;
import org.topcased.gpm.ui.application.shared.util.ExtendedActionType;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.server.extendedaction.ExtendedActionFacade;
import org.topcased.gpm.ui.facade.shared.action.UiAction;
import org.topcased.gpm.ui.facade.shared.container.inputdata.UiInputData;
import org.topcased.gpm.ui.facade.shared.container.sheet.UiSheet;
import org.topcased.gpm.ui.facade.shared.extendedaction.AbstractUiExtendedActionResult;
import org.topcased.gpm.ui.facade.shared.extendedaction.UiFileEAResult;
import org.topcased.gpm.ui.facade.shared.extendedaction.UiFilterEAResult;
import org.topcased.gpm.ui.facade.shared.extendedaction.UiMessageEAResult;
import org.topcased.gpm.ui.facade.shared.extendedaction.UiSheetCreationEAResult;
import org.topcased.gpm.ui.facade.shared.extendedaction.UiSheetsEAResult;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * ExecuteExtendedActionHandler
 * 
 * @author nveillet
 */
public class ExecuteExtendedActionHandler
        extends
        AbstractCommandWithMenuActionHandler<ExecuteExtendedActionAction, AbstractExecuteExtendedActionResult> {

    /**
     * Create the ExecuteExtendedActionHandler
     * 
     * @param pHttpSession
     *            The http session
     */
    @Inject
    public ExecuteExtendedActionHandler(Provider<HttpSession> pHttpSession) {
        super(pHttpSession);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#execute(net.customware.gwt.dispatch.shared.Action,
     *      net.customware.gwt.dispatch.server.ExecutionContext)
     */
    @Override
    public AbstractExecuteExtendedActionResult execute(
            ExecuteExtendedActionAction pAction, ExecutionContext pContext)
        throws ActionException {

        UiSession lSession = getSession(pAction.getProductName());

        ExtendedActionFacade lExtendedActionFacade =
                getFacadeLocator().getExtendedActionFacade();

        boolean lHasInputData =
                lExtendedActionFacade.hasInputData(lSession,
                        pAction.getExtendedActionName(),
                        pAction.getExtensionContainerId());

        AbstractExecuteExtendedActionResult lResult = null;

        // Return the input data
        if (lHasInputData && pAction.getInputDataId() == null) {
            UiInputData lInputData =
                    lExtendedActionFacade.getInputData(lSession,
                            pAction.getExtendedActionName(),
                            pAction.getExtensionContainerId(),
                            pAction.getSheetId(), pAction.getSheetIds());
            // disable the uploaded files cleaning to keep files from the calling 
            // sheet to the final extended action 
            lSession.getParent().setKeepTemporaryFiles(true);

            lResult =
                    new GetInputDataResult(pAction.getExtendedActionName(),
                            pAction.getExtensionContainerId(), lInputData,
                            pAction.getExtendedActionType());
        }
        // Execute extended action
        else {
            // Add files content for the input data wizard
            addFileContent(lSession.getParent(), pAction.getInputDataFields());
            // get files from the sheet in order to have the content available
            // for the extended action target
            addFileContent(lSession.getParent(),
                    pAction.getCurrentSheetUpdatedFields());

            DisplayMode lCurrentSheetDisplayMode = null;
            if (ExtendedActionType.SHEET_CREATION.equals(pAction.getExtendedActionType())) {
                lCurrentSheetDisplayMode = DisplayMode.CREATION;
            }
            else if (ExtendedActionType.SHEET_EDITION.equals(pAction.getExtendedActionType())) {
                lCurrentSheetDisplayMode = DisplayMode.EDITION;
            }

            AbstractUiExtendedActionResult lExtendedActionResult =
                    lExtendedActionFacade.executeExtendedAction(lSession,
                            pAction.getExtendedActionName(),
                            pAction.getExtensionContainerId(),
                            pAction.getInputDataId(),
                            pAction.getInputDataFields(), pAction.getSheetId(),
                            pAction.getSheetIds(), pAction.getFilterId(),
                            pAction.getCurrentSheetUpdatedFields(),
                            pAction.getCurrentLinksUpdatedFields(),
                            lCurrentSheetDisplayMode);

            if (lExtendedActionResult instanceof UiMessageEAResult) {
                lResult =
                        new MessageExtendedActionResult(
                                ((UiMessageEAResult) lExtendedActionResult).getMessage());
            }
            else if (lExtendedActionResult instanceof UiSheetsEAResult) {
                UiSheetsEAResult lUiSheetsEAResult =
                        (UiSheetsEAResult) lExtendedActionResult;
                List<GetSheetResult> lGetSheetResults =
                        new ArrayList<GetSheetResult>();
                for (String lSheetId : lUiSheetsEAResult.getSheetIds()) {
                    switch (lUiSheetsEAResult.getDisplayMode()) {
                        case EDITION:
                            lGetSheetResults.add(pContext.execute(new GetSheetEditionAction(
                                    pAction.getProductName(), lSheetId)));
                            break;
                        default:
                            lGetSheetResults.add(pContext.execute(new GetSheetVisualizationAction(
                                    pAction.getProductName(), lSheetId)));
                            break;
                    }
                }
                lResult = new GetSheetsExtendedActionResult(lGetSheetResults);
            }
            else if (lExtendedActionResult instanceof UiSheetCreationEAResult) {
                UiSheetCreationEAResult lUiSheetCreationEAResult =
                        (UiSheetCreationEAResult) lExtendedActionResult;
                List<GetSheetResult> lSheetResults =
                        new ArrayList<GetSheetResult>();
                if (lUiSheetCreationEAResult.getSheetTypeName() != null) {
                    lSheetResults.add(pContext.execute(new GetSheetCreationAction(
                            pAction.getProductName(),
                            lUiSheetCreationEAResult.getSheetTypeName())));
                }
                else {
                    lSheetResults.add(getSheetCreationResult(lSession,
                            lUiSheetCreationEAResult.getSheet(),
                            pAction.getProductName(), pContext));
                }
                lResult = new GetSheetsExtendedActionResult(lSheetResults);
            }
            else if (lExtendedActionResult instanceof UiFileEAResult) {
                UiFileEAResult lUiFileEAResult =
                        (UiFileEAResult) lExtendedActionResult;
                lResult =
                        new GetFileExtendedActionResult(
                                lUiFileEAResult.getFileId(),
                                lUiFileEAResult.getFileName(),
                                lUiFileEAResult.getMimeType());
            }
            else if (lExtendedActionResult instanceof UiFilterEAResult) {
                lResult =
                        getFilterResult(lSession,
                                (UiFilterEAResult) lExtendedActionResult);
            }

            // Send potential displayed message 
            lResult.setResultMessage(lExtendedActionResult.getResultMessage());
            
            lResult.setRefreshNeeded(lExtendedActionResult.getRefreshNeeded());
        }

        return lResult;
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#getActionType()
     */
    @Override
    public Class<ExecuteExtendedActionAction> getActionType() {
        return ExecuteExtendedActionAction.class;
    }

    /**
     * Get the filter result
     * 
     * @param pSession
     *            the session
     * @param pUiFilterEAResult
     *            the FilterEAResult
     * @return the FilterExtendedActionResult
     */
    private FilterExtendedActionResult getFilterResult(UiSession pSession,
            UiFilterEAResult pUiFilterEAResult) {

        // Get standard actions
        Map<String, UiAction> lActions =
                getFilterActions(pSession, pUiFilterEAResult.getFilter());
        if (FilterType.PRODUCT.equals(pUiFilterEAResult.getFilter().getFilterType())) {
            lActions.remove(ActionName.FILTER_PRODUCT_REFRESH);
            lActions.remove(ActionName.FILTER_PRODUCT_EDIT);
        }
        else {
            lActions.remove(ActionName.FILTER_SHEET_REFRESH);
            lActions.remove(ActionName.FILTER_SHEET_EXPORT);
            lActions.remove(ActionName.FILTER_SHEET_EDIT);
        }

        // Get Extended actions
        List<UiAction> lExtendedActions = new ArrayList<UiAction>();
        if (pUiFilterEAResult.getFilter().getFilterType().equals(
                FilterType.SHEET)) {
            lExtendedActions =
                    getFacadeLocator().getExtendedActionFacade().getAvailableExtendedActions(
                            pSession,
                            pUiFilterEAResult.getFilter().getContainerTypes());
        }

        // Merge actions and apply access controls
        mergeActions(pSession, lActions, lExtendedActions,
                pUiFilterEAResult.getFilter().getContainerTypes(), null);

        return new FilterExtendedActionResult(new ExecuteTableFilterResult(
                pUiFilterEAResult.getFilterResult(), null, null, null,
                lActions, lExtendedActions));
    }

    /**
     * Get the sheet creation result with a given Sheet
     * 
     * @param pSession
     *            the session
     * @param pSheet
     *            the sheet
     * @param pCurrentProductName
     *            the current product name
     * @param pContext
     *            the execution context
     * @return the GetSheetCreationResult
     * @throws ActionException
     *             action exception
     */
    private GetSheetCreationResult getSheetCreationResult(UiSession pSession,
            UiSheet pSheet, String pCurrentProductName,
            ExecutionContext pContext) throws ActionException {

        // Actions
        Map<String, UiAction> lActions =
                getSheetActions(pSession, pSheet, DisplayMode.CREATION);

        // Extended actions
        List<UiAction> lExtendedActions =
                getFacadeLocator().getExtendedActionFacade().getAvailableExtendedActions(
                        pSession, pSheet.getTypeId(), DisplayMode.CREATION);

        // Merge actions and apply access controls
        mergeActions(pSession, lActions, lExtendedActions, null, pSheet);

        // Connect result
        ConnectResult lConnectResult =
                getConnectResult(pCurrentProductName, pSheet.getProductName(),
                        pContext);

        return new GetSheetCreationResult(pSheet, lActions, lExtendedActions,
                lConnectResult, null);
    }
}
