/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.exportation;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.topcased.gpm.business.ContextLocator;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.authorization.impl.AuthorizationServiceImpl;
import org.topcased.gpm.business.exportation.ExportProperties.ExportFlag;
import org.topcased.gpm.business.serialization.data.User;

/**
 * Tests the user export.
 * 
 * @author tpanuel
 */
public class TestUserExport extends AbstractTestExport<User> {
    private final static Set<String> allIds = new HashSet<String>();

    private final static Set<String> idsWithRoleOn = new HashSet<String>();

    private static boolean init = false;

    private AuthorizationServiceImpl authorizationService;

    /**
     * Create TestUserExport.
     */
    public TestUserExport() {
        super("users", User.class);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.AbstractBusinessServiceTestCase#setUp()
     */
    @Override
    protected void setUp() {
        super.setUp();
        authorizationService =
                (AuthorizationServiceImpl) ContextLocator.getContext().getBean(
                        "authorizationServiceImpl");
        if (!init) {
            // Fill all elements ids
            init(GpmTestValues.USER_VIEWER4);
            init(GpmTestValues.USER_USER3);
            init(GpmTestValues.USER_ADMIN3);
            init(GpmTestValues.USER_ADMIN);
            init(GpmTestValues.USER_ADMIN_PRODUCT);
            init(GpmTestValues.USER_USER6);
            init(GpmTestValues.USER_NOROLE);
            init(GpmTestValues.USER_VIEWER3);
            init(GpmTestValues.USER_USER2);
            init(GpmTestValues.USER_USER4);
            init(GpmTestValues.USER_USER1);
            init(GpmTestValues.USER_VIEWER1);
            init(GpmTestValues.USER_ADMIN_INSTANCE);
            init(GpmTestValues.USER_VIEWER2);
            init(GpmTestValues.USER_USER5);
            init = true;
        }
    }

    private void init(final String pUserLogin) {
        allIds.add(authorizationService.getUserData(pUserLogin).getId());
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.exportation.AbstractTestExport#setSpecificFlag(org.topcased.gpm.business.exportation.ExportProperties,
     *      org.topcased.gpm.business.exportation.ExportProperties.ExportFlag)
     */
    protected void setSpecificFlag(final ExportProperties pProperties,
            final ExportFlag pFlag) {
        pProperties.setUsersFlag(pFlag);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.exportation.AbstractTestExport#getId(java.io.Serializable,
     *      org.topcased.gpm.business.exportation.ExportProperties)
     */
    protected String getId(final User pObject,
            final ExportProperties pProperties) {
        return authorizationService.getUserData(pObject.getLogin()).getId();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.exportation.AbstractTestExport#getExpectedIdsForAll()
     */
    protected Set<String> getExpectedIdsForAll() {
        return allIds;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.exportation.AbstractTestExport#getExpectedIdsForLimitedByProduct()
     */
    protected Set<String> getExpectedIdsForLimitedByProduct() {
        return allIds;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.exportation.AbstractTestExport#getLimitedProductNames()
     */
    protected Set<String> getLimitedProductNames() {
        return Collections.emptySet();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.exportation.AbstractTestExport#getExpectedIdsForLimitedByType()
     */
    protected Set<String> getExpectedIdsForLimitedByType() {
        return allIds;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.exportation.AbstractTestExport#getLimitedTypeNames()
     */
    protected Set<String> getLimitedTypeNames() {
        return Collections.emptySet();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.exportation.AbstractTestExport#getIdsWithRoleOn()
     */
    protected Set<String> getIdsWithRoleOn() {
        return idsWithRoleOn;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.exportation.AbstractTestExport#getElementInfo(java.lang.String)
     */
    protected String getElementInfo(final String pElementId) {
        return pElementId;
    }
}