/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Olivier Gehin (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.sheet.export.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.extensions.service.Context;
import org.topcased.gpm.business.fields.FieldSummaryData;
import org.topcased.gpm.business.search.service.SearchService;
import org.topcased.gpm.business.sheet.export.service.SheetExportFormat;
import org.topcased.gpm.business.sheet.export.service.SheetExportService;
import org.topcased.gpm.business.sheet.service.SheetSummaryData;

import au.com.bytecode.opencsv.CSVWriter;

/**
 * The CSV exporter.
 * 
 * @author ogehin
 */
public class CSVExporter {

    /** The warning message when filter limit is reached. */
    protected static final String LIMIT_WARNING =
            "WARNING: this generated file doesn't contain "
                    + "all filter results because filter limit was reached";

    /**
     * Export a collection of sheet summaries in CSV format. {@inheritDoc}
     * 
     * @see SheetExportService#exportSheetSummaries(String, OutputStream, Map,
     *      Collection, SheetExportFormat, Context)
     */
    public void exportSheetSummaries(String pRoleToken,
            OutputStream pOutputStream, Map<String, String> pLabels,
            Collection<SheetSummaryData> pSheetSummaries,
            SheetExportFormat pExportFormat, Context pContext) {

        OutputStreamWriter lWriter;
        try {
            lWriter = new OutputStreamWriter(pOutputStream, "UTF-8");

        }
        catch (UnsupportedEncodingException e1) {

            throw new GDMException("Error of CSV encoding.", e1);
        }

        char lSeparator = CSVWriter.DEFAULT_SEPARATOR;
        char lQuote = CSVWriter.DEFAULT_QUOTE_CHARACTER;
        char lEscape = CSVWriter.DEFAULT_ESCAPE_CHARACTER;

        // Retrieve separator, quote and escape characters if context exist.
        if (pContext != null) {
            if (pContext.get("separatorCharacter") != null) {
                lSeparator =
                        pContext.get("separatorCharacter", String.class).charAt(
                                0);
            }
            if (pContext.get("quoteCharacter") != null) {
                lQuote = pContext.get("quoteCharacter", String.class).charAt(0);
            }
            if (pContext.get("escapeCharacter") != null) {
                lEscape =
                        pContext.get("escapeCharacter", String.class).charAt(0);
            }
        }

        CSVWriter lCSVWriter =
                new CSVWriter(lWriter, lSeparator, lQuote, lEscape);

        boolean lIsFirstLine = true;

        if (pContext != null) {
            Boolean lLimitReached =
                    pContext.get(SearchService.LIMIT_REACHED, Boolean.class);
            if (lLimitReached != null && lLimitReached.booleanValue()) {
                String[] lWarningLimit = { LIMIT_WARNING };
                lCSVWriter.writeNext(lWarningLimit);
            }
        }

        for (SheetSummaryData lSheetSummaryData : pSheetSummaries) {
            ArrayList<String> lLine = new ArrayList<String>();
            if (lIsFirstLine) {
                for (FieldSummaryData lFieldSummaryData : lSheetSummaryData.getFieldSummaryDatas()) {
                    lLine.add(pLabels.get(lFieldSummaryData.getLabelKey()));
                }
                lCSVWriter.writeNext(lLine.toArray(new String[lLine.size()]));
                lLine = new ArrayList<String>();
                lIsFirstLine = false;
            }
            for (FieldSummaryData lFieldSummaryData : lSheetSummaryData.getFieldSummaryDatas()) {
                String lValue = lFieldSummaryData.getValue();
                if (null != lValue
                        && lValue.length() >= org.topcased.gpm.util.lang.StringUtils.LARGE_STRING_LENGTH) {
                    lValue =
                            org.topcased.gpm.util.lang.StringUtils.PARTIAL_INFO
                                    + lValue;
                }

                lLine.add(lValue);
            }
            lCSVWriter.writeNext(lLine.toArray(new String[lLine.size()]));
        }
        try {
            try {
                if (lCSVWriter != null) {
                    lCSVWriter.flush();
                }
            }
            catch (IOException e) {
                throw new GDMException("Error creating CSV.", e);
            }
            finally {
                if (lCSVWriter != null) {
                    lCSVWriter.close();
                }
            }
        }
        catch (IOException e) {
            throw new GDMException("Error closing CSV.", e);
        }
    }
}
