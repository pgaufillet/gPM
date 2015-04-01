/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.container.field;

import org.topcased.gpm.business.values.field.BusinessField;
import org.topcased.gpm.business.values.field.simple.BusinessAttachedField;
import org.topcased.gpm.business.values.field.simple.BusinessSimpleField;
import org.topcased.gpm.business.values.field.simple.BusinessStringField;
import org.topcased.gpm.ui.component.client.button.GpmTextButton;
import org.topcased.gpm.ui.component.client.util.GpmAnchorTarget;
import org.topcased.gpm.ui.component.client.util.GpmStringUtils;
import org.topcased.gpm.ui.component.client.util.GpmUrlBuilder;
import org.topcased.gpm.ui.component.shared.util.DownloadParameter;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;

/**
 * Anchor field.
 * <p>
 * Use {@link GpmTextButton} widget to display the link.
 * </p>
 * 
 * @author mkargbo
 */
public class GpmAnchor extends AbstractGpmField<GpmTextButton> implements
        BusinessSimpleField<String>, BusinessAttachedField {
    private GpmUrlBuilder urlBuilder;

    private final GpmAnchorTarget target;

    private ClickHandler clickHandler;

    /**
     * Constructor of an anchor.
     * 
     * @param pTarget
     *            The target.
     */
    public GpmAnchor(final GpmAnchorTarget pTarget) {
        this(null, pTarget);
    }

    /**
     * Constructor for internal link to Sheets
     * 
     * @param pClickHandler
     *            the ClickHandler
     */
    public GpmAnchor(ClickHandler pClickHandler) {
        super(new GpmTextButton());
        urlBuilder = null;
        target = null;
        clickHandler = pClickHandler;
        getWidget().addClickHandler(clickHandler);
    }

    /**
     * Constructor an anchor.
     * 
     * @param pUrlBuilder
     *            The URL builder.
     * @param pTarget
     *            The target.
     */
    public GpmAnchor(final GpmUrlBuilder pUrlBuilder,
            final GpmAnchorTarget pTarget) {
        super(new GpmTextButton());
        urlBuilder = pUrlBuilder;
        target = pTarget;
        getWidget().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent pEvent) {
                if (urlBuilder != null) {
                    String lVal = urlBuilder.buildString();
                    if (lVal != null) {
                        Window.open(lVal, target.getValue(), "");
                    }

                }
            }
        });
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.container.field.AbstractGpmField#getEmptyClone()
     */
    @Override
    public AbstractGpmField<GpmTextButton> getEmptyClone() {
        final GpmAnchor lClone;

        if (target == null) {
            // Anchor is an Internal URL
            lClone = new GpmAnchor(clickHandler);
            // Nothing to initialize
        }
        else {
            // Anchor is a classic URL
            if (urlBuilder == null) {
                lClone = new GpmAnchor(target);
            }
            else {
                final GpmUrlBuilder lUrlBuilder = urlBuilder.getClone();

                lClone = new GpmAnchor(lUrlBuilder, target);
                // Remove attached file download parameter
                lUrlBuilder.setParameter(DownloadParameter.FILE_ID, null);
                lUrlBuilder.setParameter(DownloadParameter.FILE_NAME, null);
                lUrlBuilder.setParameter(DownloadParameter.FILE_MIME_TYPE, null);
            }
        }

        initEmptyClone(lClone);

        return lClone;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.container.field.AbstractGpmField#setEnabled(boolean)
     */
    @Override
    public void setEnabled(boolean pEnabled) {
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessSimpleField#get()
     */
    @Override
    public String get() {
        return getWidget().getText();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessSimpleField#set(java.lang.Object)
     */
    @Override
    public void set(final String pValue) {
        getWidget().setText(pValue);
        if (pValue != null) {
            urlBuilder = new GpmUrlBuilder(pValue);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessSimpleField#setAsString(java.lang.String)
     */
    @Override
    public void setAsString(final String pValue) {
        set(pValue);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#copy(org.topcased.gpm.business.values.field.BusinessField)
     */
    @Override
    public void copy(final BusinessField pOther) {
        if (pOther instanceof BusinessAttachedField) {
            final BusinessAttachedField lOtherAttached =
                    (BusinessAttachedField) pOther;

            setId(lOtherAttached.getId());
            setFileName(lOtherAttached.getFileName());
            setMimeType(lOtherAttached.getMimeType());
        }
        else if (pOther instanceof BusinessStringField) {
            BusinessStringField lOther = (BusinessStringField) pOther;
            // internal URL case
            if (lOther.getInternalUrlSheetReference() != null) {
                setInternalUrlSheet(lOther.get(),
                        lOther.getInternalUrlSheetReference());
            }
            else {
                // set the URL value if it is not internal URL
                set(pOther.getAsString());
            }
        }

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#getAsString()
     */
    @Override
    public String getAsString() {
        return getWidget().getText();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#hasSameValues(org.topcased.gpm.business.values.field.BusinessField)
     */
    @Override
    public boolean hasSameValues(BusinessField pOther) {
        final String lValue = GpmStringUtils.getEmptyIfNull(get());
        final String lOtherValue =
                GpmStringUtils.getEmptyIfNull(pOther.getAsString());

        if (lValue == null) {
            return lOtherValue == null;
        }
        else {
            return lValue.equals(lOtherValue);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#isUpdatable()
     */
    @Override
    public boolean isUpdatable() {
        return false;
    }

    /**
     * Set the field width
     * 
     * @param pWidth
     *            Width of the field.
     */
    public void setWidth(final String pWidth) {
        getWidget().setWidth(pWidth);
    }

    /**
     * Set the field height
     * 
     * @param pHeight
     *            Height of the fields.
     */
    public void setHeight(final String pHeight) {
        getWidget().setHeight(pHeight);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessAttachedField#getId()
     */
    @Override
    public String getId() {
        return urlBuilder.getParameter(DownloadParameter.FILE_ID);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessAttachedField#getFileName()
     */
    @Override
    public String getFileName() {
        return urlBuilder.getParameter(DownloadParameter.FILE_NAME);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessAttachedField#getMimeType()
     */
    @Override
    public String getMimeType() {
        return urlBuilder.getParameter(DownloadParameter.FILE_MIME_TYPE);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessAttachedField#getContent()
     */
    @Override
    public byte[] getContent() {
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessAttachedField#setId(java.lang.String)
     */
    @Override
    public void setId(final String pId) {
        urlBuilder.setParameter(DownloadParameter.FILE_ID, pId);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessAttachedField#setFileName(java.lang.String)
     */
    @Override
    public void setFileName(final String pFileName) {
        getWidget().setText(pFileName);
        urlBuilder.setParameter(DownloadParameter.FILE_NAME, pFileName);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessAttachedField#setMimeType(java.lang.String)
     */
    @Override
    public void setMimeType(final String pMimeType) {
        urlBuilder.setParameter(DownloadParameter.FILE_MIME_TYPE, pMimeType);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessAttachedField#setContent(byte[])
     */
    @Override
    public void setContent(final byte[] pContent) {
        // Nothing todo
    }

    /**
     * Set internal url sheet values
     * 
     * @param pSheetId
     *            the sheet identifier
     * @param pSheetReference
     *            the sheet reference
     */
    public void setInternalUrlSheet(String pSheetId, String pSheetReference) {
        getWidget().setId(pSheetId);
        getWidget().setText(pSheetReference);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.container.field.AbstractGpmField#isEnabled()
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}