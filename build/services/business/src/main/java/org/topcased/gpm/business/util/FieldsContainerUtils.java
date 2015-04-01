/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.util;

import org.apache.commons.lang.NotImplementedException;
import org.topcased.gpm.business.fields.impl.CacheableFieldsContainer;
import org.topcased.gpm.business.fields.impl.CacheableValuesContainer;
import org.topcased.gpm.business.link.impl.CacheableLink;
import org.topcased.gpm.business.link.impl.CacheableLinkType;
import org.topcased.gpm.business.product.impl.CacheableProduct;
import org.topcased.gpm.business.product.impl.CacheableProductType;
import org.topcased.gpm.business.search.criterias.FilterTypeData;
import org.topcased.gpm.business.serialization.data.FieldsContainer;
import org.topcased.gpm.business.serialization.data.LinkType;
import org.topcased.gpm.business.serialization.data.ProductType;
import org.topcased.gpm.business.serialization.data.SheetType;
import org.topcased.gpm.business.sheet.impl.CacheableSheet;
import org.topcased.gpm.business.sheet.impl.CacheableSheetType;
import org.topcased.gpm.domain.fields.ValuesContainer;
import org.topcased.gpm.domain.link.Link;
import org.topcased.gpm.domain.product.Product;
import org.topcased.gpm.domain.sheet.Sheet;

/**
 * FieldsContainerUtils
 * 
 *@author mkargbo
 */
public class FieldsContainerUtils {

    /**
     * Get the entity class corresponding to this business class.
     * <p>
     * Only FieldsContainer, SheetType, ProductType and LinkType are handled.
     * 
     * @param pClazz
     *            Business class
     * @return Entity class
     * @throws NotImplementedException
     *             If the Business class is not handle.
     */
    public static Class<? extends org.topcased.gpm.domain.fields.FieldsContainer> getEntityClass(
            Class<? extends FieldsContainer> pClazz) {
        if (pClazz.equals(FieldsContainer.class)) {
            return org.topcased.gpm.domain.fields.FieldsContainer.class;
        }
        else if (pClazz.equals(SheetType.class)) {
            return org.topcased.gpm.domain.sheet.SheetType.class;
        }
        else if (pClazz.equals(ProductType.class)) {
            return org.topcased.gpm.domain.product.ProductType.class;
        }
        else if (pClazz.equals(LinkType.class)) {
            return org.topcased.gpm.domain.link.LinkType.class;
        }
        else {
            throw new NotImplementedException("The type '" + pClazz
                    + "' is not supported.");
        }
    }

    /**
     * Get entity class implementation corresponding to the business class
     * (serialization)
     * 
     * @param pClazz
     *            Business class (serialization)
     * @return Entity implementation class
     * @throws NotImplementedException
     *             If the Business class is not handle.
     */
    public static Class<? extends org.topcased.gpm.domain.fields.FieldsContainer>
    getEntityImplClass(Class<? extends FieldsContainer> pClazz) {
        if (pClazz.equals(FieldsContainer.class)) {
            return org.topcased.gpm.domain.fields.FieldsContainer.class;
        }
        else if (pClazz.equals(SheetType.class)) {
            return org.topcased.gpm.domain.sheet.SheetType.class;
        }
        else if (pClazz.equals(ProductType.class)) {
            return org.topcased.gpm.domain.product.ProductType.class;
        }
        else if (pClazz.equals(LinkType.class)) {
            return org.topcased.gpm.domain.link.LinkType.class;
        }
        else {
            throw new NotImplementedException("The type '" + pClazz
                    + "' is not supported.");
        }
    }

    /**
     * Get business class (cacheable) implementation corresponding to the
     * business class (serialization)
     * 
     * @param pClazz
     *            Business class (serialization)
     * @return Business (cacheable) class
     * @throws NotImplementedException
     *             If the Business class is not handle.
     */
    public static Class<? extends CacheableFieldsContainer> getCachedClass(
            Class<? extends FieldsContainer> pClazz) {
        if (pClazz.equals(FieldsContainer.class)) {
            return CacheableFieldsContainer.class;
        }
        else if (pClazz.equals(SheetType.class)) {
            return CacheableSheetType.class;
        }
        else if (pClazz.equals(ProductType.class)) {
            return CacheableProductType.class;
        }
        else if (pClazz.equals(LinkType.class)) {
            return CacheableLinkType.class;
        }
        else {
            throw new NotImplementedException("The type '" + pClazz
                    + "' is not supported.");
        }
    }

    /**
     * Get the entity class corresponding to this business class.
     * <p>
     * Only FieldsContainer, SheetType, ProductType and LinkType are handled.
     * 
     * @param pClazz
     *            Business class
     * @return Entity class
     * @throws NotImplementedException
     *             If the Business class is not handle.
     */
    public static Class<? extends ValuesContainer> getEntityValuesContainerClass(
            Class<? extends CacheableValuesContainer> pClazz) {
        if (pClazz.equals(CacheableValuesContainer.class)) {
            return ValuesContainer.class;
        }
        else if (pClazz.equals(CacheableSheet.class)) {
            return Sheet.class;
        }
        else if (pClazz.equals(CacheableProduct.class)) {
            return Product.class;
        }
        else if (pClazz.equals(CacheableLink.class)) {
            return Link.class;
        }
        else {
            throw new NotImplementedException("The type '" + pClazz
                    + "' is not supported.");
        }
    }

    /**
     * Get the entity class corresponding to this filter type.
     * <p>
     * Only Sheet, Product and Link are handled.
     * 
     * @param pFilterTypeData
     *            Type of the filter
     * @return Entity class
     * @throws NotImplementedException
     *             If the filter's type is not handle.
     */
    public static Class<? extends ValuesContainer> getEntityValuesContainerClass(
            final FilterTypeData pFilterTypeData) {

        final Class<? extends ValuesContainer> lClazz;
        switch (pFilterTypeData) {
            case SHEET:
                lClazz = Sheet.class;
                break;
            case PRODUCT:
                lClazz = Product.class;
                break;
            case LINK:
                lClazz = Link.class;
                break;
            default:
                throw new NotImplementedException("The type '"
                        + pFilterTypeData + "' is not supported.");
        }
        return lClazz;
    }
}
