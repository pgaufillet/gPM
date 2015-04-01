/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin)
 ******************************************************************/

package org.topcased.gpm.business;

/**
 * Definition of names, values, constants expected by the various unit tests
 * (according to the 'test' instance).
 * 
 * @author llatil
 */
public class GpmTestValues {

    /** Business process */
    public static final String PROCESS_NAME = "PET STORE";

    /** Invalid category name (this category DOES NOT exist) */
    public static final String PROCESS_INVALID_NAME = "Invalid";

    /** -- Users -- */

    public static final String USER_ADMIN = "admin";

    public static final String USER_USER1 = "user1";

    public static final String USER_USER2 = "user2";

    public static final String USER_USER3 = "user3";

    public static final String USER_USER4 = "user4";

    public static final String USER_USER5 = "user5";

    public static final String USER_USER6 = "user6";

    public static final String USER_VIEWER1 = "viewer1";

    public static final String USER_VIEWER2 = "viewer2";

    public static final String USER_VIEWER3 = "viewer3";

    public static final String USER_VIEWER4 = "viewer4";

    public static final String USER_ADMIN3 = "admin3";

    public static final String USER_NOROLE = "Norole";

    public static final String USER_ADMIN_INSTANCE = "adminInstance";

    public static final String USER_ADMIN_PRODUCT = "adminProduct";

    /** List of users login defined in the instance */
    public static final String[] USERS_LOGIN =
            { USER_ADMIN, USER_USER1, USER_USER2, USER_USER3, USER_USER4,
             USER_USER5, USER_USER6, USER_VIEWER1, USER_VIEWER2, USER_VIEWER3,
             USER_VIEWER4, USER_ADMIN3, USER_NOROLE, USER_ADMIN_INSTANCE,
             USER_ADMIN_PRODUCT };

    public static final int USERS_COUNT = USERS_LOGIN.length;

    /**
     * Dictionary / Environments
     */

    /** The environments names. */

    public static final String ENVIRONMENT_PROFESSIONAL = "Professional";

    public static final String ENVIRONMENT_CLASSICAL = "Classical";

    public static final String ENVIRONMENT_ENV1 = "Env1";

    public static final String ENVIRONMENT_ENV2 = "Env2";

    public static final String ENVIRONMENT_INVALID_ENV = "invalid_env";

    public static final String PRIVATE_ENVIRONMENT_NAME = "Classical_1";

    public static final String[] ENVIRONMENT_NAMES =
            { ENVIRONMENT_PROFESSIONAL, ENVIRONMENT_CLASSICAL,
             ENVIRONMENT_ENV1, ENVIRONMENT_ENV2 };

    /** The categories names */
    public static final String CATEGORY_COLOR = "Color";

    public static final String CATEGORY_LENGTH = "Length";

    public static final String CATEGORY_CAT_PEDIGRE = "CAT_pedigre";

    public static final String CATEGORY_USAGE = "Usage";

    public static final String CATEGORY_ERROR_LEVEL = "ErrorLevel";

    public static final String CATEGORY_FILTER_TEST_SHEET_01_CHOICE_CATEGORY =
            "FILTER_TEST_SHEET_01_CHOICE_CATEGORY";

    public static final String CATEGORY_FILTER_TEST_SHEET_02_CHOICE_CATEGORY =
            "FILTER_TEST_SHEET_02_CHOICE_CATEGORY";

    public static final String CATEGORY_TEST_IMAGE_TEXT_DISPLAYHINT_01_CATEGORY =
            "Test_image_text_displayHint_01_CATEGORY";

    public static final String CATEGORY_CATEGORY_DEFINITION =
            "CategoryDefinition";

    /** Invalid category name (this category DOES NOT exist in the dictionary) */
    public static final String CATEGORY_INVALID = "Invalid";

    public static final String[] CATEGORIES_NAME =
            { CATEGORY_COLOR, CATEGORY_LENGTH, CATEGORY_CAT_PEDIGRE,
             CATEGORY_USAGE, CATEGORY_ERROR_LEVEL,
             CATEGORY_FILTER_TEST_SHEET_01_CHOICE_CATEGORY,
             CATEGORY_FILTER_TEST_SHEET_02_CHOICE_CATEGORY,
             CATEGORY_TEST_IMAGE_TEXT_DISPLAYHINT_01_CATEGORY,
             CATEGORY_CATEGORY_DEFINITION };

    /**
     * Category values
     */
    public static final String CATEGORY_COLOR_VALUE_WHITE = "WHITE";

    public static final String CATEGORY_COLOR_VALUE_GREY = "GREY";

    public static final String CATEGORY_COLOR_VALUE_GREEN = "GREEN";

    public static final String CATEGORY_COLOR_VALUE_YELLOW = "YELLOW";

    public static final String CATEGORY_COLOR_VALUE_RED = "RED";

    public static final String CATEGORY_COLOR_VALUE_BLACK = "BLACK";

    /** The Color category values. */
    public static final String[] COLOR_VALUES =
            { CATEGORY_COLOR_VALUE_WHITE, CATEGORY_COLOR_VALUE_GREY,
             CATEGORY_COLOR_VALUE_GREEN, CATEGORY_COLOR_VALUE_YELLOW,
             CATEGORY_COLOR_VALUE_RED, CATEGORY_COLOR_VALUE_BLACK };

    public static final String[] COLOR_VALUES_FOR_ENV =
            { CATEGORY_COLOR_VALUE_RED, CATEGORY_COLOR_VALUE_BLACK };

    /** Number of values defined in dictionary for the 'color' category */
    public static final int COLOR_VALUES_COUNT = COLOR_VALUES.length;

    public static final String CATEGORY_USAGE_VALUE_SELF_DEFENSE =
            "Self Defense";

    public static final String CATEGORY_USAGE_VALUE_POLICE = "Police";

    public static final String CATEGORY_USAGE_VALUE_CHILDREN = "Children";

    /** The Usage category values. */
    public static final String[] USAGE_VALUES =
            { CATEGORY_USAGE_VALUE_SELF_DEFENSE, CATEGORY_USAGE_VALUE_POLICE,
             CATEGORY_USAGE_VALUE_CHILDREN };

    /** Number of values defined in dictionary for the 'usage' category */
    public static final int USAGE_VALUES_COUNT = USAGE_VALUES.length;

    /**
     * Products
     */
    public static final String PRODUCT_BERNARD_STORE_NAME = "Bernard's store";

    public static final String PRODUCT1_NAME = "product1";

    public static final String PRODUCT_HAPPY_MOUSE_NAME = "Happy Mouse";

    public static final String PRODUCT_STORE1_NAME = "store1";

    public static final String PRODUCT_STORE1_1_NAME = "store1_1";

    public static final String PRODUCT_STORE2_NAME = "store2";

    public static final String PRODUCT_ENVIRONMENT_TEST_STORE =
            "environment test store";

    public static final String PRODUCT_SUBSTORE = "substore";
    
    public static final String PRODUCT_PRODUCT1 = "product1";

    public static final String PRODUCT_PRODUCT1_1 = "product1_1";

    public static final String PRODUCT_PRODUCT1_2 = "product1_2";

    public static final String PRODUCT_PRODUCT2 = "product2";

    public static final String PRODUCT_PRODUCT3 = "product3";
    
    public static final String PRODUCT_4 = "product4";
    
    public static final String PRODUCT_CHEZ_MIMOUN = "ChezMimoun";

    public static final String PRODUCT_PRODUCT_WITH_NO_USERS =
            "productWithNoUsers";

    public static final String[] PRODUCT_ATTR_NAMES =
            { "productAttr1", "productAttr2" };

    public static final String[] PRODUCT_ATTR1_VALUES = { "val1" };

    public static final String[] PRODUCT_ATTR2_VALUES = { "multi1", "multi2" };

    public static final String[] FALSE_PRODUCT_ATTR_NAMES =
            { "WrongprodAttr1", "WrongprodAttr2" };

    /** Names of all products defined in the instance. */
    public static final String[] PRODUCT_NAMES =
            { PRODUCT_BERNARD_STORE_NAME, PRODUCT_STORE1_NAME,
             PRODUCT_STORE1_1_NAME, PRODUCT_STORE2_NAME,
             PRODUCT_HAPPY_MOUSE_NAME, PRODUCT_ENVIRONMENT_TEST_STORE,
             PRODUCT_SUBSTORE, PRODUCT1_NAME, PRODUCT_PRODUCT1_1,
             PRODUCT_PRODUCT1_2, PRODUCT_PRODUCT2, PRODUCT_PRODUCT3,
             PRODUCT_PRODUCT_WITH_NO_USERS };

    public static final String[] PRODUCT1_SUBPRODUCTS =
            { PRODUCT_PRODUCT1_1, PRODUCT_PRODUCT1_2 };

    /* -- Sheet types -- */

    public static final String SHEET_TYPE_CAT = "Cat";

    public static final String SHEET_TYPE_MOUSE = "Mouse";

    public static final String SHEET_TYPE_DOG = "Dog";

    public static final String SHEET_TYPE_PRICE = "Price";

    public static final String SHEET_TYPE_SHEETTYPE1 = "SheetType1";

    public static final String SHEET_TYPE_SHEET_TYPE_WITH_MANDATORY_VALUES =
            "SheetType_withMandatoryValues";

    public static final String SHEET_TYPE_SIMPLE_SHEET_TYPE1 =
            "SimpleSheetType1";

    public static final String SHEET_TYPE_SIMPLE_SHEET_TYPE2 =
            "SimpleSheetType2";

    public static final String SHEET_TYPE_SIMPLE_SHEET_TYPE3 =
            "SimpleSheetType3";

    public static final String SHEET_TYPE_DOJO_TEST = "DOJO_Test";

    public static final String SHEET_TYPE_GRID_SIMPLE_TEST = "Grid_Single_Test";

    public static final String SHEET_TYPE_GRID_MULTIVALUE_TEST =
            "Grid_Multivalue_Test";

    public static final String SHEET_TYPE_GRID_DATE_BOOLEAN_TEST =
            "Grid_StringDateBoolean_Test";

    public static final String SHEET_TYPE_FILTER_TEST_SHEET_01 =
            "FILTER_TEST_SHEET_01";

    public static final String SHEET_TYPE_TEST_IMAGE_TEXT_DISPLAYHINT_01 =
            "Test_image_text_displayHint_01";

    public static final String SHEET_TYPE_CONTROL_TYPE = "ControlType";

    public static final String SHEET_TYPE_CONTROL_TYPE2 = "ControlType2";

    public static final String SHEET_TYPE_TEST_CHOICE_STRING_DISPLAYHINT_01 =
            "Test_choicestring_displayHint_01";

    public static final String SHEET_TYPE_TEST_DESCRIPTION_01 =
            "Test_description_01";

    public static final String SHEET_TYPE_ORDER_BY_CATEGORY_SHEET =
            "OrderByCategorySheet";

    public static final String SHEET_TYPE_CHOICE_MULTIVALUED =
            "Test_ChoiceMultiValued";

    public static final String SHEET_TYPE_SHEET_WITH_SOME_CONFIDENTIAL_FIELDS =
            "SheetWithSomeConfidentialFields";

    public static final String SHEET_TYPE_SHEET_MULTIPLE_WITH_SOME_CONFIDENTIAL_FIELDS =
            "SheetMultipleWithSomeConfidentialFields";

    public static final String SHEET_TYPE_TEST_MULTIVALUED_FIELD =
            "test_multivalued_field";

    public static final String SHEET_TYPE_TEST_DATE_DISPLAYHINT =
            "TestDateDisplayHint";

    public static final String SHEET_TYPE_TEST_POINTER_FIELDS1 =
            "TestPointerFields1";

    public static final String SHEET_TYPE_TEST_POINTER_FIELDS2 =
            "TestPointerFields2";

    public static final String SHEET_TYPE_TEST_AUTOLOCKING_SHEET =
            "TestAutolockingSheet";

    public static final String SHEET_TYPE_FILTER_SHEET_TEST = "FilterSheetTest";

    public static final String SHEET_TYPE_SHEET_TEST_WITH_DYNAMIC_CRITERIA_VALUE =
            "SheetTestWithDynamicCriteriaValue";

    public static final String SHEET_TYPE_TYPE_VFD_01 = "type_vfd_01";

    public static final String SHEET_TYPE_TYPE_VFD_02 = "type_vfd_02";

    public static final String SHEET_TYPE_SHEET_USABLE_FIELD_ORDER =
            "SheetUsableFieldOrder";

    public static final String SHEET_TYPE_MAX_SIZE = "maxSize";

    public static final String SHEET_TYPE_TEST_EXPORT_SHEET = "testExportSheet";

    public static final String SHEET_TYPE_TEST_EXPORT_SHEET_2 =
            "testExportSheet_2";

    public static final String SHEET_TYPE_CONTAINER_SHEET_TYPE_AC_1 =
            "CONTAINER_SHEET_TYPE_AC_1";

    public static final String SHEET_TYPE_EXCEL_SIZE_TEST = "excelSizeTest";

    public static final String SAME_SHEET_TYPE_01 = "sameSheetType_01";

    public static final String SAME_SHEET_TYPE_02 = "sameSheetType_02";

    public static final String SAME_SHEET_TYPEM_01 = "sameSheetTypeM_01";

    public static final String SAME_SHEET_TYPEM_02 = "sameSheetTypeM_02";
    
    // New added.
    public static final String STARTER = "Starter";
    public static final String MAIN_DISH = "MainDish";
    public static final String DESSERT = "Dessert";
    public static final String CONTROL_TYPE_IMPORT_3 = "ControlTypeImport3";
    public static final String CONTROL_TYPE_IMPORT_2 = "ControlTypeImport2";
    public static final String CONTROL_TYPE_IMPORT = "ControlTypeImport";
    public static final String CHIEF = "Chief";

    /** Expected sheet types names. */
    public static final String[] SHEET_TYPE_NAMES =
            { SHEET_TYPE_CAT, SHEET_TYPE_MOUSE, SHEET_TYPE_DOG,
             SHEET_TYPE_PRICE, SHEET_TYPE_SHEETTYPE1,
             SHEET_TYPE_SHEET_TYPE_WITH_MANDATORY_VALUES,
             SHEET_TYPE_SIMPLE_SHEET_TYPE1, SHEET_TYPE_SIMPLE_SHEET_TYPE2,
             SHEET_TYPE_SIMPLE_SHEET_TYPE3, SHEET_TYPE_DOJO_TEST,
             SHEET_TYPE_GRID_SIMPLE_TEST, SHEET_TYPE_GRID_MULTIVALUE_TEST,
             SHEET_TYPE_GRID_DATE_BOOLEAN_TEST,
             SHEET_TYPE_FILTER_TEST_SHEET_01,
             SHEET_TYPE_TEST_IMAGE_TEXT_DISPLAYHINT_01,
             SHEET_TYPE_CONTROL_TYPE, SHEET_TYPE_CONTROL_TYPE2,
             SHEET_TYPE_TEST_CHOICE_STRING_DISPLAYHINT_01,
             SHEET_TYPE_TEST_DESCRIPTION_01,
             SHEET_TYPE_ORDER_BY_CATEGORY_SHEET, SHEET_TYPE_CHOICE_MULTIVALUED,
             SHEET_TYPE_SHEET_WITH_SOME_CONFIDENTIAL_FIELDS,
             SHEET_TYPE_SHEET_MULTIPLE_WITH_SOME_CONFIDENTIAL_FIELDS,
             SHEET_TYPE_TEST_MULTIVALUED_FIELD,
             SHEET_TYPE_TEST_DATE_DISPLAYHINT, SHEET_TYPE_TEST_POINTER_FIELDS1,
             SHEET_TYPE_TEST_POINTER_FIELDS2,
             SHEET_TYPE_TEST_AUTOLOCKING_SHEET, SHEET_TYPE_FILTER_SHEET_TEST,
             SHEET_TYPE_SHEET_TEST_WITH_DYNAMIC_CRITERIA_VALUE,
             SHEET_TYPE_TYPE_VFD_01, SHEET_TYPE_TYPE_VFD_02,
             SHEET_TYPE_SHEET_USABLE_FIELD_ORDER, SHEET_TYPE_MAX_SIZE,
             SHEET_TYPE_TEST_EXPORT_SHEET, SHEET_TYPE_TEST_EXPORT_SHEET_2,
             SHEET_TYPE_CONTAINER_SHEET_TYPE_AC_1, SHEET_TYPE_EXCEL_SIZE_TEST,
             SAME_SHEET_TYPE_01, SAME_SHEET_TYPE_02, SAME_SHEET_TYPEM_01,
             SAME_SHEET_TYPEM_02, STARTER, MAIN_DISH, 
             DESSERT, CONTROL_TYPE_IMPORT_3, 
             CONTROL_TYPE_IMPORT_2, CONTROL_TYPE_IMPORT, CHIEF};

    public static final String SHEET_REF_GARFIELD = "Garfield";

    public static final String SHEET_REF_FAMOUS_CAT = "Famous Cat";

    /** Some expected created sheets ref */
    public static final String[] CREATED_SHEET_REF =
            { SHEET_REF_GARFIELD, SHEET_REF_FAMOUS_CAT };

    // SheetType1 states
    public static final String SHEET_TYPE1_START_STATE = "Start";

    public static final String SHEET_TYPE1_FINAL_STATE = "Closed";

    /** Sheets functional ref */
    public static final String SHEETREF_WITHOUT_DEFINED_VALUES =
            "SheetWithoutDefinedValues:0";

    /* -- Sheets -- */

    /** 'SheetType1' sheets references */
    public static final String SHEET1_REF = "Sheet_1:1";

    public static final String SHEET1_PRODUCT = "store1";

    public static final String SHEET2_REF = "Sheet_2:2";

    public static final String SHEET2_PRODUCT = "store2";

    public static final String SHEET1_1_REF = "Sheet_1_1:3";

    public static final String SHEET1_1_PRODUCT = "store1_1";

    public static final String INVALID_SHEET_REF = "Invalid_Sheet_Ref";

    /* -- FILTERS -- */

    public static final String FILTER_HMI_FILTER_ON_SEVERAL_LEVEL =
            "HMI - FilterOnSeveralLevel";

    public static final String FILTER_OPENED_CATS = "OPENED_CATS";

    public static final String FILTER_SHEETS_WITH_POINTERS =
            "SHEETS_WITH_POINTERS";

    public static final String FILTER_SHEETTYPE1 = "SHEETTYPE1";

    public static final String FILTER_TEST_FILTER_1 = "TEST_FILTER_1";

    public static final String FILTER_TEST_FILTER_2 = "TEST_FILTER_2";

    public static final String FILTER_TEST_REMOVEFILTER_CF_004_FILTER_INSTANCE =
            "Test_RemoveFilter_CF_004_FILTER_INSTANCE";

    public static final String FILTER_TEST_FILTER_WITH_SAME_NAME =
            "TEST_FILTER_WITH_SAME_NAME";

    public static final String FILTER_ALL_SHEETS = "ALL_SHEETS";

    public static final String FILTER_TEST_MIGRATION = "TestMigration";

    public static final String FILTER_TYPE_VFD_SHEET_STATE_FILTER =
            "type_vfd_sheet_state_Filter";

    public static final String FILTER_TYPE_VFD_SHEET_TYPE_FILTER =
            "type_vfd_sheet_type_Filter";

    public static final String FILTER_TYPE_VFD_SHEET_REFERENCE_FILTER =
            "type_vfd_sheet_reference_Filter";

    public static final String FILTER_LONG_STRING_VALUE_SHEETS =
            "long string value sheets";

    /** Name of the filters defined in instance */
    public static final String[] INSTANCE_FILTER_NAMES =
            { FILTER_HMI_FILTER_ON_SEVERAL_LEVEL, FILTER_OPENED_CATS,
             FILTER_SHEETS_WITH_POINTERS, FILTER_SHEETTYPE1,
             FILTER_TEST_FILTER_1, FILTER_TEST_FILTER_2,
             FILTER_TEST_REMOVEFILTER_CF_004_FILTER_INSTANCE,
             FILTER_TEST_FILTER_WITH_SAME_NAME, FILTER_ALL_SHEETS,
             FILTER_TEST_MIGRATION, FILTER_TYPE_VFD_SHEET_STATE_FILTER,
             FILTER_TYPE_VFD_SHEET_TYPE_FILTER,
             FILTER_TYPE_VFD_SHEET_REFERENCE_FILTER,
             FILTER_LONG_STRING_VALUE_SHEETS };

    /** Name of the filters defined for admin user */
    public static final String[] ADMIN_FILTER_NAMES =
            { FILTER_TEST_FILTER_WITH_SAME_NAME };

    /**
     * Numbers of filters defined in the instance (this includes the hidden
     * filters and filters defined for admin user only)
     */
    public static final int FILTER_NUMBER =
            INSTANCE_FILTER_NAMES.length + ADMIN_FILTER_NAMES.length;

    public static final int TABLE_FILTER_INDEX = 4;

    /* SHEET LINKS */

    public static final String SHEETLINK_CAT_PRICE = "CAT_PRICE";

    public static final String SHEETLINK_PRICE_PRICE = "PRICE_PRICE";

    public static final String SHEETLINK_SHEETTYPE1_CAT = "SHEETTYPE1_CAT";

    public static final String SHEETLINK_LINK_TYPE1_TYPE1 = "Link-Type1-Type1";

    public static final String SHEETLINK_LINK_TYPE1_TYPE2 = "Link-Type1-Type2";

    public static final String SHEETLINK_CAT_CAT = "Cat-Cat";

    public static final String SHEETLINK_LINK_TEST = "LinkTest";

    public static final String SHEETLINK_SHEET_MULTIPLE_WITH_SOME_CONFIDENTIAL_FIELDS =
            "SheetMultipleWithSomeConfidentialFields";

    /** Expected sheet link types names. */
    public static final String[] SHEET_LINK_TYPE_NAMES =
            { SHEETLINK_CAT_PRICE, SHEETLINK_PRICE_PRICE,
             SHEETLINK_SHEETTYPE1_CAT, SHEETLINK_LINK_TYPE1_TYPE1,
             SHEETLINK_LINK_TYPE1_TYPE2, SHEETLINK_CAT_CAT,
             SHEETLINK_LINK_TEST,
             SHEETLINK_SHEET_MULTIPLE_WITH_SOME_CONFIDENTIAL_FIELDS };

    /**
     * Login and password for an admin access defined on Instance
     */
    public static final String[] ADMIN_INSTANCE_LOGIN_PWD =
            { "adminInstance", "pwd" };

    /**
     * Login and password for admin access defined on Product
     */
    public static final String[] ADMIN_PRODUCT_LOGIN_PWD =
            { "adminProduct", "pwd" };

    /**
     * Login and password for a non admin and no admin access defined
     */
    public static final String[] NO_ADMIN_LOGIN_PWD = { "viewer4", "pwd4" };

    /**
     * The administrator role
     */
    public static final String ADMINISTRATOR_ROLE = "administrator";

    /**
     * The viewer role
     */
    public static final String VIEWER_ROLE = "viewer";

    /**
     * Name of a category with user access.
     */
    public static final String CATEGORY_USER_NAME = "ErrorLevel";

}
