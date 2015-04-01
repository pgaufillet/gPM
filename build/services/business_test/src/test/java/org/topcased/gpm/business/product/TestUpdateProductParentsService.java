/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Magali Franchet (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.product;

import static org.topcased.gpm.business.GpmTestValues.PRODUCT1_NAME;

import java.util.ArrayList;
import java.util.List;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.exception.AuthorizationException;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.product.service.ProductService;

/**
 * TestUpdateProductParentsService
 * 
 * @author mfranche
 */
public class TestUpdateProductParentsService extends
        AbstractBusinessServiceTestCase {

    /** The Product Service. */
    private ProductService productService;

    private static final String PRODUCT1_1_NAME =
            GpmTestValues.PRODUCT_PRODUCT1_1;

    private static final String PRODUCT2_NAME = GpmTestValues.PRODUCT_PRODUCT2;

    /**
     * Tests the update product parents method
     */
    public void testNormalCase() {
        // Get the product service
        productService = serviceLocator.getProductService();

        List<String> lCurrentProductParentNames =
                productService.getProductParentsNames(getProcessName(),
                        PRODUCT1_1_NAME);
        assertTrue(lCurrentProductParentNames.size() == 1);
        assertTrue(lCurrentProductParentNames.contains(PRODUCT1_NAME));

        List<String> lNewParentNames = new ArrayList<String>();
        lNewParentNames.add(PRODUCT2_NAME);
        productService.updateProductParents(adminRoleToken, getProcessName(),
                PRODUCT1_1_NAME, lNewParentNames);

        List<String> lNewProductParentNames =
                productService.getProductParentsNames(getProcessName(),
                        PRODUCT1_1_NAME);
        assertTrue(lNewProductParentNames.size() == 1);
        assertTrue(lNewProductParentNames.contains(PRODUCT2_NAME));
    }

    /**
     * Tests the method when the new parent is a child of the product => an
     * exception must be thrown.
     */
    public void testInvalidCase() {
        // Get the product service
        productService = serviceLocator.getProductService();
        List<String> lNewParentNames = new ArrayList<String>();
        lNewParentNames.add(PRODUCT1_1_NAME);

        try {
            productService.updateProductParents(adminRoleToken,
                    getProcessName(), PRODUCT1_NAME, lNewParentNames);
        }
        catch (GDMException lGDMException) {
            // ok
        }
        catch (Throwable e) {
            fail("The exception thrown is not a GDMException.");
        }
    }

    /**
     * Check that an admin access set on instance can update product parents
     */
    public void testUpdateProductParentsWithAdminInstanceCase() {
        // Gets the product service.
        productService = serviceLocator.getProductService();

        // Login with Admin Instance
        String lAdminInstanceUserToken =
                authorizationService.login(
                        GpmTestValues.ADMIN_INSTANCE_LOGIN_PWD[0],
                        GpmTestValues.ADMIN_INSTANCE_LOGIN_PWD[1]);
        String lAdminInstanceRoleToken =
                authorizationService.selectRole(lAdminInstanceUserToken,
                        GpmTestValues.ADMINISTRATOR_ROLE, getProductName(),
                        getProcessName());

        try {
            productService.updateProductParents(lAdminInstanceRoleToken,
                    getProcessName(), getProductName(), null);
        }
        catch (Exception ex) {
            fail("An exception has been thrown ...");
        }
    }

    /**
     * Check than an admin access set on product can only update the product on
     * which his role is defined
     */
    public void testUpdateProductWithAdminProductCase() {
        // Gets the product service.
        productService = serviceLocator.getProductService();

        // Login with Product Instance
        String lProductInstanceUserToken =
                authorizationService.login(
                        GpmTestValues.ADMIN_PRODUCT_LOGIN_PWD[0],
                        GpmTestValues.ADMIN_PRODUCT_LOGIN_PWD[1]);
        String lProductInstanceRoleToken =
                authorizationService.selectRole(lProductInstanceUserToken,
                        GpmTestValues.ADMINISTRATOR_ROLE,
                        GpmTestValues.PRODUCT1_NAME, getProcessName());

        // Try to update the product on which an admin access is defined.
        try {
            productService.updateProductParents(lProductInstanceRoleToken,
                    getProcessName(), GpmTestValues.PRODUCT1_NAME, null);
        }
        catch (Exception ex) {
            fail("An exception has been thrown ...");
        }

        // Try to update another product (no admin access on it) => an exception must be thrown. 
        try {
            productService.updateProductParents(lProductInstanceRoleToken,
                    getProcessName(), getProductName(), null);
            fail("The exception has not been thrown.");
        }
        catch (AuthorizationException lAuthorizationException) {
            // ok
        }
        catch (Throwable e) {
            fail("The exception thrown is not an AuthorizationException.");
        }

        // Select another product (admin access is not set on this role)
        String lViewerRoleToken =
                authorizationService.selectRole(lProductInstanceUserToken,
                        GpmTestValues.VIEWER_ROLE, getProductName(),
                        getProcessName());
        try {
            productService.updateProductParents(lViewerRoleToken,
                    getProcessName(), GpmTestValues.PRODUCT1_NAME, null);
            fail("The exception has not been thrown.");
        }
        catch (AuthorizationException lAuthorizationException) {
            // ok
        }
        catch (Throwable e) {
            fail("The exception thrown is not an AuthorizationException.");
        }
    }

    /**
     * Check that a classical role (no global admin, no admin access) cannot
     * update product
     */
    public void testUpdateProductWithNoAdminAccessCase() {
        // Gets the product service.
        productService = serviceLocator.getProductService();

        // Login with classical role
        String lUserToken =
                authorizationService.login(GpmTestValues.NO_ADMIN_LOGIN_PWD[0],
                        GpmTestValues.NO_ADMIN_LOGIN_PWD[1]);
        String lRoleToken =
                authorizationService.selectRole(lUserToken,
                        GpmTestValues.VIEWER_ROLE, getProductName(),
                        getProcessName());

        try {
            productService.updateProductParents(lRoleToken, getProcessName(),
                    getProductName(), null);
            fail("The exception has not been thrown.");
        }
        catch (AuthorizationException lAuthorizationException) {
            // ok
        }
        catch (Throwable e) {
            fail("The exception thrown is not an AuthorizationException.");
        }
    }
}
