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
import org.topcased.gpm.business.util.Translation;
import org.topcased.gpm.ui.application.server.command.AbstractCommandWithMenuActionHandler;
import org.topcased.gpm.ui.application.shared.command.product.GetProductResult;
import org.topcased.gpm.ui.application.shared.command.product.GetProductVisualizationAction;
import org.topcased.gpm.ui.application.shared.command.product.GetProductVisualizationResult;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.shared.action.UiAction;
import org.topcased.gpm.ui.facade.shared.container.product.UiProduct;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * GetProductVisualizationHandler
 * 
 * @author nveillet
 */
public class GetProductVisualizationHandler
        extends
        AbstractCommandWithMenuActionHandler<GetProductVisualizationAction, GetProductResult> {

    /**
     * Create the GetProductVisualizationHandler
     * 
     * @param pHttpSession
     *            The http session
     */
    @Inject
    public GetProductVisualizationHandler(Provider<HttpSession> pHttpSession) {
        super(pHttpSession);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#execute(net.customware.gwt.dispatch.shared.Action,
     *      net.customware.gwt.dispatch.server.ExecutionContext)
     */
    @Override
    public GetProductResult execute(GetProductVisualizationAction pAction,
            ExecutionContext pContext) throws ActionException {

        String lProductId = pAction.getProductId();

        UiSession lSession = getDefaultSessionByProductId(lProductId);
        if (lSession == null) {
            lSession = connectDefaultSessionByProductId(lProductId);
        }

        // Product
        UiProduct lProduct =
                getFacadeLocator().getProductFacade().getProduct(lSession,
                        lProductId, DisplayMode.VISUALIZATION);

        // Links
        List<Translation> lLinkGroups =
                getFacadeLocator().getLinkFacade().getLinkGroups(lSession,
                        lProductId);

        // Actions
        Map<String, UiAction> lActions =
                getProductActions(lSession, lProduct, DisplayMode.VISUALIZATION);
        // Apply access controls
        mergeActions(lSession, lActions, new ArrayList<UiAction>(), null,
                lProduct);

        return new GetProductVisualizationResult(lProduct, lActions,
                lLinkGroups);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#getActionType()
     */
    @Override
    public Class<GetProductVisualizationAction> getActionType() {
        return GetProductVisualizationAction.class;
    }

}
