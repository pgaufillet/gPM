/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.exception;

/**
 * Exception to be used for any system dependent error.
 * <p>
 * This exception contains the original exception as nested exception (the
 * actual exception causing the problem).
 * 
 * @author llatil
 */
public class SystemException extends GDMException {

    /** serialVersionUID. */
    private static final long serialVersionUID = 6053920135491328399L;

    /**
     * Constructs a new system exception.
     * 
     * @param pMessage
     *            the message
     * @param pCause
     *            the cause
     */
    public SystemException(String pMessage, Throwable pCause) {
        super(pMessage, pCause);
    }

    /**
     * Constructs a new system exception.
     * 
     * @param pCause
     *            the cause
     */
    public SystemException(Throwable pCause) {
        super(pCause);
    }
}
