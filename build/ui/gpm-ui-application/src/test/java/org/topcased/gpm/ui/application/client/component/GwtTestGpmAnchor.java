/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien EBALLARD (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.component;

import static org.topcased.gpm.ui.application.client.GpmTestsConstants.DEFAULT_OTHER_TEST_STRING_VALUE;

import org.topcased.gpm.ui.application.client.GpmTestsConstants;
import org.topcased.gpm.ui.component.client.container.field.GpmAnchor;
import org.topcased.gpm.ui.component.client.util.GpmAnchorTarget;
import org.topcased.gpm.ui.facade.shared.container.field.simple.UiSimpleField;
import org.topcased.gpm.ui.facade.shared.container.field.simple.UiStringField;

/**
 * GwtTestGpmAnchor : unit tests on gPM Anchor component.
 * 
 * @author jeballar
 */
public class GwtTestGpmAnchor extends AbstractGpmTestCase {

    private static final String DEFAULT_TEST_STR_VALUE =
            GpmTestsConstants.DEFAULT_TEST_STRING_VALUE;

    public String getModuleName() {
        return GpmTestsConstants.MODULE_NAME;
    }

    /**
     * Test data transfer UiStringField > GpmField. <br/>
     * value = DEFAULT_TEST_STR_VALUE.
     */
    public void testUiStringToGpmField() {
        final GpmAnchor lAnchor = new GpmAnchor(GpmAnchorTarget.SELF);
        final UiSimpleField<String> lUiStringField = new UiStringField();
        lUiStringField.set(DEFAULT_TEST_STR_VALUE);

        executeTestCopySimpleFieldAppToHMI(lUiStringField, lAnchor);

        // Test with different values
        lUiStringField.set(DEFAULT_OTHER_TEST_STRING_VALUE);
        assertNotHasSameValues("Check that values are different",
                lUiStringField, lAnchor);
    }

    /**
     * Test data transfer GpmField > UiStringField. <br/>
     * value = DEFAULT_TEST_STR_VALUE.
     */
    public void testGpmFieldToUiString() {
        final GpmAnchor lAnchor = new GpmAnchor(GpmAnchorTarget.SELF);
        final UiSimpleField<String> lUiStringField = new UiStringField();
        lAnchor.set(DEFAULT_TEST_STR_VALUE);

        executeTestCopySimpleFieldHMIToApp(lUiStringField, lAnchor);

        // Test with different values
        lAnchor.set(DEFAULT_OTHER_TEST_STRING_VALUE);
        assertNotHasSameValues("Check that values are different", lAnchor,
                lUiStringField);
    }

    /**
     * Test with no value.
     */
    public void testEmpty() {
        // test empty fields from HMI to App.
        executeTestCopySimpleFieldHMIToApp(new UiStringField(), new GpmAnchor(
                GpmAnchorTarget.BLANK));

        // test empty fields from App to HMI.
        executeTestCopySimpleFieldAppToHMI(new UiStringField(), new GpmAnchor(
                GpmAnchorTarget.BLANK), false);
    }

    /**
     * Test data transfer GpmField > UiStringField. <br/>
     * value = null.
     */
    public void testNullStringAppToHMI() {
        final UiSimpleField<String> lUiStringField = new UiStringField();
        final GpmAnchor lAnchor = new GpmAnchor(GpmAnchorTarget.SELF);
        lUiStringField.set(null);

        executeTestCopySimpleFieldAppToHMI(lUiStringField, lAnchor, false);

        // Check if empty string on other side. 
        assertEquals("Test that the text value is the empty string", "",
                lAnchor.get());
    }
}