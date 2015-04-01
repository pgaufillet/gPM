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

import org.topcased.gpm.ui.application.client.GpmTestsConstants;
import org.topcased.gpm.ui.component.client.container.field.GpmCheckBox;
import org.topcased.gpm.ui.facade.shared.container.field.simple.UiBooleanField;
import org.topcased.gpm.ui.facade.shared.container.field.simple.UiSimpleField;

/**
 * GwtTestGpmCheckBox : unit tests on gPM Check Box component.
 * 
 * @author frosier
 */
public class GwtTestGpmCheckBox extends AbstractGpmTestCase {

    public String getModuleName() {
        return GpmTestsConstants.MODULE_NAME;
    }

    /**
     * Test Ui boolean field to gPM check box.
     */
    public void testUiToGpmCheckBox() {
        final GpmCheckBox lCheckBox = new GpmCheckBox(true);
        final UiSimpleField<Boolean> lUiBooleanField = new UiBooleanField();

        // test true value.
        lUiBooleanField.set(Boolean.TRUE);
        executeTestCopySimpleFieldAppToHMI(lUiBooleanField, lCheckBox);

        // test false value.
        lUiBooleanField.set(Boolean.FALSE);
        executeTestCopySimpleFieldAppToHMI(lUiBooleanField, lCheckBox);

        // test different value
        lUiBooleanField.set(Boolean.TRUE);
        assertNotHasSameValues(
                "Check that different values are not considered equal",
                lCheckBox, lUiBooleanField);

        // test null value.
        lUiBooleanField.set(null);
        try {
            executeTestCopySimpleFieldAppToHMI(lUiBooleanField, lCheckBox);
            fail("GpmCheckBox should not be abble to accept null values.");
        }
        catch (IllegalArgumentException e) {
            // OK : gwt CheckBox do not allows null values.
        }
    }

    /**
     * Test gPM check box to Ui boolean field.
     */
    public void testGpmCheckBoxToUi() {
        final GpmCheckBox lCheckBox = new GpmCheckBox(true);
        final UiSimpleField<Boolean> lUiBooleanField = new UiBooleanField();

        // test true value.
        lCheckBox.set(Boolean.TRUE);
        executeTestCopySimpleFieldHMIToApp(lUiBooleanField, lCheckBox);

        // test false value.
        lCheckBox.set(Boolean.FALSE);
        executeTestCopySimpleFieldHMIToApp(lUiBooleanField, lCheckBox);

        // test different values
        lCheckBox.set(Boolean.TRUE);
        assertNotHasSameValues(
                "Check that different values are not considered equal",
                lUiBooleanField, lCheckBox);
    }
}