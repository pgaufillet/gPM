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
import org.topcased.gpm.ui.application.shared.command.user.GetUserResult;
import org.topcased.gpm.ui.application.shared.command.user.UpdateUserAction;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.server.user.UserFacade;
import org.topcased.gpm.ui.facade.shared.administration.user.UiUser;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * Update user action.
 * 
 * @author jlouisy
 */
public class UpdateUserHandler extends
        AbstractCommandActionHandler<UpdateUserAction, GetUserResult> {

    /**
     * Create UpdateUserHandler.
     * 
     * @param pHttpSession
     *            The http session
     */
    @Inject
    public UpdateUserHandler(Provider<HttpSession> pHttpSession) {
        super(pHttpSession);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#execute(net.customware.gwt.dispatch.shared.Action,
     *      net.customware.gwt.dispatch.server.ExecutionContext)
     */
    @Override
    public GetUserResult execute(UpdateUserAction pAction,
            ExecutionContext pContext) throws ActionException {

        UserFacade lUserFacade = getFacadeLocator().getUserFacade();
        UiUser lUser = pAction.getUser();

        UiSession lSession = getDefaultSession();

        lUserFacade.updateUser(lSession, lUser);
        lUser.setPassWord(null);

        return new GetUserResult(lUserFacade.getUserLists(), lUser,
                lUserFacade.getUserAffectation(lSession, lUser.getLogin()));
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#getActionType()
     */
    @Override
    public Class<UpdateUserAction> getActionType() {
        return UpdateUserAction.class;
    }

}
