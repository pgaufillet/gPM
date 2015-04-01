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
package org.topcased.gpm.business.values;

import org.topcased.gpm.business.values.field.BusinessFieldSet;
import org.topcased.gpm.business.values.field.virtual.BusinessVirtualField;
import org.topcased.gpm.business.values.field.virtual.VirtualFieldType;

/**
 * Interface used to access on a container.
 * 
 * @author tpanuel
 */
public interface BusinessContainer extends BusinessFieldSet {
    /**
     * Get the id of the container.
     * 
     * @return The id of the container.
     */
    public String getId();

    /**
     * Get the id of the type of container.
     * 
     * @return The id of the type of container.
     */
    public String getTypeId();

    /**
     * Get the name of the fields container.
     * 
     * @return The name of the fields container.
     */
    public String getTypeName();

    /**
     * Get the name of the business process.
     * 
     * @return The name of the business process.
     */
    public String getBusinessProcessName();

    /**
     * Get the description of the fields container.
     * 
     * @return The description of the fields container.
     */
    public String getTypeDescription();

    /**
     * Test if the fields container is confidential.
     * 
     * @return If the fields container is confidential.
     */
    public boolean isConfidential();

    /**
     * Test if the fields container can be update.
     * 
     * @return If the fields container can be update.
     */
    public boolean isUpdatable();

    /**
     * Test if the fields container can be delete.
     * 
     * @return If the fields container can be delete.
     */
    public boolean isDeletable();

    /**
     * Get a virtual field access.
     * 
     * @param pVirtualFieldType
     *            The type of virtual field.
     * @return The virtual field access.
     */
    public BusinessVirtualField getVirtualField(
            final VirtualFieldType pVirtualFieldType);
}