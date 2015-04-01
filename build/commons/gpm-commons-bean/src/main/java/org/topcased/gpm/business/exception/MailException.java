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
 * Exceptions for mail service.
 * 
 * @author tszadel
 */
public class MailException extends GDMException {

    /** Generated UID. */
    private static final long serialVersionUID = 7578068415204126989L;

    /**
     * Constructs a new mail exception.
     * 
     * @param pMessage
     *            the message
     */
    public MailException(String pMessage) {
        super(pMessage);
    }
}
