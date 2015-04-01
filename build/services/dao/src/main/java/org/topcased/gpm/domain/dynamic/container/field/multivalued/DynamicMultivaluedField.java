/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.domain.dynamic.container.field.multivalued;

import org.topcased.gpm.domain.PersistentObject;

/**
 * Supper class of dynamic multi valued fields
 * 
 * @author tpanuel
 */
public abstract class DynamicMultivaluedField implements PersistentObject {
    /**
     * Getter on the id
     * 
     * @return The id
     */
    abstract public String getId();

    /**
     * Setter on the id
     * 
     * @param pId
     *            The new id
     */
    abstract public void setId(String pId);
}