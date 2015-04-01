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
import org.topcased.gpm.ui.application.shared.command.sheet.ChangeStateAction;
import org.topcased.gpm.ui.application.shared.command.sheet.GetSheetEditionAction;
import org.topcased.gpm.ui.application.shared.command.sheet.GetSheetResult;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * ChangeStateHandler
 * 
 * @author nveillet
 */
public class ChangeStateHandler extends
        AbstractCommandActionHandler<ChangeStateAction, GetSheetResult> {

    /**
     * Create the ChangeStateHandler
     * 
     * @param pHttpSession
     *            The http session
     */
    @Inject
    public ChangeStateHandler(Provider<HttpSession> pHttpSession) {
        super(pHttpSession);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#execute(net.customware.gwt.dispatch.shared.Action,
     *      net.customware.gwt.dispatch.server.ExecutionContext)
     */
    @Override
    public GetSheetResult execute(ChangeStateAction pAction,
            ExecutionContext pContext) throws ActionException {

        // Change the sheet state
        getFacadeLocator().getSheetFacade().changeState(
                getSession(pAction.getProductName()), pAction.getSheetId(),
                pAction.getTransitionName());

        return pContext.execute(new GetSheetEditionAction(
                pAction.getProductName(), pAction.getSheetId()));
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#getActionType()
     */
    @Override
    public Class<ChangeStateAction> getActionType() {
        return ChangeStateAction.class;
    }

}
