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

import javax.servlet.http.HttpSession;

import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.ui.application.server.command.AbstractCommandActionHandler;
import org.topcased.gpm.ui.application.shared.command.filter.edit.SaveFilterAction;
import org.topcased.gpm.ui.application.shared.command.filter.edit.SaveFilterResult;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.shared.exception.UiException;
import org.topcased.gpm.ui.facade.shared.filter.UiFilter;
import org.topcased.gpm.ui.facade.shared.filter.summary.UiFilterSummaries;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * SaveFilterHandler
 * 
 * @author nveillet
 */
public class SaveFilterHandler extends
        AbstractCommandActionHandler<SaveFilterAction, SaveFilterResult> {

    /**
     * Create the SaveFilterHandler
     * 
     * @param pHttpSession
     *            The http session
     */
    @Inject
    public SaveFilterHandler(Provider<HttpSession> pHttpSession) {
        super(pHttpSession);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#execute(net.customware.gwt.dispatch.shared.Action,
     *      net.customware.gwt.dispatch.server.ExecutionContext)
     */
    @Override
    public SaveFilterResult execute(SaveFilterAction pAction,
            ExecutionContext pContext) throws ActionException {

        UiSession lSession =
                getSession(pAction.getProductName(), pAction.getFilterType());

        // BOUCHON
        UiFilter lFilter = pAction.getFilter();

        String lSavedId = "";
        try {
            lSavedId = getFacadeLocator().getFilterFacade().saveFilter(lSession, lFilter);
        }
        catch (GDMException ex) {
            throw new UiException(ex.getLocalizedMessage());
        }
        UiFilterSummaries lFilterSummaries = null;
        lFilterSummaries =
                getFacadeLocator().getFilterFacade().getFilters(lSession,
                        pAction.getFilterType());
        return new SaveFilterResult(pAction.getProductName(),
                pAction.getFilterType(), lSavedId, lFilterSummaries);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#getActionType()
     */
    @Override
    public Class<SaveFilterAction> getActionType() {
        return SaveFilterAction.class;
    }

}
