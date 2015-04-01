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
import java.util.Arrays;

import org.topcased.gpm.serialization.common.MigrationElementType;
import org.topcased.gpm.serialization.common.MigrationOptions;
import org.topcased.gpm.serialization.common.MigrationProducer;
import org.topcased.gpm.serialization.common.MigrationStopProperties;
import org.topcased.gpm.serialization.common.MigrationTaskManager;

/**
 * Importation producer
 * 
 * @author nveillet
 */
public class ImportationProducer extends MigrationProducer {

    static {
        ELEMENTS_TYPE.add(MigrationElementType.DICTIONARY);
        ELEMENTS_TYPE.add(MigrationElementType.ENVIRONMENT);
        ELEMENTS_TYPE.add(MigrationElementType.PRODUCT);
        ELEMENTS_TYPE.add(MigrationElementType.PRODUCT_LINK);
        ELEMENTS_TYPE.add(MigrationElementType.USER);
        ELEMENTS_TYPE.add(MigrationElementType.FILTER);
        ELEMENTS_TYPE.add(MigrationElementType.SHEET);
        ELEMENTS_TYPE.add(MigrationElementType.SHEET_LINK);
    }

    /**
     * Constructor
     * 
     * @param pTaskManager
     *            the task manager
     * @param pOptions
     *            the options
     */
    public ImportationProducer(MigrationTaskManager pTaskManager,
            MigrationOptions pOptions) {
        super(pTaskManager, pOptions);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.serialization.common.MigrationProducer#execute()
     */
    @Override
    protected void execute() throws InterruptedException {

        logHeader("IMPORTATION");

        for (MigrationElementType lElementType : ELEMENTS_TYPE) {

            int lPackageNumber = 0;

            if (!isError()) {
                String lDirectoryName =
                        getOptions().getElementDirectories().get(lElementType);

                if (!getOptions().getImportDisableDirectories().contains(
                        lDirectoryName)) {
                    File lDirectory =
                            new File(getOptions().getBaseDirectory()
                                    + File.separator + lDirectoryName);
                    if (lDirectory.exists() && lDirectory.isDirectory()) {
                        File[] lFiles = lDirectory.listFiles();
                        Arrays.sort(lFiles);
                        for (File lFile : lFiles) {

                            if (MigrationElementType.PRODUCT.equals(lElementType)) {
                                waitTaskEnding(lPackageNumber);
                            }

                            if (lFile.isFile()) {
                                ImportationProperties lImportationProperties =
                                        new ImportationProperties();
                                lImportationProperties.setElementType(lElementType);
                                lImportationProperties.setFileName(lFile.getName());
                                getTaskManager().addTask(lImportationProperties);

                                lPackageNumber++;
                            }
                        }
                    }
                }
            }

            waitLogs(lElementType, lPackageNumber);
        }

        getTaskManager().addTask(new MigrationStopProperties());
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.serialization.common.MigrationProducer#isErrorStop()
     */
    @Override
    protected boolean isErrorStop() {
        return true;
    }

    /**
     * Wait the given task is finish
     * 
     * @param pTaskNumber
     *            the task number
     * @throws InterruptedException
     *             thread interrupted exception
     */
    private void waitTaskEnding(final int pTaskNumber)
        throws InterruptedException {
        while (getTaskManager().getTerminatedTasks().size() < pTaskNumber) {
            Thread.sleep(SLEEP_TIME);
        }
    }
}
