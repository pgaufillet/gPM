/***************************************************************
 * Copyright (c) 2011 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Anne Haugommard (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.exception;

/**
 * All exception thrown by user actions implying no technical issue
 * 
 * @author RCapecchi
 */
public class BusinessException extends GDMException {

    /** Generated UID */
    private static final long serialVersionUID = 8552216094111398567L;

    /**
     * Default constructor
     */
    public BusinessException() {
    }

    /**
     * Create the expected exception from a message
     * 
     * @param pMessage
     *            the message
     */
    public BusinessException(String pMessage) {
        super(pMessage);
    }

    /**
     * Create a new Business exception object.
     * 
     * @param pTechnicalMessage
     *            Message to report in the exception
     * @param pUserMessage
     *            Message to be displayed to the final user
     */
    public BusinessException(String pTechnicalMessage, String pUserMessage) {
        super(pTechnicalMessage, pUserMessage);
    }

    /**
     * Create a new User exception object containing a nested exception
     * <p>
     * This constructor should be used when the problem to report is caused by
     * an internal exception (catched in the code). In this case, the original
     * exception should be passed in as the <code>cause</code> parameter in
     * order to be preserved in this exception
     * 
     * @param pMessage
     *            Message to report in the exception
     * @param pCause
     *            Original exception object wrapped in this exception
     */
    public BusinessException(String pMessage, Throwable pCause) {
        super(pMessage, pCause);
    }

}
