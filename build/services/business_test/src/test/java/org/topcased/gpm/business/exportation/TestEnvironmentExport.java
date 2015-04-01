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
import org.topcased.gpm.business.environment.impl.EnvironmentServiceImpl;
import org.topcased.gpm.business.exportation.ExportProperties.ExportFlag;
import org.topcased.gpm.business.serialization.data.Environment;

/**
 * Tests the environment export.
 * 
 * @author tpanuel
 */
public class TestEnvironmentExport extends AbstractTestExport<Environment> {
    private final static Set<String> allIds = new HashSet<String>();

    private final static Set<String> idsWithRoleOn = new HashSet<String>();

    private static boolean init = false;

    private EnvironmentServiceImpl environmentService;

    /**
     * Create TestEnvironmentExport.
     */
    public TestEnvironmentExport() {
        super("environments", Environment.class);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.AbstractBusinessServiceTestCase#setUp()
     */
    @Override
    protected void setUp() {
        super.setUp();
        environmentService =
                (EnvironmentServiceImpl) ContextLocator.getContext().getBean(
                        "environmentServiceImpl");
        if (!init) {
            // Fill all elements ids
            init(GpmTestValues.ENVIRONMENT_PROFESSIONAL);
            init(GpmTestValues.ENVIRONMENT_CLASSICAL);
            init("Classical_1");
            init(GpmTestValues.ENVIRONMENT_ENV1);
            init(GpmTestValues.ENVIRONMENT_ENV2);
            // Fill environment with role on
            idsWithRoleOn.add(environmentService.getEnvironmentByName(
                    adminRoleToken, getProcessName(),
                    GpmTestValues.ENVIRONMENT_ENV2).getId());
            init = true;
        }
    }

    private void init(final String pEnvironmentName) {
        allIds.add(environmentService.getEnvironmentByName(adminRoleToken,
                getProcessName(), pEnvironmentName).getId());
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.exportation.AbstractTestExport#setSpecificFlag(org.topcased.gpm.business.exportation.ExportProperties,
     *      org.topcased.gpm.business.exportation.ExportProperties.ExportFlag)
     */
    protected void setSpecificFlag(final ExportProperties pProperties,
            final ExportFlag pFlag) {
        pProperties.setEnvironmentsFlag(pFlag);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.exportation.AbstractTestExport#getId(java.io.Serializable,
     *      org.topcased.gpm.business.exportation.ExportProperties)
     */
    protected String getId(final Environment pObject,
            final ExportProperties pProperties) {
        return environmentService.getEnvironmentByName(adminRoleToken,
                getProcessName(), pObject.getName()).getId();
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