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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * The Class AttributeValue.
 * 
 * @author llatil
 */
public class AttributeValue {

    /** The value. */
    private String value;

    /**
     * Constructs a new attribute value.
     */
    public AttributeValue() {
    }

    /**
     * Constructs a new attribute value.
     * 
     * @param pValue
     *            the value
     */
    public AttributeValue(String pValue) {
        value = pValue;
    }

    /**
     * Get the value of the attribute.
     * 
     * @return Attribute value
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value.
     * 
     * @param pValue
     *            the value
     */
    public void setValue(String pValue) {
        value = pValue;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object pOther) {
        if (pOther instanceof AttributeValue) {
            AttributeValue lOther = (AttributeValue) pOther;
            return StringUtils.equals(value, lOther.value);
        }
        return false;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(value).toHashCode();
    }
}
