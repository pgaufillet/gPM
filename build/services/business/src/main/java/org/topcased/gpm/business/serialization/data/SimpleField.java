/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin), Sophian Idjellidaine (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.serialization.data;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * This class implement a concrete simple field.
 * 
 * @author sidjelli
 */
@XStreamAlias("simpleField")
public class SimpleField extends Field {

    private static final long serialVersionUID = 3472844671028955197L;

    /** String defining infinite string simple field */
    private static final String INFINITE = "Infinite";

    /** Default value for size */
    public static final int DEFAULT_SIZE = 255;

    /** The default value. */
    @XStreamAsAttribute
    private String defaultValue;

    /** The value type. (Boolean, Real, Integer, Date, String) */
    @XStreamAsAttribute
    private String valueType;

    /**
     * Size of the field (only used for String fields) This size is read in
     * instantiation file : it can be a number or infinite for unlimited field
     */
    @XStreamAsAttribute
    private String maxSize;

    /**
     * Gets the default value.
     * 
     * @return the default value
     */
    public String getDefaultValue() {
        return defaultValue;
    }

    /**
     * Sets the default value.
     * 
     * @param pDefaultValue
     *            the new default value
     */
    public void setDefaultValue(String pDefaultValue) {
        defaultValue = pDefaultValue;
    }

    /**
     * Gets the value type.
     * 
     * @return the value type (Boolean, Real, Integer, Date, String)
     */
    public String getValueType() {
        return valueType;
    }

    /**
     * Sets the value type.
     * 
     * @param pValueType
     *            the new value type
     */
    public void setValueType(String pValueType) {
        valueType = pValueType;
    }

    /**
     * get maxSize
     * 
     * @return the maxSize
     */
    public String getMaxSize() {
        return maxSize;
    }

    /**
     * set maxSize
     * 
     * @param pMaxSize
     *            the maxSize to set
     */
    public void setMaxSize(String pMaxSize) {
        this.maxSize = pMaxSize;
    }

    /**
     * get size
     * 
     * @return the size
     */
    public int getSizeAsInt() {
        int lSize = 0;

        if (maxSize == null) {
            lSize = DEFAULT_SIZE;
        }
        else if (INFINITE.equals(maxSize)) {
            lSize = -1;
        }
        else {
            try {
                lSize = Integer.parseInt(maxSize);
            }
            catch (NumberFormatException ex) {
                lSize = DEFAULT_SIZE;
            }
        }

        return lSize;
    }

    /**
     * set size
     * 
     * @param pSize
     *            the size to set
     */
    public void setSizeAsInt(int pSize) {
        if (pSize == -1) {
            maxSize = INFINITE;
        }
        else {
            maxSize = String.valueOf(pSize);
        }
    }

}
