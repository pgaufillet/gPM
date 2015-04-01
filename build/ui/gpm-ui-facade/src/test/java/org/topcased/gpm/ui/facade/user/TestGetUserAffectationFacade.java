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

import java.util.Arrays;
import java.util.Collection;

import org.topcased.gpm.ui.facade.AbstractFacadeTestCase;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.shared.administration.user.UiUserAffectation;

/**
 * TestGetUserListSortedByLoginFacade
 * 
 * @author jlouisy
 */
public class TestGetUserAffectationFacade extends AbstractFacadeTestCase {

    private static final String[] EXPECTED_PROCESS_ROLES =
            { "user", "role 2 (inst order 2)", "role 4 (inst order 3)",
             "role 1 (inst order 4)" };

    private static final String[] EXPECTED_PRODUCT_ORDER =
            { "EMPTY_CHILD_PRODUCT_1", "EMPTY_CHILD_PRODUCT_2", "ROOT_PRODUCT" };

    private static final String[][] EXPECTED_PRODUCT_ROLES =
            { { "role 4 (inst order 3)" },
             { "user", "role 4 (inst order 3)", "role 1 (inst order 4)" },
             { "user", "role 2 (inst order 2)", "role 1 (inst order 4)" } };

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

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Get User affectations.");
        }
        UiUserAffectation lAffectation =
                getFacadeLocator().getUserFacade().getUserAffectation(lSession,
                        "user bis");

        assertEquals(EXPECTED_PROCESS_ROLES.length,
                lAffectation.getProcessAffectations().size());
        assertTrue(Arrays.asList(EXPECTED_PROCESS_ROLES).containsAll(
                lAffectation.getProcessAffectations()));

        assertEquals(EXPECTED_PRODUCT_ORDER.length,
                lAffectation.getProductAffectations().size());
        String[] lKeySet =
                lAffectation.getProductAffectations().keySet().toArray(
                        new String[0]);
        areEquals(EXPECTED_PRODUCT_ORDER, lKeySet);
        for (int i = 0; i < EXPECTED_PRODUCT_ORDER.length; i++) {
            assertEquals(EXPECTED_PRODUCT_ORDER[i], lKeySet[i]);
        }

        int i = 0;
        for (Collection<String> lRoles : lAffectation.getProductAffectations().values()) {
            assertTrue(Arrays.asList(EXPECTED_PRODUCT_ROLES[i]).containsAll(
                    lRoles));
            i++;
        }

        String[] lActualRoleNames =
                new String[lAffectation.getRoleTranslations().size()];
        for (int j = 0; j < lAffectation.getRoleTranslations().size(); j++) {
            lActualRoleNames[j] =
                    lAffectation.getRoleTranslations().get(j).getTranslatedValue();
        }
        areEquals(EXPECTED_ROLES, lActualRoleNames);

    }
}
