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
import org.topcased.gpm.ui.application.shared.command.CloseContainerResult;
import org.topcased.gpm.ui.application.shared.command.product.CloseProductAction;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.server.product.ProductFacade;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * CloseProductHandler
 * 
 * @author nveillet
 */
public class CloseProductHandler extends
        AbstractCommandActionHandler<CloseProductAction, CloseContainerResult> {

    /**
     * Create the CloseProductHandler
     * 
     * @param pHttpSession
     *            The http session
     */
    @Inject
    public CloseProductHandler(Provider<HttpSession> pHttpSession) {
        super(pHttpSession);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#execute(net.customware.gwt.dispatch.shared.Action,
     *      net.customware.gwt.dispatch.server.ExecutionContext)
     */
    @Override
    public CloseContainerResult execute(CloseProductAction pAction,
            ExecutionContext pContext) throws ActionException {

        ProductFacade lProductFacade = getFacadeLocator().getProductFacade();

        UiSession lSession =
                getDefaultSessionByProductId(pAction.getProductId());

        // If session not already created = product not exist (close on creation)
        if (lSession == null) {
            lSession = getDefaultSession();
        }

        // remove product to the facade product cache
        lProductFacade.clearCache(lSession, pAction.getProductId());

        return new CloseContainerResult();
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#getActionType()
     */
    @Override
    public Class<CloseProductAction> getActionType() {
        return CloseProductAction.class;
    }

}
