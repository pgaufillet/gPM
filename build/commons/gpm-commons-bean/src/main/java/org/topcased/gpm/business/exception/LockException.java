/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Atos Origin
 ******************************************************************/

package org.topcased.gpm.business.exception;

/**
 * Exception thrown when a forbidden read or update is performed on a locked
 * element.
 * 
 * @author llatil
 */
public class LockException extends ConstraintException {

    /** serialVersionUID */
    private static final long serialVersionUID = 8671802798125637242L;

    /** Id of the locked container */
    private final String lockedContainerId;

    /** Default user message */
    private static final String DEFAULT_LOCK_ERROR_MSG =
            "This element is currently locked. It seems that an other user is working on it.";

    /**
     * Constructs a new lock exception
     * 
     * @param pMessage
     *            Message
     * @param pContainerId
     *            Technical identifier of the locked element.
     */
    public LockException(String pMessage, String pContainerId) {
        super(pMessage);
        lockedContainerId = pContainerId;
        setUserMessage(DEFAULT_LOCK_ERROR_MSG);
    }

    /**
     * Get the identifier of the locked element.
     * 
     * @return Technical identifier
     */
    public String getContainerId() {
        return lockedContainerId;
    }
}
