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
package org.topcased.gpm.business.values.field.virtual.impl.cacheable;

import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.exception.AuthorizationException;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.values.ValuesAccessProperties;
import org.topcased.gpm.business.values.field.BusinessField;
import org.topcased.gpm.business.values.field.virtual.BusinessVirtualField;
import org.topcased.gpm.business.values.field.virtual.VirtualFieldType;
import org.topcased.gpm.business.values.impl.cacheable.AbstractCacheableContainerAccess;

/**
 * An abstract virtual field on a CacheableValues object.
 * 
 * @author tpanuel
 * @param <C>
 *            The type of container.
 */
public abstract class AbstractCacheableVirtualFieldAccess<C extends AbstractCacheableContainerAccess<?, ?>>
        implements BusinessVirtualField {
    private final VirtualFieldType virtualFieldType;

    private final C containerAccess;

    private final ValuesAccessProperties properties;

    /**
     * Create a access on virtual field of a container.
     * 
     * @param pVirtualFieldType
     *            The type of the virtual field.
     * @param pContainerAccess
     *            The container to access.
     */
    public AbstractCacheableVirtualFieldAccess(
            final VirtualFieldType pVirtualFieldType, final C pContainerAccess) {
        virtualFieldType = pVirtualFieldType;
        containerAccess = pContainerAccess;
        properties = containerAccess.getProperties();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#getFieldName()
     */
    public String getFieldName() {
        return virtualFieldType.getValue();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#getFieldDescription()
     */
    public String getFieldDescription() {
        return "The virtual field " + virtualFieldType.getValue() + ".";
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#isConfidential()
     */
    public boolean isConfidential() {
        return containerAccess.isConfidential();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#isUpdatable()
     */
    public boolean isUpdatable() {
        return false;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#isMandatory()
     */
    public Boolean isMandatory() {
        return true;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#isExportable()
     */
    public boolean isExportable() {
        return !isConfidential();
    }

    /**
     * Get the value to read only container.
     * 
     * @return The container to access.
     */
    public C read() {
        if (properties.isCheckAccessRight() && isConfidential()) {
            throw new AuthorizationException(getFieldName(),
                    "The field is confidential");
        }

        return containerAccess;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#getAsString()
     */
    public String getAsString() {
        return toString();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#clear()
     */
    public void clear() {
        throw new GDMException("Cannot clear a virtual field.");
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#reset()
     */
    public void reset() {
        throw new GDMException("Cannot reset a virtual field.");
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#copy(org.topcased.gpm.business.values.field.BusinessField)
     */
    public void copy(final BusinessField pOther) {
        throw new GDMException("Cannot copy a virtual field.");
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#hasSameValues(org.topcased.gpm.business.values.field.BusinessField)
     */
    public boolean hasSameValues(final BusinessField pOther) {
        if (!(pOther instanceof BusinessVirtualField)) {
            return false;
        }
        else if (pOther == null || pOther.isEmpty()) {
            return isEmpty();
        }
        else {
            final String lValue = getValue();
            final String lOtherValue =
                    ((BusinessVirtualField) pOther).getValue();

            if (lValue == null) {
                return lOtherValue == null;
            }
            else {
                return lOtherValue != null && lValue.equals(lOtherValue);
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#isEmpty()
     */
    public boolean isEmpty() {
        return StringUtils.isEmpty(getValue());
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return getValue();
    }
}