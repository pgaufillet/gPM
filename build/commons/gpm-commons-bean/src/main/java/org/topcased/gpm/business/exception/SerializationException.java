/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available 
 * under the terms of the Lesser Gnu Public License (LGPL)which 
 * accompanies this distribution, and is available 
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin), Thomas Szadel
 * (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.exception;

/**
 * An Exception for Serialization service.
 * 
 * @author tszadel
 */
public class SerializationException extends GDMException {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = 2490280789502559397L;

    /**
     * Constructor.
     */
    public SerializationException() {
        super();
    }

    /**
     * Constructor.
     * 
     * @param pMessage
     *            The message.
     * @param pCause
     *            The cause.
     */
    public SerializationException(String pMessage, Throwable pCause) {
        super(pMessage, pCause);
    }

    /**
     * Constructor.
     * 
     * @param pCause
     *            The cause.
     */
    public SerializationException(Throwable pCause) {
        super(pCause);
    }

    /**
     * Constructor.
     * 
     * @param pMessage
     *            The message.
     */
    public SerializationException(String pMessage) {
        super(pMessage);
    }

}
