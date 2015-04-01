/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.field;

import java.util.HashMap;
import java.util.List;

import org.topcased.gpm.business.util.ChoiceDisplayHintType;
import org.topcased.gpm.ui.component.client.container.field.GpmChoiceBoxValue;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasBlurHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Radio button widget. Create a group of radio buttons according to the choice
 * type {@link ChoiceDisplayHintType}
 * 
 * @author mkargbo
 */
public class GpmRadioWidget extends VerticalPanel implements HasBlurHandlers {

    private final HashMap<GpmChoiceBoxValue, RadioButton> radioButtons;

    private final String group;

    private ChoiceDisplayHintType displayHintType;

    /**
     * Create radio buttons group
     * 
     * @param pGroup
     *            Identifier of the group
     * @param pType
     *            Type of the radio button
     */
    public GpmRadioWidget(String pGroup, ChoiceDisplayHintType pType) {
        super();
        radioButtons = new HashMap<GpmChoiceBoxValue, RadioButton>();
        group = pGroup;
        displayHintType = pType;
    }

    /**
     * Creates radio buttons with the specified values
     * 
     * @param pValues
     *            Values to choice
     */
    public void init(List<GpmChoiceBoxValue> pValues) {
        for (GpmChoiceBoxValue lPossibleValue : pValues) {
            final RadioButton lRadioButton = createRadioButton(lPossibleValue);

            radioButtons.put(lPossibleValue, lRadioButton);
            add(lRadioButton);
        }
    }

    /**
     * Creates a RadioButton GWT component from label values.
     * 
     * @param pValue
     *            The radio button label key value.
     * @return The radio button to put in the container.
     */
    private RadioButton createRadioButton(final GpmChoiceBoxValue pValue) {
        String lLabelContent;
        Image lImage = null;
        InlineLabel lLabel;
        String lImageUrl = null;
        switch (displayHintType) {
            case NOT_LIST_IMAGE:
                lImageUrl = GWT.getModuleBaseURL() + pValue.getDisplayedImage();
                if (null != pValue.getValue()
                        && !pValue.getValue().trim().isEmpty()) {
                    lImage = new Image(lImageUrl);
                    lImage.setVisible(true);
                }
                lLabel = new InlineLabel("");
                break;
            case NOT_LIST_IMAGE_TEXT:
                lImageUrl = GWT.getModuleBaseURL() + pValue.getDisplayedImage();
                lLabelContent = pValue.getValue(); //lImage + "&nbsp;" + pValue.getValue();

                // Avoid not to display empty image
                if (null != lLabelContent && !lLabelContent.trim().isEmpty()) {
                    lImage = new Image(lImageUrl);
                    lImage.setVisible(true);
                }
                lLabel = new InlineLabel(lLabelContent);
                break;
            default:
                lLabelContent = pValue.getDisplayedValue();
                lLabel = new InlineLabel(lLabelContent);
                break;
        }

        final RadioButton lRadioButton = new RadioButton(group);
        setHandler(lImage, lLabel, lRadioButton);
        return lRadioButton;
    }

    /**
     * To reduce cyclomatic complexity
     * @param pImage
     * @param pLabel
     * @param pRadioButton
     */
    private void setHandler(final Image pImage, final InlineLabel pLabel,
            final RadioButton pRadioButton) {
        if (null != pImage) {
            // Case NOT_LIST_IMAGE_TEXT
            pRadioButton.getElement().appendChild(pImage.getElement());
            pRadioButton.getElement().appendChild(pLabel.getElement());
        }
        else {
            // DEFAULT
            pRadioButton.getElement().appendChild(pLabel.getElement());
        }
        pLabel.addClickHandler(new ClickHandler() {
            @SuppressWarnings("deprecation")
            @Override
            public void onClick(final ClickEvent pEvt) {
                pRadioButton.setChecked(true);
            }
        });

        if (null != pImage) {
            pImage.addClickHandler(new ClickHandler() {
                @SuppressWarnings("deprecation")
                @Override
                public void onClick(final ClickEvent pEvt) {
                    pRadioButton.setChecked(true);
                }
            });
        }
    }

    public HashMap<GpmChoiceBoxValue, RadioButton> getRadioButtons() {
        return radioButtons;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.client.ui.Panel#clear()
     */
    public void clear() {
        radioButtons.clear();
        super.clear();
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.event.dom.client.HasBlurHandlers#addBlurHandler(com.google.gwt.event.dom.client.BlurHandler)
     */
    @Override
    public HandlerRegistration addBlurHandler(BlurHandler pHandler) {
        for (RadioButton lRadioButton : radioButtons.values()) {
            lRadioButton.addBlurHandler(pHandler);
        }
        return null;
    }
}