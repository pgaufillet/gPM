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

import java.io.Serializable;

/**
 * The Class ContextParam.
 * 
 * @author llatil
 */
public abstract class ContextParam implements Serializable {

    /** Serialization UID. */
    private static final long serialVersionUID = -4899502065933035312L;

    /** Parameter name. */
    private String name;

    /**
     * Constructs a new context param.
     */
    public ContextParam() {
    }

    /**
     * Constructs a new context param.
     * 
     * @param pName
     *            the name
     */
    public ContextParam(String pName) {
        name = pName;
    }

    /**
     * Creates the.
     * 
     * @param pName
     *            the name
     * @param pValue
     *            the value
     * @return the context param
     */
    public static ContextParam create(String pName, String pValue) {
        StringParam lStringParam = new StringParam();
        lStringParam.setName(pName);
        lStringParam.setStringValue(pValue);
        return lStringParam;
    }

    /**
     * Gets the value.
     * 
     * @return the value
     */
    public abstract Object getValue();

    /**
     * Sets the name.
     * 
     * @param pName
     *            the new name
     */
    public void setName(String pName) {
        name = pName;
    }

    /**
     * Gets the name.
     * 
     * @return the name
     */
    public String getName() {
        return name;
    }
}
