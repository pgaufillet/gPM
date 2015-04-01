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
import org.topcased.gpm.business.fields.impl.CacheableFieldsContainer;
import org.topcased.gpm.business.fieldscontainer.service.FieldsContainerService;
import org.topcased.gpm.business.serialization.data.ProductType;

/**
 * TestGetFieldsContainerInfo
 * 
 * @author mkargbo
 */
public class TestGetFieldsContainerInfo extends AbstractBusinessServiceTestCase {

    private FieldsContainerService fieldsContainerService;

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.AbstractBusinessServiceTestCase#setUp()
     */
    protected void setUp() {
        super.setUp();
        fieldsContainerService = serviceLocator.getFieldsContainerService();
    }

    private static final String[] EXPECTED_FIELDS_CONTAINER_CC =
            new String[] { "Store" };

    private static final String INSTANCE_FILE_CC =
            "fieldscontainer/testGetFieldsContainerCC.xml";

    /**
     * Only not confidential and fields container that can be create are
     * returned
     */
    public void testConfidentialCreate() {
        instantiate(getProcessName(), INSTANCE_FILE_CC);

        List<CacheableFieldsContainer> lList =
                fieldsContainerService.getFieldsContainer(normalRoleToken,
                        ProductType.class,
                        FieldsContainerService.NOT_CONFIDENTIAL
                                | FieldsContainerService.CREATE);

        String[] lFieldsContainers = new String[lList.size()];
        for (int i = 0; i < lList.size(); i++) {
            lFieldsContainers[i] = lList.get(i).getName();
        }

        assertEqualsUnordered(EXPECTED_FIELDS_CONTAINER_CC, lFieldsContainers);
    }

    private static final String[] EXPECTED_FIELDS_CONTAINER_CUD =
            new String[] { "Store" };

    private static final String INSTANCE_FILE_CUD =
            "fieldscontainer/testGetFieldsContainerCUD.xml";

    /**
     * Only fields container that can be create, update and delete are returned
     */
    public void testCreateUpdateDelete() {
        instantiate(getProcessName(), INSTANCE_FILE_CUD);

        List<CacheableFieldsContainer> lList =
                fieldsContainerService.getFieldsContainer(normalRoleToken,
                        ProductType.class, FieldsContainerService.CREATE
                                | FieldsContainerService.UPDATE
                                | FieldsContainerService.DELETE);

        String[] lFieldsContainers = new String[lList.size()];
        for (int i = 0; i < lList.size(); i++) {
            lFieldsContainers[i] = lList.get(i).getName();
        }
        assertEqualsUnordered(EXPECTED_FIELDS_CONTAINER_CUD, lFieldsContainers);
    }
}
