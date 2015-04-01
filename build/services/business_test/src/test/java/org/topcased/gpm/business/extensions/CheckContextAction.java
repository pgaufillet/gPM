/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.extensions;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.ServiceLocator;
import org.topcased.gpm.business.authorization.service.AuthorizationService;
import org.topcased.gpm.business.extensions.service.Context;
import org.topcased.gpm.business.extensions.service.GDMExtension;

/**
 * CheckContextAction class
 * 
 * @author llatil
 */
public class CheckContextAction implements GDMExtension {

    /**
     * This method simply checks the context content, to ensure that the default
     * parameters are present in the calling context.
     * 
     * @param pContext the current context
     * @return true
     */
    public boolean execute(Context pContext) {
        String lProcessName = (String) pContext.get("processName");
        String lUserToken = (String) pContext.get("userToken");
        String lRoleToken = (String) pContext.get("roleToken");
        String lExtPointName = (String) pContext.get("extensionPointName");

        String lAdminRoleToken = (String) pContext.get("adminRoleToken");

        ServiceLocator lServiceLocator =
                (ServiceLocator) pContext.get("serviceLocator");
        AuthorizationService lAuthService =
                lServiceLocator.getAuthorizationService();

        if (null == lProcessName) {
            throw new RuntimeException("Process name in context is null !");
        }

        if (!AbstractBusinessServiceTestCase.getProcessName().equals(
                lProcessName)) {
            throw new RuntimeException("Process name in context has not the "
                    + "expected value. '" + lProcessName + "' received while '"
                    + AbstractBusinessServiceTestCase.getProcessName()
                    + "' expected");
        }

        if (null == lUserToken || null == lRoleToken) {
            throw new RuntimeException(
                    "Role / User token is not defined in context");
        }

        if (lExtPointName == null || !lExtPointName.equals("CheckContext")) {
            throw new RuntimeException(
                    "Invalid extension point name in context");
        }

        if (!lAuthService.hasAdminAccess(lAdminRoleToken)) {
            throw new RuntimeException(
                    "The admin role token must have administrator privileges");
        }
        return true;
    }
}
