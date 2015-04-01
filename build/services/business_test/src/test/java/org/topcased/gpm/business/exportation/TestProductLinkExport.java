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

import java.util.HashSet;
import java.util.Set;

import org.topcased.gpm.business.ContextLocator;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.exportation.ExportProperties.ExportFlag;
import org.topcased.gpm.business.link.impl.LinkServiceImpl;
import org.topcased.gpm.business.serialization.data.ProductLink;

/**
 * Tests the product link export.
 * 
 * @author tpanuel
 */
public class TestProductLinkExport extends
        AbstractValuesContainerTestExport<ProductLink> {
    private final static Set<String> allIds = new HashSet<String>();

    private final static Set<String> byProductIds = new HashSet<String>();

    private final static Set<String> byTypeIds = new HashSet<String>();

    private final static Set<String> limitedProductNames =
            new HashSet<String>();

    private final static Set<String> limitedTypeNames = new HashSet<String>();

    private final static Set<String> idsWithRoleOn = new HashSet<String>();

    private static boolean init = false;

    private LinkServiceImpl linkService;

    /**
     * Create TestProductLinkExport.
     */
    public TestProductLinkExport() {
        super("productLinks", ProductLink.class);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.AbstractBusinessServiceTestCase#setUp()
     */
    @Override
    protected void setUp() {
        super.setUp();
        linkService =
                (LinkServiceImpl) ContextLocator.getContext().getBean(
                        "linkServiceImpl");
        if (!init) {
            // Fill limited product names
            limitedProductNames.add(GpmTestValues.PRODUCT_STORE1_NAME);
            // Fill limited type names
            limitedTypeNames.add("ProdLinkType");
            // Fill all elements ids
            init(GpmTestValues.PRODUCT_STORE1_NAME,
                    GpmTestValues.PRODUCT_STORE2_NAME, "ProdLinkType");
            init = true;
        }
    }

    private void init(final String pOriginProductName,
            final String pDestinationProductName, final String pTypeName) {
        final String lProductLinkId =
                linkService.getProductLinkId(getProcessName(),
                        pOriginProductName, pDestinationProductName, pTypeName);

        allIds.add(lProductLinkId);
        if (limitedProductNames.contains(pOriginProductName)
                || limitedProductNames.contains(pDestinationProductName)) {
            byProductIds.add(lProductLinkId);
        }
        if (limitedTypeNames.contains(pTypeName)) {
            byTypeIds.add(lProductLinkId);
        }
        if (productNamesWithRoleOn.contains(pOriginProductName)
                || productNamesWithRoleOn.contains(pDestinationProductName)) {
            idsWithRoleOn.add(lProductLinkId);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.exportation.AbstractTestExport#setSpecificFlag(org.topcased.gpm.business.exportation.ExportProperties,
     *      org.topcased.gpm.business.exportation.ExportProperties.ExportFlag)
     */
    protected void setSpecificFlag(final ExportProperties pProperties,
            final ExportFlag pFlag) {
        pProperties.setProductLinksFlag(pFlag);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.exportation.AbstractTestExport#getId(java.io.Serializable,
     *      org.topcased.gpm.business.exportation.ExportProperties)
     */
    protected String getId(final ProductLink pObject) {
        // Check that UID are not exported
        assertNull("Origin id has been exported.", pObject.getOriginId());
        assertNull("Destination id has been exported.",
                pObject.getDestinationId());

        return linkService.getProductLinkId(getProcessName(),
                pObject.getOriginProductName(),
                pObject.getDestinationProductName(), pObject.getType());
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
        return byProductIds;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.exportation.AbstractTestExport#getLimitedProductNames()
     */
    protected Set<String> getLimitedProductNames() {
        return limitedProductNames;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.exportation.AbstractTestExport#getExpectedIdsForLimitedByType()
     */
    protected Set<String> getExpectedIdsForLimitedByType() {
        return byTypeIds;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.exportation.AbstractTestExport#getLimitedTypeNames()
     */
    protected Set<String> getLimitedTypeNames() {
        return limitedTypeNames;
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