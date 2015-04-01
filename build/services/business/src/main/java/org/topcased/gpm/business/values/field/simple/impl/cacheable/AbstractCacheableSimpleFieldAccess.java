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
package org.topcased.gpm.business.values.field.simple.impl.cacheable;

import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.serialization.data.FieldValueData;
import org.topcased.gpm.business.serialization.data.SimpleField;
import org.topcased.gpm.business.values.field.BusinessField;
import org.topcased.gpm.business.values.field.impl.cacheable.AbstractCacheableFieldAccess;
import org.topcased.gpm.business.values.field.simple.BusinessSimpleField;
import org.topcased.gpm.business.values.impl.cacheable.ICacheableParentAccess;

/**
 * Abstract access on a simple field.
 * 
 * @author tpanuel
 * @param <T>
 *            The type of the simple field.
 */
public abstract class AbstractCacheableSimpleFieldAccess<T> extends
        AbstractCacheableFieldAccess<SimpleField, FieldValueData> implements
        BusinessSimpleField<T> {
    /**
     * Create an access to an empty simple field.
     * 
     * @param pSimpleField
     *            The simple field to access.
     * @param pParentAccess
     *            The parent access.
     */
    public AbstractCacheableSimpleFieldAccess(final SimpleField pSimpleField,
            final ICacheableParentAccess pParentAccess) {
        super(pSimpleField, new FieldValueData(pSimpleField.getLabelKey()),
                pParentAccess, false);
    }

    /**
     * Create an access to a simple field.
     * 
     * @param pSimpleField
     *            The simple field to access.
     * @param pValue
     *            The field value data to access.
     * @param pParentAccess
     *            The parent access.
     */
    public AbstractCacheableSimpleFieldAccess(final SimpleField pSimpleField,
            final FieldValueData pValue,
            final ICacheableParentAccess pParentAccess) {
        super(pSimpleField, pValue, pParentAccess, true);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessSimpleField#setAsString(java.lang.String)
     */
    public void setAsString(final String pValue) {
        write().setValue(pValue);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessSimpleField#get()
     */
    public T get() {
        return parse(getAsString());
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessSimpleField#set(java.lang.Object)
     */
    public void set(final T pValue) {
        setAsString(format(pValue));
    }

    /**
     * Format a value.
     * 
     * @param pValue
     *            The value to format.
     * @return The formatted value.
     */
    abstract protected String format(final T pValue);

    /**
     * Parse a value.
     * 
     * @param pValue
     *            The value to parse.
     * @return The parsed value.
     */
    abstract protected T parse(final String pValue);

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#hasSameValues(org.topcased.gpm.business.values.field.BusinessField)
     */
    @SuppressWarnings("unchecked")
    public boolean hasSameValues(final BusinessField pOther) {
        if (!(pOther instanceof BusinessSimpleField)) {
            return false;
        }
        else if (pOther == null || pOther.isEmpty()) {
            return isEmpty();
        }
        else {
            final T lValue = get();
            final T lOtherValue = ((BusinessSimpleField<T>) pOther).get();

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
        return StringUtils.isEmpty(getAsString());
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#clear()
     */
    public void clear() {
        setAsString(null);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#reset()
     */
    public void reset() {
        setAsString(getFieldType().getDefaultValue());
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#copy(org.topcased.gpm.business.values.field.BusinessField)
     */
    @SuppressWarnings("unchecked")
    public void copy(final BusinessField pOther) {
        if (pOther != null) {
            if (pOther instanceof BusinessSimpleField) {
                final BusinessSimpleField<?> lOther =
                        (BusinessSimpleField<?>) pOther;

                set((T) lOther.get());
            }
            else {
                throw new GDMException("Cannot copy "
                        + pOther.getClass().getName() + " on "
                        + getClass().getName() + ".");
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return read().getValue();
    }
}