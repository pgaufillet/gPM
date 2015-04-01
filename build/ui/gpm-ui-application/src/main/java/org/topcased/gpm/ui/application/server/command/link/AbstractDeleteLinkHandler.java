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

import net.customware.gwt.dispatch.shared.Action;

import org.topcased.gpm.business.util.DisplayMode;
import org.topcased.gpm.business.util.Translation;
import org.topcased.gpm.ui.application.server.command.AbstractCommandActionHandler;
import org.topcased.gpm.ui.application.shared.command.container.GetContainerResult;
import org.topcased.gpm.ui.application.shared.command.link.AbstractCommandLinkAction;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.server.link.LinkFacade;
import org.topcased.gpm.ui.facade.shared.container.UiContainer;
import org.topcased.gpm.ui.facade.shared.container.link.UiLink;

import com.google.inject.Provider;

/**
 * AbstractDeleteLinkHandler
 * 
 * @param <A>
 *            The {@link Action} implementation.
 * @param <R>
 *            The {@link GetContainerResult} implementation.
 * @author nveillet
 */
public abstract class AbstractDeleteLinkHandler<A extends Action<R>, R extends GetContainerResult<? extends UiContainer>>
        extends AbstractCommandActionHandler<A, R> {

    /**
     * Action handler constructor that initialize the httpSession object.
     * 
     * @param pHttpSession
     *            Http session
     */
    public AbstractDeleteLinkHandler(Provider<HttpSession> pHttpSession) {
        super(pHttpSession);
    }

    /**
     * Execute the link deletion
     * 
     * @param pSession
     *            the session
     * @param pAction
     *            the action
     */
    protected void doExecute(UiSession pSession,
            AbstractCommandLinkAction<R> pAction) {

        LinkFacade lLinkFacade = getFacadeLocator().getLinkFacade();

        // get all links by groups
        List<Translation> lLinkGroups =
                lLinkFacade.getLinkGroups(pSession, pAction.getOriginId());

        // get link from link type name, origin id and destination id
        for (String lDestinationId : pAction.getDestinationIds()) {
            for (Translation lLinkGroup : lLinkGroups) {
                if (lLinkGroup.getValue().equals(pAction.getLinkTypeName())) {
                    for (UiLink lLink : lLinkFacade.getLinks(pSession,
                            pAction.getOriginId(), lLinkGroup.getValue(),
                            DisplayMode.EDITION)) {
                        if (lLink.getOriginId().equals(lDestinationId)
                                || lLink.getDestinationId().equals(
                                        lDestinationId)) {
                            // delete link
                            lLinkFacade.deleteLink(pSession, lLink.getId());
                        }
                    }
                }
            }
        }
    }
}
