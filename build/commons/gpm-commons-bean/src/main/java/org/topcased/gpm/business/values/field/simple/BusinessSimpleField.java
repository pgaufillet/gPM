/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 *
 ******************************************************************/
package org.topcased.gpm.business.values.field.simple;

import org.topcased.gpm.business.values.field.BusinessField;

/**
 * Interface used to access on a simple field.
 * 
 * @author tpanuel
 * @param <T>
 *            The type of value
 */
public interface BusinessSimpleField<T> extends BusinessField {
    /**
     * Set the value of the simple field as a string.
     * 
     * @param pValue
     *            The new value of the simple field as a string.
     */
    public void setAsString(final String pValue);

    /**
     * Get the value of the simple field.
     * 
     * @return The value of the simple field.
     */
    public T get();

    /**
     * Set the value of the simple field.
     * 
     * @param pValue
     *            The new value of the simple field.
     */
    public void set(final T pValue);
}