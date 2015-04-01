/***************************************************************
 * Copyright (c) 2012 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Mimoun Mennad (Atos)
 ******************************************************************/
package org.topcased.gpm.business.attributes;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.attributes.service.AttributesService;

/**
 * This class tests the org.topcased.gpm.business getGlobalAttrNames() method
 * from the AttributesService implementation.
 * 
 * @author mmennad
 */
public class TestGetGlobalAttrNames extends AbstractBusinessServiceTestCase {

    // These attributes always exist, whatever attributes have been defined in the instance creation
    private static final String[] GENERAL_STANDARD_NAMES =
            { "authentication", "autolocking", "filterFieldsMaxDepth", "loginCaseSensitive" };

    /**
     * Tests a regular call
     */
    public void testNormalCase() {

        //Instantiation
        AttributesService lAttributesService =
                serviceLocator.getAttributesService();

        //Setup
        String[] lGlobalNames = lAttributesService.getGlobalAttrNames();

        //Check
        assertNotNull(lGlobalNames);
        assertEquals(GENERAL_STANDARD_NAMES.length, lGlobalNames.length);

        for (int i = 0; i < lGlobalNames.length; i++) {
            assertEquals(GENERAL_STANDARD_NAMES[i], lGlobalNames[i]);
        }

    }

}
