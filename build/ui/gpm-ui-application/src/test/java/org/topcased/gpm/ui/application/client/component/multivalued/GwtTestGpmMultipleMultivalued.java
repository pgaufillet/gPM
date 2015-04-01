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

import java.util.ArrayList;

import org.topcased.gpm.ui.application.client.GpmTestsConstants;
import org.topcased.gpm.ui.application.client.component.AbstractGpmTestCase;
import org.topcased.gpm.ui.component.client.container.field.AbstractGpmField;
import org.topcased.gpm.ui.component.client.container.field.GpmAnchor;
import org.topcased.gpm.ui.component.client.container.field.GpmMultiple;
import org.topcased.gpm.ui.component.client.container.field.GpmTextBox;
import org.topcased.gpm.ui.component.client.container.field.multivalued.GpmMultipleMultivaluedField;
import org.topcased.gpm.ui.component.client.field.formater.GpmDoubleFormatter;
import org.topcased.gpm.ui.component.client.field.formater.GpmIntegerFormatter;
import org.topcased.gpm.ui.component.client.field.formater.GpmStringFormatter;
import org.topcased.gpm.ui.component.client.field.multivalued.GpmMultipleMultivaluedElement;
import org.topcased.gpm.ui.component.client.util.GpmAnchorTarget;
import org.topcased.gpm.ui.facade.shared.container.field.multiple.UiMultipleField;
import org.topcased.gpm.ui.facade.shared.container.field.multivalued.UiMultivaluedField;
import org.topcased.gpm.ui.facade.shared.container.field.simple.UiIntegerField;
import org.topcased.gpm.ui.facade.shared.container.field.simple.UiRealField;
import org.topcased.gpm.ui.facade.shared.container.field.simple.UiSimpleField;
import org.topcased.gpm.ui.facade.shared.container.field.simple.UiStringField;

/**
 * GwtTestGpmMultiple : unit tests on gPM Label component.
 * 
 * @author jeballar
 */
public class GwtTestGpmMultipleMultivalued extends AbstractGpmTestCase {

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
    public void testUiStringToGpmField() {
        final GpmMultiple lGpmMultiple =
                new GpmMultiple(new ArrayList<AbstractGpmField<?>>());
        UiMultipleField lUiMultiple = new UiMultipleField();

        // Template fields
        UiSimpleField<String> lUiField = new UiStringField();
        final String lFieldName1 =
                addFieldToUiAndGpmMultiple(
                        lGpmMultiple,
                        lUiMultiple,
                        new GpmTextBox<String>(GpmStringFormatter.getInstance()),
                        lUiField);

        lUiField = new UiStringField();
        final String lFieldName2 =
                addFieldToUiAndGpmMultiple(
                        lGpmMultiple,
                        lUiMultiple,
                        new GpmTextBox<String>(GpmStringFormatter.getInstance()),
                        lUiField);

        // Initialize multivalued multiples
        final UiMultivaluedField lUiMultivalued =
                new UiMultivaluedField(lUiMultiple);
        final GpmMultipleMultivaluedField lGpmMultipleMultivalued =
                new GpmMultipleMultivaluedField(lGpmMultiple.getFields(),
                        false, false, false, false);

        // UI Value fields
        lUiMultiple = (UiMultipleField) lUiMultivalued.addLine();
        lUiField = new UiStringField();
        lUiField.set(DEFAULT_TEST_STRING_VALUE);
        lUiField.setFieldName(lFieldName1);
        lUiMultiple.addField(lUiField);
        lUiField = new UiStringField();
        lUiField.set(DEFAULT_OTHER_TEST_STRING_VALUE);
        lUiField.setFieldName(lFieldName2);
        lUiMultiple.addField(lUiField);

        lUiMultiple = (UiMultipleField) lUiMultivalued.addLine();
        lUiField = new UiStringField();
        lUiField.set(DEFAULT_OTHER_TEST_STRING_VALUE);
        lUiField.setFieldName(lFieldName1);
        lUiMultiple.addField(lUiField);
        lUiField = new UiStringField();
        lUiField.set(DEFAULT_TEST_STRING_VALUE);
        lUiField.setFieldName(lFieldName2);
        lUiMultiple.addField(lUiField);

        executeTestCopyMultivaluedFieldAppToHMI(lUiMultivalued,
                lGpmMultipleMultivalued);

        // Test with different values
        lUiField.set(DEFAULT_OTHER_TEST_STRING_VALUE);
        assertNotHasSameValues("Check that values are different",
                lUiMultivalued, lGpmMultipleMultivalued);
    }

    /**
     * Test multiple UiString Field to App
     */
    public void testGpmFieldToUiString() {
        final GpmMultiple lGpmMultiple =
                new GpmMultiple(new ArrayList<AbstractGpmField<?>>());
        UiMultipleField lUiMultiple = new UiMultipleField();

        // Template fields
        GpmAnchor lGpmField = new GpmAnchor(GpmAnchorTarget.SELF);
        addFieldToUiAndGpmMultiple(lGpmMultiple, lUiMultiple, lGpmField,
                new UiStringField());

        lGpmField = new GpmAnchor(GpmAnchorTarget.SELF);
        addFieldToUiAndGpmMultiple(lGpmMultiple, lUiMultiple, lGpmField,
                new UiStringField());

        // Initialize mutlivalued multiples
        final GpmMultipleMultivaluedField lGpmMultivalued =
                new GpmMultipleMultivaluedField(lGpmMultiple.getFields(),
                        false, false, false, false);
        final UiMultivaluedField lUiMultivalued =
                new UiMultivaluedField(lUiMultiple);

        // Set GpmMultiple values
        GpmMultipleMultivaluedElement lElement = lGpmMultivalued.addLine();
        lGpmField = (GpmAnchor) lElement.getFields().get(0);
        lGpmField.set(DEFAULT_TEST_STRING_VALUE);
        lGpmField = (GpmAnchor) lElement.getFields().get(1);
        lGpmField.set(DEFAULT_OTHER_TEST_STRING_VALUE);

        executeTestCopyMultivaluedFieldHMIToApp(lUiMultivalued, lGpmMultivalued);

        // Test with different values
        lGpmField.set(DEFAULT_TEST_STRING_VALUE);
        assertNotHasSameValues("Check that values are different",
                lGpmMultivalued, lUiMultivalued);
    }

    /**
     * Test data transfer UiStringField > GpmField. <br/>
     * value = DEFAULT_TEST_STR_VALUE.
     */
    public void testUiIntegerToGpmField() {
        final GpmMultiple lGpmMultiple =
                new GpmMultiple(new ArrayList<AbstractGpmField<?>>());
        UiMultipleField lUiMultiple = new UiMultipleField();

        // Template fields
        UiSimpleField<Integer> lUiField = new UiIntegerField();
        final String lFieldName1 =
                addFieldToUiAndGpmMultiple(lGpmMultiple, lUiMultiple,
                        new GpmTextBox<Integer>(
                                GpmIntegerFormatter.getInstance()), lUiField);

        lUiField = new UiIntegerField();
        final String lFieldName2 =
                addFieldToUiAndGpmMultiple(lGpmMultiple, lUiMultiple,
                        new GpmTextBox<Integer>(
                                GpmIntegerFormatter.getInstance()), lUiField);

        // Initialize multivalued multiples
        final UiMultivaluedField lUiMultivalued =
                new UiMultivaluedField(lUiMultiple);
        final GpmMultipleMultivaluedField lGpmMultipleMultivalued =
                new GpmMultipleMultivaluedField(lGpmMultiple.getFields(),
                        false, false, false, false);

        // UI Value fields
        lUiMultiple = (UiMultipleField) lUiMultivalued.addLine();
        lUiField = new UiIntegerField();
        lUiField.set(DEFAULT_TEST_INT_VALUE);
        lUiField.setFieldName(lFieldName1);
        lUiMultiple.addField(lUiField);
        lUiField = new UiIntegerField();
        lUiField.set(DEFAULT_OTHER_TEST_INT_VALUE);
        lUiField.setFieldName(lFieldName2);
        lUiMultiple.addField(lUiField);

        lUiMultiple = (UiMultipleField) lUiMultivalued.addLine();
        lUiField = new UiIntegerField();
        lUiField.set(DEFAULT_OTHER_TEST_INT_VALUE);
        lUiField.setFieldName(lFieldName1);
        lUiMultiple.addField(lUiField);
        lUiField = new UiIntegerField();
        lUiField.set(DEFAULT_TEST_INT_VALUE);
        lUiField.setFieldName(lFieldName2);
        lUiMultiple.addField(lUiField);

        executeTestCopyMultivaluedFieldAppToHMI(lUiMultivalued,
                lGpmMultipleMultivalued);

        // Test with different values
        lUiField.set(DEFAULT_OTHER_TEST_INT_VALUE);
        assertNotHasSameValues("Check that values are different",
                lUiMultivalued, lGpmMultipleMultivalued);
    }

    /**
     * Test data transfer GpmField > UiStringField. <br/>
     * value = DEFAULT_TEST_INT_VALUE.
     */
    @SuppressWarnings("unchecked")
    public void testGpmFieldToUiInteger() {
        final GpmMultiple lGpmMultiple =
                new GpmMultiple(new ArrayList<AbstractGpmField<?>>());
        UiMultipleField lUiMultiple = new UiMultipleField();

        // Template fields
        GpmTextBox<Integer> lGpmField =
                new GpmTextBox<Integer>(GpmIntegerFormatter.getInstance());
        addFieldToUiAndGpmMultiple(lGpmMultiple, lUiMultiple, lGpmField,
                new UiIntegerField());

        lGpmField = new GpmTextBox<Integer>(GpmIntegerFormatter.getInstance());
        addFieldToUiAndGpmMultiple(lGpmMultiple, lUiMultiple, lGpmField,
                new UiIntegerField());

        // Initialize mutlivalued multiples
        final GpmMultipleMultivaluedField lGpmMultivalued =
                new GpmMultipleMultivaluedField(lGpmMultiple.getFields(),
                        false, false, false, false);
        final UiMultivaluedField lUiMultivalued =
                new UiMultivaluedField(lUiMultiple);

        // Set GpmMultiple values
        GpmMultipleMultivaluedElement lElement = lGpmMultivalued.addLine();
        lGpmField = (GpmTextBox<Integer>) lElement.getFields().get(0);
        lGpmField.set(DEFAULT_TEST_INT_VALUE);
        lGpmField = (GpmTextBox<Integer>) lElement.getFields().get(1);
        lGpmField.set(DEFAULT_OTHER_TEST_INT_VALUE);

        executeTestCopyMultivaluedFieldHMIToApp(lUiMultivalued, lGpmMultivalued);

        // Test with different values
        lGpmField.set(DEFAULT_TEST_INT_VALUE);
        assertNotHasSameValues("Check that values are different",
                lGpmMultivalued, lUiMultivalued);
    }

    /**
     * Test data transfer UiStringField > GpmField. <br/>
     * value = DEFAULT_TEST_REAL_VALUE.
     */
    public void testUiRealToGpmField() {
        final GpmMultiple lGpmMultiple =
                new GpmMultiple(new ArrayList<AbstractGpmField<?>>());
        UiMultipleField lUiMultiple = new UiMultipleField();

        // Template fields
        UiSimpleField<Double> lUiField = new UiRealField();
        final String lFieldName1 =
                addFieldToUiAndGpmMultiple(
                        lGpmMultiple,
                        lUiMultiple,
                        new GpmTextBox<Double>(GpmDoubleFormatter.getInstance()),
                        lUiField);

        lUiField = new UiRealField();
        final String lFieldName2 =
                addFieldToUiAndGpmMultiple(
                        lGpmMultiple,
                        lUiMultiple,
                        new GpmTextBox<Double>(GpmDoubleFormatter.getInstance()),
                        lUiField);

        // Initialize multivalued multiples
        final UiMultivaluedField lUiMultivalued =
                new UiMultivaluedField(lUiMultiple);
        final GpmMultipleMultivaluedField lGpmMultipleMultivalued =
                new GpmMultipleMultivaluedField(lGpmMultiple.getFields(),
                        false, false, false, false);

        // UI Value fields
        lUiMultiple = (UiMultipleField) lUiMultivalued.addLine();
        lUiField = new UiRealField();
        lUiField.set(DEFAULT_TEST_REAL_VALUE);
        lUiField.setFieldName(lFieldName1);
        lUiMultiple.addField(lUiField);
        lUiField = new UiRealField();
        lUiField.set(DEFAULT_OTHER_TEST_REAL_VALUE);
        lUiField.setFieldName(lFieldName2);
        lUiMultiple.addField(lUiField);

        lUiMultiple = (UiMultipleField) lUiMultivalued.addLine();
        lUiField = new UiRealField();
        lUiField.set(DEFAULT_OTHER_TEST_REAL_VALUE);
        lUiField.setFieldName(lFieldName1);
        lUiMultiple.addField(lUiField);
        lUiField = new UiRealField();
        lUiField.set(DEFAULT_TEST_REAL_VALUE);
        lUiField.setFieldName(lFieldName2);
        lUiMultiple.addField(lUiField);

        executeTestCopyMultivaluedFieldAppToHMI(lUiMultivalued,
                lGpmMultipleMultivalued);

        // Test with different values
        lUiField.set(DEFAULT_OTHER_TEST_REAL_VALUE);
        assertNotHasSameValues("Check that values are different",
                lUiMultivalued, lGpmMultipleMultivalued);
    }

    /**
     * Test data transfer GpmField > UiStringField. <br/>
     * value = DEFAULT_TEST_REAL_VALUE.
     */
    @SuppressWarnings("unchecked")
    public void testGpmFieldToUiReal() {
        final GpmMultiple lGpmMultiple =
                new GpmMultiple(new ArrayList<AbstractGpmField<?>>());
        UiMultipleField lUiMultiple = new UiMultipleField();

        // Template fields
        GpmTextBox<Double> lGpmField =
                new GpmTextBox<Double>(GpmDoubleFormatter.getInstance());
        addFieldToUiAndGpmMultiple(lGpmMultiple, lUiMultiple, lGpmField,
                new UiRealField());

        lGpmField = new GpmTextBox<Double>(GpmDoubleFormatter.getInstance());
        addFieldToUiAndGpmMultiple(lGpmMultiple, lUiMultiple, lGpmField,
                new UiRealField());

        // Initialize mutlivalued multiples
        final GpmMultipleMultivaluedField lGpmMultivalued =
                new GpmMultipleMultivaluedField(lGpmMultiple.getFields(),
                        false, false, false, false);
        final UiMultivaluedField lUiMultivalued =
                new UiMultivaluedField(lUiMultiple);

        // Set GpmMultiple values
        GpmMultipleMultivaluedElement lElement = lGpmMultivalued.addLine();
        lGpmField = (GpmTextBox<Double>) lElement.getFields().get(0);
        lGpmField.set(DEFAULT_TEST_REAL_VALUE);
        lGpmField = (GpmTextBox<Double>) lElement.getFields().get(1);
        lGpmField.set(DEFAULT_OTHER_TEST_REAL_VALUE);

        executeTestCopyMultivaluedFieldHMIToApp(lUiMultivalued, lGpmMultivalued);

        // Test with different values
        lGpmField.set(DEFAULT_TEST_REAL_VALUE);
        assertNotHasSameValues("Check that values are different",
                lGpmMultivalued, lUiMultivalued);
    }

    /**
     * Test with no value. Special case because UiField allows empty
     * MultipleMultivalued, whereas GpmMultipleMultivalued will always
     * initialize with one template clone line. Copy from Gpm To UI will be
     * "hasSameValues" equal, but copy from UI to Gpm will not be
     * "hasSameValues" equal.
     */
    public void testEmpty() {
        final GpmMultiple lGpmMultiple =
                new GpmMultiple(new ArrayList<AbstractGpmField<?>>());
        UiMultipleField lUiMultiple = new UiMultipleField();

        // Template fields
        UiSimpleField<Double> lUiField = new UiRealField();
        addFieldToUiAndGpmMultiple(lGpmMultiple, lUiMultiple,
                new GpmTextBox<Double>(GpmDoubleFormatter.getInstance()),
                lUiField);

        lUiField = new UiRealField();
        addFieldToUiAndGpmMultiple(lGpmMultiple, lUiMultiple,
                new GpmTextBox<Double>(GpmDoubleFormatter.getInstance()),
                lUiField);

        // Initialize multivalued multiples
        UiMultivaluedField lUiMultivalued = new UiMultivaluedField(lUiMultiple);
        GpmMultipleMultivaluedField lGpmMultipleMultivalued =
                new GpmMultipleMultivaluedField(lGpmMultiple.getFields(),
                        false, false, false, false);

        // test empty fields from HMI to App.
        executeTestCopyMultivaluedFieldHMIToApp(lUiMultivalued,
                lGpmMultipleMultivalued);

        lUiMultivalued = new UiMultivaluedField(lUiMultiple);
        lGpmMultipleMultivalued =
                new GpmMultipleMultivaluedField(lGpmMultiple.getFields(),
                        false, false, false, false);

        // test empty fields from App to HMI.
        lGpmMultipleMultivalued.copy(lUiMultivalued);

        assertNotHasSameValues(
                "Check that values of mutlivalued UI and Gpm are different "
                        + "instead of 'hasSameValues' equal", lUiMultivalued,
                lGpmMultipleMultivalued);
    }
}