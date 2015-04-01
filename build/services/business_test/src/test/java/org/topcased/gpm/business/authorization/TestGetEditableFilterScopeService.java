/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.authorization;

import java.util.List;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.search.service.FilterScope;

/**
 * TestGetEditableFilterScopeService
 * 
 * @author nveillet
 */
public class TestGetEditableFilterScopeService extends
        AbstractBusinessServiceTestCase {

    private static final String INSTANCE_FILE =
            "search/TestFilterAccessNonEditable.xml";

    /**
     * Test the filter access visibilities
     */
    public void testNormalCase() {
        // Instanciate filter access
        instantiate(getProcessName(), INSTANCE_FILE);

        // Instanciate filter access
        List<FilterScope> lFilterScopes =
                authorizationService.getEditableFilterScope(normalRoleToken);

        assertTrue("Process filters are not editable",
                lFilterScopes.contains(FilterScope.INSTANCE_FILTER));

        assertTrue("Product filters are not editable",
                lFilterScopes.contains(FilterScope.PRODUCT_FILTER));

        assertFalse("User filters are editable",
                lFilterScopes.contains(FilterScope.USER_FILTER));
    }
}
