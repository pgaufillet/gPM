/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.i18n;

import java.util.List;
import java.util.Map;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.i18n.service.I18nService;

/**
 * TestGetAllValues
 * 
 * @author mkargbo
 */
public class TestGetAllValues extends AbstractBusinessServiceTestCase {

    private I18nService i18nService;

    /**
     * Setup
     */
    @Override
    protected void setUp() {
        super.setUp();
        i18nService = serviceLocator.getI18nService();
    }

    /**
     * Test method for
     * {@link org.topcased.gpm.business.i18n.impl.I18nServiceImpl#getValue(java.lang.String)}
     * .
     */
    public final void testGetValueString() {
        List<Map<String, String>> lValues = i18nService.getValue("fr");

        assertNotNull(lValues);
    }

}
