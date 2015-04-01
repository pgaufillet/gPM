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

import org.topcased.gpm.ui.component.client.component.field.BusinessChoiceFieldTestStub;
import org.topcased.gpm.ui.component.client.component.field.BusinessDateFieldTestStub;
import org.topcased.gpm.ui.component.client.component.field.BusinessTextFieldTestStub;
import org.topcased.gpm.ui.component.client.container.field.GpmDateBox;
import org.topcased.gpm.ui.component.client.container.field.GpmDateTimeBox;
import org.topcased.gpm.ui.component.client.container.field.GpmListBox;
import org.topcased.gpm.ui.component.client.container.field.GpmTextBox;
import org.topcased.gpm.ui.component.client.field.formater.GpmStringFormatter;

import com.google.gwt.junit.client.GWTTestCase;

/**
 * GpmFieldsTest is the unit tests class for gPM fields components.
 * 
 * @author frosier
 */
public class GwtTestGpmFields extends GWTTestCase {

    private static final String MODULE_NAME =
            "org.topcased.gpm.ui.component.Component";

    public String getModuleName() {
        return MODULE_NAME;
    }

    /**
     * Test the GpmTextBox component.
     */
    public void testGpmTextBox() {
        final BusinessTextFieldTestStub lBusinessTextField =
                new BusinessTextFieldTestStub();
        final GpmTextBox<String> lTextBox =
                new GpmTextBox<String>(GpmStringFormatter.getInstance());

        // Test copy.
        lTextBox.copy(lBusinessTextField);
        Assert.assertEquals(lBusinessTextField.getAsString(),
                lTextBox.getAsString());

        // Test hasSameValues.
        Assert.assertTrue(lTextBox.hasSameValues(lBusinessTextField));

        // Test wrong fieldType
        try {
            lTextBox.copy(new BusinessChoiceFieldTestStub());
            fail("lTextBox.copy() is not applicable for another field as BusinessSimpleField.");
        }
        catch (ClassCastException e) {
            // Good behavior.
        }
    }

    /**
     * JED test
     */
    public void testGpmTextBoxBis() {
        final GpmTextBox<String> lExpected =
                new GpmTextBox<String>(GpmStringFormatter.getInstance());
        lExpected.set("Humpfrey");
        final GpmTextBox<String> lCopy = lExpected.getEmptyClone();
        lCopy.copy(lExpected);
        assertTrue(lExpected.hasSameValues(lCopy));
    }

    /**
     * Test the GpmDateBox component.
     */
    public void testGpmDateBox() {
        final BusinessDateFieldTestStub lBusinessDateField =
                new BusinessDateFieldTestStub();
        final GpmDateBox lDateBox = new GpmDateBox();

        // Test copy.
        lDateBox.copy(lBusinessDateField);

        // Test hasSameValues.
        Assert.assertTrue(lDateBox.hasSameValues(lBusinessDateField));

        // Test wrong fieldType
        try {
            lDateBox.copy(new BusinessChoiceFieldTestStub());
            fail("lTextBox.copy() is not applicable for another field as BusinessSimpleField.");
        }
        catch (ClassCastException e) {
            // Good behavior.
        }
    }

    /**
     * Test the GpmDateTimeBox component.
     */
    public void testGpmDateTimeBox() {
        final BusinessDateFieldTestStub lBusinessDateField =
                new BusinessDateFieldTestStub();
        final GpmDateTimeBox lDateTimeBox = new GpmDateTimeBox();

        // Test copy.
        lDateTimeBox.copy(lBusinessDateField);

        // Test hasSameValues.
        Assert.assertTrue(lDateTimeBox.hasSameValues(lBusinessDateField));

        // Test wrong fieldType
        try {
            lDateTimeBox.copy(new BusinessChoiceFieldTestStub());
            fail("lTextBox.copy() is not applicable for another field as BusinessSimpleField.");
        }
        catch (ClassCastException e) {
            // Good behavior.
        }
    }

    /**
     * Test the GpmListBox component.
     */
    public void testGpmListBox() {
        final BusinessChoiceFieldTestStub lBusinessChoiceField =
                new BusinessChoiceFieldTestStub();
        final GpmListBox lListBox = new GpmListBox();

        // Populate list.
        lListBox.setPossibleValues(BusinessChoiceFieldTestStub.CHOICE_VALUES);

        // Test copy.
        lListBox.copy(lBusinessChoiceField);

        Assert.assertEquals(lBusinessChoiceField.getCategoryValue(),
                lListBox.getCategoryValue());

        // Test hasSameValues.
        Assert.assertTrue(lListBox.hasSameValues(lBusinessChoiceField));
    }
}