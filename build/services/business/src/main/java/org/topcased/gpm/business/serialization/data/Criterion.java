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

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * The Class Criterion.
 * 
 * @author llatil
 */
@XStreamAlias("criterion")
public class Criterion implements Serializable {
    
    /**
     * Serialization ID 
     */
    private static final long serialVersionUID = 1L;

    /** The field key. */
    @XStreamAsAttribute
    private String fieldKey;

    /** The operator. */
    @XStreamAsAttribute
    private String operator;

    /** The value. */
    @XStreamAsAttribute
    private String value;

    /** The case sensitivity. */
    @XStreamAsAttribute
    private Boolean caseSensitive;

    /**
     * Constructs a new criterion.
     * 
     * @param pFieldKey
     *            the field key
     * @param pOperator
     *            the operator
     * @param pValue
     *            the value
     */
    public Criterion(String pFieldKey, String pOperator, String pValue) {
        super();
        fieldKey = pFieldKey;
        operator = pOperator;
        value = pValue;
    }

    /**
     * Gets the field key.
     * 
     * @return the field key
     */
    public String getFieldKey() {
        return fieldKey;
    }

    /**
     * Gets the operator.
     * 
     * @return the operator
     */
    public String getOperator() {
        return operator;
    }

    /**
     * Gets the value.
     * 
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * get caseSensitive
     * 
     * @return the caseSensitive
     */
    public Boolean getCaseSensitive() {
        return caseSensitive;
    }
}
