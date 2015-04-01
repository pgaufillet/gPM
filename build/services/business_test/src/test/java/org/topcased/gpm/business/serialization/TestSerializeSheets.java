/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Sébastien René(Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.serialization;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.ServiceLocator;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.serialization.service.SerializationService;
import org.topcased.gpm.business.sheet.service.SheetSummaryData;

/**
 * TestSerializeSheets
 * 
 * @author srene
 */
public class TestSerializeSheets extends AbstractBusinessServiceTestCase {

    /** The serialization service */
    private final SerializationService serializationService =
            ServiceLocator.instance().getSerializationService();;

    private static final String SHEET_TYPE_NAME = GpmTestValues.SHEET_TYPE_DOG;

    /**
     * testNormalCase
     */
    public void testNormalCase() {
        List<SheetSummaryData> lSheets =
                sheetService.getSheetsByType(getProcessName(), SHEET_TYPE_NAME);
        List<String> lSheetIds = new ArrayList<String>(lSheets.size());
        for (SheetSummaryData lSheet : lSheets) {
            lSheetIds.add(lSheet.getId());
        }
        if (lSheetIds.size() == 0) {
            fail("No sheet found for the serialization");
        }

        OutputStream lTempOutputStream;
        try {
            lTempOutputStream = new ByteArrayOutputStream();
            serializationService.serializeSheets(adminRoleToken, lSheetIds,
                    lTempOutputStream);
            lTempOutputStream.close();
        }
        catch (FileNotFoundException ex) {
            fail("Error while creating the XML file");
        }
        catch (GDMException ex) {
            fail(ex.getMessage());
        }
        catch (IOException ex) {
            fail("Error while creating the XML file");
        }
    }

    /**
     * Tests the method with null sheet ids.
     */
    public void testWithNullSheetIdsCase() {
        OutputStream lTempOutputStream = new ByteArrayOutputStream();
        try {
            serializationService.serializeSheets(adminRoleToken, null,
                    lTempOutputStream);
            fail("The exception has not been thrown.");
        }
        catch (IllegalArgumentException lIllegalArgumentException) {
            // ok
        }
        catch (Throwable e) {
            fail("The exception thrown is not an IllegalArgumentException.");
        }
    }
}
