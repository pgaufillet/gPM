/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available 
 * under the terms of the Lesser Gnu Public License (LGPL)which 
 * accompanies this distribution, and is available 
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Anne Haugommard(Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.report.service;

import java.io.OutputStream;
import java.util.List;
import java.util.Locale;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.topcased.gpm.business.exception.AuthorizationException;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.extensions.service.Context;
import org.topcased.gpm.business.report.ReportModelData;
import org.topcased.gpm.business.sheet.export.service.SheetExportFormat;
import org.topcased.gpm.domain.export.ExportType;
import org.topcased.gpm.domain.export.ExportTypeEnum;

/**
 * ReportingService
 * 
 * @author ahaugomm
 */
@Transactional(propagation = Propagation.REQUIRED)
public interface ReportingService {

    /** Extension for the compiled jasper report file */
    public static final String JASPER_EXTENSION = ".jasper";

    /**
     * PARAMETERS PUT IN THE REPORT : - ROLE_TOKEN - PROCESS - USER_TOKEN -
     * USER_LOGIN - USER_ROLE - USER_NAME - USER_EMAIL
     */
    public static final String PARAM_ROLE_TOKEN = "ROLE_TOKEN";

    public static final String PARAM_USER_TOKEN = "USER_TOKEN";

    public static final String PARAM_USER_LOGIN = "USER_LOGIN";

    public static final String PARAM_USER_ROLE = "USER_ROLE";

    public static final String PARAM_USER_NAME = "USER_NAME";

    public static final String PARAM_USER_EMAIL = "USER_EMAIL";

    public static final String PARAM_PROCESS = "PROCESS";

    /**
     * Export a sheet list into an outputstream
     * 
     * @param pRoleToken
     *            The roleToken
     * @param pProcessName
     *            The business process name
     * @param pOutputStream
     *            The outpustream
     * @param pSheetIds
     *            The list of sheet ids
     * @param pExportFormat
     *            The export format (XML, PDF, or XLS)
     * @param pLocale
     *            The user Locale
     * @param pReportModelData
     *            The report model
     * @param pContext
     *            Context
     * @throws AuthorizationException
     *             Throws a Authorization Exception when you try to export too
     *             many sheets compared to the max exportable sheets
     *             configuration.
     * @throws GDMException
     *             Throws a GDM Exception in case of IOException or JRException
     */
    public void generateReport(String pRoleToken, String pProcessName,
            OutputStream pOutputStream, List<String> pSheetIds,
            SheetExportFormat pExportFormat, Locale pLocale,
            ReportModelData pReportModelData, Context pContext)
        throws AuthorizationException, GDMException;

    /**
     * Get all the report models compatibles with the given list of values
     * containers IDs and the given export type.
     * 
     * @param pRoleToken
     *            Role token (use for i18nService)
     * @param pContainerIds
     *            the list of values containers ID
     * @param pExportType
     *            the export type
     * @return a list of report model data
     */
    public List<ReportModelData> getCompatibleModels(final String pRoleToken,
            String[] pContainerIds, ExportType pExportType);

    /**
     * Create a report model in DB.
     * 
     * @param pRoleToken
     *            the role token
     * @param pReportModelData
     *            the report model data
     * @return the ID of the newly created report model.
     */
    public String createReportModel(String pRoleToken,
            ReportModelData pReportModelData);

    /**
     * Update a report model in DB.
     * 
     * @param pRoleToken
     *            the role token
     * @param pReportModelData
     *            the report model data
     */
    public void updateReportModel(String pRoleToken,
            ReportModelData pReportModelData);

    /**
     * DElete a report model from DB.
     * 
     * @param pRoleToken
     *            the role token
     * @param pReportModelData
     *            the report model data
     */
    public void deleteReportModel(String pRoleToken,
            ReportModelData pReportModelData);

    /**
     * Get an ExportType object from its name
     * 
     * @param pType
     *            the type (as a string)
     * @return an ExportType object corresponding to this type.
     */
    public ExportType getExportType(String pType);

    /**
     * Get an ExportType object from its enumeration value
     * 
     * @param pType
     *            the type (as an Enum value)
     * @return an ExportType object corresponding to this type.
     */
    public ExportType getExportType(ExportTypeEnum pType);

    /**
     * Get a report model from its name
     * 
     * @param pReportName
     *            the report name
     * @param pProcessName
     *            the business process name
     * @return the report model data
     */
    public ReportModelData getReportModel(String pReportName,
            String pProcessName);

    /**
     * Get a report model from its name
     * 
     * @param pRoleToken
     *            Role token (use for i18nService)
     * @param pReportName
     *            the report name
     * @param pProcessName
     *            the business process name
     * @return the report model data
     */
    public ReportModelData getReportModel(final String pRoleToken,
            String pReportName, String pProcessName);

    /**
     * Create or update a report model
     * 
     * @param pRoleToken
     *            role token
     * @param pProcessName
     *            process name
     * @param pReportModelData
     *            report model
     */
    public void createOrUpdate(String pRoleToken, String pProcessName,
            ReportModelData pReportModelData);
}
