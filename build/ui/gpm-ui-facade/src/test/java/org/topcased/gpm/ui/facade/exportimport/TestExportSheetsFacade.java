/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien LOUISY (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.exportimport;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.exception.MethodNotImplementedException;
import org.topcased.gpm.business.sheet.service.SheetService;
import org.topcased.gpm.business.util.ExportFormat;
import org.topcased.gpm.ui.facade.AbstractFacadeTestCase;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.server.exportimport.ExportImportFacade;

/**
 * TestExportSheetsFacade
 * 
 * @author jlouisy
 */
public class TestExportSheetsFacade extends AbstractFacadeTestCase {

    private ExportImportFacade lExportImportFacade;

    private UiSession lUiSession;

    private static final String MODEL_1 = "SheetsInPDF_model_1";

    private void checkExportResult(String pExportedDataId) {
        ByteArrayOutputStream lOutputStream = new ByteArrayOutputStream();
        lExportImportFacade.getExportedData(lUiSession, pExportedDataId,
                lOutputStream);
        assertTrue(lOutputStream.size() != 0);
    }

    /**
     * Normal case
     */
    public void testNormalCase() {

        lUiSession = adminUserSession.getSession(DEFAULT_PRODUCT_NAME);
        lExportImportFacade = getFacadeLocator().getExportImportFacade();

        // Get origin sheet
        SheetService lSheetService = getSheetService();

        List<String> lSheetIds = new ArrayList<String>();
        lSheetIds.add(lSheetService.getSheetIdByReference(
                lUiSession.getRoleToken(), "REF_Origin_Sheet"));
        lSheetIds.add(lSheetService.getSheetIdByReference(
                lUiSession.getRoleToken(), "REF_Destination_Sheet"));

        Locale lLocale = Locale.FRENCH;

        ArrayList<String> lFieldNames = new ArrayList<String>();
        lFieldNames.add("SHEET_NAME");
        lFieldNames.add("EXTENSION_POINT_FIELD");
        lFieldNames.add("EXTENDED_ACTION_FIELD");

        //Export XML format
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Export Origin and Destination sheets "
                    + "(XML - with Model - with Field Names).");
        }
        String lExportedDataId;
        try {
            lExportedDataId =
                    lExportImportFacade.exportSheets(lUiSession, lSheetIds,
                            ExportFormat.XML, MODEL_1, lFieldNames, lLocale);
        }
        catch (GDMException lE) {
            assertEquals("Invalid export format : XML", lE.getMessage());
        }

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Export Origin and Destination sheets "
                    + "(XML - no Model - with Field Names).");
        }
        lExportedDataId =
                lExportImportFacade.exportSheets(lUiSession, lSheetIds,
                        ExportFormat.XML, null, lFieldNames, lLocale);
        checkExportResult(lExportedDataId);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Export Origin and Destination sheets "
                    + "(XML - no Model - no Field Names).");
        }
        lExportedDataId =
                lExportImportFacade.exportSheets(lUiSession, lSheetIds,
                        ExportFormat.XML, null, null, lLocale);
        checkExportResult(lExportedDataId);

        //<<<<<<<<<<<<<<<<<<<<<<<< END XML >>>>>>>>>>>>>>>>>><<<

        //Export PDF format
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Export Origin and Destination sheets "
                    + "(PDF - with Model - with Field Names).");
        }
        lExportedDataId =
                lExportImportFacade.exportSheets(lUiSession, lSheetIds,
                        ExportFormat.PDF, MODEL_1, lFieldNames, lLocale);
        checkExportResult(lExportedDataId);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Export Origin and Destination sheets "
                    + "(PDF - no Model - with Field Names).");
        }
        try {
            lExportedDataId =
                    lExportImportFacade.exportSheets(lUiSession, lSheetIds,
                            ExportFormat.PDF, null, lFieldNames, lLocale);
        }
        catch (MethodNotImplementedException lE) {
            assertEquals(
                    "Method 'exportSheets with exported fields label' not implemented",
                    lE.getMessage());
        }

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Export Origin and Destination sheets "
                    + "(PDF - no Model - no Field Names).");
        }
        lExportedDataId =
                lExportImportFacade.exportSheets(lUiSession, lSheetIds,
                        ExportFormat.PDF, null, null, lLocale);
        checkExportResult(lExportedDataId);

        //<<<<<<<<<<<<<<<<<<<<<<<< END PDF >>>>>>>>>>>>>>>>>><<<

        //Export EXCEL format
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Export Origin and Destination sheets "
                    + "(EXCEL - with Model - with Field Names).");
        }
        lExportedDataId =
                lExportImportFacade.exportSheets(lUiSession, lSheetIds,
                        ExportFormat.EXCEL, MODEL_1, lFieldNames, lLocale);
        checkExportResult(lExportedDataId);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Export Origin and Destination sheets "
                    + "(EXCEL - no Model - with Field Names).");
        }
        lExportedDataId =
                lExportImportFacade.exportSheets(lUiSession, lSheetIds,
                        ExportFormat.EXCEL, null, lFieldNames, lLocale);
        checkExportResult(lExportedDataId);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Export Origin and Destination sheets "
                    + "(EXCEL - no Model - no Field Names).");
        }
        lExportedDataId =
                lExportImportFacade.exportSheets(lUiSession, lSheetIds,
                        ExportFormat.EXCEL, null, null, lLocale);
        checkExportResult(lExportedDataId);

        //<<<<<<<<<<<<<<<<<<<<<<<< END EXCEL >>>>>>>>>>>>>>>>>><<<

    }
}
