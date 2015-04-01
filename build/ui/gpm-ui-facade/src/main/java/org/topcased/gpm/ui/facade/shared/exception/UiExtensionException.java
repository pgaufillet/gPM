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
 * UiExtensionException
 * 
 * @author nveillet
 */
public class UiExtensionException extends RuntimeException {

    /** serialVersionUID */
    private static final long serialVersionUID = -1943336051568963928L;

    /**
     * Empty constructor for serialization
     */
    protected UiExtensionException() {
        super();
    }

    /**
     * Constructor with message
     * 
     * @param pMessage
     *            the message
     */
    public UiExtensionException(String pMessage) {
        super(pMessage);
    }
}
