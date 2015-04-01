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
import java.util.List;

import org.topcased.gpm.ui.application.client.popup.PopupView;
import org.topcased.gpm.ui.component.client.container.field.AbstractGpmField;
import org.topcased.gpm.ui.component.client.container.field.GpmLabel;
import org.topcased.gpm.ui.component.client.container.field.multivalued.GpmMultipleMultivaluedField;
import org.topcased.gpm.ui.component.client.container.field.multivalued.GpmMultivaluedField;
import org.topcased.gpm.ui.component.client.field.formater.GpmStringFormatter;
import org.topcased.gpm.ui.component.client.field.multivalued.GpmMultipleMultivaluedElement;

import com.google.gwt.user.client.ui.ScrollPanel;

/**
 * View for attribute visualization.
 * 
 * @author nveillet
 */
public class AttributesVisualizationView extends PopupView implements
        AttributesVisualizationDisplay {

    private static final String ATTRIBUTE_NAME_FIELD = "Name";

    private static final String ATTRIBUTE_VALUES_FIELD = "Values";

    private final static int HEIGHT = 300;

    private final static int WIDTH = 475;

    private final GpmMultipleMultivaluedField attributes;

    /**
     * Create a send sheet(s) by mail view.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public AttributesVisualizationView() {
        super(CONSTANTS.attributePopupTitleVisualization());

        // Build panel
        final ScrollPanel lFieldPanel = new ScrollPanel();
        lFieldPanel.addStyleName(INSTANCE.css().gpmBigBorder());

        // Create sub-fields
        List<AbstractGpmField<?>> lFields =
                new ArrayList<AbstractGpmField<?>>();

        // Attribute name field
        GpmLabel<String> lName =
                new GpmLabel<String>(GpmStringFormatter.getInstance());
        lName.setFieldName(ATTRIBUTE_NAME_FIELD);
        lName.setTranslatedFieldName(CONSTANTS.attributePopupFieldName());
        lFields.add(lName);

        // Attribute values field
        GpmMultivaluedField<GpmLabel<String>> lValues =
                new GpmMultivaluedField(new GpmLabel<String>(
                        GpmStringFormatter.getInstance()), false, false, false);
        lValues.setFieldName(ATTRIBUTE_VALUES_FIELD);
        lValues.setTranslatedFieldName(CONSTANTS.attributePopupFieldValues());
        lFields.add(lValues);

        // Attribute field
        attributes =
                new GpmMultipleMultivaluedField(lFields, false, false, false,
                        true);
        attributes.getWidget().removeAll();
        lFieldPanel.add(attributes.getWidget());

        ScrollPanel lPanel = new ScrollPanel();
        lPanel.addStyleName(INSTANCE.css().gpmSmallBorder());
        lPanel.setWidget(lFieldPanel);

        setContent(lPanel);

        setPixelSize(WIDTH, HEIGHT);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.attribute.AttributesVisualizationDisplay#addAttribute(java.lang.String,
     *      java.util.List)
     */
    @Override
    @SuppressWarnings("unchecked")
    public void addAttribute(String pName, List<String> pValues) {
        GpmMultipleMultivaluedElement lAttribute = attributes.addLine();
        ((GpmLabel<String>) lAttribute.getField(ATTRIBUTE_NAME_FIELD)).set(pName);

        GpmMultivaluedField<GpmLabel<String>> lValuesField =
                (GpmMultivaluedField<GpmLabel<String>>) lAttribute.getField(ATTRIBUTE_VALUES_FIELD);
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
}