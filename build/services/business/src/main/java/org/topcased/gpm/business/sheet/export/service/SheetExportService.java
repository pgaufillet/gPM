/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Szadel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.sheet.export.service;

import java.io.OutputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.topcased.gpm.business.extensions.service.Context;
import org.topcased.gpm.business.sheet.service.SheetData;
import org.topcased.gpm.business.sheet.service.SheetSummaryData;

/**
 * Sheet export service
 * 
 * @author llatil
 */
@Transactional(propagation = Propagation.REQUIRED)
public interface SheetExportService {
    /**
     * Exports sheets in various format.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pSheetDatas
     *            The sheets to export.
     * @param pExportFormat
     *            The format.
     * @param pLang
     *            The language (EN_en, FR_fr...)
     * @return The content of the export.
     * @deprecated Since 1.7
     * @see #exportSheets(String, OutputStream, List, SheetExportFormat)
     */
    @Transactional(readOnly = true)
    public byte[] exportSheets(String pRoleToken, List<SheetData> pSheetDatas,
            SheetExportFormat pExportFormat, String pLang);

    /**
     * Export some fields of sheets in various format.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pOutputStream
     *            The outputstream to be write
     * @param pSheetIds
     *            The sheets id
     * @param pExportFormat
     *            The format.
     */
    @Transactional(readOnly = true)
    public void exportSheets(String pRoleToken, OutputStream pOutputStream,
            List<String> pSheetIds, SheetExportFormat pExportFormat);

    /**
     * Export some fields of sheets in various format.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pOutputStream
     *            The outputstream to be write
     * @param pSheetIds
     *            The sheets id
     * @param pExportFormat
     *            The format.
     * @param pExportedFieldsLabel
     *            The fields to export
     */
    @Transactional(readOnly = true)
    public void exportSheets(String pRoleToken, OutputStream pOutputStream,
            List<String> pSheetIds, SheetExportFormat pExportFormat,
            List<String> pExportedFieldsLabel);

    /**
     * Export sheet summaries.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pOutputStream
     *            The output stream to be write
     * @param pLabels
     *            Map linking labelkey and displayed text (labelkey translation
     *            or optional label of filter result).
     * @param pSheetSummaries
     *            The sheet summaries collection to export
     * @param pExportFormat
     *            The format
     * @param pContext
     *            The context. Unused now but could be usefull for export option
     *            in future.
     */
    public void exportSheetSummaries(String pRoleToken,
            OutputStream pOutputStream, Map<String, String> pLabels,
            Collection<SheetSummaryData> pSheetSummaries,
            SheetExportFormat pExportFormat, Context pContext);

    /**
     * get maxExportableSheets
     * 
     * @return the maxExportableSheets
     */
    public int getMaxExportableSheets();
}
