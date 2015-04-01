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
import org.topcased.gpm.ui.component.client.container.field.GpmDateBox;
import org.topcased.gpm.ui.component.client.util.GpmDateTimeHelper;
import org.topcased.gpm.ui.facade.shared.container.field.simple.UiDateField;
import org.topcased.gpm.ui.facade.shared.container.field.simple.UiSimpleField;

import com.google.gwt.i18n.client.DateTimeFormat;

/**
 * GwtTestGpmDateBox : Unit tests on data transfer around GpmDateBox field.
 * 
 * @author frosier
 */
public class GwtTestGpmDateBox extends AbstractGpmTestCase {

    public String getModuleName() {
        return GpmTestsConstants.MODULE_NAME;
    }

    /**
     * Test data transfer UiField > GpmField. <br/>
     * No DateTimeFormat => the default format (MediumDateFormat).
     */
    public void testUiFieldToGpmDateBoxDefault() {
        final GpmDateBox lDateBox = new GpmDateBox();
        final UiDateField lUiDateField = new UiDateField();

        // Test with current date. Uses a private method to handle specific GpmDateBox day accuracy
        lUiDateField.set(DEFAULT_TEST_DATE);
        executeTestCopyDateFieldAppToHMI(lUiDateField, lDateBox);

        // Now, trim the value to a "day" value
        lUiDateField.set(GpmDateTimeHelper.getDailyAccuracy(DEFAULT_TEST_DATE));
        executeTestCopySimpleFieldAppToHMI(lUiDateField, lDateBox, false);

        // Check with different values
        UiSimpleField<Date> lOtherDateField = new UiDateField();
        lOtherDateField.set(DEFAULT_OTHER_TEST_DATE);

        assertNotHasSameValues("Check that date are different",
                lOtherDateField, lDateBox);
    }

    /**
     * Test data transfer UiField > GpmField with null value. <br/>
     * No DateTimeFormat => the default format (MediumDateFormat).
     */
    public void testUiFieldToGpmDateBoxNullValue() {
        final GpmDateBox lDateBox = new GpmDateBox();
        final UiSimpleField<Date> lUiDateField = new UiDateField();

        // Test with null value.
        lUiDateField.set(null);
        executeTestCopySimpleFieldAppToHMI(lUiDateField, lDateBox, false);
    }

    /**
     * Test data transfer GpmField > UiField. <br/>
     * No DateTimeFormat => the default format (MediumDateFormat).
     */
    public void testGpmDateBoxToUiFieldDefault() {
        final GpmDateBox lDateBox = new GpmDateBox();
        final UiSimpleField<Date> lUiDateField = new UiDateField();

        // equals tests are not executed
        lDateBox.set(DEFAULT_TEST_DATE);
        executeTestCopySimpleFieldHMIToApp(lUiDateField, lDateBox, false);

        // Check with different values

        GpmDateBox lOtherDateBox = new GpmDateBox();
        Date lOtherDate = new Date();
        lOtherDate.setTime(0);
        lOtherDateBox.set(lOtherDate);

        assertNotHasSameValues("Check that date are different", lDateBox,
                lOtherDateBox);
    }

    /**
     * Test data transfer GpmField > UiField with null value. <br/>
     * No DateTimeFormat => the default format (MediumDateFormat).
     */
    public void testGpmDateBoxToUiFieldNullValue() {
        final GpmDateBox lDateBox = new GpmDateBox();
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
    public void testGpmDateBoxToUiFieldShortDateFormat() {
        final GpmDateBox lDateBox = new GpmDateBox();
        final UiSimpleField<Date> lUiDateField = new UiDateField();

        lDateBox.set(DEFAULT_TEST_DATE);
        lDateBox.setFormat(DateTimeFormat.getShortDateFormat());

        executeTestCopySimpleFieldHMIToApp(lUiDateField, lDateBox, false);
    }

    /**
     * Test data transfer GpmField > UiField. <br/>
     * DateTimeFormat = MediumDateFormat.
     */
    public void testGpmDateBoxToUiFieldMediumDateFormat() {
        final GpmDateBox lDateBox = new GpmDateBox();
        final UiSimpleField<Date> lUiDateField = new UiDateField();

        lDateBox.set(DEFAULT_TEST_DATE);
        lDateBox.setFormat(DateTimeFormat.getMediumDateFormat());

        executeTestCopySimpleFieldHMIToApp(lUiDateField, lDateBox, false);
    }

    /**
     * Test data transfer GpmField > UiField. <br/>
     * DateTimeFormat = LongDateFormat.
     */
    public void testGpmDateBoxToUiFieldLongDateFormat() {
        final GpmDateBox lDateBox = new GpmDateBox();
        final UiSimpleField<Date> lUiDateField = new UiDateField();

        lDateBox.set(DEFAULT_TEST_DATE);
        lDateBox.setFormat(DateTimeFormat.getLongDateFormat());

        executeTestCopySimpleFieldHMIToApp(lUiDateField, lDateBox, false);
    }

    /**
     * Test data transfer GpmField > UiField. <br/>
     * DateTimeFormat = FullDateFormat.
     */
    public void testGpmDateBoxToUiFieldFullDateFormat() {
        final GpmDateBox lDateBox = new GpmDateBox();
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
    public void testGpmDateBoxToUiFieldShortDateTimeFormat() {
        final GpmDateBox lDateBox = new GpmDateBox();
        final UiSimpleField<Date> lUiDateField = new UiDateField();

        lDateBox.set(DEFAULT_TEST_DATE);
        lDateBox.setFormat(DateTimeFormat.getShortDateTimeFormat());

        executeTestCopySimpleFieldHMIToApp(lUiDateField, lDateBox, false);
    }

    /**
     * Test data transfer GpmField > UiField. <br/>
     * DateTimeFormat = MediumDateTimeFormat.
     */
    public void testGpmDateBoxToUiFieldMediumDateTimeFormat() {
        final GpmDateBox lDateBox = new GpmDateBox();
        final UiSimpleField<Date> lUiDateField = new UiDateField();

        lDateBox.set(DEFAULT_TEST_DATE);
        lDateBox.setFormat(DateTimeFormat.getMediumDateTimeFormat());

        executeTestCopySimpleFieldHMIToApp(lUiDateField, lDateBox, false);
    }

    /**
     * Test data transfer GpmField > UiField. <br/>
     * DateTimeFormat = LongDateTimeFormat.
     */
    public void testGpmDateBoxToUiFieldLongDateTimeFormat() {
        final GpmDateBox lDateBox = new GpmDateBox();
        final UiSimpleField<Date> lUiDateField = new UiDateField();

        lDateBox.set(DEFAULT_TEST_DATE);
        lDateBox.setFormat(DateTimeFormat.getLongDateTimeFormat());

        executeTestCopySimpleFieldHMIToApp(lUiDateField, lDateBox, false);
    }

    /**
     * Test data transfer GpmField > UiField. <br/>
     * DateTimeFormat = FullDateTimeFormat.
     */
    public void testGpmDateBoxToUiFieldFullDateTimeFormat() {
        final GpmDateBox lDateBox = new GpmDateBox();
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
    public void testUiFieldToGpmDateBoxShortDateFormat() {
        final GpmDateBox lDateBox = new GpmDateBox();
        final UiDateField lUiDateField = new UiDateField();

        lUiDateField.set(DEFAULT_TEST_DATE);
        lUiDateField.setDateDisplayHintType(DateDisplayHintType.DATE_SHORT);

        executeTestCopyDateFieldAppToHMI(lUiDateField, lDateBox);
    }

    /**
     * Test data transfer UiField > GpmField. <br/>
     * DateTimeFormat = MediumDateFormat.
     */
    public void testUiFieldToGpmDateBoxMediumDateFormat() {
        final GpmDateBox lDateBox = new GpmDateBox();
        final UiDateField lUiDateField = new UiDateField();

        lUiDateField.set(DEFAULT_TEST_DATE);
        lUiDateField.setDateDisplayHintType(DateDisplayHintType.DATE_MEDIUM);

        executeTestCopyDateFieldAppToHMI(lUiDateField, lDateBox);
    }

    /**
     * Test data transfer UiField > GpmField. <br/>
     * DateTimeFormat = LongDateFormat.
     */
    public void testUiFieldToGpmDateBoxLongDateFormat() {
        final GpmDateBox lDateBox = new GpmDateBox();
        final UiDateField lUiDateField = new UiDateField();

        lUiDateField.set(DEFAULT_TEST_DATE);
        lUiDateField.setDateDisplayHintType(DateDisplayHintType.DATE_LONG);

        executeTestCopyDateFieldAppToHMI(lUiDateField, lDateBox);
    }

    /**
     * Test data transfer UiField > GpmField. <br/>
     * DateTimeFormat = FullDateFormat.
     */
    public void testUiFieldToGpmDateBoxFullDateFormat() {
        final GpmDateBox lDateBox = new GpmDateBox();
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
    public void testUiFieldToGpmDateBoxShortDateTimeFormat() {
        final GpmDateBox lDateBox = new GpmDateBox();
        final UiDateField lUiDateField = new UiDateField();

        lUiDateField.set(DEFAULT_TEST_DATE);
        lUiDateField.setDateDisplayHintType(DateDisplayHintType.DATE_TIME_SHORT);

        executeTestCopyDateFieldAppToHMI(lUiDateField, lDateBox);
    }

    /**
     * Test data transfer UiField > GpmField. <br/>
     * DateTimeFormat = MediumDateTimeFormat.
     */
    public void testUiFieldToGpmDateBoxMediumDateTimeFormat() {
        final GpmDateBox lDateBox = new GpmDateBox();
        final UiDateField lUiDateField = new UiDateField();

        lUiDateField.set(DEFAULT_TEST_DATE);
        lUiDateField.setDateDisplayHintType(DateDisplayHintType.DATE_TIME_MEDIUM);

        executeTestCopyDateFieldAppToHMI(lUiDateField, lDateBox);
    }

    /**
     * Test data transfer UiField > GpmField. <br/>
     * DateTimeFormat = LongDateTimeFormat.
     */
    public void testUiFieldToGpmDateBoxLongDateTimeFormat() {
        final GpmDateBox lDateBox = new GpmDateBox();
        final UiDateField lUiDateField = new UiDateField();

        lUiDateField.set(DEFAULT_TEST_DATE);
        lUiDateField.setDateDisplayHintType(DateDisplayHintType.DATE_TIME_LONG);

        executeTestCopyDateFieldAppToHMI(lUiDateField, lDateBox);
    }

    /**
     * Test data transfer UiField > GpmField. <br/>
     * DateTimeFormat = FullDateTimeFormat.
     */
    public void testUiFieldToGpmDateBoxFullDateTimeFormat() {
        final GpmDateBox lDateBox = new GpmDateBox();
        final UiDateField lUiDateField = new UiDateField();

        lUiDateField.set(DEFAULT_TEST_DATE);
        lUiDateField.setDateDisplayHintType(DateDisplayHintType.DATE_TIME_FULL);

        executeTestCopyDateFieldAppToHMI(lUiDateField, lDateBox);
    }
}