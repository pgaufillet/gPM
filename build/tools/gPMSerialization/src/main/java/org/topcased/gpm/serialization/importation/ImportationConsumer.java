/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.serialization.importation;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.topcased.gpm.business.ServiceLocator;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.exception.ImportException;
import org.topcased.gpm.business.exception.SchemaValidationException;
import org.topcased.gpm.business.extensions.service.Context;
import org.topcased.gpm.business.importation.ImportProperties;
import org.topcased.gpm.business.importation.ImportProperties.ImportFlag;
import org.topcased.gpm.business.importation.service.ImportService;
import org.topcased.gpm.serialization.common.MigrationConsumer;
import org.topcased.gpm.serialization.common.MigrationOptions;
import org.topcased.gpm.serialization.common.MigrationTaskManager;

/**
 * Importation consumer
 * 
 * @author nveillet
 */
public class ImportationConsumer extends
        MigrationConsumer<ImportationProperties> {

    private ImportService importService;

    /**
     * Constructor
     * 
     * @param pTaskManager
     *            the task manager
     * @param pOptions
     *            the options
     */
    public ImportationConsumer(MigrationTaskManager pTaskManager,
            MigrationOptions pOptions) {
        super(pTaskManager, pOptions, ImportationProperties.class);
        importService = ServiceLocator.instance().getImportService();
    }

    /**
     * {@inheritDoc}
     * 
     * @throws ImportException
     * @throws SchemaValidationException
     * @see org.topcased.gpm.serialization.common.MigrationConsumer#execute(java.lang.Integer)
     */
    @Override
    protected void execute(final ImportationProperties pProperties)
        throws SchemaValidationException, ImportException {

        // Fill import properties
        ImportProperties lImportProperties = new ImportProperties();

        if (getOptions().isImportTypeControl()) {
            lImportProperties.setAllFlags(ImportFlag.ERROR);
            switch (pProperties.getElementType()) {
                case DICTIONARY:
                    lImportProperties.setCategoriesFlag(ImportFlag.ERASE);
                    break;
                case ENVIRONMENT:
                    lImportProperties.setEnvironmentsFlag(ImportFlag.CREATE_OR_UPDATE);
                    break;
                case USER:
                    lImportProperties.setUsersFlag(ImportFlag.ERASE);
                    lImportProperties.setUserRolesFlag(ImportFlag.ERASE);
                    break;
                case FILTER:
                    lImportProperties.setFiltersFlag(ImportFlag.CREATE_OR_UPDATE);
                    break;
                case PRODUCT:
                    lImportProperties.setProductsFlag(ImportFlag.ERASE);
                    break;
                case PRODUCT_LINK:
                    lImportProperties.setProductLinksFlag(ImportFlag.ERASE);
                    break;
                case SHEET:
                    lImportProperties.setSheetsFlag(ImportFlag.ERASE);
                    break;
                case SHEET_LINK:
                    lImportProperties.setSheetLinksFlag(ImportFlag.ERASE);
                    break;
                default:
                    // DO NOTHING
                    break;
            }
        }
        else {
            lImportProperties.setAllFlags(ImportFlag.ERASE);
        }

        // Create execution context
        Context lContext = Context.getEmptyContext();
        lContext.put(Context.GPM_SKIP_EXT_PTS, Boolean.TRUE);

        // Open the file output stream
        InputStream lFileInputStream;
        String lFileName =
                getOptions().getBaseDirectory()
                        + File.separator
                        + getOptions().getElementDirectories().get(
                                pProperties.getElementType()) + File.separator
                        + pProperties.getFileName();

        try {
            lFileInputStream = new FileInputStream(lFileName);
        }
        catch (IOException e) {
            throw new GDMException("Cannot open file '" + lFileName + "'");
        }

        // Import
        importService.importData(getOptions().getRoleToken(), lFileInputStream,
                lImportProperties, lContext);
    }
}
