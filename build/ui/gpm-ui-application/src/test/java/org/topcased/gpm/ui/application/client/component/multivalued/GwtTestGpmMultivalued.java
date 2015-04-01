/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien EBALLARD (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.component.multivalued;

import org.topcased.gpm.ui.application.client.GpmTestsConstants;
import org.topcased.gpm.ui.application.client.component.AbstractGpmTestCase;
import org.topcased.gpm.ui.component.client.container.field.AbstractGpmField;
import org.topcased.gpm.ui.component.client.container.field.GpmTextBox;
import org.topcased.gpm.ui.component.client.container.field.multivalued.GpmMultivaluedField;
import org.topcased.gpm.ui.component.client.field.formater.GpmDoubleFormatter;
import org.topcased.gpm.ui.component.client.field.formater.GpmIntegerFormatter;
import org.topcased.gpm.ui.component.client.field.formater.GpmStringFormatter;
import org.topcased.gpm.ui.facade.shared.container.field.multivalued.UiMultivaluedField;
import org.topcased.gpm.ui.facade.shared.container.field.simple.UiIntegerField;
import org.topcased.gpm.ui.facade.shared.container.field.simple.UiRealField;
import org.topcased.gpm.ui.facade.shared.container.field.simple.UiSimpleField;
import org.topcased.gpm.ui.facade.shared.container.field.simple.UiStringField;

/**
 * Unit tests on GpmMultivalued component containing various Gpm fields.
 * 
 * @author jeballar
 */
public class GwtTestGpmMultivalued extends AbstractGpmTestCase {

    private static final String DEFAULT_TEST_STRING_VALUE =
            GpmTestsConstants.DEFAULT_TEST_STRING_VALUE;

    private static final String DEFAULT_OTHER_TEST_STRING_VALUE =
            GpmTestsConstants.DEFAULT_OTHER_TEST_STRING_VALUE;

    private static final Integer DEFAULT_TEST_INT_VALUE =
            GpmTestsConstants.DEFAULT_TEST_INT_VALUE;

    private static final Integer DEFAULT_OTHER_TEST_INT_VALUE =
            GpmTestsConstants.DEFAULT_OTHER_TEST_INT_VALUE;

    private static final Double DEFAULT_TEST_REAL_VALUE =
            GpmTestsConstants.DEFAULT_TEST_REAL_VALUE;

    private static final Double DEFAULT_OTHER_TEST_REAL_VALUE =
            GpmTestsConstants.DEFAULT_OTHER_TEST_REAL_VALUE;

    public String getModuleName() {
        return GpmTestsConstants.MODULE_NAME;
    }

    /**
     * Test Multiple UiStringFields to HMI
     */
    @SuppressWarnings("unchecked")
    public void testUiStringToGpmField() {
        // Template fields
        UiSimpleField<String> lUiField = new UiStringField();
        final UiMultivaluedField lUiMulti = new UiMultivaluedField(lUiField);
        GpmTextBox<String> lGpmField =
                new GpmTextBox<String>(GpmStringFormatter.getInstance());
        GpmMultivaluedField<AbstractGpmField<?>> lGpmMulti =
                new GpmMultivaluedField<AbstractGpmField<?>>(lGpmField, false,
                        false, false);

        // Set values
        ((UiSimpleField<String>) lUiMulti.addLine()).set(DEFAULT_TEST_STRING_VALUE);
        ((UiSimpleField<String>) lUiMulti.addLine()).set(DEFAULT_OTHER_TEST_STRING_VALUE);

        // Checks
        executeTestCopyMultivaluedFieldAppToHMI(lUiMulti, lGpmMulti);

        // Test with different values
        ((UiSimpleField<String>) lUiMulti.addLine()).set(DEFAULT_TEST_STRING_VALUE);
        assertNotHasSameValues("Check that values are different", lUiMulti,
                lGpmMulti);
    }

    /**
     * Test multiple UiString Field to App
     */
    @SuppressWarnings("unchecked")
    public void testGpmFieldToUiString() {
        // Template fields
        UiSimpleField<String> lUiField = new UiStringField();
        final UiMultivaluedField lUiMulti = new UiMultivaluedField(lUiField);
        GpmTextBox<String> lGpmField =
                new GpmTextBox<String>(GpmStringFormatter.getInstance());
        GpmMultivaluedField<AbstractGpmField<?>> lGpmMulti =
                new GpmMultivaluedField<AbstractGpmField<?>>(lGpmField, false,
                        false, false);

        // Set values
        ((GpmTextBox<String>) lGpmMulti.addLine()).set(DEFAULT_TEST_STRING_VALUE);
        ((GpmTextBox<String>) lGpmMulti.addLine()).set(DEFAULT_OTHER_TEST_STRING_VALUE);

        // Checks
        executeTestCopyMultivaluedFieldHMIToApp(lUiMulti, lGpmMulti);

        // Test with different values
        ((GpmTextBox<String>) lGpmMulti.addLine()).set(DEFAULT_TEST_STRING_VALUE);
        assertNotHasSameValues("Check that values are different", lUiMulti,
                lGpmMulti);
    }

    /**
     * Test data transfer UiIntegerField > GpmField. <br/>
     * value = DEFAULT_TEST_INT_VALUE.
     */
    @SuppressWarnings("unchecked")
    public void testUiIntegerToGpmField() {
        // Template fields
        UiSimpleField<Integer> lUiField = new UiIntegerField();
        final UiMultivaluedField lUiMulti = new UiMultivaluedField(lUiField);
        GpmTextBox<Integer> lGpmField =
                new GpmTextBox<Integer>(GpmIntegerFormatter.getInstance());
        GpmMultivaluedField<AbstractGpmField<?>> lGpmMulti =
                new GpmMultivaluedField<AbstractGpmField<?>>(lGpmField, false,
                        false, false);

        // Set values
        ((UiSimpleField<Integer>) lUiMulti.addLine()).set(DEFAULT_TEST_INT_VALUE);
        ((UiSimpleField<Integer>) lUiMulti.addLine()).set(DEFAULT_OTHER_TEST_INT_VALUE);

        // Checks
        executeTestCopyMultivaluedFieldAppToHMI(lUiMulti, lGpmMulti);

        // Test with different values
        ((UiSimpleField<Integer>) lUiMulti.addLine()).set(DEFAULT_TEST_INT_VALUE);
        assertNotHasSameValues("Check that values are different", lUiMulti,
                lGpmMulti);
    }

    /**
     * Test data transfer GpmField > UiStringField. <br/>
     * value = DEFAULT_TEST_INT_VALUE.
     */
    @SuppressWarnings("unchecked")
    public void testGpmFieldToUiInteger() {
        // Template fields
        UiSimpleField<Integer> lUiField = new UiIntegerField();
        final UiMultivaluedField lUiMulti = new UiMultivaluedField(lUiField);
        GpmTextBox<Integer> lGpmField =
                new GpmTextBox<Integer>(GpmIntegerFormatter.getInstance());
        GpmMultivaluedField<AbstractGpmField<?>> lGpmMulti =
                new GpmMultivaluedField<AbstractGpmField<?>>(lGpmField, false,
                        false, false);

        // Set values
        ((GpmTextBox<Integer>) lGpmMulti.addLine()).set(DEFAULT_TEST_INT_VALUE);
        ((GpmTextBox<Integer>) lGpmMulti.addLine()).set(DEFAULT_OTHER_TEST_INT_VALUE);

        // Checks
        executeTestCopyMultivaluedFieldHMIToApp(lUiMulti, lGpmMulti);

        // Test with different values
        ((GpmTextBox<Integer>) lGpmMulti.addLine()).set(DEFAULT_TEST_INT_VALUE);
        assertNotHasSameValues("Check that values are different", lUiMulti,
                lGpmMulti);
    }

    /**
     * Test data transfer UiStringField > GpmField. <br/>
     * value = DEFAULT_TEST_REAL_VALUE.
     */
    @SuppressWarnings("unchecked")
    public void testUiRealToGpmField() {
        // Template fields
        UiSimpleField<Double> lUiField = new UiRealField();
        final UiMultivaluedField lUiMulti = new UiMultivaluedField(lUiField);
        GpmTextBox<Double> lGpmField =
                new GpmTextBox<Double>(GpmDoubleFormatter.getInstance());
        GpmMultivaluedField<AbstractGpmField<?>> lGpmMulti =
                new GpmMultivaluedField<AbstractGpmField<?>>(lGpmField, false,
                        false, false);

        // Set values
        ((UiSimpleField<Double>) lUiMulti.addLine()).set(DEFAULT_TEST_REAL_VALUE);
        ((UiSimpleField<Double>) lUiMulti.addLine()).set(DEFAULT_OTHER_TEST_REAL_VALUE);

        // Checks
        executeTestCopyMultivaluedFieldAppToHMI(lUiMulti, lGpmMulti);

        // Test with different values
        ((UiSimpleField<Double>) lUiMulti.addLine()).set(DEFAULT_TEST_REAL_VALUE);
        assertNotHasSameValues("Check that values are different", lUiMulti,
                lGpmMulti);
    }

    /**
     * Test data transfer GpmField > UiStringField. <br/>
     * value = DEFAULT_TEST_REAL_VALUE.
     */
    @SuppressWarnings("unchecked")
    public void testGpmFieldToUiReal() {
        // Template fields
        UiSimpleField<Double> lUiField = new UiRealField();
        final UiMultivaluedField lUiMulti = new UiMultivaluedField(lUiField);
        GpmTextBox<Double> lGpmField =
                new GpmTextBox<Double>(GpmDoubleFormatter.getInstance());
        GpmMultivaluedField<AbstractGpmField<?>> lGpmMulti =
                new GpmMultivaluedField<AbstractGpmField<?>>(lGpmField, false,
                        false, false);

        // Set values
        ((GpmTextBox<Double>) lGpmMulti.addLine()).set(DEFAULT_TEST_REAL_VALUE);
        ((GpmTextBox<Double>) lGpmMulti.addLine()).set(DEFAULT_OTHER_TEST_REAL_VALUE);

        // Checks
        executeTestCopyMultivaluedFieldHMIToApp(lUiMulti, lGpmMulti);

        // Test with different values
        ((GpmTextBox<Double>) lGpmMulti.addLine()).set(DEFAULT_TEST_REAL_VALUE);
        assertNotHasSameValues("Check that values are different", lUiMulti,
                lGpmMulti);
    }

    /**
     * Test with no value. Special case because UiField allows empty
     * Multivalued, whereas GpmMultivalued will always initialize with one
     * template clone line. Copy from Gpm To UI will be "hasSameValues" equal,
     * but copy from UI to Gpm will not be "hasSameValues" equal.
     */
    public void testEmpty() { // Template fields
        UiSimpleField<Double> lUiField = new UiRealField();
        UiMultivaluedField lUiMulti = new UiMultivaluedField(lUiField);
        GpmTextBox<Double> lGpmField =
                new GpmTextBox<Double>(GpmDoubleFormatter.getInstance());
        GpmMultivaluedField<AbstractGpmField<?>> lGpmMulti =
                new GpmMultivaluedField<AbstractGpmField<?>>(lGpmField, false,
                        false, false);

        // test empty fields from HMI to App.
        executeTestCopyMultivaluedFieldHMIToApp(lUiMulti, lGpmMulti);

        lUiMulti = new UiMultivaluedField(lUiField);

        // test empty fields from App to HMI.
        lGpmMulti.copy(lUiMulti);

        assertNotHasSameValues(
                "Check that values of mutlivalued UI and Gpm are different "
                        + "instead of 'hasSameValues' equal", lUiMulti,
                lGpmMulti);
    }
}