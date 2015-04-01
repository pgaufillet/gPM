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
import org.topcased.gpm.ui.application.shared.command.product.CreateProductAction;
import org.topcased.gpm.ui.application.shared.command.product.GetProductResult;
import org.topcased.gpm.ui.application.shared.command.product.GetProductVisualizationAction;
import org.topcased.gpm.ui.application.shared.command.product.GetProductVisualizationResult;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * CreateProductHandler
 * 
 * @author nveillet
 */
public class CreateProductHandler extends
        AbstractCommandActionHandler<CreateProductAction, GetProductResult> {

    /**
     * Create the CreateProductHandler
     * 
     * @param pHttpSession
     *            The http session
     */
    @Inject
    public CreateProductHandler(Provider<HttpSession> pHttpSession) {
        super(pHttpSession);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#execute(net.customware.gwt.dispatch.shared.Action,
     *      net.customware.gwt.dispatch.server.ExecutionContext)
     */
    @Override
    public GetProductVisualizationResult execute(CreateProductAction pAction,
            ExecutionContext pContext) throws ActionException {
        final UiSession lSession = getDefaultSession();

        // Add files content for the product
        addFileContent(lSession.getParent(), pAction.getFields());
        // Create product
        getFacadeLocator().getProductFacade().createProduct(lSession,
                pAction.getProductId(), pAction.getProductName(),
                pAction.getProductDescription(), pAction.getFields(),
                pAction.getParents(), pAction.getChildren());

        // return sheet in visualization mode
        return (GetProductVisualizationResult) pContext.execute(new GetProductVisualizationAction(
                pAction.getProductId()));
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#getActionType()
     */
    @Override
    public Class<CreateProductAction> getActionType() {
        return CreateProductAction.class;
    }

}
