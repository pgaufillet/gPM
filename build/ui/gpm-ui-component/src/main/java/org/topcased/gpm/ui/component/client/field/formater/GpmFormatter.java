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
 * A formatter for a specific type of object.
 * 
 * @author tpanuel
 * @param <T>
 *            The type of object.
 */
public interface GpmFormatter<T> {
    /**
     * Format a value.
     * 
     * @param pValue
     *            The value.
     * @return The formatted value.
     */
    public String format(final T pValue);

    /**
     * Parse a value.
     * 
     * @param pValue
     *            The value.
     * @return The parsed value.
     */
    public T parse(final String pValue);
}
