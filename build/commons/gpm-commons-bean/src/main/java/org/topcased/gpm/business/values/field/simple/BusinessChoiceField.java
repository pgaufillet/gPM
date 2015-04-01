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
 * Interface used to access on a choice field.
 * 
 * @author tpanuel
 */
public interface BusinessChoiceField extends BusinessField {
    /**
     * Get the category value used by the choice field access.
     * 
     * @return The category value used by the choice field access.
     */
    public String getCategoryValue();

    /**
     * Set the category value used by the choice field access.
     * 
     * @param pValue
     *            The new category value used by the choice field access.
     */
    public void setCategoryValue(final String pValue);
}