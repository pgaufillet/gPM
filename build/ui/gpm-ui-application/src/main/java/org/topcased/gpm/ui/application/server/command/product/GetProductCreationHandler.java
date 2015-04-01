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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import org.topcased.gpm.business.util.DisplayMode;
import org.topcased.gpm.ui.application.server.command.AbstractCommandWithMenuActionHandler;
import org.topcased.gpm.ui.application.shared.command.product.GetProductCreationAction;
import org.topcased.gpm.ui.application.shared.command.product.GetProductCreationResult;
import org.topcased.gpm.ui.application.shared.command.product.GetProductResult;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.shared.action.UiAction;
import org.topcased.gpm.ui.facade.shared.container.product.UiProduct;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * GetProductCreationHandler
 * 
 * @author nveillet
 */
public class GetProductCreationHandler
        extends
        AbstractCommandWithMenuActionHandler<GetProductCreationAction, GetProductResult> {

    /**
     * Create the GetProductCreationHandler
     * 
     * @param pHttpSession
     *            The http session
     */
    @Inject
    public GetProductCreationHandler(Provider<HttpSession> pHttpSession) {
        super(pHttpSession);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#execute(net.customware.gwt.dispatch.shared.Action,
     *      net.customware.gwt.dispatch.server.ExecutionContext)
     */
    @Override
    public GetProductResult execute(GetProductCreationAction pAction,
            ExecutionContext pContext) throws ActionException {

        UiSession lSession = getDefaultSession();

        // Product
        UiProduct lProduct = getFacadeLocator().getProductFacade().getProductByType(
                        lSession, pAction.getProductTypeName(),
                        pAction.getEnvironmentNames());

        // Actions
        Map<String, UiAction> lActions = getProductActions(lSession, lProduct, DisplayMode.CREATION);
        
        // Apply access controls
        mergeActions(lSession, lActions, new ArrayList<UiAction>(), null, lProduct);

        // Product Hierarchy
        List<String> lProductNames = getFacadeLocator().getAuthorizationFacade()
        		.getAvailableProducts(getUserSession());

        return new GetProductCreationResult(lProduct, lActions, lProductNames);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#getActionType()
     */
    @Override
    public Class<GetProductCreationAction> getActionType() {
        return GetProductCreationAction.class;
    }
}
