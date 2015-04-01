/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas Samson (Atos Origin),
 * Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.environment;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.dictionary.CategoryAccessData;
import org.topcased.gpm.business.dictionary.CategoryData;
import org.topcased.gpm.business.dictionary.CategoryValueData;
import org.topcased.gpm.business.environment.service.EnvironmentService;
import org.topcased.gpm.business.exception.AuthorizationException;
import org.topcased.gpm.business.exception.UndeletableValuesException;
import org.topcased.gpm.business.serialization.data.CategoryValue;

/**
 * Tests the method <CODE>setDictionaryCategory<CODE> of the Environment
 * Service.
 * 
 * @author nsamson
 */
public class TestSetDictionaryCategoryService extends
        AbstractBusinessServiceTestCase {

    /** The Environment Service. */
    private EnvironmentService environmentService;

    /** The Label of category. */
    private static final String CATEGORY_LABEL = "CategoryTest";

    /** The values. */
    private static final String[] VALUES = { "value1", "value2", "value3" };

    /** The values. */
    private static final String[] NEW_VALUES =
            { "value1bis", "value2bis", "value3bis" };

    /** A used category */
    private static final String CATEGORY_USED_LABEL =
            GpmTestValues.CATEGORY_COLOR;

    @Override
    protected void setUp() {
        super.setUp();
        environmentService = serviceLocator.getEnvironmentService();
    }

    /**
     * Tests the setDictionaryCategory method.
     */
    public void testNormalCase() {
        // Defining the category in the dictionary the categories
        int lValuesSize = VALUES.length;
        CategoryValueData[] lCVDs = new CategoryValueData[lValuesSize];
        for (int i = 0; i < lValuesSize; i++) {
            lCVDs[i] = new CategoryValueData(VALUES[i]);
        }
        CategoryData lCategory =
                new CategoryData(null, CATEGORY_LABEL, "", getProcessName(),
                        CategoryAccessData.PROCESS, lCVDs);
        environmentService.setDictionaryCategory(adminRoleToken, lCategory,
                true);

        // Retrieve the category
        Collection<CategoryData> lCategories =
                environmentService.getCategories(adminRoleToken,
                        getProcessName());

        assertNotNull("Method getCategories returns null.", lCategories);

        // Verify the values are corrects
        boolean lCategorySetted = false;
        for (CategoryData lCategoryData : lCategories) {
            if (lCategoryData.getLabelKey().equals(CATEGORY_LABEL)) {
                lCategorySetted = true;
                assertNotNull("The i18nName is incorrect.",
                        lCategoryData.getI18nName());
                CategoryValueData[] lCVDatas =
                        lCategoryData.getCategoryValueDatas();
                assertEquals("Number of values is not the number expected.",
                        lCVDs.length, lCVDatas.length);
                for (int i = 0; i < lCVDatas.length; i++) {
                    assertEquals("Values are not thoses expected.",
                            lCVDatas[i].getValue(), lCVDs[i].getValue());
                }
            }
        }
        assertTrue("The category " + CATEGORY_LABEL + " is not in BD.",
                lCategorySetted);
    }

    /**
     * Tests the setDictionaryCategory method.
     */
    public void testUpdateCategoryCase() {
        // Defining the category in the dictionary the categories
        int lValuesSize = VALUES.length;
        CategoryValueData[] lCVDs = new CategoryValueData[lValuesSize];
        for (int i = 0; i < lValuesSize; i++) {
            lCVDs[i] = new CategoryValueData(VALUES[i]);
        }
        CategoryData lCategory =
                new CategoryData(null, CATEGORY_LABEL, "", getProcessName(),
                        CategoryAccessData.PROCESS, lCVDs);
        environmentService.setDictionaryCategory(adminRoleToken, lCategory,
                true);

        // Updating the category in the dictionary the categories
        lValuesSize = NEW_VALUES.length;
        lCVDs = new CategoryValueData[lValuesSize];
        for (int i = 0; i < lValuesSize; i++) {
            lCVDs[i] = new CategoryValueData(NEW_VALUES[i]);
        }
        lCategory =
                new CategoryData(null, CATEGORY_LABEL, "", getProcessName(),
                        CategoryAccessData.PROCESS, lCVDs);
        environmentService.setDictionaryCategory(adminRoleToken, lCategory,
                true);

        // Retrieve the category
        Collection<CategoryData> lCategories =
                environmentService.getCategories(adminRoleToken,
                        getProcessName());

        assertNotNull("Method getCategories returns null.", lCategories);

        // Verify the values are corrects
        boolean lCategorySetted = false;
        for (CategoryData lCategoryData : lCategories) {
            if (lCategoryData.getLabelKey().equals(CATEGORY_LABEL)) {
                lCategorySetted = true;
                CategoryValueData[] lCVDatas =
                        lCategoryData.getCategoryValueDatas();
                assertEquals("Number of values is not the number expected.",
                        lCVDs.length, lCVDatas.length);
                for (int i = 0; i < lCVDatas.length; i++) {
                    assertEquals("Values are not thoses expected.",
                            lCVDatas[i].getValue(), lCVDs[i].getValue());
                }
            }
        }
        assertTrue("The category " + CATEGORY_LABEL + " is not in BD.",
                lCategorySetted);
    }

    /**
     * Tests the method with a used category name
     */
    public void testUndeletableValueCase() {
        // Defining the category in the dictionary the categories
        int lValuesSize = VALUES.length;
        CategoryValueData[] lCVDs = new CategoryValueData[lValuesSize];
        for (int i = 0; i < lValuesSize; i++) {
            lCVDs[i] = new CategoryValueData(VALUES[i]);
        }
        CategoryData lCategory =
                new CategoryData(null, CATEGORY_USED_LABEL, "",
                        getProcessName(), CategoryAccessData.PROCESS, lCVDs);

        try {
            environmentService.setDictionaryCategory(adminRoleToken, lCategory,
                    true);
            fail("The exception has not been thrown.");
        }

        catch (UndeletableValuesException ex) {
            // ok.
            assertNotNull(
                    "The message attribute of the UndeletableValuesException is incorrect.",
                    ex.getMessage());
            assertNotNull(
                    "The values list of the UndeletableValuesException is incorrect.",
                    ex.getValuesList());
        }
        catch (Throwable e) {
            e.printStackTrace();
            fail("The exception thrown is not an UndeletableValuesException.");
        }
    }

    /**
     * Update the category values of an existing category. Do not remove values
     * that not redefine.
     */
    public void testUpdateValuesCase() {
        int lValuesSize = VALUES.length;
        CategoryValueData[] lCategoryValueDatas =
                new CategoryValueData[lValuesSize];
        for (int i = 0; i < lValuesSize; i++) {
            lCategoryValueDatas[i] = new CategoryValueData(VALUES[i]);
        }
        CategoryData lCategory =
                new CategoryData(null, CATEGORY_LABEL, "", getProcessName(),
                        CategoryAccessData.PROCESS, lCategoryValueDatas);
        environmentService.setDictionaryCategory(adminRoleToken, lCategory,
                true);

        lValuesSize = NEW_VALUES.length;
        lCategoryValueDatas = new CategoryValueData[lValuesSize];
        for (int i = 0; i < lValuesSize; i++) {
            lCategoryValueDatas[i] = new CategoryValueData(NEW_VALUES[i]);
        }
        lCategory =
                new CategoryData(null, CATEGORY_LABEL, "", getProcessName(),
                        CategoryAccessData.PROCESS, lCategoryValueDatas);
        environmentService.setDictionaryCategory(adminRoleToken, lCategory,
                false);

        List<String> lExpectedValues = new LinkedList<String>();
        lExpectedValues.addAll(Arrays.asList(VALUES));
        lExpectedValues.addAll(Arrays.asList(NEW_VALUES));

        Map<String, List<CategoryValue>> lCategoryValueDatas2 =
                environmentService.getCategoryValues(getProcessName(),
                        Collections.singletonList(CATEGORY_LABEL));
        List<String> lValues = new LinkedList<String>();
        for (CategoryValue lCategoryValue : lCategoryValueDatas2.get(CATEGORY_LABEL)) {
            lValues.add(lCategoryValue.getValue());
        }

        assertEquals(lExpectedValues.size(), lValues.size());
        assertTrue(lValues.containsAll(lExpectedValues));
    }

    /**
     * Update the access attribute of a category.
     * <ol>
     * <li>The default value for access attribute is "PROCESS" ( for a new
     * category with null as access).
     * <li>Redefinition of the access attribute.
     * <li>Redefinition of the category without updating the value of access
     * attribute.
     */
    public void testAccessCase() {
        int lValuesSize = VALUES.length;
        CategoryValueData[] lCategoryValueDatas =
                new CategoryValueData[lValuesSize];
        for (int i = 0; i < lValuesSize; i++) {
            lCategoryValueDatas[i] = new CategoryValueData(VALUES[i]);
        }
        CategoryData lCategory =
                new CategoryData(null, CATEGORY_LABEL, "", getProcessName(),
                        null, lCategoryValueDatas);
        environmentService.setDictionaryCategory(adminRoleToken, lCategory,
                true);

        CategoryData lCategoryData = findCategory(CATEGORY_LABEL);
        assertNotNull("Cannot get the category", lCategoryData);
        assertEquals(CategoryAccessData.PROCESS, lCategoryData.getAccessLevel());

        lCategory =
                new CategoryData(null, CATEGORY_LABEL, "", getProcessName(),
                        CategoryAccessData.USER, lCategoryValueDatas);
        environmentService.setDictionaryCategory(adminRoleToken, lCategory,
                false);

        lCategoryData = findCategory(CATEGORY_LABEL);
        assertNotNull("Cannot get the category", lCategoryData);
        assertEquals(CategoryAccessData.USER, lCategoryData.getAccessLevel());

        lCategory =
                new CategoryData(null, CATEGORY_LABEL, "", getProcessName(),
                        null, lCategoryValueDatas);
        environmentService.setDictionaryCategory(adminRoleToken, lCategory,
                false);

        lCategoryData = findCategory(CATEGORY_LABEL);
        assertNotNull("Cannot get the category", lCategoryData);
        assertEquals(CategoryAccessData.USER, lCategoryData.getAccessLevel());
    }

    /**
     * Find a category by its name.
     * 
     * @param pCategoryName
     *            Category's name
     * @return Category if found. (null otherwise)
     */
    private CategoryData findCategory(String pCategoryName) {
        Collection<CategoryData> lCategoryDataList =
                environmentService.getCategories(adminRoleToken,
                        getProcessName());

        for (CategoryData lCategoryData : lCategoryDataList) {
            if (lCategoryData.getLabelKey().equals(pCategoryName)) {
                return lCategoryData;
            }
        }

        return null;

    }

    /**
     * Check that an admin access set on instance can set dictionary categories
     * of access user but not for categories of access process.
     */
    public void testSetDictionaryCategoryWithAdminInstanceCase() {
        // Login with Admin Instance
        String lAdminInstanceUserToken =
                authorizationService.login(
                        GpmTestValues.ADMIN_INSTANCE_LOGIN_PWD[0],
                        GpmTestValues.ADMIN_INSTANCE_LOGIN_PWD[1]);
        String lAdminInstanceRoleToken =
                authorizationService.selectRole(lAdminInstanceUserToken,
                        GpmTestValues.ADMINISTRATOR_ROLE, getProductName(),
                        getProcessName());

        // Get a process category data
        CategoryData lProcessCategoryData =
                findCategory(GpmTestValues.CATEGORY_COLOR);
        assertNotNull(lProcessCategoryData);

        // Update this process category data
        try {
            environmentService.setDictionaryCategory(lAdminInstanceRoleToken,
                    lProcessCategoryData, true);
            fail("The exception has not been thrown.");
        }
        catch (AuthorizationException lAuthorizationException) {
            // ok
        }
        catch (Throwable e) {
            fail("The exception thrown is not an AuthorizationException.");
        }

        // Get a user category data
        CategoryData lUserCategoryData =
                findCategory(GpmTestValues.CATEGORY_USER_NAME);
        assertNotNull(lUserCategoryData);

        try {
            // Update this user category data
            environmentService.setDictionaryCategory(lAdminInstanceRoleToken,
                    lUserCategoryData, true);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            fail("An exception has been thrown.");
        }
    }

    /**
     * Check that an admin access set on product can set dictionary categories
     * of access user but not for categories of access process.
     */
    public void testSetDictionaryCategoryWithProductInstanceCase() {
        // Login with Product Instance
        String lProductInstanceUserToken =
                authorizationService.login(
                        GpmTestValues.ADMIN_PRODUCT_LOGIN_PWD[0],
                        GpmTestValues.ADMIN_PRODUCT_LOGIN_PWD[1]);
        String lProductInstanceRoleToken =
                authorizationService.selectRole(lProductInstanceUserToken,
                        GpmTestValues.ADMINISTRATOR_ROLE,
                        GpmTestValues.PRODUCT1_NAME, getProcessName());

        // Get a process category data
        CategoryData lProcessCategoryData =
                findCategory(GpmTestValues.CATEGORY_COLOR);
        assertNotNull(lProcessCategoryData);

        // Update this process category data
        try {
            environmentService.setDictionaryCategory(lProductInstanceRoleToken,
                    lProcessCategoryData, true);
            fail("The exception has not been thrown.");
        }
        catch (AuthorizationException lAuthorizationException) {
            // ok
        }
        catch (Throwable e) {
            fail("The exception thrown is not an AuthorizationException.");
        }

        // Get a user category data
        CategoryData lUserCategoryData =
                findCategory(GpmTestValues.CATEGORY_USER_NAME);
        assertNotNull(lUserCategoryData);

        try {
            // Update this user category data
            environmentService.setDictionaryCategory(lProductInstanceRoleToken,
                    lUserCategoryData, true);
        }
        catch (Exception ex) {
            fail("An exception has been thrown.");
        }
    }

    /**
     * Check that a classical role (no global admin, no admin access on
     * instance, no admin access on product) cannot set dictionary categories
     * (not access process, not access user)
     */
    public void testSetDictionaryCategoryWithNoAdminAccessCase() {
        // Login with classical role
        String lUserToken =
                authorizationService.login(GpmTestValues.NO_ADMIN_LOGIN_PWD[0],
                        GpmTestValues.NO_ADMIN_LOGIN_PWD[1]);
        String lRoleToken =
                authorizationService.selectRole(lUserToken,
                        GpmTestValues.VIEWER_ROLE, getProductName(),
                        getProcessName());

        // Get a process category data
        CategoryData lProcessCategoryData =
                findCategory(GpmTestValues.CATEGORY_COLOR);
        assertNotNull(lProcessCategoryData);

        // Update this process category data
        try {
            environmentService.setDictionaryCategory(lRoleToken,
                    lProcessCategoryData, true);
            fail("The exception has not been thrown.");
        }
        catch (AuthorizationException lAuthorizationException) {
            // ok
        }
        catch (Throwable e) {
            fail("The exception thrown is not an AuthorizationException.");
        }

        // Get a user category data
        CategoryData lUserCategoryData =
                findCategory(GpmTestValues.CATEGORY_USER_NAME);
        assertNotNull(lUserCategoryData);

        try {
            // Update this user category data
            environmentService.setDictionaryCategory(lRoleToken,
                    lUserCategoryData, true);
            fail("The exception has not been thrown.");
        }
        catch (AuthorizationException lAuthorizationException) {
            // ok
        }
        catch (Throwable e) {
            fail("The exception thrown is not an AuthorizationException.");
        }
    }
}