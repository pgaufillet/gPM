/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.events;

import org.springframework.context.ApplicationEvent;
import org.topcased.gpm.business.authorization.impl.AbstractContext;

/**
 * Event launched when a user (or role) session is closed
 * 
 * @author tpanuel
 */
public class LogoutEvent extends ApplicationEvent {
    private static final long serialVersionUID = -3067369378561894793L;

    private AbstractContext closedSessionContext;

    /**
     * Create a event when the a user (or role) session is closed
     * 
     * @param pSource
     *            The object that has launched the event
     * @param pClosedSessionContext
     *            User (or role) session context
     */
    public LogoutEvent(Object pSource, AbstractContext pClosedSessionContext) {
        super(pSource);
        closedSessionContext = pClosedSessionContext;
    }

    /**
     * Get the closedSessionContext
     * 
     * @return the closedSessionContext
     */
    public AbstractContext getClosedSessionContext() {
        return closedSessionContext;
    }

    /**
     * Set the closedSessionContext
     * 
     * @param pClosedSessionContext
     *            the new closedSessionContext
     */
    public void setClosedSessionContext(AbstractContext pClosedSessionContext) {
        closedSessionContext = pClosedSessionContext;
    }
}
