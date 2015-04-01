/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.serialization.exportation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.topcased.gpm.business.ServiceLocator;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.exportation.ExportProperties;
import org.topcased.gpm.business.exportation.ExportProperties.ExportFlag;
import org.topcased.gpm.business.exportation.service.ExportService;
import org.topcased.gpm.serialization.common.MigrationConsumer;
import org.topcased.gpm.serialization.common.MigrationElementType;
import org.topcased.gpm.serialization.common.MigrationOptions;
import org.topcased.gpm.serialization.common.MigrationTaskManager;
import org.topcased.gpm.util.xml.SchemaValidator;

/**
 * Exportation consumer
 * 
 * @author nveillet
 */
public class ExportationConsumer extends
MigrationConsumer<ExportationProperties> {

    private ExportService exportService;

    /**
     * Constructor
     * 
     * @param pTaskManager
     *            the task manager
     * @param pOptions
     *            the options
     */
    public ExportationConsumer(MigrationTaskManager pTaskManager,
            MigrationOptions pOptions) {
        super(pTaskManager, pOptions, ExportationProperties.class);

        exportService = ServiceLocator.instance().getExportService();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.serialization.common.MigrationConsumer#execute(java.lang.Integer)
     */
    @Override
    protected void execute(final ExportationProperties pProperties) {

        // Fill export properties
        ExportProperties lExportProperties = new ExportProperties();
        lExportProperties.setLimitedElementsId(pProperties.getElementsId().toArray(
                new String[pProperties.getElementsId().size()]));
        lExportProperties.setAllFlags(ExportFlag.NO);

        if (getOptions().isExportAttachedFiles()) {
            lExportProperties.setAttachedFilePath(getOptions().getBaseDirectory()
                    + File.separator
                    + getOptions().getElementDirectories().get(
                            MigrationElementType.ATTACHED_FILE));
        }

        switch (pProperties.getElementType()) {
            case DICTIONARY:
                lExportProperties.setCategoriesFlag(ExportFlag.LIMITED);
                break;
            case ENVIRONMENT:
                lExportProperties.setEnvironmentsFlag(ExportFlag.LIMITED);
                break;
            case USER:
                lExportProperties.setUsersFlag(ExportFlag.LIMITED);
                lExportProperties.setUserRolesFlag(ExportFlag.LIMITED);
                break;
            case FILTER:
                lExportProperties.setFiltersFlag(ExportFlag.LIMITED);
                break;
            case PRODUCT:
                lExportProperties.setProductsFlag(ExportFlag.LIMITED);
                break;
            case PRODUCT_LINK:
                lExportProperties.setProductLinksFlag(ExportFlag.LIMITED);
                break;
            case SHEET:
                lExportProperties.setSheetsFlag(ExportFlag.LIMITED);
                break;
            case SHEET_LINK:
                lExportProperties.setSheetLinksFlag(ExportFlag.LIMITED);
                break;
            default:
                // DO NOTHING
                break;
        }

        // Open the file output stream
        String lFileName =
                getOptions().getBaseDirectory()
                + File.separator
                + getOptions().getElementDirectories().get(
                        pProperties.getElementType()) + File.separator
                        + pProperties.getFileName();

        FileOutputStream lFileOutputStream = null;
        FileInputStream lFis = null;
        try {
            try {
                lFileOutputStream = new FileOutputStream(lFileName);
            }
            catch (IOException e) {
                throw new GDMException("Cannot open file '" + lFileName + "'");
            }

            // Export
            exportService.exportData(getOptions().getRoleToken(),
                    lFileOutputStream, lExportProperties);

            // Check XML
            try {
                SchemaValidator lSchemaValidator = new SchemaValidator();
                lFis = new FileInputStream(lFileName);
                lSchemaValidator.validate(lFis,
                        Boolean.FALSE);
            }
            catch (IOException e) {
                throw new GDMException("Cannot open file '" + lFileName);
            }
        } finally {
            if (lFileOutputStream != null) {
                try {
                    lFileOutputStream.close();
                } catch (IOException lEx) {
                    throw new GDMException("Cannot close file '" + lFileName);
                }
            }
            if (lFis != null) {
                try {
                    lFis.close();
                } catch (IOException lEx) {
                    throw new GDMException("Cannot close file '" + lFileName);
                }
            }
        }
    }
}
