/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available 
 * under the terms of the Lesser Gnu Public License (LGPL)which 
 * accompanies this distribution, and is available 
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Anne Haugommard(Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.search.service;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;

/**
 * Tests the method <CODE>checkVisibilityConstraint<CODE> of the Search Service.
 * 
 * @author ahaugomm
 */
public class TestCheckVisibilityConstraintsService extends
        AbstractBusinessServiceTestCase {

    /** The Search Service. */
    private SearchService searchService;

    /** When no user defined for the constraint. */
    private static final String ALL_USERS = null;

    /** The user login used. */
    private static final String USER_LOGIN = GpmTestValues.USER_ADMIN;

    /** The other user login used. */
    private static final String INCORRECT_USER_LOGIN = "notadmin";

    /** The product name. */
    private static final String PRODUCT_NAME =
            GpmTestValues.PRODUCT_HAPPY_MOUSE_NAME;

    /** When no product defined for the constraint. */
    private static final String ALL_PRODUCTS = null;

    /** Incorrect business process name. */
    private static final String INCORRECT_PROCESS = "GREFIE";

    /**
     * Tests the method checkVisibilityConstraints with a constraint on all
     * users and products
     */
    public void testNormalCaseWithAllUsersAllProducts() {
        // Gets the search service.
        searchService = serviceLocator.getSearchService();

        // First, we create a FilterVisibilityConstraintData for all users and
        // product
        FilterVisibilityConstraintData lConstraint =
                new FilterVisibilityConstraintData(ALL_USERS, getProcessName(),
                        ALL_PRODUCTS);

        // Check of the constraint with a user and a product
        assertTrue(
                "Check with product and user should be OK when no constraints on user nor product",
                this.searchService.checkVisibilityConstraints(USER_LOGIN,
                        getProcessName(), PRODUCT_NAME, lConstraint));

        // Check with another process
        assertFalse(
                "The constraint check should be NOK with another business process",
                this.searchService.checkVisibilityConstraints(USER_LOGIN,
                        INCORRECT_PROCESS, PRODUCT_NAME, lConstraint));
    }

    /**
     * Tests the method checkVisibilityConstraints with a constraint on a user
     * and all products
     */
    public void testNormalCaseWithAUserAllProducts() {
        // Gets the search service.
        searchService = serviceLocator.getSearchService();

        // First, we create a FilterVisibilityConstraintData for all users and
        // product
        FilterVisibilityConstraintData lConstraint =
                new FilterVisibilityConstraintData(USER_LOGIN,
                        getProcessName(), ALL_PRODUCTS);

        // Check of the constraint with correct user and a product
        assertTrue("Check with product and correct user should be OK ",
                this.searchService.checkVisibilityConstraints(USER_LOGIN,
                        getProcessName(), PRODUCT_NAME, lConstraint));

        // Check of the constraint with incorrect user and a product
        assertFalse("Check with product and incorrect user should be NOK ",
                this.searchService.checkVisibilityConstraints(
                        INCORRECT_USER_LOGIN, getProcessName(), PRODUCT_NAME,
                        lConstraint));

        // Check with another process
        assertFalse(
                "The constraint check should be NOK with another business process",
                this.searchService.checkVisibilityConstraints(USER_LOGIN,
                        INCORRECT_PROCESS, PRODUCT_NAME, lConstraint));
    }

    /**
     * Tests the method checkVisibilityConstraints with a constraint on all
     * users and a product
     */
    public void testNormalCaseWithAllUserAProduct() {
        // Gets the search service.
        searchService = serviceLocator.getSearchService();

        // First, we create a FilterVisibilityConstraintData for all users and
        // product
        FilterVisibilityConstraintData lConstraint =
                new FilterVisibilityConstraintData(ALL_USERS, getProcessName(),
                        PRODUCT_NAME);

        // Check of the constraint with correct product and a user
        assertTrue("Check with correct product and a user should be OK ",
                this.searchService.checkVisibilityConstraints(USER_LOGIN,
                        getProcessName(), PRODUCT_NAME, lConstraint));

        // Check with another process
        assertFalse(
                "The constraint check should be NOK with another business process",
                this.searchService.checkVisibilityConstraints(USER_LOGIN,
                        INCORRECT_PROCESS, PRODUCT_NAME, lConstraint));
    }

    /**
     * Tests the method checkVisibilityConstraints with a constraint on a user
     * and a product
     */
    public void testNormalCaseWithAUserAProduct() {
        // Gets the search service.
        searchService = serviceLocator.getSearchService();

        // First, we create a FilterVisibilityConstraintData for all users and
        // product
        FilterVisibilityConstraintData lConstraint =
                new FilterVisibilityConstraintData(USER_LOGIN,
                        getProcessName(), PRODUCT_NAME);

        // Check of the constraint with correct product and a user
        assertTrue("Check with correct product and correct user should be OK ",
                this.searchService.checkVisibilityConstraints(USER_LOGIN,
                        getProcessName(), PRODUCT_NAME, lConstraint));

        // Check of the constraint with correct product and incorrect user
        assertFalse(
                "Check with correct product and incorrect user should be NOK ",
                this.searchService.checkVisibilityConstraints(
                        INCORRECT_USER_LOGIN, getProcessName(), PRODUCT_NAME,
                        lConstraint));

        // Check with another process
        assertFalse(
                "The constraint check should be NOK with another business process",
                this.searchService.checkVisibilityConstraints(USER_LOGIN,
                        INCORRECT_PROCESS, PRODUCT_NAME, lConstraint));
    }

}