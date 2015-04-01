/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.shared.exception;

/**
 * UiSessionException
 * 
 * @author nveillet
 */
public class UiSessionException extends RuntimeException {

    /** serialVersionUID */
    private static final long serialVersionUID = -4995087707805752227L;

    /**
     * Empty constructor for serialization
     */
    public UiSessionException() {
        super();
    }

    /**
     * Constructor
     * 
     * @param pMessage
     *            the message
     */
    public UiSessionException(String pMessage) {
        super(pMessage);
    }
}
