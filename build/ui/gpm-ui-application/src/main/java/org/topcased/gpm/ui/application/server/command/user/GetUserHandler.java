/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien LOUISY (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.server.command.user;

import javax.servlet.http.HttpSession;

import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import org.topcased.gpm.ui.application.server.command.AbstractCommandActionHandler;
import org.topcased.gpm.ui.application.shared.command.user.GetUserAction;
import org.topcased.gpm.ui.application.shared.command.user.GetUserResult;
import org.topcased.gpm.ui.facade.server.user.UserFacade;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * Get user for management action.
 * 
 * @author jlouisy
 */
public class GetUserHandler extends
        AbstractCommandActionHandler<GetUserAction, GetUserResult> {

    /**
     * Create GetUserHandler.
     * 
     * @param pHttpSession
     *            The http session
     */
    @Inject
    public GetUserHandler(Provider<HttpSession> pHttpSession) {
        super(pHttpSession);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#execute(net.customware.gwt.dispatch.shared.Action,
     *      net.customware.gwt.dispatch.server.ExecutionContext)
     */
    @Override
    public GetUserResult execute(GetUserAction pAction,
            ExecutionContext pContext) throws ActionException {

        String lLogin = pAction.getLogin();
        UserFacade lUserFacade = getFacadeLocator().getUserFacade();

        return new GetUserResult(lUserFacade.getUserLists(),
                lUserFacade.getUser(lLogin), lUserFacade.getUserAffectation(
                        getDefaultSession(), lLogin));
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#getActionType()
     */
    @Override
    public Class<GetUserAction> getActionType() {
        return GetUserAction.class;
    }

}
