/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.serialization;

import org.topcased.gpm.serialization.exportation.Exportation;
import org.topcased.gpm.serialization.importation.Importation;

/**
 * Migrate
 * 
 * @author nveillet
 */
public abstract class Migrate {

    /**
     * Print error
     */
    private static void error() {
        System.err.println("Error: First argument different of "
                + "'Exportation', 'Importation' or 'Serialize'.");
        System.exit(1);
    }

    /**
     * Application entry point.
     * 
     * @param pArgs
     *            Cmd line arguments
     */
    public static void main(String[] pArgs) {

        if (pArgs.length < 1) {
            error();
        }

        String lScript = pArgs[0];

        String[] lArgs = new String[pArgs.length - 1];
        for (int i = 0; i < lArgs.length; i++) {
            lArgs[i] = pArgs[i + 1];
        }

        if ("Exportation".equals(lScript)) {
            Exportation.main(lArgs);
        }
        else if ("Importation".equals(lScript)) {
            Importation.main(lArgs);
        }
        else if ("Serialize".equals(lScript)) {
            Serialize.main(lArgs);
        }
        else {
            error();
        }
    }
}
