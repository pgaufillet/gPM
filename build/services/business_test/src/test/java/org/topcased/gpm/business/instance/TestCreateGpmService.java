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
import org.topcased.gpm.business.exception.AuthorizationException;
import org.topcased.gpm.business.instance.service.InstanceService;

/**
 * TestCreateGpmService
 * 
 * @author ahaugomm
 */
public class TestCreateGpmService extends AbstractBusinessServiceTestCase {

    private InstanceService instanceService;

    /**
     * Tests the method in normal case.
     */
    public void testNormalCase() {
        instanceService = serviceLocator.getInstanceService();
        instanceService.createGpm(adminRoleToken);
    }

    /**
     * Tests the method with a user without valid access
     */
    public void testWithoutAdminAccessCase() {
        instanceService = serviceLocator.getInstanceService();
        try {
            instanceService.createGpm(normalRoleToken);
            fail("The current user, without admin role, "
                    + "should not be able to create the gPM object.");
        }
        catch (AuthorizationException e) {
            // Normal behavior
        }
    }
}
