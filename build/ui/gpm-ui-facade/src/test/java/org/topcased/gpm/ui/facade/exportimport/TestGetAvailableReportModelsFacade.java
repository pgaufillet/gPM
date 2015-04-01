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

import java.util.ArrayList;
import java.util.List;

import org.topcased.gpm.business.sheet.service.SheetService;
import org.topcased.gpm.business.util.ExportFormat;
import org.topcased.gpm.business.util.Translation;
import org.topcased.gpm.ui.facade.AbstractFacadeTestCase;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.server.exportimport.ExportImportFacade;

/**
 * TestGetAvailableReportModelsFacade
 * 
 * @author jlouisy
 */
public class TestGetAvailableReportModelsFacade extends AbstractFacadeTestCase {

    private static final Translation MODEL_1 =
            new Translation("SheetsInPDF_model_1", "SheetsInPDF_model_1");

    private static final Translation MODEL_2 =
            new Translation("SheetsInPDF_model_2", "SheetsInPDF_model_2");

    /**
     * Normal case
     */
    public void testNormalCase() {

        UiSession lUiSession =
                adminUserSession.getSession(DEFAULT_PRODUCT_NAME);
        ExportImportFacade lExportImportFacade =
                getFacadeLocator().getExportImportFacade();

        // Get origin sheet
        SheetService lSheetService = getSheetService();

        List<String> lSheetIds = new ArrayList<String>();
        lSheetIds.add(lSheetService.getSheetIdByReference(
                lUiSession.getRoleToken(), "REF_Origin_Sheet"));
        lSheetIds.add(lSheetService.getSheetIdByReference(
                lUiSession.getRoleToken(), "REF_Destination_Sheet"));

        ArrayList<String> lFieldNames = new ArrayList<String>();
        lFieldNames.add("SHEET_NAME");
        lFieldNames.add("EXTENSION_POINT_FIELD");
        lFieldNames.add("EXTENDED_ACTION_FIELD");

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get available models (XML).");
        }
        List<Translation> lModelsList;
        try {
            lModelsList =
                    lExportImportFacade.getAvailableReportModels(lUiSession,
                            lSheetIds, ExportFormat.XML);
        }
        catch (IllegalArgumentException lE) {
            assertEquals(
                    "invalid value 'XML', possible values are: [PDF, XLS]",
                    lE.getMessage());
        }

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get available models (PDF).");
        }
        lModelsList =
                lExportImportFacade.getAvailableReportModels(lUiSession,
                        lSheetIds, ExportFormat.PDF);
        assertEquals(2, lModelsList.size());
        assertTrue(lModelsList.contains(MODEL_1));
        assertTrue(lModelsList.contains(MODEL_2));

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get available models (XSL).");
        }
        lModelsList =
                lExportImportFacade.getAvailableReportModels(lUiSession,
                        lSheetIds, ExportFormat.EXCEL);
        assertEquals(2, lModelsList.size());
        assertTrue(lModelsList.contains(MODEL_1));
        assertTrue(lModelsList.contains(MODEL_2));
    }
}
