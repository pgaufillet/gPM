/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.values.field.simple.impl.pointed;

import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.ServiceLocator;
import org.topcased.gpm.business.exception.MethodNotImplementedException;
import org.topcased.gpm.business.values.field.BusinessField;
import org.topcased.gpm.business.values.field.impl.pointed.AbstractBusinessPointedField;
import org.topcased.gpm.business.values.field.simple.BusinessAttachedField;

/**
 * BusinessAttachedPointedField
 * 
 * @author nveillet
 */
public class BusinessAttachedPointedField extends AbstractBusinessPointedField
        implements BusinessAttachedField {

    private byte[] content;

    final private String fileName;

    final private String id;

    final private String mimeType;

    final private String roleToken;

    /**
     * Constructor
     * 
     * @param pRoleToken
     *            the user role token
     * @param pFieldName
     *            The field name
     * @param pFieldDescription
     *            The field description
     * @param pId
     *            The identifier
     * @param pFileName
     *            The file name
     * @param pMimeType
     *            The file mime type
     */
    public BusinessAttachedPointedField(String pRoleToken, String pFieldName,
            String pFieldDescription, String pId, String pFileName,
            String pMimeType) {
        super(pFieldName, pFieldDescription);
        roleToken = pRoleToken;
        id = pId;
        fileName = pFileName;
        mimeType = pMimeType;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.impl.pointed.AbstractBusinessPointedField#getAsString()
     */
    @Override
    public String getAsString() {
        return fileName;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessAttachedField#getContent()
     */
    public byte[] getContent() {
        // If no new content specified, load the old one on data base
        if (content == null) {
            final String lFileId = getId();

            if (lFileId != null) {
                content =
                        ServiceLocator.instance().getSheetService().getAttachedFileContent(
                                roleToken, lFileId);
            }
        }
        return content;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessAttachedField#getFileName()
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessAttachedField#getId()
     */
    public String getId() {
        return id;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessAttachedField#getMimeType()
     */
    public String getMimeType() {
        return mimeType;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#hasSameValues(org.topcased.gpm.business.values.field.BusinessField)
     */
    public boolean hasSameValues(BusinessField pOther) {
        final BusinessAttachedField lOtherAttached =
                (BusinessAttachedField) pOther;
        String lId1 = id;
        if (lId1 == null) {
            lId1 = StringUtils.EMPTY;
        }
        String lId2 = lOtherAttached.getId();
        if (lId2 == null) {
            lId2 = StringUtils.EMPTY;
        }

        String lFileName1 = fileName;
        if (lFileName1 == null) {
            lFileName1 = StringUtils.EMPTY;
        }
        String lFileName2 = lOtherAttached.getFieldName();
        if (lFileName2 == null) {
            lFileName2 = StringUtils.EMPTY;
        }

        return lId1.equals(lId2) && lFileName1.equals(lFileName2);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessAttachedField#setContent(byte[])
     */
    public void setContent(byte[] pContent) {
        throw new MethodNotImplementedException("setContent");
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessAttachedField#setFileName(java.lang.String)
     */
    public void setFileName(String pFileName) {
        throw new MethodNotImplementedException("setFileName");
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessAttachedField#setId(java.lang.String)
     */
    public void setId(String pId) {
        throw new MethodNotImplementedException("setId");
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessAttachedField#setMimeType(java.lang.String)
     */
    public void setMimeType(String pMimeType) {
        throw new MethodNotImplementedException("setMimeType");
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.impl.pointed.AbstractBusinessPointedField#toString()
     */
    @Override
    public String toString() {
        return fileName;
    }
}
