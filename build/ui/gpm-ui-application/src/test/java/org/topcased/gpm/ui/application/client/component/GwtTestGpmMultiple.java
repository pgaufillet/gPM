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

import java.util.ArrayList;

import org.topcased.gpm.business.values.field.multiple.BusinessMultipleField;
import org.topcased.gpm.ui.application.client.GpmTestsConstants;
import org.topcased.gpm.ui.component.client.container.field.AbstractGpmField;
import org.topcased.gpm.ui.component.client.container.field.GpmAnchor;
import org.topcased.gpm.ui.component.client.container.field.GpmMultiple;
import org.topcased.gpm.ui.component.client.container.field.GpmTextBox;
import org.topcased.gpm.ui.component.client.field.formater.GpmDoubleFormatter;
import org.topcased.gpm.ui.component.client.field.formater.GpmIntegerFormatter;
import org.topcased.gpm.ui.component.client.field.formater.GpmStringFormatter;
import org.topcased.gpm.ui.component.client.util.GpmAnchorTarget;
import org.topcased.gpm.ui.facade.shared.container.field.multiple.UiMultipleField;
import org.topcased.gpm.ui.facade.shared.container.field.simple.UiIntegerField;
import org.topcased.gpm.ui.facade.shared.container.field.simple.UiRealField;
import org.topcased.gpm.ui.facade.shared.container.field.simple.UiSimpleField;
import org.topcased.gpm.ui.facade.shared.container.field.simple.UiStringField;

/**
 * GwtTestGpmMultiple : unit tests on gPM Label component.
 * 
 * @author jeballar
 */
public class GwtTestGpmMultiple extends AbstractGpmTestCase {

    private static final String DEFAULT_TEST_STRING_VALUE =
            GpmTestsConstants.DEFAULT_TEST_STRING_VALUE;

    private static final String DEFAULT_OTHER_TEST_STRING_VALUE =
            GpmTestsConstants.DEFAULT_OTHER_TEST_STRING_VALUE;

    private static final Integer DEFAULT_TEST_INT_VALUE =
            GpmTestsConstants.DEFAULT_TEST_INT_VALUE;

    private static final Double DEFAULT_TEST_REAL_VALUE =
            GpmTestsConstants.DEFAULT_TEST_REAL_VALUE;

    private static final Integer DEFAULT_OTHER_TEST_INT_VALUE =
            GpmTestsConstants.DEFAULT_OTHER_TEST_INT_VALUE;

    public String getModuleName() {
        return GpmTestsConstants.MODULE_NAME;
    }

    private void executeTestCopyMultipleFieldAppToHMI(
            final UiMultipleField pUiField,
            final BusinessMultipleField pGpmMultiple) {
        // Fail when at least one field is null
        failWhenNullParameter(pUiField, pGpmMultiple);

        // Test copy.
        pGpmMultiple.copy(pUiField);

        // Test hasSameValues.
        assertHasSameValues("Check that hasSameValues is true", pGpmMultiple,
                pUiField);
    }

    /**
     * Execute a copy of the GpmMultiple into the UiMultiple and compare them
     * using hasSameValues. Fails if hasSameValues returns <CODE>false</CODE>
     * 
     * @param pUiField
     *            The UiMultiple
     * @param pGpmMultiple
     *            The GpmMultiple
     */
    private void executeTestCopyMultipleFieldHMIToApp(
            final UiMultipleField pUiField,
            final BusinessMultipleField pGpmMultiple) {
        // Fail when at least one field is null
        failWhenNullParameter(pUiField, pGpmMultiple);

        // Test copy.
        pUiField.copy(pGpmMultiple);

        // Test hasSameValues.
        assertHasSameValues("Check that hasSameValues is true", pGpmMultiple,
                pUiField);
    }

    /**
     * Test Multiple UiStringFields to HMI
     */
    public void testUiStringToGpmField() {
        final UiMultipleField lUiMultiple = new UiMultipleField();
        final GpmMultiple lGpmMultiple =
                new GpmMultiple(new ArrayList<AbstractGpmField<?>>());

        UiSimpleField<String> lUiField = new UiStringField();
        addFieldToUiAndGpmMultiple(lGpmMultiple, lUiMultiple,
                new GpmTextBox<String>(GpmStringFormatter.getInstance()),
                lUiField);
        lUiField.set(DEFAULT_TEST_STRING_VALUE);

        lUiField = new UiStringField();
        addFieldToUiAndGpmMultiple(lGpmMultiple, lUiMultiple,
                new GpmTextBox<String>(GpmStringFormatter.getInstance()),
                lUiField);
        lUiField.set(DEFAULT_OTHER_TEST_STRING_VALUE);

        executeTestCopyMultipleFieldAppToHMI(lUiMultiple, lGpmMultiple);

        // Test with different values
        lUiField.set(DEFAULT_TEST_STRING_VALUE);
        assertNotHasSameValues("Check that values are different", lUiField,
                lGpmMultiple);
    }

    /**
     * Test multiple UiString Field to App
     */
    public void testGpmFieldToUiString() {
        final GpmMultiple lGpmMultiple =
                new GpmMultiple(new ArrayList<AbstractGpmField<?>>());
        final UiMultipleField lUiMultiple = new UiMultipleField();

        GpmAnchor lGpmField = new GpmAnchor(GpmAnchorTarget.SELF);
        addFieldToUiAndGpmMultiple(lGpmMultiple, lUiMultiple, lGpmField,
                new UiStringField());
        lGpmField.set(DEFAULT_TEST_STRING_VALUE);

        lGpmField = new GpmAnchor(GpmAnchorTarget.SELF);
        addFieldToUiAndGpmMultiple(lGpmMultiple, lUiMultiple, lGpmField,
                new UiStringField());
        lGpmField.set(DEFAULT_OTHER_TEST_STRING_VALUE);

        executeTestCopyMultipleFieldHMIToApp(lUiMultiple, lGpmMultiple);

        // Test with different values
        lGpmField.set(DEFAULT_TEST_STRING_VALUE);
        assertNotHasSameValues("Check that values are different", lGpmMultiple,
                lUiMultiple);
    }

    /**
     * Test data transfer UiStringField > GpmField. <br/>
     * value type = INT
     */
    public void testUiIntegerToGpmField() {
        final GpmMultiple lGpmMultiple =
                new GpmMultiple(new ArrayList<AbstractGpmField<?>>());
        final UiMultipleField lUiMultiple = new UiMultipleField();

        UiIntegerField lUiField = new UiIntegerField();
        addFieldToUiAndGpmMultiple(lGpmMultiple, lUiMultiple,
                new GpmTextBox<Integer>(GpmIntegerFormatter.getInstance()),
                lUiField);
        lUiField.set(DEFAULT_TEST_INT_VALUE);

        lUiField = new UiIntegerField();
        addFieldToUiAndGpmMultiple(lGpmMultiple, lUiMultiple,
                new GpmTextBox<Integer>(GpmIntegerFormatter.getInstance()),
                lUiField);
        lUiField.set(DEFAULT_OTHER_TEST_INT_VALUE);

        executeTestCopyMultipleFieldAppToHMI(lUiMultiple, lGpmMultiple);

        // Test with different values
        lUiField.set(DEFAULT_TEST_INT_VALUE);
        assertNotHasSameValues("Check that values are different", lUiField,
                lGpmMultiple);
    }

    /**
     * Test data transfer GpmField > UiStringField. <br/>
     * value type = INT
     */
    public void testGpmFieldToUiInteger() {
        final GpmMultiple lGpmMultiple =
                new GpmMultiple(new ArrayList<AbstractGpmField<?>>());
        final UiMultipleField lUiMultiple = new UiMultipleField();

        final GpmTextBox<Integer> lGpmTextBox =
                new GpmTextBox<Integer>(GpmIntegerFormatter.getInstance());
        addFieldToUiAndGpmMultiple(lGpmMultiple, lUiMultiple, lGpmTextBox,
                new UiStringField());
        lGpmTextBox.set(DEFAULT_TEST_INT_VALUE);

        executeTestCopyMultipleFieldHMIToApp(lUiMultiple, lGpmMultiple);
    }

    /**
     * Test data transfer UiStringField > GpmField. <br/>
     * value type = REAL
     */
    public void testUiRealToGpmField() {
        final GpmMultiple lGpmMultiple =
                new GpmMultiple(new ArrayList<AbstractGpmField<?>>());
        final UiMultipleField lUiMultiple = new UiMultipleField();

        final UiSimpleField<Double> lUiRealField = new UiRealField();
        addFieldToUiAndGpmMultiple(lGpmMultiple, lUiMultiple,
                new GpmTextBox<Double>(GpmDoubleFormatter.getInstance()),
                lUiRealField);
        lUiRealField.set(DEFAULT_TEST_REAL_VALUE);

        executeTestCopyMultipleFieldAppToHMI(lUiMultiple, lGpmMultiple);
    }

    /**
     * Test data transfer GpmField > UiStringField. <br/>
     * value type = REAL
     */
    public void testGpmFieldToUiReal() {
        final GpmMultiple lGpmMultiple =
                new GpmMultiple(new ArrayList<AbstractGpmField<?>>());
        final UiMultipleField lUiMultiple = new UiMultipleField();

        GpmTextBox<Double> lGpmTextBox =
                new GpmTextBox<Double>(GpmDoubleFormatter.getInstance());
        addFieldToUiAndGpmMultiple(lGpmMultiple, lUiMultiple, lGpmTextBox,
                new UiStringField());
        lGpmTextBox.set(DEFAULT_TEST_REAL_VALUE);

        executeTestCopyMultipleFieldHMIToApp(lUiMultiple, lGpmMultiple);
    }

    /**
     * Test with no value.
     */
    public void testEmpty() {
        // test empty fields from HMI to App.
        executeTestCopyMultipleFieldHMIToApp(new UiMultipleField(),
                new GpmMultiple(new ArrayList<AbstractGpmField<?>>()));

        // test empty fields from App to HMI.
        executeTestCopyMultipleFieldAppToHMI(new UiMultipleField(),
                new GpmMultiple(new ArrayList<AbstractGpmField<?>>()));
    }

    /**
     * Test data transfer GpmField > UiStringField. <br/>
     * value = null.
     */
    public void testNullStringAppToHMI() {
        final UiMultipleField lUiMultiple = new UiMultipleField();
        final GpmMultiple lMultiple =
                new GpmMultiple(new ArrayList<AbstractGpmField<?>>());

        executeTestCopyMultipleFieldAppToHMI(lUiMultiple, lMultiple);
    }
}