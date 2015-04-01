/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 ******************************************************************/
package metadata.commands.java;

import org.topcased.gpm.business.extensions.service.Context;
import org.topcased.gpm.business.extensions.service.ExtensionPointParameters;
import org.topcased.gpm.business.extensions.service.ExtensionsService;
import org.topcased.gpm.business.extensions.service.GDMExtension;
import org.topcased.gpm.common.extensions.ResultingScreen;

public class PrintMessage implements GDMExtension {

    public boolean execute(Context pContext) {
        String lExtensionPointName =
                pContext.get(ExtensionPointParameters.EXTENSION_POINT_NAME);
        pContext.set(ExtensionsService.RESULT_SCREEN, ResultingScreen.MESSAGE);
        pContext.set(ExtensionsService.MESSAGE, lExtensionPointName
                + " has been executed.");
        return true;
    }

}
