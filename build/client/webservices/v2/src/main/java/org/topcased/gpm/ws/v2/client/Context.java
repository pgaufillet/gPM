/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Atos Origin
 ******************************************************************/
package org.topcased.gpm.ws.v2.client;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class Context.
 * 
 * @author llatil
 */
public class Context extends ArrayList<ContextParam> {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new context.
     */
    public Context() {
    }

    /**
     * Put a new String parameter in the context.
     * 
     * @param pName
     *            the name of parameter
     * @param pValue
     *            the value
     */
    public void put(String pName, String pValue) {
        StringParam lParam = new StringParam();
        lParam.setName(pName);
        lParam.setStringValue(pValue);

        add(lParam);
    }

    /**
     * Put a new Boolean parameter in the context.
     * 
     * @param pName
     *            the name of parameter
     * @param pValue
     *            the value
     */
    public void put(String pName, Boolean pValue) {
        BooleanParam lParam = new BooleanParam();
        lParam.setName(pName);
        lParam.setBooleanValue(pValue);
        add(lParam);
    }

    /**
     * Put a new Integer parameter in the context.
     * 
     * @param pName
     *            the name of parameter
     * @param pValue
     *            the value
     */
    public void put(String pName, Integer pValue) {
        IntegerParam lParam = new IntegerParam();
        lParam.setName(pName);
        lParam.setIntValue(pValue);
        add(lParam);
    }

    /**
     * Put a strings list parameter in the context.
     * 
     * @param pName
     *            the name of parameter
     * @param pValue
     *            the value
     */
    public void put(String pName, List<String> pValue) {
        StringsListParam lParam = new StringsListParam();
        lParam.setName(pName);
        lParam.getStringValues().addAll(pValue);

        add(lParam);
    }
}
