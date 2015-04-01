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

import org.topcased.gpm.ui.application.client.GpmTestsConstants;
import org.topcased.gpm.ui.component.client.container.field.GpmChoiceBoxValue;
import org.topcased.gpm.ui.component.client.container.field.GpmListBox;
import org.topcased.gpm.ui.facade.shared.container.field.simple.UiChoiceField;
import org.topcased.gpm.ui.facade.shared.container.field.simple.UiSimpleField;
import org.topcased.gpm.ui.facade.shared.container.field.simple.UiStringField;

/**
 * GwtTestGpmListBox : unit tests on gPM listBox component.
 * 
 * @author frosier
 */
public class GwtTestGpmListBox extends AbstractGpmTestCase {

    public static final List<String> DEFAULT_TEST_VALUES =
            GpmTestsConstants.DEFAULT_LIST_TEST_VALUES;

    public static final int DEFAULT_TEST_VALUE_SELECTED_INDEX = 3; // => matched value = "value 4"

    public static final String DEFAULT_TEST_VALUE =
            DEFAULT_TEST_VALUES.get(DEFAULT_TEST_VALUE_SELECTED_INDEX);

    public static final String DEFAULT_OTHER_VALID_TEST_VALUE =
            DEFAULT_TEST_VALUES.get(DEFAULT_TEST_VALUE_SELECTED_INDEX + 1);

    public String getModuleName() {
        return GpmTestsConstants.MODULE_NAME;
    }

    //***** Tests on UiSimpleField implementation ****//

    /**
     * Test data transfer UiField > GpmField. <br/>
     * Type of the simple field = String.
     */
    public void testUiStringToGpmField() {
        final GpmListBox lListBox = new GpmListBox();
        final UiSimpleField<String> lUiStringField = new UiStringField();

        lUiStringField.set(DEFAULT_TEST_VALUE);
        lListBox.setPossibleValues(GpmChoiceBoxValue.buildFromStrings(DEFAULT_TEST_VALUES));

        executeTestCopySimpleFieldAppToHMI(lUiStringField, lListBox);

        // Test different value
        lUiStringField.set(DEFAULT_OTHER_TEST_STRING_VALUE);
        assertNotHasSameValues("Check that values are different",
                lUiStringField, lListBox);
    }

    /**
     * Test data transfer GpmField > UiField. <br/>
     * Type of the simple field = String.
     */
    public void testGpmFieldToUiString() {
        final GpmListBox lListBox = new GpmListBox();
        final UiSimpleField<String> lUiStringField = new UiStringField();

        lListBox.setPossibleValues(GpmChoiceBoxValue.buildFromStrings(DEFAULT_TEST_VALUES));
        lListBox.set(DEFAULT_TEST_VALUE);

        executeTestCopySimpleFieldHMIToApp(lUiStringField, lListBox);

        // Test that values not in possible values can not be set
        lListBox.set(DEFAULT_OTHER_TEST_STRING_VALUE);
        assertHasSameValues(
                "Check that the listBox didn't took the value that "
                        + "was not in possible values", lListBox,
                lUiStringField);
    }

    /**
     * Test with no value with UiSimpleField<String> implementation.
     */
    public void testEmptyStringField() {
        // Will work because selecting "empty value" in GpmListBox unselect all values
        executeTestCopySimpleFieldAppToHMI(new UiStringField(),
                new GpmListBox());

        // Will work because no value selected in GpmListBox equals an empty UiStringField
        executeTestCopySimpleFieldHMIToApp(new UiStringField(),
                new GpmListBox());
    }

    //***** Tests on UiChoiceField implementation ****//
    /**
     * Test data transfer UiField > GpmField. <br/>
     * Type of the simple field = String.
     */
    public void testUiChoiceToGpmField() {
        final GpmListBox lListBox = new GpmListBox();
        final UiChoiceField lUiChoiceField = new UiChoiceField();

        lUiChoiceField.setCategoryValue(DEFAULT_TEST_VALUE);
        lListBox.setPossibleValues(GpmChoiceBoxValue.buildFromStrings(DEFAULT_TEST_VALUES));

        executeTestCopyChoiceFieldAppToHMI(lUiChoiceField, lListBox, true);

        // Test different value
        lUiChoiceField.setCategoryValue(DEFAULT_OTHER_VALID_TEST_VALUE);
        assertNotHasSameValues("Check that values are different",
                lUiChoiceField, lListBox);
    }

    /**
     * Test data transfer GpmField > UiField. <br/>
     * Type of the simple field = String.
     */
    public void testGpmFieldToUiChoice() {
        final GpmListBox lListBox = new GpmListBox();
        final UiChoiceField lUiChoiceField = new UiChoiceField();

        lListBox.setPossibleValues(GpmChoiceBoxValue.buildFromStrings(DEFAULT_TEST_VALUES));
        lListBox.set(DEFAULT_TEST_VALUE);

        executeTestCopyChoiceFieldHMIToApp(lUiChoiceField, lListBox, true);
        // Test different value
        lListBox.setCategoryValue(DEFAULT_OTHER_VALID_TEST_VALUE);
        assertNotHasSameValues("Check that values are different", lListBox,
                lUiChoiceField);
    }

    /**
     * Test with no value with UiChoiceField implementation.
     */
    public void testEmptyChoiceField() {
        // Will work because selecting "empty value" in GpmListBox unselect all values
        executeTestCopyChoiceFieldAppToHMI(new UiChoiceField(),
                new GpmListBox(), true);

        // Will work because no value selected in GpmListBox equals an empty UiChoiceField
        executeTestCopyChoiceFieldHMIToApp(new UiChoiceField(),
                new GpmListBox(), true);
    }
}