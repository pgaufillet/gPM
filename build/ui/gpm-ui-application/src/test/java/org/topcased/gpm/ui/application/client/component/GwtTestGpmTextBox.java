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
import static org.topcased.gpm.ui.application.client.GpmTestsConstants.DEFAULT_TEST_INT_VALUE;
import static org.topcased.gpm.ui.application.client.GpmTestsConstants.DEFAULT_TEST_REAL_VALUE;

import org.topcased.gpm.ui.application.client.GpmTestsConstants;
import org.topcased.gpm.ui.component.client.container.field.GpmTextBox;
import org.topcased.gpm.ui.component.client.field.formater.GpmDoubleFormatter;
import org.topcased.gpm.ui.component.client.field.formater.GpmIntegerFormatter;
import org.topcased.gpm.ui.component.client.field.formater.GpmStringFormatter;
import org.topcased.gpm.ui.facade.shared.container.field.simple.UiIntegerField;
import org.topcased.gpm.ui.facade.shared.container.field.simple.UiRealField;
import org.topcased.gpm.ui.facade.shared.container.field.simple.UiSimpleField;
import org.topcased.gpm.ui.facade.shared.container.field.simple.UiStringField;

/**
 * GwtTestGpmTextBox : unit tests on gPM text box component.
 * 
 * @author frosier
 */
public class GwtTestGpmTextBox extends AbstractGpmTestCase {

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
        final GpmTextBox<String> lTextBox =
                new GpmTextBox<String>(GpmStringFormatter.getInstance());
        final UiSimpleField<String> lUiStringField = new UiStringField();
        lUiStringField.set(DEFAULT_TEST_STR_VALUE);

        executeTestCopySimpleFieldAppToHMI(lUiStringField, lTextBox);

        // Test with different values
        lUiStringField.set(DEFAULT_OTHER_TEST_STRING_VALUE);
        assertNotHasSameValues("Check that values are different",
                lUiStringField, lTextBox);
    }

    /**
     * Test data transfer GpmField > UiField. <br/>
     * Type of the simple field = String. <br/>
     */
    public void testGpmFieldToUiString() {
        final GpmTextBox<String> lTextBox =
                new GpmTextBox<String>(GpmStringFormatter.getInstance());
        final UiSimpleField<String> lUiStringField = new UiStringField();
        lTextBox.set(DEFAULT_TEST_STR_VALUE);

        executeTestCopySimpleFieldHMIToApp(lUiStringField, lTextBox);

        // Test with different values
        lTextBox.set(DEFAULT_OTHER_TEST_STRING_VALUE);
        assertNotHasSameValues("Check that values are different", lTextBox,
                lUiStringField);
    }

    /**
     * Test data transfer UiField > GpmField. <br/>
     * Type of the simple field = Integer. <br/>
     */
    public void testUiIntegerToGpmField() {
        final GpmTextBox<Integer> lTextBox =
                new GpmTextBox<Integer>(GpmIntegerFormatter.getInstance());
        final UiSimpleField<Integer> lUiIntegerField = new UiIntegerField();
        lUiIntegerField.set(DEFAULT_TEST_INT_VALUE);

        executeTestCopySimpleFieldAppToHMI(lUiIntegerField, lTextBox);

        // Test with different values
        lUiIntegerField.set(DEFAULT_OTHER_TEST_INT_VALUE);
        assertNotHasSameValues("Check that values are different",
                lUiIntegerField, lTextBox);
    }

    /**
     * Test data transfer GpmField > UiField. <br/>
     * Type of the simple field = Integer. <br/>
     */
    public void testGpmFieldToUiInteger() {
        final GpmTextBox<Integer> lTextBox =
                new GpmTextBox<Integer>(GpmIntegerFormatter.getInstance());
        final UiSimpleField<Integer> lUiIntegerField = new UiIntegerField();
        lTextBox.set(DEFAULT_TEST_INT_VALUE);

        executeTestCopySimpleFieldHMIToApp(lUiIntegerField, lTextBox);

        // Test with different values
        lTextBox.set(DEFAULT_OTHER_TEST_INT_VALUE);
        assertNotHasSameValues("Check that values are different", lTextBox,
                lUiIntegerField);
    }

    /**
     * Test data transfer UiField > GpmField. <br/>
     * Type of the simple field = Double. <br/>
     */
    public void testUiRealToGpmField() {
        final GpmTextBox<Double> lTextBox =
                new GpmTextBox<Double>(GpmDoubleFormatter.getInstance());
        final UiSimpleField<Double> lUiRealField = new UiRealField();
        lUiRealField.set(DEFAULT_TEST_REAL_VALUE);

        executeTestCopySimpleFieldAppToHMI(lUiRealField, lTextBox);

        // Test with different values
        lUiRealField.set(DEFAULT_OTHER_TEST_REAL_VALUE);
        assertNotHasSameValues("Check that values are different", lUiRealField,
                lTextBox);
    }

    /**
     * Test data transfer GpmField > UiField. <br/>
     * Type of the simple field = Double. <br/>
     */
    public void testGpmFieldToUiReal() {
        final GpmTextBox<Double> lTextBox =
                new GpmTextBox<Double>(GpmDoubleFormatter.getInstance());
        final UiSimpleField<Double> lUiRealField = new UiRealField();
        lTextBox.set(DEFAULT_TEST_REAL_VALUE);

        executeTestCopySimpleFieldHMIToApp(lUiRealField, lTextBox);

        // Test with different values
        lTextBox.set(DEFAULT_OTHER_TEST_REAL_VALUE);
        assertNotHasSameValues("Check that values are different", lTextBox,
                lUiRealField);
    }

    /**
     * Test with no value.
     */
    public void testEmpty() {
        executeTestCopySimpleFieldHMIToApp(new UiStringField(),
                new GpmTextBox<String>(GpmStringFormatter.getInstance()));

        executeTestCopySimpleFieldHMIToApp(new UiIntegerField(),
                new GpmTextBox<Integer>(GpmIntegerFormatter.getInstance()));

        executeTestCopySimpleFieldHMIToApp(new UiRealField(),
                new GpmTextBox<Double>(GpmDoubleFormatter.getInstance()));

        executeTestCopySimpleFieldAppToHMI(new UiStringField(),
                new GpmTextBox<String>(GpmStringFormatter.getInstance()), false);

        executeTestCopySimpleFieldAppToHMI(new UiIntegerField(),
                new GpmTextBox<Integer>(GpmIntegerFormatter.getInstance()));

        executeTestCopySimpleFieldAppToHMI(new UiRealField(),
                new GpmTextBox<Double>(GpmDoubleFormatter.getInstance()));
    }
}