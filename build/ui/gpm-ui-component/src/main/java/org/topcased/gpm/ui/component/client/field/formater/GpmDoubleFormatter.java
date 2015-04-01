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

/**
 * A formatter for Double values.
 * 
 * @author tpanuel
 */
public final class GpmDoubleFormatter implements GpmFormatter<Double> {
    private final static GpmDoubleFormatter INSTANCE = new GpmDoubleFormatter();

    private GpmDoubleFormatter() {
    }

    /**
     * It is a singleton.
     * 
     * @return The string formatter.
     */
    public final static GpmDoubleFormatter getInstance() {
        return INSTANCE;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.field.formater.GpmFormatter#format(java.lang.Object)
     */
    public String format(final Double pValue) {
        if (pValue == null) {
            return null;
        }
        else {
            return pValue.toString();
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.field.formater.GpmFormatter#parse(java.lang.String)
     */
    public Double parse(final String pValue) {
        if (pValue == null) {
            return null;
        }
        else {
            try {
                return Double.valueOf(pValue);
            }
            catch (NumberFormatException e) {
                return null;
            }
        }
    }
}