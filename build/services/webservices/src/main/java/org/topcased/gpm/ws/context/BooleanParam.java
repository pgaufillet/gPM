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
 * The Class BooleanParam.
 * 
 * @author llatil
 */
public class BooleanParam extends ContextParam {

    /** serialVersionUID. */
    private static final long serialVersionUID = 5030672942243171445L;

    /** The boolean value. */
    private Boolean booleanValue;

    /**
     * Default ctor
     */
    public BooleanParam() {
    }

    /**
     * Constructs a new boolean param.
     * 
     * @param pName
     *            the name
     * @param pValue
     *            the value
     */
    public BooleanParam(String pName, Boolean pValue) {
        super(pName);
        booleanValue = pValue;
    }

    /**
     * Gets the boolean value.
     * 
     * @return the boolean value
     */
    public final Boolean getBooleanValue() {
        return booleanValue;
    }

    /**
     * Sets the boolean value.
     * 
     * @param pValue
     *            the new boolean value
     */
    public void setBooleanValue(Boolean pValue) {
        booleanValue = pValue;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ws.context.ContextParam#getValue()
     */
    @Override
    public Object getValue() {
        return booleanValue;
    }
}
