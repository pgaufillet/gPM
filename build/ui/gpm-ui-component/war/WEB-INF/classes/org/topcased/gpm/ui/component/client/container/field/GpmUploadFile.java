/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.container.field;

import java.util.List;

import org.topcased.gpm.business.values.field.BusinessField;
import org.topcased.gpm.business.values.field.simple.BusinessAttachedField;
import org.topcased.gpm.ui.component.client.field.GpmUploadFileWidget;
import org.topcased.gpm.ui.component.client.util.GpmAnchorTarget;
import org.topcased.gpm.ui.component.client.util.GpmStringUtils;
import org.topcased.gpm.ui.component.client.util.GpmUrlBuilder;
import org.topcased.gpm.ui.component.shared.util.DownloadParameter;

/**
 * Attached field on edition mode.
 * 
 * @author tpanuel
 */
public class GpmUploadFile extends AbstractGpmField<GpmUploadFileWidget>
        implements BusinessAttachedField {
    private final GpmUploadFileRegister register;

    private String mimeType;

    private List<String> attachedFilenamesInError;

    /**
     * Create a GpmUploadFile.
     * 
     * @param pRegister
     *            The upload file register.
     * @param pUrlBuilder
     *            The URL builder.
     * @param pTarget
     *            The target.
     */
    public GpmUploadFile(final GpmUploadFileRegister pRegister,
            final GpmUrlBuilder pUrlBuilder, final GpmAnchorTarget pTarget) {
        super(new GpmUploadFileWidget(pUrlBuilder, pTarget));
        register = pRegister;
        // Warning to circle reference to remove explicitly
        if (pRegister != null) {
            register.registerUploadFile(this);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessAttachedField#getId()
     */
    @Override
    public String getId() {
        return getWidget().getId();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessAttachedField#getFileName()
     */
    @Override
    public String getFileName() {
        return getWidget().getFileName();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessAttachedField#getMimeType()
     */
    @Override
    public String getMimeType() {
        return mimeType;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessAttachedField#getContent()
     */
    @Override
    public byte[] getContent() {
        // No content
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessAttachedField#setId(java.lang.String)
     */
    @Override
    public void setId(final String pId) {
        getWidget().setId(pId);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessAttachedField#setFileName(java.lang.String)
     */
    @Override
    public void setFileName(final String pFileName) {
        getWidget().setFileName(pFileName);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessAttachedField#setContent(byte[])
     */
    @Override
    public void setContent(final byte[] pContent) {
        // Nothing to do
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessAttachedField#setMimeType(java.lang.String)
     */
    @Override
    public void setMimeType(final String pMimeType) {
        mimeType = pMimeType;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#getAsString()
     */
    @Override
    public String getAsString() {
        return getFileName();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#copy(org.topcased.gpm.business.values.field.BusinessField)
     */
    @Override
    public void copy(final BusinessField pOther) {
        final BusinessAttachedField lOtherAttached = (BusinessAttachedField) pOther;

        boolean hasError = false;
        if (attachedFilenamesInError != null) {
        	for (String filename : attachedFilenamesInError) {
        		if (filename.equals(lOtherAttached.getFieldName())) {
        			hasError = true;
        		}
        	}
        }
        
        if (hasError) {
        	setFileName(null);
        }
        else {
            setFileName(lOtherAttached.getFileName());
            setId(lOtherAttached.getId());
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#hasSameValues(org.topcased.gpm.business.values.field.BusinessField)
     */
    @Override
    public boolean hasSameValues(final BusinessField pOther) {
        final BusinessAttachedField lOtherAttached =
                (BusinessAttachedField) pOther;
        if (org.topcased.gpm.ui.component.client.util.GpmStringUtils.getEmptyIfNull(
                getId()).equals(
                GpmStringUtils.getEmptyIfNull(lOtherAttached.getId()))) {
            if (GpmStringUtils.getEmptyIfNull(getFileName()).equals(
                    GpmStringUtils.getEmptyIfNull(lOtherAttached.getFileName()))) {
                return true;
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#isUpdatable()
     */
    @Override
    public boolean isUpdatable() {
        return true;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.container.field.AbstractGpmField#getEmptyClone()
     */
    @Override
    public AbstractGpmField<GpmUploadFileWidget> getEmptyClone() {
        final GpmUrlBuilder lUrlBuilder =
                getWidget().getUrlBuilder().getClone();
        final GpmUploadFile lClone =
                new GpmUploadFile(register, lUrlBuilder,
                        getWidget().getTarget());

        // Remove attached file download parameter
        lUrlBuilder.setParameter(DownloadParameter.FILE_ID, null);
        lUrlBuilder.setParameter(DownloadParameter.FILE_NAME, null);
        lUrlBuilder.setParameter(DownloadParameter.FILE_MIME_TYPE, null);
        initEmptyClone(lClone);
        lClone.setFileName(null);

        return lClone;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.container.field.AbstractGpmField#setEnabled(boolean)
     */
    @Override
    public void setEnabled(boolean pEnabled) {
        if (pEnabled) {
            register.registerUploadFile(this);
        }
        else {
            register.unregisterUploadFile(this);
        }
        getWidget().setEnabled(pEnabled);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.container.field.AbstractGpmField#isEnabled()
     */
    @Override
    public boolean isEnabled() {
        return getWidget().isEnabled();
    }

    public void setAttachedFilenamesInError(List<String> pAttachedFilenamesInError) {
        this.attachedFilenamesInError = pAttachedFilenamesInError;
    }
}
