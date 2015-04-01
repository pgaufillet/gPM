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

import javax.servlet.http.HttpSession;

import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import org.topcased.gpm.ui.application.server.command.AbstractCommandActionHandler;
import org.topcased.gpm.ui.application.shared.command.authorization.DisconnectProductAction;
import org.topcased.gpm.ui.application.shared.command.authorization.DisconnectProductResult;
import org.topcased.gpm.ui.application.shared.command.sheet.CloseSheetAction;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * DisconnectProductHandler
 * 
 * @author nveillet
 */
public class DisconnectProductHandler
        extends
        AbstractCommandActionHandler<DisconnectProductAction, DisconnectProductResult> {

    /**
     * Create the DisconnectProductHandler
     * 
     * @param pHttpSession
     *            The http session
     */
    @Inject
    public DisconnectProductHandler(Provider<HttpSession> pHttpSession) {
        super(pHttpSession);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#execute(net.customware.gwt.dispatch.shared.Action,
     *      net.customware.gwt.dispatch.server.ExecutionContext)
     */
    @Override
    public DisconnectProductResult execute(DisconnectProductAction pAction,
            ExecutionContext pContext) throws ActionException {

        // Close all opened sheets
        if (pAction.getOpenedSheetIds() != null) {
            for (String lSheetId : pAction.getOpenedSheetIds()) {
                pContext.execute(new CloseSheetAction(pAction.getProductName(),
                        lSheetId));
            }
        }

        // Remove the connection to the product
        getUserSession().removeSession(pAction.getProductName());

        return new DisconnectProductResult(pAction.getProductName());
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#getActionType()
     */
    @Override
    public Class<DisconnectProductAction> getActionType() {
        return DisconnectProductAction.class;
    }

}
