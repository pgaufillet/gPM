/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 ******************************************************************/
package org.topcased.gpm.business.extensions;

import java.util.List;
import java.util.Map;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.extensions.service.ExtensionsService;
import org.topcased.gpm.business.serialization.data.InputDataType;

/**
 * Test support of Extension point for InputData type
 * 
 * @author mkargbo
 */
public class TestCreateExtension extends AbstractBusinessServiceTestCase {

    private ExtensionsService extensionsService;

    /** The extension name */
    private static final String EXTENSION_NAME =
            "testExtendedActionWithExtensionPoints_inputDataExtPt";

    /** The extension command name */
    private static final String COMMAND_NAME =
            "testExtendedActionWithExtensionPointsScript";

    /** Number of command for this extension */
    private static final int NUMBER_OF_COMMAND = 1;

    /** The input data type name */
    private static final String INPUT_DATA_TYPE_NAME =
            "testExtendedActionWithExtensionPoints_InputDataTypeTest";

    /** Number of extension point for this input data type */
    private static final int NUMBER_OF_EXTENSION_POINT = 1;

    private static final String XML_INSTANCE =
            "extensions/testExtendedActionWithExtensionPoints.xml";

    /**
     * Tests the method in a normal case.
     */
    public void testNormalCase() {
        instantiate(getProcessName(), XML_INSTANCE);

        extensionsService = serviceLocator.getExtensionsService();

        //Get the input data type
        InputDataType lInputDataType =
                fieldsService.getInputDataType(adminRoleToken,
                        INPUT_DATA_TYPE_NAME, getProcessName());

        assertNotNull("Cannot get input data type called "
                + INPUT_DATA_TYPE_NAME, lInputDataType);

        //Get its extension points
        Map<String, List<String>> lExtensions =
                extensionsService.getAllExtensions(lInputDataType.getId());

        assertNotNull(lExtensions);
        assertEquals(NUMBER_OF_EXTENSION_POINT, lExtensions.size());

        //Get the extension points
        List<String> lExtension = lExtensions.get(EXTENSION_NAME);
        assertNotNull(lExtension);
        assertEquals(NUMBER_OF_COMMAND, lExtension.size());
        assertTrue(lExtension.contains(COMMAND_NAME));
    }
}
