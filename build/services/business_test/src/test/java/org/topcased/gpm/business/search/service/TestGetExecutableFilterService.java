/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.search.service;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;

/**
 * TestGetExecutableFilterService
 * 
 * @author nveillet
 */
public class TestGetExecutableFilterService extends
        AbstractBusinessServiceTestCase {

    private static final String INSTANCE_FILE =
            "search/TestGetExecutableFilterService.xml";

    private static final String FILTER_NAME_NON_EDITABLE_TYPE =
            GpmTestValues.INSTANCE_FILTER_NAMES[1];

    private static final String FILTER_NAME_NON_EXECUTABLE_TYPE =
            GpmTestValues.INSTANCE_FILTER_NAMES[2];

    private static final String FILTER_NAME_NON_EDITABLE_FIELD =
            GpmTestValues.INSTANCE_FILTER_NAMES[3];

    private static final String FILTER_NAME_NON_EXECUTABLE_FIELD =
            GpmTestValues.INSTANCE_FILTER_NAMES[0];

    private static final String FILTER_NAME_NON_EDITABLE_VIRTUAL_FIELD =
            GpmTestValues.INSTANCE_FILTER_NAMES[10];

    private static final String FILTER_NAME_NON_EXECUTABLE_VIRTUAL_FIELD =
            GpmTestValues.INSTANCE_FILTER_NAMES[5];

    /**
     * Test to get filter to valid access when the user don't have edition
     * access on type
     */
    public void testWithNonEditableType() {
        // Instanciate filter access
        instantiate(getProcessName(), INSTANCE_FILE);

        // get filter
        ExecutableFilterData lFilter =
                serviceLocator.getSearchService().getExecutableFilterByName(
                        normalRoleToken, getProcessName(), null, null,
                        FILTER_NAME_NON_EDITABLE_TYPE);

        assertFalse("The filter " + FILTER_NAME_NON_EDITABLE_TYPE
                + " is editable.", lFilter.isEditable());
    }

    /**
     * Test to get filter to valid access when the user don't have execution
     * access on type
     */
    public void testWithNonExecutableType() {
        // Instanciate filter access
        instantiate(getProcessName(), INSTANCE_FILE);

        // get filter
        ExecutableFilterData lFilter =
                serviceLocator.getSearchService().getExecutableFilterByName(
                        normalRoleToken, getProcessName(), null, null,
                        FILTER_NAME_NON_EXECUTABLE_TYPE);

        assertFalse("The filter " + FILTER_NAME_NON_EXECUTABLE_TYPE
                + " is executable.", lFilter.isExecutable());
    }

    /**
     * Test to get filter to valid access when the user don't have edition
     * access on a field
     */
    public void testWithNonEditableField() {
        // Instanciate filter access
        instantiate(getProcessName(), INSTANCE_FILE);

        // get filter
        ExecutableFilterData lFilter =
                serviceLocator.getSearchService().getExecutableFilterByName(
                        normalRoleToken, getProcessName(), null, null,
                        FILTER_NAME_NON_EDITABLE_FIELD);

        assertFalse("The filter " + FILTER_NAME_NON_EDITABLE_FIELD
                + " is executable.", lFilter.isEditable());
    }

    /**
     * Test to get filter to valid access when the user don't have edition
     * access on a virtual field
     */
    public void testWithNonEditableVirtualField() {
        // Instanciate filter access
        instantiate(getProcessName(), INSTANCE_FILE);

        // get filter
        ExecutableFilterData lFilter =
                serviceLocator.getSearchService().getExecutableFilterByName(
                        normalRoleToken, getProcessName(), null, null,
                        FILTER_NAME_NON_EDITABLE_VIRTUAL_FIELD);

        assertFalse("The filter " + FILTER_NAME_NON_EDITABLE_VIRTUAL_FIELD
                + " is executable.", lFilter.isEditable());
    }

    /**
     * Test to get filter to valid access when the user don't have execution
     * access on a field
     */
    public void testWithNonExecutableField() {
        // Instanciate filter access
        instantiate(getProcessName(), INSTANCE_FILE);

        // get filter
        ExecutableFilterData lFilter =
                serviceLocator.getSearchService().getExecutableFilterByName(
                        normalRoleToken, getProcessName(), null, null,
                        FILTER_NAME_NON_EXECUTABLE_FIELD);

        assertFalse("The filter " + FILTER_NAME_NON_EXECUTABLE_FIELD
                + " is executable.", lFilter.isExecutable());
    }

    /**
     * Test to get filter to valid access when the user don't have execution
     * access on a virtual field
     */
    public void testWithNonExecutableVirtualField() {
        // Instanciate filter access
        instantiate(getProcessName(), INSTANCE_FILE);

        // get filter
        ExecutableFilterData lFilter =
                serviceLocator.getSearchService().getExecutableFilterByName(
                        normalRoleToken, getProcessName(), null, null,
                        FILTER_NAME_NON_EXECUTABLE_VIRTUAL_FIELD);

        assertFalse("The filter " + FILTER_NAME_NON_EXECUTABLE_VIRTUAL_FIELD
                + " is executable.", lFilter.isExecutable());
    }
}
