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
import static org.topcased.gpm.ui.application.client.GpmTestsConstants.DEFAULT_TEST_STRING_VALUE;

import org.topcased.gpm.ui.application.client.GpmTestsConstants;
import org.topcased.gpm.ui.component.client.container.field.GpmMultilineBox;
import org.topcased.gpm.ui.facade.shared.container.field.simple.UiSimpleField;
import org.topcased.gpm.ui.facade.shared.container.field.simple.UiStringField;

/**
 * GwtTestGpmListBox : unit tests on gPM mulitiline box component.
 * 
 * @author frosier
 */
public class GwtTestGpmMultilineBox extends AbstractGpmTestCase {

    public String getModuleName() {
        return GpmTestsConstants.MODULE_NAME;
    }

    /**
     * Test data transfer UiField > GpmField. <br/>
     * Type of the simple field = String.
     */
    public void testUiStringToGpmField() {
        final GpmMultilineBox lMultilineBox = new GpmMultilineBox();
        final UiSimpleField<String> lUiStringField = new UiStringField();

        lUiStringField.set(DEFAULT_TEST_STRING_VALUE);

        executeTestCopySimpleFieldAppToHMI(lUiStringField, lMultilineBox);

        // Test with different values
        lUiStringField.set(DEFAULT_OTHER_TEST_STRING_VALUE);
        assertNotHasSameValues("Check that values are different",
                lUiStringField, lMultilineBox);
    }

    /**
     * Test data transfer GpmField > UiField. <br/>
     * Type of the simple field = String.
     */
    public void testGpmFieldToUiString() {
        final GpmMultilineBox lMultilineBox = new GpmMultilineBox();
        final UiSimpleField<String> lUiStringField = new UiStringField();

        lMultilineBox.set(DEFAULT_TEST_STRING_VALUE);

        executeTestCopySimpleFieldHMIToApp(lUiStringField, lMultilineBox);
        // Test with different values
        lMultilineBox.set(DEFAULT_OTHER_TEST_STRING_VALUE);
        assertNotHasSameValues("Check that values are different",
                lMultilineBox, lUiStringField);
    }

    /**
     * Test with no value.
     */
    public void testEmpty() {
        executeTestCopySimpleFieldHMIToApp(new UiStringField(),
                new GpmMultilineBox());
        executeTestCopySimpleFieldAppToHMI(new UiStringField(),
                new GpmMultilineBox(), false);
    }
}