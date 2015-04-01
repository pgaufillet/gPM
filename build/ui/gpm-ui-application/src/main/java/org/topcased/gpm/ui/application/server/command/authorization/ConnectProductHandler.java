/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin),
 * Michael KARGBO(Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.server.command.authorization;

import java.util.List;

import javax.servlet.http.HttpSession;

import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import org.topcased.gpm.business.exception.AuthorizationException;
import org.topcased.gpm.ui.application.server.command.AbstractCommandActionHandler;
import org.topcased.gpm.ui.application.shared.command.authorization.AbstractConnectionResult;
import org.topcased.gpm.ui.application.shared.command.authorization.ChangeRoleAction;
import org.topcased.gpm.ui.application.shared.command.authorization.ConnectProductAction;
import org.topcased.gpm.ui.application.shared.command.authorization.GetConnectionInformationAction;
import org.topcased.gpm.ui.facade.server.authorization.AuthorizationFacade;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * ConnectProductHandler
 * <p>
 * Connect the user to a product. The process is automatically selected (the
 * first one) and the role too.
 * </p>
 * <ul>
 * <li>{@link InitApplicationAction}
 * <dl>
 * <dt>OnSuccess</dt>
 * <dd>Getting connection informations {@link GetConnectionInformationAction}</dd>
 * <dt>OnFailure</dt>
 * <dd>Nothing</dd>
 * </dl>
 * </li>
 * </ul>
 * 
 * @author nveillet
 */
public class ConnectProductHandler
        extends
        AbstractCommandActionHandler<ConnectProductAction, AbstractConnectionResult> {

    /**
     * Create the LoginHandler
     * 
     * @param pHttpSession
     *            The http session
     */
    @Inject
    public ConnectProductHandler(Provider<HttpSession> pHttpSession) {
        super(pHttpSession);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#execute(net.customware.gwt.dispatch.shared.Action,
     *      net.customware.gwt.dispatch.server.ExecutionContext)
     */
    @Override
    public AbstractConnectionResult execute(ConnectProductAction pAction,
            ExecutionContext pContext) throws ActionException {

        AuthorizationFacade lAuthorizationFacade =
                getFacadeLocator().getAuthorizationFacade();

        String lProductName = pAction.getProductName();

        // get available roles
        String lRoleName = null;
        List<String> lRoleNames =
                lAuthorizationFacade.getAvailableRoles(getUserSession(),
                        lProductName);

        if (lRoleNames.size() == 0) {
            throw new AuthorizationException(
                    "No role available for the user ''"
                            + getUserSession().getLogin()
                            + "'' on to the product ''" + lProductName + "''.");
        }
        else {
            lRoleName = lRoleNames.get(0);
        }

        return pContext.execute(new ChangeRoleAction(lProductName, lRoleName,
                null));
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#getActionType()
     */
    @Override
    public Class<ConnectProductAction> getActionType() {
        return ConnectProductAction.class;
    }
}
