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
import java.util.Map.Entry;

import javax.servlet.http.HttpSession;

import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import org.topcased.gpm.ui.application.server.command.AbstractCommandActionHandler;
import org.topcased.gpm.ui.application.shared.command.product.GetProductEditionAction;
import org.topcased.gpm.ui.application.shared.command.product.GetProductResult;
import org.topcased.gpm.ui.application.shared.command.product.UpdateProductAction;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.server.link.LinkFacade;
import org.topcased.gpm.ui.facade.shared.container.field.UiField;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * UpdateProductHandler
 * 
 * @author nveillet
 */
public class UpdateProductHandler extends
        AbstractCommandActionHandler<UpdateProductAction, GetProductResult> {

    /**
     * Create the UpdateProductHandler
     * 
     * @param pHttpSession
     *            The http session
     */
    @Inject
    public UpdateProductHandler(Provider<HttpSession> pHttpSession) {
        super(pHttpSession);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#execute(net.customware.gwt.dispatch.shared.Action,
     *      net.customware.gwt.dispatch.server.ExecutionContext)
     */
    @Override
    public GetProductResult execute(UpdateProductAction pAction,
            ExecutionContext pContext) throws ActionException {
        // Get session
        final UiSession lSession =
                getDefaultSessionByProductId(pAction.getProductId());

        // Add files content for the product
        addFileContent(lSession.getParent(), pAction.getFields());
        // Update product
        getFacadeLocator().getProductFacade().updateProduct(lSession,
                pAction.getProductId(), pAction.getProductDescription(),
                pAction.getFields(), pAction.getParents(),
                pAction.getChildren());

        // Update links
        LinkFacade lLinkFacade = getFacadeLocator().getLinkFacade();
        for (Entry<String, List<UiField>> lLink : pAction.getLinks().entrySet()) {
            final List<UiField> lLinkFields = lLink.getValue();

            if (lLinkFields != null && !lLinkFields.isEmpty()) {
                // Add files content for the link
                addFileContent(lSession.getParent(), lLinkFields);
                lLinkFacade.updateLink(lSession, lLink.getKey(), lLinkFields);
            }
        }

        // return product in edition mode
        return pContext.execute(new GetProductEditionAction(pAction.getProductId()));
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#getActionType()
     */
    @Override
    public Class<UpdateProductAction> getActionType() {
        return UpdateProductAction.class;
    }

}
