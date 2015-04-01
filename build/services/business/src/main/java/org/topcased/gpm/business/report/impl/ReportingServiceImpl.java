/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Anne Haugommard(Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.report.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JExcelApiExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.query.JRXPathQueryExecuterFactory;
import net.sf.jasperreports.engine.util.JRLoader;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.ServiceImplBase;
import org.topcased.gpm.business.authorization.service.EndUserData;
import org.topcased.gpm.business.exception.AuthorizationException;
import org.topcased.gpm.business.exception.ExpectedException;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.exception.InvalidNameException;
import org.topcased.gpm.business.extensions.service.Context;
import org.topcased.gpm.business.extensions.service.ExtensionPointParameters;
import org.topcased.gpm.business.report.ReportModelData;
import org.topcased.gpm.business.report.service.ReportingService;
import org.topcased.gpm.business.sheet.export.service.SheetExportFormat;
import org.topcased.gpm.domain.export.ExportType;
import org.topcased.gpm.domain.export.ExportTypeDao;
import org.topcased.gpm.domain.export.ExportTypeEnum;
import org.topcased.gpm.domain.export.ReportModel;
import org.topcased.gpm.domain.export.ReportModelDao;
import org.topcased.gpm.domain.extensions.ExtensionsContainerDao;
import org.topcased.gpm.domain.fields.FieldsContainer;
import org.w3c.dom.Document;

/**
 * ReportingServiceImpl
 * 
 * @author ahaugomm
 */
public class ReportingServiceImpl extends ServiceImplBase implements
        ReportingService {

    /** The report model dao */
    private ReportModelDao reportModelDao;

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.report.service.ReportingService#generateReport(java.lang.String,
     *      java.lang.String, java.io.OutputStream, java.util.List,
     *      org.topcased.gpm.business.sheet.export.service.SheetExportFormat,
     *      java.util.Locale, org.topcased.gpm.business.report.ReportModelData)
     */
    @Override
    public void generateReport(String pRoleToken, String pProcessName,
            OutputStream pOutputStream, List<String> pSheetIds,
            SheetExportFormat pExportFormat, Locale pLocale,
            ReportModelData pReportModelData, Context pContext)
        throws AuthorizationException, GDMException {

        // The report shall not be null
        if (pReportModelData == null) {
            throw new GDMException("Null report model");
        }

        // Control the max exportable sheets
        int lMaxExportableSheets =
                getSheetExportService().getMaxExportableSheets();
        if (pSheetIds.size() > lMaxExportableSheets) {
            throw new AuthorizationException(
                    "search.request.execute.export.limit.error");
        }

        Document lXMLDocument = null;
        // getSerializationService().serializeSheets(pSheetIds, lOutputStream);
        try {
            final Context lNewContext = Context.createContext(pContext);

            lNewContext.put(ExtensionPointParameters.REPORT_MODEL_NAME,
                    pReportModelData.getName());

            DocumentBuilder lDocumentBuilder =
                    DocumentBuilderFactory.newInstance().newDocumentBuilder();
            lXMLDocument = lDocumentBuilder.newDocument();

            getSerializationService().serializeSheets(pRoleToken, pSheetIds,
                    lXMLDocument, lNewContext);

        }
        catch (ParserConfigurationException e) {
            throw new GDMException("", e);
        }

        try {
            generateReportInXMLDocument(pRoleToken, pProcessName,
                    pOutputStream, pExportFormat, pLocale, pReportModelData,
                    lXMLDocument);
        }
        catch (IOException e) {
            throw new GDMException("IO Error", e);
        }
    }

    /**
     * Export a sheet list from an XML document to an outputstream
     * 
     * @param pRoleToken
     *            The roleToken
     * @param pProcessName
     *            The business process name
     * @param pOutputStream
     *            The outpustream
     * @param pExportFormat
     *            The export format (XLS, PDF).
     * @param pLocale
     *            The user Locale
     * @param pReportModelData
     *            The report model structure.
     * @param pXMLDocument
     *            The XML document
     */
    private void generateReportInXMLDocument(String pRoleToken,
            String pProcessName, OutputStream pOutputStream,
            SheetExportFormat pExportFormat, Locale pLocale,
            ReportModelData pReportModelData, Document pXMLDocument)
        throws IOException {

        // Check that the export format is one of the model export formats
        if (!isAValidFormat(pExportFormat, pReportModelData.getExportTypes())) {
            throw new GDMException("Invalid export format : " + pExportFormat
                    + " with model :" + pReportModelData.getName());
        }

        // Write the XML file
        Map<String, Object> lParametersMap = new HashMap<String, Object>();

        // set the default locale to XML_LOCALE to prevent a NullPointerException when log level is set to DEBUG
        lParametersMap.put(JRXPathQueryExecuterFactory.XML_LOCALE, Locale.getDefault());

        // put the configuration parameters
        lParametersMap.put(JRXPathQueryExecuterFactory.XML_DATE_PATTERN,
                "yyyy-MM-dd");
        String lResourceFileName =
                pReportModelData.getPath().substring(
                        0,
                        pReportModelData.getPath().length()
                                - JASPER_EXTENSION.length())
                        + "_" + pLocale.toString();

        try {
            ResourceBundle lResourceBundle =
                    ResourceBundle.getBundle(lResourceFileName);
            lParametersMap.put(JRParameter.REPORT_LOCALE, pLocale);
            lParametersMap.put(JRParameter.REPORT_RESOURCE_BUNDLE,
                    lResourceBundle);
        }
        catch (MissingResourceException e) {
            // do Nothing
        }

        lParametersMap.put(
                JRXPathQueryExecuterFactory.PARAMETER_XML_DATA_DOCUMENT,
                pXMLDocument);

        String lUserToken =
                getAuthService().getUserSessionFromRoleSession(pRoleToken);
        EndUserData lEndUserData =
                getAuthService().getLoggedUserData(lUserToken, true);

        // Put the report parameters
        lParametersMap.put(PARAM_ROLE_TOKEN, pRoleToken);
        lParametersMap.put(PARAM_PROCESS, pProcessName);
        lParametersMap.put(PARAM_USER_TOKEN, lUserToken);
        lParametersMap.put(PARAM_USER_ROLE,
                getAuthService().getRole(pRoleToken).getName());
        lParametersMap.put(PARAM_USER_NAME, lEndUserData.getName());
        lParametersMap.put(PARAM_USER_LOGIN, lEndUserData.getLogin());
        lParametersMap.put(PARAM_USER_EMAIL, lEndUserData.getMailAddr());

        JasperReport lCompiledReport;
        try {
            lCompiledReport =
                    (JasperReport) JRLoader.loadObjectFromLocation(pReportModelData.getPath());
        }
        catch (JRException e) {
            throw new ExpectedException("Cannot load compiled Jasper report '"
                    + pReportModelData.getPath() + "'.", e);
        }

        // fill the report with datasource and parameters
        JasperPrint lJasperPrint;
        try {
            lJasperPrint =
                    JasperFillManager.fillReport(lCompiledReport,
                            lParametersMap);
        }
        catch (JRException e) {
            throw new GDMException("Cannot fill Jasper report '"
                    + pReportModelData.getPath() + "'.", e);
        }
        catch (MissingResourceException e) {
            throw new GDMException("Cannot fill Jasper report '"
                    + pReportModelData.getPath() + "'.", e);
        }

        exportFile(pOutputStream, pReportModelData, pExportFormat, lJasperPrint);
    }

    /**
     * Get the PDF of XLS file as a result of the Jasper export as an
     * outputstream
     * 
     * @param pOutputStream
     *            The outputstream
     * @param pReportModelData
     *            The report model structure.
     * @param pExportFormat
     *            The export format (XLS, PDF).
     * @param pJasperPrint
     *            The JasperPrint object, containing the Jasper template filled.
     */
    private void exportFile(OutputStream pOutputStream,
            ReportModelData pReportModelData, SheetExportFormat pExportFormat,
            JasperPrint pJasperPrint) throws IOException {

        switch (pExportFormat) {
            case PDF:
                JRPdfExporter lPDFExporter = new JRPdfExporter();
                lPDFExporter.setParameter(JRPdfExporterParameter.JASPER_PRINT,
                        pJasperPrint);
                lPDFExporter.setParameter(JRPdfExporterParameter.OUTPUT_STREAM,
                        pOutputStream);
                try {
                    lPDFExporter.exportReport();
                }
                catch (JRException e) {
                    throw new GDMException("Cannot export PDF report for '"
                            + pReportModelData.getPath() + "'.", e);
                }
                break;

            case EXCEL:
                JExcelApiExporter lXLSExporter = new JExcelApiExporter();
                lXLSExporter.setParameter(JRExporterParameter.JASPER_PRINT,
                        pJasperPrint);
                lXLSExporter.setParameter(JRExporterParameter.OUTPUT_STREAM,
                        pOutputStream);
                lXLSExporter.setParameter(
                        JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND,
                        Boolean.FALSE);
                try {
                    lXLSExporter.exportReport();
                }
                catch (JRException e) {
                    throw new GDMException("Cannot export XLS report '"
                            + pReportModelData.getPath() + "'.", e);
                }
                break;

            default:
                throw new GDMException("Unknown format : " + pExportFormat);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.report.service.ReportingService#getCompatibleModels(java.lang.String,
     *      java.lang.String[], org.topcased.gpm.domain.export.ExportType)
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<ReportModelData> getCompatibleModels(final String pRoleToken,
            String[] pContainerIds, ExportType pExportType) {
        if (pExportType == null) {
            throw new GDMException("Invalid export type : " + null);
        }
        if (pContainerIds == null) {
            throw new GDMException("Invalid parameter for containersIds");
        }

        List<ReportModel> lReportModels =
                getReportModelDao().getCompatibleReportModels(pContainerIds,
                        pExportType);
        List<ReportModelData> lReportModelDatas =
                new ArrayList<ReportModelData>(lReportModels.size());
        for (ReportModel lReportModel : lReportModels) {
            String[] lFieldsContainerIds =
                    new String[lReportModel.getFieldsContainers().size()];
            int i = 0;
            for (FieldsContainer lFieldsContainer : lReportModel.getFieldsContainers()) {
                lFieldsContainerIds[i] = lFieldsContainer.getId();
                i++;
            }
            String[] lExportTypes =
                    new String[lReportModel.getExportTypes().size()];
            i = 0;
            for (ExportType lExportType : lReportModel.getExportTypes()) {
                lExportTypes[i] = lExportType.getType().getValue();
                i++;
            }

            String lDescription = lReportModel.getDescription();
            if ((StringUtils.isNotBlank(lReportModel.getDescription()))
                    && (StringUtils.isNotBlank(pRoleToken))) {
                //For upward compatibility.
                //Previous method signature doesn't have roletoken parameter. 
                lDescription =
                        getI18nService().getValueForUser(pRoleToken,
                                lReportModel.getDescription());
            }

            lReportModelDatas.add(new ReportModelData(lReportModel.getName(),
                    lDescription, lReportModel.getPath(), lFieldsContainerIds,
                    lExportTypes, lReportModel.getId()));
        }
        return lReportModelDatas;
    }

    /**
     * Check if the given sheet export format is contained in the list of valid
     * formats given
     * 
     * @param pSheetExportFormat
     *            the sheet export format
     * @param pValidFormats
     *            the valid formats
     * @return true if the format is valid, false elsewhere.
     */
    private boolean isAValidFormat(SheetExportFormat pSheetExportFormat,
            String[] pValidFormats) {
        String lExportFormat = null;

        if (pSheetExportFormat.equals(SheetExportFormat.EXCEL)) {
            lExportFormat = ExportTypeEnum.XLS.getValue();
        }
        else if (pSheetExportFormat.equals(SheetExportFormat.PDF)) {
            lExportFormat = ExportTypeEnum.PDF.getValue();
        }
        else {
            throw new GDMException("Invalid export format : "
                    + pSheetExportFormat);
        }
        return (ArrayUtils.contains(pValidFormats, lExportFormat));
    }

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
            ReportModelData pReportModelData) {

        // Check that the roleToken is an admin role token
        if (!getAuthService().hasGlobalAdminRole(pRoleToken)) {
            throw new AuthorizationException(
                    "Unsufficient rights to create a report model.");
        }

        // Check that there is at least one export type
        if (pReportModelData.getExportTypes() == null
                || pReportModelData.getExportTypes().length < 1) {
            throw new GDMException("No export type defined for this model.");
        }

        // Check that there is at least 1 container
        if (pReportModelData.getFieldsContainerIds() == null
                || pReportModelData.getFieldsContainerIds().length < 1) {
            throw new GDMException("No sheet types associated to this model.");
        }

        // Check the fieldsContainer
        String lProcessName = null;
        for (String lContainerID : pReportModelData.getFieldsContainerIds()) {
            if (lProcessName == null) {
                lProcessName =
                        getFieldsContainer(lContainerID).getBusinessProcess().getName();
            }
            else {
                if (!lProcessName.equals(getFieldsContainer(lContainerID)
                        .getBusinessProcess().getName())) {
                    throw new org.topcased.gpm.business.exception.GDMException(
                            "The fields containers do not belong to a unique Business Process.");
                }
            }
        }

        // Check that the report model name is not used in the process.
        ReportModelData lReportModelData =
                getReportModel(pRoleToken, pReportModelData.getName(),
                        lProcessName);
        if (lReportModelData != null) {
            throw new InvalidNameException(
                    "The name '"
                            + lReportModelData.getName()
                            + "' is already used by another report in business process '"
                            + lProcessName + "'.");
        }

        ReportModel lReportModel = ReportModel.newInstance();
        lReportModel.setName(pReportModelData.getName());
        lReportModel.setDescription(pReportModelData.getDescription());
        lReportModel.setPath(pReportModelData.getPath());
        for (String lType : pReportModelData.getExportTypes()) {
            ExportType lExportType =
                    getExportType(ExportTypeEnum.fromString(lType));
            if (lExportType == null) {
                lExportType = ExportType.newInstance();
                lExportType.setType(ExportTypeEnum.fromString(lType));
                getExportTypeDao().create(lExportType);
            }
            lReportModel.addToExportTypeList(lExportType);
        }
        for (String lContainerID : pReportModelData.getFieldsContainerIds()) {
            lReportModel.addToFieldsContainerList(getFieldsContainer(lContainerID));
        }
        getReportModelDao().create(lReportModel);
        pReportModelData.setId(lReportModel.getId());

        return lReportModel.getId();
    }

    /**
     * Update a report model in DB.
     * 
     * @param pRoleToken
     *            the role token
     * @param pReportModelData
     *            the report model data
     */
    public void updateReportModel(String pRoleToken,
            ReportModelData pReportModelData) {
        // Check that the roleToken is an admin role token
        if (!getAuthService().hasGlobalAdminRole(pRoleToken)) {
            throw new AuthorizationException(
                    "Unsufficient rights to create a report model.");
        }

        // Check that the report model can be updated.
        if (pReportModelData.getId() == null) {
            throw new org.topcased.gpm.business.exception.InvalidIdentifierException(
                    "The report model identifier cannot be null before update.");
        }

        ReportModel lReportModel =
                getReportModelDao().load(pReportModelData.getId());
        lReportModel.setName(pReportModelData.getName());
        lReportModel.setDescription(pReportModelData.getDescription());
        lReportModel.setPath(pReportModelData.getPath());
        lReportModel.getExportTypes().clear();
        for (String lType : pReportModelData.getExportTypes()) {
            ExportType lExportType =
                    getExportTypeDao().getExportType(
                            ExportTypeEnum.fromString(lType));
            if (lExportType == null) {
                lExportType = ExportType.newInstance();
                lExportType.setType(ExportTypeEnum.fromString(lType));
                getExportTypeDao().create(lExportType);
            }
            lReportModel.addToExportTypeList(lExportType);
        }
        lReportModel.getFieldsContainers().clear();

        for (String lContainerID : pReportModelData.getFieldsContainerIds()) {
            lReportModel.addToFieldsContainerList(getFieldsContainer(lContainerID));
        }
    }

    /**
     * Delete a report model in DB.
     * 
     * @param pRoleToken
     *            the role token
     * @param pReportModelData
     *            the report model data
     */
    public void deleteReportModel(String pRoleToken,
            ReportModelData pReportModelData) {

        // Check that the roleToken is an admin role token
        if (!getAuthService().hasGlobalAdminRole(pRoleToken)) {
            throw new AuthorizationException(
                    "Unsufficient rights to create a report model.");
        }

        if (pReportModelData.getId() == null) {
            throw new org.topcased.gpm.business.exception.InvalidIdentifierException(
                    "The report model identifier cannot be null before deletion.");
        }

        ReportModel lReportModel =
                getReportModelDao().load(pReportModelData.getId());
        getReportModelDao().remove(lReportModel);

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.report.service.ReportingService#getReportModel(java.lang.String,
     *      java.lang.String)
     */
    public ReportModelData getReportModel(String pReportName,
            String pProcessName) {
        return getReportModel(null, pReportName, pProcessName);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.report.service.ReportingService#getReportModel(java.lang.String,
     *      java.lang.String)
     */
    public ReportModelData getReportModel(final String pRoleToken,
            String pReportName, String pProcessName) {

        ReportModel lReportModel =
                getReportModelDao().getReportModel(pReportName, pProcessName);

        if (lReportModel == null) {
            return null;
        }

        // Create report model data from DAO ReportModel
        ReportModelData lReportModelData = new ReportModelData();
        lReportModelData.setId(lReportModel.getId());

        if ((StringUtils.isNotBlank(lReportModel.getDescription()))
                && (StringUtils.isNotBlank(pRoleToken))) {
            //For upward compatibility. Previous method signature doesn't have roletoken parameter. 
            lReportModelData.setDescription(getI18nService().getValueForUser(
                    pRoleToken, lReportModel.getDescription()));
        }
        else {
            lReportModelData.setDescription(lReportModel.getDescription());
        }
        lReportModelData.setName(lReportModel.getName());
        lReportModelData.setPath(lReportModel.getPath());
        String[] lFieldsContainerIds =
                new String[lReportModel.getFieldsContainers().size()];
        int i = 0;
        for (FieldsContainer lContainer : lReportModel.getFieldsContainers()) {
            lFieldsContainerIds[i] = lContainer.getId();
            i++;
        }
        lReportModelData.setFieldsContainerIds(lFieldsContainerIds);
        String[] lExportTypes =
                new String[lReportModel.getExportTypes().size()];
        i = 0;
        for (ExportType lExportType : lReportModel.getExportTypes()) {
            lExportTypes[i] = lExportType.getType().getValue();
            i++;
        }
        lReportModelData.setExportTypes(lExportTypes);

        return lReportModelData;

    }

    /**
     * Get an ExportType object from a String
     * 
     * @param pType
     *            the type (as a string)
     * @return an ExportType object corresponding to this type.
     */
    public ExportType getExportType(String pType) {
        ExportType lExportType =
                getExportTypeDao().getExportType(
                        ExportTypeEnum.fromString(pType));
        if (lExportType == null) {
            lExportType = ExportType.newInstance();
            lExportType.setType(ExportTypeEnum.fromString(pType));
            getExportTypeDao().create(lExportType);
        }
        return lExportType;
    }

    /**
     * Get an ExportType object from its enumeration value
     * 
     * @param pType
     *            the type (as an Enum value)
     * @return an ExportType object corresponding to this type.
     */
    public ExportType getExportType(ExportTypeEnum pType) {
        ExportType lExportType = getExportTypeDao().getExportType(pType);
        if (lExportType == null) {
            lExportType = ExportType.newInstance();
            lExportType.setType(pType);
            getExportTypeDao().create(lExportType);
        }
        return lExportType;
    }

    /** The exportTypeDao */
    private ExportTypeDao exportTypeDao;

    /**
     * getExportTypeDao
     * 
     * @return the ExportTypeDao
     */
    public ExportTypeDao getExportTypeDao() {
        return exportTypeDao;
    }

    /**
     * setExportTypeDao
     * 
     * @param pExportTypeDao
     *            the ExportTypeDao to set
     */
    public void setExportTypeDao(ExportTypeDao pExportTypeDao) {
        exportTypeDao = pExportTypeDao;
    }

    /**
     * get reportModelDao
     * 
     * @return the reportModelDao
     */
    public ReportModelDao getReportModelDao() {
        return reportModelDao;
    }

    /**
     * set reportModelDao
     * 
     * @param pReportModelDao
     *            the reportModelDao to set
     */
    public void setReportModelDao(ReportModelDao pReportModelDao) {
        reportModelDao = pReportModelDao;
    }

    private ExtensionsContainerDao extensionsContainerDao;

    /**
     * getExtensionsContainerDao
     * 
     * @return the ExtensionsContainerDao
     */
    @Override
    public ExtensionsContainerDao getExtensionsContainerDao() {
        return extensionsContainerDao;
    }

    /**
     * setExtensionsContainerDao
     * 
     * @param pExtensionsContainerDao
     *            the ExtensionContainerDao to set
     */
    @Override
    public void setExtensionsContainerDao(
            ExtensionsContainerDao pExtensionsContainerDao) {
        extensionsContainerDao = pExtensionsContainerDao;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.report.service.ReportingService#createOrUpdate(java.lang.String,
     *      java.lang.String, org.topcased.gpm.business.report.ReportModelData)
     */
    public void createOrUpdate(String pRoleToken, String pProcessName,
            ReportModelData pReportModelData) {
        ReportModelData lReportModelData =
                getReportModel(pRoleToken, pReportModelData.getName(),
                        pProcessName);
        if (lReportModelData == null) {
            createReportModel(pRoleToken, pReportModelData);
        }
        else {
            pReportModelData.setId(lReportModelData.getId());
            updateReportModel(pRoleToken, pReportModelData);
        }
    }

}
