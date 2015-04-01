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

import net.customware.gwt.dispatch.shared.Action;

import org.topcased.gpm.ui.application.server.command.AbstractCommandActionHandler;
import org.topcased.gpm.ui.application.shared.command.container.GetContainerResult;
import org.topcased.gpm.ui.application.shared.command.link.AbstractCommandLinkAction;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.server.link.LinkFacade;
import org.topcased.gpm.ui.facade.shared.container.UiContainer;

import com.google.inject.Provider;

/**
 * AbstractCreateLinkHandler
 * 
 * @param <A>
 *            The {@link Action} implementation.
 * @param <R>
 *            The {@link GetContainerResult} implementation.
 * @author nveillet
 */
public abstract class AbstractCreateLinkHandler<A extends Action<R>, R extends GetContainerResult<? extends UiContainer>>
        extends AbstractCommandActionHandler<A, R> {

    /**
     * Action handler constructor that initialize the httpSession object.
     * 
     * @param pHttpSession
     *            Http session
     */
    public AbstractCreateLinkHandler(Provider<HttpSession> pHttpSession) {
        super(pHttpSession);
    }

    /**
     * Execute the link creation
     * 
     * @param pSession
     *            the session
     * @param pAction
     *            the action
     */
    protected void doExecute(UiSession pSession,
            AbstractCommandLinkAction<R> pAction) {
        LinkFacade lLinkFacade = getFacadeLocator().getLinkFacade();

        for (String lDestinationId : pAction.getDestinationIds()) {
            lLinkFacade.createLink(pSession, pAction.getLinkTypeName(),
                    pAction.getOriginId(), lDestinationId);
        }
    }
}
