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

import javax.servlet.http.HttpSession;

import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import org.topcased.gpm.ui.application.server.command.AbstractCommandActionHandler;
import org.topcased.gpm.ui.application.shared.command.product.DeleteProductAction;
import org.topcased.gpm.ui.application.shared.command.product.DeleteProductResult;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * DeleteProductHandler
 * 
 * @author nveillet
 */
public class DeleteProductHandler extends
        AbstractCommandActionHandler<DeleteProductAction, DeleteProductResult> {

    /**
     * Create the DeleteProductHandler
     * 
     * @param pHttpSession
     *            The http session
     */
    @Inject
    public DeleteProductHandler(Provider<HttpSession> pHttpSession) {
        super(pHttpSession);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#execute(net.customware.gwt.dispatch.shared.Action,
     *      net.customware.gwt.dispatch.server.ExecutionContext)
     */
    @Override
    public DeleteProductResult execute(DeleteProductAction pAction,
            ExecutionContext pContext) throws ActionException {

        String lProductId = pAction.getProductId();

        String lProductName =
                getFacadeLocator().getProductFacade().getProductName(
                        getDefaultSession(), lProductId);

        UiSession lSession = getDefaultSession(lProductName);

        getFacadeLocator().getProductFacade().deleteProduct(lSession,
                lProductId, false);

        getUserSession().removeDefaultSession(lProductName);

        return new DeleteProductResult();
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#getActionType()
     */
    @Override
    public Class<DeleteProductAction> getActionType() {
        return DeleteProductAction.class;
    }

}
