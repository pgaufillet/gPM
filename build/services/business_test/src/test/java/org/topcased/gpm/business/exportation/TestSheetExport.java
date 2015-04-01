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
import org.topcased.gpm.business.serialization.data.SheetData;
import org.topcased.gpm.business.sheet.impl.CacheableSheet;
import org.topcased.gpm.business.sheet.impl.SheetServiceImpl;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * Tests the sheet export.
 * 
 * @author tpanuel
 */
public class TestSheetExport extends
        AbstractValuesContainerTestExport<SheetData> {
    private final static Set<String> allIds = new HashSet<String>();

    private final static Set<String> byProductIds = new HashSet<String>();

    private final static Set<String> byTypeIds = new HashSet<String>();

    private final static Set<String> limitedProductNames =
            new HashSet<String>();

    private final static Set<String> limitedTypeNames = new HashSet<String>();

    private final static Set<String> idsWithRoleOn = new HashSet<String>();

    private static boolean init = false;

    private SheetServiceImpl sheetService;

    /**
     * Create TestSheetExport.
     */
    public TestSheetExport() {
        super("sheets", SheetData.class);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.AbstractBusinessServiceTestCase#setUp()
     */
    @Override
    protected void setUp() {
        super.setUp();
        sheetService =
                (SheetServiceImpl) ContextLocator.getContext().getBean(
                        "sheetServiceImpl");
        if (!init) {
            // Fill limited product names
            limitedProductNames.add(GpmTestValues.PRODUCT_HAPPY_MOUSE_NAME);
            limitedProductNames.add(GpmTestValues.PRODUCT_STORE1_NAME);
            // Fill limited type names
            limitedTypeNames.add(GpmTestValues.SHEET_TYPE_CAT);
            limitedTypeNames.add(GpmTestValues.SHEET_TYPE_PRICE);
            limitedTypeNames.add(GpmTestValues.SHEET_TYPE_DOG);
            // Fill all elements ids
            init(GpmTestValues.PRODUCT_HAPPY_MOUSE_NAME, "Gros Minet",
                    GpmTestValues.SHEET_TYPE_CAT);
            init(GpmTestValues.PRODUCT_HAPPY_MOUSE_NAME,
                    GpmTestValues.SHEET_REF_FAMOUS_CAT,
                    GpmTestValues.SHEET_TYPE_PRICE);
            init(GpmTestValues.PRODUCT_BERNARD_STORE_NAME, "Medor",
                    GpmTestValues.SHEET_TYPE_DOG);
            init(GpmTestValues.PRODUCT_BERNARD_STORE_NAME, "Rantanplan",
                    GpmTestValues.SHEET_TYPE_DOG);
            init(GpmTestValues.PRODUCT_BERNARD_STORE_NAME, "Milou",
                    GpmTestValues.SHEET_TYPE_DOG);
            init(GpmTestValues.PRODUCT_BERNARD_STORE_NAME, "Dog 500",
                    GpmTestValues.SHEET_TYPE_PRICE);
            init(GpmTestValues.PRODUCT_BERNARD_STORE_NAME, "Cat 200",
                    GpmTestValues.SHEET_TYPE_PRICE);
            init(GpmTestValues.PRODUCT_HAPPY_MOUSE_NAME, "testSheet",
                    GpmTestValues.SHEET_TYPE_CAT);
            init(GpmTestValues.PRODUCT_BERNARD_STORE_NAME, "GRID",
                    GpmTestValues.SHEET_TYPE_GRID_SIMPLE_TEST);
            init(
                    GpmTestValues.PRODUCT_STORE1_NAME,
                    "sheet_with_some_confidential_fields_01",
                    GpmTestValues.SHEET_TYPE_SHEET_WITH_SOME_CONFIDENTIAL_FIELDS);
            init(
                    GpmTestValues.PRODUCT_STORE1_1_NAME,
                    "sheet_with_some_confidential_fields_02",
                    GpmTestValues.SHEET_TYPE_SHEET_WITH_SOME_CONFIDENTIAL_FIELDS);
            init(
                    GpmTestValues.PRODUCT_STORE2_NAME,
                    "sheet_with_some_confidential_fields_03",
                    GpmTestValues.SHEET_TYPE_SHEET_WITH_SOME_CONFIDENTIAL_FIELDS);
            init(
                    GpmTestValues.PRODUCT_STORE1_NAME,
                    "sheet_with_some_confidential_fields_04",
                    GpmTestValues.SHEET_TYPE_SHEET_WITH_SOME_CONFIDENTIAL_FIELDS);
            init(
                    GpmTestValues.PRODUCT_STORE1_NAME,
                    "sheet_multiple_with_some_confidential_fields_01",
                    GpmTestValues.SHEETLINK_SHEET_MULTIPLE_WITH_SOME_CONFIDENTIAL_FIELDS);
            init(
                    GpmTestValues.PRODUCT_STORE1_1_NAME,
                    "sheet_multiple_with_some_confidential_fields_02",
                    GpmTestValues.SHEETLINK_SHEET_MULTIPLE_WITH_SOME_CONFIDENTIAL_FIELDS);
            init(
                    GpmTestValues.PRODUCT_STORE2_NAME,
                    "sheet_multiple_with_some_confidential_fields_03",
                    GpmTestValues.SHEETLINK_SHEET_MULTIPLE_WITH_SOME_CONFIDENTIAL_FIELDS);
            init(
                    GpmTestValues.PRODUCT_STORE1_NAME,
                    "sheet_multiple_with_some_confidential_fields_04",
                    GpmTestValues.SHEETLINK_SHEET_MULTIPLE_WITH_SOME_CONFIDENTIAL_FIELDS);
            init(GpmTestValues.PRODUCT1_NAME, "filterTestSheet_01",
                    GpmTestValues.SHEET_TYPE_FILTER_TEST_SHEET_01);
            init(GpmTestValues.PRODUCT1_NAME, "filterTestSheet_02",
                    GpmTestValues.SHEET_TYPE_FILTER_TEST_SHEET_01);
            init(GpmTestValues.PRODUCT1_NAME, "filterTestSheet_03",
                    GpmTestValues.SHEET_TYPE_FILTER_TEST_SHEET_01);
            init(GpmTestValues.PRODUCT1_NAME, "filterTestSheet_04",
                    GpmTestValues.SHEET_TYPE_FILTER_TEST_SHEET_01);
            init(GpmTestValues.PRODUCT1_NAME, "filterTestSheet_05",
                    GpmTestValues.SHEET_TYPE_FILTER_TEST_SHEET_01);
            init(GpmTestValues.PRODUCT1_NAME, "filterTestSheet_06",
                    GpmTestValues.SHEET_TYPE_FILTER_TEST_SHEET_01);
            init(GpmTestValues.PRODUCT1_NAME, "ControlType_01",
                    GpmTestValues.SHEET_TYPE_CONTROL_TYPE);
            init(GpmTestValues.PRODUCT1_NAME, "ControlType_02",
                    GpmTestValues.SHEET_TYPE_CONTROL_TYPE);
            init(GpmTestValues.PRODUCT1_NAME, "ControlType2_01",
                    GpmTestValues.SHEET_TYPE_CONTROL_TYPE2);
            init(GpmTestValues.PRODUCT1_NAME, "ControlType2_02",
                    GpmTestValues.SHEET_TYPE_CONTROL_TYPE2);
            init(GpmTestValues.PRODUCT1_NAME, "pointer:1",
                    GpmTestValues.SHEET_TYPE_TEST_POINTER_FIELDS1);
            init(GpmTestValues.PRODUCT1_NAME, "pointer:2",
                    GpmTestValues.SHEET_TYPE_TEST_POINTER_FIELDS1);
            init(GpmTestValues.PRODUCT1_NAME, "pointed:1",
                    GpmTestValues.SHEET_TYPE_TEST_POINTER_FIELDS2);
            init(GpmTestValues.PRODUCT1_NAME, "pointed:2",
                    GpmTestValues.SHEET_TYPE_TEST_POINTER_FIELDS2);
            init(GpmTestValues.PRODUCT1_NAME, "pointed:3",
                    GpmTestValues.SHEET_TYPE_TEST_POINTER_FIELDS2);
            init(GpmTestValues.PRODUCT1_NAME, "pointer:3",
                    GpmTestValues.SHEET_TYPE_TEST_POINTER_FIELDS1);
            init(GpmTestValues.PRODUCT1_NAME, "pointer:4",
                    GpmTestValues.SHEET_TYPE_TEST_POINTER_FIELDS1);
            init(GpmTestValues.PRODUCT1_NAME, "pointed:4",
                    GpmTestValues.SHEET_TYPE_TEST_POINTER_FIELDS2);
            init(GpmTestValues.PRODUCT1_NAME, "pointed:5",
                    GpmTestValues.SHEET_TYPE_TEST_POINTER_FIELDS2);
            init(GpmTestValues.PRODUCT1_NAME, "pointer:6",
                    GpmTestValues.SHEET_TYPE_TEST_POINTER_FIELDS1);
            init(
                    GpmTestValues.PRODUCT1_NAME,
                    "ref1",
                    GpmTestValues.SHEET_TYPE_SHEET_TEST_WITH_DYNAMIC_CRITERIA_VALUE);
            init(
                    GpmTestValues.PRODUCT1_NAME,
                    "ref2",
                    GpmTestValues.SHEET_TYPE_SHEET_TEST_WITH_DYNAMIC_CRITERIA_VALUE);
            init(
                    GpmTestValues.PRODUCT1_NAME,
                    "ref3",
                    GpmTestValues.SHEET_TYPE_SHEET_TEST_WITH_DYNAMIC_CRITERIA_VALUE);
            init(GpmTestValues.PRODUCT1_NAME, "type_vfd_01_01",
                    GpmTestValues.SHEET_TYPE_TYPE_VFD_01);
            init(GpmTestValues.PRODUCT1_NAME, "type_vfd_02_01",
                    GpmTestValues.SHEET_TYPE_TYPE_VFD_02);
            init(GpmTestValues.PRODUCT1_NAME, "type_vfd_01_02",
                    GpmTestValues.SHEET_TYPE_TYPE_VFD_01);
            init(GpmTestValues.PRODUCT1_NAME, "type_vfd_02_02",
                    GpmTestValues.SHEET_TYPE_TYPE_VFD_02);
            init(GpmTestValues.PRODUCT_STORE1_NAME,
                    "type_vfd_02_anotherProduct",
                    GpmTestValues.SHEET_TYPE_TYPE_VFD_02);
            init(GpmTestValues.PRODUCT1_NAME, "A",
                    GpmTestValues.SHEET_TYPE_FILTER_SHEET_TEST);
            init(GpmTestValues.PRODUCT1_NAME, "B",
                    GpmTestValues.SHEET_TYPE_FILTER_SHEET_TEST);
            init(GpmTestValues.PRODUCT_PRODUCT2, "A",
                    GpmTestValues.SHEET_TYPE_FILTER_SHEET_TEST);
            init(GpmTestValues.PRODUCT_PRODUCT2, "B",
                    GpmTestValues.SHEET_TYPE_FILTER_SHEET_TEST);
            init(GpmTestValues.PRODUCT_BERNARD_STORE_NAME,
                    "OrderByCategorySheet_Sheet1",
                    GpmTestValues.SHEET_TYPE_ORDER_BY_CATEGORY_SHEET);
            init(GpmTestValues.PRODUCT_BERNARD_STORE_NAME,
                    "OrderByCategorySheet_Sheet2",
                    GpmTestValues.SHEET_TYPE_ORDER_BY_CATEGORY_SHEET);
            init(GpmTestValues.PRODUCT_BERNARD_STORE_NAME,
                    "OrderByCategorySheet_Sheet3",
                    GpmTestValues.SHEET_TYPE_ORDER_BY_CATEGORY_SHEET);
            init(GpmTestValues.PRODUCT_BERNARD_STORE_NAME,
                    "OrderByCategorySheet_Sheet4",
                    GpmTestValues.SHEET_TYPE_ORDER_BY_CATEGORY_SHEET);
            init(GpmTestValues.PRODUCT_HAPPY_MOUSE_NAME, "SheetType1:4",
                    GpmTestValues.SHEET_TYPE_SHEETTYPE1);
            init(GpmTestValues.PRODUCT_HAPPY_MOUSE_NAME, "SheetType1:6",
                    GpmTestValues.SHEET_TYPE_SHEETTYPE1);
            init(GpmTestValues.PRODUCT_HAPPY_MOUSE_NAME, "Cat1",
                    GpmTestValues.SHEET_TYPE_CAT);
            init(GpmTestValues.PRODUCT_HAPPY_MOUSE_NAME, "Cat3",
                    GpmTestValues.SHEET_TYPE_CAT);
            init(GpmTestValues.PRODUCT_BERNARD_STORE_NAME, "SimpleShee1",
                    GpmTestValues.SHEET_TYPE_SIMPLE_SHEET_TYPE1);
            init(GpmTestValues.PRODUCT_BERNARD_STORE_NAME, "SimpleShee2",
                    GpmTestValues.SHEET_TYPE_SIMPLE_SHEET_TYPE2);
            init(GpmTestValues.PRODUCT_BERNARD_STORE_NAME,
                    "SheetWithoutDefinedValues:0",
                    GpmTestValues.SHEET_TYPE_SHEETTYPE1);
            init(GpmTestValues.PRODUCT_STORE1_NAME, "Sheet_1:1",
                    GpmTestValues.SHEET_TYPE_SHEETTYPE1);
            init(GpmTestValues.PRODUCT_STORE2_NAME, "Sheet_2:2",
                    GpmTestValues.SHEET_TYPE_SHEETTYPE1);
            init(GpmTestValues.PRODUCT_STORE1_1_NAME, "Sheet_1_1:3",
                    GpmTestValues.SHEET_TYPE_SHEETTYPE1);
            init(GpmTestValues.PRODUCT_HAPPY_MOUSE_NAME, "Tom",
                    GpmTestValues.SHEET_TYPE_CAT);
            init(GpmTestValues.PRODUCT_HAPPY_MOUSE_NAME,
                    GpmTestValues.SHEET_REF_GARFIELD,
                    GpmTestValues.SHEET_TYPE_CAT);
            init(GpmTestValues.PRODUCT_HAPPY_MOUSE_NAME,
                    "Famous Cat with credit", GpmTestValues.SHEET_TYPE_PRICE);
            init(GpmTestValues.PRODUCT_BERNARD_STORE_NAME, "Lassie",
                    GpmTestValues.SHEET_TYPE_DOG);
            init(GpmTestValues.PRODUCT_BERNARD_STORE_NAME, "Snoopy",
                    GpmTestValues.SHEET_TYPE_DOG);
            init(GpmTestValues.PRODUCT_BERNARD_STORE_NAME, "Mickey",
                    GpmTestValues.SHEET_TYPE_MOUSE);
            init(GpmTestValues.PRODUCT_BERNARD_STORE_NAME, "Dog 800",
                    GpmTestValues.SHEET_TYPE_PRICE);
            init(GpmTestValues.PRODUCT_BERNARD_STORE_NAME, "Price1",
                    GpmTestValues.SHEET_TYPE_PRICE);
            init(GpmTestValues.PRODUCT_STORE1_NAME, "testLargeString",
                    GpmTestValues.SHEET_TYPE_CAT);
            init(GpmTestValues.PRODUCT_HAPPY_MOUSE_NAME, "SheetType1:5",
                    GpmTestValues.SHEET_TYPE_SHEETTYPE1);
            init(GpmTestValues.PRODUCT_HAPPY_MOUSE_NAME, "Sheet24:7",
                    GpmTestValues.SHEET_TYPE_SHEETTYPE1);
            init(GpmTestValues.PRODUCT_HAPPY_MOUSE_NAME, "Cat2",
                    GpmTestValues.SHEET_TYPE_CAT);
            init(GpmTestValues.PRODUCT1_NAME, "SheetForTestLinkFilters1",
                    GpmTestValues.SHEET_TYPE_SIMPLE_SHEET_TYPE1);
            init(GpmTestValues.PRODUCT1_NAME, "SheetForTestLinkFilters2",
                    GpmTestValues.SHEET_TYPE_SIMPLE_SHEET_TYPE2);
            init(GpmTestValues.PRODUCT1_NAME, "SheetForTestLinkFilters3",
                    GpmTestValues.SHEET_TYPE_SIMPLE_SHEET_TYPE1);
            init(GpmTestValues.PRODUCT1_NAME, "SheetForTestLinkFilters4",
                    GpmTestValues.SHEET_TYPE_SIMPLE_SHEET_TYPE2);
            init(GpmTestValues.PRODUCT1_NAME, "SheetForTestLinkFilters5",
                    GpmTestValues.SHEET_TYPE_SIMPLE_SHEET_TYPE1);
            init(GpmTestValues.PRODUCT1_NAME, "SheetForTestLinkFilters6",
                    GpmTestValues.SHEET_TYPE_SIMPLE_SHEET_TYPE2);
            init(GpmTestValues.PRODUCT1_NAME, "SheetForTestLinkFilters7",
                    GpmTestValues.SHEET_TYPE_SIMPLE_SHEET_TYPE1);
            init(GpmTestValues.PRODUCT1_NAME, "testExportSheet_01",
                    GpmTestValues.SHEET_TYPE_TEST_EXPORT_SHEET);
            init(GpmTestValues.PRODUCT1_NAME, "testExportSheet_02",
                    GpmTestValues.SHEET_TYPE_TEST_EXPORT_SHEET);
            init(GpmTestValues.PRODUCT1_NAME, "testExportSheet_03",
                    GpmTestValues.SHEET_TYPE_TEST_EXPORT_SHEET);
            init(GpmTestValues.PRODUCT_STORE1_NAME, "testExportSheet_04",
                    GpmTestValues.SHEET_TYPE_TEST_EXPORT_SHEET);
            init(GpmTestValues.PRODUCT1_NAME, "testExportSheet_10",
                    GpmTestValues.SHEET_TYPE_TEST_EXPORT_SHEET);
            init(GpmTestValues.PRODUCT1_NAME, "testExportSheet_11",
                    GpmTestValues.SHEET_TYPE_TEST_EXPORT_SHEET);
            init(GpmTestValues.PRODUCT_STORE1_NAME, "testExportSheet_2_01",
                    GpmTestValues.SHEET_TYPE_TEST_EXPORT_SHEET_2);
            init = true;
        }
    }

    private void init(final String pProductName, final String pReference,
            final String pTypeName) {
        final String lSheetId =
                sheetService.getSheetIdByReference(getProcessName(),
                        pProductName, pReference);

        allIds.add(lSheetId);
        if (limitedProductNames.contains(pProductName)) {
            byProductIds.add(lSheetId);
        }
        if (limitedTypeNames.contains(pTypeName)) {
            byTypeIds.add(lSheetId);
        }
        if (productNamesWithRoleOn.contains(pProductName)) {
            idsWithRoleOn.add(lSheetId);
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
        pProperties.setSheetsFlag(pFlag);
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
        final CacheableSheet lSheet =
                sheetService.getCacheableSheet(pElementId,
                        CacheProperties.IMMUTABLE);

        return lSheet.getProductName() + " " + lSheet.getFunctionalReference();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.exportation.AbstractValuesContainerTestExport#getId(org.topcased.gpm.business.serialization.data.ValuesContainerData)
     */
    protected String getId(final SheetData pObject) {
        return sheetService.getSheetIdByReference(getProcessName(),
                pObject.getProductName(), pObject.getReference());
    }
}