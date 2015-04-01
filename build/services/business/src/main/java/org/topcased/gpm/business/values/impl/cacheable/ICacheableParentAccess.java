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
package org.topcased.gpm.business.values.impl.cacheable;

import org.topcased.gpm.business.serialization.data.Field;
import org.topcased.gpm.business.values.ValuesAccessProperties;
import org.topcased.gpm.business.values.field.impl.cacheable.AbstractCacheableFieldAccess;

/**
 * Interface for all access (container, multiple and multi valued) that used sub
 * access.
 * 
 * @author tpanuel
 */
public interface ICacheableParentAccess {
    /**
     * Get a field type.
     * 
     * @param pFieldName
     *            The name of the field.
     * @return The field type.
     */
    public Field getFieldType(final String pFieldName);

    /**
     * Get a field value.
     * 
     * @param pFieldName
     *            The name of the field.
     * @return The field's value.
     */
    public Object getValue(final String pFieldName);

    /**
     * Add a new value on the parent container when a user modify a null value
     * via access.
     * 
     * @param pFieldAccess
     *            The field access.
     */
    public void addNewValue(
            final AbstractCacheableFieldAccess<?, ?> pFieldAccess);

    /**
     * Get the values access properties.
     * 
     * @return The values access properties.
     */
    public ValuesAccessProperties getProperties();

    /**
     * Get the role token used to create the access.
     * 
     * @return The role token.
     */
    public String getRoleToken();
}