/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin)
 ******************************************************************/
//license-header java merge-point
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 */
package org.topcased.gpm.domain.product;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.topcased.gpm.domain.dynamic.container.product.DynamicProductGeneratorFactory;

/**
 * @see org.topcased.gpm.domain.product.Product
 * @author llatil
 */
public class ProductDaoImpl
        extends
        org.topcased.gpm.domain.AbstractDao<org.topcased.gpm.domain.product.Product, java.lang.String>
        implements org.topcased.gpm.domain.product.ProductDao {

    private final static String PRODUCT_BY_NAME =
            "FROM " + Product.class.getName() + " p "
                    + "WHERE p.businessProcess.name = :processName "
                    + "AND p.name = :productName";

    /**
     * Constructor
     */
    public ProductDaoImpl() {
        super(org.topcased.gpm.domain.product.Product.class);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.product.ProductDaoBase#getProduct(java.lang.String,
     *      java.lang.String)
     */
    public Product getProduct(final String pProcessName,
            final String pProductName) {
        final Query lQuery = getSession(false).createQuery(PRODUCT_BY_NAME);

        lQuery.setParameter("processName", pProcessName);
        lQuery.setParameter("productName", pProductName);

        return (Product) lQuery.uniqueResult();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.product.ProductDaoBase#getProducts(org.topcased.gpm.domain.product.ProductType)
     */
    @SuppressWarnings("unchecked")
    public List<Product> getProducts(final ProductType pProductType) {
        final Query lQuery =
                getSession(false).createQuery(
                        "from org.topcased.gpm.domain.product.Product as product "
                                + "where product.definition.id = :productTypeId");
        lQuery.setParameter("productTypeId", pProductType.getId());

        return lQuery.list();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.product.ProductDaoBase#getParentNames(java.lang.String,
     *      java.lang.String)
     */
    @SuppressWarnings("unchecked")
    public List<String> getParentNames(final String pProcessName,
            final String pProductName) {
        final String lStringQuery =
                "select product.name from Product product where "
                        + "product.definition.businessProcess.name = :bPName "
                        + "and ( exists (from Product child where child in elements(product.children) and child.name = :productName)) ";

        final Query lQuery = getSession(false).createQuery(lStringQuery);
        lQuery.setParameter("bPName", pProcessName);
        lQuery.setParameter("productName", pProductName);

        return lQuery.list();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.product.ProductDaoBase#getParentNamesFromId(java.lang.String)
     */
    @SuppressWarnings("unchecked")
    public List<String> getParentNamesFromId(final String pProductId) {
        final String lStringQuery =
                "select product.name from Product product where "
                        + "exists (from Product child where "
                        + "child in elements(product.children) and child.id = :productId))";

        final Query lQuery = getSession(false).createQuery(lStringQuery);
        lQuery.setParameter("productId", pProductId);

        return lQuery.list();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.product.ProductDaoBase#getProductsWithoutParent(java.lang.String)
     */
    @SuppressWarnings("unchecked")
    public List<Product> getProductsWithoutParent(final String pProcessName) {
        final String lStringQuery =
                "from Product as product where "
                        + "product.definition.businessProcess.name = :bPName "
                        + "and (not exists (from Product parent where product in elements(parent.children)))"
                        + "order by product.name";
        final Query lQuery = getSession(false).createQuery(lStringQuery);
        lQuery.setParameter("bPName", pProcessName);
        return lQuery.list();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.product.ProductDaoBase#getProductsWithoutParent(java.lang.String,
     *      java.util.List)
     */
    @SuppressWarnings("unchecked")
    public List<Product> getProductsWithoutParent(final String pProcessName,
            final List<String> pAccessibleProductsName) {
        final String lStringQuery =
                "from Product as product where "
                        + "product.definition.businessProcess.name = :bPName "
                        + "and (not exists (from Product parent where product in elements(parent.children)))"
                        + "and (product in (from Product p where (p.children is empty and p.name in (:param)) or (p.children is not empty))) "
                        + "order by product.name";
        final Query lQuery = getSession(false).createQuery(lStringQuery);
        lQuery.setParameter("bPName", pProcessName);
        lQuery.setParameterList("param", pAccessibleProductsName);
        return lQuery.list();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.product.ProductDaoBase#getProductNames(java.lang.String,
     *      boolean)
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public List getProductNames(final String pProcessName, boolean pTopLevelOnly) {
        String lStringQuery =
                "select product.name from " + Product.class.getName()
                        + " product where "
                        + "product.businessProcess.name = :bPName ";
        if (pTopLevelOnly) {
            lStringQuery +=
                    "and (not exists (from "
                            + Product.class.getName()
                            + " parent where product in elements(parent.children))) ";
        }
        lStringQuery += "order by product.name";
        final Query lQuery = getSession(false).createQuery(lStringQuery);
        lQuery.setParameter("bPName", pProcessName);
        return lQuery.list();
    }

    private final static String IS_PRODUCT_EXISTS =
            "FROM " + Product.class.getName() + " p "
                    + "WHERE p.businessProcess.name = :processName "
                    + "AND p.name = :productName";

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.product.ProductDaoBase#isProductExists(java.lang.String,
     *      java.lang.String)
     */
    public Boolean isProductExists(final String pProcessName,
            final String pProductName) {
        final Query lQuery = createQuery(IS_PRODUCT_EXISTS);

        lQuery.setParameter("processName", pProcessName);
        lQuery.setParameter("productName", pProductName);
        lQuery.setMaxResults(1);

        return hasResult(lQuery);
    }

    private final static String SUB_PRODUCTS_NAME =
            "SELECT c.name FROM " + Product.class.getName()
                    + " p INNER JOIN p.children c "
                    + "WHERE p.businessProcess.name = :processName "
                    + "AND p.name = :productName ORDER BY c.name";

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.product.ProductDaoBase#getSubProductsName(java.lang.String,
     *      java.lang.String)
     */
    @SuppressWarnings("unchecked")
    public List<String> getSubProductsName(final String pProcessName,
            final String pProductName) {
        final Query lQuery = getSession(false).createQuery(SUB_PRODUCTS_NAME);

        lQuery.setParameter("processName", pProcessName);
        lQuery.setParameter("productName", pProductName);

        return lQuery.list();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.product.ProductDaoBase#getNewProduct(java.lang.String)
     */
    public Product getNewProduct(final String pTypeId) {
        return DynamicProductGeneratorFactory.getInstance().getDynamicObjectGenerator(
                pTypeId).create();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.product.ProductDaoBase#productsIterator(java.lang.String,
     *      java.util.Collection, java.util.Collection)
     */
    @Override
    public Iterator<String> productsIterator(final String pProcessName,
            final Collection<String> pProductNames,
            final Collection<String> pTypeNames) {
        final StringBuffer lQueryString =
                new StringBuffer("select p.id from " + Product.class.getName()
                        + " p where p.businessProcess.name = :processName ");

        if (!pProductNames.isEmpty()) {
            lQueryString.append("and p.name in (:productNames) ");
        }
        if (!pTypeNames.isEmpty()) {
            lQueryString.append("and p.definition.name in (:typeNames) ");
        }
        lQueryString.append("order by p.name");

        final Query lQuery = createQuery(lQueryString.toString());

        lQuery.setParameter("processName", pProcessName);
        if (!pProductNames.isEmpty()) {
            lQuery.setParameterList("productNames", pProductNames);
        }
        if (!pTypeNames.isEmpty()) {
            lQuery.setParameterList("typeNames", pTypeNames);
        }

        return iterate(lQuery, String.class);
    }

    /**
     * @see org.topcased.gpm.domain.product.Product#getId(String,
     *      java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    public java.lang.String getId(final String pProcessName,
            final java.lang.String pProductName) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.product.Product as product where product.processName = :processName and product.productName = :productName";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("processName", pProcessName);
            lQueryObject.setParameter("productName", pProductName);
            java.util.List lResults = lQueryObject.list();
            java.lang.String lResult = null;
            if (lResults != null) {
                if (lResults.size() > 1) {
                    throw new org.springframework.dao.InvalidDataAccessResourceUsageException(
                            "More than one instance of 'java.lang.String"
                                    + "' was found when executing query --> '"
                                    + lQueryString + "'");
                }
                else if (lResults.size() == 1) {
                    lResult = (java.lang.String) lResults.iterator().next();
                }
            }
            return lResult;
        }
        catch (org.hibernate.HibernateException ex) {
            throw super.convertHibernateAccessException(ex);
        }
    }

    /**
     * @see org.topcased.gpm.domain.product.Product#load(java.lang.String,
     *      boolean)
     */
    @SuppressWarnings("rawtypes")
    public org.topcased.gpm.domain.fields.ValuesContainer load(
            final java.lang.String pId, final boolean pEagerLoading) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.product.Product as product where product.id = :id and product.eagerLoading = :eagerLoading";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("id", pId);
            lQueryObject.setParameter("eagerLoading",
                    java.lang.Boolean.valueOf(pEagerLoading));
            java.util.List lResults = lQueryObject.list();
            org.topcased.gpm.domain.fields.ValuesContainer lResult = null;
            if (lResults != null) {
                if (lResults.size() > 1) {
                    throw new org.springframework.dao.InvalidDataAccessResourceUsageException(
                            "More than one instance of 'org.topcased.gpm.domain.fields.ValuesContainer"
                                    + "' was found when executing query --> '"
                                    + lQueryString + "'");
                }
                else if (lResults.size() == 1) {
                    lResult =
                            (org.topcased.gpm.domain.fields.ValuesContainer) lResults.iterator().next();
                }
            }
            return lResult;
        }
        catch (org.hibernate.HibernateException ex) {
            throw super.convertHibernateAccessException(ex);
        }
    }

    /**
     * @see org.topcased.gpm.domain.product.Product#getVersion(java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    public java.lang.Integer getVersion(final java.lang.String pId) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.product.Product as product where product.id = :id";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("id", pId);
            java.util.List lResults = lQueryObject.list();
            java.lang.Integer lResult = null;
            if (lResults != null) {
                if (lResults.size() > 1) {
                    throw new org.springframework.dao.InvalidDataAccessResourceUsageException(
                            "More than one instance of 'java.lang.Integer"
                                    + "' was found when executing query --> '"
                                    + lQueryString + "'");
                }
                else if (lResults.size() == 1) {
                    lResult = (java.lang.Integer) lResults.iterator().next();
                }
            }
            return lResult;
        }
        catch (org.hibernate.HibernateException ex) {
            throw super.convertHibernateAccessException(ex);
        }
    }

    /**
     * @see org.topcased.gpm.domain.product.Product#getCount(java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    public java.lang.Long getCount(final java.lang.String pFieldsContainerId) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.product.Product as product where product.fieldsContainerId = :fieldsContainerId";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("fieldsContainerId", pFieldsContainerId);
            java.util.List lResults = lQueryObject.list();
            java.lang.Long lResult = null;
            if (lResults != null) {
                if (lResults.size() > 1) {
                    throw new org.springframework.dao.InvalidDataAccessResourceUsageException(
                            "More than one instance of 'java.lang.Long"
                                    + "' was found when executing query --> '"
                                    + lQueryString + "'");
                }
                else if (lResults.size() == 1) {
                    lResult = (java.lang.Long) lResults.iterator().next();
                }
            }
            return lResult;
        }
        catch (org.hibernate.HibernateException ex) {
            throw super.convertHibernateAccessException(ex);
        }
    }

    /**
     * @see org.topcased.gpm.domain.product.Product#getAll(java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    public java.util.List getAll(final java.lang.String pFieldsContainerId) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.product.Product as product where product.fieldsContainerId = :fieldsContainerId";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("fieldsContainerId", pFieldsContainerId);
            final java.util.List lResults = lQueryObject.list();
            return lResults;
        }
        catch (org.hibernate.HibernateException ex) {
            throw super.convertHibernateAccessException(ex);
        }
    }

    /**
     * @see org.topcased.gpm.domain.product.Product#deleteContainers(java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    public java.lang.Boolean deleteContainers(
            final java.lang.String pFieldsContainerId) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.product.Product as product where product.fieldsContainerId = :fieldsContainerId";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("fieldsContainerId", pFieldsContainerId);
            final java.util.List lResults = lQueryObject.list();
            java.lang.Boolean lResult = null;
            if (lResults != null) {
                if (lResults.size() > 1) {
                    throw new org.springframework.dao.InvalidDataAccessResourceUsageException(
                            "More than one instance of 'java.lang.Boolean"
                                    + "' was found when executing query --> '"
                                    + lQueryString + "'");
                }
                else if (lResults.size() == 1) {
                    lResult = (java.lang.Boolean) lResults.iterator().next();
                }
            }
            return lResult;
        }
        catch (org.hibernate.HibernateException ex) {
            throw super.convertHibernateAccessException(ex);
        }
    }

    /**
     * @see org.topcased.gpm.domain.product.Product#getTypeId(java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    public java.lang.String getTypeId(final java.lang.String pValuesContainerId) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.product.Product as product where product.ValuesContainerId = :ValuesContainerId";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("ValuesContainerId", pValuesContainerId);
            java.util.List lResults = lQueryObject.list();
            java.lang.String lResult = null;
            if (lResults != null) {
                if (lResults.size() > 1) {
                    throw new org.springframework.dao.InvalidDataAccessResourceUsageException(
                            "More than one instance of 'java.lang.String"
                                    + "' was found when executing query --> '"
                                    + lQueryString + "'");
                }
                else if (lResults.size() == 1) {
                    lResult = (java.lang.String) lResults.iterator().next();
                }
            }
            return lResult;
        }
        catch (org.hibernate.HibernateException ex) {
            throw super.convertHibernateAccessException(ex);
        }
    }

    /**
     * @see 
     *      org.topcased.gpm.domain.product.Product#getTypesId(java.util.Collection
     *      <String>)
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public java.util.List<String> getTypesId(
            final java.util.Collection<String> pIds) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.product.Product as product where product.Ids = :Ids";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("Ids", pIds);
            final java.util.List lResults = lQueryObject.list();
            java.util.List<String> lResult = null;
            if (lResults != null) {
                if (lResults.size() > 1) {
                    throw new org.springframework.dao.InvalidDataAccessResourceUsageException(
                            "More than one instance of 'java.util.List<String>"
                                    + "' was found when executing query --> '"
                                    + lQueryString + "'");
                }
                else if (lResults.size() == 1) {
                    lResult =
                            (java.util.List<String>) lResults.iterator().next();
                }
            }
            return lResult;
        }
        catch (org.hibernate.HibernateException ex) {
            throw super.convertHibernateAccessException(ex);
        }
    }

    /**
     * @see org.topcased.gpm.domain.product.Product#getLinkedElements(java.lang.String,
     *      java.lang.String)
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public java.util.List<String> getLinkedElements(final java.lang.String pId,
            final java.lang.String pLinkTypeName) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.product.Product as product where product.id = :id and product.linkTypeName = :linkTypeName";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("id", pId);
            lQueryObject.setParameter("linkTypeName", pLinkTypeName);
            java.util.List lResults = lQueryObject.list();
            java.util.List<String> lResult = null;
            if (lResults != null) {
                if (lResults.size() > 1) {
                    throw new org.springframework.dao.InvalidDataAccessResourceUsageException(
                            "More than one instance of 'java.util.List<String>"
                                    + "' was found when executing query --> '"
                                    + lQueryString + "'");
                }
                else if (lResults.size() == 1) {
                    lResult =
                            (java.util.List<String>) lResults.iterator().next();
                }
            }
            return lResult;
        }
        catch (org.hibernate.HibernateException ex) {
            throw super.convertHibernateAccessException(ex);
        }
    }

    /**
     * @see org.topcased.gpm.domain.product.Product#removeSubField(java.lang.Object)
     */
    @SuppressWarnings("rawtypes")
    public java.lang.Object removeSubField(final java.lang.Object pSubField) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.product.Product as product where product.subField = :subField";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("subField", pSubField);
            java.util.List lResults = lQueryObject.list();
            java.lang.Object lResult = null;
            if (lResults != null) {
                if (lResults.size() > 1) {
                    throw new org.springframework.dao.InvalidDataAccessResourceUsageException(
                            "More than one instance of 'java.lang.Object"
                                    + "' was found when executing query --> '"
                                    + lQueryString + "'");
                }
                else if (lResults.size() == 1) {
                    lResult = (java.lang.Object) lResults.iterator().next();
                }
            }
            return lResult;
        }
        catch (org.hibernate.HibernateException ex) {
            throw super.convertHibernateAccessException(ex);
        }
    }

    /**
     * @see org.topcased.gpm.domain.product.Product#getAllId(java.lang.String)
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public java.util.List<String> getAllId(final java.lang.String pTypeId) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.product.Product as product where product.typeId = :typeId";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("typeId", pTypeId);
            java.util.List lResults = lQueryObject.list();
            java.util.List<String> lResult = null;
            if (lResults != null) {
                if (lResults.size() > 1) {
                    throw new org.springframework.dao.InvalidDataAccessResourceUsageException(
                            "More than one instance of 'java.util.List<String>"
                                    + "' was found when executing query --> '"
                                    + lQueryString + "'");
                }
                else if (lResults.size() == 1) {
                    lResult =
                            (java.util.List<String>) lResults.iterator().next();
                }
            }
            return lResult;
        }
        catch (org.hibernate.HibernateException ex) {
            throw super.convertHibernateAccessException(ex);
        }
    }

    /**
     * @see org.topcased.gpm.domain.product.Product#getIdByReference(java.lang.String,
     *      java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    public java.lang.String getIdByReference(final java.lang.String pReference,
            final java.lang.String pTypeName) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.product.Product as product where product.reference = :reference and product.typeName = :typeName";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("reference", pReference);
            lQueryObject.setParameter("typeName", pTypeName);
            java.util.List lResults = lQueryObject.list();
            java.lang.String lResult = null;
            if (lResults != null) {
                if (lResults.size() > 1) {
                    throw new org.springframework.dao.InvalidDataAccessResourceUsageException(
                            "More than one instance of 'java.lang.String"
                                    + "' was found when executing query --> '"
                                    + lQueryString + "'");
                }
                else if (lResults.size() == 1) {
                    lResult = (java.lang.String) lResults.iterator().next();
                }
            }
            return lResult;
        }
        catch (org.hibernate.HibernateException ex) {
            throw super.convertHibernateAccessException(ex);
        }
    }

    /**
     * @see org.topcased.gpm.domain.product.Product#getTypeName(java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    public java.lang.String getTypeName(
            final java.lang.String pValuesContainerId) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.product.Product as product where product.valuesContainerId = :valuesContainerId";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("valuesContainerId", pValuesContainerId);
            java.util.List lResults = lQueryObject.list();
            java.lang.String lResult = null;
            if (lResults != null) {
                if (lResults.size() > 1) {
                    throw new org.springframework.dao.InvalidDataAccessResourceUsageException(
                            "More than one instance of 'java.lang.String"
                                    + "' was found when executing query --> '"
                                    + lQueryString + "'");
                }
                else if (lResults.size() == 1) {
                    lResult = (java.lang.String) lResults.iterator().next();
                }
            }
            return lResult;
        }
        catch (org.hibernate.HibernateException ex) {
            throw super.convertHibernateAccessException(ex);
        }
    }

    /**
     * @see org.topcased.gpm.domain.product.Product#getFunctionalReference(java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    public java.lang.String getFunctionalReference(
            final java.lang.String pValuesContainerId) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.product.Product as product where product.valuesContainerId = :valuesContainerId";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("valuesContainerId", pValuesContainerId);
            java.util.List lResults = lQueryObject.list();
            java.lang.String lResult = null;
            if (lResults != null) {
                if (lResults.size() > 1) {
                    throw new org.springframework.dao.InvalidDataAccessResourceUsageException(
                            "More than one instance of 'java.lang.String"
                                    + "' was found when executing query --> '"
                                    + lQueryString + "'");
                }
                else if (lResults.size() == 1) {
                    lResult = (java.lang.String) lResults.iterator().next();
                }
            }
            return lResult;
        }
        catch (org.hibernate.HibernateException ex) {
            throw super.convertHibernateAccessException(ex);
        }
    }

    /**
     * @see org.topcased.gpm.domain.product.Product#getAllAttrNames(org.topcased.gpm.domain.attributes.AttributesContainer)
     */
    @SuppressWarnings("rawtypes")
    public java.util.List getAllAttrNames(
            final org.topcased.gpm.domain.attributes.AttributesContainer pAttrContainer) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.product.Product as product where product.attrContainer.id = :attrContainerId";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("attrContainerId", pAttrContainer.getId());
            final java.util.List lResults = lQueryObject.list();
            return lResults;
        }
        catch (org.hibernate.HibernateException ex) {
            throw super.convertHibernateAccessException(ex);
        }
    }

    /**
     * @see org.topcased.gpm.domain.product.Product#getAttributes(org.topcased.gpm.domain.attributes.AttributesContainer,
     *      java.lang.String[])
     */
    @SuppressWarnings("rawtypes")
    public java.util.List getAttributes(
            final org.topcased.gpm.domain.attributes.AttributesContainer pAttrContainer,
            final java.lang.String[] pAttrNames) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.product.Product as product where product.attrContainer.id = :attrContainerId and product.attrNames = :attrNames";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("attrContainerId", pAttrContainer.getId());
            lQueryObject.setParameterList("attrNames", pAttrNames);
            java.util.List lResults = lQueryObject.list();
            return lResults;
        }
        catch (org.hibernate.HibernateException ex) {
            throw super.convertHibernateAccessException(ex);
        }
    }

    /**
     * @see org.topcased.gpm.domain.product.Product#getAttribute(org.topcased.gpm.domain.attributes.AttributesContainer,
     *      java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    public org.topcased.gpm.domain.attributes.Attribute getAttribute(
            final org.topcased.gpm.domain.attributes.AttributesContainer pAttrContainer,
            final java.lang.String pAttrName) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.product.Product as product where product.attrContainer.id = :attrContainerId and product.attrName = :attrName";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("attrContainerId", pAttrContainer.getId());
            lQueryObject.setParameter("attrName", pAttrName);
            java.util.List lResults = lQueryObject.list();
            org.topcased.gpm.domain.attributes.Attribute lResult = null;
            if (lResults != null) {
                if (lResults.size() > 1) {
                    throw new org.springframework.dao.InvalidDataAccessResourceUsageException(
                            "More than one instance of 'org.topcased.gpm.domain.attributes.Attribute"
                                    + "' was found when executing query --> '"
                                    + lQueryString + "'");
                }
                else if (lResults.size() == 1) {
                    lResult =
                            (org.topcased.gpm.domain.attributes.Attribute) lResults.iterator().next();
                }
            }
            return lResult;
        }
        catch (org.hibernate.HibernateException ex) {
            throw super.convertHibernateAccessException(ex);
        }
    }
}