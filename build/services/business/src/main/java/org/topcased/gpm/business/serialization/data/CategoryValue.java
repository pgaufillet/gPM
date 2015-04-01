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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * This class implement a concrete categoryValue.
 * 
 * @author sidjelli
 */
@XStreamAlias("categoryValue")
public class CategoryValue implements Serializable {

    /** Generated UID */
    private static final long serialVersionUID = 2491657755923835893L;

    /** The value of the categoryValue. */
    @XStreamAsAttribute
    private String value;

    /**
     * Gets the value.
     * 
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * Constructs a new category value.
     * 
     * @param pValue
     *            the value
     */
    public CategoryValue(String pValue) {
        super();
        this.value = pValue;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object pOther) {

        if (pOther instanceof CategoryValue) {
            CategoryValue lOther = (CategoryValue) pOther;

            if (!StringUtils.equals(value, lOther.value)) {
                return false;
            }

            return true;
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
