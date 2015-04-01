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
 * Scalar value data for string.
 * 
 * @author tpanuel
 */
public class StringValueData extends ScalarValueData {
    private static final long serialVersionUID = -7576799179897527179L;

    private String value;

    /**
     * Default constructor.
     */
    public StringValueData() {
    }

    /**
     * Constructor taking all properties.
     * 
     * @param pValue
     *            The value.
     */
    public StringValueData(final String pValue) {
        value = pValue;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.scalar.ScalarValueData#getValue()
     */
    public String getValue() {
        return value;
    }

    /**
     * Get the value.
     * 
     * @return The value.
     */
    public String getStringValue() {
        return value;
    }

    /**
     * Set the value.
     * 
     * @param pValue
     *            The value.
     */
    public void setStringValue(final String pValue) {
        value = pValue;
    }
}