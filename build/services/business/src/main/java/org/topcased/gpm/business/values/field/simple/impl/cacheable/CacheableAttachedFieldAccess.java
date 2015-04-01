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
import org.topcased.gpm.business.ServiceLocator;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.serialization.data.AttachedField;
import org.topcased.gpm.business.serialization.data.AttachedFieldValueData;
import org.topcased.gpm.business.values.field.BusinessField;
import org.topcased.gpm.business.values.field.impl.cacheable.AbstractCacheableFieldAccess;
import org.topcased.gpm.business.values.field.simple.BusinessAttachedField;
import org.topcased.gpm.business.values.impl.cacheable.ICacheableParentAccess;

/**
 * Access on an attached field.
 * 
 * @author tpanuel
 */
public class CacheableAttachedFieldAccess extends
        AbstractCacheableFieldAccess<AttachedField, AttachedFieldValueData>
        implements BusinessAttachedField {
    private byte[] content;

    /**
     * Create an access on an empty attached field.
     * 
     * @param pAttachedField
     *            The attached field to access.
     * @param pParentAccess
     *            The parent access.
     */
    public CacheableAttachedFieldAccess(final AttachedField pAttachedField,
            final ICacheableParentAccess pParentAccess) {
        super(pAttachedField, new AttachedFieldValueData(
                pAttachedField.getLabelKey()), pParentAccess, false);
        content = null;
    }

    /**
     * Create an access on an attached field.
     * 
     * @param pAttachedField
     *            The attached field to access.
     * @param pAttachedFieldValue
     *            The attached field value to access.
     * @param pParentAccess
     *            The parent access.
     */
    public CacheableAttachedFieldAccess(final AttachedField pAttachedField,
            final AttachedFieldValueData pAttachedFieldValue,
            final ICacheableParentAccess pParentAccess) {
        super(pAttachedField, pAttachedFieldValue, pParentAccess, true);
        content = pAttachedFieldValue.getNewContent();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessAttachedField#getId()
     */
    public String getId() {
        return read().getId();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessAttachedField#setId(java.lang.String)
     */
    public void setId(final String pId) {
        write().setId(pId);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessAttachedField#getFileName()
     */
    public String getFileName() {
        return read().getFilename();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessAttachedField#setFileName(java.lang.String)
     */
    public void setFileName(final String pFileName) {
        write().setFilename(pFileName);
        write().setValue(pFileName);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessAttachedField#getMimeType()
     */
    public String getMimeType() {
        return read().getMimeType();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessAttachedField#setMimeType(java.lang.String)
     */
    public void setMimeType(final String pMimeType) {
        write().setMimeType(pMimeType);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessAttachedField#getContent()
     */
    public byte[] getContent() {
        // If no new content specified, load the old one on data base
        if (content == null) {
            final String lFileId = read().getId();

            if (lFileId != null) {
                content =
                        ServiceLocator.instance().getSheetService().getAttachedFileContent(
                                getRoleToken(), lFileId);
            }
        }
        return content;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessAttachedField#setContent(byte[])
     */
    public void setContent(final byte[] pContent) {
        write().setNewContent(pContent);
        content = pContent;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#hasSameValues(org.topcased.gpm.business.values.field.BusinessField)
     */
    public boolean hasSameValues(final BusinessField pOther) {
        if (!(pOther instanceof BusinessAttachedField)) {
            return false;
        }
        else if (pOther == null || pOther.isEmpty()) {
            return isEmpty();
        }
        else {
            final BusinessAttachedField lOther = (BusinessAttachedField) pOther;

            return StringUtils.equals(getId(), lOther.getId())
                    && StringUtils.equals(getFileName(), lOther.getFileName());
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#isEmpty()
     */
    public boolean isEmpty() {
        return StringUtils.isEmpty(getId())
                && StringUtils.isEmpty(getFileName());
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#clear()
     */
    public void clear() {
        setId(null);
        setFileName(null);
        setMimeType(null);
        setContent(null);
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
            if (pOther instanceof BusinessAttachedField) {
                final BusinessAttachedField lOther =
                        (BusinessAttachedField) pOther;

                if ((!StringUtils.isBlank(lOther.getFileName()) && lOther.getContent() != null)
                        || lOther.getId() != null) {
                    setId(lOther.getId());
                    setFileName(lOther.getFileName());
                    setMimeType(lOther.getMimeType());
                    setContent(lOther.getContent());
                }
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
        return getFileName();
    }
}