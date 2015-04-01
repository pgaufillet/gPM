/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.server.command.filter.edit;

import java.util.List;

import javax.servlet.http.HttpSession;

import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import org.topcased.gpm.ui.application.server.command.AbstractCommandActionHandler;
import org.topcased.gpm.ui.application.shared.command.filter.edit.PreSaveFilterAction;
import org.topcased.gpm.ui.application.shared.command.filter.edit.PreSaveFilterResult;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.shared.filter.UiFilter;
import org.topcased.gpm.ui.facade.shared.filter.UiFilterUsage;
import org.topcased.gpm.ui.facade.shared.filter.UiFilterVisibility;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * PreSaveFilterHandler
 * 
 * @author nveillet
 */
public class PreSaveFilterHandler extends
        AbstractCommandActionHandler<PreSaveFilterAction, PreSaveFilterResult> {

    /**
     * Create the PreSaveFilterHandler
     * 
     * @param pHttpSession
     *            The http session
     */
    @Inject
    public PreSaveFilterHandler(Provider<HttpSession> pHttpSession) {
        super(pHttpSession);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#execute(net.customware.gwt.dispatch.shared.Action,
     *      net.customware.gwt.dispatch.server.ExecutionContext)
     */
    @Override
    public PreSaveFilterResult execute(PreSaveFilterAction pAction,
            ExecutionContext pContext) throws ActionException {

        UiSession lUiSession =
                getSession(pAction.getProductName(), pAction.getFilterType());

        String lFilterId = pAction.getFilterId();

        String lFilterName = null;
        String lFilterDescription = null;
        UiFilterVisibility lFilterVisibility = null;
        UiFilterUsage lFilterUsage = null;
        Boolean lIsHidden = null;

        List<UiFilterVisibility> lAvailableVisibilities =
                getFacadeLocator().getFilterFacade().getAvailableVisibilities(
                        lUiSession, pAction.getFilterType());

        if (lFilterId != null) {
            UiFilter lFilter =
                    getFacadeLocator().getFilterFacade().getFilter(lUiSession,
                            lFilterId);
            lFilterName = lFilter.getName();
            lFilterDescription = lFilter.getDescription();
            lFilterVisibility = lFilter.getVisibility();
            lFilterUsage = lFilter.getUsage();
            if (getFacadeLocator().getAuthorizationFacade().hasGlobalAdminAccess(
                    lUiSession)) {
                lIsHidden = lFilter.getHidden();
            }
        }
        else if (getFacadeLocator().getAuthorizationFacade().hasGlobalAdminAccess(
                lUiSession)) {
            lIsHidden = Boolean.FALSE;
        }

        return new PreSaveFilterResult(lFilterName, lFilterDescription,
                lAvailableVisibilities, lFilterVisibility, lFilterUsage,
                lIsHidden);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#getActionType()
     */
    @Override
    public Class<PreSaveFilterAction> getActionType() {
        return PreSaveFilterAction.class;
    }

}
