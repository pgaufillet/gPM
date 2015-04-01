/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Florian ROSIER (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.field;

import static org.topcased.gpm.ui.component.client.resources.i18n.Ui18n.CONSTANTS;

import org.topcased.gpm.ui.component.client.popup.GpmPopupPanel;
import org.topcased.gpm.ui.component.client.resources.ComponentResources;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBoxBase;

/**
 * GpmZoomPopup is a popup panel dedicated to zoom editing text fields in order
 * to have more place to type text.
 * <p>
 * It is composed of a text area that is initialized with the text of the
 * editable attached field.
 * </p>
 * 
 * @author frosier
 */
public final class GpmZoomPopup extends GpmPopupPanel {
    private static final GpmZoomPopup INSTANCE = new GpmZoomPopup();

    // Labels.
    private static final String VALIDATE_LB = CONSTANTS.ok();

    private static final String EDIT_FIELD_LB = CONSTANTS.zoomPopupTitle();

    // Size ratios
    private static final double ZOOM_TEXT_AREA_SCREEN_SIZE_WIDTH_RATIO = 0.8;

    private static final double ZOOM_TEXT_AREA_SCREEN_SIZE_HEIGHT_RATIO = 0.8;

    // Attributes.
    private TextBoxBase attachedField;

    private final TextArea zoomTextArea;

    private boolean isBuilt = false;

    private GpmZoomPopup() {
        super(true);
        setGlassEnabled(true);
        zoomTextArea = new TextArea();
        zoomTextArea.addStyleName(ComponentResources.INSTANCE.css().gpmTextArea());
    }

    /**
     * Get the unique instance of zoom popup.
     * 
     * @return The instance
     */
    public static final GpmZoomPopup getInstance() {
        return INSTANCE;
    }

    /**
     * Attach the widget to the Zoom popup.
     * 
     * @param pField
     *            The field to attach.
     */
    private void attach(final TextBoxBase pField) {
        attachedField = pField;
        zoomTextArea.setEnabled(attachedField.isEnabled());
    }

    /**
     * Build the popup content if it is the first show call. <br />
     * Attach the field to the popup an show it centered.
     * 
     * @param pField
     *            The field to attache with the popup.
     */
    public void show(final TextBoxBase pField) {
        attach(pField);
        center();
    }

    /**
     * {@inheritDoc}
     * <p>
     * The building of the popup content done at the show call.
     * </p>
     * 
     * @see com.google.gwt.user.client.ui.PopupPanel#show()
     */
    @Override
    public void show() {
        if (!isBuilt) {
            build();
        }
        refresh(); // Actualize content
        super.show();
    }

    /**
     * Build the popup content.
     */
    private void build() {
        if (attachedField == null) {
            throw new IllegalStateException(
                    "GpmZoomPopup can not be instanciated since no field is attached.");
        }

        // Title
        Label lHeader = new Label(EDIT_FIELD_LB);
        setHeader(lHeader);

        // Button
        addButton(VALIDATE_LB).addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent pEvent) {
                attachedField.setValue(zoomTextArea.getValue());
                hide();
            }
        });

        // Content
        zoomTextArea.setValue(attachedField.getValue());
        setContent(zoomTextArea);

        isBuilt = true;
    }

    /**
     * Refresh the value of date pricker.
     */
    public void refresh() {
        if (attachedField == null) {
            throw new IllegalStateException(
                    "GpmZoomPopup can not be refreshed cause no field is attached.");
        }
        // Refresh size depending on window size
        setRatioSize(ZOOM_TEXT_AREA_SCREEN_SIZE_WIDTH_RATIO,
                ZOOM_TEXT_AREA_SCREEN_SIZE_HEIGHT_RATIO);
        zoomTextArea.setValue(attachedField.getValue());
    }
}