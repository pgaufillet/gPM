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

import java.util.List;

import javax.servlet.http.HttpSession;

import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import org.topcased.gpm.business.util.DisplayMode;
import org.topcased.gpm.ui.application.server.command.AbstractCommandActionHandler;
import org.topcased.gpm.ui.application.shared.command.link.GetLinksAction;
import org.topcased.gpm.ui.application.shared.command.link.GetLinksResult;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.shared.container.link.UiLink;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * GetLinksHandler
 * 
 * @author nveillet
 */
public class GetLinksHandler extends
        AbstractCommandActionHandler<GetLinksAction, GetLinksResult> {

    /**
     * Create the GetLinksHandler
     * 
     * @param pHttpSession
     *            The http session
     */
    @Inject
    public GetLinksHandler(Provider<HttpSession> pHttpSession) {
        super(pHttpSession);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#execute(net.customware.gwt.dispatch.shared.Action,
     *      net.customware.gwt.dispatch.server.ExecutionContext)
     */
    @Override
    public GetLinksResult execute(GetLinksAction pAction,
            ExecutionContext pContext) throws ActionException {
        UiSession lSession;
        final String lValuesContainerId = pAction.getValuesContainerId();
        final String lLinkTypeName = pAction.getLinkTypeName();
        final String lProductName = pAction.getProductName();
        final DisplayMode lDisplayMode = pAction.getDisplayMode();

        if (lProductName != null) {
            lSession = getSession(lProductName);
        }
        else {
            lSession = getDefaultSessionByProductId(lValuesContainerId);
        }

        List<UiLink> lLinks =
                getFacadeLocator().getLinkFacade().getLinks(lSession,
                        lValuesContainerId, lLinkTypeName, lDisplayMode);

        return new GetLinksResult(lValuesContainerId, lLinkTypeName, lLinks);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.dispatch.server.ActionHandler#getActionType()
     */
    @Override
    public Class<GetLinksAction> getActionType() {
        return GetLinksAction.class;
    }

}
