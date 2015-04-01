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
 * Exception raised when an unimplemented method is invoked.
 * 
 * @author llatil
 */
public class MethodNotImplementedException extends GDMException {

    /** Generated UID. */
    private static final long serialVersionUID = 7626455407857265165L;

    /**
     * Constructs a new exception.
     * 
     * @param pMethodName
     *            Name of the unimplemented method.
     */
    public MethodNotImplementedException(String pMethodName) {
        super("Method '" + pMethodName + "' not implemented");
        methodName = pMethodName;
    }

    /**
     * Gets the method name.
     * 
     * @return the method name
     */
    public String getMethodName() {
        return methodName;
    }

    /** The method name. */
    protected String methodName;
}
