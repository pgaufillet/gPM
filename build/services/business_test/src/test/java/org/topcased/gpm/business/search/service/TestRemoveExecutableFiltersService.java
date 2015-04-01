/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Magali Franchet (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.search.service;

import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.exception.AuthorizationException;
import org.topcased.gpm.business.exception.GDMException;

/**
 * TestRemoveExecutableFiltersService
 * 
 * @author mfranche
 */
public class TestRemoveExecutableFiltersService extends FiltersCreationUtils {

    /** The Search Service. */
    private SearchService searchService;

    /**
     * Tests the method in normal conditions
     */
    public void testNormalCase() {
        searchService = serviceLocator.getSearchService();

        // create Instance filter
        ExecutableFilterData lInstanceFilterData =
                createInstanceExecutableFilterData();
        String lInstanceId =
                searchService.createExecutableFilter(adminRoleToken,
                        lInstanceFilterData);

        // create Product filter
        ExecutableFilterData lProductFilterData =
                createProductExecutableFilterData();
        String lProductId =
                searchService.createExecutableFilter(adminRoleToken,
                        lProductFilterData);

        // create User filter
        ExecutableFilterData lUserFilterData = createUserExecutableFilterData();
        String lUserId =
                searchService.createExecutableFilter(adminRoleToken,
                        lUserFilterData);

        // Remove the 3 filters
        searchService.removeExecutableFilter(adminRoleToken, lInstanceId);
        searchService.removeExecutableFilter(adminRoleToken, lProductId);
        searchService.removeExecutableFilter(adminRoleToken, lUserId);

        try {
            searchService.getExecutableFilter(adminRoleToken, lInstanceId);
            searchService.getExecutableFilter(adminRoleToken, lProductId);
            searchService.getExecutableFilter(adminRoleToken, lUserId);
            fail("An exception should have been thrown.");
        }
        catch (GDMException e) {
            // OK
        }
    }

    /**
     * Test to remove an instance filter with a role having admin access on
     * global instance : deletion is authorized
     */
    public void testRemoveInstanceFilterWithAdminInstanceAccessCase() {
        searchService = serviceLocator.getSearchService();
        authorizationService = serviceLocator.getAuthorizationService();

        // Login with Admin Instance
        String lAdminInstanceUserToken =
                authorizationService.login(
                        GpmTestValues.ADMIN_INSTANCE_LOGIN_PWD[0],
                        GpmTestValues.ADMIN_INSTANCE_LOGIN_PWD[1]);
        String lAdminInstanceRoleToken =
                authorizationService.selectRole(lAdminInstanceUserToken,
                        GpmTestValues.ADMINISTRATOR_ROLE, getProductName(),
                        getProcessName());

        // create Instance filter
        ExecutableFilterData lInstanceFilterData =
                createInstanceExecutableFilterData();
        String lInstanceId =
                searchService.createExecutableFilter(adminRoleToken,
                        lInstanceFilterData);

        // Remove this filter
        searchService.removeExecutableFilter(lAdminInstanceRoleToken,
                lInstanceId);

        try {
            lInstanceFilterData =
                    searchService.getExecutableFilter(adminRoleToken,
                            lInstanceId);
            fail("An exception should have been thrown.");
        }
        catch (GDMException e) {
            // OK. 
        }
    }

    /**
     * Test to remove an instance filter with a role having admin access on
     * product : deletion is not authorized
     */
    public void testRemoveInstanceFilterWithProductAccessCase() {
        searchService = serviceLocator.getSearchService();
        authorizationService = serviceLocator.getAuthorizationService();

        // Login with Product Instance
        String lProductInstanceUserToken =
                authorizationService.login(
                        GpmTestValues.ADMIN_PRODUCT_LOGIN_PWD[0],
                        GpmTestValues.ADMIN_PRODUCT_LOGIN_PWD[1]);
        String lProductInstanceRoleToken =
                authorizationService.selectRole(lProductInstanceUserToken,
                        GpmTestValues.ADMINISTRATOR_ROLE,
                        GpmTestValues.PRODUCT1_NAME, getProcessName());

        // create Instance filter
        ExecutableFilterData lInstanceFilterData =
                createInstanceExecutableFilterData();
        String lInstanceId =
                searchService.createExecutableFilter(adminRoleToken,
                        lInstanceFilterData);

        // Remove this filter
        try {
            searchService.removeExecutableFilter(lProductInstanceRoleToken,
                    lInstanceId);
            fail("An exception should have been thrown.");
        }
        catch (AuthorizationException ex) {
            // ok
        }

        lInstanceFilterData =
                searchService.getExecutableFilter(adminRoleToken, lInstanceId);
        assertNotNull(lInstanceFilterData);

    }

    /**
     * Test to remove an instance filter with a role with no admin access
     */
    public void testRemoveInstanceFilterNoAdminAccessCase() {
        searchService = serviceLocator.getSearchService();
        authorizationService = serviceLocator.getAuthorizationService();

        // Log with a non-admin role
        adminUserToken =
                authorizationService.login(GpmTestValues.USER_USER2, "pwd2");

        // create Instance filter
        ExecutableFilterData lInstanceFilterData =
                createInstanceExecutableFilterData();
        String lInstanceId =
                searchService.createExecutableFilter(adminRoleToken,
                        lInstanceFilterData);

        adminRoleToken =
                authorizationService.selectRole(adminUserToken, "notadmin",
                        getProductName(), getProcessName());

        // Remove this filter
        try {
            searchService.removeExecutableFilter(adminRoleToken, lInstanceId);
            fail("An exception should have been thrown.");
        }
        catch (AuthorizationException ex) {
            // ok
        }

        lInstanceFilterData =
                searchService.getExecutableFilter(adminRoleToken, lInstanceId);
        assertNotNull(lInstanceFilterData);
    }

    /**
     * Test to remove a product filter with a role having admin access on global
     * instance : deletion is authorized
     */
    public void testRemoveProductFilterWithAdminInstanceAccessCase() {
        searchService = serviceLocator.getSearchService();
        authorizationService = serviceLocator.getAuthorizationService();

        // Login with Admin Instance
        String lAdminInstanceUserToken =
                authorizationService.login(
                        GpmTestValues.ADMIN_INSTANCE_LOGIN_PWD[0],
                        GpmTestValues.ADMIN_INSTANCE_LOGIN_PWD[1]);
        String lAdminInstanceRoleToken =
                authorizationService.selectRole(lAdminInstanceUserToken,
                        GpmTestValues.ADMINISTRATOR_ROLE, getProductName(),
                        getProcessName());

        // create product filter
        ExecutableFilterData lProductFilterData =
                createProductExecutableFilterData();
        String lProductId =
                searchService.createExecutableFilter(adminRoleToken,
                        lProductFilterData);

        // Remove this filter
        searchService.removeExecutableFilter(lAdminInstanceRoleToken,
                lProductId);

        try {
            lProductFilterData =
                    searchService.getExecutableFilter(adminRoleToken,
                            lProductId);
            fail("An exception should have been thrown.");
        }
        catch (GDMException e) {
            // OK. 
        }
    }

    /**
     * Test to remove a product filter with a role having admin access on
     * product : deletion is authorized
     */
    public void testRemoveProductFilterWithProductAccessCase() {
        searchService = serviceLocator.getSearchService();
        authorizationService = serviceLocator.getAuthorizationService();

        // Login with Product Instance
        String lProductInstanceUserToken =
                authorizationService.login(
                        GpmTestValues.ADMIN_PRODUCT_LOGIN_PWD[0],
                        GpmTestValues.ADMIN_PRODUCT_LOGIN_PWD[1]);
        String lProductInstanceRoleToken =
                authorizationService.selectRole(lProductInstanceUserToken,
                        GpmTestValues.ADMINISTRATOR_ROLE,
                        GpmTestValues.PRODUCT1_NAME, getProcessName());

        // create product filter
        ExecutableFilterData lProductFilterData =
                createProductExecutableFilterData();
        String lProductId =
                searchService.createExecutableFilter(adminRoleToken,
                        lProductFilterData);

        // Remove this filter
        searchService.removeExecutableFilter(lProductInstanceRoleToken,
                lProductId);

        try {
            lProductFilterData =
                    searchService.getExecutableFilter(adminRoleToken,
                            lProductId);
            fail("An exception should have been thrown.");
        }
        catch (GDMException e) {
            // OK. 
        }
    }

    /**
     * Test to remove a product filter with a role with no admin access
     */
    public void testRemoveProductFilterNoAdminAccessCase() {
        searchService = serviceLocator.getSearchService();
        authorizationService = serviceLocator.getAuthorizationService();

        // Log with a non-admin role
        adminUserToken =
                authorizationService.login(GpmTestValues.USER_USER2, "pwd2");

        // create product filter
        ExecutableFilterData lProductFilterData =
                createProductExecutableFilterData();
        String lProductId =
                searchService.createExecutableFilter(adminRoleToken,
                        lProductFilterData);

        adminRoleToken =
                authorizationService.selectRole(adminUserToken, "notadmin",
                        getProductName(), getProcessName());

        // Remove this filter
        try {
            searchService.removeExecutableFilter(adminRoleToken, lProductId);
            fail("An exception should have been thrown.");
        }
        catch (AuthorizationException ex) {
            // ok
        }
        lProductFilterData =
                searchService.getExecutableFilter(adminRoleToken, lProductId);
        assertNotNull(lProductFilterData);
    }

    /**
     * Test to remove a product filter and an instance filter with an invalid
     * access (role != admin)
     */
    public void testRemoveInstanceAndProductFilterInvalidAccessCase() {
        searchService = serviceLocator.getSearchService();
        authorizationService = serviceLocator.getAuthorizationService();

        // Log with a non-admin role
        adminUserToken =
                authorizationService.login(GpmTestValues.USER_USER2, "pwd2");

        // create product filter
        ExecutableFilterData lProductFilterData =
                createProductExecutableFilterData();
        String lProductId =
                searchService.createExecutableFilter(adminRoleToken,
                        lProductFilterData);

        // create Instance filter
        ExecutableFilterData lInstanceFilterData =
                createInstanceExecutableFilterData();
        String lInstanceId =
                searchService.createExecutableFilter(adminRoleToken,
                        lInstanceFilterData);

        adminRoleToken =
                authorizationService.selectRole(adminUserToken, "notadmin",
                        getProductName(), getProcessName());

        // Remove this filter
        try {
            searchService.removeExecutableFilter(adminRoleToken, lProductId);
            searchService.removeExecutableFilter(adminRoleToken, lInstanceId);
            fail("An exception should have been thrown.");
        }
        catch (AuthorizationException ex) {
            // ok
        }

        // Try to access to the two filters
        lInstanceFilterData =
                searchService.getExecutableFilter(adminRoleToken, lInstanceId);
        assertNotNull(lInstanceFilterData);
        lProductFilterData =
                searchService.getExecutableFilter(adminRoleToken, lProductId);
        assertNotNull(lProductFilterData);
    }

}
