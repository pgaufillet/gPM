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
 * Exception thrown when an element cannot be modified.
 * 
 * @author llatil
 */
public class UnmodifiableElementException extends ConstraintException {

    private static final long serialVersionUID = 6561936974956888534L;

    /**
     * Create a new unmodifiable element exception.
     * 
     * @param pElementName
     *            Name of the unmodifiable element.
     */
    public UnmodifiableElementException(String pElementName) {
        super("'" + pElementName + "' cannot be modified");
        elementName = pElementName;
    }

    /**
     * Create a new unmodifiable element exception.
     * 
     * @param pElementName
     *            Name of the unmodifiable element.
     * @param pMessage
     *            Message used to give details about the error.
     */
    public UnmodifiableElementException(String pElementName, String pMessage) {
        super("'" + pElementName + "': " + pMessage);
        elementName = pElementName;
    }

    /**
     * Get the name of the unmodifiable element.
     * 
     * @return Name of element.
     */
    public String getElementName() {
        return elementName;
    }

    private String elementName;
}
