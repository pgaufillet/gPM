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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import org.topcased.gpm.business.util.DisplayMode;
import org.topcased.gpm.business.util.Translation;
import org.topcased.gpm.ui.application.server.command.AbstractCommandWithMenuActionHandler;
import org.topcased.gpm.ui.application.shared.command.authorization.ConnectResult;
import org.topcased.gpm.ui.application.shared.command.sheet.GetSheetEditionAction;
import org.topcased.gpm.ui.application.shared.command.sheet.GetSheetEditionResult;
import org.topcased.gpm.ui.application.shared.command.sheet.GetSheetResult;
import org.topcased.gpm.ui.application.shared.command.sheet.GetSheetVisualizationResult;
import org.topcased.gpm.ui.application.shared.exception.UIAttachmentException;
import org.topcased.gpm.ui.application.shared.exception.UIAttachmentTotalSizeException;
import org.topcased.gpm.ui.application.shared.exception.UIEmptyAttachmentException;
import org.topcased.gpm.ui.application.shared.exception.UIInvalidCharacterException;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.server.util.UIAttachmentUtils;
import org.topcased.gpm.ui.facade.shared.action.UiAction;
import org.topcased.gpm.ui.facade.shared.container.sheet.UiSheet;
import org.topcased.gpm.ui.facade.shared.exception.FacadeAttachmentException;
import org.topcased.gpm.ui.facade.shared.exception.FacadeAttachmentTotalSizeException;
import org.topcased.gpm.ui.facade.shared.exception.FacadeEmptyAttachmentException;
import org.topcased.gpm.ui.facade.shared.exception.FacadeInvalidCharacterException;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * GetSheetEditionHandler
 * 
 * @author nveillet
 */
public class GetSheetEditionHandler
        extends
        AbstractCommandWithMenuActionHandler<GetSheetEditionAction, GetSheetResult> {

    /**
     * Create the GetSheetEditionHandler
     * 
     * @param pHttpSession
     *            The http session
     */
    @Inject
    public GetSheetEditionHandler(Provider<HttpSession> pHttpSession) {
        super(pHttpSession);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#execute(net.customware.gwt.dispatch.shared.Action,
     *      net.customware.gwt.dispatch.server.ExecutionContext)
     */
    @Override
    public GetSheetResult execute(GetSheetEditionAction pAction,
            ExecutionContext pContext) throws ActionException {

        String lSheetId = pAction.getSheetId();

        /* get the product name using the linked sheet id, 
         * to make sure that  access rules are the suitable one 
         */
        final String lProductName =
                getFacadeLocator().getSheetFacade().getProductName(
                        getUserSession(), lSheetId);

        // Select the role session
        UiSession lSession = getSession(pAction.getProductName());

        DisplayMode lDisplayMode = DisplayMode.EDITION;

        boolean lIsSheetLocked =
                getFacadeLocator().getSheetFacade().isSheetLocked(lSession,
                        lSheetId, lDisplayMode);

        // Sheet
        UiSheet lSheet;
        ConnectResult lConnectResult =
                getConnectResult(pAction.getProductName(), lProductName,
                        pContext);
        if (lIsSheetLocked) {
            //get the sheet in visualization mode in case of the sheet is 
            //currently locked (edited by another user)
            lSheet =
                    getFacadeLocator().getSheetFacade().getSheet(lSession,
                            lSheetId, DisplayMode.VISUALIZATION);
        }
        else if (!pAction.getProductName().equalsIgnoreCase(lProductName)) {
            // the sheet is on another product
            // get a session on that product  
            lSession = getSession(lProductName);

            // get the sheet with the destination product rules 
            lSheet =
                    getFacadeLocator().getSheetFacade().getSheet(lSession,
                            lSheetId, lDisplayMode);
        }
        else {
            lSheet =
                    getFacadeLocator().getSheetFacade().getSheet(lSession,
                            lSheetId, lDisplayMode);
        }

        if (!lSheet.isUpdatable()) {
            lDisplayMode = DisplayMode.VISUALIZATION;
        }

        // Links
        List<Translation> lLinkGroups =
                getFacadeLocator().getLinkFacade().getLinkGroups(lSession,
                        lSheetId);

        // Actions
        Map<String, UiAction> lActions =
                getSheetActions(lSession, lSheet, lDisplayMode);

        // Extended actions
        List<UiAction> lExtendedActions =
                getFacadeLocator().getExtendedActionFacade().getAvailableExtendedActions(
                        lSession, lSheet.getTypeId(), lDisplayMode);

        // Merge actions and apply access controls
        mergeActions(lSession, lActions, lExtendedActions, null, lSheet);

        // Attached fields in error
        List<FacadeAttachmentException> lFacadeAttachedFieldsInError =
                getFacadeLocator().getSheetFacade().getAndAcknowledgeAttachedFilesInError(lSheetId);
        // Convert to UI objects
        List<UIAttachmentException> lUIAttachedFieldsInError = null;
        if (lFacadeAttachedFieldsInError != null) {
        	lUIAttachedFieldsInError = new ArrayList<UIAttachmentException>();
        	for (FacadeAttachmentException lFacadeException : lFacadeAttachedFieldsInError) {
        		if (lFacadeException instanceof FacadeEmptyAttachmentException) {
        			lUIAttachedFieldsInError.add(new UIEmptyAttachmentException(lFacadeException.getItem()));
        		}
        		else if (lFacadeException instanceof FacadeAttachmentTotalSizeException) {
        			lUIAttachedFieldsInError.add(new UIAttachmentTotalSizeException(lFacadeException.getItem()));
        		}
        		else if (lFacadeException instanceof FacadeInvalidCharacterException) {
        			lUIAttachedFieldsInError.add(new UIInvalidCharacterException(lFacadeException.getItem(),
        					 UIAttachmentUtils.getInvalidCharactersAsString()));
        		}
        	}
        }

        // If the sheet is not locked
        if (DisplayMode.EDITION.equals(lDisplayMode) && !lIsSheetLocked) {
            return new GetSheetEditionResult(lSheet, lActions,
                    lExtendedActions, lLinkGroups, lConnectResult,
                    lUIAttachedFieldsInError);
        }
        else {
            //Else return a sheetResult that will ask if the user wants
            //to see the locked sheet in visualization instead of edition mode
            return new GetSheetVisualizationResult(lSheet, lActions,
                    lExtendedActions, lLinkGroups, lConnectResult,
                    lIsSheetLocked, lUIAttachedFieldsInError);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#getActionType()
     */
    @Override
    public Class<GetSheetEditionAction> getActionType() {
        return GetSheetEditionAction.class;
    }
}
