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

import java.util.List;

import javax.servlet.http.HttpSession;

import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import org.topcased.gpm.ui.application.server.command.AbstractCommandActionHandler;
import org.topcased.gpm.ui.application.shared.command.authorization.AbstractConnectionResult;
import org.topcased.gpm.ui.application.shared.command.authorization.SelectProductAction;
import org.topcased.gpm.ui.application.shared.command.authorization.SelectProductResult;
import org.topcased.gpm.ui.facade.server.authorization.AuthorizationFacade;
import org.topcased.gpm.ui.facade.server.authorization.UiUserSession;
import org.topcased.gpm.ui.facade.shared.container.product.UiProduct;
import org.topcased.gpm.ui.facade.shared.container.product.UiProductHierarchy;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * SelectProductHandler
 * 
 * @author nveillet
 */
public class SelectProductHandler
        extends
        AbstractCommandActionHandler<SelectProductAction, AbstractConnectionResult> {

    /**
     * Create the SelectProductHandler
     * 
     * @param pHttpSession
     *            The http session
     */
    @Inject
    public SelectProductHandler(Provider<HttpSession> pHttpSession) {
        super(pHttpSession);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#execute(net.customware.gwt.dispatch.shared.Action,
     *      net.customware.gwt.dispatch.server.ExecutionContext)
     */
    @Override
    public SelectProductResult execute(SelectProductAction pAction,
            ExecutionContext pContext) throws ActionException {

        AuthorizationFacade lAuthorizationFacade =
                getFacadeLocator().getAuthorizationFacade();

        UiUserSession lUserSession = getUserSession();

        // get available product list
        List<UiProduct> lProductNames =
                lAuthorizationFacade.getAvailableUiProducts(lUserSession);

        // get available product hierarchy
        List<UiProductHierarchy> lProductHierarchy =
                lAuthorizationFacade.getAvailableProductsHierarchy(lUserSession);

        return new SelectProductResult(lProductNames, lProductHierarchy);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#getActionType()
     */
    @Override
    public Class<SelectProductAction> getActionType() {
        return SelectProductAction.class;
    }
}
