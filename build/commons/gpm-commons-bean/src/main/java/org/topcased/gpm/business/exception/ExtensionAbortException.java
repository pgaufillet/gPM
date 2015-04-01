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
 * ExtensionAbortException
 * 
 * @author ahaugommard
 */
public class ExtensionAbortException extends ExpectedException {

    /** generated serialVersionUID */
    private static final long serialVersionUID = -7955569513623803119L;

    /**
     * Create an ExtensionAbortException from a message and a cause
     * 
     * @param pMessage
     *            Message describing the exception
     * @param pCause
     *            Initial cause of the exception
     */
    public ExtensionAbortException(String pMessage, Throwable pCause) {
        super(pMessage, pCause);
    }

    /**
     * Create the ExtensionAbortException from a message
     * 
     * @param pMessage
     *            the message
     */
    public ExtensionAbortException(String pMessage) {
        super(pMessage);
    }

    /**
     * Create the ExtensionAbortException from a cause
     * 
     * @param pCause
     *            the cause
     */
    public ExtensionAbortException(Throwable pCause) {
        super(pCause);
    }
}