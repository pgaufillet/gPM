/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Sébastien René(Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.serialization.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.CacheableGpmObject;
import org.topcased.gpm.business.ServiceImplBase;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.extensions.service.Context;
import org.topcased.gpm.business.extensions.service.ContextBase;
import org.topcased.gpm.business.extensions.service.ExtensionPointNames;
import org.topcased.gpm.business.extensions.service.ExtensionPointParameters;
import org.topcased.gpm.business.fields.FieldSummaryData;
import org.topcased.gpm.business.serialization.converter.XMLConverter;
import org.topcased.gpm.business.serialization.data.FieldValueData;
import org.topcased.gpm.business.serialization.data.PointerFieldValueData;
import org.topcased.gpm.business.serialization.data.SheetData;
import org.topcased.gpm.business.serialization.service.SerializationService;
import org.topcased.gpm.business.sheet.impl.CacheableSheet;
import org.topcased.gpm.business.sheet.impl.CacheableSheetType;
import org.topcased.gpm.business.sheet.service.SheetSummaryData;
import org.topcased.gpm.domain.businessProcess.BusinessProcess;
import org.topcased.gpm.domain.extensions.ExtensionPoint;
import org.topcased.gpm.domain.fields.FieldDao;
import org.topcased.gpm.domain.product.Product;
import org.topcased.gpm.util.bean.CacheProperties;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * SerializationServiceImpl.
 * 
 * @author srene
 */
public class SerializationServiceImpl extends ServiceImplBase implements
        SerializationService {

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.serialization.service.SerializationService
     *      #serializeSheets(java.util.List)
     */
    public void serializeSheets(String pRoleToken, List<String> pSheetIds,
            OutputStream pOutputStream) throws GDMException {
        if (null == pSheetIds) {
            throw new IllegalArgumentException(
                    "Impossible to serialize sheets :"
                            + "the given sheet list is null.");
        }

        // creation of the XML converter
        XMLConverter lXMLConverter = new XMLConverter(pOutputStream);
        lXMLConverter.startNode("sheets");
        for (String lSheetId : pSheetIds) {
            serializeSheet(pRoleToken, lSheetId, lXMLConverter, null, null);
        }
        lXMLConverter.endNode();
        lXMLConverter.close();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.serialization.service.SerializationService
     *      #serializeSheet(java.lang.String, java.io.OutputStream)
     */
    public void serializeSheet(String pRoleToken, String pSheetId,
            OutputStream pOutputStream) throws GDMException {
        // creation of the XML converter
        XMLConverter lXMLConverter = new XMLConverter(pOutputStream);
        lXMLConverter.startNode("sheets");
        serializeSheet(pRoleToken, pSheetId, lXMLConverter, null, null);
        lXMLConverter.endNode();
        lXMLConverter.close();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.serialization.service.SerializationService
     *      #serializeCacheableSheets(String, List, OutputStream)
     */
    public void serializeCacheableSheets(String pRoleToken,
            List<CacheableSheet> pSheets, OutputStream pOutputStream)
        throws GDMException {
        // creation of the XML converter
        XMLConverter lXMLConverter = new XMLConverter(pOutputStream);
        lXMLConverter.startNode("sheets");
        for (CacheableSheet lSheet : pSheets) {
            serializeSheet(pRoleToken, lSheet, lXMLConverter, null, null);
        }
        lXMLConverter.endNode();
        lXMLConverter.close();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.serialization.service.SerializationService
     *      #serializeCacheableSheets(String, List, OutputStream)
     */
    public void serialize(String pRoleToken,
            Collection<SheetSummaryData> pSheetSummaries,
            Map<String, String> pLabels, OutputStream pOutputStream)
        throws GDMException {
        // creation of the XML converter
        XMLConverter lXMLConverter = new XMLConverter(pOutputStream);
        lXMLConverter.startNode("sheets");
        for (SheetSummaryData lSummary : pSheetSummaries) {
            SheetData lSheet = toSheetData(pLabels, lSummary);
            try {
                lXMLConverter.writeObject(lSheet);
            }
            catch (IOException e) {
                throw new GDMException("Impossible to serialize the sheet : "
                        + lSheet.getId(), e);
            }
        }
        lXMLConverter.endNode();
        lXMLConverter.close();
    }

    /**
     * Serialize sheets on an outputstream
     * 
     * @param pRoleToken
     *            The role token
     * @param pSheetsIdIterator
     *            The iterator of sheet ids
     * @param pOutStream
     *            The outputStream
     */
    public void exportSheets(String pRoleToken,
            Iterator<String> pSheetsIdIterator, OutputStream pOutStream) {
        getAuthService().assertGlobalAdminRole(pRoleToken);

        XMLConverter lConverter = new XMLConverter(pOutStream);

        try {
            lConverter.startNode("sheets");
            while (pSheetsIdIterator.hasNext()) {
                String lCurrentId = pSheetsIdIterator.next();
                CacheableGpmObject lCachedSheet =
                        getCachedValuesContainer(lCurrentId,
                                CACHE_MUTABLE_OBJECT | CACHE_EVICT_ENTITY);
                Object lSerializedSheet = lCachedSheet.marshal();

                lConverter.writeObject(lSerializedSheet);
            }
        }
        catch (IOException e) {
            throw new GDMException("Error writing to output stream", e);
        }
        finally {
            if (lConverter != null) {
                lConverter.endNode();
                lConverter.close();
            }
        }
    }

    /**
     * Export all sheets from a product or an instance.
     * 
     * @param pRoleToken
     *            Role session
     * @param pProductName
     *            Name of the product (or null to export all sheets of the
     *            current instance)
     * @param pOutStream
     *            OutputStream to write the content
     */
    public void exportSheets(String pRoleToken, String pProductName,
            OutputStream pOutStream) {
        String lInstanceName =
                getAuthService().getProcessNameFromToken(pRoleToken);
        Iterator<String> lSheetsIdIterator;
        if (StringUtils.isNotBlank(pProductName)) {
            Product lProduct = getProduct(lInstanceName, pProductName);
            lSheetsIdIterator = getSheetDao().sheetsIterator(lProduct);
        }
        else {
            BusinessProcess lInstance = getBusinessProcess(lInstanceName);
            lSheetsIdIterator = getSheetDao().sheetsIterator(lInstance);
        }
        exportSheets(pRoleToken, lSheetsIdIterator, pOutStream);
    }

    private SheetData toSheetData(Map<String, String> pLabels,
            SheetSummaryData pSheetSummary) {
        SheetData lSheet = new SheetData();
        lSheet.setId(pSheetSummary.getId());
        lSheet.setType(pSheetSummary.getSheetType());
        lSheet.setFieldValues(transformIntoFieldValueDatas(pLabels,
                pSheetSummary.getFieldSummaryDatas()));
        return lSheet;
    }

    private List<FieldValueData> transformIntoFieldValueDatas(
            Map<String, String> pLabels, FieldSummaryData[] pFieldSummaryDatas) {

        List<FieldValueData> lResults = new ArrayList<FieldValueData>();

        for (FieldSummaryData lFieldSummaryData : pFieldSummaryDatas) {
            FieldValueData lFieldValueData =
                    new FieldValueData(
                            pLabels.get(lFieldSummaryData.getLabelKey()));
            String lValue = lFieldSummaryData.getValue();
            if (null != lValue
                    && lValue.length() >= 
                        org.topcased.gpm.util.lang.StringUtils.LARGE_STRING_LENGTH) {
                lValue =
                        org.topcased.gpm.util.lang.StringUtils.PARTIAL_INFO
                                + lValue;
            }
            lFieldValueData.setValue(lValue);
            lResults.add(lFieldValueData);
        }
        return lResults;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.serialization.service.SerializationService#serializeCacheableSheets(java.lang.String,
     *      java.util.List, org.w3c.dom.Document)
     * @deprecated
     * @since 1.8
     */
    public void serializeCacheableSheets(String pRoleToken,
            List<CacheableSheet> pSheets, Document pXMLDomDocument)
        throws GDMException {
        serializeCacheableSheets(pRoleToken, pSheets, pXMLDomDocument, null);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.serialization.service.SerializationService#serializeCacheableSheets(java.lang.String,
     *      java.util.List, org.w3c.dom.Document,
     *      org.topcased.gpm.business.extensions.service.Context)
     */
    public void serializeCacheableSheets(String pRoleToken,
            List<CacheableSheet> pSheets, Document pXMLDomDocument,
            Context pContext) throws GDMException {
        if (null == pSheets) {
            throw new IllegalArgumentException(
                    "Impossible to serialize sheets :"
                            + "the given sheet list is null.");
        }

        // creation of the XML converter
        XMLConverter lXMLConverter = new XMLConverter(pXMLDomDocument);
        lXMLConverter.startNode("sheets");
        for (CacheableSheet lSheet : pSheets) {
            serializeSheet(pRoleToken, lSheet, lXMLConverter, null, pContext);
        }
        lXMLConverter.endNode();
        lXMLConverter.close();
    }

    /**
     * serializeSheet.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pSheetId
     *            String
     * @param pXMLConverter
     *            XMLConverter
     * @param pGlobalXMLDomDocument
     *            DOM Document
     * @param pContext
     *            The execution context
     * @throws GDMException
     *             error in the serialization
     */
    private void serializeSheet(String pRoleToken, String pSheetId,
            XMLConverter pXMLConverter, Document pGlobalXMLDomDocument,
            Context pContext) throws GDMException {
        CacheableSheet lSheet =
                getSheetService().getCacheableSheet(pRoleToken, pSheetId,
                        CacheProperties.MUTABLE);
        serializeSheet(pRoleToken, lSheet, pXMLConverter,
                pGlobalXMLDomDocument, pContext);
    }

    /**
     * serializeSheet.
     * 
     * @param pRoleToken
     *            Role session token
     * @param pSheetId
     *            String
     * @param pXMLConverter
     *            XMLConverter
     * @param pGlobalXMLDomDocument
     *            DOM Document
     * @param pContext
     *            The execution context
     * @throws GDMException
     *             error in the serialization
     */
    private void serializeSheet(String pRoleToken, CacheableSheet pSheet,
            XMLConverter pXMLConverter, Document pGlobalXMLDomDocument,
            Context pContext) throws GDMException {
        // creation of the sheetData that will be serialized
        SheetData lSheetData = new SheetData();
        pSheet.marshal(lSheetData);

        // Replace pointer values by "real" pointed values before export
        replacePointerFieldValuesByPointedValues(pRoleToken, lSheetData);
        try {
            pXMLConverter.writeObject(lSheetData);
        }
        catch (IOException ex) {
            throw new GDMException("Impossible to serialize the sheet : "
                    + pSheet.getId());
        }

        if (pGlobalXMLDomDocument != null) {
            String lExpression =
                    "/gpm/sheets/sheet[@id='" + pSheet.getId() + "']";
            javax.xml.xpath.XPath lXpath =
                    XPathFactory.newInstance().newXPath();
            Node lSheetXMLDocument;
            try {
                lSheetXMLDocument =
                        (Node) lXpath.evaluate(lExpression,
                                pGlobalXMLDomDocument, XPathConstants.NODE);
            }
            catch (XPathExpressionException e) {
                throw new GDMException(
                        "Invalid XPath expression for finding sheet", e);
            }
            if (lSheetXMLDocument == null) {
                throw new GDMException("Invalid XML content");
            }

            CacheableSheetType lSheetType =
                    getSheetService().getCacheableSheetType(pSheet.getTypeId(),
                            CacheProperties.IMMUTABLE);
            // Extension point preExport
            final ExtensionPoint lPreExport =
                    getExecutableExtensionPoint(lSheetType.getId(),
                            ExtensionPointNames.PRE_EXPORT, null);

            if (lPreExport != null) {
                final Context lCtx = new ContextBase(pContext);

                lCtx.put(ExtensionPointParameters.XML_SHEET_NODE,
                        lSheetXMLDocument);
                lCtx.put(ExtensionPointParameters.XML_DOCUMENT,
                        pGlobalXMLDomDocument);

                lCtx.put(ExtensionPointParameters.SHEET_ID, pSheet.getId());

                getExtensionsService().execute(pRoleToken, lPreExport, lCtx);
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @deprecated
     * @since 1.8
     * @see org.topcased.gpm.business.serialization.service.SerializationService#serializeSheets(java.lang.String,
     *      java.util.List, org.w3c.dom.Document)
     */
    public void serializeSheets(String pRoleToken, List<String> pSheetIds,
            Document pXMLDomDocument) throws GDMException {
        serializeSheets(pRoleToken, pSheetIds, pXMLDomDocument, null);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.serialization.service.SerializationService#serializeSheets(java.lang.String,
     *      java.util.List, org.w3c.dom.Document,
     *      org.topcased.gpm.business.extensions.service.Context)
     */
    public void serializeSheets(String pRoleToken, List<String> pSheetIds,
            Document pXMLDomDocument, Context pContext) throws GDMException {
        if (null == pSheetIds) {
            throw new IllegalArgumentException(
                    "Impossible to serialize sheets :"
                            + "the given sheet list is null.");
        }

        // creation of the XML converter
        XMLConverter lXMLConverter = new XMLConverter(pXMLDomDocument);
        lXMLConverter.startNode("sheets");
        for (String lSheetId : pSheetIds) {
            serializeSheet(pRoleToken, lSheetId, lXMLConverter,
                    pXMLDomDocument, pContext);
        }
        lXMLConverter.endNode();
        lXMLConverter.close();
    }

    /**
     * All pointer field values are replaced by pointed field values in
     * pSheetData
     * 
     * @param pRoleToken
     *            session token
     * @param pSheetData
     *            sheet data to export.
     */
    @SuppressWarnings("unchecked")
    private void replacePointerFieldValuesByPointedValues(String pRoleToken,
            SheetData pSheetData) {

        if (pSheetData.getFieldValues() != null) {
            Collection<FieldValueData> lPointerFieldValues =
                    new ArrayList<FieldValueData>();
            Collection<FieldValueData> lPointedFieldValues =
                    new ArrayList<FieldValueData>();
            for (FieldValueData lFieldValueData : pSheetData.getFieldValues()) {
                if (lFieldValueData instanceof PointerFieldValueData) {
                    lPointerFieldValues.add(lFieldValueData);
                    PointerFieldValueData lPointerValue =
                            (PointerFieldValueData) lFieldValueData;
                    Object lPointedValue =
                            getFieldsManager().getPointedFieldValue(pRoleToken,
                                    lPointerValue.getName(),
                                    lPointerValue.getReferencedContainerId(),
                                    lPointerValue.getReferencedFieldLabel());
                    if (lPointedValue instanceof FieldValueData) {
                        lPointedFieldValues.add((FieldValueData) lPointedValue);
                    }
                    else if (lPointedValue instanceof List) {
                        lPointedFieldValues.addAll((List<FieldValueData>) lPointedValue);
                    }
                }
            }
            pSheetData.getFieldValues().addAll(lPointedFieldValues);
            pSheetData.getFieldValues().removeAll(lPointerFieldValues);
        }
    }

    /** The field dao. */
    private FieldDao fieldDao;

    /**
     * getFieldDao.
     * 
     * @return the fieldDao
     */
    public FieldDao getFieldDao() {
        return fieldDao;
    }

    /**
     * setFieldDao.
     * 
     * @param pFieldDao
     *            the fieldDao to set
     */
    public void setFieldDao(FieldDao pFieldDao) {
        this.fieldDao = pFieldDao;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPointedFieldValue(String pRoleToken,
            FieldValueData pFieldValueData) {
        String lResult = null;
        PointerFieldValueData lPointerValue =
                (PointerFieldValueData) pFieldValueData;
        FieldValueData lPointedValue =
                (FieldValueData) getFieldsManager().getPointedFieldValue(
                        pRoleToken, lPointerValue.getName(),
                        lPointerValue.getReferencedContainerId(),
                        lPointerValue.getReferencedFieldLabel());
        if (lPointedValue != null) {
            lResult = lPointedValue.getValue();
        }
        return lResult;
    }
}
