/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.field.formater;

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;

/**
 * A formatter for Date values.
 * 
 * @author tpanuel
 */
public abstract class AbstractGpmDateFormatter implements GpmFormatter<Date> {
    private final DateTimeFormat format;

    /**
     * Create a gPM date formatter.
     * 
     * @param pFormat
     *            The format.
     */
    protected AbstractGpmDateFormatter(final DateTimeFormat pFormat) {
        format = pFormat;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.field.formater.GpmFormatter#format(java.lang.Object)
     */
    public String format(final Date pValue) {
        if (pValue == null) {
            return null;
        }
        else {
            return format.format(pValue);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.field.formater.GpmFormatter#parse(java.lang.String)
     */
    public Date parse(final String pValue) {
        if (pValue == null || pValue.trim().isEmpty()) {
            return null;
        }
        else {
            try {
                return format.parse(pValue);
            }
            catch (IllegalArgumentException e) {
                return null;
            }
        }
    }
}