/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.server.command.product;

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
import org.topcased.gpm.ui.application.shared.command.product.DeleteProductsAction;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * DeleteProductsHandler
 * 
 * @author nveillet
 */
public class DeleteProductsHandler
        extends
        AbstractCommandActionHandler<DeleteProductsAction, AbstractCommandFilterResult> {

    /**
     * Create the DeleteProductsHandler
     * 
     * @param pHttpSession
     *            The http session
     */
    @Inject
    public DeleteProductsHandler(Provider<HttpSession> pHttpSession) {
        super(pHttpSession);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#execute(net.customware.gwt.dispatch.shared.Action,
     *      net.customware.gwt.dispatch.server.ExecutionContext)
     */
    @Override
    public AbstractCommandFilterResult execute(DeleteProductsAction pAction,
            ExecutionContext pContext) throws ActionException {

        // Delete products
        for (String lProductId : pAction.getProductIds()) {
            String lProductName =
                    getFacadeLocator().getProductFacade().getProductName(
                            getDefaultSession(), lProductId);

            UiSession lSession = getDefaultSession(lProductName);
            if (lSession == null) {
                lSession = connectDefaultSession(lProductName);
            }

            getFacadeLocator().getProductFacade().deleteProduct(lSession,
                    lProductId, false);

            getUserSession().removeDefaultSession(lProductName);
        }

        // Execute the current filter
        ExecuteTableFilterResult lTableFilterResult =
                (ExecuteTableFilterResult) pContext.execute(new ExecuteTableFilterAction(
                        null, FilterType.PRODUCT, pAction.getFilterId(), true));

        // Remove the deleted sheets to the filter result
        List<FilterResult> lResultValues =
                lTableFilterResult.getFilterResult().getResultValues();

        for (int i = lResultValues.size() - 1; i >= 0; i--) {
            if (pAction.getProductIds().contains(
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
    public Class<DeleteProductsAction> getActionType() {
        return DeleteProductsAction.class;
    }

}
