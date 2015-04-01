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

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import org.topcased.gpm.business.util.action.AdministrationAction;
import org.topcased.gpm.ui.application.server.command.AbstractCommandActionHandler;
import org.topcased.gpm.ui.application.shared.command.user.GetAffectationAction;
import org.topcased.gpm.ui.application.shared.command.user.GetAffectationResult;
import org.topcased.gpm.ui.facade.server.authorization.AuthorizationFacade;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.server.user.UserFacade;
import org.topcased.gpm.ui.facade.shared.administration.user.UiUserAffectation;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * Get user affectation action for affectation edition popup.
 * 
 * @author jlouisy
 */
public class GetAffectationHandler
        extends
        AbstractCommandActionHandler<GetAffectationAction, GetAffectationResult> {

    /**
     * Create GetUserAffectationHandler.
     * 
     * @param pHttpSession
     *            The http session
     */
    @Inject
    public GetAffectationHandler(Provider<HttpSession> pHttpSession) {
        super(pHttpSession);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#execute(net.customware.gwt.dispatch.shared.Action,
     *      net.customware.gwt.dispatch.server.ExecutionContext)
     */
    @Override
    public GetAffectationResult execute(GetAffectationAction pAction,
            ExecutionContext pContext) throws ActionException {

        String lLogin = pAction.getLogin();
        UserFacade lUserFacade = getFacadeLocator().getUserFacade();

        UiSession lSession = getDefaultSession();

        List<String> lProducts = getFacadeLocator().getAuthorizationFacade().getAvailableProducts(
                        lSession.getParent());
        List<String> lRoles = getFacadeLocator().getAuthorizationFacade().getAllRoles(
                        lSession.getParent());
        String[] lDisabledRoles = new String[0];

        if (getFacadeLocator().getAuthorizationFacade().hasAdminAccessOnInstance(
                lSession, AdministrationAction.USER_MODIFY)
                || getFacadeLocator().getAuthorizationFacade().hasAdminAccessOnInstance(
                        lSession, AdministrationAction.USER_ROLES_MODIFY)) {
            lProducts.add(0, "$PROCESS");
        } else {
            lDisabledRoles = getFacadeLocator().getProductFacade().getNonAssignableRolesForNonAdmins();
            List<String> lProductNames = new ArrayList<String>(lProducts);
            lProducts.clear();
            for (String lProductName : lProductNames) {
                if (getFacadeLocator().getAuthorizationFacade().hasSpecifiedAdminAccess(
                		lProductName, lSession, AdministrationAction.USER_MODIFY)
                        || getFacadeLocator().getAuthorizationFacade().hasSpecifiedAdminAccess(
                        		lProductName, lSession, AdministrationAction.USER_ROLES_MODIFY)) {
                    lProducts.add(lProductName);
                }
            }
        }

        UiUserAffectation lUserAffectation =
                lUserFacade.getUserAffectation(lSession, lLogin);
        lUserAffectation.setRoleTranslations(getFacadeLocator().getI18nFacade().getTranslations(
                lSession.getParent(), lRoles));
        lUserAffectation.setDisabledRoleNames(lDisabledRoles);
        
        return new GetAffectationResult(lUserAffectation, lProducts);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#getActionType()
     */
    @Override
    public Class<GetAffectationAction> getActionType() {
        return GetAffectationAction.class;
    }

}
