/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien EBALLARD (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.server.command.user;

import javax.servlet.http.HttpSession;

import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import org.topcased.gpm.business.util.action.AdministrationAction;
import org.topcased.gpm.ui.application.server.command.AbstractCommandActionHandler;
import org.topcased.gpm.ui.application.shared.command.user.GetUserProfileResult;
import org.topcased.gpm.ui.application.shared.command.user.UpdateUserProfileAction;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.server.user.UserFacade;
import org.topcased.gpm.ui.facade.shared.administration.user.UiUser;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * Update user action.
 * 
 * @author jeballar
 */
public class UpdateUserProfileHandler
        extends
        AbstractCommandActionHandler<UpdateUserProfileAction, GetUserProfileResult> {

    /**
     * Create UpdateUserHandler.
     * 
     * @param pHttpSession
     *            The http session
     */
    @Inject
    public UpdateUserProfileHandler(Provider<HttpSession> pHttpSession) {
        super(pHttpSession);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#execute(net.customware.gwt.dispatch.shared.Action,
     *      net.customware.gwt.dispatch.server.ExecutionContext)
     */
    @Override
    public GetUserProfileResult execute(UpdateUserProfileAction pAction,
            ExecutionContext pContext) throws ActionException {
        boolean lEditable = true;
        if (!getFacadeLocator().getAuthorizationFacade().hasAnyAdminAccess(
                getDefaultSession(), AdministrationAction.USER_MODIFY_CURRENT)) {
            lEditable = false;
        }

        UserFacade lUserFacade = getFacadeLocator().getUserFacade();
        UiSession lSession = getDefaultSession();
        UiUser lUserUpdate = pAction.getUser();

        // Set the user to update login for security reasons
        lUserUpdate.setLogin(getUserSession().getLogin());

        if (lEditable) {
            // Perform update (controls in Facade method)
            lUserFacade.updateUserProfile(lSession, lUserUpdate);
        }

        // Set passwords to null before returning
        lUserUpdate.setPassWord(null);
        lUserUpdate.setNewPassword(null);

        return new GetUserProfileResult(lUserUpdate, lEditable, false, null);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#getActionType()
     */
    @Override
    public Class<UpdateUserProfileAction> getActionType() {
        return UpdateUserProfileAction.class;
    }

}
