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
 * Exception raised when trying to delete an element still referenced elsewhere.
 * 
 * @author llatil
 */
public class UndeletableElementException extends BusinessException {
    /**
     * Automatically generated ID
     */
    private static final long serialVersionUID = -268102281382387516L;

    /**
     * Constructs a new UndeletableElementException.
     * 
     * @param pElementName
     *            Name of element that cannot be deleted.
     */
    public UndeletableElementException(String pElementName) {
        super("Cannot delete '" + pElementName + "'", "Cannot delete '"
                + pElementName + "'");
        elementName = pElementName;
    }

    /**
     * Get the name of the element that cannot be deleted.
     * 
     * @return Name of element.
     */
    public String getElementName() {
        return elementName;
    }

    private String elementName;
}
