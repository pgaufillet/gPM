/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.field;

import org.topcased.gpm.ui.component.client.button.GpmDoubleImageButton;
import org.topcased.gpm.ui.component.client.container.field.GpmAnchor;
import org.topcased.gpm.ui.component.client.resources.ComponentResources;
import org.topcased.gpm.ui.component.client.util.GpmAnchorTarget;
import org.topcased.gpm.ui.component.client.util.GpmUrlBuilder;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;

/**
 * Widget for upload field.
 * 
 * @author tpanuel
 */
public class GpmUploadFileWidget extends FlowPanel implements HasChangeHandlers {
    private static final double BUTTON_MARGIN = 4;

    private final GpmUrlBuilder urlBuilder;

    private final GpmAnchorTarget target;

    private GpmAnchor anchor;

    private FormPanel form;

    private GpmFileUpload upload;

    private GpmDoubleImageButton delete;

    private GpmDoubleImageButton revert;

    private boolean uploadMode;

    /**
     * Default constructor.
     * 
     * @param pUrlBuilder
     *            The URL builder.
     * @param pTarget
     *            The target.
     */
    public GpmUploadFileWidget(final GpmUrlBuilder pUrlBuilder,
            final GpmAnchorTarget pTarget) {
        uploadMode = false;
        urlBuilder = pUrlBuilder;
        target = pTarget;
    }

    /**
     * Get the id.
     * 
     * @return The id.
     */
    public String getId() {
        if (uploadMode || anchor == null) {
            return null;
        }
        else {
            return anchor.getId();
        }
    }

    /**
     * Get the file name.
     * 
     * @return The file name.
     */
    public String getFileName() {
        if (uploadMode) {
            return upload.getFilename();
        }
        else {
            if (anchor == null) {
                return null;
            }
            else {
                return anchor.getFileName();
            }
        }
    }

    /**
     * Set the id.
     * 
     * @param pId
     *            The id.
     */
    public void setId(final String pId) {
        // Create and/or initialize anchor if id has been set
        if (pId != null && !pId.isEmpty()) {
            if (anchor == null) {
                anchor = new GpmAnchor(urlBuilder, target);
            }
            anchor.setId(pId);
        }
    }

    /**
     * Set the file name.
     * 
     * @param pFileName
     *            The file name.
     */
    public void setFileName(final String pFileName) {
        // Display anchor if file name has been set
        if (pFileName == null || pFileName.isEmpty()) {
            displayUpload();
        }
        else {
            // Initialize anchor before displaying
            if (anchor == null) {
                anchor = new GpmAnchor(urlBuilder, target);
            }
            anchor.setFileName(pFileName);
            displayAnchor();
        }
    }

    /**
     * Set the mime type.
     * 
     * @param pMimeType
     *            The mime type.
     */
    public void setMimeType(final String pMimeType) {
        // Create and/or initialize anchor if mime type has been set
        if (pMimeType != null && !pMimeType.isEmpty()) {
            if (anchor == null) {
                anchor = new GpmAnchor(urlBuilder, target);
            }
            anchor.setMimeType(pMimeType);
        }
    }

    private void displayAnchor() {
        // Clear the panel
        clear();
        // Initialize the anchor, if not yet
        if (anchor == null) {
            anchor = new GpmAnchor(urlBuilder, target);
        }

        // Display delete button
        if (delete == null) {
            delete =
                    new GpmDoubleImageButton(
                            ComponentResources.INSTANCE.images().close(),
                            ComponentResources.INSTANCE.images().closeHover());
            delete.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(final ClickEvent pEvent) {
                    displayUpload();
                }
            });
            delete.getElement().getStyle().setFloat(
                    com.google.gwt.dom.client.Style.Float.LEFT);
            delete.getElement().getStyle().setMarginLeft(BUTTON_MARGIN, Unit.PX);
        }

        add(anchor.getWidget());
        add(delete);
        anchor.getWidget().getElement().getStyle().setFloat(
                com.google.gwt.dom.client.Style.Float.LEFT);

        uploadMode = false;
    }

    private void displayUpload() {
        // Clear the panel
        clear();
        // Reset the upload file
        form = new FormPanel();
        FlowPanel lFormPanel = new FlowPanel();
        upload = new GpmFileUpload();
        // Name is need to be submit
        upload.setName("upload");
        form.setEncoding(FormPanel.ENCODING_MULTIPART);
        form.setMethod(FormPanel.METHOD_POST);

        form.add(lFormPanel);
        lFormPanel.add(upload);
        add(form);
        // Add revert button if the anchor already exist
        if (anchor != null) {
            if (revert == null) {
                revert =
                        new GpmDoubleImageButton(
                                ComponentResources.INSTANCE.images().undo(),
                                ComponentResources.INSTANCE.images().undoHover());
                revert.addClickHandler(new ClickHandler() {
                    @Override
                    public void onClick(final ClickEvent pEvent) {
                        displayAnchor();
                    }
                });
                revert.getElement().getStyle().setMarginLeft(BUTTON_MARGIN,
                        Unit.PX);
            }
            lFormPanel.add(revert);
        }
        uploadMode = true;
    }

    /**
     * Set if the widget is enabled.
     * 
     * @param pEnabled
     *            If the widget is enabled.
     */
    public void setEnabled(boolean pEnabled) {
        if (upload != null) {
            upload.setEnabled(pEnabled);
        }
        if (delete != null) {
            delete.setVisible(pEnabled);
        }
        if (revert != null) {
            revert.setVisible(pEnabled);
        }
    }

    /**
     * Get URL builder.
     * 
     * @return The URL builder.
     */
    public GpmUrlBuilder getUrlBuilder() {
        return urlBuilder;
    }

    /**
     * Get the target.
     * 
     * @return The target.
     */
    public GpmAnchorTarget getTarget() {
        return target;
    }

    /**
     * Test if has file to upload.
     * 
     * @return If has file to upload.
     */
    public boolean hasFileToUpload() {
        return uploadMode && upload.isEnabled() && upload.getFilename() != null
                && !upload.getFilename().isEmpty();
    }

    /**
     * Send the file by submitting the form.
     * 
     * @param pServletUrl
     *            the URL of the file upload servlet
     * @param pHandler
     *            the post submission handler
     */
    public void sendFile(final String pServletUrl,
            final SubmitCompleteHandler pHandler) {
        form.setAction(pServletUrl);
        form.addSubmitCompleteHandler(pHandler);
        form.submit();
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.event.dom.client.HasChangeHandlers#addChangeHandler(com.google.gwt.event.dom.client.ChangeHandler)
     */
    @Override
    public HandlerRegistration addChangeHandler(ChangeHandler pHandler) {
        if (upload != null) {
            return upload.addChangeHandler(pHandler);
        }
        else {
            return null;
        }
    }

    /**
     * Indicates if the widget is enabled
     * 
     * @return <CODE>true</CODE> if the widget is enabled
     */
    public boolean isEnabled() {
        if (upload != null) {
            return upload.isEnabled();
        }
        return false;
    }
}