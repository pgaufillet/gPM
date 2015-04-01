/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Florian ROSIER (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.exception;

/**
 * NotImplementedException is a simple implementation of
 * org.apache.commons.lang.NotImplementedException.
 * 
 * @author frosier
 */
public class NotImplementedException extends UnsupportedOperationException {
    private static final long serialVersionUID = 8031601599908898594L;

    /**
     * Constructs an UnsupportedOperationException with no detail message.
     */
    public NotImplementedException() {
        super();
    }

    /**
     * Constructs an NotImplementedException with the specified detail message.
     * 
     * @param pMsg
     *            The detail message.
     */
    public NotImplementedException(String pMsg) {
        super(pMsg);
    }

    /**
     * Constructs a new exception with the specified detail message and cause.
     * 
     * @param pMsg
     *            The detail message.
     * @param pCause
     *            The cause.
     */
    public NotImplementedException(String pMsg, Throwable pCause) {
        super(pMsg, pCause);
    }
}