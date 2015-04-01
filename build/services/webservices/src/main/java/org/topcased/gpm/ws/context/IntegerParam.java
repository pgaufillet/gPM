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
 * The Class IntegerParam.
 * 
 * @author llatil
 */
public class IntegerParam extends ContextParam {

    /** serialVersionUID. */
    private static final long serialVersionUID = -4198455018299816423L;

    /** The int value. */
    private Integer intValue;

    /**
     * Default ctor
     */
    public IntegerParam() {
    }

    /**
     * Constructs a new integer param.
     * 
     * @param pName
     *            the name
     * @param pValue
     *            the value
     */
    public IntegerParam(String pName, Integer pValue) {
        super(pName);
        intValue = pValue;
    }

    /**
     * Gets the int value.
     * 
     * @return the int value
     */
    public final Integer getIntValue() {
        return intValue;
    }

    /**
     * Sets the int value.
     * 
     * @param pIntValue
     *            the new int value
     */
    public final void setIntValue(Integer pIntValue) {
        intValue = pIntValue;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.context.ContextParam#getValue()
     */
    public Object getValue() {
        return intValue;
    }
}
