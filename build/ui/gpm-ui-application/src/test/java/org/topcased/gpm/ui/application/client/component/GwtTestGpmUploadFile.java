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

import static org.topcased.gpm.ui.application.client.GpmTestsConstants.DEFAULT_OTHER_TEST_STRING_VALUE;

import org.topcased.gpm.business.values.field.simple.BusinessAttachedField;
import org.topcased.gpm.ui.application.client.GpmTestsConstants;
import org.topcased.gpm.ui.component.client.container.field.GpmUploadFile;
import org.topcased.gpm.ui.component.client.container.field.GpmUploadFileRegister;
import org.topcased.gpm.ui.component.client.util.GpmAnchorTarget;
import org.topcased.gpm.ui.component.client.util.GpmUrlBuilder;
import org.topcased.gpm.ui.facade.shared.container.field.simple.UiAttachedField;

/**
 * Unit tests on gPM UploadFile component.
 * 
 * @author jeballar
 */
public class GwtTestGpmUploadFile extends AbstractGpmTestCase {

    private static final String DEFAULT_TEST_STRING_VALUE =
            GpmTestsConstants.DEFAULT_TEST_STRING_VALUE;

    private static final GpmUploadFileRegister UPLOADER_REGISTER =
            new GpmUploadFileRegister();

    public String getModuleName() {
        return GpmTestsConstants.MODULE_NAME;
    }

    /**
     * Executes the copy of a gPM field Value to a coherent UI attached field
     * (app hmi entry).
     * <ul>
     * <li>Check if values are the same after copy (equals execution).</li>
     * <li>Check if values are the same after copy by hasSameValues execution of
     * the HMI component object.</li>
     * </ul>
     * 
     * @param pUiAttachedField
     *            A typed instance of the Ui simple field to be copied.
     * @param pGpmField
     *            A typed instance of the gPM field that receive data from core.
     */
    private static void executeTestCopyUploadFieldHMIToApp(
            final UiAttachedField pUiAttachedField,
            final BusinessAttachedField pGpmField) {
        // Fail when at least one field is null
        failWhenNullParameter(pUiAttachedField, pGpmField);

        // Test copy.
        pUiAttachedField.copy(pGpmField);

        // Test hasSameValues.
        assertHasSameValues("Check that hasSameValues is true",
                pUiAttachedField, pGpmField);
    }

    /**
     * Executes the copy of a UI attached field to a gPM field Value (hmi app
     * entry).
     * <ul>
     * <li>Check if values are the same after copy (equals execution).</li>
     * <li>Check if values are the same after copy by hasSameValues execution of
     * the HMI component object.</li>
     * </ul>
     * 
     * @param pUiAttachedField
     *            A typed instance of the Ui simple field to be copied.
     * @param pGpmField
     *            A typed instance of the gPM field that receive data from core.
     */
    private static void executeTestCopyUploadFieldAppToHMI(
            final UiAttachedField pUiAttachedField,
            final BusinessAttachedField pGpmField) {
        // Fail when at least one field is null
        failWhenNullParameter(pUiAttachedField, pGpmField);

        // Test copy.
        pGpmField.copy(pUiAttachedField);

        // Test hasSameValues.
        assertHasSameValues("Check that hasSameValues is true",
                pUiAttachedField, pGpmField);
    }

    /**
     * Test data transfer UiStringField > GpmField. <br/>
     * value = DEFAULT_TEST_STR_VALUE.
     */
    public void testUiStringToGpmField() {
        final GpmUploadFile lUploadFile =
                new GpmUploadFile(UPLOADER_REGISTER, new GpmUrlBuilder(""),
                        GpmAnchorTarget.SELF);
        final UiAttachedField lUiField = new UiAttachedField();
        lUiField.setId(DEFAULT_TEST_STRING_VALUE);
        lUiField.setFileName(DEFAULT_OTHER_TEST_STRING_VALUE);

        executeTestCopyUploadFieldHMIToApp(lUiField, lUploadFile);

        // Test with different values
        lUiField.setId(DEFAULT_OTHER_TEST_STRING_VALUE);
        assertNotHasSameValues("Check that values are different", lUiField,
                lUploadFile);
    }

    /**
     * Test data transfer GpmField > UiStringField. <br/>
     * value = DEFAULT_TEST_STR_VALUE.
     */
    public void testGpmFieldToUiString() {
        final GpmUploadFile lUploadFile =
                new GpmUploadFile(UPLOADER_REGISTER, new GpmUrlBuilder(""),
                        GpmAnchorTarget.SELF);
        final UiAttachedField lUiField = new UiAttachedField();
        lUploadFile.setId(DEFAULT_TEST_STRING_VALUE);
        lUploadFile.setFileName(DEFAULT_OTHER_TEST_STRING_VALUE);

        executeTestCopyUploadFieldHMIToApp(lUiField, lUploadFile);

        // Test with different values
        lUploadFile.setId(DEFAULT_OTHER_TEST_STRING_VALUE);
        assertNotHasSameValues("Check that values are different", lUploadFile,
                lUiField);
    }

    /**
     * Test with no value.
     */
    public void testEmpty() {
        // test empty fields from HMI to App.
        executeTestCopyUploadFieldHMIToApp(new UiAttachedField(),
                new GpmUploadFile(UPLOADER_REGISTER, new GpmUrlBuilder(""),
                        GpmAnchorTarget.SELF));

        // test empty fields from App to HMI.
        executeTestCopyUploadFieldAppToHMI(new UiAttachedField(),
                new GpmUploadFile(UPLOADER_REGISTER, new GpmUrlBuilder(""),
                        GpmAnchorTarget.SELF));
    }
}