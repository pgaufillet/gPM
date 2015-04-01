/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Atos Origin
 ******************************************************************/
package org.topcased.gpm.ws.context;

import java.util.Arrays;

/**
 * The Class StringsListParam.
 * 
 * @author llatil
 */
public class StringsListParam extends ContextParam {

    /** serialVersionUID. */
    private static final long serialVersionUID = 9150736967897948797L;

    /** The strings list value. */
    private String[] stringValues;

    /**
     * Default ctor.
     */
    public StringsListParam() {
    }

    /**
     * Constructs a new strings list param.
     * 
     * @param pName
     *            the name
     * @param pValue
     *            the value
     */
    public StringsListParam(String pName, String[] pValue) {
        super(pName);
        stringValues = pValue;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.context.ContextParam#getValue()
     */
    public Object getValue() {
        return Arrays.asList(stringValues);
    }

    /**
     * Gets the strings list value.
     * 
     * @return the strings list value
     */
    public String[] getStringValues() {
        return stringValues;
    }

    /**
     * Sets the string values.
     * 
     * @param pValues
     *            the new string values
     */
    public void setStringValues(String[] pValues) {
        stringValues = pValues;
    }
}
