/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.extensions.service;

/**
 * Parameter that define a variable of the context
 * 
 * @author tpanuel
 */
public class ContextParameter<T> {
    private final String parameterName;

    private final Class<T> parameterClass;

    /**
     * Construct a context parameter
     * 
     * @param pParameterName
     *            The name of the parameter
     * @param pParameterClass
     *            The class of the parameter
     */
    public ContextParameter(String pParameterName, Class<T> pParameterClass) {
        parameterName = pParameterName;
        parameterClass = pParameterClass;
    }

    /**
     * Get the parameter name
     * 
     * @return The parameter name
     */
    public String getParameterName() {
        return parameterName;
    }

    /**
     * Get the parameter class
     * 
     * @return The parameter class
     */
    public Class<T> getParameterClass() {
        return parameterClass;
    }
}
