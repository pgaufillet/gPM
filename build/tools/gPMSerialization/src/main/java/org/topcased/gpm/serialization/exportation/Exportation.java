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

import org.topcased.gpm.serialization.common.MigrationLauncher;
import org.topcased.gpm.serialization.common.MigrationTaskManager;

/**
 * Exportation
 * 
 * @author nveillet
 */
public class Exportation extends
        MigrationLauncher<ExportationProducer, ExportationConsumer> {

    /**
     * Export main
     * 
     * @param pArgs
     *            Arguments
     */
    public static void main(String[] pArgs) {
        new Exportation(pArgs);
    }

    /**
     * Constructor
     * 
     * @param pArgs
     *            Arguments
     */
    public Exportation(String[] pArgs) {
        super(pArgs);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.serialization.common.MigrationLauncher#createConsumer(org.topcased.gpm.serialization.common.MigrationTaskManager)
     */
    @Override
    protected ExportationConsumer createConsumer(
            final MigrationTaskManager pTaskManager) {
        return new ExportationConsumer(pTaskManager, getOptions());
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.serialization.common.MigrationLauncher#createProducer(org.topcased.gpm.serialization.common.MigrationTaskManager)
     */
    @Override
    protected ExportationProducer createProducer(
            final MigrationTaskManager pTaskManager) {
        return new ExportationProducer(pTaskManager, getOptions());
    }

}
