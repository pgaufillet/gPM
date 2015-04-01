/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.exportation.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.ServiceLocator;
import org.topcased.gpm.business.exportation.ExportProperties;
import org.topcased.gpm.business.exportation.ExportProperties.ExportFlag;
import org.topcased.gpm.business.fields.impl.CacheableValuesContainer;
import org.topcased.gpm.business.product.impl.CacheableProduct;
import org.topcased.gpm.business.serialization.data.Attribute;
import org.topcased.gpm.business.serialization.data.FieldValueData;
import org.topcased.gpm.business.serialization.data.NamedElement;
import org.topcased.gpm.business.serialization.data.Product;
import org.topcased.gpm.domain.product.ProductDao;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * Manager used to export products.
 * 
 * @author tpanuel
 */
public class ProductExportManager extends
        AbstractValuesContainerExportManager<Product> {
    private ProductDao productDao;

    /**
     * Create a product export manager.
     */
    public ProductExportManager() {
        super("products");
    }

    /**
     * Setter for spring injection.
     * 
     * @param pProductDao
     *            The DAO.
     */
    public void setProductDao(final ProductDao pProductDao) {
        productDao = pProductDao;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.exportation.impl.AbstractValuesContainerExportManager#getAllElementsId(java.lang.String,
     *      java.util.Collection, java.util.Collection)
     */
    @SuppressWarnings("unchecked")
	@Override
    protected Iterator<String> getAllElementsId(final String pRoleToken,
            final Collection<String> pProductsName,
            final Collection<String> pTypesName) {
        //reset User counter (user are going to be reexported)
        ExportationData.getInstance().setUserCounter(0);
        List<String> lProductToExport =
                IteratorUtils.toList(productDao.productsIterator(
                        authorizationServiceImpl.getProcessName(pRoleToken),
                        pProductsName, pTypesName));

        ArrayList<String> lProductsId = new ArrayList<String>();
        ArrayList<String> lProductsName = new ArrayList<String>();

        while (lProductToExport.size() > 0) {
            String lProductId = lProductToExport.get(0);
            CacheableProduct lProduct =
                    ServiceLocator.instance().getProductService().getCacheableProduct(
                            pRoleToken, lProductId, CacheProperties.IMMUTABLE);
            if (lProduct.getParentNames().isEmpty()
                    || lProductsName.containsAll(lProduct.getParentNames())) {
                lProductsId.add(lProductId);
                lProductsName.add(lProduct.getProductName());
            }
            else {
                lProductToExport.add(lProductId);
            }
            lProductToExport.remove(0);
            //fill the maps
            if (!ExportationData.getInstance().getProductNames().containsKey(
                    lProduct.getProductName())) {
                ExportationData.getInstance().getProductNames().put(
                        lProduct.getProductName(),
                        ExportationConstants.PRODUCT
                                + ExportationData.getInstance().getProductCounter());
            }

            ExportationData.getInstance().incrementProductCounter();
        }

        return lProductsId.iterator();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.exportation.impl.AbstractExportManager#isValidIdentifier(java.lang.String)
     */
    @Override
    protected boolean isValidIdentifier(final String pId) {
        return productDao.exist(pId);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.exportation.impl.AbstractExportManager#getExportFlag(org.topcased.gpm.business.exportation.ExportProperties)
     */
    @Override
    protected ExportFlag getExportFlag(final ExportProperties pExportProperties) {
        return pExportProperties.getProductsFlag();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.exportation.impl.AbstractValuesContainerExportManager#marshal(org.topcased.gpm.business.fields.impl.CacheableValuesContainer,
     *      org.topcased.gpm.business.exportation.ExportProperties)
     */
    @Override
    protected Product marshal(final CacheableValuesContainer pCacheable,
            final ExportProperties pExportProperties) {
        final Product lProduct = new Product();
        pCacheable.marshal(lProduct);
        if (getObfuscatedProductName(lProduct) != null) {
            //handle obfuscated parents name
            if (lProduct.getParentsAsStrings().size() > 0) {
                for (NamedElement lNamedElement : lProduct.getParents()) {
                    if ((ExportationData.getInstance().getProductNames().get(
                            lNamedElement.getName()) != null)
                            && (ReadProperties.getInstance().isObfProducts())) {
                        lNamedElement.setName(ExportationData.getInstance().getProductNames().get(
                                lNamedElement.getName()));
                    }
                }
            }

            if (ReadProperties.getInstance().isObfProducts()) {
                lProduct.setName(getObfuscatedProductName(lProduct));
                lProduct.setDescription(lProduct.getName() + ExportationConstants.DESCRIPTION);
            }
        }

        //Handle attributevalues fields    
        if (ReadProperties.getInstance().isObfUsers()) {
            if (lProduct.getAttributes() != null) {
                for (Attribute lAttribute : lProduct.getAttributes()) {
                    if (lAttribute.getValues() != null) {
                        List<String> lValues = new ArrayList<String>();
                        String[] lValuesTab =
                                new String[lAttribute.getValues().length];
                        for (String lAttributeValue : lAttribute.getValues()) {
                            lValues.add(replaceUserLogin(lAttributeValue));
                        }
                        for (int j = 0; j < lValues.size(); j++) {
                            lValuesTab[j] = lValues.get(j);
                        }
                        lAttribute.setValues(lValuesTab);
                    }
                }
            }
        }

        //Handle fieldvalues fields        
        if (ReadProperties.getInstance().isObfProducts()) {
            if (lProduct.getFieldValues() != null) {
                for (FieldValueData lFieldValueData : lProduct.getFieldValues()) {
                    if (lFieldValueData != null
                            && (!(("").equals(lFieldValueData.getValue())))) {
                        String lObfProductName =
                                ExportationData.getInstance().getProductNames().get(
                                        lFieldValueData.getValue());
                        if (lObfProductName != null) {
                            lFieldValueData.setValue(lObfProductName);
                        }

                        // Change product name by the obfuscated one in FieldValueData
                        obfuscateFieldValueData(
                                ExportationData.getInstance().getProductNames().entrySet(),
                                lFieldValueData);
                        // Change User login by the obfuscated one in FieldValueData
                        obfuscateFieldValueData(
                                ExportationData.getInstance().getUserLogin().entrySet(),
                                lFieldValueData);

                    }
                }
            }
        }
        return lProduct;
    }

    /**
     * Retrieve the obfuscated product name for a given product
     * 
     * @param pProduct
     *            the product
     * @return the obfuscated product name
     */
    private String getObfuscatedProductName(final Product pProduct) {
        return ExportationData.getInstance().getProductNames().get(
                pProduct.getName());
    }

    /**
     * Replace the content of a fieldValueData by the obfuscated one
     * 
     * @param pEntrySet
     *            an EntrySet (product or userLogin)
     * @param pFieldValueData
     *            the fieldValueData to modify
     */
    private void obfuscateFieldValueData(Set<Entry<String, String>> pEntrySet,
            FieldValueData pFieldValueData) {
        for (Map.Entry<String, String> lEntry : pEntrySet) {
            if (StringUtils.contains(pFieldValueData.getValue(),
                    lEntry.getKey())) {
                pFieldValueData.setValue(StringUtils.replace(
                        pFieldValueData.getValue(), lEntry.getKey(),
                        lEntry.getValue()));
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.exportation.impl.AbstractExportManager#getProductNames(java.lang.String)
     */
    @Override
    protected List<String> getProductNames(final String pElementId) {
        return Collections.singletonList(productDao.load(pElementId).getName());
    }

    /**
     * Check if an attribute matches the pattern User (Login)
     * 
     * @param pAttributeValue
     *            Attribute
     * @return boolean
     */
    public String replaceUserLogin(String pAttributeValue) {
        String lTemp = pAttributeValue;

        String lUserNameString = StringUtils.substringBefore(lTemp, "(").trim();

        String lLoginString =
                StringUtils.substringBetween(pAttributeValue, "(", ")");

        String lNewUserNameString =
                ExportationData.getInstance().getUserName().get(lUserNameString);

        String lNewLoginString =
                ExportationData.getInstance().getUserLogin().get(lLoginString);

        if (lNewUserNameString != null && lNewLoginString != null
                && (!(("").equals(lNewUserNameString)))
                && (!(("").equals(lNewLoginString)))) {
            String lNewAttributeValue =
                    lNewUserNameString + " (" + lNewLoginString + ")";
            return lNewAttributeValue;
        }
        else {
            return pAttributeValue;
        }
    }
}