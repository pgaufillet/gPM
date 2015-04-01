/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.extensions;

import org.topcased.gpm.business.extensions.service.Context;
import org.topcased.gpm.business.extensions.service.ExtensionsService;
import org.topcased.gpm.business.extensions.service.GDMExtension;
import org.topcased.gpm.business.serialization.data.FieldValueData;
import org.topcased.gpm.business.sheet.impl.CacheableSheet;
import org.topcased.gpm.common.extensions.ResultingScreen;

/**
 * ExtendedAction_CF_018
 */
public class ExtendedActionCF018 implements GDMExtension {

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.extensions.service.GDMExtension#execute(org.topcased.gpm.business.extensions.service.Context)
     */
    public boolean execute(Context pContext) {
        CacheableSheet lCacheableSheet =
                pContext.get("currentEditedSheet", CacheableSheet.class);
        if (lCacheableSheet == null) {
            pContext.set("errorMessage", "Error cannot retrieve the sheet.");
            pContext.set(ExtensionsService.RESULT_SCREEN, ResultingScreen.ERROR);
        }
        else {
            StringBuilder lStringBuilder = new StringBuilder();
            for (FieldValueData lFieldValueData : lCacheableSheet.getAllTopLevelValues()) {
                lStringBuilder.append(lFieldValueData.getName()).append(" = ");
                lStringBuilder.append(lFieldValueData.getValue());
                lStringBuilder.append("\n");
            }
            pContext.set("message", lStringBuilder.toString());
            pContext.set(ExtensionsService.RESULT_SCREEN,
                    ResultingScreen.MESSAGE);
        }
        return true;
    }
}
