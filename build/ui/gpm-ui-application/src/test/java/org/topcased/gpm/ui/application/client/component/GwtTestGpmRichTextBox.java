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

import static org.topcased.gpm.ui.application.client.GpmTestsConstants.DEFAULT_OTHER_TEST_STRING_VALUE;

import org.topcased.gpm.ui.application.client.GpmTestsConstants;
import org.topcased.gpm.ui.component.client.container.field.GpmRichTextBox;
import org.topcased.gpm.ui.facade.shared.container.field.simple.UiSimpleField;
import org.topcased.gpm.ui.facade.shared.container.field.simple.UiStringField;

import com.google.gwt.core.client.GWT;

/**
 * GwtTestGpmRichTextBox : unit tests on gPM richText box component.
 * 
 * @author frosier
 */
public class GwtTestGpmRichTextBox extends AbstractGpmTestCase {

    private static final String DEFAULT_TEST_VALUE =
            GpmTestsConstants.DEFAULT_TEST_STRING_VALUE;

    public String getModuleName() {
        return GpmTestsConstants.MODULE_NAME;
    }

    /**
     * Not effective test, to see access transformation on RichTextBox.
     */
    public void testWidgetSetAndGetAccess() {
        final GpmRichTextBox lRichTextBox = new GpmRichTextBox();
        final String lTestString = "<br>";

        GWT.log("Before set html : " + lTestString);
        lRichTextBox.set(lTestString);
        GWT.log("After set html : " + lRichTextBox.get());
    }

    /**
     * Test data transfer UiField > GpmField. <br/>
     * Type of the simple field = String. <br/>
     * Special case because of the text formatting of the RichTextBox
     */
    public void testUiStringToGpmField() {
        final GpmRichTextBox lRichTextBox = new GpmRichTextBox();
        final UiSimpleField<String> lUiStringField = new UiStringField();

        lUiStringField.set(DEFAULT_TEST_VALUE);

        lRichTextBox.copy(lUiStringField);
        assertNotHasSameValues(
                "Check that values are different for RichTextBox which formats"
                        + " its content to HTML", lUiStringField, lRichTextBox);

        // Test with formatted HTML content
        String lHTMLFormatted = lRichTextBox.getAsString();
        lUiStringField.set(lHTMLFormatted);

        executeTestCopySimpleFieldAppToHMI(lUiStringField, lRichTextBox);
    }

    /**
     * Test data transfer GpmField > UiField <br/>
     * Type of the simple field = String. <br/>
     */
    public void testGpmFieldToUiString() {
        final GpmRichTextBox lRichTextBox = new GpmRichTextBox();
        final UiSimpleField<String> lUiStringField = new UiStringField();

        lRichTextBox.set(DEFAULT_TEST_VALUE);

        executeTestCopySimpleFieldHMIToApp(lUiStringField, lRichTextBox);

        // Test with different values
        lRichTextBox.set(DEFAULT_OTHER_TEST_STRING_VALUE);
        assertNotHasSameValues("Check that values are different", lRichTextBox,
                lUiStringField);
    }

    /**
     * Test with no value.
     */
    public void testEmpty() {
        executeTestCopySimpleFieldHMIToApp(new UiStringField(),
                new GpmRichTextBox());
        executeTestCopySimpleFieldAppToHMI(new UiStringField(),
                new GpmRichTextBox(), false);
    }
}