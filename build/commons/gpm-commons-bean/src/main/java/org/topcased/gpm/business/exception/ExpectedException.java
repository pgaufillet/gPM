/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Anne Haugommard (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.exception;

/**
 * ExpectedException
 * 
 * @author ahaugommard
 */
public class ExpectedException extends GDMException {

    /** Generated UID */
    private static final long serialVersionUID = 5786822035333609343L;

    /**
     * Default constructor
     */
    public ExpectedException() {
    }

    /**
     * Create the expected exception from a message
     * 
     * @param pMessage
     *            the message
     */
    public ExpectedException(String pMessage) {
        super(pMessage);
    }

    /**
     * Create the expected exception from a cause
     * 
     * @param pCause
     *            the cause
     */
    public ExpectedException(Throwable pCause) {
        super(pCause);
    }

    /**
     * Create an expected exception from a message and a cause
     * 
     * @param pMessage
     *            the message
     * @param pCause
     *            the cause
     */
    public ExpectedException(String pMessage, Throwable pCause) {
        super(pMessage, pCause);
    }

}
