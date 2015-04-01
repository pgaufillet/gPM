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

import static org.topcased.gpm.ui.application.client.GpmTestsConstants.DEFAULT_OTHER_TEST_DATE;
import static org.topcased.gpm.ui.application.client.GpmTestsConstants.DEFAULT_TEST_DATE;

import java.util.Date;

import org.topcased.gpm.business.util.DateDisplayHintType;
import org.topcased.gpm.ui.application.client.GpmTestsConstants;
import org.topcased.gpm.ui.component.client.container.field.GpmDateTimeBox;
import org.topcased.gpm.ui.facade.shared.container.field.simple.UiDateField;
import org.topcased.gpm.ui.facade.shared.container.field.simple.UiSimpleField;

import com.google.gwt.i18n.client.DateTimeFormat;

/**
 * GwtTestGpmDateTimeBox : Unit tests on data transfer around GpmDateTimeBox
 * field.
 * 
 * @author frosier
 */
public class GwtTestGpmDateTimeBox extends AbstractGpmTestCase {

    public String getModuleName() {
        return GpmTestsConstants.MODULE_NAME;
    }

    /**
     * Test data transfer UiField > GpmField. <br/>
     * No DateTimeFormat => the default format (MediumDateFormat).
     */
    public void testUiFieldToGpmDateTimeBoxDefault() {
        final GpmDateTimeBox lDateBox = new GpmDateTimeBox();
        final UiDateField lUiDateField = new UiDateField();

        // equals tests are not executed

        // Test with current date.
        lUiDateField.set(DEFAULT_TEST_DATE);

        executeTestCopyDateFieldAppToHMI(lUiDateField, lDateBox);

        // Test with different values
        final UiSimpleField<Date> lOtherDateField = new UiDateField();
        lOtherDateField.set(DEFAULT_OTHER_TEST_DATE);

        assertNotHasSameValues("Check that values are different",
                lOtherDateField, lDateBox);
    }

    /**
     * Test data transfer UiField > GpmField with null value. <br/>
     * No DateTimeFormat => the default format (MediumDateFormat).
     */
    public void testUiFieldToGpmDateTimeBoxNullValue() {
        final GpmDateTimeBox lDateBox = new GpmDateTimeBox();
        final UiSimpleField<Date> lUiDateField = new UiDateField();

        lUiDateField.set(null);

        executeTestCopySimpleFieldAppToHMI(lUiDateField, lDateBox, false);
    }

    /**
     * Test data transfer GpmField > UiField. <br/>
     * No DateTimeFormat => the default format (MediumDateFormat).
     */
    public void testGpmDateTimeBoxToUiFieldDefault() {
        final GpmDateTimeBox lDateBox = new GpmDateTimeBox();
        final UiSimpleField<Date> lUiDateField = new UiDateField();

        // equals tests are not executed

        lDateBox.set(DEFAULT_TEST_DATE);
        executeTestCopySimpleFieldHMIToApp(lUiDateField, lDateBox, false);

        // Test with different values
        final GpmDateTimeBox lOtherDateTimeBox = new GpmDateTimeBox();
        lOtherDateTimeBox.set(DEFAULT_OTHER_TEST_DATE);

        assertNotHasSameValues("Check that values are different",
                lOtherDateTimeBox, lUiDateField);
    }

    /**
     * Test data transfer GpmField > UiField with null value. <br/>
     * No DateTimeFormat => the default format (MediumDateFormat).
     */
    public void testGpmDateTimeBoxToUiFieldNullValue() {
        final GpmDateTimeBox lDateBox = new GpmDateTimeBox();
        final UiSimpleField<Date> lUiDateField = new UiDateField();

        // Test with null value.
        lDateBox.set(null);

        executeTestCopySimpleFieldHMIToApp(lUiDateField, lDateBox, false);
    }

    //======= Test with all formatters GpmField >> UIDateField =======//

    /**
     * Test data transfer GpmField > UiField. <br/>
     * DateTimeFormat = ShortDateFormat.
     */
    public void testGpmDateTimeBoxToUiFieldShortDateFormat() {
        final GpmDateTimeBox lDateBox = new GpmDateTimeBox();
        final UiSimpleField<Date> lUiDateField = new UiDateField();

        lDateBox.set(DEFAULT_TEST_DATE);
        lDateBox.setFormat(DateTimeFormat.getShortDateFormat());

        executeTestCopySimpleFieldHMIToApp(lUiDateField, lDateBox, false);
    }

    /**
     * Test data transfer GpmField > UiField. <br/>
     * DateTimeFormat = MediumDateFormat.
     */
    public void testGpmDateTimeBoxToUiFieldMediumDateFormat() {
        final GpmDateTimeBox lDateBox = new GpmDateTimeBox();
        final UiSimpleField<Date> lUiDateField = new UiDateField();

        lDateBox.set(DEFAULT_TEST_DATE);
        lDateBox.setFormat(DateTimeFormat.getMediumDateFormat());

        executeTestCopySimpleFieldHMIToApp(lUiDateField, lDateBox, false);
    }

    /**
     * Test data transfer GpmField > UiField. <br/>
     * DateTimeFormat = LongDateFormat.
     */
    public void testGpmDateTimeBoxToUiFieldLongDateFormat() {
        final GpmDateTimeBox lDateBox = new GpmDateTimeBox();
        final UiSimpleField<Date> lUiDateField = new UiDateField();

        lDateBox.set(DEFAULT_TEST_DATE);
        lDateBox.setFormat(DateTimeFormat.getLongDateFormat());

        executeTestCopySimpleFieldHMIToApp(lUiDateField, lDateBox, false);
    }

    /**
     * Test data transfer GpmField > UiField. <br/>
     * DateTimeFormat = FullDateFormat.
     */
    public void testGpmDateTimeBoxToUiFieldFullDateFormat() {
        final GpmDateTimeBox lDateBox = new GpmDateTimeBox();
        final UiSimpleField<Date> lUiDateField = new UiDateField();

        lDateBox.set(DEFAULT_TEST_DATE);
        lDateBox.setFormat(DateTimeFormat.getFullDateFormat());

        executeTestCopySimpleFieldHMIToApp(lUiDateField, lDateBox, false);
    }

    // Date/Time formats.

    /**
     * Test data transfer GpmField > UiField. <br/>
     * DateTimeFormat = ShortDateTimeFormat.
     */
    public void testGpmDateTimeBoxToUiFieldShortDateTimeFormat() {
        final GpmDateTimeBox lDateBox = new GpmDateTimeBox();
        final UiSimpleField<Date> lUiDateField = new UiDateField();

        lDateBox.set(DEFAULT_TEST_DATE);
        lDateBox.setFormat(DateTimeFormat.getShortDateTimeFormat());

        executeTestCopySimpleFieldHMIToApp(lUiDateField, lDateBox, false);
    }

    /**
     * Test data transfer GpmField > UiField. <br/>
     * DateTimeFormat = MediumDateTimeFormat.
     */
    public void testGpmDateTimeBoxToUiFieldMediumDateTimeFormat() {
        final GpmDateTimeBox lDateBox = new GpmDateTimeBox();
        final UiSimpleField<Date> lUiDateField = new UiDateField();

        lDateBox.set(DEFAULT_TEST_DATE);
        lDateBox.setFormat(DateTimeFormat.getMediumDateTimeFormat());

        executeTestCopySimpleFieldHMIToApp(lUiDateField, lDateBox, false);
    }

    /**
     * Test data transfer GpmField > UiField. <br/>
     * DateTimeFormat = LongDateTimeFormat.
     */
    public void testGpmDateTimeBoxToUiFieldLongDateTimeFormat() {
        final GpmDateTimeBox lDateBox = new GpmDateTimeBox();
        final UiSimpleField<Date> lUiDateField = new UiDateField();

        lDateBox.set(DEFAULT_TEST_DATE);
        lDateBox.setFormat(DateTimeFormat.getLongDateTimeFormat());

        executeTestCopySimpleFieldHMIToApp(lUiDateField, lDateBox, false);
    }

    /**
     * Test data transfer GpmField > UiField. <br/>
     * DateTimeFormat = FullDateTimeFormat.
     */
    public void testGpmDateTimeBoxToUiFieldFullDateTimeFormat() {
        final GpmDateTimeBox lDateBox = new GpmDateTimeBox();
        final UiSimpleField<Date> lUiDateField = new UiDateField();

        lDateBox.set(DEFAULT_TEST_DATE);
        lDateBox.setFormat(DateTimeFormat.getFullDateTimeFormat());

        executeTestCopySimpleFieldHMIToApp(lUiDateField, lDateBox, false);
    }

    //======= Test with all formatters UIDateField >> GpmField ========//

    /**
     * Test data transfer UiField > GpmField. <br/>
     * DateTimeFormat = ShortDateFormat.
     */
    public void testUiFieldToGpmDateTimeBoxShortDateFormat() {
        final GpmDateTimeBox lDateBox = new GpmDateTimeBox();
        final UiDateField lUiDateField = new UiDateField();

        lUiDateField.set(DEFAULT_TEST_DATE);
        lUiDateField.setDateDisplayHintType(DateDisplayHintType.DATE_SHORT);

        executeTestCopyDateFieldAppToHMI(lUiDateField, lDateBox);
    }

    /**
     * Test data transfer UiField > GpmField. <br/>
     * DateTimeFormat = MediumDateFormat.
     */
    public void testUiFieldToGpmDateTimeBoxMediumDateFormat() {
        final GpmDateTimeBox lDateBox = new GpmDateTimeBox();
        final UiDateField lUiDateField = new UiDateField();

        lUiDateField.set(DEFAULT_TEST_DATE);
        lUiDateField.setDateDisplayHintType(DateDisplayHintType.DATE_MEDIUM);

        executeTestCopyDateFieldAppToHMI(lUiDateField, lDateBox);
    }

    /**
     * Test data transfer UiField > GpmField. <br/>
     * DateTimeFormat = LongDateFormat.
     */
    public void testUiFieldToGpmDateTimeBoxLongDateFormat() {
        final GpmDateTimeBox lDateBox = new GpmDateTimeBox();
        final UiDateField lUiDateField = new UiDateField();

        lUiDateField.set(DEFAULT_TEST_DATE);
        lUiDateField.setDateDisplayHintType(DateDisplayHintType.DATE_LONG);

        executeTestCopyDateFieldAppToHMI(lUiDateField, lDateBox);
    }

    /**
     * Test data transfer UiField > GpmField. <br/>
     * DateTimeFormat = FullDateFormat.
     */
    public void testUiFieldToGpmDateTimeBoxFullDateFormat() {
        final GpmDateTimeBox lDateBox = new GpmDateTimeBox();
        final UiDateField lUiDateField = new UiDateField();

        lUiDateField.set(DEFAULT_TEST_DATE);
        lUiDateField.setDateDisplayHintType(DateDisplayHintType.DATE_FULL);

        executeTestCopyDateFieldAppToHMI(lUiDateField, lDateBox);
    }

    // Date/Time formats.

    /**
     * Test data transfer UiField > GpmField. <br/>
     * DateTimeFormat = ShortDateTimeFormat.
     */
    public void testUiFieldToGpmDateTimeBoxShortDateTimeFormat() {
        final GpmDateTimeBox lDateBox = new GpmDateTimeBox();
        final UiDateField lUiDateField = new UiDateField();

        lUiDateField.set(DEFAULT_TEST_DATE);
        lUiDateField.setDateDisplayHintType(DateDisplayHintType.DATE_TIME_SHORT);

        executeTestCopyDateFieldAppToHMI(lUiDateField, lDateBox);
    }

    /**
     * Test data transfer UiField > GpmField. <br/>
     * DateTimeFormat = MediumDateTimeFormat.
     */
    public void testUiFieldToGpmDateTimeBoxMediumDateTimeFormat() {
        final GpmDateTimeBox lDateBox = new GpmDateTimeBox();
        final UiDateField lUiDateField = new UiDateField();

        lUiDateField.set(DEFAULT_TEST_DATE);
        lUiDateField.setDateDisplayHintType(DateDisplayHintType.DATE_TIME_MEDIUM);

        executeTestCopyDateFieldAppToHMI(lUiDateField, lDateBox);
    }

    /**
     * Test data transfer UiField > GpmField. <br/>
     * DateTimeFormat = LongDateTimeFormat.
     */
    public void testUiFieldToGpmDateTimeBoxLongDateTimeFormat() {
        final GpmDateTimeBox lDateBox = new GpmDateTimeBox();
        final UiDateField lUiDateField = new UiDateField();

        lUiDateField.set(DEFAULT_TEST_DATE);
        lUiDateField.setDateDisplayHintType(DateDisplayHintType.DATE_TIME_LONG);

        executeTestCopyDateFieldAppToHMI(lUiDateField, lDateBox);
    }

    /**
     * Test data transfer UiField > GpmField. <br/>
     * DateTimeFormat = FullDateTimeFormat.
     */
    public void testUiFieldToGpmDateTimeBoxFullDateTimeFormat() {
        final GpmDateTimeBox lDateBox = new GpmDateTimeBox();
        final UiDateField lUiDateField = new UiDateField();

        lUiDateField.set(DEFAULT_TEST_DATE);
        lUiDateField.setDateDisplayHintType(DateDisplayHintType.DATE_TIME_FULL);

        executeTestCopyDateFieldAppToHMI(lUiDateField, lDateBox);
    }
}