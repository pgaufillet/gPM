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
 * Scalar value data for real.
 * 
 * @author tpanuel
 */
public class RealValueData extends ScalarValueData {
    private static final long serialVersionUID = 5505655848663425967L;

    private Double value;

    /**
     * Default constructor.
     */
    public RealValueData() {
    }

    /**
     * Constructor taking all properties.
     * 
     * @param pValue
     *            The value.
     */
    public RealValueData(final Double pValue) {
        value = pValue;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.scalar.ScalarValueData#getValue()
     */
    public Double getValue() {
        return value;
    }

    /**
     * Get the value.
     * 
     * @return The value.
     */
    public Double getRealValue() {
        return value;
    }

    /**
     * Set the value.
     * 
     * @param pValue
     *            The value.
     */
    public void setRealValue(final Double pValue) {
        value = pValue;
    }
}