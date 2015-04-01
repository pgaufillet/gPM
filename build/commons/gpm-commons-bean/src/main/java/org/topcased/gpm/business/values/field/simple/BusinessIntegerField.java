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

/**
 * Interface used to access on a simple field of type Integer.
 * 
 * @author tpanuel
 */
public interface BusinessIntegerField extends BusinessSimpleField<Integer> {
    /**
     * Increment the value. If no value, default value + 1 or 1.
     */
    public void increment();

    /**
     * Decrement the value. If no value, default value or 0.
     */
    public void decrement();
}