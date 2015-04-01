/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Olivier Gehin (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.sheet.export.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.topcased.gpm.business.ServiceLocator;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.extensions.service.Context;
import org.topcased.gpm.business.search.service.SearchService;
import org.topcased.gpm.business.serialization.data.Field;
import org.topcased.gpm.business.serialization.data.MultipleField;
import org.topcased.gpm.business.serialization.impl.SerializationServiceImpl;
import org.topcased.gpm.business.sheet.export.service.SheetExportFormat;
import org.topcased.gpm.business.sheet.export.service.SheetExportService;
import org.topcased.gpm.business.sheet.impl.CacheableSheet;
import org.topcased.gpm.business.sheet.impl.CacheableSheetType;
import org.topcased.gpm.business.sheet.service.SheetData;
import org.topcased.gpm.business.sheet.service.SheetService;
import org.topcased.gpm.business.sheet.service.SheetSummaryData;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * The XML writer.
 * 
 * @author ogehin
 */
public class XMLExporter extends AbstractExporter {

    private SerializationServiceImpl serializationService;

    public XMLExporter(SerializationServiceImpl pSerializationImpl) {
        serializationService = pSerializationImpl;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.export.impl.AbstractExporter#exportSheets(java.lang.String,
     *      java.util.List)
     * @deprecated Since 1.7
     * @see #exportSheets(String, OutputStream, List)
     */
    public byte[] exportSheets(String pRoleToken, List<SheetData> pSheetDatas)
        throws IOException {
        ArrayList<String> lSheetIds = new ArrayList<String>();
        for (SheetData lSheetData : pSheetDatas) {
            lSheetIds.add(lSheetData.getId());
        }
        ByteArrayOutputStream lOutputStream = new ByteArrayOutputStream();
        ServiceLocator.instance().getSerializationService().serializeSheets(
                pRoleToken, lSheetIds, lOutputStream);
        return lOutputStream.toByteArray();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.export.impl.AbstractExporter#exportSheets(java.lang.String,
     *      java.io.OutputStream, java.util.List)
     */
    @Override
    public void exportSheets(String pRoleToken, OutputStream pOutputStream,
            List<String> pSheetIds) throws IOException {

        if (pOutputStream == null) {
            throw new IllegalArgumentException(
                    "The outputstream field cannot be null.");
        }

        ServiceLocator.instance().getSerializationService().serializeSheets(
                pRoleToken, pSheetIds, pOutputStream);

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.export.impl.AbstractExporter#exportSheets(java.lang.String,
     *      java.io.OutputStream, java.util.List, java.util.List)
     */
    public void exportSheets(String pRoleToken, OutputStream pOutputStream,
            List<String> pSheetIds, List<String> pExportedFieldsLabel)
        throws IOException {
        // We test if OutputStream is correct
        if (pOutputStream == null) {
            throw new IllegalArgumentException(
                    "The outputstream field cannot be null.");
        }
        if (pExportedFieldsLabel == null) {
            throw new IllegalArgumentException(
                    "The exported fields list cannot be null.");
        }

        // We get only simple field labelkeys.
        List<String> lSimpleFieldsLabel = new ArrayList<String>();
        for (String lLabel : pExportedFieldsLabel) {
            String[] lLabelTabs = lLabel.split(MULTIPLE_SEPARATOR);
            if (lLabelTabs.length > 1) {
                lSimpleFieldsLabel.add(lLabelTabs[1]);
            }
            else {
                lSimpleFieldsLabel.add(lLabelTabs[0]);
            }
        }
        ArrayList<CacheableSheet> lSheets = new ArrayList<CacheableSheet>();
        ServiceLocator lServiceLocator = ServiceLocator.instance();
        SheetService lSheetService = lServiceLocator.getSheetService();

        for (String lSheetId : pSheetIds) {
            CacheableSheet lSheet =
                    lSheetService.getCacheableSheet(pRoleToken, lSheetId,
                            CacheProperties.MUTABLE);

            // We get list of labelkeys to remove for export.
            CacheableSheetType lType =
                    lSheetService.getCacheableSheetType(pRoleToken,
                            lSheet.getTypeId(), CacheProperties.IMMUTABLE);
            List<String> lFieldsToRemove = new ArrayList<String>();
            for (Field lField : lType.getFields()) {
                if (lField.getMultiple()) {
                    for (Field lSubField : ((MultipleField) lField).getFields()) {
                        if (!lSimpleFieldsLabel.contains(lSubField.getLabelKey())) {
                            lFieldsToRemove.add(lField.getLabelKey()
                                    + MULTIPLE_SEPARATOR
                                    + lSubField.getLabelKey());
                        }
                    }
                }
                else if (!lSimpleFieldsLabel.contains(lField.getLabelKey())) {
                    lFieldsToRemove.add(lField.getLabelKey());
                }
            }

            // We filter the sheet
            lSheetService.filterCacheableSheet(lSheet, lFieldsToRemove);
            lSheets.add(lSheet);
        }

        //And finally we export filtered sheets
        lServiceLocator.getSerializationService().serializeCacheableSheets(
                pRoleToken, lSheets, pOutputStream);
    }

    /**
     * Export a collection of sheet summaries in XML format. {@inheritDoc}
     * 
     * @see SheetExportService#exportSheetSummaries(String, OutputStream, Map,
     *      Collection, SheetExportFormat, Context)
     */
    public void exportSheetSummaries(String pRoleToken,
            OutputStream pOutputStream, Map<String, String> pLabels,
            Collection<SheetSummaryData> pSheetSummaries,
            SheetExportFormat pExportFormat, Context pContext) {

        if (pContext != null) {
            Boolean lLimitReached =
                    pContext.get(SearchService.LIMIT_REACHED, Boolean.class);
            if (Boolean.TRUE.equals(lLimitReached)) {
                OutputStreamWriter lWriter =
                        new OutputStreamWriter(pOutputStream);
                try {
                    try {
                        lWriter.write("<!-- " + LIMIT_WARNING + " -->\n");
                        lWriter.flush();
                    }
                    catch (IOException e) {
                        throw new GDMException("Error creating XML.", e);
                    }
                    finally {
                        if (lWriter != null) {
                            lWriter.close();
                        }
                    }
                }
                catch (IOException e) {
                    throw new GDMException("Error closing XML.", e);
                }
            }
        }

        //And finally we export filtered sheets
        serializationService.serialize(pRoleToken, pSheetSummaries, pLabels,
                pOutputStream);
    }
}
