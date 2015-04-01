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
 * Scalar value data for boolean.
 * 
 * @author tpanuel
 */
public class BooleanValueData extends ScalarValueData {
    private static final long serialVersionUID = -2384429330958249828L;

    private Boolean value;

    /**
     * Default constructor.
     */
    public BooleanValueData() {
    }

    /**
     * Constructor taking all properties.
     * 
     * @param pValue
     *            The value.
     */
    public BooleanValueData(final Boolean pValue) {
        value = pValue;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.scalar.ScalarValueData#getValue()
     */
    public Boolean getValue() {
        return value;
    }

    /**
     * Get the value.
     * 
     * @return The value.
     */
    public Boolean getBooleanValue() {
        return value;
    }

    /**
     * Set the value.
     * 
     * @param pValue
     *            The value.
     */
    public void setBooleanValue(final Boolean pValue) {
        value = pValue;
    }
}