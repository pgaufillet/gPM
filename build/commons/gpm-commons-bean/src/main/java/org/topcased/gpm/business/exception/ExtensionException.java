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
 * Exception raised in case of an extension point execution problem
 * 
 * @author llatil
 */
public class ExtensionException extends GDMException {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = 9119949131797764339L;

    /**
     * Name of the extension point that raised the exception
     */
    private String extensionPointName;

    /**
     * Construct a new ExtensionException
     * 
     * @param pExtPointName
     *            Name of extension point
     * @param pCause
     *            Original exception
     */
    public ExtensionException(String pExtPointName, Throwable pCause) {
        super("Extension point '" + pExtPointName + "' raised an exception",
                pCause);

        extensionPointName = pExtPointName;
    }

    /**
     * Get the name of the extension point that raised the error.
     * 
     * @return Name of extension point.
     */
    public String getExtensionPointName() {
        return extensionPointName;
    }
}
