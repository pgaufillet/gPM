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

import javax.servlet.http.HttpSession;

import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import org.topcased.gpm.ui.application.server.command.AbstractCommandActionHandler;
import org.topcased.gpm.ui.application.shared.command.sheet.CreateSheetAction;
import org.topcased.gpm.ui.application.shared.command.sheet.GetSheetEditionAction;
import org.topcased.gpm.ui.application.shared.command.sheet.GetSheetEditionResult;
import org.topcased.gpm.ui.application.shared.command.sheet.GetSheetResult;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * CreateSheetHandler
 * 
 * @author nveillet
 */
public class CreateSheetHandler extends
        AbstractCommandActionHandler<CreateSheetAction, GetSheetResult> {
    /**
     * Create the CreateSheetHandler
     * 
     * @param pHttpSession
     *            The http session
     */
    @Inject
    public CreateSheetHandler(Provider<HttpSession> pHttpSession) {
        super(pHttpSession);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#execute(net.customware.gwt.dispatch.shared.Action,
     *      net.customware.gwt.dispatch.server.ExecutionContext)
     */
    @Override
    public GetSheetEditionResult execute(CreateSheetAction pAction,
            ExecutionContext pContext) throws ActionException {
        final UiSession lSession = getSession(pAction.getProductName());

        // Add files content
        addFileContent(lSession.getParent(), pAction.getFields());
        // Create sheet
        getFacadeLocator().getSheetFacade().createSheet(lSession,
                pAction.getSheetId(), pAction.getFields());

        // return sheet in Edition mode
        GetSheetEditionResult lResult =
                (GetSheetEditionResult) pContext.execute(new GetSheetEditionAction(
                        pAction.getProductName(), pAction.getSheetId()));

        // Product may have changed: remove from cache
        getFacadeLocator().getProductFacade().removeProductFromCache(lSession,
                pAction.getProductName());

        return lResult;
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#getActionType()
     */
    @Override
    public Class<CreateSheetAction> getActionType() {
        return CreateSheetAction.class;
    }
}