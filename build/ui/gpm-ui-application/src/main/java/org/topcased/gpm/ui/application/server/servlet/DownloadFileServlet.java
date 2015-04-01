/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.server.servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.orm.hibernate3.HibernateTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.topcased.gpm.business.util.ExportFormat;
import org.topcased.gpm.ui.application.server.command.AbstractCommandActionHandler;
import org.topcased.gpm.ui.application.server.exception.DownloadException;
import org.topcased.gpm.ui.component.shared.util.DownloadParameter;
import org.topcased.gpm.ui.facade.server.FacadeLocator;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.server.exportimport.ExportImportFacade;

import com.google.inject.Singleton;

/**
 * DownloadFileServlet
 * <p>
 * Retrieve the file's content to download peer its identifier.
 * </p>
 * <p>
 * The servlet handle 'GET' method to retrieve elements.
 * <ul>
 * <li>'file_id'</li>
 * <li>'product_name'</li>
 * <li>'type' with value ('type_export' or 'type_attached_file')</li>
 * <li>'format' (use for sheet's export and its optional)</li>
 * </ul>
 * </p>
 * <p>
 * Throw {@link DownloadException} if the identifier cannot be retrieve.
 * </p>
 * 
 * @author mkargbo
 */
@Singleton
public class DownloadFileServlet extends HttpServlet {
    private static final long serialVersionUID = -8166285348337450696L;

    private static final String EXPORT_FILE_NAME = "exportFile";

    private static final String DEFAULT_FILE_NAME = "gpmDownloadedFile";

    private static final String TEXT_MIME_TYPE = "text/plain";

    private static final String PDF_MIME_TYPE = "application/pdf";

    private static final String EXCEL_MIME_TYPE = "application/vnd.ms-excel";

    private static final String DEFAULT_MIME_TYPE = "application/octet-stream";

    /**
     * {@inheritDoc}
     * 
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet(final HttpServletRequest pReq,
            final HttpServletResponse pResp) throws ServletException,
        IOException {
        try {
            // Start transaction
            final HibernateTransactionManager lTransactionManager =
                    (HibernateTransactionManager) FacadeLocator.getContext().getBean(
                            "transactionManager");
            final TransactionStatus lTransactionStatus =
                    lTransactionManager.getTransaction(new DefaultTransactionAttribute(
                            TransactionDefinition.PROPAGATION_REQUIRED));

            try {
                // Get user session
                final String lProductName =
                        pReq.getParameter(DownloadParameter.PRODUCT_NAME.name().toLowerCase());
                final UiSession lSession =
                        AbstractCommandActionHandler.getSession(pReq,
                                lProductName);

                // Get file identifier
                final String lFileId =
                        pReq.getParameter(DownloadParameter.FILE_ID.name().toLowerCase());

                if (StringUtils.isBlank(lFileId)) {
                    throw new DownloadException(
                            DownloadException.MISSING_PARAMETER + " '"
                                    + DownloadParameter.FILE_ID.name() + "'");
                }

                // Get file data
                final String lDownloadType =
                        pReq.getParameter(DownloadParameter.TYPE.name().toLowerCase());
                if (StringUtils.isBlank(lDownloadType)) {
                    throw new DownloadException(
                            DownloadException.MISSING_PARAMETER + " '"
                                    + DownloadParameter.TYPE.name() + "'");
                }

                final ExportImportFacade lExportImportFacade =
                        FacadeLocator.instance().getExportImportFacade();

                // Get file metadata
                final String lFileName;
                final String lFileMimeType;
                // Get file data
                final byte[] lFile;

                switch (DownloadParameter.valueOf(lDownloadType.toUpperCase())) {
                    case TYPE_EXPORT:
                        final String lFormat =
                                pReq.getParameter(DownloadParameter.FORMAT.name().toLowerCase());
                        if (StringUtils.isBlank(lFormat)) {
                            lFileName = EXPORT_FILE_NAME;
                            lFileMimeType = StringUtils.EMPTY;
                        }
                        else {
                            final ExportFormat lExportFormat =
                                    ExportFormat.valueOf(lFormat.toUpperCase());
                            lFileMimeType = getMimeType(lExportFormat);
                            lFileName =
                                    EXPORT_FILE_NAME + "."
                                            + lExportFormat.getExtension();
                        }
                        final ByteArrayOutputStream lOutputStream =
                                new ByteArrayOutputStream();
                        lExportImportFacade.getExportedData(lSession, lFileId,
                                lOutputStream);
                        lFile = lOutputStream.toByteArray();
                        break;
                    case TYPE_ATTACHED_FILE:
                        lFile =
                                lExportImportFacade.getAttachedFile(lSession,
                                        lFileId);

                        final String lFileNameParam =
                                pReq.getParameter(DownloadParameter.FILE_NAME.name().toLowerCase());
                        if (StringUtils.isBlank(lFileNameParam)) {
                            lFileName = DEFAULT_FILE_NAME;
                        }
                        else {
                            lFileName = lFileNameParam;
                        }
                        lFileMimeType =
                                pReq.getParameter(DownloadParameter.FILE_MIME_TYPE.name().toLowerCase());
                        break;
                    default:
                        throw new DownloadException(
                                DownloadException.PARAMETER_VALUE + " '"
                                        + lDownloadType + "'");
                }

                // Configure response
                if (StringUtils.isNotBlank(lFileMimeType)) {
                    pResp.setContentType(lFileMimeType);
                }
                else {
                    pResp.setContentType(DEFAULT_MIME_TYPE);
                }
                setResponseHeader(pResp, lFileName);

                // Write on output stream
                final OutputStream lOs = pResp.getOutputStream();
                lOs.write(lFile);
                lOs.close();

                // Commit
                lTransactionManager.commit(lTransactionStatus);
            }
            // All exception are caught because an exception can restart the application
            catch (Exception lE1) {
                try {
                    lTransactionManager.rollback(lTransactionStatus);
                }
                catch (Exception lE2) {
                    // Roll back is less critic than application restarting                
                }
                try {
                    getExceptionResponseHeader(pResp, lE1);
                }
                catch (Exception lE2) {
                    // Wrong header is less critic than application restarting                
                }
            }
        }
        catch (Exception lE0) {
            try {
                getExceptionResponseHeader(pResp, lE0);
            }
            catch (Exception lE1) {
                // Wrong header is less critic than application restarting                
            }
        }
    }

    private void getExceptionResponseHeader(final HttpServletResponse pResp,
            Exception pException) throws IOException {
        // We are in the case where the max selected sheets have been reached. 
        if ("search.request.execute.export.limit.error".equals(pException.getMessage())) {
            final int lMaxExportableSheets =
                    FacadeLocator.instance().getExportImportFacade().getMaxExportableSheets();

            pResp.sendError(HttpServletResponse.SC_FORBIDDEN,
                    "Too many sheets selected. The export limit number is reached (max "
                            + lMaxExportableSheets + ")");
        }
        else {
            setResponseHeader(pResp, "FileNotFound");
        }
    }

    private void setResponseHeader(final HttpServletResponse pResp,
            final String pFileName) {
        pResp.setHeader("Pragma", "public");
        pResp.setHeader("Cache-Control", "max-age=0");
        pResp.setHeader("Content-Disposition", "attachment; filename=\""
                + pFileName + "\"");
    }

    private String getMimeType(final ExportFormat pExportFormat) {
        final String lFileMimeType;
        switch (pExportFormat) {
            case PDF:
                lFileMimeType = PDF_MIME_TYPE;
                break;
            case EXCEL:
                lFileMimeType = EXCEL_MIME_TYPE;
                break;
            case XML:
            case CSV:
                lFileMimeType = TEXT_MIME_TYPE;
                break;
            default:
                lFileMimeType = StringUtils.EMPTY;
        }
        return lFileMimeType;
    }
}