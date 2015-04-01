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
 * Interface used to access on a pointer field.
 * 
 * @author tpanuel
 */
public interface BusinessPointerField extends BusinessField {
    /**
     * Get the pointed container id.
     * 
     * @return The pointed container id.
     */
    public String getPointedContainerId();

    /**
     * Set the pointed container id.
     * 
     * @param pPointedContainerId
     *            The new pointed container id.
     */
    public void setPointedContainerId(final String pPointedContainerId);

    /**
     * Get the pointed field name.
     * 
     * @return The pointed field name.
     */
    public String getPointedFieldName();

    /**
     * Set the pointed field name.
     * 
     * @param pPointedFieldName
     *            The new pointed field name.
     */
    public void setPointedFieldName(final String pPointedFieldName);

    /**
     * Get the pointed field.
     * 
     * @return The pointed field.
     */
    public BusinessField getPointedField();
}