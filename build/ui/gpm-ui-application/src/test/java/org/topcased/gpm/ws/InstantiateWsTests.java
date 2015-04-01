/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ws;

import org.topcased.gpm.resetinstance.ResetInstance;

/**
 * Instantiate for WebServicesTests
 * 
 * @author tpanuel
 */
public class InstantiateWsTests {
    /**
     * Run InstantiateWsTests
     * 
     * @param pArgs
     *            The arguments
     */
    public static void main(String[] pArgs) {
        new ResetInstance().execute("tests_db.properties");
    }
}