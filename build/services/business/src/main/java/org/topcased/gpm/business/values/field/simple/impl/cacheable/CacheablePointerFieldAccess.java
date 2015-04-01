/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
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
import org.topcased.gpm.business.ServiceLocator;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.serialization.data.Field;
import org.topcased.gpm.business.serialization.data.PointerFieldValueData;
import org.topcased.gpm.business.values.field.BusinessField;
import org.topcased.gpm.business.values.field.impl.cacheable.AbstractCacheableFieldAccess;
import org.topcased.gpm.business.values.field.simple.BusinessPointerField;
import org.topcased.gpm.business.values.impl.cacheable.ICacheableParentAccess;

/**
 * Access on a pointer field.
 * 
 * @author tpanuel
 */
public class CacheablePointerFieldAccess extends
        AbstractCacheableFieldAccess<Field, PointerFieldValueData> implements
        BusinessPointerField {
    private BusinessField pointedField;

    /**
     * Create an access on an empty pointer field.
     * 
     * @param pPointerField
     *            The pointer field to access.
     * @param pParentAccess
     *            The parent access.
     */
    public CacheablePointerFieldAccess(final Field pPointerField,
            final ICacheableParentAccess pParentAccess) {
        super(pPointerField, new PointerFieldValueData(
                pPointerField.getLabelKey()), pParentAccess, false);
        // Field must be a pointer
        if (!pPointerField.isPointerField()) {
            throw new GDMException(pPointerField.getLabelKey()
                    + " is not a pointer.");
        }
        pointedField = null;
    }

    /**
     * Create an access on a pointer field.
     * 
     * @param pPointerField
     *            The pointer field to access.
     * @param pPointerFieldValue
     *            The pointer field value data to access.
     * @param pParentAccess
     *            The parent access.
     */
    public CacheablePointerFieldAccess(final Field pPointerField,
            final PointerFieldValueData pPointerFieldValue,
            final ICacheableParentAccess pParentAccess) {
        super(pPointerField, pPointerFieldValue, pParentAccess, true);
        // Field must be a pointer
        if (!pPointerField.isPointerField()) {
            throw new GDMException(pPointerField.getLabelKey()
                    + " is not a pointer.");
        }
        pointedField = null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessPointerField#getPointedContainerId()
     */
    public String getPointedContainerId() {
        return read().getReferencedContainerId();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessPointerField#setPointedContainerId(java.lang.String)
     */
    public void setPointedContainerId(String pPointedContainerId) {
        write().setReferencedContainerId(pPointedContainerId);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessPointerField#getPointedFieldName()
     */
    public String getPointedFieldName() {
        return read().getReferencedFieldLabel();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessPointerField#setPointedFieldName(java.lang.String)
     */
    public void setPointedFieldName(String pPointedFieldName) {
        write().setReferencedFieldLabel(pPointedFieldName);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessPointerField#getPointedField()
     */
    public BusinessField getPointedField() {
        if (pointedField == null && getPointedContainerId() != null) {
            pointedField =
                    ServiceLocator.instance().getFieldsService().getPointedField(
                            getRoleToken(), getFieldName(),
                            getPointedContainerId(), getPointedFieldName());
        }

        return pointedField;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#hasSameValues(org.topcased.gpm.business.values.field.BusinessField)
     */
    public boolean hasSameValues(final BusinessField pOther) {
        if (!(pOther instanceof BusinessPointerField)) {
            return false;
        }
        else if (pOther == null || pOther.isEmpty()) {
            return isEmpty();
        }
        else {
            final BusinessPointerField lOther = (BusinessPointerField) pOther;

            return StringUtils.equals(getPointedContainerId(),
                    lOther.getPointedContainerId())
                    && StringUtils.equals(getPointedFieldName(),
                            lOther.getPointedFieldName());
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#isEmpty()
     */
    public boolean isEmpty() {
        return StringUtils.isEmpty(getPointedContainerId())
                && StringUtils.isEmpty(getPointedFieldName());
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#clear()
     */
    public void clear() {
        setPointedContainerId(null);
        setPointedFieldName(null);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#reset()
     */
    public void reset() {
        clear();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#copy(org.topcased.gpm.business.values.field.BusinessField)
     */
    public void copy(final BusinessField pOther) {
        if (pOther != null) {
            if (pOther instanceof BusinessPointerField) {
                final BusinessPointerField lOther =
                        (BusinessPointerField) pOther;

                setPointedContainerId(lOther.getPointedContainerId());
                setPointedFieldName(lOther.getPointedFieldName());
            }
            //ELSE pointer field is considered as transformed to StringField
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        BusinessField lPointedField = getPointedField();
        if (lPointedField != null) {
            return lPointedField.toString();
        }
        return StringUtils.EMPTY;
    }
}