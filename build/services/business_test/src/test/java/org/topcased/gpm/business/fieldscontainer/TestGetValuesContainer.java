/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.fieldscontainer;

import java.util.List;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.fieldscontainer.service.FieldsContainerService;

/**
 * TestGetValuesContainer
 * 
 * @author mkargbo
 */
public class TestGetValuesContainer extends AbstractBusinessServiceTestCase {

    private FieldsContainerService fieldsContainerService;

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.AbstractBusinessServiceTestCase#setUp()
     */
    public void setUp() {
        super.setUp();
        fieldsContainerService = serviceLocator.getFieldsContainerService();
    }

    private static final String TYPE_NAME = GpmTestValues.SHEETLINK_CAT_CAT;

    private static final int EXPECTED_NUMBER = 4;

    /**
     * Get values container of the type 'Cat-Cat'. There are 4.
     */
    public void testGetValuesContainerId() {
        String lFieldsContainerId =
                fieldsContainerService.getFieldsContainerId(normalRoleToken,
                        TYPE_NAME);
        List<String> lValuesContainer =
                fieldsContainerService.getValuesContainerId(normalRoleToken,
                        lFieldsContainerId);
        assertEquals(EXPECTED_NUMBER, lValuesContainer.size());
    }
}