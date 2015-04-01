/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.server.command.link;

import javax.servlet.http.HttpSession;

import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import org.topcased.gpm.ui.application.shared.command.link.DeleteSheetLinkAction;
import org.topcased.gpm.ui.application.shared.command.sheet.GetSheetEditionAction;
import org.topcased.gpm.ui.application.shared.command.sheet.GetSheetResult;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * DeleteSheetLinkHandler
 * 
 * @author nveillet
 */
public class DeleteSheetLinkHandler extends
        AbstractDeleteLinkHandler<DeleteSheetLinkAction, GetSheetResult> {

    /**
     * Create the DeleteSheetLinkHandler
     * 
     * @param pHttpSession
     *            The http session
     */
    @Inject
    public DeleteSheetLinkHandler(Provider<HttpSession> pHttpSession) {
        super(pHttpSession);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#execute(net.customware.gwt.dispatch.shared.Action,
     *      net.customware.gwt.dispatch.server.ExecutionContext)
     */
    @Override
    public GetSheetResult execute(DeleteSheetLinkAction pAction,
            ExecutionContext pContext) throws ActionException {

        UiSession lSession = getSession(pAction.getProductName());

        // Delete links
        doExecute(lSession, pAction);

        return pContext.execute(new GetSheetEditionAction(
                pAction.getProductName(), pAction.getOriginId()));
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#getActionType()
     */
    @Override
    public Class<DeleteSheetLinkAction> getActionType() {
        return DeleteSheetLinkAction.class;
    }

}
