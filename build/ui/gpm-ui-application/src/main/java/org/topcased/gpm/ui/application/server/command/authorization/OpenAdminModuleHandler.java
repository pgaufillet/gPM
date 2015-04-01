/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.server.command.authorization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.util.FieldsContainerType;
import org.topcased.gpm.business.util.FilterType;
import org.topcased.gpm.business.util.action.ActionName;
import org.topcased.gpm.business.util.action.AdministrationAction;
import org.topcased.gpm.ui.application.server.command.AbstractCommandWithMenuActionHandler;
import org.topcased.gpm.ui.application.shared.command.authorization.OpenAdminModuleAction;
import org.topcased.gpm.ui.application.shared.command.authorization.OpenAdminModuleResult;
import org.topcased.gpm.ui.application.shared.command.filter.GetFilterSummariesAction;
import org.topcased.gpm.ui.application.shared.command.filter.GetFilterSummariesResult;
import org.topcased.gpm.ui.facade.server.authorization.AuthorizationFacade;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.shared.action.UiAction;
import org.topcased.gpm.ui.facade.shared.action.UiActionWithSubMenu;
import org.topcased.gpm.ui.facade.shared.administration.user.UiUser;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * OpenAdminModuleHandler
 * 
 * @author nveillet
 */
public class OpenAdminModuleHandler
        extends
        AbstractCommandWithMenuActionHandler<OpenAdminModuleAction, OpenAdminModuleResult> {

    /**
     * Create the OpenAdminModuleHandler
     * 
     * @param pHttpSession
     *            The http session
     */
    @Inject
    public OpenAdminModuleHandler(Provider<HttpSession> pHttpSession) {
        super(pHttpSession);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#execute(net.customware.gwt.dispatch.shared.Action,
     *      net.customware.gwt.dispatch.server.ExecutionContext)
     */
    @Override
    public OpenAdminModuleResult execute(OpenAdminModuleAction pAction,
            ExecutionContext pContext) throws ActionException {

        AuthorizationFacade lAuthorizationFacade =
                getFacadeLocator().getAuthorizationFacade();

        String lProductName =
                lAuthorizationFacade.getProductWithHighRole(getUserSession());

        String lRoleName = null;
        if (lProductName != null) {
            // get global default role
            lRoleName = lAuthorizationFacade.getDefaultRole(getUserSession(), lProductName);
        }
        // else User was authorized to login without product : it can only be the admin user
        else {
            lRoleName = AuthorizationFacade.ADMIN_ROLE_NAME;
            lProductName = StringUtils.EMPTY;
        }

        // Connection
        UiSession lSession = lAuthorizationFacade.connect(getUserSession(), lProductName, lRoleName);
        getUserSession().setDefaultGlobalSession(lSession);

        // Get filters informations
        GetFilterSummariesResult lFilterSummariesResult =
                pContext.execute(new GetFilterSummariesAction(
                        lSession.getProductName(), FilterType.PRODUCT));

        // get product administration actions
        Map<String, UiAction> lProductActions = getProductActions(lSession);

        // get User administration data
        List<UiUser> lUsersSortedByLogin = null;
        List<UiUser> lUsersSortedByName = null;

        // equivalent to AuthorizationFacade.hasUserManagementAccess()
        if (lAuthorizationFacade.hasAnyAdminAccess(lSession,
                AdministrationAction.USER_MODIFY) || lAuthorizationFacade.hasAnyAdminAccess(lSession,
                        AdministrationAction.USER_ROLES_MODIFY) || lAuthorizationFacade.hasProductAdminAccess(lSession)) {
            lUsersSortedByLogin =
                    getFacadeLocator().getUserFacade().getUserLists().getUserListSortedByLogin();
            lUsersSortedByName =
                    getFacadeLocator().getUserFacade().getUserLists().getUserListSortedByName();
        }

        // get user administration actions
        Map<String, UiAction> lUserActions = getUserActions(lSession);

        // Private environment creation access
        boolean lPrivateEnvironmentCreationAccess =
                getFacadeLocator().getAuthorizationFacade().hasPrivateEnvironmentCreationAccess(
                        lSession);

        // availables languages
        List<String> lAvailablesLanguages =
                getFacadeLocator().getI18nFacade().getAvailableLanguages();

        // get dictionary administration data
        List<String> lAvailableEnvironments =
                getFacadeLocator().getDictionaryFacade().getEnvironmentNames(
                        lSession);
        Map<String, UiAction> lDictionaryActions =
                getDictionaryActions(lSession);

        return new OpenAdminModuleResult(lProductActions,
                lFilterSummariesResult.getFilters(),
                lFilterSummariesResult.isFilterCreatable(),
                lUsersSortedByLogin, lUsersSortedByName, lUserActions,
                lAvailablesLanguages, lAvailableEnvironments,
                lDictionaryActions, lPrivateEnvironmentCreationAccess);
    }

    /**
     * Get actions for product administration
     * 
     * @param pSession
     * @return the actions
     */
    private Map<String, UiAction> getProductActions(UiSession pSession) {
        Map<String, UiAction> lActions = new HashMap<String, UiAction>();

        // Import product
        UiAction lAction = null;
        if (getFacadeLocator().getAuthorizationFacade().hasAnyAdminAccess(
                pSession, AdministrationAction.PRODUCT_IMPORT)) {
            lAction = new UiAction(ActionName.PRODUCTS_IMPORT);
        }
        lActions.put(ActionName.PRODUCTS_IMPORT, lAction);

        // Creation product
        lAction = null;
        if (getFacadeLocator().getAuthorizationFacade().hasAnyAdminAccess(
                pSession, AdministrationAction.PRODUCT_CREATE)) {
            lAction =
                    new UiActionWithSubMenu(ActionName.PRODUCT_CREATION,
                            getCreatableFieldsContainers(pSession,
                                    FieldsContainerType.PRODUCT, null));
        }
        lActions.put(ActionName.PRODUCT_CREATION, lAction);

        // Apply access controls
        mergeActions(pSession, lActions, new ArrayList<UiAction>(), null, null);

        return lActions;
    }

    /**
     * Get actions for dictionary administration
     * 
     * @param pSession the session
     * @return the actions
     */
    private Map<String, UiAction> getDictionaryActions(UiSession pSession) {
        Map<String, UiAction> lActions = new HashMap<String, UiAction>();

        // Creation user
        if (getFacadeLocator().getAuthorizationFacade().hasAnyAdminAccess(
                pSession, AdministrationAction.DICT_MODIFY)) {
            lActions.put(ActionName.DICTIONARY_EDITION, new UiAction(
                    ActionName.DICTIONARY_EDITION));
        }
        if (getFacadeLocator().getAuthorizationFacade().hasAnyAdminAccess(
                pSession, AdministrationAction.ENV_MODIFY)) {
            lActions.put(ActionName.ENVIRONMENT_EDITION, new UiAction(
                    ActionName.ENVIRONMENT_EDITION));
        }
        if (getFacadeLocator().getAuthorizationFacade().hasAnyAdminAccess(
                pSession, AdministrationAction.ENV_CREATE)) {
            lActions.put(ActionName.ENVIRONMENT_CREATION, new UiAction(
                    ActionName.ENVIRONMENT_CREATION));
        }
        return lActions;
    }

    /**
     * Get actions for user administration
     * 
     * @param pSession
     *            the session
     * @return the actions
     */
    private Map<String, UiAction> getUserActions(UiSession pSession) {
        Map<String, UiAction> lActions = new HashMap<String, UiAction>();

        // Creation user
        if (getFacadeLocator().getAuthorizationFacade().hasAnyAdminAccess(
                pSession, AdministrationAction.USER_CREATE)) {
            lActions.put(ActionName.USER_CREATION, new UiAction(
                    ActionName.USER_CREATION));
        }

        // Update user
        if (getFacadeLocator().getAuthorizationFacade().hasAnyAdminAccess(
                pSession, AdministrationAction.USER_MODIFY) ||
                getFacadeLocator().getAuthorizationFacade().hasProductAdminAccess(pSession)) {
            lActions.put(ActionName.USER_SAVE, new UiAction(
                    ActionName.USER_SAVE));
            lActions.put(ActionName.USER_AFFECTATION, new UiAction(
                    ActionName.USER_AFFECTATION));
        }
        
        // Update user roles only
        else if (getFacadeLocator().getAuthorizationFacade().hasAnyAdminAccess(
                pSession, AdministrationAction.USER_ROLES_MODIFY) ||
                getFacadeLocator().getAuthorizationFacade().hasProductAdminAccess(pSession)) {
            lActions.put(ActionName.USER_AFFECTATION, new UiAction(
                    ActionName.USER_AFFECTATION));
        }
        
        // Delete user
        if (getFacadeLocator().getAuthorizationFacade().hasAnyAdminAccess(
                pSession, AdministrationAction.USER_DELETE)) {
            lActions.put(ActionName.USER_DELETE, new UiAction(
                    ActionName.USER_DELETE));
        }

        return lActions;
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#getActionType()
     */
    @Override
    public Class<OpenAdminModuleAction> getActionType() {
        return OpenAdminModuleAction.class;
    }

}
