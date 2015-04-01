/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 ******************************************************************/
package org.topcased.gpm.business.scalar;

import java.util.Date;

/**
 * Scalar value data for date.
 * 
 * @author tpanuel
 */
public class DateValueData extends ScalarValueData {
    private static final long serialVersionUID = 3190173290797894199L;

    private Date value;

    /**
     * Default constructor.
     */
    public DateValueData() {
    }

    /**
     * Constructor taking all properties.
     * 
     * @param pValue
     *            The value.
     */
    public DateValueData(final Date pValue) {
        value = pValue;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.scalar.ScalarValueData#getValue()
     */
    public Date getValue() {
        return value;
    }

    /**
     * Get the value.
     * 
     * @return The value.
     */
    public Date getDateValue() {
        return value;
    }

    /**
     * Set the value.
     * 
     * @param pValue
     *            The value.
     */
    public void setDateValue(final Date pValue) {
        value = pValue;
    }
}