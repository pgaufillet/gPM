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
 * UiBusinessException
 * 
 * @author nveillet
 */
public class UiBusinessException extends RuntimeException {

    /** serialVersionUID */
    private static final long serialVersionUID = 904392920171384436L;

    /**
     * Empty constructor for serialization
     */
    protected UiBusinessException() {
        super();
    }

    /**
     * Constructor
     * 
     * @param pMessage
     *            the message
     */
    public UiBusinessException(String pMessage) {
        super(pMessage);
    }
}
