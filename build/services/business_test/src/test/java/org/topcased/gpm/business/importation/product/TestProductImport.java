/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.importation.product;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.ContextLocator;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.exception.ImportException;
import org.topcased.gpm.business.fields.impl.CacheableValuesContainer;
import org.topcased.gpm.business.importation.AbstractValuesContainerImportTest;
import org.topcased.gpm.business.importation.ImportProperties;
import org.topcased.gpm.business.importation.ImportProperties.ImportFlag;
import org.topcased.gpm.business.product.impl.CacheableProduct;
import org.topcased.gpm.business.product.impl.ProductServiceImpl;
import org.topcased.gpm.business.serialization.data.FieldValueData;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * TestProductImport: Test product importation.
 * 
 * @author mkargbo
 */
public class TestProductImport extends AbstractValuesContainerImportTest {

    /** ERASE_FIELD_TO_UPDATE */
    private static final String ERASE_FIELD_TO_UPDATE = "product_name";

    /** ERASE_NEW_VALUE */
    private static final String ERASE_NEW_VALUE = "to be erase";

    private static final String INSTANCE_FILE =
            "importation/product/productsToInstantiate.xml";

    private static final String IMPORT_FILE =
            "importation/product/productsToImport.xml";

    private static final String IMPORT_FILE_TO_UPDATE =
            "importation/product/productsToImportToUpdate.xml";

    private static final Set<String[]> IMPORTED_PRODUCTS;

    /** Fields that should be import for 'admin' role */
    private static final Map<String, String> EXPECTED_FIELDS_TO_UPDATE_VALUES_ADMIN;

    /** Fields that should be import for 'notadmin' role */
    private static final Map<String, String> EXPECTED_FIELDS_TO_UPDATE_VALUES_NOT_ADMIN;

    /** Fields that should not be import for 'notadmin' role */
    private static final Map<String, String> NOT_EXPECTED_FIELDS_TO_UPDATE_VALUES_NOT_ADMIN;

    private static final Map<String, String[]> EXPECTED_ATTRIBUTES_VALUES;

    static {
        IMPORTED_PRODUCTS = new HashSet<String[]>();
        IMPORTED_PRODUCTS.add(new String[] { "productTestImport_01" });
        IMPORTED_PRODUCTS.add(new String[] { "productTestImport_02" });

        EXPECTED_FIELDS_TO_UPDATE_VALUES_ADMIN = new HashMap<String, String>();
        EXPECTED_FIELDS_TO_UPDATE_VALUES_ADMIN.put("product_name",
                "productTestImport_UPDATED");
        EXPECTED_FIELDS_TO_UPDATE_VALUES_ADMIN.put("product_location",
                "location_UPDATED");
        EXPECTED_FIELDS_TO_UPDATE_VALUES_ADMIN.put("product_storetype",
                GpmTestValues.CATEGORY_USAGE_VALUE_POLICE);
        EXPECTED_FIELDS_TO_UPDATE_VALUES_ADMIN.put("Store_Field3",
                "productTestImport_conf_field_UPDATED");

        EXPECTED_FIELDS_TO_UPDATE_VALUES_NOT_ADMIN =
                new HashMap<String, String>();
        EXPECTED_FIELDS_TO_UPDATE_VALUES_NOT_ADMIN.put("product_name",
                "productTestImport_UPDATED");
        EXPECTED_FIELDS_TO_UPDATE_VALUES_NOT_ADMIN.put("product_location",
                "location_UPDATED");
        EXPECTED_FIELDS_TO_UPDATE_VALUES_NOT_ADMIN.put("product_storetype",
                GpmTestValues.CATEGORY_USAGE_VALUE_POLICE);

        NOT_EXPECTED_FIELDS_TO_UPDATE_VALUES_NOT_ADMIN =
                new HashMap<String, String>();
        NOT_EXPECTED_FIELDS_TO_UPDATE_VALUES_NOT_ADMIN.put("Store_Field3",
                "productTestImport_01_conf_field");

        EXPECTED_ATTRIBUTES_VALUES = new HashMap<String, String[]>();
        EXPECTED_ATTRIBUTES_VALUES.put("productTestImport_attribute",
                new String[] { "value_UPDATED" });
    }

    private ProductServiceImpl productService;

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#setUp()
     */
    @Override
    protected void setUp() {
        super.setUp();
        productService =
                (ProductServiceImpl) ContextLocator.getContext().getBean(
                        "productServiceImpl");
        instantiate(getProcessName(), INSTANCE_FILE);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#eraseAssertion(java.lang.Object[])
     */
    @Override
    protected void eraseAssertion(Object... pArgs) {
        // Unused
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#erasePreCondition()
     */
    @Override
    protected Object erasePreCondition() {
        //Retrieve products identifiers.
        final Map<String, Integer> lProducts = new HashMap<String, Integer>();
        for (String[] lRef : getImportedElement()) {
            String lId = getElementId(lRef);
            CacheableProduct lProduct =
                    productService.getCacheableProduct(adminRoleToken, lId,
                            CacheProperties.MUTABLE);
            ((FieldValueData) lProduct.getValue(ERASE_FIELD_TO_UPDATE)).setValue(ERASE_NEW_VALUE);
            productService.updateProduct(adminRoleToken, lProduct, CONTEXT);
            lProduct =
                    productService.getCacheableProduct(adminRoleToken, lId,
                            CacheProperties.IMMUTABLE);
            lProducts.put(lRef[0], lProduct.getVersion());
        }
        return lProducts;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#getBusinessObject(java.lang.String,
     *      java.lang.String)
     */
    @Override
    protected CacheableValuesContainer getBusinessObject(String pRoleToken,
            String pElementId) {
        CacheableProduct lProduct =
                productService.getCacheableProduct(pRoleToken, pElementId,
                        CacheProperties.IMMUTABLE);
        return lProduct;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#getElementId(java.lang.String)
     */
    @Override
    protected String getElementId(String... pElementRef) {
        String lRef = pElementRef[0];
        String lProductId = productService.getProductId(adminRoleToken, lRef);
        return lProductId;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#getExpectedFieldsToImportAdmin()
     */
    @Override
    protected Map<String, String> getExpectedFieldsToImportAdmin() {
        return EXPECTED_FIELDS_TO_UPDATE_VALUES_ADMIN;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#getExpectedFieldsToImportNotAdmin()
     */
    @Override
    protected Map<String, String> getExpectedFieldsToImportNotAdmin() {
        return EXPECTED_FIELDS_TO_UPDATE_VALUES_NOT_ADMIN;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#getImportFile()
     */
    @Override
    protected String getImportFile() {
        return IMPORT_FILE;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#getImportFileForUpdating()
     */
    @Override
    protected String getImportFileForUpdating() {
        return IMPORT_FILE_TO_UPDATE;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#getImportedElement()
     */
    @Override
    protected Set<String[]> getImportedElement() {
        return IMPORTED_PRODUCTS;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#getNotExpectedFieldsToImportNotAdmin()
     */
    @Override
    protected Map<String, String> getNotExpectedFieldsToImportNotAdmin() {
        return NOT_EXPECTED_FIELDS_TO_UPDATE_VALUES_NOT_ADMIN;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#removeElement(java.lang.String)
     */
    @Override
    protected void removeElement(String pElement) {
        productService.deleteProduct(adminRoleToken, pElement, false, null);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#setImportFlag(org.topcased.gpm.business.importation.ImportProperties,
     *      org.topcased.gpm.business.importation.ImportProperties.ImportFlag)
     */
    @Override
    protected void setImportFlag(ImportProperties pProperties, ImportFlag pFlag) {
        pProperties.setProductsFlag(pFlag);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#testCreateOnlyImportNotAdmin()
     */
    @Override
    public void testCreateOnlyImportNotAdmin() {
        try {
            doImport(normalRoleToken, ImportFlag.CREATE_ONLY, getImportFile());
            fail("An ImportException must be raise.");
        }
        catch (ImportException e) {
            //Ok (cannot test with the error message).
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#testUpdateOnlyImportNotAdmin()
     */
    @Override
    public void testUpdateOnlyImportNotAdmin() {
        try {
            doImport(normalRoleToken, ImportFlag.UPDATE_ONLY, getImportFile());
            fail("An ImportException must be raise.");
        }
        catch (ImportException e) {
            //Ok (cannot test with the error message).
        }
    }

    private static final String[] USER =
            { "userImportProduct", "pwd", GpmTestValues.USER_ADMIN };

    private static final String USER_PRODUCT = "productTestImport_03";

    private static final String PRODUCT_PARENT = "productTestImport_04";

    private static final String IMPORT_FILE_TO_UPDATE_2 =
            "importation/product/productsToImportToUpdate2.xml";

    /**
     * Test the importation of a product with user that have a role on the
     * product to import
     */
    public void testUpdateOnlyImportRoleOnProduct() {
        String lUserToken = authorizationService.login(USER[0], USER[1]);
        String lRoleToken =
                authorizationService.selectRole(lUserToken, USER[2],
                        USER_PRODUCT, getProcessName());

        String lProductId = getElementId(USER_PRODUCT);
        try {
            doImport(lRoleToken, ImportFlag.UPDATE_ONLY,
                    IMPORT_FILE_TO_UPDATE_2);

            //Test elements existence
            String lRef = USER_PRODUCT;
            String lId = getElementId(lRef);
            assertTrue(
                    "Product '"
                            + lRef
                            + "' has not been imported correctly (product does not exist)",
                    StringUtils.isNotBlank(lId));
            assertEquals("Product '" + lRef
                    + "' has not the same identifier of its previous version",
                    lProductId, lId);
            CacheableProduct lProduct =
                    (CacheableProduct) getBusinessObject(lRoleToken, lId);

            //Check imported fields.
            checkFields(lProduct, getExpectedFieldsToImportNotAdmin(),
                    getNotExpectedFieldsToImportNotAdmin(), true);
            checkAttributes(lProduct, EXPECTED_ATTRIBUTES_VALUES);

            assertTrue("The product '" + lRef + "' must have a parent.",
                    lProduct.getParentNames().contains(PRODUCT_PARENT));
        }
        catch (ImportException e) {
            e.printStackTrace();
            fail(e);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.importation.AbstractImportTest#testEraseImport()
     */
    @Override
    public void testEraseImport() {
        doTestEraseImport(adminRoleToken, getExpectedFieldsToImportAdmin(),
                EMPTY_MAP);
    }

}
