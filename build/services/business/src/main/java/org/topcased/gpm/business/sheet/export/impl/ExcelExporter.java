/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Szadel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.sheet.export.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.topcased.gpm.business.ServiceLocator;
import org.topcased.gpm.business.authorization.service.AccessControlContextData;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.extensions.service.Context;
import org.topcased.gpm.business.fields.FieldAccessData;
import org.topcased.gpm.business.fields.FieldData;
import org.topcased.gpm.business.fields.FieldSummaryData;
import org.topcased.gpm.business.fields.FieldType;
import org.topcased.gpm.business.fields.FieldValueData;
import org.topcased.gpm.business.fields.LineFieldData;
import org.topcased.gpm.business.fields.MultipleLineFieldData;
import org.topcased.gpm.business.serialization.data.DisplayGroup;
import org.topcased.gpm.business.serialization.data.Field;
import org.topcased.gpm.business.serialization.data.FieldRef;
import org.topcased.gpm.business.serialization.data.MultipleField;
import org.topcased.gpm.business.serialization.data.PointerFieldValueData;
import org.topcased.gpm.business.sheet.export.service.SheetExportFormat;
import org.topcased.gpm.business.sheet.impl.CacheableSheet;
import org.topcased.gpm.business.sheet.impl.CacheableSheetType;
import org.topcased.gpm.business.sheet.service.FieldGroupData;
import org.topcased.gpm.business.sheet.service.SheetData;
import org.topcased.gpm.business.sheet.service.SheetService;
import org.topcased.gpm.business.sheet.service.SheetSummaryData;
import org.topcased.gpm.domain.util.FieldsUtil;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * The Excel writer.
 * 
 * @author tszadel
 */
public class ExcelExporter extends AbstractExporter {

    /** This is the max size for an Excel cell */
    public static final int EXCEL_CELL_MAX_SIZE = 32767;

    /** Title "Reference" */
    private static final String REFERENCE = "Reference";

    /** Title "Product" */
    private static final String PRODUCT = "Product";

    /** The separator for a multiline field. */
    private static final String MULTILINE_SEPARATOR = "\n";

    /** The separator for different value of a field. */
    private static final String MULTIPLE_VALUE_SEPARATOR = ";;";

    /** A map for different sheet type. */
    private Map<String, HSSFSheet> mapSheets = new HashMap<String, HSSFSheet>();

    /** A map for different sheet type with exported fields */
    private Map<String, HSSFSheet> mapSheetsWithExportedFields =
            new HashMap<String, HSSFSheet>();

    private Map<String, List<String>> mapExportableFields =
            new HashMap<String, List<String>>();

    /**
     * Exports sheets into Excel format.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pSheetDatas
     *            The sheets.
     * @return The content.
     * @throws IOException
     *             Error.
     * @deprecated Since 1.7
     * @see #exportSheets(String, OutputStream, List)
     */
    @Override
    public byte[] exportSheets(String pRoleToken, List<SheetData> pSheetDatas)
        throws IOException {
        HSSFWorkbook lWb = new HSSFWorkbook();

        for (SheetData lData : pSheetDatas) {
            HSSFSheet lSheet = mapSheets.get(lData.getSheetTypeName());
            if (lSheet == null) {
                // Xls Sheet doesn't exist : creating it.
                lSheet = createSheet(pRoleToken, lWb, lData);
            }
            fillSheet(lSheet, lData);
        }
        ByteArrayOutputStream lOut = new ByteArrayOutputStream();
        lWb.write(lOut);
        lOut.flush();
        lOut.close();

        return lOut.toByteArray();
    }

    /**
     * Fills a sheet.
     * 
     * @param pSheet
     *            The Excel Sheet.
     * @param pSheetData
     *            The data to add.
     * @deprecated Since 1.7
     */
    private void fillSheet(HSSFSheet pSheet, SheetData pSheetData) {
        int lRowNb = pSheet.getLastRowNum() + 1;
        HSSFRow lRow = pSheet.createRow(lRowNb);
        // The ref
        lRow.createCell((short) 0).setCellValue(pSheetData.getSheetReference());
        // The product
        lRow.createCell((short) 1).setCellValue(pSheetData.getProductName());
        short lCol = (short) 2;
        // The fields
        for (FieldGroupData lFieldGroupData : pSheetData.getFieldGroupDatas()) {
            for (MultipleLineFieldData lMultipleLineFieldData : lFieldGroupData.getMultipleLineFieldDatas()) {
                if (lMultipleLineFieldData.isExportable()) {
                    String lMultiLineSep = StringUtils.EMPTY;
                    String lFieldValue = StringUtils.EMPTY;
                    // On each loop, we have a new field (can be multiple line
                    // and/or multiple field)

                    for (LineFieldData lLineFieldData : lMultipleLineFieldData.getLineFieldDatas()) {
                        // On each loop, we have a new line
                        lFieldValue += lMultiLineSep;
                        lMultiLineSep = MULTILINE_SEPARATOR;

                        String lMultipleSep = StringUtils.EMPTY;
                        for (FieldData lFieldData : lLineFieldData.getFieldDatas()) {
                            if (lFieldData.isExportable()) {
                                boolean lIsDate =
                                        lFieldData.getFieldType().equals(
                                                DATE_TYPE);

                                // On each loop, we have a field (cf multiple
                                // field)
                                lFieldValue += lMultipleSep;
                                lMultipleSep = MULTIPLE_SEPARATOR;

                                String lMultipleValSep = StringUtils.EMPTY;
                                FieldValueData lFvd = lFieldData.getValues();
                                if (lFvd != null && lFvd.getValues() != null) {
                                    // Each field can have multiple value
                                    for (String lValue : lFvd.getValues()) {
                                        String lRealValue = lValue;
                                        if (lValue == null) {
                                            lRealValue = StringUtils.EMPTY;
                                        }
                                        if (lIsDate) {
                                            // Converting timestamp into Date
                                            lRealValue = getDateValue(lValue);
                                        }
                                        lFieldValue +=
                                                lMultipleValSep + lRealValue;
                                        lMultipleValSep =
                                                MULTIPLE_VALUE_SEPARATOR;
                                    }
                                }
                            }
                        }
                    }
                    // Writing the value
                    lRow.createCell(lCol++).setCellValue(lFieldValue);
                }
            }
        }
    }

    /**
     * Creates an Excel sheet.
     * 
     * @param pWb
     *            The Workbook.
     * @param pSheetData
     *            The sheet data.
     * @return The created sheet.
     * @deprecated Since 1.7
     */
    private HSSFSheet createSheet(String pRoleToken, HSSFWorkbook pWb,
            SheetData pSheetData) {
        HSSFCellStyle lStyle = pWb.createCellStyle();
        lStyle.setFillForegroundColor(HSSFColor.GREY_50_PERCENT.index);
        lStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        HSSFSheet lSheet = pWb.createSheet();
        // Creating header
        HSSFRow lRow = lSheet.createRow(0);
        HSSFCell lCell = lRow.createCell((short) 0);
        lCell.setCellValue(REFERENCE);
        lCell.setCellStyle(lStyle);
        lCell = lRow.createCell((short) 1);
        lCell.setCellValue(PRODUCT);
        lCell.setCellStyle(lStyle);

        short lCol = (short) 2;
        for (FieldGroupData lFieldGroupData : pSheetData.getFieldGroupDatas()) {
            for (MultipleLineFieldData lMultipleLineFieldData : lFieldGroupData.getMultipleLineFieldDatas()) {
                if (lMultipleLineFieldData.isExportable()) {
                    lCell = lRow.createCell(lCol++);
                    lCell.setCellStyle(lStyle);

                    String lFieldLabel =
                            i18nService.getValueForUser(pRoleToken,
                                    lMultipleLineFieldData.getLabelKey());
                    lCell.setCellValue(lFieldLabel);
                }
            }
        }
        // Adding into map
        mapSheets.put(pSheetData.getSheetTypeName(), lSheet);
        return lSheet;
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

        HSSFWorkbook lHSSFWorkbook = new HSSFWorkbook();

        // Get Sheet service
        SheetService lSheetService =
                ServiceLocator.instance().getSheetService();

        for (String lSheetId : pSheetIds) {
            CacheableSheet lCacheableSheet =
                    lSheetService.getCacheableSheet(pRoleToken, lSheetId,
                            CacheProperties.IMMUTABLE);
            CacheableSheetType lCacheableSheetType =
                    lSheetService.getCacheableSheetType(pRoleToken,
                            lCacheableSheet.getTypeId(),
                            CacheProperties.IMMUTABLE);

            List<String> lExportableFields =
                    mapExportableFields.get(lCacheableSheet.getTypeName());

            if (lExportableFields == null) {
                // Exportable field list doesn't exist : creating it.
                lExportableFields =
                        createExportableFields(lCacheableSheetType,
                                lCacheableSheet, pRoleToken);
            }

            HSSFSheet lSheet =
                    mapSheetsWithExportedFields.get(lCacheableSheet.getTypeName());
            if (lSheet == null) {
                // Xls Sheet doesn't exist : creating it.
                lSheet =
                        createExcelSheet(pRoleToken, lHSSFWorkbook,
                                lCacheableSheet, lCacheableSheetType,
                                lExportableFields);
            }

            fillExcelSheet(lSheet, lCacheableSheet, lCacheableSheetType,
                    lExportableFields, pRoleToken, lHSSFWorkbook);
        }

        try {
            lHSSFWorkbook.write(pOutputStream);
        }
        catch (IOException e) {
            throw new GDMException("Error creating XSL.", e);
        }
        finally {
            if (pOutputStream != null) {
                pOutputStream.flush();
                pOutputStream.close();
            }
        }
    }

    /**
     * Creates the list of exportable fields
     * 
     * @param cacheableSheetType
     *            The cacheable sheet type
     * @param pCacheableSheetType
     *            The cacheable sheet
     * @param pRoleToken
     *            The role token
     * @return
     */
    private List<String> createExportableFields(
            CacheableSheetType pCacheableSheetType,
            CacheableSheet pCacheableSheet, String pRoleToken) {

        final AccessControlContextData lAccessControlContext =
                getAccessControlContext(pRoleToken, pCacheableSheet);

        ArrayList<String> lExportableFields = new ArrayList<String>();

        ArrayList<DisplayGroup> lDisplayGroups =
                new ArrayList<DisplayGroup>(
                        pCacheableSheetType.getDisplayGroups());

        // Find all fields in order
        for (DisplayGroup lDisplayGroup : lDisplayGroups) {
            for (FieldRef lFieldRef : lDisplayGroup.getFields()) {
                String lFieldName = lFieldRef.getName();
                Field lField =
                        pCacheableSheetType.getFieldFromLabel(lFieldName);

                // Check access field
                FieldAccessData lFieldAccessData =
                        authorizationService.getFieldAccess(pRoleToken,
                                lAccessControlContext, lField.getId());

                // add the field in the list if is a simple exportable field 
                if (lField.isExportable()
                        && !lFieldAccessData.getConfidential()
                        && !lField.getMultiple()) {
                    lExportableFields.add(lFieldName);
                }
                // add all sub field in the list if is a multiple exportable field 
                else if (lField.isExportable()
                        && !lFieldAccessData.getConfidential()
                        && lField.getMultiple()) {
                    MultipleField lMultipleField = (MultipleField) lField;
                    for (Field lSubField : lMultipleField.getFields()) {
                        // Check access field
                        FieldAccessData lSubFieldAccessData =
                                authorizationService.getFieldAccess(pRoleToken,
                                        lAccessControlContext, lField.getId());

                        if (lSubField.isExportable()
                                && !lSubFieldAccessData.getConfidential()) {
                            lExportableFields.add(lMultipleField.getLabelKey()
                                    + MULTIPLE_SEPARATOR
                                    + lSubField.getLabelKey());
                        }
                    }
                }
            }
        }

        // Adding into map
        mapExportableFields.put(pCacheableSheetType.getName(),
                lExportableFields);
        return lExportableFields;
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

        if (pExportedFieldsLabel == null) {
            throw new IllegalArgumentException(
                    "The exported fields label cannot be null.");
        }

        if (pOutputStream == null) {
            throw new IllegalArgumentException(
                    "The outputstream field cannot be null.");
        }

        HSSFWorkbook lWb = new HSSFWorkbook();

        // Get Sheet service
        SheetService lSheetService =
                ServiceLocator.instance().getSheetService();

        for (String lSheetId : pSheetIds) {
            CacheableSheet lCacheableSheet =
                    lSheetService.getCacheableSheet(pRoleToken, lSheetId,
                            CacheProperties.IMMUTABLE);
            CacheableSheetType lCacheableSheetType =
                    lSheetService.getCacheableSheetType(pRoleToken,
                            lCacheableSheet.getTypeId(),
                            CacheProperties.IMMUTABLE);

            HSSFSheet lSheet =
                    mapSheetsWithExportedFields.get(lCacheableSheet.getTypeName());
            if (lSheet == null) {
                // Xls Sheet doesn't exist : creating it.
                lSheet =
                        createExcelSheet(pRoleToken, lWb, lCacheableSheet,
                                lCacheableSheetType, pExportedFieldsLabel);
            }
            fillExcelSheet(lSheet, lCacheableSheet, lCacheableSheetType,
                    pExportedFieldsLabel, pRoleToken, lWb);
        }
        try {
            lWb.write(pOutputStream);

        }
        catch (IOException e) {
            throw new GDMException("Error creating XSL.", e);
        }
        finally {
            if (pOutputStream != null) {
                pOutputStream.flush();
                pOutputStream.close();
            }
        }
    }

    /**
     * Fills a sheet.
     * 
     * @param pSheet
     *            The Excel Sheet.
     * @param pSheetData
     *            The data to add.
     * @param pCacheableSheet
     *            The cacheable sheet data
     * @param pExportedFieldsLabel
     *            The exported fields
     */
    private void fillExcelSheet(HSSFSheet pSheet,
            CacheableSheet pCacheableSheet,
            CacheableSheetType pCacheableSheetType,
            List<String> pExportedFieldsLabel, String pRoleToken,
            HSSFWorkbook pWb) {

        //Create Style
        HSSFCellStyle lStyle2 = pWb.createCellStyle();
        lStyle2.setWrapText(true);

        int lRowNb = pSheet.getLastRowNum() + 1;
        HSSFRow lRow = pSheet.createRow(lRowNb);
        // The ref
        HSSFCell lCell0 = lRow.createCell((short) 0);
        lCell0.setCellValue(org.topcased.gpm.util.lang.StringUtils.unEscapeNewLine(pCacheableSheet.getFunctionalReference()));
        lCell0.setCellStyle(lStyle2);

        // The product        
        HSSFCell lCell1 = lRow.createCell((short) 1);
        lCell1.setCellValue(org.topcased.gpm.util.lang.StringUtils.unEscapeNewLine(pCacheableSheet.getProductName()));
        lCell1.setCellStyle(lStyle2);
        short lCol = (short) 2;

        // Through the exported fields
        for (String lExportedField : pExportedFieldsLabel) {
            String[] lFieldsNameTab = lExportedField.split(MULTIPLE_SEPARATOR);
            String lMultipleFieldName = lFieldsNameTab[0];
            String lSimpleFieldName = null;
            if (lFieldsNameTab.length > 1) {
                lSimpleFieldName = lFieldsNameTab[1];
            }

            List<org.topcased.gpm.business.serialization.data.FieldValueData> lFieldValueDataList =
                    getFieldValueData(pCacheableSheet,
                            pCacheableSheet.getValuesMap().get(
                                    lMultipleFieldName), lSimpleFieldName);

            //The use of stringBuilder is recommended in case of concatenation within a loop
            StringBuilder lFieldValueString = new StringBuilder();
            String lSeparator = StringUtils.EMPTY;
            for (org.topcased.gpm.business.serialization.data.FieldValueData lFieldValueData : lFieldValueDataList) {
                String lFieldValue =
                        getFieldValue(pCacheableSheetType, lFieldValueData);

                /* if the field is pointer field, then fill the value */
                if (lFieldValue == null) {
                    if (lFieldValueData instanceof PointerFieldValueData) {
                        lFieldValue =
                                ServiceLocator.instance().getSerializationService().getPointedFieldValue(
                                        pRoleToken, lFieldValueData);
                    }
                }

                //Test if the field value is too long for Excel cells
                if (lFieldValue != null
                        && lFieldValue.length() >= EXCEL_CELL_MAX_SIZE) {
                    //The static text is inserted at the beginning of the field to indicate the field has been truncated
                    //The characters above the EXCEL_CELL_MAX_SIZE-PARTIAL_INFO.length() are not inserted

                    lFieldValue =
                            org.topcased.gpm.util.lang.StringUtils.PARTIAL_INFO
                                    + lFieldValue.substring(
                                            0,
                                            EXCEL_CELL_MAX_SIZE
                                                    - org.topcased.gpm.util.lang.StringUtils.PARTIAL_INFO.length());

                }

                lFieldValueString.append(lSeparator);
                lFieldValueString.append(org.topcased.gpm.util.lang.StringUtils.unEscapeNewLine(lFieldValue));
                lSeparator = MULTIPLE_VALUE_SEPARATOR;
            }
            HSSFCell lCell = lRow.createCell(lCol++);
            lCell.setCellValue(lFieldValueString.toString());

            //Create Style
            HSSFCellStyle lStyle3 = pWb.createCellStyle();
            lStyle3.setWrapText(true);
            lCell.setCellStyle(lStyle3);
        }
    }

    /**
     * Creates an Excel sheet.
     * 
     * @param pWb
     *            The Workbook.
     * @param pSheetData
     *            The sheet data.
     * @param pExportedFieldsLabel
     *            The exported fields label
     * @return The created sheet.
     */
    private HSSFSheet createExcelSheet(String pRoleToken, HSSFWorkbook pWb,
            CacheableSheet pCacheableSheet,
            CacheableSheetType pCacheableSheetType,
            List<String> pExportedFieldsLabel) {
        HSSFCellStyle lStyle = pWb.createCellStyle();
        lStyle.setFillForegroundColor(HSSFColor.GREY_50_PERCENT.index);
        lStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        lStyle.setWrapText(true);

        HSSFSheet lSheet = pWb.createSheet();
        // Creating header
        HSSFRow lRow = lSheet.createRow(0);
        HSSFCell lCell = lRow.createCell((short) 0);
        lCell.setCellValue(REFERENCE);
        lCell.setCellStyle(lStyle);
        lCell = lRow.createCell((short) 1);
        lCell.setCellValue(PRODUCT);
        lCell.setCellStyle(lStyle);

        short lCol = (short) 2;

        updateExportedFieldsListWithMultipleFields(pExportedFieldsLabel,
                pCacheableSheetType, pCacheableSheet, pRoleToken);

        for (String lExportedField : pExportedFieldsLabel) {
            lCell = lRow.createCell(lCol++);
            lCell.setCellStyle(lStyle);
            String[] lFieldsNameTab = lExportedField.split(MULTIPLE_SEPARATOR);
            if (lFieldsNameTab.length > 1) {
                String lMultipleFieldName = lFieldsNameTab[0];
                String lSimpleFieldName = lFieldsNameTab[1];
                lCell.setCellValue(i18nService.getValueForUser(pRoleToken,
                        lMultipleFieldName)
                        + MULTIPLE_SEPARATOR
                        + i18nService.getValueForUser(pRoleToken,
                                lSimpleFieldName));
            }
            else {
                lCell.setCellValue(i18nService.getValueForUser(pRoleToken,
                        lExportedField));
            }
        }

        // Adding into map
        mapSheetsWithExportedFields.put(pCacheableSheet.getTypeName(), lSheet);
        return lSheet;
    }

    /**
     * Update the exported fields list label for multiple fields, add all
     * exported sub fields
     * 
     * @param pExportedFieldsLabel
     *            the initial exported fields label
     * @param pCacheableSheetType
     *            the cacheable sheet type
     * @param pCacheableSheet
     *            The cacheable element
     * @param pRoleToken
     *            The role token
     */
    protected void updateExportedFieldsListWithMultipleFields(
            List<String> pExportedFieldsLabel,
            CacheableSheetType pCacheableSheetType,
            CacheableSheet pCacheableSheet, String pRoleToken) {
        List<String> lNewFields = new ArrayList<String>();

        final AccessControlContextData lAccessControlContext =
                getAccessControlContext(pRoleToken, pCacheableSheet);

        for (String lExportedField : pExportedFieldsLabel) {
            Field lFieldFromLabel =
                    pCacheableSheetType.getFieldFromLabel(lExportedField);
            if (lFieldFromLabel instanceof MultipleField) {

                MultipleField lMultipleField = (MultipleField) lFieldFromLabel;
                for (Field lField : lMultipleField.getFields()) {
                    // Check access field
                    FieldAccessData lFieldAccessData =
                            authorizationService.getFieldAccess(pRoleToken,
                                    lAccessControlContext, lField.getId());

                    if (lField.isExportable()
                            && !lFieldAccessData.getConfidential()) {
                        lNewFields.add(lMultipleField.getLabelKey()
                                + MULTIPLE_SEPARATOR + lField.getLabelKey());
                    }
                }
            }
            else {
                lNewFields.add(lExportedField);
            }
        }

        pExportedFieldsLabel.clear();

        for (String lFieldToAdd : lNewFields) {
            pExportedFieldsLabel.add(lFieldToAdd);
        }
    }

    public void exportSheetSummaries(String pRoleToken,
            OutputStream pOutputStream, Map<String, String> pLabels,
            Collection<SheetSummaryData> pSheetSummaries,
            SheetExportFormat pExportFormat, Context pContext) {
        if (pSheetSummaries == null) {
            throw new IllegalArgumentException(
                    "Sheet summaries parameter for export cannot be null.");
        }
        if (pOutputStream == null) {
            throw new IllegalArgumentException(
                    "The outputstream field cannot be null.");
        }
        try {
            try {
                HSSFWorkbook lWb = new HSSFWorkbook();

                boolean lLimitReached = false;
                if (pContext != null && pContext.get("limitReached") != null) {
                    lLimitReached = pContext.get("limitReached", Boolean.class);
                }

                fillSheetSummaries(lWb, pLabels, pSheetSummaries, lLimitReached);

                lWb.write(pOutputStream);

            }
            catch (IOException e) {
                throw new GDMException("Error creating XSL.", e);
            }
            finally {
                if (pOutputStream != null) {
                    pOutputStream.flush();
                    pOutputStream.close();
                }
            }
        }
        catch (IOException e) {
            throw new GDMException("Error while closing XSL after creation.", e);
        }
    }

    private void fillSheetSummaries(HSSFWorkbook pWb,
            Map<String, String> pLabels,
            Collection<SheetSummaryData> pSheetSummaries, Boolean pLimitReached) {

        //creating XSL sheet.
        HSSFSheet lSheet = pWb.createSheet();
        short lRowIndex = 0;

        if (pLimitReached) {
            HSSFRow lRow = lSheet.createRow(lRowIndex);
            HSSFCell lCell = lRow.createCell(lRowIndex);
            lCell.setCellValue(LIMIT_WARNING);
            lRowIndex++;
        }

        boolean lIsFirstLine = true;
        boolean lEvenRow = true;

        for (SheetSummaryData lSheetSummaryData : pSheetSummaries) {
            lEvenRow = !lEvenRow;
            // Creating header
            if (lIsFirstLine) {
                HSSFRow lRow = lSheet.createRow(lRowIndex);

                HSSFCellStyle lStyle = pWb.createCellStyle();
                lStyle.setFillForegroundColor(HSSFColor.GREY_50_PERCENT.index);
                lStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

                short lIndex = 0;
                for (FieldSummaryData lFieldSummaryData : lSheetSummaryData.getFieldSummaryDatas()) {
                    HSSFCell lCell = lRow.createCell(lIndex);
                    String lValue =
                            pLabels.get(lFieldSummaryData.getLabelKey());
                    lCell.setCellValue(lValue);
                    lCell.setCellStyle(lStyle);
                    lIndex++;
                }
                lIsFirstLine = false;
                lRowIndex++;
            }
            HSSFRow lRow = lSheet.createRow(lRowIndex);
            short lIndex = 0;
            for (FieldSummaryData lFieldSummaryData : lSheetSummaryData.getFieldSummaryDatas()) {
                HSSFCell lCell = lRow.createCell(lIndex);
                String lType = lFieldSummaryData.getType();
                
                String lFieldValue = org.topcased.gpm.util.lang.StringUtils.unEscapeNewLine(lFieldSummaryData.getValue());

                if (lFieldValue != null) {
                    if (FieldType.SIMPLE_BOOLEAN_FIELD.toString().equals(lType)) {
                        lCell.setCellType(HSSFCell.CELL_TYPE_BOOLEAN);
                        lCell.setCellValue(Boolean.valueOf(lFieldValue));
                    }
                    else if (FieldType.SIMPLE_INTEGER_FIELD.toString().equals(
                            lType)
                            || FieldType.SIMPLE_REAL_FIELD.toString().equals(
                                    lType)) {
                        lCell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                        lCell.setCellValue(Double.valueOf(lFieldValue));
                    }
                    else if (FieldType.SIMPLE_DATE_FIELD.toString().equals(
                            lType)) {
                        HSSFCellStyle lStyle = pWb.createCellStyle();
                        lStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy h:mm"));
                        try {
                            lCell.setCellValue(FieldsUtil.parseDate(lFieldValue));
                        }
                        catch (ParseException e) {
                            throw new GDMException("Error creating XSL.", e);
                        }
                        lCell.setCellStyle(lStyle);
                    }
                    // All other cases : simple field , choice field ...
                    else {
                        lCell.setCellType(HSSFCell.CELL_TYPE_STRING);
                        //Test if the field value is too long for Excel cells
                        if (lFieldValue != null) {
                            if (lFieldValue.length() >= EXCEL_CELL_MAX_SIZE) {
                                //The static text is inserted at the beginning of the field to indicate the field has been truncated
                                //The characters above the EXCEL_CELL_MAX_SIZE-PARTIAL_INFO.length() are not inserted
                                lFieldValue =
                                        org.topcased.gpm.util.lang.StringUtils.PARTIAL_INFO
                                                + lFieldValue.substring(
                                                        0,
                                                        EXCEL_CELL_MAX_SIZE
                                                                - org.topcased.gpm.util.lang.StringUtils.PARTIAL_INFO.length());
                            }
                            else if (lFieldValue.length() >= org.topcased.gpm.util.lang.StringUtils.LARGE_STRING_LENGTH) {
                                lFieldValue =
                                        org.topcased.gpm.util.lang.StringUtils.PARTIAL_INFO
                                                + lFieldValue;
                            }

                        }
                        lCell.setCellValue(lFieldValue);
                        HSSFCellStyle lStyle = pWb.createCellStyle();
                        lStyle.setWrapText(true);
                        lCell.setCellStyle(lStyle);
                    }
                }
                lIndex++;
            }
            lRowIndex++;
        }
    }
}
