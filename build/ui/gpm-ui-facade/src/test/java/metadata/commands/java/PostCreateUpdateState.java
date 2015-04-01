/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Olivier Gehin (Atos Origin)
 ******************************************************************/

package metadata.commands.java;

import org.topcased.gpm.business.ServiceLocator;
import org.topcased.gpm.business.extensions.service.Context;
import org.topcased.gpm.business.extensions.service.ExtensionPointParameters;
import org.topcased.gpm.business.extensions.service.GDMExtension;
import org.topcased.gpm.business.lifecycle.service.LifeCycleService;
import org.topcased.gpm.business.sheet.impl.CacheableSheet;

/**
 * InitFields
 * 
 * @author jlouisy
 */
public class PostCreateUpdateState implements GDMExtension {

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.extensions.service.GDMExtension#execute(org.topcased.gpm.business.extensions.service.Context)
     */
    public boolean execute(Context pContext) {

        //get values from context
        String lRoleToken = pContext.get(ExtensionPointParameters.ROLE_TOKEN);
        CacheableSheet lSheet = pContext.get(ExtensionPointParameters.SHEET);
        ServiceLocator lServiceLocator =
                pContext.get(ExtensionPointParameters.SERVICE_LOCATOR);

        String lTransitionName = "TO CREATE";

        LifeCycleService lLifecycleService =
                lServiceLocator.getLifeCycleService();

        lLifecycleService.performTransition(lRoleToken, lSheet.getId(),
                lTransitionName);

        return true;
    }

}
