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

import java.util.ArrayList;
import java.util.List;

import org.topcased.gpm.business.extensions.service.Context;
import org.topcased.gpm.business.extensions.service.ExtensionPointParameters;
import org.topcased.gpm.business.extensions.service.GDMExtension;

/**
 * InitFields
 * 
 * @author jlouisy
 */
public class ExtensionPointCSDH implements GDMExtension {

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.extensions.service.GDMExtension#execute(org.topcased.gpm.business.extensions.service.Context)
     */
    public boolean execute(Context pContext) {

        // Get values from context
        String lFieldName = pContext.get(ExtensionPointParameters.FIELD_NAME);

        List<String> lChoices = new ArrayList<String>();

        for (int lI = 0; lI < 10; lI++) {
            lChoices.add(lFieldName + " - " + lI);
        }

        pContext.set("choices", lChoices);
        return true;
    }

}
