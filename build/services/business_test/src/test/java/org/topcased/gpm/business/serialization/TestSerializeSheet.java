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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.serialization.service.SerializationService;
import org.topcased.gpm.business.sheet.service.SheetService;
import org.topcased.gpm.business.sheet.service.SheetSummaryData;

/**
 * TestSerializeSheet
 * 
 * @author srene
 */
public class TestSerializeSheet extends AbstractBusinessServiceTestCase {

    /** The serialization service */
    private SerializationService serializationService;

    /** The sheet service */
    private SheetService sheetService;

    private static final String SHEET_TYPE_NAME = GpmTestValues.SHEET_TYPE_DOG;

    /**
     * testNormalCase
     */
    public void testNormalCase() {
        serializationService = serviceLocator.getSerializationService();
        sheetService = serviceLocator.getSheetService();

        List<SheetSummaryData> lSheets =
                sheetService.getSheetsByType(getProcessName(), SHEET_TYPE_NAME);
        List<String> lSheetIds = new ArrayList<String>(lSheets.size());
        for (SheetSummaryData lSheet : lSheets) {
            lSheetIds.add(lSheet.getId());
        }
        if (lSheetIds.size() == 0) {
            fail("No sheet found for the serialization");
        }
        File lTempFile = null;
        FileOutputStream lFileOutputStream = null;
        try {
            lTempFile = File.createTempFile("gpmSerialization", null);
            lFileOutputStream = new FileOutputStream(lTempFile);

            startTimer();
            serializationService.serializeSheet(adminRoleToken,
                    lSheetIds.get(0), lFileOutputStream);
            stopTimer();

            lFileOutputStream.close();
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
        finally {
            if (null != lTempFile) {
                lTempFile.delete();
            }
        }
    }
}
