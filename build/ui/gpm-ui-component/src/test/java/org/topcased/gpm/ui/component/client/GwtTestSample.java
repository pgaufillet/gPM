/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 ******************************************************************/
package org.topcased.gpm.ui.component.client;

import junit.framework.Assert;

import com.google.gwt.junit.client.GWTTestCase;

public class GwtTestSample extends GWTTestCase {

    private static final String MODULE_NAME =
            "org.topcased.gpm.ui.component.Component";

    public String getModuleName() {
        return MODULE_NAME;
    }

    public void testSomething() {

        // Not much to actually test in this sample app
        // Ideally you would test your Controller here (NOT YOUR UI)
        // (Make calls to RPC services, test client side model objects, test client side logic, etc)
        Assert.assertTrue(true);
    }
}