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
 * ExtensionCheckException
 * 
 * @author ahaugommoard
 */
public class ExtensionCheckException extends ExpectedException {

    /** generated serialVersionUID */
    private static final long serialVersionUID = -7955569513623803119L;

    /**
     * Create an ExtensionCheckException from a message and a cause
     * 
     * @param pMessage
     *            Message of the exception
     * @param pCause
     *            Initial cause of the exception
     */
    public ExtensionCheckException(String pMessage, Throwable pCause) {
        super(pMessage, pCause);
    }

    /**
     * Create the ExtensionCheckException from a message
     * 
     * @param pMessage
     *            the message
     */
    public ExtensionCheckException(String pMessage) {
        super(pMessage);
    }

    /**
     * Create the ExtensionCheckException from a cause
     * 
     * @param pCause
     *            the cause
     */
    public ExtensionCheckException(Throwable pCause) {
        super(pCause);
    }
}
