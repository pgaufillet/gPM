/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.fields;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.dictionary.CategoryValueData;
import org.topcased.gpm.business.extensions.service.Context;
import org.topcased.gpm.business.product.impl.CacheableProduct;
import org.topcased.gpm.business.product.service.ProductService;
import org.topcased.gpm.business.sheet.impl.CacheableSheet;
import org.topcased.gpm.business.sheet.impl.CacheableSheetType;
import org.topcased.gpm.business.sheet.service.SheetService;
import org.topcased.gpm.business.values.ValuesAccessProperties;
import org.topcased.gpm.business.values.sheet.BusinessSheet;
import org.topcased.gpm.business.values.sheet.impl.cacheable.CacheableSheetAccess;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * Tests the update of field multivalued choice fields.
 * 
 * @author nveillet
 */
public class TestUpdateChoiceField extends AbstractBusinessServiceTestCase {

    /** The Sheet Service. */
    private SheetService sheetService;

    private ProductService productService;

    /** The field name. */
    private static final String FIELD_NAME_SIMPLE_CHOICE =
            "Test_ChoiceMultiValued_choice";

    /** The field name. */
    private static final String FIELD_NAME_MULTIPLE =
            "Test_ChoiceMultiValued_multiple1";

    /** The field name. */
    private static final String FIELD_NAME_MULTIPLE_CHOICE =
            "Test_ChoiceMultiValued_choiceM";

    /** The field name. */
    private static final String FIELD_NAME_MULTIPLE_MULTIVALUED =
            "Test_ChoiceMultiValued_multiple1M";

    /** The field name. */
    private static final String FIELD_NAME_MULTIPLE_MULTIVALUED_CHOICE =
            "Test_ChoiceMultiValued_choiceMM";

    /** The field category name. */
    private static final String FIELD_CATEGORY_NAME =
            GpmTestValues.CATEGORY_COLOR;

    /**
     * Tests the createChoiceField method.
     */
    public void testUpdateMultivaluedCase() {
        // Gets the fields service and the sheet service.
        sheetService = serviceLocator.getSheetService();
        productService = serviceLocator.getProductService();

        // get sheet
        CacheableSheetType lCacheableSheetType =
                sheetService.getCacheableSheetTypeByName(normalRoleToken,
                        GpmTestValues.SHEET_TYPE_NAMES[19],
                        CacheProperties.IMMUTABLE);
        BusinessSheet lSheet =
                getBusinessSheet(lCacheableSheetType,
                        sheetService.getCacheableSheetModel(normalRoleToken,
                                lCacheableSheetType, getProductName(), null));

        // get category values
        CacheableProduct lProduct =
                productService.getCacheableProduct(normalRoleToken,
                        productService.getProductId(normalRoleToken,
                                getProductName()), CacheProperties.IMMUTABLE);
        CategoryValueData[] lCategoryValues =
                serviceLocator.getEnvironmentService().getEnvironmentCategory(
                        normalRoleToken, getProcessName(),
                        lProduct.getEnvironmentNames().get(0),
                        FIELD_CATEGORY_NAME).getCategoryValueDatas();

        // fill choice fields with all values
        for (CategoryValueData lValues : lCategoryValues) {
            lSheet.getMultivaluedChoiceField(FIELD_NAME_SIMPLE_CHOICE).addLine().setCategoryValue(
                    lValues.getValue());
            lSheet.getMultipleField(FIELD_NAME_MULTIPLE).getMultivaluedChoiceField(
                    FIELD_NAME_MULTIPLE_CHOICE).addLine().setCategoryValue(
                    lValues.getValue());
            lSheet.getMultivaluedMultipleField(FIELD_NAME_MULTIPLE_MULTIVALUED)
                .getFirst().getMultivaluedChoiceField(
                    FIELD_NAME_MULTIPLE_MULTIVALUED_CHOICE).addLine().setCategoryValue(
                    lValues.getValue());
        }

        // Save sheet
        String lSheetId =
                sheetService.createSheet(normalRoleToken,
                        getCacheableSheet(lSheet), Context.getEmptyContext());

        // get saved sheet
        lSheet =
                getBusinessSheet(lCacheableSheetType,
                        sheetService.getCacheableSheet(normalRoleToken,
                                lSheetId, CacheProperties.MUTABLE));

        // check choice fields values
        assertEquals(lCategoryValues.length, lSheet.getMultivaluedChoiceField(
                FIELD_NAME_SIMPLE_CHOICE).size());
        assertEquals(lCategoryValues.length, lSheet.getMultipleField(
                FIELD_NAME_MULTIPLE).getMultivaluedChoiceField(
                FIELD_NAME_MULTIPLE_CHOICE).size());
        assertEquals(
                lCategoryValues.length,
                lSheet.getMultivaluedMultipleField(
                        FIELD_NAME_MULTIPLE_MULTIVALUED).getFirst().getMultivaluedChoiceField(
                        FIELD_NAME_MULTIPLE_MULTIVALUED_CHOICE).size());

        // update choice fields with all values (unfill)
        lSheet.getMultivaluedChoiceField(FIELD_NAME_SIMPLE_CHOICE).clear();
        lSheet.getMultipleField(FIELD_NAME_MULTIPLE).getMultivaluedChoiceField(
                FIELD_NAME_MULTIPLE_CHOICE).clear();
        lSheet.getMultivaluedMultipleField(FIELD_NAME_MULTIPLE_MULTIVALUED)
            .getFirst().getMultivaluedChoiceField(
                FIELD_NAME_MULTIPLE_MULTIVALUED_CHOICE).clear();

        // Save sheet
        sheetService.updateSheet(normalRoleToken, getCacheableSheet(lSheet),
                Context.getEmptyContext());

        // get saved sheet
        lSheet =
                getBusinessSheet(lCacheableSheetType,
                        sheetService.getCacheableSheet(normalRoleToken,
                                lSheetId, CacheProperties.IMMUTABLE));

        // check choice fields values
        assertEquals(0, lSheet.getMultivaluedChoiceField(
                FIELD_NAME_SIMPLE_CHOICE).size());
        assertEquals(
                0,
                lSheet.getMultipleField(FIELD_NAME_MULTIPLE).getMultivaluedChoiceField(
                        FIELD_NAME_MULTIPLE_CHOICE).size());
        assertEquals(
                0,
                lSheet.getMultivaluedMultipleField(
                        FIELD_NAME_MULTIPLE_MULTIVALUED).getFirst().getMultivaluedChoiceField(
                        FIELD_NAME_MULTIPLE_MULTIVALUED_CHOICE).size());
    }

    /**
     * Get business sheet from cacheable sheet
     * 
     * @param pCacheableSheetType
     *            The cacheable sheet type
     * @param pCacheableSheet
     *            The cacheable sheet
     * @return The business sheet
     */
    private BusinessSheet getBusinessSheet(
            CacheableSheetType pCacheableSheetType,
            CacheableSheet pCacheableSheet) {
        BusinessSheet lSheet =
                new CacheableSheetAccess(normalRoleToken, pCacheableSheetType,
                        pCacheableSheet,
                        ValuesAccessProperties.NOT_CHECKED_READ_OR_WRITE);
        return lSheet;
    }

    /**
     * Get cacheable sheet from business sheet
     * 
     * @param pBusinessSheet
     *            The business sheet
     * @return The cacheable sheet
     */
    private CacheableSheet getCacheableSheet(BusinessSheet pBusinessSheet) {
        return ((CacheableSheetAccess) pBusinessSheet).read();
    }
}