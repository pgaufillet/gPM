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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

import org.topcased.gpm.ui.facade.AbstractFacadeTestCase;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.server.exportimport.ExportImportFacade;

/**
 * TestExportFileFacade
 * 
 * @author jlouisy
 */
public class TestExportFileFacade extends AbstractFacadeTestCase {

    private ExportImportFacade lExportImportFacade;

    private UiSession lUiSession;

    private void checkExportResult(String pExportedDataId, byte[] pExpectedFile) {
        ByteArrayOutputStream lOutputStream = new ByteArrayOutputStream();
        lExportImportFacade.getExportedData(lUiSession, pExportedDataId,
                lOutputStream);
        assertTrue(lOutputStream.size() != 0);
        assertTrue(Arrays.equals(pExpectedFile, lOutputStream.toByteArray()));
    }

    /**
     * Normal case
     */
    public void testNormalCase() {

        lUiSession = adminUserSession.getSession(DEFAULT_PRODUCT_NAME);
        lExportImportFacade = getFacadeLocator().getExportImportFacade();

        try {
            File lFile = new File("src/test/resources/data/resource/file1.txt");
            byte[] lFileAsByte = new byte[(int) lFile.length()];
            FileInputStream lFis = new FileInputStream(lFile);
            lFis.read(lFileAsByte);
            lFis.close();

            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Export Origin and Destination sheets "
                        + "(XML - with Model - with Field Names).");
            }
            String lExportedDataId =
                    lExportImportFacade.exportFile(lUiSession, lFileAsByte);
            checkExportResult(lExportedDataId, lFileAsByte);
        }
        catch (FileNotFoundException e) {
            fail(e);
        }
        catch (IOException e) {
            fail(e);
        }
    }
}
