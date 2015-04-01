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

import org.topcased.gpm.ui.application.client.GpmTestsConstants;
import org.topcased.gpm.ui.component.client.container.field.GpmPasswordBox;
import org.topcased.gpm.ui.facade.shared.container.field.simple.UiSimpleField;
import org.topcased.gpm.ui.facade.shared.container.field.simple.UiStringField;

/**
 * Unit tests on gPM password box component.
 * 
 * @author jeballar
 */
public class GwtTestGpmPasswordBox extends AbstractGpmTestCase {

    private static final String DEFAULT_TEST_STR_VALUE =
            GpmTestsConstants.DEFAULT_TEST_STRING_VALUE;

    public String getModuleName() {
        return GpmTestsConstants.MODULE_NAME;
    }

    /**
     * Test data transfer UiField > GpmField. <br/>
     * Type of the simple field = String. <br/>
     */
    public void testUiStringToGpmField() {
        final GpmPasswordBox lPwdBox = new GpmPasswordBox();
        final UiSimpleField<String> lUiStringField = new UiStringField();
        lUiStringField.set(DEFAULT_TEST_STR_VALUE);

        executeTestCopySimpleFieldAppToHMI(lUiStringField, lPwdBox);

        // Test with different values
        lUiStringField.set(DEFAULT_OTHER_TEST_STRING_VALUE);
        assertNotHasSameValues("Check that values are different",
                lUiStringField, lPwdBox);
    }

    /**
     * Test data transfer GpmField > UiField. <br/>
     * Type of the simple field = String. <br/>
     */
    public void testGpmFieldToUiString() {
        final GpmPasswordBox lPwdBox = new GpmPasswordBox();
        final UiSimpleField<String> lUiStringField = new UiStringField();
        lPwdBox.set(DEFAULT_TEST_STR_VALUE);

        executeTestCopySimpleFieldHMIToApp(lUiStringField, lPwdBox);

        // Test with different values
        lPwdBox.set(DEFAULT_OTHER_TEST_STRING_VALUE);
        assertNotHasSameValues("Check that values are different", lPwdBox,
                lUiStringField);
    }

    /**
     * Test with no value.
     */
    public void testEmpty() {
        executeTestCopySimpleFieldHMIToApp(new UiStringField(),
                new GpmPasswordBox());

        executeTestCopySimpleFieldAppToHMI(new UiStringField(),
                new GpmPasswordBox(), false);
    }
}