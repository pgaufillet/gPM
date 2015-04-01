/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.fields;

import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.fields.impl.CacheableFieldsContainer;

/**
 * Test the fields container get identifier method.
 * 
 * @author mkargbo
 */
public class TestGetFieldsContainer extends AbstractBusinessServiceTestCase {

    /** Name of a non confidential type */
    private static final String TYPE_NAME = GpmTestValues.SHEET_TYPE_CAT;

    /**
     * Normal case. Must get an identifier.
     */
    public void testGetFieldsContainerId() {
        String lContainerIdentifier =
                getFieldsContainerService().getFieldsContainerId(
                        adminRoleToken, TYPE_NAME);

        assertFalse("Cannot get the container identifier for container name: "
                + TYPE_NAME, StringUtils.isBlank(lContainerIdentifier));
    }

    /**
     * Try to get a cached fields container which doesn't exist.
     */
    public void testGetCacheableFieldsContainerNotExists() {
        CacheableFieldsContainer lCacheableFieldsContainer =
                fieldsService.getCacheableFieldsContainer(adminRoleToken,
                        "azertyuiop");
        assertNull("The fields container doesn't have to exist",
                lCacheableFieldsContainer);
    }
}
