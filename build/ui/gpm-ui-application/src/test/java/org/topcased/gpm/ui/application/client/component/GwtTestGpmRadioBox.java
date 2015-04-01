/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Florian ROSIER (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.component;

import static org.topcased.gpm.ui.application.client.GpmTestsConstants.DEFAULT_OTHER_TEST_STRING_VALUE;

import java.util.List;

import org.topcased.gpm.business.util.ChoiceDisplayHintType;
import org.topcased.gpm.ui.application.client.GpmTestsConstants;
import org.topcased.gpm.ui.component.client.container.field.GpmChoiceBoxValue;
import org.topcased.gpm.ui.component.client.container.field.GpmRadioBox;
import org.topcased.gpm.ui.facade.shared.container.field.simple.UiChoiceField;
import org.topcased.gpm.ui.facade.shared.container.field.value.UiChoiceFieldValue;

/**
 * GwtTestGpmListBox : unit tests on gPM radio box component.
 * 
 * @author frosier
 */
public class GwtTestGpmRadioBox extends AbstractGpmTestCase {

    public static final List<String> DEFAULT_TEST_VALUES =
            GpmTestsConstants.DEFAULT_LIST_TEST_VALUES;

    public static final int DEFAULT_TEST_VALUE_SELECTED_INDEX = 3; // => matched value = "value 4"

    public static final String DEFAULT_TEST_VALUE =
            DEFAULT_TEST_VALUES.get(DEFAULT_TEST_VALUE_SELECTED_INDEX);

    public String getModuleName() {
        return GpmTestsConstants.MODULE_NAME;
    }

    /**
     * Test data transfer UiField > GpmField. <br/>
     * Type of the field = UiChoiceField. <br/>
     * Type of display hint = ChoiceDisplayHintType.LIST.
     */
    public void testUiChoiceToGpmFieldList() {
        final GpmRadioBox lRadioBox =
                new GpmRadioBox(ChoiceDisplayHintType.LIST);
        final UiChoiceField lUiChoiceField = new UiChoiceField();

        lUiChoiceField.setCategoryValue(DEFAULT_TEST_VALUE);
        lRadioBox.setPossibleValues(GpmChoiceBoxValue.buildFromStrings(DEFAULT_TEST_VALUES));

        executeTestCopyChoiceFieldAppToHMI(lUiChoiceField, lRadioBox, true);

        // Test different value
        lUiChoiceField.setCategoryValue(DEFAULT_OTHER_TEST_STRING_VALUE);
        assertNotHasSameValues("Check that values are different",
                lUiChoiceField, lRadioBox);
    }

    /**
     * Test data transfer GpmField > UiField. <br/>
     * Type of the field = UiChoiceField. <br/>
     * Type of display hint = ChoiceDisplayHintType.LIST.
     */
    public void testGpmFieldToUiChoiceList() {
        final GpmRadioBox lRadioBox =
                new GpmRadioBox(ChoiceDisplayHintType.LIST);
        final UiChoiceField lUiChoiceField = new UiChoiceField();

        lRadioBox.setPossibleValues(GpmChoiceBoxValue.buildFromStrings(DEFAULT_TEST_VALUES));
        lRadioBox.setCategoryValue(DEFAULT_TEST_VALUE);
        for (String lValue : DEFAULT_TEST_VALUES) {
            lUiChoiceField.addPossibleValue(new UiChoiceFieldValue(lValue,
                    lValue, null));
        }

        executeTestCopyChoiceFieldHMIToApp(lUiChoiceField, lRadioBox, true);

        // Test different values
        lUiChoiceField.setCategoryValue(DEFAULT_OTHER_TEST_STRING_VALUE);
        assertNotHasSameValues(
                "Check that the listBox didn't took the value that "
                        + "was not in possible values", lUiChoiceField,
                lRadioBox);
    }

    /**
     * Test data transfer UiField > GpmField. <br/>
     * Type of the field = UiChoiceField. <br/>
     * Type of display hint = ChoiceDisplayHintType.NOT_LIST.
     */
    public void testUiChoiceToGpmFieldNotList() {
        final GpmRadioBox lRadioBox =
                new GpmRadioBox(ChoiceDisplayHintType.NOT_LIST);
        final UiChoiceField lUiChoiceField = new UiChoiceField();

        lUiChoiceField.setCategoryValue(DEFAULT_TEST_VALUE);
        lRadioBox.setPossibleValues(GpmChoiceBoxValue.buildFromStrings(DEFAULT_TEST_VALUES));

        executeTestCopyChoiceFieldAppToHMI(lUiChoiceField, lRadioBox, true);

        // Test different value
        lUiChoiceField.setCategoryValue(DEFAULT_OTHER_TEST_STRING_VALUE);
        assertNotHasSameValues("Check that values are different",
                lUiChoiceField, lRadioBox);
    }

    /**
     * Test data transfer GpmField > UiField. <br/>
     * Type of the field = UiChoiceField. <br/>
     * Type of display hint = ChoiceDisplayHintType.NOT_LIST.
     */
    public void testGpmFieldToUiChoiceNotList() {
        final GpmRadioBox lRadioBox =
                new GpmRadioBox(ChoiceDisplayHintType.NOT_LIST);
        final UiChoiceField lUiChoiceField = new UiChoiceField();

        lRadioBox.setPossibleValues(GpmChoiceBoxValue.buildFromStrings(DEFAULT_TEST_VALUES));
        lRadioBox.setCategoryValue(DEFAULT_TEST_VALUE);
        for (String lValue : DEFAULT_TEST_VALUES) {
            lUiChoiceField.addPossibleValue(new UiChoiceFieldValue(lValue,
                    lValue, null));
        }

        executeTestCopyChoiceFieldHMIToApp(lUiChoiceField, lRadioBox, true);

        // Test different values
        lUiChoiceField.setCategoryValue(DEFAULT_OTHER_TEST_STRING_VALUE);
        assertNotHasSameValues(
                "Check that the listBox didn't took the value that "
                        + "was not in possible values", lUiChoiceField,
                lRadioBox);
    }

    /**
     * Test data transfer UiField > GpmField. <br/>
     * Type of the field = UiChoiceField. <br/>
     * Type of display hint = ChoiceDisplayHintType.NOT_LIST_IMAGE.
     */
    public void testUiChoiceToGpmFieldNotListImage() {
        final GpmRadioBox lRadioBox =
                new GpmRadioBox(ChoiceDisplayHintType.NOT_LIST_IMAGE);
        final UiChoiceField lUiChoiceField = new UiChoiceField();

        lRadioBox.setPossibleValues(GpmChoiceBoxValue.buildFromStrings(DEFAULT_TEST_VALUES));
        lUiChoiceField.setCategoryValue(DEFAULT_TEST_VALUE);

        executeTestCopyChoiceFieldAppToHMI(lUiChoiceField, lRadioBox, true);

        // Test different value
        lUiChoiceField.setCategoryValue(DEFAULT_OTHER_TEST_STRING_VALUE);
        assertNotHasSameValues("Check that values are different",
                lUiChoiceField, lRadioBox);
    }

    /**
     * Test data transfer GpmField > UiField. <br/>
     * Type of the field = UiChoiceField. <br/>
     * Type of display hint = ChoiceDisplayHintType.NOT_LIST_IMAGE.
     */
    public void testGpmFieldToUiChoiceNotListImage() {
        final GpmRadioBox lRadioBox =
                new GpmRadioBox(ChoiceDisplayHintType.NOT_LIST);
        final UiChoiceField lUiChoiceField = new UiChoiceField();

        lRadioBox.setPossibleValues(GpmChoiceBoxValue.buildFromStrings(DEFAULT_TEST_VALUES));
        lRadioBox.setCategoryValue(DEFAULT_TEST_VALUE);

        for (String lValue : DEFAULT_TEST_VALUES) {
            lUiChoiceField.addPossibleValue(new UiChoiceFieldValue(lValue,
                    lValue, null));
        }

        executeTestCopyChoiceFieldHMIToApp(lUiChoiceField, lRadioBox, true);

        // Test different values
        lUiChoiceField.setCategoryValue(DEFAULT_OTHER_TEST_STRING_VALUE);
        assertNotHasSameValues(
                "Check that the listBox didn't took the value that "
                        + "was not in possible values", lUiChoiceField,
                lRadioBox);
    }

    /**
     * Test data transfer UiField > GpmField. <br/>
     * Type of the field = UiChoiceField. <br/>
     * Type of display hint = ChoiceDisplayHintType.NOT_LIST_IMAGE_TEXT.
     */
    public void testUiChoiceToGpmFieldNotListImageText() {
        final GpmRadioBox lRadioBox =
                new GpmRadioBox(ChoiceDisplayHintType.NOT_LIST_IMAGE_TEXT);
        final UiChoiceField lUiChoiceField = new UiChoiceField();

        lRadioBox.setPossibleValues(GpmChoiceBoxValue.buildFromStrings(DEFAULT_TEST_VALUES));
        lUiChoiceField.setCategoryValue(DEFAULT_TEST_VALUE);

        executeTestCopyChoiceFieldAppToHMI(lUiChoiceField, lRadioBox, true);

        // Test different value
        lUiChoiceField.setCategoryValue(DEFAULT_OTHER_TEST_STRING_VALUE);
        assertNotHasSameValues("Check that values are different",
                lUiChoiceField, lRadioBox);
    }

    /**
     * Test data transfer GpmField > UiField. <br/>
     * Type of the field = UiChoiceField. <br/>
     * Type of display hint = ChoiceDisplayHintType.NOT_LIST_IMAGE_TEXT.
     */
    public void testGpmFieldToUiChoiceNotListImageText() {
        final GpmRadioBox lRadioBox =
                new GpmRadioBox(ChoiceDisplayHintType.NOT_LIST_IMAGE_TEXT);
        final UiChoiceField lUiChoiceField = new UiChoiceField();

        lRadioBox.setPossibleValues(GpmChoiceBoxValue.buildFromStrings(DEFAULT_TEST_VALUES));
        lRadioBox.setCategoryValue(DEFAULT_TEST_VALUE);

        for (String lValue : DEFAULT_TEST_VALUES) {
            lUiChoiceField.addPossibleValue(new UiChoiceFieldValue(lValue,
                    lValue, null));
        }

        executeTestCopyChoiceFieldHMIToApp(lUiChoiceField, lRadioBox, true);

        // Test different values
        lUiChoiceField.setCategoryValue(DEFAULT_OTHER_TEST_STRING_VALUE);
        assertNotHasSameValues(
                "Check that the listBox didn't took the value that "
                        + "was not in possible values", lUiChoiceField,
                lRadioBox);
    }

    /**
     * Test with no category value. <br />
     * Type of the field = UiChoiceField. <br/>
     * Type of display hint = ChoiceDisplayHintType.LIST.
     */
    public void testEmptyGpmFieldToUiChoiceList() {
        final GpmRadioBox lRadioBox =
                new GpmRadioBox(ChoiceDisplayHintType.LIST);
        final UiChoiceField lUiChoiceField = new UiChoiceField();

        lRadioBox.setPossibleValues(GpmChoiceBoxValue.buildFromStrings(DEFAULT_TEST_VALUES));

        executeTestCopyChoiceFieldHMIToApp(lUiChoiceField, lRadioBox, true);
    }

    /**
     * Test with no category value. <br />
     * Type of the field = UiChoiceField. <br/>
     * Type of display hint = ChoiceDisplayHintType.LIST.
     */
    public void testEmptyUiChoiceListToGpmField() {
        final GpmRadioBox lRadioBox =
                new GpmRadioBox(ChoiceDisplayHintType.LIST);
        final UiChoiceField lUiChoiceField = new UiChoiceField();

        for (String lValue : DEFAULT_TEST_VALUES) {
            lUiChoiceField.addPossibleValue(new UiChoiceFieldValue(lValue,
                    lValue, null));
        }

        executeTestCopyChoiceFieldAppToHMI(lUiChoiceField, lRadioBox, true);
    }
}