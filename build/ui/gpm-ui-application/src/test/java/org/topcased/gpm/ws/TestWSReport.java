/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ws;

import java.util.ArrayList;
import java.util.List;

import org.topcased.gpm.ws.v2.client.GDMException_Exception;
import org.topcased.gpm.ws.v2.client.SheetData;
import org.topcased.gpm.ws.v2.client.SheetExportFormat;

/**
 * TestWSReport
 * 
 * @author nveillet
 */
public class TestWSReport extends AbstractWSTestCase {

    private final static String SHEET_REFERENCE_CAT = "Gros Minet";

    private final static String SHEET_REFERENCE_DOG = "Medor";

    private final static String SHEET_REFERENCE_OTHER = "SimpleShee1";

    private final static String PRODUCT_NAME_CAT = "Happy Mouse";

    private final static String PRODUCT_NAME = "Bernard's store";

    private final static SheetExportFormat EXPORT_FORMAT =
            SheetExportFormat.PDF;

    private final static List<String> REPORT_CAT;
    static {
        REPORT_CAT = new ArrayList<String>();
        REPORT_CAT.add("monRapport");
        REPORT_CAT.add("myPDFReport");
        REPORT_CAT.add("allSheets");
    }

    private final static List<String> REPORT_CAT_DOG;
    static {
        REPORT_CAT_DOG = new ArrayList<String>(1);
        REPORT_CAT_DOG.add("allSheets");
    }

    /**
     * Tests method getAvailableReportModels
     * 
     * @throws GDMException_Exception
     *             WS Exception
     */
    public void testGetAvailableReportModels() throws GDMException_Exception {
        // Get sheets
        SheetData lSheetCat =
                staticServices.getSheetByRef(roleToken, getProcessName(),
                        PRODUCT_NAME_CAT, SHEET_REFERENCE_CAT);
        SheetData lSheetDog =
                staticServices.getSheetByRef(roleToken, getProcessName(),
                        PRODUCT_NAME, SHEET_REFERENCE_DOG);
        SheetData lSheetOther =
                staticServices.getSheetByRef(roleToken, getProcessName(),
                        PRODUCT_NAME, SHEET_REFERENCE_OTHER);

        // Get available report model for cat
        List<String> lSheetIds = new ArrayList<String>(1);
        lSheetIds.add(lSheetCat.getId());
        List<String> lReports =
                staticServices.getAvailableReportModels(roleToken, lSheetIds,
                        EXPORT_FORMAT);
        assertEquals(REPORT_CAT.size(), lReports.size());
        assertTrue("All available report models are not return.",
                REPORT_CAT.containsAll(lReports));

        // Get available report model for cat and dog
        lSheetIds.add(lSheetDog.getId());
        lReports =
                staticServices.getAvailableReportModels(roleToken, lSheetIds,
                        EXPORT_FORMAT);
        assertEquals(REPORT_CAT_DOG.size(), lReports.size());
        assertTrue("All available report models are not return.",
                REPORT_CAT_DOG.containsAll(lReports));

        // Get available report model for three different sheet types
        lSheetIds.add(lSheetOther.getId());
        lReports =
                staticServices.getAvailableReportModels(roleToken, lSheetIds,
                        EXPORT_FORMAT);
        assertTrue("Available report models are not empty.", lReports.isEmpty());
    }
}
