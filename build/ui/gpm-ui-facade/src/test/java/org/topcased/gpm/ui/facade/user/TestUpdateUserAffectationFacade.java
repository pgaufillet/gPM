/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien LOUISY (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.user;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.TreeMap;

import org.topcased.gpm.ui.facade.AbstractFacadeTestCase;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.shared.administration.user.UiUserAffectation;

/**
 * TestGetUserListSortedByLoginFacade
 * 
 * @author jlouisy
 */
public class TestUpdateUserAffectationFacade extends AbstractFacadeTestCase {

    private static final String[] PROCESS_ROLES =
            { "user", "role 2 (inst order 2)" };

    private static final String[] PRODUCTS =
            { "EMPTY_CHILD_PRODUCT_2", "ROOT_PRODUCT" };

    private static final String[][] PRODUCT_ROLES =
            { { "user", "role 4 (inst order 3)", "role 1 (inst order 4)" },
             { "user" } };

    private static final String[] EXPECTED_ROLES =
            { "user", "role 2 (inst order 2)", "role 4 (inst order 3)",
             "role 1 (inst order 4)" };

    private void areEquals(String[] pExpected, String[] pActual) {
        assertEquals(pExpected.length, pActual.length);
        for (int i = 0; i < pExpected.length; i++) {
            assertEquals(pExpected[i], pActual[i]);
        }
    }

    /**
     * Normal case
     */
    public void testNormalCase() {

        UiSession lSession = adminUserSession.getSession(DEFAULT_PRODUCT_NAME);

        List<String> lProcessRoles =
                new ArrayList<String>(Arrays.asList(PROCESS_ROLES));
        TreeMap<String, List<String>> lProductRoles =
                new TreeMap<String, List<String>>();
        lProductRoles.put(PRODUCTS[0], new ArrayList<String>(
                Arrays.asList(PRODUCT_ROLES[0])));
        lProductRoles.put(PRODUCTS[1], new ArrayList<String>(
                Arrays.asList(PRODUCT_ROLES[1])));
        UiUserAffectation lAffectation =
                new UiUserAffectation(lProcessRoles, lProductRoles);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Update User affectations.");
        }
        getFacadeLocator().getUserFacade().updateUserAffectation(lSession,
                "user bis", lAffectation);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get updated User affectations.");
        }
        UiUserAffectation lUpdatedAffectation =
                getFacadeLocator().getUserFacade().getUserAffectation(lSession,
                        "user bis");

        assertEquals(PROCESS_ROLES.length,
                lUpdatedAffectation.getProcessAffectations().size());
        assertTrue(Arrays.asList(PROCESS_ROLES).containsAll(
                lUpdatedAffectation.getProcessAffectations()));

        assertEquals(PRODUCTS.length,
                lUpdatedAffectation.getProductAffectations().size());
        String[] lKeySet =
                lUpdatedAffectation.getProductAffectations().keySet().toArray(
                        new String[0]);
        areEquals(PRODUCTS, lKeySet);
        for (int i = 0; i < PRODUCTS.length; i++) {
            assertEquals(PRODUCTS[i], lKeySet[i]);
        }

        int i = 0;
        for (Collection<String> lRoles : lUpdatedAffectation.getProductAffectations().values()) {
            assertTrue(Arrays.asList(PRODUCT_ROLES[i]).containsAll(lRoles));
            i++;
        }

        String[] lActualRoleNames =
                new String[lUpdatedAffectation.getRoleTranslations().size()];
        for (int j = 0; j < lUpdatedAffectation.getRoleTranslations().size(); j++) {
            lActualRoleNames[j] =
                    lUpdatedAffectation.getRoleTranslations().get(j).getTranslatedValue();
        }
        areEquals(EXPECTED_ROLES, lActualRoleNames);
    }
}
