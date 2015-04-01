/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Florian ROSIER (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client;

import junit.framework.Assert;

import org.topcased.gpm.business.util.ChoiceDisplayHintType;
import org.topcased.gpm.ui.component.client.component.field.BusinessChoiceFieldTestStub;
import org.topcased.gpm.ui.component.client.component.field.BusinessTextFieldTestStub;
import org.topcased.gpm.ui.component.client.container.field.GpmChoiceBoxValue;
import org.topcased.gpm.ui.component.client.container.field.GpmLabel;
import org.topcased.gpm.ui.component.client.container.field.GpmRadioBox;
import org.topcased.gpm.ui.component.client.field.formater.GpmStringFormatter;

import com.google.gwt.junit.client.GWTTestCase;

/**
 * GpmDisplayHintFieldsTest is the unit tests class for gPM display hint fields
 * components.
 * 
 * @author frosier
 */
public class GwtTestGpmDisplayHintFields extends GWTTestCase {

    private static final String MODULE_NAME =
            "org.topcased.gpm.ui.component.Component";

    public String getModuleName() {
        return MODULE_NAME;
    }

    /**
     * Test the GpmListBox component.
     */
    public void testGpmRadioBox() {
        final BusinessChoiceFieldTestStub lBusinessChoiceField =
                new BusinessChoiceFieldTestStub();
        final GpmRadioBox lRadioBox =
                new GpmRadioBox(ChoiceDisplayHintType.NOT_LIST);

        // Populate container.
        lRadioBox.setPossibleValues(GpmChoiceBoxValue.buildFromStrings(BusinessChoiceFieldTestStub.getRadioButtonValues()));

        // Test copy.
        lRadioBox.copy(lBusinessChoiceField);

        Assert.assertEquals(lBusinessChoiceField.getCategoryValue(),
                lRadioBox.getCategoryValue());

        // Test hasSameValues.
        Assert.assertTrue(lRadioBox.hasSameValues(lBusinessChoiceField));
    }

    /**
     * Test the GpmLabel component in String type.
     */
    public void testGpmLabel() {
        final BusinessTextFieldTestStub lBusinessTextField =
                new BusinessTextFieldTestStub();

        final GpmLabel<String> lLabel =
                new GpmLabel<String>(GpmStringFormatter.getInstance());

        // Test copy.
        lLabel.copy(lBusinessTextField);

        Assert.assertEquals(lBusinessTextField.get(), lLabel.get());

        // Test hasSameValues.
        Assert.assertTrue(lLabel.hasSameValues(lBusinessTextField));
    }
}