/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin)
 ******************************************************************/

package org.topcased.gpm.business.authorization;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.exception.AuthorizationException;
import org.topcased.gpm.business.exception.InvalidNameException;

/**
 * @author llatil
 */
public class TestDeleteAcl extends AbstractBusinessServiceTestCase {

    public void testDeleteAcl() {

        boolean lExceptionRaised = false;

        getAuthorizationService().deleteAllAccessControls(adminRoleToken,
                GpmTestValues.PROCESS_NAME);

        try {
            getAuthorizationService().deleteAllAccessControls(normalRoleToken,
                    GpmTestValues.PROCESS_NAME);
        }
        catch (AuthorizationException e) {
            lExceptionRaised = true;
        }
        assertTrue("deleteAllAccessControls() does not check admin privilege",
                lExceptionRaised);

        lExceptionRaised = false;
        try {
            getAuthorizationService().deleteAllAccessControls(adminRoleToken,
                    "INVALID_INSTANCE_NAME");
        }
        catch (InvalidNameException e) {
            lExceptionRaised = true;
        }
        assertTrue(
                "deleteAllAccessControls() does not raise excpetion for an invalid instance name",
                lExceptionRaised);
    }
}
