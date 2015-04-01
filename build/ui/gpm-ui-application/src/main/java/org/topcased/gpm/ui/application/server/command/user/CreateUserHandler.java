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
import org.topcased.gpm.ui.application.shared.command.user.CreateUserAction;
import org.topcased.gpm.ui.application.shared.command.user.CreateUserResult;
import org.topcased.gpm.ui.facade.server.user.UserFacade;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * Create user action.
 * 
 * @author jlouisy
 */
public class CreateUserHandler extends
        AbstractCommandActionHandler<CreateUserAction, CreateUserResult> {

    /**
     * Create GetUserHandler.
     * 
     * @param pHttpSession
     *            The http session
     */
    @Inject
    public CreateUserHandler(Provider<HttpSession> pHttpSession) {
        super(pHttpSession);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#execute(net.customware.gwt.dispatch.shared.Action,
     *      net.customware.gwt.dispatch.server.ExecutionContext)
     */
    @Override
    public CreateUserResult execute(CreateUserAction pAction,
            ExecutionContext pContext) throws ActionException {

        UserFacade lUserFacade = getFacadeLocator().getUserFacade();

        lUserFacade.createUser(getDefaultSession(), pAction.getUser());
        return new CreateUserResult(lUserFacade.getUserLists());
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#getActionType()
     */
    @Override
    public Class<CreateUserAction> getActionType() {
        return CreateUserAction.class;
    }

}
