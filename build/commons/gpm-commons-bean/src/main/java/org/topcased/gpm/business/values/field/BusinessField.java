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
package org.topcased.gpm.business.values.field;

/**
 * Interface used to access on a field.
 * 
 * @author tpanuel
 */
public interface BusinessField {
    /**
     * Get the name of the field.
     * 
     * @return The name of the field.
     */
    public String getFieldName();

    /**
     * Get the description of the field.
     * 
     * @return The description of the field.
     */
    public String getFieldDescription();

    /**
     * Test if the field is confidential.
     * 
     * @return If the field is confidential.
     */
    public boolean isConfidential();

    /**
     * Test if the field can be updated.
     * 
     * @return If the field can be updated.
     */
    public boolean isUpdatable();

    /**
     * Test if the field is mandatory.
     * 
     * @return If the field is mandatory.
     */
    // Change Boolean to boolean
    public Boolean isMandatory();

    /**
     * Test if the field can be exported.
     * 
     * @return If the field can be exported.
     */
    public boolean isExportable();

    /**
     * Get the value of the simple field as a string.
     * 
     * @return The value of the simple field as a string.
     */
    public String getAsString();

    /**
     * Test if two field access has the same values.
     * 
     * @param pOther
     *            The other field access.
     * @return If the two field access has the same values.
     */
    public boolean hasSameValues(final BusinessField pOther);

    /**
     * Test if the value is empty.
     * 
     * @return If the value is empty.
     */
    public boolean isEmpty();

    /**
     * Clear all the values, the field will be empty.
     */
    public void clear();

    /**
     * Reset all the values, the field will be empty or initialized with default
     * value.
     */
    public void reset();

    /**
     * Copy the values of an other business field.
     * 
     * @param pOther
     *            The business field that defined the new value.
     */
    public void copy(final BusinessField pOther);
}