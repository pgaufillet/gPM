/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael KARGBO (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.server.command.authorization;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import org.apache.commons.lang.StringEscapeUtils;
import org.topcased.gpm.business.util.FieldsContainerType;
import org.topcased.gpm.business.util.FilterType;
import org.topcased.gpm.business.util.Translation;
import org.topcased.gpm.business.util.action.ActionName;
import org.topcased.gpm.ui.application.server.command.AbstractCommandWithMenuActionHandler;
import org.topcased.gpm.ui.application.shared.command.authorization.ConnectResult;
import org.topcased.gpm.ui.application.shared.command.authorization.GetConnectionInformationAction;
import org.topcased.gpm.ui.application.shared.command.filter.GetFilterSummariesAction;
import org.topcased.gpm.ui.application.shared.command.filter.GetFilterSummariesResult;
import org.topcased.gpm.ui.facade.server.FacadeLocator;
import org.topcased.gpm.ui.facade.server.authorization.AuthorizationFacade;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.server.authorization.UiUserSession;
import org.topcased.gpm.ui.facade.shared.action.UiAction;
import org.topcased.gpm.ui.facade.shared.action.UiActionWithSubMenu;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * Handle the connection information retrieving <h4>Pre-condition</h4> User need
 * to have a product's session <h4>Connection informations</h4>
 * <ul>
 * <li>Standard actions</li>
 * <li>Extended actions</li>
 * <li>Table and treeview filters</li>
 * <li>Url of the home page</li>
 * <li>Initialization information {@link InitApplicationAction}</li>
 * <li>roles name</li>
 * </ul>
 * 
 * @author mkargbo
 */
public class GetConnectionInformationHandler
        extends
        AbstractCommandWithMenuActionHandler<GetConnectionInformationAction, ConnectResult> {

    /**
     * Constructor
     * 
     * @param pHttpSession
     *            Http session
     */
    @Inject
    public GetConnectionInformationHandler(Provider<HttpSession> pHttpSession) {
        super(pHttpSession);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#execute(net.customware.gwt.dispatch.shared.Action,
     *      net.customware.gwt.dispatch.server.ExecutionContext)
     */
    @Override
    public ConnectResult execute(GetConnectionInformationAction pAction,
            ExecutionContext pContext) throws ActionException {
        final String lProductName = pAction.getProductName();

        // Select the available role names
        final AuthorizationFacade lAuthorizationFacade =
                getFacadeLocator().getAuthorizationFacade();
        final List<String> lRoleNames =
                lAuthorizationFacade.getAvailableRoles(getUserSession(),
                        lProductName);

        // Select the role session
        UiSession lSession = getSession(pAction.getProductName());

        // If a user is not connected to a product yet, connect him
        if (lSession == null) {
            final UiUserSession lUserSesion = getUserSession();

            lSession =
                    lAuthorizationFacade.connect(lUserSesion, lProductName,
                            lRoleNames.get(0));
            lUserSesion.addSession(lProductName, lSession);
        }

        // Standard actions
        Map<String, UiAction> lActions = new HashMap<String, UiAction>();
        lActions.put(
                ActionName.SHEET_CREATION,
                new UiActionWithSubMenu(ActionName.SHEET_CREATION,
                        getCreatableFieldsContainers(lSession,
                                FieldsContainerType.SHEET, null)));
        lActions.put(ActionName.SHEETS_CLOSE, new UiAction(
                ActionName.SHEETS_CLOSE));

        // Extended actions
        List<UiAction> lExtendedActions =
                getFacadeLocator().getExtendedActionFacade().getAvailableExtendedActions(
                        lSession);

        // Merge actions and apply access controls
        mergeActions(lSession, lActions, lExtendedActions, null, null);

        // Get filters informations
        GetFilterSummariesResult lFilterSummariesResult =
                pContext.execute(new GetFilterSummariesAction(
                        lSession.getProductName(), FilterType.SHEET));

        // Home page
        String lHomePageUrl =
                getFacadeLocator().getAttributeFacade().getHomepageUrl(lSession);

        final List<Translation> lRoles =
                FacadeLocator.instance().getI18nFacade().getTranslations(
                        getUserSession(), lRoleNames);
        // encode  HTML tags and java script code in product name before set them to the browser 
        return new ConnectResult(
                StringEscapeUtils.escapeHtml(lSession.getProductName()),
                lSession.getRoleName(), lRoles, lActions, lExtendedActions,
                lFilterSummariesResult.getFilters(),
                lFilterSummariesResult.isFilterCreatable(), lHomePageUrl);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#getActionType()
     */
    @Override
    public Class<GetConnectionInformationAction> getActionType() {
        return GetConnectionInformationAction.class;
    }
}