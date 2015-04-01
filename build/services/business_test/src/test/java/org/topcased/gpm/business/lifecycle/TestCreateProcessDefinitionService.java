/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Anne Haugommard(Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.lifecycle;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.exception.InvalidNameException;
import org.topcased.gpm.business.lifecycle.service.LifeCycleService;

/**
 * TestCreateProcessDefinitionService
 * 
 * @author ahaugomm
 */
public class TestCreateProcessDefinitionService extends
        AbstractBusinessServiceTestCase {

    /** The lifecycle service */
    private LifeCycleService lifecycleService;

    /** Process Instance file path */
    private static final String FILE_NAME =
            "lifecycle_resources/processdefinition.xml";

    /**
     * Test the method in a normal way
     */
    public void testNormalCase() {
        lifecycleService = serviceLocator.getLifeCycleService();

        InputStream lInputStream = null;

        try {
            lInputStream = new FileInputStream(FILE_NAME);
        }
        catch (java.io.FileNotFoundException e) {
            lInputStream =
                    this.getClass().getClassLoader().getResourceAsStream(
                            FILE_NAME);
        }

        try {
            lifecycleService.createProcessDefinition(adminRoleToken,
                    getProcessName(), lInputStream);
        }
        catch (Exception e) {
            fail("An exception has been thrown." + e.getMessage());
        }
    }

    /**
     * Test the createProcessDefinition method with bytes
     */
    public void testNormalCaseWithBytes() {
        lifecycleService = serviceLocator.getLifeCycleService();

        InputStream lInputStream = null;

        try {
            lInputStream = new FileInputStream(FILE_NAME);
        }
        catch (java.io.FileNotFoundException e) {
            lInputStream =
                    this.getClass().getClassLoader().getResourceAsStream(
                            FILE_NAME);
        }

        byte[] lBytes = null;

        try {
            lBytes = new byte[lInputStream.available()];
            lInputStream.read(lBytes);
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }

        try {
            lifecycleService.createProcessDefinition(adminRoleToken,
                    getProcessName(), lBytes);
        }
        catch (Exception e) {
            fail("An exception has been thrown." + e.getMessage());
        }
    }

    /**
     * Test the method with a null process name
     */
    public void testWithNullProcessName() {
        lifecycleService = serviceLocator.getLifeCycleService();

        InputStream lInputStream = null;

        try {
            lInputStream = new FileInputStream(FILE_NAME);
        }
        catch (java.io.FileNotFoundException e) {
            lInputStream =
                    this.getClass().getClassLoader().getResourceAsStream(
                            FILE_NAME);
        }

        // Check that a null business process name throws an
        // InvalidNameException
        try {
            lifecycleService.createProcessDefinition(adminRoleToken, null,
                    lInputStream);
            fail("The null process name did not throw any exception.");
        }
        catch (InvalidNameException e) {
        }

    }

}
