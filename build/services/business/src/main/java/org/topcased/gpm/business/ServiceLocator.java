/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.topcased.gpm.business.attributes.service.AttributesService;
import org.topcased.gpm.business.authorization.service.AuthorizationService;
import org.topcased.gpm.business.display.service.DisplayService;
import org.topcased.gpm.business.environment.service.EnvironmentService;
import org.topcased.gpm.business.exportation.service.ExportService;
import org.topcased.gpm.business.extensions.service.ExtensionsService;
import org.topcased.gpm.business.fields.service.FieldsService;
import org.topcased.gpm.business.fieldscontainer.service.FieldsContainerService;
import org.topcased.gpm.business.help.service.HelpService;
import org.topcased.gpm.business.i18n.service.I18nService;
import org.topcased.gpm.business.importation.service.ImportService;
import org.topcased.gpm.business.instance.service.InstanceService;
import org.topcased.gpm.business.lifecycle.service.LifeCycleService;
import org.topcased.gpm.business.link.service.LinkService;
import org.topcased.gpm.business.mail.service.MailService;
import org.topcased.gpm.business.product.service.ProductService;
import org.topcased.gpm.business.report.service.ReportingService;
import org.topcased.gpm.business.revision.service.RevisionService;
import org.topcased.gpm.business.search.service.SearchService;
import org.topcased.gpm.business.serialization.service.SerializationService;
import org.topcased.gpm.business.sheet.export.service.SheetExportService;
import org.topcased.gpm.business.sheet.service.SheetService;
import org.topcased.gpm.business.transformation.service.DataTransformationService;
import org.topcased.gpm.business.values.service.ValuesService;

/**
 * Locates and provides all available application services.
 * 
 * @author llatil
 */

public final class ServiceLocator implements ApplicationContextAware {

    /**
     * Get the shared instance (singleton) of the service locator
     * 
     * @return the shared service locator instance.
     */
    public static synchronized ServiceLocator instance() {
        if (null != staticServiceLocator) {
            return staticServiceLocator;
        }
        return getNewInstance();
    }

    /**
     * Create a new service locator
     * 
     * @return the newly created service locator instance.
     */
    public static synchronized ServiceLocator getNewInstance() {
        // Create a new context locator.
        ContextLocator.getContext();

        // Create the service locator (actually created by Spring)
        staticServiceLocator =
                (ServiceLocator) ContextLocator.getContext().getBean(
                        "serviceLocator");
        return staticServiceLocator;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
     */
    public void setApplicationContext(ApplicationContext pContext)
        throws BeansException {
        ContextLocator.setContext(pContext);
    }

    /**
     * Check if the service locator instance has been initialized
     * 
     * @return True is service locator instance is initialized.
     */
    public static synchronized boolean isInitialized() {
        return staticServiceLocator != null;
    }

    /**
     * Get the Authorization service instance.
     * 
     * @return Authorization service.
     */
    public AuthorizationService getAuthorizationService() {
        return authorizationService;
    }

    /**
     * Set the Authorization service instance.
     * 
     * @param pAuthorizationService
     *            Authorization service.
     */
    public void setAuthorizationService(
            final AuthorizationService pAuthorizationService) {
        authorizationService = pAuthorizationService;
    }

    /**
     * Get the Life Cycle service instance.
     * 
     * @return LifeCycle service.
     */
    public LifeCycleService getLifeCycleService() {
        if (null == lifeCycleService) {
            lifeCycleService =
                    (LifeCycleService) ContextLocator.getContext().getBean(
                            "lifeCycleService");
        }
        return lifeCycleService;
    }

    /**
     * Get the Sheet service instance.
     * 
     * @return Sheet service.
     */
    public SheetService getSheetService() {
        return sheetService;
    }

    /**
     * Set the Sheet service instance.
     * 
     * @param pSheetService
     *            Sheet service.
     * @note This method is used to initialize this bean, and should not be
     *       called by user code.
     */
    public void setSheetService(final SheetService pSheetService) {
        sheetService = pSheetService;
    }

    /**
     * Get the Mail service instance.
     * 
     * @return Returns the mail Service.
     */
    public synchronized MailService getMailService() {
        if (null == mailService) {
            mailService =
                    (MailService) ContextLocator.getContext().getBean(
                            "mailService");
        }
        return mailService;
    }

    /**
     * Get the sheet export service instance.
     * 
     * @return Returns the Service.
     */
    public synchronized SheetExportService getSheetExportService() {
        if (null == sheetExportService) {
            sheetExportService =
                    (SheetExportService) ContextLocator.getContext().getBean(
                            "sheetExportService");
        }
        return sheetExportService;
    }

    /**
     * Get I18N service.
     * 
     * @return Returns the i18nService.
     */
    public I18nService getI18nService() {
        return i18nService;
    }

    /**
     * Set the I18N service
     * 
     * @param pService
     *            The i18nService to set.
     */
    public void setI18nService(final I18nService pService) {
        i18nService = pService;
    }

    /**
     * Get the extensions service
     * 
     * @return Returns the extensionsService.
     */
    public ExtensionsService getExtensionsService() {
        return extensionsService;
    }

    /**
     * Set the extensions service
     * 
     * @param pExtensionsService
     *            The extensionsService to set.
     */
    public void setExtensionsService(final ExtensionsService pExtensionsService) {
        extensionsService = pExtensionsService;
    }

    /**
     * Get the display Group service
     * 
     * @return Returns the DisplayGroupService.
     */
    public DisplayService getDisplayService() {
        return displayService;
    }

    /**
     * Set the display Group service
     * 
     * @param pDisplayGroupService
     *            The DisplayGroupService to set.
     */
    public void setDisplayService(final DisplayService pDisplayGroupService) {
        displayService = pDisplayGroupService;
    }

    /**
     * Get the Fields service instance.
     * 
     * @return Fields service.
     */
    public FieldsService getFieldsService() {
        return fieldsService;
    }

    /**
     * Set the Fields service instance.
     * 
     * @param pFieldsService
     *            Fields service.
     * @note This method is used to initialize this bean, and should not be
     *       called by user code.
     */
    public void setFieldsService(final FieldsService pFieldsService) {
        fieldsService = pFieldsService;
    }

    /**
     * Get the environment service
     * 
     * @return Returns the environmentService.
     */
    public EnvironmentService getEnvironmentService() {
        return environmentService;
    }

    /**
     * Set the environment service
     * 
     * @param pEnvironmentService
     *            The environmentService to set.
     */
    public void setEnvironmentService(
            final EnvironmentService pEnvironmentService) {
        environmentService = pEnvironmentService;
    }

    /**
     * Get the product service
     * 
     * @return Returns the productService.
     */
    public ProductService getProductService() {
        return productService;
    }

    /**
     * Set the product service
     * 
     * @param pProductService
     *            The productService to set.
     * @note This method is normaly called automatically during initialization
     *       and should not be called explicitely.
     */
    public void setProductService(final ProductService pProductService) {
        productService = pProductService;
    }

    /**
     * Get the search service
     * 
     * @return Returns the searchService
     */
    public SearchService getSearchService() {
        return searchService;
    }

    /**
     * set the search service
     * 
     * @param pSearchService
     *            the searchService to set
     */
    public void setSearchService(final SearchService pSearchService) {
        searchService = pSearchService;
    }

    /**
     * Set the Service Locator singleton
     * 
     * @param pServiceLocator
     *            The serviceLocator to set.
     * @note This method is normally called automatically during initialization
     *       and should not be called explicitly.
     */
    public static void setStaticServiceLocator(
            final ServiceLocator pServiceLocator) {
        staticServiceLocator = pServiceLocator;
    }

    /**
     * Get the gPM instance service.
     * 
     * @return Instance service.
     */
    public InstanceService getInstanceService() {
        return instanceService;
    }

    /**
     * Set the gPM instance service.
     * 
     * @param pInstanceService
     *            The instance service
     */
    public void setInstanceService(final InstanceService pInstanceService) {
        instanceService = pInstanceService;
    }

    /**
     * getSerializationService
     * 
     * @return the serializationService
     */
    public SerializationService getSerializationService() {
        return serializationService;
    }

    /**
     * Get the attributes service instance
     * 
     * @return Attributes service
     */
    public AttributesService getAttributesService() {
        return attributesService;
    }

    /**
     * Get the revision service instance
     * 
     * @return Revision service
     */
    public RevisionService getRevisionService() {
        return revisionService;
    }

    /**
     * setSerializationService
     * 
     * @param pSerializationService
     *            the serializationService to set
     */
    public void setSerializationService(
            final SerializationService pSerializationService) {
        serializationService = pSerializationService;
    }

    /**
     * Set the attributes service
     * 
     * @param pAttributesService
     *            The attributes service
     */
    public void setAttributesService(final AttributesService pAttributesService) {
        attributesService = pAttributesService;
    }

    /**
     * Set the revision service
     * 
     * @param pRevisionService
     *            The revision service
     */
    public void setRevisionService(final RevisionService pRevisionService) {
        revisionService = pRevisionService;
    }

    /**
     * get reportingService
     * 
     * @return the reportingService
     */
    public ReportingService getReportingService() {
        return reportingService;
    }

    /**
     * get helpService
     * 
     * @return the helpService
     */
    public HelpService getHelpService() {
        return helpService;
    }

    /**
     * Get link management service
     * 
     * @return the link service
     */
    public final LinkService getLinkService() {
        return linkService;
    }

    /**
     * Set link management service
     * 
     * @param pLinkService
     *            Link service
     */

    public final void setLinkService(LinkService pLinkService) {
        linkService = pLinkService;
    }

    /**
     * set reportingService
     * 
     * @param pReportingService
     *            the reportingService to set
     */
    public void setReportingService(final ReportingService pReportingService) {
        reportingService = pReportingService;
    }

    /**
     * Set the help service
     * 
     * @param pHelpService
     *            the helpService to set
     */
    public void setHelpService(HelpService pHelpService) {
        helpService = pHelpService;
    }

    /**
     * Get the 'export' service
     * 
     * @return Export service interface
     */
    public final ExportService getExportService() {
        return exportService;
    }

    public final void setExportService(ExportService pExportService) {
        exportService = pExportService;
    }

    /**
     * Get the 'import' service
     * 
     * @return Import service interface
     */
    public final ImportService getImportService() {
        return importService;
    }

    public final void setImportService(ImportService pImportService) {
        importService = pImportService;
    }

    public FieldsContainerService getFieldsContainerService() {
        return fieldsContainerService;
    }

    public void setFieldsContainerService(
            FieldsContainerService pFieldsContainerService) {
        fieldsContainerService = pFieldsContainerService;
    }

    /**
     * Getter on the 'data transformation' service
     * 
     * @return Data Transformation service interface
     */
    public DataTransformationService getDataTransformationService() {
        return dataTransformationService;
    }

    /**
     * Setter on the 'data transformation' service
     * 
     * @param pDataTransformationService
     *            The new Data Transformation service
     */
    public void setDataTransformationService(
            DataTransformationService pDataTransformationService) {
        dataTransformationService = pDataTransformationService;
    }

    /**
     * Getter on the 'values' service
     * 
     * @return Values service interface
     */
    public ValuesService getValuesService() {
        return valuesService;
    }

    /**
     * Setter on the 'values' service
     * 
     * @param pValuesService
     *            The new Values service
     */
    public void setValuesService(ValuesService pValuesService) {
        valuesService = pValuesService;
    }

    /** Sheet service */
    private SheetService sheetService;

    /** Link service */
    private LinkService linkService;

    /** Lifecycle service */
    private LifeCycleService lifeCycleService;

    /** Authorization service */
    private AuthorizationService authorizationService;

    /** Mail service */
    private MailService mailService;

    /** Export service */
    private SheetExportService sheetExportService;

    /** Extensions service */
    private ExtensionsService extensionsService;

    /** Display Service. */
    private DisplayService displayService;

    /** Environment service */
    private EnvironmentService environmentService;

    /** The i18nService. */
    private I18nService i18nService;

    /** ProductService */
    private ProductService productService;

    /** Search service */
    private SearchService searchService;

    /** Fields service */
    private FieldsService fieldsService;

    /** Instance service */
    private InstanceService instanceService;

    /** Serialization service */
    private SerializationService serializationService;

    /** Attributes service */
    private AttributesService attributesService;

    /** Revision service */
    private RevisionService revisionService;

    /** Reporting service */
    private ReportingService reportingService;

    /** Help service */
    private HelpService helpService;

    /** Export service */
    private ExportService exportService;

    /** Import service */
    private ImportService importService;

    /** Data Transformation service */
    private DataTransformationService dataTransformationService;

    private FieldsContainerService fieldsContainerService;

    /** Values service */
    private ValuesService valuesService;

    private static ServiceLocator staticServiceLocator;

    /**
     * Private constructor used to prevent instanciation of this class.
     */
    private ServiceLocator() {
        // shouldn't be instantiated
    }
}
