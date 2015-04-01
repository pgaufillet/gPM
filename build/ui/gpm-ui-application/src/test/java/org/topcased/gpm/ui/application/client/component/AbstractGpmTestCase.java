/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien Eballard (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.component;

import static org.topcased.gpm.ui.application.client.GpmTestsConstants.DEFAULT_OTHER_TEST_DATE;

import java.io.Serializable;

import org.topcased.gpm.business.values.field.BusinessField;
import org.topcased.gpm.business.values.field.multivalued.BusinessMultivaluedField;
import org.topcased.gpm.business.values.field.simple.BusinessChoiceField;
import org.topcased.gpm.business.values.field.simple.BusinessDateField;
import org.topcased.gpm.business.values.field.simple.BusinessSimpleField;
import org.topcased.gpm.ui.component.client.container.field.AbstractGpmField;
import org.topcased.gpm.ui.component.client.container.field.GpmMultiple;
import org.topcased.gpm.ui.facade.shared.container.field.UiField;
import org.topcased.gpm.ui.facade.shared.container.field.multiple.UiMultipleField;
import org.topcased.gpm.ui.facade.shared.container.field.multivalued.UiMultivaluedField;
import org.topcased.gpm.ui.facade.shared.container.field.simple.UiChoiceField;
import org.topcased.gpm.ui.facade.shared.container.field.simple.UiDateField;
import org.topcased.gpm.ui.facade.shared.container.field.simple.UiSimpleField;

import com.google.gwt.core.client.GWT;
import com.google.gwt.junit.client.GWTTestCase;

/**
 * Regroups common methods for gPM Test cases
 * 
 * @author jeballar
 */
public abstract class AbstractGpmTestCase extends GWTTestCase {

    /** Stores the last generated field ID */
    private static int staticFieldID = 1;

    /**
     * Executes the copy of a UI choice field value to a coherent gPM field (Gwt
     * widget) on the HMI side.
     * <ul>
     * <li>Check if values are the same after copy (equals execution).</li>
     * <li>Check if values are the same after copy by hasSameValues execution of
     * the HMI component object.</li>
     * </ul>
     * 
     * @param <T>
     *            The type of the UI simple field.
     * @param pUiChoiceField
     *            A typed instance of an UI choice field that receives data from
     *            HMI.
     * @param pGpmField
     *            A typed instance of the HMI component (Business field facade
     *            is used here to access get methods) to be copied.
     * @param pTestEquals
     *            Execute the equals test if <code>true</code>.
     */
    public static <T extends Serializable> void executeTestCopyChoiceFieldAppToHMI(
            final UiChoiceField pUiChoiceField,
            final BusinessChoiceField pGpmField, final boolean pTestEquals) {
        // Fail when at least one field is null
        failWhenNullParameter(pUiChoiceField, pGpmField);

        // Test copy.
        pGpmField.copy(pUiChoiceField);

        // Test hasSameValues.
        assertHasSameValues("Check that hasSameValues is true", pGpmField,
                pUiChoiceField);

        // Test equals.
        if (pTestEquals) {
            assertEquals("Check that values by get() are equal",
                    pUiChoiceField.getCategoryValue(),
                    pGpmField.getCategoryValue());
        }
    }

    /**
     * Executes the copy of a gPM field Value to a coherent UI simple field
     * (application. HMI entry).
     * <ul>
     * <li>Check if values are the same after copy (equals execution).</li>
     * <li>Check if values are the same after copy by hasSameValues execution of
     * the HMI component object.</li>
     * </ul>
     * 
     * @param <T>
     *            The choice field type.
     * @param pUiChoiceField
     *            A typed instance of the Ui choice field to be copied.
     * @param pGpmField
     *            A typed instance of the gPM field that receive data from core.
     * @param pTestEquals
     *            Executes equals tests if <code>true</code>
     */
    public static <T extends Serializable> void executeTestCopyChoiceFieldHMIToApp(
            final UiChoiceField pUiChoiceField,
            final BusinessChoiceField pGpmField, final boolean pTestEquals) {
        // Fail when at least one field is null
        failWhenNullParameter(pUiChoiceField, pGpmField);

        // Test copy.
        pUiChoiceField.copy(pGpmField);

        // Test hasSameValues.
        assertHasSameValues("Check that hasSameValues is true", pUiChoiceField,
                pGpmField);

        // Test equals.
        if (pTestEquals) {
            assertEquals("Check that category value for the fields are equal",
                    pGpmField.getCategoryValue(),
                    pUiChoiceField.getCategoryValue());
        }
    }

    //***** Tests methods UISimpleField >> GpmField ******//

    /**
     * Equals tests are executed.
     * 
     * @param pUiSimpleField
     *            the UiField
     * @param pGpmField
     *            the GpmField
     * @param <T>
     *            type of the represented values
     * @see org.topcased.gpm.ui.client.GenericGpmFieldsTestExecutor#executeTestCopySimpleFieldAppToHMI(UiSimpleField,
     *      BusinessSimpleField, boolean)
     */
    public static <T extends Serializable> void executeTestCopySimpleFieldAppToHMI(
            final UiSimpleField<T> pUiSimpleField,
            final BusinessSimpleField<T> pGpmField) {
        executeTestCopySimpleFieldAppToHMI(pUiSimpleField, pGpmField,
                Boolean.TRUE);
    }

    /**
     * Executes the copy of a UI simple field Value to a coherent gPM field (Gwt
     * widget) on the HMI side.
     * <ul>
     * <li>Check if values are the same after copy (equals execution).</li>
     * <li>Check if values are the same after copy by hasSameValues execution of
     * the HMI component object.</li>
     * </ul>
     * 
     * @param <T>
     *            The type of the UI simple field.
     * @param pUiSimpleField
     *            A typed instance of an UI simple field that receives data from
     *            HMI.
     * @param pGpmField
     *            A typed instance of the HMI component (Business field facade
     *            is used here to access get methods) to be copied.
     * @param pTestEquals
     *            Execute the equals test if <code>true</code>.
     */
    public static <T extends Serializable> void executeTestCopySimpleFieldAppToHMI(
            final UiSimpleField<T> pUiSimpleField,
            final BusinessSimpleField<T> pGpmField, final boolean pTestEquals) {
        // Fail when at least one field is null
        failWhenNullParameter(pUiSimpleField, pGpmField);

        // Test copy.
        pGpmField.copy(pUiSimpleField);

        // Test hasSameValues.
        assertHasSameValues("Check that hasSameValues is true", pGpmField,
                pUiSimpleField);

        // Test equals.
        if (pTestEquals) {
            assertEquals("Check that values by get() are equal",
                    pUiSimpleField.get(), pGpmField.get());
        }
    }

    //***** Tests methods GpmField >> UISimpleField ******//

    /**
     * {@inheritDoc}
     * <p>
     * Equals tests are executed.
     * </p>
     * 
     * @see org.topcased.gpm.ui.client.GenericGpmFieldsTestExecutor#executeTestCopySimpleFieldHMIToApp(UiSimpleField,
     *      BusinessSimpleField, boolean)
     */
    public static <T extends Serializable> void executeTestCopySimpleFieldHMIToApp(
            final UiSimpleField<T> pUiSimpleField,
            final BusinessSimpleField<T> pGpmField) {
        executeTestCopySimpleFieldHMIToApp(pUiSimpleField, pGpmField,
                Boolean.TRUE);
    }

    /**
     * Executes the copy of a gPM field Value to a coherent UI simple field (app
     * hmi entry).
     * <ul>
     * <li>Check if values are the same after copy (equals execution).</li>
     * <li>Check if values are the same after copy by hasSameValues execution of
     * the HMI component object.</li>
     * </ul>
     * 
     * @param <T>
     *            The simple field type.
     * @param pUiSimpleField
     *            A typed instance of the Ui simple field to be copied.
     * @param pGpmField
     *            A typed instance of the gPM field that receive data from core.
     * @param pTestEquals
     *            Executes equals tests if <code>true</code>
     */
    public static <T extends Serializable> void executeTestCopySimpleFieldHMIToApp(
            final UiSimpleField<T> pUiSimpleField,
            final BusinessSimpleField<T> pGpmField, final boolean pTestEquals) {
        // Fail when at least one field is null
        failWhenNullParameter(pUiSimpleField, pGpmField);

        // Test copy.
        pUiSimpleField.copy(pGpmField);

        // Test hasSameValues.
        assertHasSameValues("Check that hasSameValues is true", pUiSimpleField,
                pGpmField);

        // Test equals.
        if (pTestEquals) {
            assertEquals("Check that values by get() are equal",
                    pGpmField.get(), pUiSimpleField.get());
        }
    }

    /**
     * Execute the corresponding copy and asserts, considering that GpmDateBox
     * handles only days accuracy whereas UiDateField handles minutes accuracy
     * Copy the UiField to the BusinessField
     * 
     * @param pUiDateField
     *            the date field to test
     * @param pDateBox
     *            the date box to test
     */
    public static void executeTestCopyDateFieldAppToHMI(
            final UiDateField pUiDateField, final BusinessDateField pDateBox) {
        pDateBox.copy(pUiDateField);
        // Values will be equals since GpmDateBox handles only days accuracy, and not
        // hours, minutes...
        assertTrue("Check hasSameValues between GpmDateBox (day accuracy) and "
                + "UiDateField (seconds accuracy)",
                pDateBox.hasSameValues(pUiDateField));
        // But UiDateField handles it and will say no to hasSameValues
        assertFalse("Check that values are different",
                pUiDateField.hasSameValues(pDateBox));

        // Since the method is private, test different values with other date is done here
        pUiDateField.set(DEFAULT_OTHER_TEST_DATE);
        assertFalse("Check that values are different",
                pUiDateField.hasSameValues(pDateBox));
        assertFalse("Check that values are different",
                pDateBox.hasSameValues(pUiDateField));
    }

    /**
     * Execute a copy of the UiMultiple into the GpmMultiple and compare them
     * using hasSameValues. Fails if hasSameValues returns <CODE>false</CODE>
     * 
     * @param pUiField
     *            The UiMultiple
     * @param pGpmMultivalued
     *            The GpmMultiple
     */
    public void executeTestCopyMultivaluedFieldAppToHMI(
            final UiMultivaluedField pUiField,
            final BusinessMultivaluedField<?> pGpmMultivalued) {
        // Fail when at least one field is null
        failWhenNullParameter(pUiField, pGpmMultivalued);

        // Test copy.
        pGpmMultivalued.copy(pUiField);

        // Test hasSameValues.
        assertHasSameValues("Check that hasSameValues is true",
                pGpmMultivalued, pUiField);
    }

    /**
     * Execute a copy of the GpmMultiple into the UiMultiple and compare them
     * using hasSameValues. Fails if hasSameValues returns <CODE>false</CODE>
     * 
     * @param pUiField
     *            The UiMultiple
     * @param pGpmMultivalued
     *            The GpmMultiple
     */
    public void executeTestCopyMultivaluedFieldHMIToApp(
            final UiMultivaluedField pUiField,
            final BusinessMultivaluedField<?> pGpmMultivalued) {
        // Fail when at least one field is null
        failWhenNullParameter(pUiField, pGpmMultivalued);

        // Test copy.
        pUiField.copy(pGpmMultivalued);

        // Test hasSameValues.
        assertHasSameValues("Check that hasSameValues is true",
                pGpmMultivalued, pUiField);
    }

    /**
     * Executes test for a not supported type of business field to copy.
     * 
     * @param <T>
     *            The simple field type.
     * @param pGpmField
     *            A typed instance of the gPM field that receive data from core.
     * @param pUiWrongField
     *            A typed instance of the Ui simple field to be copied, must be
     *            a not supported type.
     */
    public static <T extends Serializable> void executeTestWrongFieldType(
            final BusinessSimpleField<T> pGpmField, final UiField pUiWrongField) {
        try {
            pGpmField.copy(pUiWrongField);
            fail(pGpmField.getClass().getName()
                    + ".copy() should not be applicable for "
                    + pUiWrongField.getClass().getName());
        }
        catch (ClassCastException e) {
            // Good behavior.
            GWT.log("Test wrong field OK : " + pGpmField.getClass().getName()
                    + " is not supposed to copy "
                    + pUiWrongField.getFieldType().name());
        }
    }

    /**
     * Forces the test to fail when one of the parameters are null
     * 
     * @param pA
     *            first parameter
     * @param pB
     *            second parameter
     */
    public static void failWhenNullParameter(Object pA, Object pB) {
        if (pA == null || pB == null) {
            fail("Cannot perform test : At least one of test parameters was null ("
                    + pA + ", " + pB + ")");
        }
    }

    /**
     * Same method as assertEquals but using the hasSameValues method of
     * BusinessFields
     * 
     * @param pAssertMessage
     *            the text displayed when assert fails
     * @param pBaseField
     *            the base field on which the method is called
     * @param pComparedField
     *            the field given in parameter to the method
     */
    public static void assertHasSameValues(String pAssertMessage,
            BusinessField pBaseField, BusinessField pComparedField) {
        if (pBaseField.hasSameValues(pComparedField)
                && pComparedField.hasSameValues(pBaseField)) {
            return;
        }
        else {
            fail(pAssertMessage + ": expected \"" + pBaseField.getAsString()
                    + "\" but was \"" + pComparedField.getAsString() + "\"");
        }
    }

    /**
     * Same method as assertNotEquals but using the hasSameValues method of
     * BusinessFields
     * 
     * @param pAssertMessage
     *            the text displayed when hasSameValues returns true
     * @param pBaseField
     *            the base field on which the method is called
     * @param pComparedField
     *            the field given in parameter to the method
     */
    public static void assertNotHasSameValues(String pAssertMessage,
            BusinessField pBaseField, BusinessField pComparedField) {
        if (!pBaseField.hasSameValues(pComparedField)
                && !pComparedField.hasSameValues(pBaseField)) {
            return;
        }
        else {
            fail(pAssertMessage
                    + ": values were equals but were expected different : \""
                    + pBaseField.getAsString() + "\", \""
                    + pComparedField.getAsString() + "\"");
        }
    }

    /**
     * Generate a unique field ID
     * 
     * @return The unique field ID (to be used as fieldName)
     */
    public static String generateFieldUniqueID() {
        return "field" + staticFieldID++;
    }

    /**
     * Add the corresponding fields to HMI and App representations of the
     * multiple fields. Also sets a generated fieldName to the fields. The
     * method do not put values in fields.
     * 
     * @param pGpmMultiple
     *            the Gpm multiple field
     * @param pUiMultiple
     *            the UI multiple field
     * @param pGpmField
     *            the Gpm field to add
     * @param pUiField
     *            the UI field to add
     * @return the generated field ID
     */
    public String addFieldToUiAndGpmMultiple(GpmMultiple pGpmMultiple,
            UiMultipleField pUiMultiple, AbstractGpmField<?> pGpmField,
            UiField pUiField) {
        final String lFieldName = generateFieldUniqueID();
        pUiField.setFieldName(lFieldName);
        pGpmField.setFieldName(lFieldName);
        pGpmMultiple.getFields().add(pGpmField);
        pUiMultiple.addField(pUiField);
        return lFieldName;
    }
}
