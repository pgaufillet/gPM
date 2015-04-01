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
import org.topcased.gpm.business.serialization.data.ChoiceField;
import org.topcased.gpm.business.serialization.data.FieldValueData;
import org.topcased.gpm.business.values.field.BusinessField;
import org.topcased.gpm.business.values.field.impl.cacheable.AbstractCacheableFieldAccess;
import org.topcased.gpm.business.values.field.simple.BusinessChoiceField;
import org.topcased.gpm.business.values.impl.cacheable.ICacheableParentAccess;

/**
 * Access on a choice field.
 * 
 * @author tpanuel
 */
public class CacheableChoiceFieldAccess extends
        AbstractCacheableFieldAccess<ChoiceField, FieldValueData> implements
        BusinessChoiceField {
    /**
     * Create an access on an empty choice field.
     * 
     * @param pChoiceField
     *            The choice field to access.
     * @param pParentAccess
     *            The parent access.
     */
    public CacheableChoiceFieldAccess(final ChoiceField pChoiceField,
            final ICacheableParentAccess pParentAccess) {
        super(pChoiceField, new FieldValueData(pChoiceField.getLabelKey()),
                pParentAccess, false);
    }

    /**
     * Create an access on a choice field.
     * 
     * @param pChoiceField
     *            The choice field to access.
     * @param pValue
     *            The field value data to access.
     * @param pParentAccess
     *            The parent access.
     */
    public CacheableChoiceFieldAccess(final ChoiceField pChoiceField,
            final FieldValueData pValue,
            final ICacheableParentAccess pParentAccess) {
        super(pChoiceField, pValue, pParentAccess, true);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessChoiceField#getCategoryValue()
     */
    public String getCategoryValue() {
        return read().getValue();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessChoiceField#setCategoryValue(java.lang.String)
     */
    public void setCategoryValue(final String pValue) {
        write().setValue(pValue);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#hasSameValues(org.topcased.gpm.business.values.field.BusinessField)
     */
    public boolean hasSameValues(final BusinessField pOther) {
        if (!(pOther instanceof BusinessChoiceField)) {
            return false;
        }
        else if (pOther == null || pOther.isEmpty()) {
            return isEmpty();
        }
        else {
            return StringUtils.equals(getCategoryValue(),
                    ((BusinessChoiceField) pOther).getCategoryValue());
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#isEmpty()
     */
    public boolean isEmpty() {
        return StringUtils.isEmpty(getCategoryValue());
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#clear()
     */
    public void clear() {
        setCategoryValue(null);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#reset()
     */
    public void reset() {
        setCategoryValue(getFieldType().getDefaultValue());
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#copy(org.topcased.gpm.business.values.field.BusinessField)
     */
    public void copy(final BusinessField pOther) {
        if (pOther != null) {
            if (pOther instanceof BusinessChoiceField) {
                final BusinessChoiceField lOther = (BusinessChoiceField) pOther;

                setCategoryValue(lOther.getCategoryValue());
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
        return getCategoryValue();
    }
}