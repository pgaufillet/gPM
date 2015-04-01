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

import static org.topcased.gpm.ui.application.client.GpmTestsConstants.DEFAULT_LIST_TEST_VALUES;
import static org.topcased.gpm.ui.application.client.GpmTestsConstants.DEFAULT_OTHER_TEST_STRING_VALUE;

import org.topcased.gpm.ui.application.client.GpmTestsConstants;
import org.topcased.gpm.ui.component.client.container.field.GpmChoiceBoxValue;
import org.topcased.gpm.ui.component.client.container.field.GpmDropDownListBox;
import org.topcased.gpm.ui.facade.shared.container.field.simple.UiSimpleField;
import org.topcased.gpm.ui.facade.shared.container.field.simple.UiStringField;

/**
 * GwtTestGpmDropDownListBox : Unit tests on data transfer around
 * GwtTestGpmDropDownListBox field.
 * 
 * @author frosier
 */
public class GwtTestGpmDropDownListBox extends AbstractGpmTestCase {

    public static final int DEFAULT_TEST_VALUE_SELECTED_INDEX = 3; // => matched value = "value 4"

    public static final String DEFAULT_TEST_VALUE =
            DEFAULT_LIST_TEST_VALUES.get(DEFAULT_TEST_VALUE_SELECTED_INDEX);

    public String getModuleName() {
        return GpmTestsConstants.MODULE_NAME;
    }

    /**
     * Test data transfer GpmField > UiField. <br/>
     * Type of the simple field = String.
     */
    public void testUiStringToGpmField() {
        final GpmDropDownListBox lDropDownListBox = new GpmDropDownListBox();
        final UiSimpleField<String> lUiStringField = new UiStringField();

        lUiStringField.set(DEFAULT_TEST_VALUE);
        lDropDownListBox.setPossibleValues(GpmChoiceBoxValue.buildFromStrings(DEFAULT_LIST_TEST_VALUES));

        executeTestCopySimpleFieldAppToHMI(lUiStringField, lDropDownListBox);

        // Test with value not in possible values
        lUiStringField.set(DEFAULT_OTHER_TEST_STRING_VALUE);
        assertNotHasSameValues("Check that values are different",
                lUiStringField, lDropDownListBox);
    }

    /**
     * Test data transfer GpmField > UiField. <br/>
     * Type of the simple field = String.
     */
    public void testGpmFieldToUiString() {
        final GpmDropDownListBox lDropDownListBox = new GpmDropDownListBox();
        final UiSimpleField<String> lUiStringField = new UiStringField();

        lDropDownListBox.setPossibleValues(GpmChoiceBoxValue.buildFromStrings(DEFAULT_LIST_TEST_VALUES));
        lDropDownListBox.set(DEFAULT_TEST_VALUE);

        executeTestCopySimpleFieldHMIToApp(lUiStringField, lDropDownListBox);

        // Test with value not in possible values
        lDropDownListBox.set(DEFAULT_OTHER_TEST_STRING_VALUE);
        assertNotHasSameValues("Check that values are different",
                lDropDownListBox, lUiStringField);
    }

    /**
     * Test with no value.
     */
    public void testEmpty() {
        // No equals test done because hasSameValues does not differentiate null and empty strings. 
        executeTestCopySimpleFieldAppToHMI(new UiStringField(),
                new GpmDropDownListBox(), false);
        executeTestCopySimpleFieldHMIToApp(new UiStringField(),
                new GpmDropDownListBox(), false);
    }
}