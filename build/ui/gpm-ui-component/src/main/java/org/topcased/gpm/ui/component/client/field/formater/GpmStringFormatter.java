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
 * A formatter for String values.
 * 
 * @author tpanuel
 */
public final class GpmStringFormatter implements GpmFormatter<String> {
    private final static GpmStringFormatter INSTANCE = new GpmStringFormatter();

    private GpmStringFormatter() {
    }

    /**
     * It is a singleton.
     * 
     * @return The string formatter.
     */
    public final static GpmStringFormatter getInstance() {
        return INSTANCE;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.field.formater.GpmFormatter#format(java.lang.Object)
     */
    public String format(final String pValue) {
        return pValue;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.field.formater.GpmFormatter#parse(java.lang.String)
     */
    public String parse(final String pValue) {
        return pValue;
    }
}
