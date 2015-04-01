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
 * UiException
 * 
 * @author nveillet
 */
public class UiException extends RuntimeException {

    /** serialVersionUID */
    private static final long serialVersionUID = -5802264514392758812L;

    /**
     * Empty constructor for serialization
     */
    protected UiException() {
        super();
    }

    /**
     * Constructor
     * 
     * @param pMessage
     *            the message
     */
    public UiException(String pMessage) {
        super(pMessage);
    }
}
