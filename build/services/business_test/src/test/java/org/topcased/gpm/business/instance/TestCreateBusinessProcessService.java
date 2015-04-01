/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Anne Haugommard(Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.instance;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.exception.InvalidNameException;
import org.topcased.gpm.business.exception.InvalidValueException;
import org.topcased.gpm.business.instance.service.InstanceService;
import org.topcased.gpm.business.process.service.BusinessProcessData;

/**
 * TestCreateBusinessProcessService
 * 
 * @author ahaugomm
 */
public class TestCreateBusinessProcessService extends
        AbstractBusinessServiceTestCase {

    private static final String BUSINESS_PROCESS_NAME = "Process1";

    private InstanceService instanceService;

    /**
     * Tests the method in normal case.
     */
    public void testNormalCase() {
        instanceService = serviceLocator.getInstanceService();

        BusinessProcessData lBusinessProcessData =
                new BusinessProcessData(BUSINESS_PROCESS_NAME);

        instanceService.createBusinessProcess(adminRoleToken,
                lBusinessProcessData);

        try {
            authorizationService.getProductNames(adminUserToken,
                    BUSINESS_PROCESS_NAME);
        }
        catch (InvalidNameException e) {
            fail("The businessProcess has not been created");
        }
    }

    /**
     * Test when the business process already exist. The method should do
     * nothing.
     */
    public void testAlreadyExistingProcessCase() {
        instanceService = serviceLocator.getInstanceService();

        BusinessProcessData lBusinessProcessData =
                new BusinessProcessData(getProcessName());

        instanceService.createBusinessProcess(adminRoleToken,
                lBusinessProcessData);

        // Check if the business process can still be found.
        try {
            authorizationService.getProductNames(adminUserToken,
                    getProcessName());
        }
        catch (InvalidNameException e) {
            fail("The businessProcess has not been created");
        }
    }

    /**
     * A Business process data with name = NULL
     */
    public void testWithNullBusinessProcessNameCase() {
        instanceService = serviceLocator.getInstanceService();

        BusinessProcessData lBusinessProcessData =
                new BusinessProcessData((String) null);

        try {
            instanceService.createBusinessProcess(adminRoleToken,
                    lBusinessProcessData);
            fail("The businessProcess has not been created");
        }
        catch (Exception e) {
            assertTrue(e instanceof InvalidValueException);
        }
    }

    /**
     * A NULL business process data.
     */
    public void testWithNullBusinessProcessDataCase() {
        instanceService = serviceLocator.getInstanceService();
        BusinessProcessData lBusinessProcessData = null;

        try {
            instanceService.createBusinessProcess(adminRoleToken,
                    lBusinessProcessData);
            fail("The businessProcess has not been created");
        }
        catch (GDMException e) {
        }
    }
}
