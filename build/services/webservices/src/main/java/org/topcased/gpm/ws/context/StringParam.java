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

/**
 * The Class StringParam.
 * 
 * @author llatil
 */
public class StringParam extends ContextParam {

    /** serialVersionUID. */
    private static final long serialVersionUID = 5555721753096819044L;

    /** The string value. */
    private String stringValue;

    /**
     * Constructs a new string param.
     */
    public StringParam() {
    }

    /**
     * Constructs a new string param.
     * 
     * @param pName
     *            the name
     * @param pValue
     *            the value
     */
    public StringParam(String pName, String pValue) {
        super(pName);
        stringValue = pValue;
    }

    /**
     * Gets the string value.
     * 
     * @return the string value
     */
    public String getStringValue() {
        return stringValue;
    }

    /**
     * Sets the string value.
     * 
     * @param pValue
     *            the new string value
     */
    public void setStringValue(String pValue) {
        stringValue = pValue;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.context.ContextParam#getValue()
     */
    public Object getValue() {
        return stringValue;
    }
}