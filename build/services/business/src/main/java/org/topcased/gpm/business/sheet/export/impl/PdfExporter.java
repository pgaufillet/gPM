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

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;

//import org.apache.log4j.Logger;
import org.topcased.gpm.business.ServiceLocator;
import org.topcased.gpm.business.authorization.service.AccessControlContextData;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.exception.MethodNotImplementedException;
import org.topcased.gpm.business.extensions.service.Context;
import org.topcased.gpm.business.fields.FieldAccessData;
import org.topcased.gpm.business.fields.FieldData;
import org.topcased.gpm.business.fields.FieldSummaryData;
import org.topcased.gpm.business.fields.FieldValueData;
import org.topcased.gpm.business.fields.LineFieldData;
import org.topcased.gpm.business.fields.MultipleLineFieldData;
import org.topcased.gpm.business.search.service.SearchService;
import org.topcased.gpm.business.serialization.data.DisplayGroup;
import org.topcased.gpm.business.serialization.data.Field;
import org.topcased.gpm.business.serialization.data.FieldRef;
import org.topcased.gpm.business.serialization.data.MultipleField;
import org.topcased.gpm.business.sheet.export.service.SheetExportFormat;
import org.topcased.gpm.business.sheet.export.service.SheetExportService;
import org.topcased.gpm.business.sheet.impl.CacheableSheet;
import org.topcased.gpm.business.sheet.impl.CacheableSheetType;
import org.topcased.gpm.business.sheet.service.FieldGroupData;
import org.topcased.gpm.business.sheet.service.SheetData;
import org.topcased.gpm.business.sheet.service.SheetService;
import org.topcased.gpm.business.sheet.service.SheetSummaryData;
import org.topcased.gpm.util.bean.CacheProperties;
import org.topcased.gpm.util.iterator.GpmIterator;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

/**
 * The PDF writer.
 * 
 * @author tszadel
 */
public class PdfExporter extends AbstractExporter {
    /** PDF_CELL_BORDER */
    private static final int PDF_CELL_BORDER = 5;

    /** PDF_TABLE_WIDTH */
    private static final float PDF_TABLE_WIDTH = 100.0f;

    /** Reference field. */
    private static final String REFERENCE = "Reference";

    /** The color used for header. */
    private static final Color HEADER_ROW_COLOR = new Color(160, 160, 160);

    /** The alternate color used for rows. */
    private static final Color ROW_COLOR = new Color(230, 230, 230);

    /** The Header font. */
    private static final Font HEADER_FONT =
            new Font(Font.HELVETICA, Font.DEFAULTSIZE, Font.BOLD, Color.BLACK);

    // The log4j logger object for this class.
//    private static Logger staticLogger =
//            org.apache.log4j.Logger.getLogger(PdfExporter.class);

    /**
     * Export sheets into PDF format.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pSheetData
     *            The sheets.
     * @return The content.
     * @throws IOException
     *             Error.
     * @deprecated Since 1.7
     * @see #exportSheets(String, OutputStream, List)
     */
    @Override
    public byte[] exportSheets(String pRoleToken, List<SheetData> pSheetData)
        throws IOException {
        try {
            Document lDocument = new Document();

            ByteArrayOutputStream lOut = new ByteArrayOutputStream();

            // We write a PDF
            PdfWriter.getInstance(lDocument, lOut);
            lDocument.open();

            boolean lFirstSheet = true;
            for (SheetData lData : pSheetData) {
                if (!lFirstSheet) {
                    // Creates a new page
                    lDocument.newPage();
                }
                else {
                    lFirstSheet = false;
                }
                PdfPTable lTable = new PdfPTable(2);
                lTable.setWidthPercentage(PDF_TABLE_WIDTH);

                fillSheet(pRoleToken, lTable, lData);
                lDocument.add(lTable);
            }

            lDocument.close();
            lOut.flush();
            lOut.close();

            return lOut.toByteArray();
        }
        catch (DocumentException e) {
            throw new GDMException("Error creating PDF.", e);
        }
    }

    /**
     * Fills a sheet.
     * 
     * @param pTable
     *            The table.
     * @param pSheetData
     *            The data to add.
     * @deprecated Since 1.7
     */
    private void fillSheet(String pRoleToken, PdfPTable pTable,
            SheetData pSheetData) {
        // The ref
        PdfPCell lCell = new PdfPCell(new Paragraph(REFERENCE, HEADER_FONT));
        lCell.setBackgroundColor(HEADER_ROW_COLOR);
        lCell.setBorder(PDF_CELL_BORDER);
        pTable.addCell(lCell);
        lCell =
                new PdfPCell(new Paragraph(pSheetData.getSheetReference(),
                        HEADER_FONT));
        lCell.setBackgroundColor(HEADER_ROW_COLOR);
        pTable.addCell(lCell);

        pTable.setHeaderRows(1);
        pTable.setHeadersInEvent(true);

        // The product
        String lProductLabel =
                i18nService.getValueForUser(pRoleToken, "$PRODUCT");
        lCell = new PdfPCell(new Paragraph(lProductLabel));
        pTable.addCell(lCell);
        lCell = new PdfPCell(new Paragraph(pSheetData.getProductName()));
        pTable.addCell(lCell);

        // The fields
        boolean lIsGrayedRow = false;
        for (FieldGroupData lFieldGroupData : pSheetData.getFieldGroupDatas()) {
            for (MultipleLineFieldData lMultipleLineFieldData : lFieldGroupData.getMultipleLineFieldDatas()) {
                if (lMultipleLineFieldData.isExportable()
                        && !lMultipleLineFieldData.isConfidential()) {
                    int lNbCol = 1;
                    if (lMultipleLineFieldData.isMultiField()) {
                        lNbCol = 0;
                        // Counts the number of exportable fields
                        for (FieldData lFieldData : lMultipleLineFieldData.getLineFieldDatas()[0].getFieldDatas()) {
                            if (lFieldData.isExportable()
                                    && !lFieldData.isConfidential()) {
                                lNbCol++;
                            }
                        }
                        if (0 == lNbCol) {
//                            if (staticLogger.isInfoEnabled()) {
//                                staticLogger.info("Multiplefield "
//                                        + lMultipleLineFieldData.getLabelKey()
//                                        + " is exportable, but none "
//                                        + "of the sub-fields are exportable."
//                                        + " Field ignored");
//                            }
                            continue;
                        }
                    }

                    // On each loop, we have a new field (can be multiple line
                    // and/or multiple field)
                    lIsGrayedRow = !lIsGrayedRow;

                    String lFieldLabel =
                            i18nService.getValueForUser(pRoleToken,
                                    lMultipleLineFieldData.getLabelKey());
                    lCell = new PdfPCell(new Paragraph(lFieldLabel));
                    if (lIsGrayedRow) {
                        lCell.setBackgroundColor(ROW_COLOR);
                    }
                    pTable.addCell(lCell);

                    PdfPTable lLineTable = new PdfPTable(lNbCol);
                    lCell = new PdfPCell(lLineTable);
                    if (lIsGrayedRow) {
                        lCell.setBackgroundColor(ROW_COLOR);
                    }
                    pTable.addCell(lCell);

                    for (LineFieldData lLineFieldData : lMultipleLineFieldData.getLineFieldDatas()) {
                        // On each loop, we have a new line
                        for (FieldData lFieldData : lLineFieldData.getFieldDatas()) {
                            if (lFieldData.isExportable()
                                    && !lFieldData.isConfidential()) {
                                // On each loop, we have a field (cf multiple
                                // field)
                                boolean lIsDate =
                                        lFieldData.getFieldType().equals(
                                                DATE_TYPE);

                                PdfPTable lValueTable = new PdfPTable(1);
                                lLineTable.addCell(lValueTable);
                                FieldValueData lFvd = lFieldData.getValues();
                                if (lFvd != null && lFvd.getValues() != null) {
                                    // Each field can have multiple value
                                    for (String lValue : lFvd.getValues()) {
                                        String lRealValue = lValue;
                                        if (lIsDate) {
                                            // Converting timestamp into Date
                                            lRealValue = getDateValue(lValue);
                                        }
                                        lCell =
                                                new PdfPCell(new Paragraph(
                                                        lRealValue));
                                        lCell.setBorder(0);
                                        lValueTable.addCell(lCell);
                                    }
                                }
                                else {
                                    lCell = new PdfPCell(new Paragraph(""));
                                    lCell.setBorder(0);
                                    lValueTable.addCell(lCell);
                                }
                            }
                        }
                    }
                }
            }
        }
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
        // TODO Auto-generated method stub
        if (pOutputStream == null) {
            throw new IllegalArgumentException(
                    "The outputstream field cannot be null.");
        }

        Document lDocument = null;

        try {
            lDocument = new Document();

            // We write a PDF
            PdfWriter.getInstance(lDocument, pOutputStream);
            lDocument.open();

            // Get Sheet service
            SheetService lSheetService =
                    ServiceLocator.instance().getSheetService();

            boolean lFirstSheet = true;

            for (String lSheetId : pSheetIds) {
                CacheableSheet lCacheableSheet =
                        lSheetService.getCacheableSheet(pRoleToken, lSheetId,
                                CacheProperties.IMMUTABLE);

                if (!lFirstSheet) {
                    // Creates a new page
                    lDocument.newPage();
                }
                else {
                    lFirstSheet = false;
                }
                PdfPTable lTable = new PdfPTable(2);
                lTable.setWidthPercentage(PDF_TABLE_WIDTH);

                fillSheet(pRoleToken, lTable, lCacheableSheet);
                lDocument.add(lTable);
            }

        }
        catch (DocumentException e) {
            throw new GDMException("Error creating PDF.", e);
        }
        finally {
            if (lDocument != null) {
                lDocument.close();
            }
            if (pOutputStream != null) {
                pOutputStream.flush();
                pOutputStream.close();
            }
        }

    }

    /**
     * Fills a sheet.
     * 
     * @param pRoleToken
     *            The user role token
     * @param pTable
     *            The table
     * @param pCacheableSheet
     *            The sheet to add
     */
    @SuppressWarnings("unchecked")
    private void fillSheet(String pRoleToken, PdfPTable pTable,
            CacheableSheet pCacheableSheet) {

        // The ref
        PdfPCell lCell = new PdfPCell(new Paragraph(REFERENCE, HEADER_FONT));
        lCell.setBackgroundColor(HEADER_ROW_COLOR);
        lCell.setBorder(PDF_CELL_BORDER);
        pTable.addCell(lCell);
        lCell =
                new PdfPCell(new Paragraph(
                        pCacheableSheet.getFunctionalReference(), HEADER_FONT));
        lCell.setBackgroundColor(HEADER_ROW_COLOR);
        pTable.addCell(lCell);

        pTable.setHeaderRows(1);
        pTable.setHeadersInEvent(true);

        // The product
        String lProductLabel =
                i18nService.getValueForUser(pRoleToken, "$PRODUCT");
        lCell = new PdfPCell(new Paragraph(lProductLabel));
        pTable.addCell(lCell);
        lCell = new PdfPCell(new Paragraph(pCacheableSheet.getProductName()));
        pTable.addCell(lCell);

        // The fields
        boolean lIsGrayedRow = false;

        CacheableSheetType lCacheableSheetType =
                ServiceLocator.instance().getSheetService().getCacheableSheetType(
                        pRoleToken, pCacheableSheet.getTypeId(),
                        CacheProperties.IMMUTABLE);

        final AccessControlContextData lAccessControlContext =
                getAccessControlContext(pRoleToken, pCacheableSheet);

        for (DisplayGroup lDisplayGroup : lCacheableSheetType.getDisplayGroups()) {
            for (FieldRef lFieldRef : lDisplayGroup.getFields()) {
                String lFieldName = lFieldRef.getName();
                Field lField =
                        lCacheableSheetType.getFieldFromLabel(lFieldName);

                // Check access field
                FieldAccessData lFieldAccessData =
                        authorizationService.getFieldAccess(pRoleToken,
                                lAccessControlContext, lField.getId());

                if (lField.isExportable()
                        && !lFieldAccessData.getConfidential()) {
                    int lNbCol = 1;
                    if (lField.getMultiple()) {
                        MultipleField lMultipleField = (MultipleField) lField;
                        lNbCol = 0;
                        for (Field lSubField : lMultipleField.getFields()) {
                            // Check access field
                            FieldAccessData lSubFieldAccessData =
                                    authorizationService.getFieldAccess(
                                            pRoleToken, lAccessControlContext,
                                            lField.getId());

                            if (lSubField.isExportable()
                                    && !lSubFieldAccessData.getConfidential()) {
                                lNbCol = lNbCol + 1;
                            }
                        }
                        if (0 == lNbCol) {
//                            if (staticLogger.isInfoEnabled()) {
//                                staticLogger.info("Multiplefield "
//                                        + lMultipleField.getLabelKey()
//                                        + " is exportable, but none "
//                                        + "of the sub-fields are exportable."
//                                        + " Field ignored");
//                            }
                            continue;
                        }
                    }

                    // On each loop, we have a new field (can be multiple line
                    // and/or multiple field)
                    lIsGrayedRow = !lIsGrayedRow;

                    String lFieldLabel =
                            i18nService.getValueForUser(pRoleToken,
                                    lField.getLabelKey());
                    lCell = new PdfPCell(new Paragraph(lFieldLabel));
                    if (lIsGrayedRow) {
                        lCell.setBackgroundColor(ROW_COLOR);
                    }
                    pTable.addCell(lCell);

                    PdfPTable lLineTable = new PdfPTable(lNbCol);
                    lCell = new PdfPCell(lLineTable);
                    if (lIsGrayedRow) {
                        lCell.setBackgroundColor(ROW_COLOR);
                    }
                    pTable.addCell(lCell);

                    Object lFieldValue = pCacheableSheet.getValue(lFieldName);

                    if (lFieldValue == null) {
                        PdfPTable lValueTable = new PdfPTable(1);
                        lLineTable.addCell(lValueTable);
                        lCell = new PdfPCell(new Paragraph(""));
                        lCell.setBorder(0);
                        lValueTable.addCell(lCell);
                    }
                    else if (!lField.getMultiple()
                            && (!lField.isMultivalued() || (lField.isMultivalued() && !(lFieldValue instanceof List)))) {
                        fillField(
                                lFieldName,
                                (org.topcased.gpm.business.serialization.data.FieldValueData) lFieldValue,
                                lCacheableSheetType, lLineTable);
                    }
                    else if (lField.getMultiple()
                            && (!lField.isMultivalued() || (lField.isMultivalued() && !(lFieldValue instanceof List)))) {
                        fillMultipleField(lFieldName,
                                (Map<String, Object>) lFieldValue,
                                lCacheableSheetType, lLineTable);
                    }
                    else if (!lField.getMultiple() && lField.isMultivalued()
                            && lFieldValue instanceof List) {
                        fillMultivaluedField(
                                lFieldName,
                                (List<org.topcased.gpm.business.serialization.data.FieldValueData>) lFieldValue,
                                lCacheableSheetType, lLineTable);
                    }
                    else if (lField.getMultiple() && lField.isMultivalued()
                            && lFieldValue instanceof List) {
                        fillMultipleMultivaluedField(lFieldName,
                                (List<Map<String, Object>>) lFieldValue,
                                lCacheableSheetType, lLineTable);
                    }
                }
            }
        }
    }

    /**
     * Fill a simple field
     * 
     * @param pFieldName
     *            The field name
     * @param pFieldValue
     *            The field value
     * @param pCacheableSheetType
     *            The sheet type
     * @param pLineTable
     *            The line table
     */
    private void fillField(
            String pFieldName,
            org.topcased.gpm.business.serialization.data.FieldValueData pFieldValue,
            CacheableSheetType pCacheableSheetType, PdfPTable pLineTable) {
        if (pFieldValue == null) {
            PdfPTable lValueTable = new PdfPTable(1);
            pLineTable.addCell(lValueTable);
            PdfPCell lCell = new PdfPCell(new Paragraph(""));
            lCell.setBorder(0);
            lValueTable.addCell(lCell);
        }
        else if (pCacheableSheetType.getFieldFromLabel(pFieldName).isExportable()) {
            PdfPTable lValueTable = new PdfPTable(1);
            pLineTable.addCell(lValueTable);
            PdfPCell lCell =
                    new PdfPCell(new Paragraph(pFieldValue.getValue()));
            lCell.setBorder(0);
            lValueTable.addCell(lCell);
        }
    }

    /**
     * Fill a Multiple field
     * 
     * @param pFieldName
     *            The field name
     * @param pFieldValue
     *            The field value
     * @param pCacheableSheetType
     *            The sheet type
     * @param pLineTable
     *            The line table
     */
    private void fillMultipleField(String pFieldName,
            Map<String, Object> pFieldValue,
            CacheableSheetType pCacheableSheetType, PdfPTable pLineTable) {
        final MultipleField lMultipleField =
                (MultipleField) pCacheableSheetType.getFieldFromLabel(pFieldName);

        for (Field lField : lMultipleField.getFields()) {
            final GpmIterator<org.topcased.gpm.business.serialization.data.FieldValueData> lValues =
                    new GpmIterator<org.topcased.gpm.business.serialization.data.FieldValueData>(
                            pFieldValue.get(lField.getLabelKey()));

            while (lValues.hasNext()) {
                fillField(lField.getLabelKey(), lValues.next(),
                        pCacheableSheetType, pLineTable);

            }
        }
    }

    /**
     * Fill a Multivalued field
     * 
     * @param pFieldName
     *            The field name
     * @param pFieldValue
     *            The field value
     * @param pCacheableSheetType
     *            The sheet type
     * @param pLineTable
     *            The line table
     */
    private void fillMultivaluedField(
            String pFieldName,
            List<org.topcased.gpm.business.serialization.data.FieldValueData> pFieldValue,
            CacheableSheetType pCacheableSheetType, PdfPTable pLineTable) {

        for (org.topcased.gpm.business.serialization.data.FieldValueData lFieldValueData : pFieldValue) {
            fillField(pFieldName, lFieldValueData, pCacheableSheetType,
                    pLineTable);
        }
    }

    /**
     * Fill a Multiple Multivalued field
     * 
     * @param pFieldName
     *            The field name
     * @param pFieldValue
     *            The field value
     * @param pCacheableSheetType
     *            The sheet type
     * @param pLineTable
     *            The line table
     */
    private void fillMultipleMultivaluedField(String pFieldName,
            List<Map<String, Object>> pFieldValue,
            CacheableSheetType pCacheableSheetType, PdfPTable pLineTable) {

        for (Map<String, Object> lFieldValueMap : pFieldValue) {
            fillMultipleField(pFieldName, lFieldValueMap, pCacheableSheetType,
                    pLineTable);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.export.impl.AbstractExporter#exportSheets(java.lang.String,
     *      java.io.OutputStream, java.util.List, java.util.List)
     */
    @Override
    public void exportSheets(String pRoleToken, OutputStream pOutputStream,
            List<String> pSheetIds, List<String> pExportedFieldsLabel)
        throws IOException {
        throw new MethodNotImplementedException(
                "exportSheets with exported fields label");
    }

    /**
     * Export a collection of sheet summaries in PDF format. {@inheritDoc}
     * 
     * @see SheetExportService#exportSheetSummaries(String, OutputStream, Map,
     *      Collection, SheetExportFormat, Context)
     */
    public void exportSheetSummaries(String pRoleToken,
            OutputStream pOutputStream, Map<String, String> pLabels,
            Collection<SheetSummaryData> pSheetSummaries,
            SheetExportFormat pExportFormat, Context pContext) {

        // Instantiate document.
        Document lDocument = new Document();

        // Change portrait to landscape.
        Rectangle lRectangle = lDocument.getPageSize();
        lDocument.setPageSize(new Rectangle(lRectangle.left(),
                lRectangle.bottom(), lRectangle.top(), lRectangle.right()));

        // Test Sheet and output stream.
        if (pSheetSummaries == null) {
            throw new IllegalArgumentException(
                    "Sheet summaries parameter for export cannot be nether null nor empty.");
        }
        if (pOutputStream == null) {
            throw new IllegalArgumentException(
                    "The outputstream field cannot be null.");
        }

        // Write a PDF.
        try {
            try {
                PdfWriter.getInstance(lDocument, pOutputStream);
                lDocument.open();

                if (pContext != null) {
                    Boolean lLimitReached =
                            pContext.get(SearchService.LIMIT_REACHED,
                                    Boolean.class);
                    if (lLimitReached != null && lLimitReached.booleanValue()) {
                        PdfPTable lTable = new PdfPTable(1);
                        lTable.setWidthPercentage(PDF_TABLE_WIDTH);
                        PdfPCell lCell =
                                new PdfPCell(new Paragraph(LIMIT_WARNING));
                        lCell.setBorder(Rectangle.BOX);
                        lTable.addCell(lCell);
                        lDocument.add(lTable);
                    }
                }
                
                if (pSheetSummaries.size() > 0) {
                    int lRowNumber =
                        pSheetSummaries.toArray(new SheetSummaryData[pSheetSummaries.size()])[0]
                            .getFieldSummaryDatas().length;
                    PdfPTable lTable = new PdfPTable(lRowNumber);
                    lTable.setWidthPercentage(PDF_TABLE_WIDTH);
    
                    fillSheetSummaries(lTable, pLabels, pSheetSummaries);
                    lDocument.add(lTable);
                }
                else {
                    // Create a blank page
                    PdfPTable lTable = new PdfPTable(1);
                    lTable.setWidthPercentage(PDF_TABLE_WIDTH);
                    PdfPCell lCell = new PdfPCell(new Paragraph());
                    lCell.setBorderColor(Color.WHITE);
                    lCell.setBorder(Rectangle.LEFT + Rectangle.RIGHT);
                    lTable.addCell(lCell);
                    lDocument.add(lTable);
                }
            }

            catch (DocumentException e) {
                throw new GDMException("Error creating PDF.", e);
            }
            finally {
                lDocument.close();
                pOutputStream.flush();
                pOutputStream.close();
            }
        }
        catch (IOException e) {
            throw new GDMException("Error creating PDF.", e);
        }

    }

    /**
     * Fills sheetSummaries
     * 
     * @param pTable
     *            The table
     * @param pSheetSummaries
     *            Data to add
     */
    private void fillSheetSummaries(PdfPTable pTable,
            Map<String, String> pLabels,
            Collection<SheetSummaryData> pSheetSummaries) {
        boolean lIsFirstLine = true;
        boolean lEvenRow = true;
        for (SheetSummaryData lSheetSummaryData : pSheetSummaries) {
            lEvenRow = !lEvenRow;
            if (lIsFirstLine) {
                for (FieldSummaryData lFieldSummaryData : lSheetSummaryData.getFieldSummaryDatas()) {
                    PdfPCell lCell =
                            new PdfPCell(
                                    new Paragraph(
                                            pLabels.get(lFieldSummaryData.getLabelKey())));
                    lCell.setBackgroundColor(HEADER_ROW_COLOR);
                    lCell.setBorder(Rectangle.LEFT + Rectangle.RIGHT);
                    pTable.addCell(lCell);
                }
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
                PdfPCell lCell = new PdfPCell(new Paragraph(lValue));

                if (lEvenRow) {
                    lCell.setBackgroundColor(ROW_COLOR);
                }
                lCell.setBorder(Rectangle.LEFT + Rectangle.RIGHT);
                pTable.addCell(lCell);
            }

        }
    }
}
