/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available 
 * under the terms of the Lesser Gnu Public License (LGPL)which 
 * accompanies this distribution, and is available 
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Sébastien René(Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.serialization.service;

import java.io.OutputStream;
import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.extensions.service.Context;
import org.topcased.gpm.business.extensions.service.ExtensionPointNames;
import org.topcased.gpm.business.serialization.data.FieldValueData;
import org.topcased.gpm.business.sheet.impl.CacheableSheet;
import org.w3c.dom.Document;

/**
 * SerializationService
 * 
 * @author srene
 */
@Transactional(propagation = Propagation.REQUIRED)
public interface SerializationService {
    /**
     * The extension called before an export
     * 
     * @deprecated
     * @see ExtensionPointNames.PRE_EXPORT
     * @since 1.8
     */
    public static final String EXTENSION_PRE_EXPORT =
            ExtensionPointNames.PRE_EXPORT;

    /**
     * serialize several sheets
     * 
     * @param pRoleToken
     *            Role session token
     * @param pSheetIds
     *            A list of sheets ids
     * @param pOutputStream
     *            the stream in which the XML will be written
     * @throws GDMException
     *             error during the serialization
     */
    public void serializeSheets(String pRoleToken, List<String> pSheetIds,
            OutputStream pOutputStream) throws GDMException;

    /**
     * serialize a sheet
     * 
     * @param pRoleToken
     *            Role session token
     * @param pSheetId
     *            A list of sheets ids
     * @param pOutputStream
     *            the stream in which the XML will be written
     * @throws GDMException
     *             error during the serialization
     */
    public void serializeSheet(String pRoleToken, String pSheetId,
            OutputStream pOutputStream) throws GDMException;

    /**
     * serialize several sheets
     * 
     * @param pRoleToken
     *            Role session token
     * @param pSheets
     *            A list of sheets
     * @param pOutputStream
     *            the stream in which the XML will be written
     * @throws GDMException
     *             error during the serialization
     */
    public void serializeCacheableSheets(String pRoleToken,
            List<CacheableSheet> pSheets, OutputStream pOutputStream)
        throws GDMException;

    /**
     * serialize several sheets
     * 
     * @param pRoleToken
     *            Role session token
     * @param pSheets
     *            A list of sheets
     * @param pXMLDomDocument
     *            the XML dom document
     * @throws GDMException
     *             error during the serialization
     * @deprecated
     * @since 1.8
     * @see SerializationService#serializeCacheableSheets(String, List,
     *      Document, Context)
     */
    public void serializeCacheableSheets(String pRoleToken,
            List<CacheableSheet> pSheets, Document pXMLDomDocument)
        throws GDMException;

    /**
     * serialize several sheets
     * 
     * @param pRoleToken
     *            Role session token
     * @param pSheets
     *            A list of sheets
     * @param pXMLDomDocument
     *            the XML dom document
     * @param pContext
     *            The execution context
     * @throws GDMException
     *             error during the serialization
     */
    public void serializeCacheableSheets(String pRoleToken,
            List<CacheableSheet> pSheets, Document pXMLDomDocument,
            Context pContext) throws GDMException;

    /**
     * serialize several sheets
     * 
     * @param pRoleToken
     *            Role session token
     * @param pSheetIds
     *            A list of sheets ids
     * @param pXMLDomDocument
     *            the XML dom document
     * @throws GDMException
     *             error during the serialization
     * @deprecated
     * @since 1.8
     * @see SerializationService#serializeSheets(String, List, Document,
     *      Context)
     */
    public void serializeSheets(String pRoleToken, List<String> pSheetIds,
            Document pXMLDomDocument) throws GDMException;

    /**
     * serialize several sheets
     * 
     * @param pRoleToken
     *            Role session token
     * @param pSheetIds
     *            A list of sheets ids
     * @param pXMLDomDocument
     *            the XML dom document
     * @param pContext
     *            The execution context
     * @throws GDMException
     *             error during the serialization
     */
    public void serializeSheets(String pRoleToken, List<String> pSheetIds,
            Document pXMLDomDocument, Context pContext) throws GDMException;

    /**
     * Export sheets
     * 
     * @param pRoleToken user role token
     * @param pProductName product name
     * @param pOutStream the output stream
     */
    @Transactional(readOnly = true)
    public void exportSheets(String pRoleToken, String pProductName,
            OutputStream pOutStream);

    /**
     * Replace pointer field by pointed field value
     * 
     * @param pRoleToken the role token
     * @param pFieldValueData the field value data
     * @return a field value
     */
    public String getPointedFieldValue(String pRoleToken,
            FieldValueData pFieldValueData);
}
