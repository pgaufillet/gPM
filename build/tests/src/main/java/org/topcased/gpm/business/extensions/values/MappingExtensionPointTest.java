/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.extensions.values;

import org.topcased.gpm.business.extensions.service.Context;
import org.topcased.gpm.business.extensions.service.ExtensionPointParameters;
import org.topcased.gpm.business.extensions.service.GDMExtension;
import org.topcased.gpm.business.values.field.simple.BusinessStringField;

/**
 * TestMappingExtension
 */
public class MappingExtensionPointTest implements GDMExtension {
    public final static String NEW_VALUE = "TEST OK";

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.extensions.service.GDMExtension#execute(org.topcased.gpm.business.extensions.service.Context)
     */
    public boolean execute(Context pContext) {
        final BusinessStringField lSource =
                (BusinessStringField) pContext.get(ExtensionPointParameters.SOURCE_BUSINESS_FIELD);
        final BusinessStringField lDestination =
                (BusinessStringField) pContext.get(ExtensionPointParameters.BUSINESS_FIELD);

        if (lSource.getAsString().equals("Sheet24")) {
            lDestination.setAsString(NEW_VALUE);
        }

        return true;
    }
}
