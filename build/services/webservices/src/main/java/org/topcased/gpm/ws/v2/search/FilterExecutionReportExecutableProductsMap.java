/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien EBALLARD (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ws.v2.search;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * FilterExecutionReportExecutableProductsMap. This object represents a Map
 * which is not allowed to transit over network. Keys and Values are wrapped as
 * two List, one for the keys and one for the values. The value associated to a
 * Key is placed in the values list at the same index as the key in the keys
 * list.
 * 
 * @author jeballar
 */
public class FilterExecutionReportExecutableProductsMap implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = -5519896650043581986L;

    /** Role names */
    private List<String> keys = new ArrayList<String>();

    /** Executable products */
    private List<String[]> values = new ArrayList<String[]>();

    /**
     * Default empty constructor
     */
    public FilterExecutionReportExecutableProductsMap() {
    }

    /**
     * Get keys.
     * 
     * @return the keys
     */
    public List<String> getKeys() {
        return keys;
    }

    /**
     * Set keys
     * 
     * @param pKeys
     *            the keys to set
     */
    public void setKeys(List<String> pKeys) {
        keys = pKeys;
    }

    /**
     * Get values
     * 
     * @return the values
     */
    public List<String[]> getValues() {
        return values;
    }

    /**
     * Set values
     * 
     * @param pValues
     *            the values to set
     */
    public void setValues(List<String[]> pValues) {
        values = pValues;
    }

    /**
     * Insert a value in the Map
     * 
     * @param pKey
     *            Key
     * @param pValue
     *            Value
     * @return The inserted Value
     */
    public String[] put(String pKey, List<String> pValue) {
        int lIndex = keys.indexOf(pKey);
        String[] lValue = null;
        if (lIndex != -1) { // Key exists
            lValue = values.get(lIndex);
            values.set(lIndex, pValue.toArray(new String[pValue.size()]));
        }
        else { // Key does not exist
            keys.add(pKey);
            values.add(pValue.toArray(new String[pValue.size()]));
        }
        return lValue;
    }
}
