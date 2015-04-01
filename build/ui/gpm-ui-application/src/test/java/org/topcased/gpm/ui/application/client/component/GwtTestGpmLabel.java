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

import static org.topcased.gpm.ui.application.client.GpmTestsConstants.DEFAULT_OTHER_TEST_INT_VALUE;
import static org.topcased.gpm.ui.application.client.GpmTestsConstants.DEFAULT_OTHER_TEST_REAL_VALUE;
import static org.topcased.gpm.ui.application.client.GpmTestsConstants.DEFAULT_OTHER_TEST_STRING_VALUE;

import org.topcased.gpm.ui.application.client.GpmTestsConstants;
import org.topcased.gpm.ui.component.client.container.field.GpmLabel;
import org.topcased.gpm.ui.component.client.container.field.GpmTextBox;
import org.topcased.gpm.ui.component.client.field.formater.GpmDoubleFormatter;
import org.topcased.gpm.ui.component.client.field.formater.GpmIntegerFormatter;
import org.topcased.gpm.ui.component.client.field.formater.GpmStringFormatter;
import org.topcased.gpm.ui.facade.shared.container.field.simple.UiIntegerField;
import org.topcased.gpm.ui.facade.shared.container.field.simple.UiRealField;
import org.topcased.gpm.ui.facade.shared.container.field.simple.UiSimpleField;
import org.topcased.gpm.ui.facade.shared.container.field.simple.UiStringField;

/**
 * GwtTestGpmLabel : unit tests on gPM Label component.
 * 
 * @author frosier
 */
public class GwtTestGpmLabel extends AbstractGpmTestCase {

    private static final String DEFAULT_TEST_STR_VALUE =
            GpmTestsConstants.DEFAULT_TEST_STRING_VALUE;

    private static final Integer DEFAULT_TEST_INT_VALUE =
            GpmTestsConstants.DEFAULT_TEST_INT_VALUE;

    private static final Double DEFAULT_TEST_REAL_VALUE =
            GpmTestsConstants.DEFAULT_TEST_REAL_VALUE;

    public String getModuleName() {
        return GpmTestsConstants.MODULE_NAME;
    }

    /**
     * Test data transfer UiStringField > GpmField. <br/>
     * value = DEFAULT_TEST_STR_VALUE.
     */
    public void testUiStringToGpmField() {
        final GpmLabel<String> lLabel =
                new GpmLabel<String>(GpmStringFormatter.getInstance());
        final UiSimpleField<String> lUiStringField = new UiStringField();
        lUiStringField.set(DEFAULT_TEST_STR_VALUE);

        executeTestCopySimpleFieldAppToHMI(lUiStringField, lLabel);

        // Test with different values
        lUiStringField.set(DEFAULT_OTHER_TEST_STRING_VALUE);
        assertNotHasSameValues("Check that values are different",
                lUiStringField, lLabel);
    }

    /**
     * Test data transfer GpmField > UiStringField. <br/>
     * value = DEFAULT_TEST_STR_VALUE.
     */
    public void testGpmFieldToUiString() {
        final GpmLabel<String> lLabel =
                new GpmLabel<String>(GpmStringFormatter.getInstance());
        final UiSimpleField<String> lUiStringField = new UiStringField();
        lLabel.set(DEFAULT_TEST_STR_VALUE);

        executeTestCopySimpleFieldHMIToApp(lUiStringField, lLabel);

        // Test with value not in possible values
        lLabel.set(DEFAULT_OTHER_TEST_STRING_VALUE);
        assertNotHasSameValues("Check that values are different", lLabel,
                lUiStringField);
    }

    /**
     * Test data transfer UiStringField > GpmField. <br/>
     * value = DEFAULT_TEST_STR_VALUE.
     */
    public void testUiIntegerToGpmField() {
        final GpmLabel<Integer> lLabel =
                new GpmLabel<Integer>(GpmIntegerFormatter.getInstance());
        final UiSimpleField<Integer> lUiIntegerField = new UiIntegerField();
        lUiIntegerField.set(DEFAULT_TEST_INT_VALUE);

        executeTestCopySimpleFieldAppToHMI(lUiIntegerField, lLabel);

        // Test with value not in possible values
        lUiIntegerField.set(DEFAULT_OTHER_TEST_INT_VALUE);
        assertNotHasSameValues("Check that values are different", lLabel,
                lUiIntegerField);
    }

    /**
     * Test data transfer GpmField > UiStringField. <br/>
     * value = DEFAULT_TEST_INT_VALUE.
     */
    public void testGpmFieldToUiInteger() {
        final GpmLabel<Integer> lLabel =
                new GpmLabel<Integer>(GpmIntegerFormatter.getInstance());
        final UiSimpleField<Integer> lUiIntegerField = new UiIntegerField();
        lLabel.set(DEFAULT_TEST_INT_VALUE);

        executeTestCopySimpleFieldHMIToApp(lUiIntegerField, lLabel);

        // Test with value not in possible values
        lLabel.set(DEFAULT_OTHER_TEST_INT_VALUE);
        assertNotHasSameValues("Check that values are different", lLabel,
                lUiIntegerField);
    }

    /**
     * Test data transfer UiStringField > GpmField. <br/>
     * value = DEFAULT_TEST_REAL_VALUE.
     */
    public void testUiRealToGpmField() {
        final GpmLabel<Double> lLabel =
                new GpmLabel<Double>(GpmDoubleFormatter.getInstance());
        final UiSimpleField<Double> lUiRealField = new UiRealField();
        lUiRealField.set(DEFAULT_TEST_REAL_VALUE);

        executeTestCopySimpleFieldAppToHMI(lUiRealField, lLabel);

        // Test with value not in possible values
        lUiRealField.set(DEFAULT_OTHER_TEST_REAL_VALUE);
        assertNotHasSameValues("Check that values are different", lLabel,
                lUiRealField);
    }

    /**
     * Test data transfer GpmField > UiStringField. <br/>
     * value = DEFAULT_TEST_REAL_VALUE.
     */
    public void testGpmFieldToUiReal() {
        final GpmLabel<Double> lLabel =
                new GpmLabel<Double>(GpmDoubleFormatter.getInstance());
        final UiSimpleField<Double> lUiRealField = new UiRealField();
        lLabel.set(DEFAULT_TEST_REAL_VALUE);

        executeTestCopySimpleFieldHMIToApp(lUiRealField, lLabel);

        // Test with value not in possible values
        lLabel.set(DEFAULT_OTHER_TEST_REAL_VALUE);
        assertNotHasSameValues("Check that values are different", lLabel,
                lUiRealField);
    }

    /**
     * Test with no value.
     */
    public void testEmpty() {
        // test empty fields from HMI to App.
        executeTestCopySimpleFieldHMIToApp(new UiStringField(),
                new GpmTextBox<String>(GpmStringFormatter.getInstance()));

        // test empty fields from App to HMI.
        executeTestCopySimpleFieldAppToHMI(new UiStringField(),
                new GpmTextBox<String>(GpmStringFormatter.getInstance()), false);
    }

    /**
     * Test data transfer GpmField > UiStringField. <br/>
     * value = null.
     */
    public void testNullStringAppToHMI() {
        final UiSimpleField<String> lUiStringField = new UiStringField();
        final GpmLabel<String> lLabel =
                new GpmLabel<String>(GpmStringFormatter.getInstance());
        lUiStringField.set(null);

        executeTestCopySimpleFieldAppToHMI(lUiStringField, lLabel, false);

        // Check if empty string on other side. 
        assertEquals("Test that the text value is the empty string", "",
                lLabel.get());
    }
}