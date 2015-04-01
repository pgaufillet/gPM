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
import org.topcased.gpm.business.link.impl.CacheableLink;
import org.topcased.gpm.business.link.impl.LinkServiceImpl;
import org.topcased.gpm.business.serialization.data.SheetLink;
import org.topcased.gpm.business.sheet.impl.CacheableSheet;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * Tests the sheet link export.
 * 
 * @author tpanuel
 */
public class TestSheetLinkExport extends
        AbstractValuesContainerTestExport<SheetLink> {
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
     * Create TestSheetExport.
     */
    public TestSheetLinkExport() {
        super("sheetLinks", SheetLink.class);
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
            limitedProductNames.add(GpmTestValues.PRODUCT_HAPPY_MOUSE_NAME);
            limitedProductNames.add(GpmTestValues.PRODUCT_STORE1_NAME);
            // Fill limited type names
            limitedTypeNames.add(GpmTestValues.SHEETLINK_CAT_CAT);
            limitedTypeNames.add(GpmTestValues.SHEETLINK_CAT_PRICE);
            // Fill all elements ids
            init(GpmTestValues.PRODUCT_HAPPY_MOUSE_NAME,
                    GpmTestValues.SHEET_REF_GARFIELD,
                    GpmTestValues.PRODUCT_HAPPY_MOUSE_NAME, "Gros Minet",
                    GpmTestValues.SHEETLINK_CAT_CAT);
            init(GpmTestValues.PRODUCT_HAPPY_MOUSE_NAME,
                    GpmTestValues.SHEET_REF_GARFIELD,
                    GpmTestValues.PRODUCT_BERNARD_STORE_NAME, "Cat 200",
                    GpmTestValues.SHEETLINK_CAT_PRICE);
            init(GpmTestValues.PRODUCT_STORE1_NAME,
                    "sheet_with_some_confidential_fields_04",
                    GpmTestValues.PRODUCT_STORE1_NAME,
                    "sheet_multiple_with_some_confidential_fields_01",
                    "SheetLinkWithSomeConfidentialFields");
            init(GpmTestValues.PRODUCT_STORE1_NAME,
                    "sheet_with_some_confidential_fields_01",
                    GpmTestValues.PRODUCT_STORE1_NAME,
                    "sheet_multiple_with_some_confidential_fields_01",
                    "SheetLinkWithSomeConfidentialFields");
            init(GpmTestValues.PRODUCT_STORE1_1_NAME,
                    "sheet_with_some_confidential_fields_02",
                    GpmTestValues.PRODUCT_STORE1_1_NAME,
                    "sheet_multiple_with_some_confidential_fields_02",
                    "SheetLinkWithSomeConfidentialFields");
            init(GpmTestValues.PRODUCT_STORE2_NAME,
                    "sheet_with_some_confidential_fields_03",
                    GpmTestValues.PRODUCT_STORE2_NAME,
                    "sheet_multiple_with_some_confidential_fields_03",
                    "SheetLinkWithSomeConfidentialFields");
            init(GpmTestValues.PRODUCT_STORE1_NAME,
                    "sheet_with_some_confidential_fields_04",
                    GpmTestValues.PRODUCT_STORE1_NAME,
                    "sheet_multiple_with_some_confidential_fields_04",
                    "SheetLinkWithSomeConfidentialFields");
            init(GpmTestValues.PRODUCT1_NAME, "pointed:2",
                    GpmTestValues.PRODUCT1_NAME, "pointer:2",
                    "TestPointerFieldLink");
            init(GpmTestValues.PRODUCT1_NAME, "pointed:3",
                    GpmTestValues.PRODUCT1_NAME, "pointer:3",
                    "TestPointerFieldLink");
            init(GpmTestValues.PRODUCT1_NAME, "pointed:2",
                    GpmTestValues.PRODUCT1_NAME, "pointer:3",
                    "TestPointerFieldLink");
            init(GpmTestValues.PRODUCT1_NAME, "pointed:5",
                    GpmTestValues.PRODUCT1_NAME, "pointer:4",
                    "TestPointerFieldLink");
            init(GpmTestValues.PRODUCT1_NAME, "pointed:4",
                    GpmTestValues.PRODUCT1_NAME, "pointer:4",
                    "TestPointerFieldLink");
            init(GpmTestValues.PRODUCT1_NAME, "type_vfd_01_01",
                    GpmTestValues.PRODUCT1_NAME, "type_vfd_02_01",
                    "type_vfd_01-type_vfd_02");
            init(GpmTestValues.PRODUCT_STORE1_NAME,
                    "type_vfd_02_anotherProduct", GpmTestValues.PRODUCT1_NAME,
                    "type_vfd_01_02", "type_vfd_02-type_vfd_01");
            init(GpmTestValues.PRODUCT1_NAME, "type_vfd_02_01",
                    GpmTestValues.PRODUCT1_NAME, "type_vfd_01_02",
                    "type_vfd_02-type_vfd_01");
            init(GpmTestValues.PRODUCT1_NAME, "type_vfd_01_02",
                    GpmTestValues.PRODUCT1_NAME, "type_vfd_02_02",
                    "type_vfd_01-type_vfd_02");
            init(GpmTestValues.PRODUCT1_NAME, "type_vfd_01_02",
                    GpmTestValues.PRODUCT_STORE1_NAME,
                    "type_vfd_02_anotherProduct", "type_vfd_01-type_vfd_02");
            init(GpmTestValues.PRODUCT_PRODUCT2, "B",
                    GpmTestValues.PRODUCT1_NAME, "A", "FilterLinkTest");
            init(GpmTestValues.PRODUCT1_NAME, "B", GpmTestValues.PRODUCT1_NAME,
                    "A", "FilterLinkTest");
            init(GpmTestValues.PRODUCT1_NAME, "A", GpmTestValues.PRODUCT1_NAME,
                    "A", "FilterLinkTest");
            init(GpmTestValues.PRODUCT1_NAME, "B", GpmTestValues.PRODUCT1_NAME,
                    "B", "FilterLinkTest");
            init(GpmTestValues.PRODUCT_PRODUCT2, "A",
                    GpmTestValues.PRODUCT_PRODUCT2, "A", "FilterLinkTest");
            init(GpmTestValues.PRODUCT1_NAME, "A",
                    GpmTestValues.PRODUCT_PRODUCT2, "A", "FilterLinkTest");
            init(GpmTestValues.PRODUCT_PRODUCT2, "A",
                    GpmTestValues.PRODUCT_PRODUCT2, "B", "FilterLinkTest");
            init(GpmTestValues.PRODUCT_PRODUCT2, "B",
                    GpmTestValues.PRODUCT_PRODUCT2, "B", "FilterLinkTest");
            init(GpmTestValues.PRODUCT_HAPPY_MOUSE_NAME, "SheetType1:4",
                    GpmTestValues.PRODUCT_HAPPY_MOUSE_NAME, "Cat1",
                    GpmTestValues.SHEETLINK_SHEETTYPE1_CAT);
            init(GpmTestValues.PRODUCT_HAPPY_MOUSE_NAME, "Cat1",
                    GpmTestValues.PRODUCT_HAPPY_MOUSE_NAME, "Cat3",
                    GpmTestValues.SHEETLINK_CAT_CAT);
            init(GpmTestValues.PRODUCT_HAPPY_MOUSE_NAME, "Cat2",
                    GpmTestValues.PRODUCT_HAPPY_MOUSE_NAME, "Cat3",
                    GpmTestValues.SHEETLINK_CAT_CAT);
            init(GpmTestValues.PRODUCT_HAPPY_MOUSE_NAME,
                    GpmTestValues.SHEET_REF_GARFIELD,
                    GpmTestValues.PRODUCT_HAPPY_MOUSE_NAME, "Tom",
                    GpmTestValues.SHEETLINK_CAT_CAT);
            init(GpmTestValues.PRODUCT_STORE2_NAME, "Sheet_2:2",
                    GpmTestValues.PRODUCT_HAPPY_MOUSE_NAME,
                    GpmTestValues.SHEET_REF_GARFIELD,
                    GpmTestValues.SHEETLINK_SHEETTYPE1_CAT);
            init(GpmTestValues.PRODUCT_STORE1_1_NAME, "Sheet_1_1:3",
                    GpmTestValues.PRODUCT_HAPPY_MOUSE_NAME,
                    GpmTestValues.SHEET_REF_GARFIELD,
                    GpmTestValues.SHEETLINK_SHEETTYPE1_CAT);
            init(GpmTestValues.PRODUCT_STORE1_NAME, "Sheet_1:1",
                    GpmTestValues.PRODUCT_HAPPY_MOUSE_NAME,
                    GpmTestValues.SHEET_REF_GARFIELD,
                    GpmTestValues.SHEETLINK_SHEETTYPE1_CAT);
            init(GpmTestValues.PRODUCT_BERNARD_STORE_NAME,
                    "SheetWithoutDefinedValues:0",
                    GpmTestValues.PRODUCT_HAPPY_MOUSE_NAME,
                    GpmTestValues.SHEET_REF_GARFIELD,
                    GpmTestValues.SHEETLINK_SHEETTYPE1_CAT);
            init(GpmTestValues.PRODUCT_HAPPY_MOUSE_NAME,
                    GpmTestValues.SHEET_REF_GARFIELD,
                    GpmTestValues.PRODUCT_BERNARD_STORE_NAME, "Price1",
                    GpmTestValues.SHEETLINK_CAT_PRICE);
            init(GpmTestValues.PRODUCT_HAPPY_MOUSE_NAME, "SheetType1:6",
                    GpmTestValues.PRODUCT_HAPPY_MOUSE_NAME, "Cat2",
                    GpmTestValues.SHEETLINK_SHEETTYPE1_CAT);
            init(GpmTestValues.PRODUCT_HAPPY_MOUSE_NAME, "SheetType1:5",
                    GpmTestValues.PRODUCT_HAPPY_MOUSE_NAME, "Cat2",
                    GpmTestValues.SHEETLINK_SHEETTYPE1_CAT);
            init(GpmTestValues.PRODUCT1_NAME, "SheetForTestLinkFilters5",
                    GpmTestValues.PRODUCT1_NAME, "SheetForTestLinkFilters1",
                    "LINK_TYPE_FOR_TEST_FILTER_UNIDIRECTIONNAL");
            init(GpmTestValues.PRODUCT1_NAME, "SheetForTestLinkFilters1",
                    GpmTestValues.PRODUCT1_NAME, "SheetForTestLinkFilters1",
                    "LINK_TYPE_FOR_TEST_FILTER_UNIDIRECTIONNAL");
            init(GpmTestValues.PRODUCT1_NAME, "SheetForTestLinkFilters5",
                    GpmTestValues.PRODUCT1_NAME, "SheetForTestLinkFilters2",
                    "LINK_TYPE_FOR_TEST_FILTER_1");
            init(GpmTestValues.PRODUCT1_NAME, "SheetForTestLinkFilters3",
                    GpmTestValues.PRODUCT1_NAME, "SheetForTestLinkFilters2",
                    "LINK_TYPE_FOR_TEST_FILTER_1");
            init(GpmTestValues.PRODUCT1_NAME, "SheetForTestLinkFilters1",
                    GpmTestValues.PRODUCT1_NAME, "SheetForTestLinkFilters2",
                    "LINK_TYPE_FOR_TEST_FILTER_1");
            init(GpmTestValues.PRODUCT1_NAME, "SheetForTestLinkFilters1",
                    GpmTestValues.PRODUCT1_NAME, "SheetForTestLinkFilters3",
                    "LINK_TYPE_FOR_TEST_FILTER_UNIDIRECTIONNAL");
            init(GpmTestValues.PRODUCT1_NAME, "SheetForTestLinkFilters5",
                    GpmTestValues.PRODUCT1_NAME, "SheetForTestLinkFilters4",
                    "LINK_TYPE_FOR_TEST_FILTER_1");
            init(GpmTestValues.PRODUCT1_NAME, "SheetForTestLinkFilters3",
                    GpmTestValues.PRODUCT1_NAME, "SheetForTestLinkFilters4",
                    "LINK_TYPE_FOR_TEST_FILTER_1");
            init(GpmTestValues.PRODUCT1_NAME, "SheetForTestLinkFilters1",
                    GpmTestValues.PRODUCT1_NAME, "SheetForTestLinkFilters4",
                    "LINK_TYPE_FOR_TEST_FILTER_1");
            init(GpmTestValues.PRODUCT1_NAME, "SheetForTestLinkFilters5",
                    GpmTestValues.PRODUCT1_NAME, "SheetForTestLinkFilters6",
                    "LINK_TYPE_FOR_TEST_FILTER_1");
            init(GpmTestValues.PRODUCT1_NAME, "SheetForTestLinkFilters3",
                    GpmTestValues.PRODUCT1_NAME, "SheetForTestLinkFilters6",
                    "LINK_TYPE_FOR_TEST_FILTER_1");
            init(GpmTestValues.PRODUCT1_NAME, "SheetForTestLinkFilters1",
                    GpmTestValues.PRODUCT1_NAME, "SheetForTestLinkFilters6",
                    "LINK_TYPE_FOR_TEST_FILTER_1");
            init(GpmTestValues.PRODUCT1_NAME, "SheetForTestLinkFilters1",
                    GpmTestValues.PRODUCT1_NAME, "SheetForTestLinkFilters7",
                    "LINK_TYPE_FOR_TEST_FILTER_UNIDIRECTIONNAL");
            init(GpmTestValues.PRODUCT1_NAME, "testExportSheet_01",
                    GpmTestValues.PRODUCT1_NAME, "testExportSheet_02",
                    "testExportLink");
            init(GpmTestValues.PRODUCT1_NAME, "testExportSheet_02",
                    GpmTestValues.PRODUCT1_NAME, "testExportSheet_03",
                    "testExportLink");
            init = true;
        }
    }

    private void init(final String pOriginProductName,
            final String pOriginReference,
            final String pDestinationProductName,
            final String pDestinationReference, final String pTypeName) {
        final String lSheetLinkId =
                linkService.getSheetLinkId(getProcessName(),
                        pOriginProductName, pOriginReference,
                        pDestinationProductName, pDestinationReference,
                        pTypeName);

        allIds.add(lSheetLinkId);
        if (limitedProductNames.contains(pOriginProductName)
                || limitedProductNames.contains(pDestinationProductName)) {
            byProductIds.add(lSheetLinkId);
        }
        if (limitedTypeNames.contains(pTypeName)) {
            byTypeIds.add(lSheetLinkId);
        }
        if (productNamesWithRoleOn.contains(pOriginProductName)
                || productNamesWithRoleOn.contains(pDestinationProductName)) {
            idsWithRoleOn.add(lSheetLinkId);
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
        pProperties.setSheetLinksFlag(pFlag);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.exportation.AbstractValuesContainerTestExport#getId(org.topcased.gpm.business.serialization.data.ValuesContainerData)
     */
    protected String getId(final SheetLink pObject) {
        // Check that UID are not exported
        assertNull("Origin id has been exported.", pObject.getOriginId());
        assertNull("Destination id has been exported.",
                pObject.getDestinationId());

        return linkService.getSheetLinkId(getProcessName(),
                pObject.getOriginProductName(), pObject.getOriginReference(),
                pObject.getDestinationProductName(),
                pObject.getDestinationReference(), pObject.getType());
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
        final CacheableLink lLink =
                linkService.getCacheableLink(pElementId,
                        CacheProperties.IMMUTABLE);
        final CacheableSheet lOriginSheet =
                sheetService.getCacheableSheet(adminRoleToken,
                        lLink.getOriginId(), CacheProperties.IMMUTABLE);
        final CacheableSheet lDestinationSheet =
                sheetService.getCacheableSheet(adminRoleToken,
                        lLink.getDestinationId(), CacheProperties.IMMUTABLE);

        return lLink.getTypeName() + "-" + lOriginSheet.getProductName() + "-"
                + lOriginSheet.getFunctionalReference() + "-"
                + lDestinationSheet.getProductName() + "-"
                + lDestinationSheet.getFunctionalReference();
    }
}