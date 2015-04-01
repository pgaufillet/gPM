/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.popup.attribute;

import static org.topcased.gpm.ui.component.client.resources.ComponentResources.INSTANCE;
import static org.topcased.gpm.ui.component.client.resources.i18n.Ui18n.CONSTANTS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.topcased.gpm.ui.application.client.popup.PopupView;
import org.topcased.gpm.ui.component.client.container.field.AbstractGpmField;
import org.topcased.gpm.ui.component.client.container.field.GpmTextBox;
import org.topcased.gpm.ui.component.client.container.field.multivalued.GpmMultipleMultivaluedField;
import org.topcased.gpm.ui.component.client.container.field.multivalued.GpmMultivaluedField;
import org.topcased.gpm.ui.component.client.field.formater.GpmStringFormatter;
import org.topcased.gpm.ui.component.client.field.multivalued.GpmMultipleMultivaluedElement;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ScrollPanel;

/**
 * View for attribute edition.
 * 
 * @author nveillet
 */
public class AttributesEditionView extends PopupView implements
        AttributesEditionDisplay {

    private static final String ATTRIBUTE_NAME_FIELD = "Name";

    private static final String ATTRIBUTE_VALUES_FIELD = "Values";

    private final static int HEIGHT = 300;

    private final static int WIDTH = 475;

    private final GpmMultipleMultivaluedField attributes;

    private final Button saveButton;

    private HandlerRegistration saveButtonRegistration;
    
    private static final int NAME_FIELD_SIZE = 50;
    
    private static final int VALUE_FIELD_SIZE = 255;

    /**
     * Create a send sheet(s) by mail view.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public AttributesEditionView() {
        super(CONSTANTS.attributePopupTitleEdition());

        // Build panel
        final ScrollPanel lFieldPanel = new ScrollPanel();
        lFieldPanel.addStyleName(INSTANCE.css().gpmBigBorder());

        // Create sub-fields
        List<AbstractGpmField<?>> lFields =
                new ArrayList<AbstractGpmField<?>>();

        // Attribute name field
        GpmTextBox<String> lName =
                new GpmTextBox<String>(GpmStringFormatter.getInstance());
        lName.setFieldName(ATTRIBUTE_NAME_FIELD);
        lName.setTranslatedFieldName(CONSTANTS.attributePopupFieldName());
        //set field size
        lName.setSize(NAME_FIELD_SIZE);
        lFields.add(lName);
        
        // Attribute values field
        //Call the constructor which includes the size definition
        GpmMultivaluedField<GpmTextBox<String>> lValues =
            new GpmMultivaluedField(new GpmTextBox<String>(
                    GpmStringFormatter.getInstance(), VALUE_FIELD_SIZE), 
                        true, true, true);
        lValues.setFieldName(ATTRIBUTE_VALUES_FIELD);
        lValues.setTranslatedFieldName(CONSTANTS.attributePopupFieldValues());
        lFields.add(lValues);
        // Attribute field
        attributes =
                new GpmMultipleMultivaluedField(lFields, true, true, false,
                        true);
        attributes.getWidget().removeAll();
        lFieldPanel.add(attributes.getWidget());

        saveButton = addButton(CONSTANTS.save());

        ScrollPanel lPanel = new ScrollPanel();
        lPanel.addStyleName(INSTANCE.css().gpmSmallBorder());
        lPanel.setWidget(lFieldPanel);

        setContent(lPanel);

        setPixelSize(WIDTH, HEIGHT);
        
        // Add Key Handler on ENTER key : Quick Execute
        final KeyPressHandler lEnterKeyPressHandler = new KeyPressHandler() {
            @Override
            public void onKeyPress(KeyPressEvent pEvent) {
                if (KeyCodes.KEY_ENTER == pEvent.getCharCode()) {
                    ((Button) saveButton).click();
                }
            }
        };
        
        this.addDomHandler(lEnterKeyPressHandler, KeyPressEvent.getType());
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.attribute.AttributesEditionDisplay#addAttribute(java.lang.String,
     *      java.util.List)
     */
    @Override
    @SuppressWarnings("unchecked")
    public void addAttribute(String pName, List<String> pValues) {
        GpmMultipleMultivaluedElement lAttribute = attributes.addLine();
        ((GpmTextBox<String>) lAttribute.getField(ATTRIBUTE_NAME_FIELD)).set(pName);

        GpmMultivaluedField<GpmTextBox<String>> lValuesField =
                (GpmMultivaluedField<GpmTextBox<String>>) lAttribute.getField(ATTRIBUTE_VALUES_FIELD);
        lValuesField.getWidget().removeAll();
        for (String lValue : pValues) {
            lValuesField.addLine().set(lValue);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.client.ui.Panel#clear()
     */
    @Override
    public void clear() {
        attributes.getWidget().removeAll();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.attribute.AttributesEditionDisplay#getAttributes()
     */
    @Override
    @SuppressWarnings("unchecked")
    public Map<String, List<String>> getAttributes() {
        Map<String, List<String>> lAttributes =
                new HashMap<String, List<String>>();

        for (GpmMultipleMultivaluedElement lAttribute : attributes) {
            List<String> lValues = new ArrayList<String>();
            for (GpmTextBox<String> lValue : (GpmMultivaluedField<GpmTextBox<String>>) lAttribute.getField(ATTRIBUTE_VALUES_FIELD)) {
                if (!lValue.get().isEmpty()) {
                    lValues.add(lValue.get());
                }
            }
            String lName =
                    ((GpmTextBox<String>) lAttribute.getField(ATTRIBUTE_NAME_FIELD)).get();
            if (lName != null && !lName.isEmpty() && !lValues.isEmpty()) {
                lAttributes.put(lName, lValues);
            }
        }

        return lAttributes;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.attribute.AttributesEditionDisplay#setSaveButtonHandler(com.google.gwt.event.dom.client.ClickHandler)
     */
    @Override
    public void setSaveButtonHandler(final ClickHandler pHandler) {
        // Remove old click handler : if exist
        if (saveButtonRegistration != null) {
            saveButtonRegistration.removeHandler();
        }
        saveButtonRegistration = saveButton.addClickHandler(pHandler);
    }
}