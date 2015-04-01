/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.server.command.sheet;

import java.util.List;

import javax.servlet.http.HttpSession;

import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import org.topcased.gpm.business.util.FilterType;
import org.topcased.gpm.business.util.search.FilterResult;
import org.topcased.gpm.ui.application.server.command.AbstractCommandActionHandler;
import org.topcased.gpm.ui.application.shared.command.filter.AbstractCommandFilterResult;
import org.topcased.gpm.ui.application.shared.command.filter.ExecuteTableFilterAction;
import org.topcased.gpm.ui.application.shared.command.filter.ExecuteTableFilterResult;
import org.topcased.gpm.ui.application.shared.command.sheet.DeleteSheetsAction;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * DeleteSheetsHandler
 * 
 * @author nveillet
 */
public class DeleteSheetsHandler
        extends
        AbstractCommandActionHandler<DeleteSheetsAction, AbstractCommandFilterResult> {

    /**
     * Create the DeleteSheetsHandler
     * 
     * @param pHttpSession
     *            The http session
     */
    @Inject
    public DeleteSheetsHandler(Provider<HttpSession> pHttpSession) {
        super(pHttpSession);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#execute(net.customware.gwt.dispatch.shared.Action,
     *      net.customware.gwt.dispatch.server.ExecutionContext)
     */
    @Override
    public AbstractCommandFilterResult execute(DeleteSheetsAction pAction,
            ExecutionContext pContext) throws ActionException {

        // Delete sheets
        for (String lSheetId : pAction.getSheetIds()) {
            getFacadeLocator().getSheetFacade().deleteSheet(
                    getSession(pAction.getProductName()), lSheetId);
        }

        // Execute the current filter
        ExecuteTableFilterResult lTableFilterResult =
                (ExecuteTableFilterResult) pContext.execute(new ExecuteTableFilterAction(
                        pAction.getProductName(), FilterType.SHEET,
                        pAction.getFilterId(), true));

        // Remove the deleted sheets to the filter result
        final List<FilterResult> lResultValues =
                lTableFilterResult.getFilterResult().getResultValues();

        for (int i = lResultValues.size() - 1; i >= 0; i--) {
            if (pAction.getSheetIds().contains(
                    lResultValues.get(i).getFilterResultId().getId())) {
                lResultValues.remove(i);
            }
        }

        return lTableFilterResult;
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#getActionType()
     */
    @Override
    public Class<DeleteSheetsAction> getActionType() {
        return DeleteSheetsAction.class;
    }

}
