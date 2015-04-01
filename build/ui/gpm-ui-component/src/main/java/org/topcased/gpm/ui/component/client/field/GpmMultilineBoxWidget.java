/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Florian ROSIER (Atos Origin), Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.field;

import static org.topcased.gpm.ui.component.client.resources.ComponentResources.INSTANCE;

import org.topcased.gpm.ui.component.client.button.GpmImageButton;
import org.topcased.gpm.ui.component.client.resources.ComponentResources;

import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasBlurHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextArea;

/**
 * GpmMultilineBox has the behavior of TextArea that can be zoomed in a popup.
 * <h3>Event</h3>
 * <dl>
 * <dt>BlueEvent</dt>
 * <dd>Use {@link TextArea} blur event.</dd>
 * </dl>
 * 
 * @author frosier
 */
public class GpmMultilineBoxWidget extends HorizontalPanel implements IHasSize,
        HasBlurHandlers {
    private static final ImageResource ZOOM_BUTTON_DEFAULT_RESOURCE =
            INSTANCE.images().zoom();

    private static final int DEFAULT_CARACTERE_WIDTH = 50;

    private static final int DEFAULT_LINE_NUMBER = 5;

    private final TextArea textArea;

    private final GpmImageButton zoomButton;

    /**
     * Creates an empty multiline box.
     */
    public GpmMultilineBoxWidget() {
        super();
        textArea = new TextArea();
        textArea.addStyleName(ComponentResources.INSTANCE.css().gpmTextArea());
        zoomButton = new GpmImageButton(ZOOM_BUTTON_DEFAULT_RESOURCE);

        initTextArea();
        initZoomButton();

        setVerticalAlignment(ALIGN_BOTTOM);

        add(textArea);
        add(zoomButton);
    }
    
    /**
     * Creates an empty multiline box and set a maximum size
     * Does not display zoom button
     * @param pSize
     *        The maximum text length
     */
    public GpmMultilineBoxWidget(String pSize) {
        super();
        textArea = new TextArea();
        textArea.getElement().setAttribute("maxlength", pSize);
        textArea.addStyleName(ComponentResources.INSTANCE.css().gpmTextArea());
        zoomButton = new GpmImageButton(ZOOM_BUTTON_DEFAULT_RESOURCE);        
        textArea.setTitle(textArea.getTitle() + "(max. " + pSize + ")");
        initTextArea();
        setVerticalAlignment(ALIGN_BOTTOM);
        add(textArea);
    }

    /**
     * Initialize the text area.
     */
    private void initTextArea() {
        textArea.setCharacterWidth(DEFAULT_CARACTERE_WIDTH);
        textArea.setVisibleLines(DEFAULT_LINE_NUMBER);
    }

    /**
     * Initialize zoom button.
     */
    public void initZoomButton() {
        zoomButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent pEvent) {
                GpmZoomPopup.getInstance().show(textArea);
            }
        });
    }

    /**
     * Set the number of visible lines.
     * 
     * @param pNbLines
     *            The number of visible lines.
     */
    public void setVisibleLines(final int pNbLines) {
        if (pNbLines > 0) {
            textArea.setVisibleLines(pNbLines);
        }
    }

    /**
     * Set the number of characters by line.
     * 
     * @param pNbChar
     *            The number of characters by line.
     */
    public void setCharacterWidth(final int pNbChar) {
        if (pNbChar > 0) {
            textArea.setCharacterWidth(pNbChar);
        }
    }

    //---- TextArea mapping methods ----//

    /**
     * Set the value of the text area.
     * 
     * @param pText
     *            The text value.
     */
    public void setValue(final String pText) {
        textArea.setValue(pText);
    }

    /**
     * Get the value of the text area.
     * 
     * @return The text area value.
     */
    public String getValue() {
        return escapeNewLine(textArea.getValue());
    }

    /**
     * Enable disable field
     * 
     * @param pEnabled
     *            <code>true</code> to enable field, <code>false</code> to
     *            disable
     */
    public void setEnabled(boolean pEnabled) {
        textArea.setEnabled(pEnabled);
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.event.dom.client.HasBlurHandlers#addBlurHandler(com.google.gwt.event.dom.client.BlurHandler)
     */
    @Override
    public HandlerRegistration addBlurHandler(BlurHandler pHandler) {
        return textArea.addBlurHandler(pHandler);
    }

    public boolean isEnabled() {
        return textArea.isEnabled();
    }

    /**
     * Retrieves a String with the correct new line encoding This make values
     * registered with "\n" as new line to be encoded as well as those which
     * using "\r\n"
     * 
     * @param pValue
     * @return new String
     */
    private String escapeNewLine(String pValue) {
        /**
         * magic value used to avoid confusion between "\r\n" and "\n" during
         * string replace.
         */
        String tag = "&@#@";
        return pValue.replaceAll("\r\n", tag).replaceAll("\n", "\r\n").replaceAll(
                tag, "\r\n");
    }
}