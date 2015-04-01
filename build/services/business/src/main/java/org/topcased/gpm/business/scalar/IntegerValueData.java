/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 ******************************************************************/
package org.topcased.gpm.business.scalar;

/**
 * Scalar value data for integer.
 * 
 * @author tpanuel
 */
public class IntegerValueData extends ScalarValueData {
    private static final long serialVersionUID = -2951714376913467192L;

    private Integer value;

    /**
     * Default constructor.
     */
    public IntegerValueData() {
    }

    /**
     * Constructor taking all properties.
     * 
     * @param pValue
     *            The value.
     */
    public IntegerValueData(final Integer pValue) {
        value = pValue;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.scalar.ScalarValueData#getValue()
     */
    public Integer getValue() {
        return value;
    }

    /**
     * Get the value.
     * 
     * @return The value.
     */
    public Integer getIntegerValue() {
        return value;
    }

    /**
     * Set the value.
     * 
     * @param pValue
     *            The value.
     */
    public void setIntegerValue(final Integer pValue) {
        value = pValue;
    }
}