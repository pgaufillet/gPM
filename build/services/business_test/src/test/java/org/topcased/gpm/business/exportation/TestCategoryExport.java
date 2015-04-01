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
import org.topcased.gpm.business.serialization.data.Category;

/**
 * Tests the category export.
 * 
 * @author tpanuel
 */
public class TestCategoryExport extends AbstractTestExport<Category> {
    private final static Set<String> allIds = new HashSet<String>();

    private final static Set<String> idsWithRoleOn = new HashSet<String>();

    private static boolean init = false;

    private EnvironmentServiceImpl environmentService;

    /**
     * Create TestCategoryExport.
     */
    public TestCategoryExport() {
        super("categories", Category.class);
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
            init(GpmTestValues.CATEGORY_COLOR);
            init(GpmTestValues.CATEGORY_LENGTH);
            init(GpmTestValues.CATEGORY_CAT_PEDIGRE);
            init(GpmTestValues.CATEGORY_USAGE);
            init(GpmTestValues.CATEGORY_ERROR_LEVEL);
            init(GpmTestValues.CATEGORY_FILTER_TEST_SHEET_01_CHOICE_CATEGORY);
            init(GpmTestValues.CATEGORY_FILTER_TEST_SHEET_02_CHOICE_CATEGORY);
            init(GpmTestValues.CATEGORY_TEST_IMAGE_TEXT_DISPLAYHINT_01_CATEGORY);
            init("CategoryDefinition");
            init = true;
        }
    }

    private void init(final String pCategoryName) {
        allIds.add(environmentService.getCategory(adminRoleToken, pCategoryName).getId());
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.exportation.AbstractTestExport#setSpecificFlag(org.topcased.gpm.business.exportation.ExportProperties,
     *      org.topcased.gpm.business.exportation.ExportProperties.ExportFlag)
     */
    protected void setSpecificFlag(final ExportProperties pProperties,
            final ExportFlag pFlag) {
        pProperties.setCategoriesFlag(pFlag);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.exportation.AbstractTestExport#getId(java.io.Serializable,
     *      org.topcased.gpm.business.exportation.ExportProperties)
     */
    protected String getId(final Category pObject,
            final ExportProperties pProperties) {
        return environmentService.getCategory(adminRoleToken, pObject.getName()).getId();
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