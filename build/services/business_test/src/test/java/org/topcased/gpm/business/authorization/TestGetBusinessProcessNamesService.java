/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas Samson (Atos Origin), Sébastien René
 * (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.authorization;

import java.util.Arrays;
import java.util.Collection;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;

/**
 * Tests the method <CODE>getBusinessProcessNames<CODE> of the Authorization
 * Service.
 * 
 * @author nsamson
 */
public class TestGetBusinessProcessNamesService extends
        AbstractBusinessServiceTestCase {

    /** The expected process names. */
    private static final String[] PROCESS_NAMES =
            { GpmTestValues.PROCESS_NAME, "Bugzilla" };

    /** The XML used to instantiate */
    private static final String XML_INSTANCE_TEST =
            "authorization/TestGetBusinessProcessNamesService.xml";

    /**
     * Tests the getBusinessProcessNames method.
     */
    public void testNormalCase() {
        instantiate(PROCESS_NAMES[1], XML_INSTANCE_TEST);

        // Retrieves the process names
        Collection<String> lProcessNames =
                authorizationService.getBusinessProcessNames(adminUserToken);

        assertNotNull("The method getBusinessProcessNames returns null.",
                lProcessNames);

        int lSize = lProcessNames.size();
        int lExpectedSize = PROCESS_NAMES.length;
        assertEquals("The method getBusinessProcessNames returns " + lSize
                + "process names instead of " + lExpectedSize + ".",
                lExpectedSize, lSize);
        assertTrue("", lProcessNames.containsAll(Arrays.asList(PROCESS_NAMES)));
    }
}