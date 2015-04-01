/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.mail;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.sheet.service.SheetService;
import org.topcased.gpm.business.util.ExportFormat;
import org.topcased.gpm.ui.facade.AbstractFacadeTestCase;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.server.exportimport.ExportImportFacade;

/**
 * MailFacade.sendMail test
 * 
 * @author nveilet
 */
public class TestSendMailFacade extends AbstractFacadeTestCase {

    private static final String REF_SHEET_1 = "REF_Origin_Sheet";

    private static final String REF_SHEET_2 = "REF_Destination_Sheet";

    private static final List<String> MAIL_TO =
            Arrays.asList("test-mail@test.fr");

    private static final String MAIL_MESSAGE = "This is a test message.";

    private static final String MAIL_SUBJECT = "Test message";

    /**
     * Normal case
     */
    public void testNormalCase() {

        UiSession lSession = adminUserSession.getSession(DEFAULT_PRODUCT_NAME);

        ExportImportFacade lExportFacade =
                getFacadeLocator().getExportImportFacade();

        // Get origin sheet
        SheetService lSheetService = getSheetService();

        // Get sheets identifiers
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get sheets identifiers.");
        }
        List<String> lSheetIds = new ArrayList<String>();
        lSheetIds.add(lSheetService.getSheetIdByReference(
                lSession.getRoleToken(), REF_SHEET_1));
        lSheetIds.add(lSheetService.getSheetIdByReference(
                lSession.getRoleToken(), REF_SHEET_2));

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get report model.");
        }
        String lReportModel =
                lExportFacade.getAvailableReportModels(lSession, lSheetIds,
                        ExportFormat.PDF).get(0).getValue();

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Export Sheets.");
        }

        String lExportId =
                lExportFacade.exportSheets(lSession, lSheetIds,
                        ExportFormat.PDF, lReportModel, null, new Locale("en"));

        ByteArrayOutputStream lOutputStream = new ByteArrayOutputStream();
        lExportFacade.getExportedData(lSession, lExportId, lOutputStream);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Send mail.");
        }
        try {
            getFacadeLocator().getMailFacade().sendMail(lSession, MAIL_TO,
                    MAIL_SUBJECT, MAIL_MESSAGE, lOutputStream.toByteArray());
        }
        catch (GDMException e) {
            LOGGER.error("SendMail failed.", e);
            fail(e);
        }
    }
}
