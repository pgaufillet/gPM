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

import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.util.action.AdministrationAction;
import org.topcased.gpm.ui.application.server.command.AbstractCommandActionHandler;
import org.topcased.gpm.ui.application.shared.command.user.GetUserProfileAction;
import org.topcased.gpm.ui.application.shared.command.user.GetUserProfileResult;
import org.topcased.gpm.ui.facade.server.authorization.AuthorizationFacade;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.server.user.UserFacade;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * Get user to edit its profile
 * 
 * @author jeballar
 */
public class GetUserProfileHandler
        extends
        AbstractCommandActionHandler<GetUserProfileAction, GetUserProfileResult> {

    /**
     * Create GetUserHandler.
     * 
     * @param pHttpSession
     *            The http session
     */
    @Inject
    public GetUserProfileHandler(Provider<HttpSession> pHttpSession) {
        super(pHttpSession);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#execute(net.customware.gwt.dispatch.shared.Action,
     *      net.customware.gwt.dispatch.server.ExecutionContext)
     */
    @Override
    public GetUserProfileResult execute(GetUserProfileAction pAction,
            ExecutionContext pContext) throws ActionException {

        // Set the highest role for user before authorizing it
        AuthorizationFacade lAuthorizationFacade =
                getFacadeLocator().getAuthorizationFacade();

        String lProductName =
                lAuthorizationFacade.getProductWithHighRole(getUserSession());

        String lRoleName = null;
        if (lProductName != null) {
            // get global default role
            lRoleName =
                    lAuthorizationFacade.getDefaultRole(getUserSession(),
                            lProductName);
        }
        // else User was authorized to login without product : it can only be the admin user
        else {
            lRoleName = AuthorizationFacade.ADMIN_ROLE_NAME;
            lProductName = StringUtils.EMPTY;
        }

        // Connection
        UiSession lSession =
                lAuthorizationFacade.connect(getUserSession(), lProductName,
                        lRoleName);
        getUserSession().setDefaultGlobalSession(lSession);

        // Check if user can modify its profile or not
        boolean lEditable = true;
        if (!lAuthorizationFacade.hasAnyAdminAccess(getDefaultSession(),
                AdministrationAction.USER_MODIFY_CURRENT)) {
            lEditable = false;
        }

        UserFacade lUserFacade = getFacadeLocator().getUserFacade();

        // Internal Auth AND user editable means password is editable
        boolean lPasswordEditable = lEditable;
        lPasswordEditable &=
                getFacadeLocator().getAttributeFacade().getAuthenticationSystem().size() <= 1;

        return new GetUserProfileResult(
                lUserFacade.getUserProfile(getUserSession().getToken()),
                lEditable, lPasswordEditable,
                getFacadeLocator().getI18nFacade().getAvailableLanguages());
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#getActionType()
     */
    @Override
    public Class<GetUserProfileAction> getActionType() {
        return GetUserProfileAction.class;
    }

}
