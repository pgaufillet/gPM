/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Magali Franchet (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.help;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.help.service.HelpService;

/**
 * TestGetHelpContentUrlService
 * 
 * @author mfranche
 */
public class TestGetHelpContentUrlService extends
        AbstractBusinessServiceTestCase {

    /** The XML used to instantiate with global attribute */
    private static final String XML_INSTANCE_GLOBAL_ATTRIBUTE_TEST =
            "help/instanceWithGlobalAttribute.xml";

    /** The XML used to instantiate with options attribute */
    private static final String XML_INSTANCE_OPTIONS_ATTRIBUTE_TEST =
            "help/instanceWithOptionAttribute.xml";

    /** The new process name. */
    private static final String PROCESS_NAME = "test_process";

    /** Value of the global helpContentUrl attribute */
    private static final String HELP_CONTENT_URL_GLOBAL_ATTRIBUTE_VALUE =
            "http://www.google.fr";

    /** Value of the global helpContentUrl option */
    private static final String HELP_CONTENT_URL_OPTION_VALUE =
            "http://www.test.fr";

    /**
     * Test the method getHelpContentUrl when attribute has not been set in the
     * instance file nor in the configuration file.
     */
    public void testEmptyCase() {
        HelpService lHelpService = serviceLocator.getHelpService();
        String lHelpContentUrl = lHelpService.getHelpContentUrl();

        assertTrue(lHelpContentUrl.length() == 0);
    }

    /**
     * Test the method getHelpContentUrl when attribute is set as global
     * attribute in the instance file.
     */
    public void testInstanceGlobalAttributeCase() {
        instantiate(PROCESS_NAME, XML_INSTANCE_GLOBAL_ATTRIBUTE_TEST);

        HelpService lHelpService = serviceLocator.getHelpService();
        String lHelpContentUrl = lHelpService.getHelpContentUrl();

        assertEquals(HELP_CONTENT_URL_GLOBAL_ATTRIBUTE_VALUE, lHelpContentUrl);
    }

    /**
     * Test the method getHelpContentUrl when attribute is set in the options
     * tag in the instance file.
     */
    public void testInstanceOptionAttributeCase() {
        instantiate(PROCESS_NAME, XML_INSTANCE_OPTIONS_ATTRIBUTE_TEST);

        HelpService lHelpService = serviceLocator.getHelpService();
        String lHelpContentUrl = lHelpService.getHelpContentUrl();

        assertEquals(HELP_CONTENT_URL_OPTION_VALUE, lHelpContentUrl);
    }
}
