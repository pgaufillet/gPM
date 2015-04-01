/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.server.exportimport;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.topcased.gpm.business.exception.ImportException;
import org.topcased.gpm.business.exception.SchemaValidationException;
import org.topcased.gpm.business.util.ExportFormat;
import org.topcased.gpm.business.util.ExportParameter;
import org.topcased.gpm.business.util.Translation;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.shared.export.UiExportableGroup;

/**
 * ExportImportFacade
 * 
 * @author nveillet
 */
public interface ExportImportFacade {

    /**
     * Clear an exported file from cache
     * 
     * @param pSession
     *            the session
     * @param pFileIdInCache
     *            the file identifier
     */
    public void clearCache(final UiSession pSession, final String pFileIdInCache);

    /**
     * Create command to export a file and put it in the cache
     * 
     * @param pSession
     *            Current user session.
     * @param pFile
     *            File to get.
     * @return Id of the command in cache
     */
    public String exportFile(final UiSession pSession, final byte[] pFile);

    /**
     * Create command to export a filter result and put it in the cache
     * 
     * @param pSession
     *            Current user session.
     * @param pFilterId
     *            Id of filter to execute.
     * @param pExportFormat
     *            Export format.
     * @param pAdditionalParameters
     *            The additional parameters
     * @return Id of the command in cache
     */
    public String exportFilterResult(final UiSession pSession,
            final String pFilterId, ExportFormat pExportFormat,
            Map<ExportParameter, String> pAdditionalParameters);

    /**
     * Create command to export a list of products and put it in the cache
     * 
     * @param pSession
     *            Current user session
     * @param pProductsIds
     *            List of identifiers of products to export
     * @return Id of the command in cache
     */
    public String exportProducts(final UiSession pSession,
            final List<String> pProductsIds);

    /**
     * Create command to export a list of sheets and put it in the cache
     * 
     * @param pSession
     *            Current user session
     * @param pSheetsIds
     *            List of Ids of sheets to export
     * @param pExportFormat
     *            Export format
     * @param pReportModel
     *            Report model
     * @param pFieldsNames
     *            List of fields to export
     * @param pLocale
     *            Current preferred language.
     * @return Id of the command in cache
     */
    public String exportSheets(final UiSession pSession,
            final List<String> pSheetsIds, final ExportFormat pExportFormat,
            final String pReportModel, final List<String> pFieldsNames,
            final Locale pLocale);

    /**
     * Get attached file content from its identifier.
     * 
     * @param pSession
     *            Current user session.
     * @param pAttachedFieldValueId
     *            Field Id.
     * @return Attached field content of given Id.
     */
    public byte[] getAttachedFile(final UiSession pSession,
            final String pAttachedFieldValueId);

    /**
     * Get compatible report model names
     * 
     * @param pSession
     *            Current user session.
     * @param pSheetIds
     *            List of sheet Ids to export.
     * @param pExportFormat
     *            Export format.
     * @return List of model names.
     */
    public List<Translation> getAvailableReportModels(final UiSession pSession,
            final List<String> pSheetIds, final ExportFormat pExportFormat);

    /**
     * Get exportable field organized by group for a given Fields Container.
     * 
     * @param pSession
     *            current user session
     * @param pContainerIds
     *            identifiers of containers. All container must be the same type
     * @return list groups contain the exportable fields
     */
    public List<UiExportableGroup> getExportableFields(
            final UiSession pSession, final List<String> pContainerIds);

    /**
     * Execute command and populate {@link OutputStream}.
     * 
     * @param pSession
     *            current user session
     * @param pId
     *            Command to execute Id
     * @param pOutputStream
     *            The stream to populate
     */
    public void getExportedData(final UiSession pSession, final String pId,
            final OutputStream pOutputStream);

    /**
     * Import product
     * 
     * @param pSession
     *            Current user session.
     * @param pInputStream
     *            Product data as {@link InputStream}
     * @throws SchemaValidationException
     *             When {@link InputStream} does not match the schema file.
     * @throws ImportException
     *             When Importation fails.
     */
    public void importProduct(final UiSession pSession,
            final InputStream pInputStream) throws SchemaValidationException,
        ImportException;

    /**
     * Get the maximum exportable sheets allowed.
     * 
     * @return the max allowed exportable sheets
     */
    public int getMaxExportableSheets();
}
