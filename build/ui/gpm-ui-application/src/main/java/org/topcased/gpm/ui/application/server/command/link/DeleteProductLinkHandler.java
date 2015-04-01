/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.server.command.link;

import javax.servlet.http.HttpSession;

import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import org.topcased.gpm.ui.application.shared.command.link.DeleteProductLinkAction;
import org.topcased.gpm.ui.application.shared.command.product.GetProductEditionAction;
import org.topcased.gpm.ui.application.shared.command.product.GetProductResult;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * DeleteProductLinkHandler
 * 
 * @author nveillet
 */
public class DeleteProductLinkHandler extends
        AbstractDeleteLinkHandler<DeleteProductLinkAction, GetProductResult> {

    /**
     * Create the DeleteProductLinkHandler
     * 
     * @param pHttpSession
     *            The http session
     */
    @Inject
    public DeleteProductLinkHandler(Provider<HttpSession> pHttpSession) {
        super(pHttpSession);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#execute(net.customware.gwt.dispatch.shared.Action,
     *      net.customware.gwt.dispatch.server.ExecutionContext)
     */
    @Override
    public GetProductResult execute(DeleteProductLinkAction pAction,
            ExecutionContext pContext) throws ActionException {

        UiSession lSession =
                getDefaultSessionByProductId(pAction.getOriginId());

        // Delete links
        doExecute(lSession, pAction);

        return pContext.execute(new GetProductEditionAction(
                pAction.getOriginId()));
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#getActionType()
     */
    @Override
    public Class<DeleteProductLinkAction> getActionType() {
        return DeleteProductLinkAction.class;
    }

}
