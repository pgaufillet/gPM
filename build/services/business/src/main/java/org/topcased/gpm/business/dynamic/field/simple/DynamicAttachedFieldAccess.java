/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.dynamic.field.simple;

import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.ContextLocator;
import org.topcased.gpm.business.dynamic.field.AbstractDynamicFieldAccess;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.extensions.service.Context;
import org.topcased.gpm.business.serialization.data.AttachedField;
import org.topcased.gpm.business.serialization.data.AttachedFieldValueData;
import org.topcased.gpm.domain.fields.AttachedFieldContentValue;
import org.topcased.gpm.domain.fields.AttachedFieldValue;
import org.topcased.gpm.domain.fields.AttachedFieldValueDao;
import org.topcased.gpm.util.comparator.DataBaseValuesComparator;

/**
 * Access on an attached field
 * 
 * @author tpanuel
 */
public class DynamicAttachedFieldAccess extends
        AbstractDynamicFieldAccess<AttachedFieldValue> {
    private final AttachedFieldValueDao attachedFieldValueDao;

    private static final double MEGA = Math.pow(1024, 2);

    /**
     * Create access on an attached field
     * 
     * @param pField
     *            The attached field
     */
    public DynamicAttachedFieldAccess(AttachedField pField) {
        super(pField, AttachedFieldValue.class);
        attachedFieldValueDao =
                (AttachedFieldValueDao) ContextLocator.getContext().getBean(
                        "attachedFieldValueDao");
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.dynamic.field.AbstractDynamicFieldAccess#getFieldValue(java.lang.Object,
     *      org.topcased.gpm.business.extensions.service.Context)
     */
    @Override
    public Object getFieldValue(Object pDomainContainer, Context pContext) {
        final AttachedFieldValue lDomainValue = getValue(pDomainContainer);

        // If no name, return a null object
        // For memory space optimization
        if (lDomainValue == null || lDomainValue.getName() == null) {
            return null;
        }
        else {
            final AttachedFieldValueData lBusinessValue =
                    new AttachedFieldValueData(getFieldName());

            // Content is not loaded on the business object
            lBusinessValue.setId(lDomainValue.getId());
            lBusinessValue.setFilename(lDomainValue.getName());
            lBusinessValue.setValue(lDomainValue.getName());
            lBusinessValue.setMimeType(lDomainValue.getMimeType());

            return lBusinessValue;
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.dynamic.field.AbstractDynamicFieldAccess#getFieldValueAsString(java.lang.Object,
     *      org.topcased.gpm.business.extensions.service.Context)
     */
    @Override
    public String getFieldValueAsString(Object pDomainContainer,
            Context pContext) {
        final AttachedFieldValue lDomainValue = getValue(pDomainContainer);

        // Attached field as string -> file name
        if (lDomainValue == null) {
            return null;
        }
        else {
            return lDomainValue.getName();
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.dynamic.field.AbstractDynamicFieldAccess#setFieldValue(java.lang.Object,
     *      java.lang.Object,
     *      org.topcased.gpm.business.extensions.service.Context)
     */
    @Override
    public void setFieldValue(Object pDomainContainer, Object pFieldValue,
            Context pContext) {
        // If null, the sub-fields is not updated
        if (pFieldValue != null) {
            // Value must be a AttachedFieldValueData
            if (pFieldValue instanceof AttachedFieldValueData) {
                final AttachedFieldValueData lBusinessValue =
                        (AttachedFieldValueData) pFieldValue;
                // Check the filename is valid
                final String lBusinessId = lBusinessValue.getId();
                final AttachedFieldValue lCurrentDomainValue =
                        getValue(pDomainContainer);
                // The Attached field can be create, re-copied or updated
                final AttachedFieldValue lNewDomainValue;
                // The content can be defined by the business
                // or it can be already on the data base
                // If null, no update necessary
                final byte[] lNewContent;

                // New attached field -> no old value
                if (lCurrentDomainValue == null) {
                    // A new Attached file is created
                    lNewDomainValue = AttachedFieldValue.newInstance();
                    setValue(pDomainContainer, lNewDomainValue);
                    // New content can be defined by the business ...
                    if (lBusinessId == null
                            || lBusinessValue.getNewContent() != null) {
                        lNewContent = lBusinessValue.getNewContent();
                    }
                    // ... or it can be already saved on the data base
                    else {
                        lNewContent = getFileContentFromCacheOrDB(lBusinessId);
                    }
                }
                // An old value exists -> it can be recreated or updated
                else {
                    final String lDomainId = lCurrentDomainValue.getId();

                    // Same id
                    if (StringUtils.equals(lDomainId, lBusinessId)) {
                        // Only update the attached field
                        lNewDomainValue = lCurrentDomainValue;
                        // Use new content defined by the business
                        lNewContent = lBusinessValue.getNewContent();
                    }
                    else {
                        // Recreated the attached field
                        lNewDomainValue = AttachedFieldValue.newInstance();
                        setValue(pDomainContainer, lNewDomainValue);
                        // The old attached field is deleted 
                        attachedFieldValueDao.remove(lCurrentDomainValue);
                        // ... but don't removed attached field referenced by the business,
                        // it will be removed when its container will be deleted

                        if (lCurrentDomainValue.getAttachedFieldContent() != null) {
                            // save the content to re-use it if it was moved to an other index in a multi-valued field
                            localCache.put(
                                    lCurrentDomainValue.getId(),
                                    lCurrentDomainValue.getAttachedFieldContent().getContent());
                        }

                        // Priority on the new content defined by the business ...
                        if (lBusinessId == null
                                || lBusinessValue.getNewContent() != null) {
                            lNewContent = lBusinessValue.getNewContent();
                        }
                        // ... or use the data always on database
                        else {
                            lNewContent =
                                    getFileContentFromCacheOrDB(lBusinessId);
                        }
                    }
                }

                // Update domain attached field, only if different values
                if (!DataBaseValuesComparator.equals(lNewDomainValue.getName(),
                        lBusinessValue.getValue())) {
                    lNewDomainValue.setName(lBusinessValue.getValue());
                }
                if (!DataBaseValuesComparator.equals(
                        lNewDomainValue.getMimeType(),
                        lBusinessValue.getMimeType())) {
                    lNewDomainValue.setMimeType(lBusinessValue.getMimeType());
                }
                if (lNewContent != null) {
                    if (lNewDomainValue.getAttachedFieldContent() == null) {
                        AttachedFieldContentValue.newInstance();
                        lNewDomainValue.setAttachedFieldContent(AttachedFieldContentValue.newInstance());
                    }

                    lNewDomainValue.getAttachedFieldContent().setContent(
                            lNewContent);
                    lNewDomainValue.setFileSize(getFileSize(lNewContent));
                    // Flush the session and evict attached field value
                    // That fix problem of out of memory on Hibernate session
                    attachedFieldValueDao.flush();
                    attachedFieldValueDao.evict(lCurrentDomainValue);
                    attachedFieldValueDao.evict(lNewDomainValue);
                }
                else {
                    if (StringUtils.isBlank(lBusinessValue.getFilename())) {
                        setValue(pDomainContainer, null);
                        attachedFieldValueDao.remove(lCurrentDomainValue);
                    }
                }
            }
            else {
                throw new GDMException(
                        "Invalid value type for attached field: "
                                + pFieldValue.getClass());
            }
        }
    }

    /**
     * get a file content from the correct source. It uses the cache if possible
     * to avoid invalid identifier exceptions during multiple field processing
     * 
     * @param pContentId
     * @return the content as it is stored in DB (byte[]) *
     */
    private byte[] getFileContentFromCacheOrDB(String pContentId) {
        byte[] lResult = null;
        if (localCache.containsKey(pContentId)) {
            lResult = (byte[]) localCache.get(pContentId);
        }
        else {
            AttachedFieldContentValue lAttachedFieldContentValue =
                    ((AttachedFieldValue) attachedFieldValueDao.load(pContentId)).getAttachedFieldContent();
            if (lAttachedFieldContentValue != null) {
                lResult = lAttachedFieldContentValue.getContent();
            }
        }
        return lResult;
    }

    private double getFileSize(byte[] pContent) {
        return (double) pContent.length / MEGA;
    }
}
