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
import org.topcased.gpm.ui.application.shared.command.sheet.DeleteSheetAction;
import org.topcased.gpm.ui.application.shared.command.sheet.DeleteSheetResult;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * DeleteSheetHandler
 * 
 * @author nveillet
 */
public class DeleteSheetHandler extends
        AbstractCommandActionHandler<DeleteSheetAction, DeleteSheetResult> {

    /**
     * Create the DeleteSheetHandler
     * 
     * @param pHttpSession
     *            The http session
     */
    @Inject
    public DeleteSheetHandler(Provider<HttpSession> pHttpSession) {
        super(pHttpSession);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#execute(net.customware.gwt.dispatch.shared.Action,
     *      net.customware.gwt.dispatch.server.ExecutionContext)
     */
    @Override
    public DeleteSheetResult execute(DeleteSheetAction pAction,
            ExecutionContext pContext) throws ActionException {

        // Delete sheet
        getFacadeLocator().getSheetFacade().deleteSheet(
                getSession(pAction.getProductName()), pAction.getSheetId());

        return new DeleteSheetResult();
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#getActionType()
     */
    @Override
    public Class<DeleteSheetAction> getActionType() {
        return DeleteSheetAction.class;
    }

}
