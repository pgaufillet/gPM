/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Szadel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.sheet.export.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;

//import org.apache.log4j.Logger;
import org.topcased.gpm.business.ServiceImplBase;
import org.topcased.gpm.business.attributes.AttributeData;
import org.topcased.gpm.business.attributes.service.AttributesService;
import org.topcased.gpm.business.exception.AuthorizationException;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.extensions.service.Context;
import org.topcased.gpm.business.serialization.impl.SerializationServiceImpl;
import org.topcased.gpm.business.sheet.export.service.SheetExportFormat;
import org.topcased.gpm.business.sheet.export.service.SheetExportService;
import org.topcased.gpm.business.sheet.service.SheetData;
import org.topcased.gpm.business.sheet.service.SheetSummaryData;

/**
 * Implementation of SheetExportService.
 * 
 * @author tszadel
 */
public class SheetExportServiceImpl extends ServiceImplBase implements
        SheetExportService {

    // The log4j logger object for this class.
//    private final Logger logger =
//            org.apache.log4j.Logger.getLogger(SheetExportServiceImpl.class);

    private int maxExportableSheets;

    /**
     * Constructor
     */
//    SheetExportServiceImpl() {
//        logger.info("Initialization of the sheet export service");
//    }

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
    public byte[] exportSheets(String pRoleToken, List<SheetData> pSheetDatas,
            SheetExportFormat pExportFormat, String pLang) throws GDMException {

        if (pSheetDatas.size() > getMaxExportableSheets()) {
            throw new AuthorizationException("Too many sheets selected (max "
                    + maxExportableSheets + ").");
        }

        try {
            switch (pExportFormat) {
                case PDF:
                    return new PdfExporter().exportSheets(pRoleToken,
                            pSheetDatas);

                case EXCEL:
                    return new ExcelExporter().exportSheets(pRoleToken,
                            pSheetDatas);

                case XML:
                    return new XMLExporter(serializationServiceImpl).exportSheets(
                            pRoleToken, pSheetDatas);

                default:
                    throw new GDMException("Unknown format : " + pExportFormat);
            }
        }
        catch (IOException e) {
            throw new GDMException("IO Error", e);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.export.service.SheetExportService#exportSheets(java.lang.String,
     *      java.io.OutputStream, java.util.List,
     *      org.topcased.gpm.business.sheet.export.service.SheetExportFormat)
     */
    public void exportSheets(String pRoleToken, OutputStream pOutputStream,
            List<String> pSheetIds, SheetExportFormat pExportFormat)
        throws GDMException {

        if (pSheetIds.size() > getMaxExportableSheets()) {
            throw new AuthorizationException(
                    "search.request.execute.export.limit.error");
        }

        try {
            switch (pExportFormat) {
                case PDF:
                    new PdfExporter().exportSheets(pRoleToken, pOutputStream,
                            pSheetIds);
                    break;

                case EXCEL:
                    new ExcelExporter().exportSheets(pRoleToken, pOutputStream,
                            pSheetIds);
                    break;

                case XML:
                    new XMLExporter(serializationServiceImpl).exportSheets(
                            pRoleToken, pOutputStream, pSheetIds);
                    break;

                default:
                    throw new GDMException("Unknown format : " + pExportFormat);
            }
        }
        catch (IOException e) {
            throw new GDMException("IO Error", e);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.export.service.SheetExportService#exportSheets(java.lang.String,
     *      java.io.OutputStream, java.util.List,
     *      org.topcased.gpm.business.sheet.export.service.SheetExportFormat,
     *      java.util.List)
     */
    public void exportSheets(String pRoleToken, OutputStream pOutputStream,
            List<String> pSheetIds, SheetExportFormat pExportFormat,
            List<String> pExportedFieldsLabel) throws GDMException {

        if (pSheetIds.size() > getMaxExportableSheets()) {
            throw new AuthorizationException(
                    "search.request.execute.export.limit.error");
        }

        try {
            switch (pExportFormat) {
                case PDF:
                    new PdfExporter().exportSheets(pRoleToken, pOutputStream,
                            pSheetIds, pExportedFieldsLabel);
                    break;

                case EXCEL:
                    new ExcelExporter().exportSheets(pRoleToken, pOutputStream,
                            pSheetIds, pExportedFieldsLabel);
                    break;

                case XML:
                    new XMLExporter(serializationServiceImpl).exportSheets(
                            pRoleToken, pOutputStream, pSheetIds,
                            pExportedFieldsLabel);
                    break;

                default:
                    throw new GDMException("Unknown format : " + pExportFormat);
            }
        }
        catch (IOException e) {
            throw new GDMException("IO Error", e);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.export.service.SheetExportService#exportSheetSummaries(java.lang.String,
     *      java.io.OutputStream, java.util.Map, java.util.Collection,
     *      org.topcased.gpm.business.sheet.export.service.SheetExportFormat,
     *      org.topcased.gpm.business.extensions.service.Context)
     */
    public void exportSheetSummaries(String pRoleToken,
            OutputStream pOutputStream, Map<String, String> pLabels,
            Collection<SheetSummaryData> pSheetSummaries,
            SheetExportFormat pExportFormat, Context pContext) {
        switch (pExportFormat) {
            case PDF:
                new PdfExporter().exportSheetSummaries(pRoleToken,
                        pOutputStream, pLabels, pSheetSummaries, pExportFormat,
                        pContext);
                break;

            case EXCEL:
                new ExcelExporter().exportSheetSummaries(pRoleToken,
                        pOutputStream, pLabels, pSheetSummaries, pExportFormat,
                        pContext);
                break;

            case XML:
                new XMLExporter(serializationServiceImpl).exportSheetSummaries(
                        pRoleToken, pOutputStream, pLabels, pSheetSummaries,
                        pExportFormat, pContext);
                break;
            case CSV:
                new CSVExporter().exportSheetSummaries(pRoleToken,
                        pOutputStream, pLabels, pSheetSummaries, pExportFormat,
                        pContext);
                break;

            default:
                throw new GDMException("Unknown format : " + pExportFormat);
        }
    }

    private SerializationServiceImpl serializationServiceImpl;

    public final SerializationServiceImpl getSerializationServiceImpl() {
        return serializationServiceImpl;
    }

    public final void setSerializationServiceImpl(
            SerializationServiceImpl pSerializationServiceImpl) {
        serializationServiceImpl = pSerializationServiceImpl;
    }

    /**
     * set maxExportableSheets
     * 
     * @param pMaxExportableSheets
     *            the maxExportableSheets to set
     */
    public void setMaxExportableSheets(int pMaxExportableSheets) {
        this.maxExportableSheets = pMaxExportableSheets;
    }

    /**
     * get maxExportableSheets
     * 
     * @return the maxExportableSheets
     */
    public int getMaxExportableSheets() {
        int lMaxExportableSheets = maxExportableSheets;

        AttributeData[] lAttributeDatas =
                getAttributesService().getGlobalAttributes(
                        new String[] { AttributesService.MAX_EXPORTABLE_SHEETS });

        if (lAttributeDatas[0] != null) {
            lMaxExportableSheets =
                    Integer.parseInt(lAttributeDatas[0].getValues()[0]);
        }

        return lMaxExportableSheets;
    }
}
