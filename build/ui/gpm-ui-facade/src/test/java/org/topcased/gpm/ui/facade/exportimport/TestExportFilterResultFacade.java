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
import java.util.HashMap;
import java.util.Map;

import org.topcased.gpm.business.search.service.ExecutableFilterData;
import org.topcased.gpm.business.util.ExportFormat;
import org.topcased.gpm.business.util.ExportParameter;
import org.topcased.gpm.ui.facade.AbstractFacadeTestCase;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.server.exportimport.ExportImportFacade;
import org.topcased.gpm.ui.facade.shared.filter.UiFilter;

/**
 * TestExportFilterResultFacade
 * 
 * @author jlouisy
 */
public class TestExportFilterResultFacade extends AbstractFacadeTestCase {

    private ExportImportFacade lExportImportFacade;

    private UiSession lUiSession;

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

        ExecutableFilterData lExecutableFilterData =
                getSearchService().getExecutableFilterByName(
                        lUiSession.getRoleToken(),
                        lUiSession.getParent().getProcessName(), null, null,
                        "SHEET_1 LIST TABLE");

        UiFilter lUiFilter =
                getFacadeLocator().getFilterFacade().getFilter(lUiSession,
                        lExecutableFilterData.getId());

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Export Filter Result (XML).");
        }
        String lExportedDataId =
                lExportImportFacade.exportFilterResult(lUiSession,
                        lUiFilter.getId(), ExportFormat.XML, null);
        checkExportResult(lExportedDataId);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Export Filter Result (PDF).");
        }
        lExportedDataId =
                lExportImportFacade.exportFilterResult(lUiSession,
                        lUiFilter.getId(), ExportFormat.PDF, null);
        checkExportResult(lExportedDataId);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Export Filter Result (EXCEL).");
        }
        lExportedDataId =
                lExportImportFacade.exportFilterResult(lUiSession,
                        lUiFilter.getId(), ExportFormat.EXCEL, null);
        checkExportResult(lExportedDataId);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Export Filter Result (CSV).");
        }
        Map<ExportParameter, String> lAdditionalParameters =
                new HashMap<ExportParameter, String>();
        lAdditionalParameters.put(ExportParameter.CSV_ESCAPE_CHARACTER,
                new Character('"').toString());
        lAdditionalParameters.put(ExportParameter.CSV_QUOTE_CHARACTER,
                new Character('"').toString());
        lAdditionalParameters.put(ExportParameter.CSV_SEPARATOR_CHARACTER, ";");
        lExportedDataId =
                lExportImportFacade.exportFilterResult(lUiSession,
                        lUiFilter.getId(), ExportFormat.CSV,
                        lAdditionalParameters);
        checkExportResult(lExportedDataId);
    }
}
