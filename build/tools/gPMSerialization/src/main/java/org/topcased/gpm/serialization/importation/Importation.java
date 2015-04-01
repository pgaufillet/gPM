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

import org.topcased.gpm.serialization.common.MigrationLauncher;
import org.topcased.gpm.serialization.common.MigrationTaskManager;

/**
 * Importation
 * 
 * @author nveillet
 */
public class Importation extends
        MigrationLauncher<ImportationProducer, ImportationConsumer> {

    /**
     * Export main
     * 
     * @param pArgs
     *            Arguments
     */
    public static void main(String[] pArgs) {
        new Importation(pArgs);
    }

    /**
     * Constructor
     * 
     * @param pArgs
     *            Arguments
     */
    public Importation(String[] pArgs) {
        super(pArgs);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.serialization.common.MigrationLauncher#createConsumer(org.topcased.gpm.serialization.common.MigrationTaskManager)
     */
    @Override
    protected ImportationConsumer createConsumer(
            final MigrationTaskManager pTaskManager) {
        return new ImportationConsumer(pTaskManager, getOptions());
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.serialization.common.MigrationLauncher#createProducer(org.topcased.gpm.serialization.common.MigrationTaskManager)
     */
    @Override
    protected ImportationProducer createProducer(
            final MigrationTaskManager pTaskManager) {
        return new ImportationProducer(pTaskManager, getOptions());
    }

}
