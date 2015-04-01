/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel(Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.utils;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.cglib.core.Constants;
import net.sf.cglib.core.ReflectUtils;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.fields.impl.CacheableInputData;
import org.topcased.gpm.business.link.impl.CacheableLink;
import org.topcased.gpm.business.revision.impl.CacheableRevision;
import org.topcased.gpm.util.bean.CacheProperties;
import org.topcased.gpm.util.lang.CopyUtils;
import org.topcased.gpm.util.proxy.ImmutableGpmObject;

/**
 * Tests the CopyUtils tools
 * 
 * @author tpanuel
 */
public class MutableImmutableTest extends AbstractBusinessServiceTestCase {
    private static final String[][] SHEET_NAMES =
            {
             { GpmTestValues.PRODUCT_HAPPY_MOUSE_NAME,
              GpmTestValues.SHEET_REF_GARFIELD },
             { GpmTestValues.PRODUCT_HAPPY_MOUSE_NAME, "Tom" },
             { GpmTestValues.PRODUCT_STORE1_NAME,
              "sheet_multiple_with_some_confidential_fields_01" },
             { GpmTestValues.PRODUCT1_NAME, "pointed:1" } };

    /**
     * Test switch between mutable and immutable for a CacheableSheet
     */
    public void testCacheableSheet() {
        for (String[] lSheetName : SHEET_NAMES) {
            final String lSheetId =
                sheetService.getSheetIdByReference("PET STORE", lSheetName[0], 
                        lSheetName[1]);

            checkMutableImmutableObject(sheetService.getCacheableSheet(
                    adminRoleToken, lSheetId, CacheProperties.MUTABLE));
        }
    }

    private static final String[] SHEET_TYPE_NAMES =
            {
             GpmTestValues.SHEET_TYPE_CAT,
             GpmTestValues.SHEETLINK_SHEET_MULTIPLE_WITH_SOME_CONFIDENTIAL_FIELDS,
             GpmTestValues.SHEET_TYPE_TEST_POINTER_FIELDS2 };

    /**
     * Test switch between mutable and immutable for a CacheableSheetType
     */
    public void testCacheableSheetType() {
        for (String lSheetTypeName : SHEET_TYPE_NAMES) {
            checkMutableImmutableObject(sheetService.getCacheableSheetTypeByName(
                    adminRoleToken, getProcessName(), lSheetTypeName,
                    CacheProperties.MUTABLE));
        }
    }

    private static final String[] PRODUCT_NAMES =
            { GpmTestValues.PRODUCT_HAPPY_MOUSE_NAME,
             GpmTestValues.PRODUCT_STORE1_NAME, GpmTestValues.PRODUCT1_NAME };

    /**
     * Test switch between mutable and immutable for a CacheableProduct
     */
    public void testCacheableProduct() {
        for (String lProductName : PRODUCT_NAMES) {
            final String lProductId =
                    serviceLocator.getProductService().getProductId(
                            adminRoleToken, lProductName);

            checkMutableImmutableObject(serviceLocator.getProductService().getCacheableProduct(
                    adminRoleToken, lProductId, CacheProperties.MUTABLE));
        }
    }

    private static final String[] PRODUCT_TYPE_NAMES = { "Store" };

    /**
     * Test switch between mutable and immutable for a CacheableProductType
     */
    public void testCacheableProductType() {
        for (String lProductTypeName : PRODUCT_TYPE_NAMES) {
            checkMutableImmutableObject(serviceLocator.getProductService().getCacheableProductTypeByName(
                    adminRoleToken, getProcessName(), lProductTypeName,
                    CacheProperties.MUTABLE));
        }
    }

    /**
     * Test switch between mutable and immutable for a CacheableRevision
     */
    public void testCacheableRevision() {
        checkMutableImmutableObject(new CacheableRevision());
    }

    /**
     * Test switch between mutable and immutable for a CacheableLink
     */
    public void testCacheableLink() {
        for (CacheableLink lLink : linkService.getLinks(adminRoleToken,
                sheetService.getSheetIdByReference(adminRoleToken,
                        SHEET_NAMES[0][0], SHEET_NAMES[0][1]),
                CacheProperties.IMMUTABLE)) {
            checkMutableImmutableObject(lLink);
        }
    }

    private static final String[] LINK_TYPE_NAMES =
            { GpmTestValues.SHEETLINK_CAT_PRICE,
             GpmTestValues.SHEETLINK_SHEETTYPE1_CAT,
             "SheetLinkWithSomeConfidentialFields" };

    /**
     * Test switch between mutable and immutable for a CacheableLinkType
     */
    public void testCacheableLinkType() {
        for (String lLinkTypeName : LINK_TYPE_NAMES) {
            checkMutableImmutableObject(linkService.getCacheableLinkTypeByName(
                    adminRoleToken, getProcessName(), lLinkTypeName,
                    CacheProperties.MUTABLE));
        }
    }

    /**
     * Test switch between mutable and immutable for a CacheableInputData
     */
    public void testCacheableInputData() {
        checkMutableImmutableObject(new CacheableInputData());
    }

    private static final String[] INPUT_DATA_TYPE_NAMES =
            { "SheetWithSomeConfidentialFields_ExtentedAction_InputData",
             "InputDataTypeTest", "ChoiceString_InputDataTypeTest" };

    /**
     * Test switch between mutable and immutable for a CacheableInputDataType
     */
    public void testCacheableInputDataType() {
        for (String lInputDataTypeName : INPUT_DATA_TYPE_NAMES) {
            checkMutableImmutableObject(fieldsService.getCacheableInputDataType(
                    adminRoleToken, lInputDataTypeName, getProcessName()));
        }
    }

    /**
     * Test switch between mutable and immutable for a CacheableSheetLinksByType
     */
    public void testCacheableSheetLinksByType() {
        final String lSheetId =
                sheetService.getSheetIdByReference(getProcessName(),
                        SHEET_NAMES[0][0], SHEET_NAMES[0][1]);
        for (String lLinkTypeName : LINK_TYPE_NAMES) {
            checkMutableImmutableObject(sheetService.getCacheableSheetLinksByType(
                    getProcessName(), lSheetId,
                    linkService.getLinkTypeByName(adminRoleToken,
                            getProcessName(), lLinkTypeName).getId()));
        }
    }

    /**
     * Test switch between mutable and immutable for an Object
     * 
     * @param pObject
     *            The object to test
     */
    private void checkMutableImmutableObject(Object pObject) {
        final Object lImmutableObject = CopyUtils.getImmutableCopy(pObject);
        final Object lMutableObject =
                CopyUtils.getMutableCopy(lImmutableObject);

        // Mutable and immutable object are the same
        checkFullEquality(pObject, lImmutableObject);
        checkFullEquality(pObject, lMutableObject);
        checkFullEquality(lImmutableObject, lMutableObject);
        // Test access on methods for the immutable object
        checkGetterSetterAccess(lImmutableObject, false);
        assertTrue(lImmutableObject + " must be immutable",
                lImmutableObject instanceof ImmutableGpmObject);
        //Test access on methods for the mutable object
        checkGetterSetterAccess(lMutableObject, true);

    }

    /**
     * Test the accessibility on the getters and the setters of a bean
     * 
     * @param pBean
     *            The bean to test
     * @param pIsMutable
     *            True: Getter and setter are accessible; False: Only getter are
     *            accessible
     */
    @SuppressWarnings("rawtypes")
	private void checkGetterSetterAccess(Object pBean, boolean pIsMutable) {

        if (pBean == null
                || pBean.getClass().isPrimitive()
                || pBean instanceof Enum
                || CopyUtils.IMMUTABLE_STANDARD_CLASS.contains(pBean.getClass())) {
            // Null object, primitive, enum or immutable object -> always immutable
        }
        else if (pBean instanceof List || pBean instanceof Set) {
            for (Object lElement : (Collection) pBean) {
                checkGetterSetterAccess(lElement, pIsMutable);
            }
        }
        else if (pBean instanceof Map) {
            for (Object lKey : ((Map) pBean).keySet()) {
                checkGetterSetterAccess(lKey, pIsMutable);
                Object lBean = ((Map) pBean).get(lKey);
                if (lBean != null) {
                    checkGetterSetterAccess(((Map) pBean).get(lKey), pIsMutable);
                }
            }
        }
        else if (pBean.getClass().isArray()) {
            final Object[] lTab = (Object[]) pBean;

            for (int i = 0; i < lTab.length; i++) {
                checkGetterSetterAccess(lTab[i], pIsMutable);
            }
        }
        else if (pBean instanceof Serializable) {
            final PropertyDescriptor[] lProperties =
                    ReflectUtils.getBeanProperties(pBean.getClass());
            final int lNbProperties = lProperties.length;

            // Check if the class is a bean
            checkIsBean(pBean.getClass());
            // Check the getter and the setter of all the properties
            for (int i = 0; i < lNbProperties; i++) {
                final Method lGetter = lProperties[i].getReadMethod();

                // Test getter access
                if (lGetter != null) {
                    final Method lSetter = lProperties[i].getWriteMethod();
                    Object lGotObect = null;

                    try {
                        assertTrue("Wrong access validity of the getter "
                                + lGetter.getName() + " of the class "
                                + pBean.getClass(), isAccessibleMethod(pBean,
                                lGetter, new Object[] {}));
                        lGotObect = lGetter.invoke(pBean, new Object[] {});
                    }
                    catch (Exception e) {
                        // Nothing done because many type of exceptions can be launched
                        // It's not the object of the test to test the good
                        // execution of invoking methods
                    }

                    // Test setter access
                    if (lSetter != null) {
                        if (lGotObect != null) {
                            // Recursive test is not need if a property has not setter
                            checkGetterSetterAccess(lGotObect, pIsMutable);
                        }
                        assertEquals("Wrong access validity of the setter "
                                + lSetter.getName() + " of the class "
                                + pBean.getClass(), isAccessibleMethod(pBean,
                                lSetter, new Object[] { lGotObect }),
                                pIsMutable);
                    }
                }
            }
        }
    }

    /**
     * Only test one times for all the tests if a class is a bean
     */
    private Set<Class<?>> beans = new HashSet<Class<?>>();

    /**
     * Test if the class in parameter is a bean
     * 
     * @param pClass
     *            The class to test
     */
    private void checkIsBean(Class<?> pClass) {
        Class<?> lClass = pClass;

        // Recursive test for all the sub class
        while (!lClass.equals(Object.class) && !beans.contains(lClass)) {
            // No test for immutable class (generated class)
            if (ImmutableGpmObject.class.isAssignableFrom(lClass)) {
                break;
            }
            // else

            // All the fields of the current fields (not fields of the sub classes)
            final Field[] lFields = lClass.getDeclaredFields();
            final int lNbFields = lFields.length;
            // Bean's properties
            final PropertyDescriptor[] lProperties =
                    ReflectUtils.getBeanProperties(lClass);
            final int lNbProperties = lProperties.length;

            for (int i = 0; i < lNbFields; i++) {
                final Field lField = lFields[i];

                // No test for static fields
                if ((lField.getModifiers() & Constants.ACC_STATIC) == 0) {
                    final String lFieldName = lField.getName();
                    boolean lFound = false;

                    // Find the property associated to the field
                    for (int j = 0; j < lNbProperties; j++) {
                        final PropertyDescriptor lProperty = lProperties[j];

                        // The getter and the setters must exist and must not be final
                        if (lProperty.getName().equals(lFieldName)) {
                            assertNotNull(notABeanErrorMessage(lClass,
                                    lFieldName), lProperty.getReadMethod());
                            assertTrue(
                                    notABeanErrorMessage(lClass, lFieldName),
                                    (lProperty.getReadMethod().getModifiers() & Constants.ACC_STATIC) == 0);
                            assertNotNull(notABeanErrorMessage(lClass,
                                    lFieldName), lProperty.getWriteMethod());
                            assertTrue(
                                    notABeanErrorMessage(lClass, lFieldName),
                                    (lProperty.getWriteMethod().getModifiers() & Constants.ACC_STATIC) == 0);
                            lFound = true;
                            break;
                        }
                    }
                    // Property not found
                    assertTrue(notABeanErrorMessage(lClass, lFieldName), lFound);
                }
            }
            beans.add(lClass);
            lClass = lClass.getSuperclass();
        }
    }

    /**
     * Compute a message error for a non bean object
     * 
     * @param pClass
     *            The name of the class
     * @param pFieldName
     *            The name of the field
     * @return The error message
     */
    private String notABeanErrorMessage(Class<?> pClass, String pFieldName) {
        return "Because of the field " + pFieldName + ", the " + pClass
                + " is not a bean";
    }

    /**
     * Test if a method can be invoke
     * 
     * @param pObject
     *            The Java object (this)
     * @param pMethod
     *            The method to invoke
     * @param pArgs
     *            The arguments
     * @return If the method can be launched
     */
    private boolean isAccessibleMethod(Object pObject, Method pMethod,
            Object[] pArgs) {
        boolean lResult = true;
        final int lNbArgs = pArgs.length;
        final Object[] lArgs = new Object[lNbArgs];

        // Fix problem with that method that used primitive type
        // and getter that return null
        for (int i = 0; i < lNbArgs; i++) {
            Class<?> lParamClass = pMethod.getParameterTypes()[i];

            if (pArgs[i] != null && lParamClass.isPrimitive()) {
                if (lParamClass.equals(boolean.class)) {
                    lArgs[i] = true;
                }
                else {
                    lArgs[i] = 0;
                }
            }
        }

        try {
            // Run the method
            pMethod.invoke(pObject, lArgs);
        }
        // Exceptions on reflection
        catch (IllegalStateException e) {
            // The method cannot be launched
            lResult = false;
        }
        catch (Exception e) {
            if (e.getCause() instanceof IllegalStateException) {
                // The method cannot be launched
                lResult = false;
            }
            else {
                fail("Method " + pMethod.getName() + " of the "
                        + pObject.getClass() + " failed: Exception " + e
                        + " launched by " + e.getCause());
            }
        }
        return lResult;
    }

    /**
     * Check if two beans are equal
     * 
     * @param pBean1
     *            The first bean
     * @param pBean2
     *            The second bean
     */
    @SuppressWarnings("unchecked")
    private void checkFullEquality(Object pBean1, Object pBean2) {
        if (pBean1 == null
                || pBean1.getClass().isPrimitive()
                || pBean1 instanceof Enum
                || CopyUtils.IMMUTABLE_STANDARD_CLASS.contains(pBean1.getClass())) {
            // If Primitive case, operator equal is used
            // or if the object is comparable, method equal is used
            assertEquals(getNotSameValuesMessage(pBean1, pBean2), pBean1,
                    pBean2);
        }
        else if (pBean1 instanceof List) {
            final Iterator<Object> lIter1 = ((List<Object>) pBean1).iterator();
            final Iterator<Object> lIter2 = ((List<Object>) pBean2).iterator();

            // The lists must have the same size
            assertEquals(getNotSameValuesMessage(pBean1, pBean2),
                    ((List<Object>) pBean1).size(),
                    ((List<Object>) pBean2).size());
            while (lIter1.hasNext()) {
                // Each elements must be equals
                checkFullEquality(lIter1.next(), lIter2.next());
            }
        }
        else if (pBean1 instanceof Set) {
            final Set<Object> lSet1 = (Set<Object>) pBean1;
            final Set<Object> lSet2 = (Set<Object>) pBean2;

            // The sets must have the same size
            assertEquals(getNotSameValuesMessage(pBean1, pBean2), lSet1.size(),
                    lSet2.size());
            for (Object lElement1 : lSet1) {
                boolean lFound = false;
                for (Object lElement2 : lSet1) {
                    if (lElement1.equals(lElement2)) {
                        checkFullEquality(lElement1, lElement2);
                        lFound = true;
                        break;
                    }
                }
                assertTrue(getNotSameValuesMessage(pBean1, pBean2), lFound);
            }
        }
        else if (pBean1 instanceof Map) {
            final Map<Object, Object> lMap1 = (Map<Object, Object>) pBean1;
            final Map<Object, Object> lMap2 = (Map<Object, Object>) pBean2;

            // The maps must have the same size
            assertEquals(getNotSameValuesMessage(pBean1, pBean2), lMap1.size(),
                    lMap2.size());
            for (Object lElement : lMap1.keySet()) {
                // Each elements must be equals
                checkFullEquality(lMap1.get(lElement), lMap2.get(lElement));
            }
        }
        else if (pBean1 instanceof Serializable) {
            final PropertyDescriptor[] lPropertiesBean1 =
                    ReflectUtils.getBeanProperties(pBean1.getClass());
            final int lNbProperties = lPropertiesBean1.length;

            try {
                // Test each field
                for (int i = 0; i < lNbProperties; i++) {
                    final Method lGetterBean1 =
                            lPropertiesBean1[i].getReadMethod();
                    final Method lSetterBean1 =
                            lPropertiesBean1[i].getWriteMethod();

                    // No test for getter without setter
                    // because they create their own data
                    if (lGetterBean1 != null && lSetterBean1 != null) {
                        final Method lGetterBean2 =
                                pBean2.getClass().getMethod(
                                        lGetterBean1.getName(),
                                        lGetterBean1.getParameterTypes());
                        // Invoke bean's getter to get the fields value
                        final Object lFieldBean1 =
                                lGetterBean1.invoke(pBean1, new Object[] {});
                        final Object lFieldBean2 =
                                lGetterBean2.invoke(pBean2, new Object[] {});

                        // Each Fields must be equals
                        checkFullEquality(lFieldBean1, lFieldBean2);
                    }
                }
            }
            // Exceptions on reflection
            catch (IllegalArgumentException e) {
                throw new RuntimeException(e);
            }
            catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }
            catch (SecurityException e) {
                throw new RuntimeException(e);
            }
            catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Compute an error message if two beans are not equal
     * 
     * @param pBean1
     *            The first bean
     * @param pBean2
     *            The second bean
     * @return The error message
     */
    private String getNotSameValuesMessage(Object pBean1, Object pBean2) {
        return "Bean of " + pBean1 + " and bean of " + pBean2
                + " has not the same values";
    }
}
