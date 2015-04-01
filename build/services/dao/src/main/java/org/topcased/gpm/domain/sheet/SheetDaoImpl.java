/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin), Thomas Szadel
 * (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.domain.sheet;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.topcased.gpm.domain.businessProcess.BusinessProcess;
import org.topcased.gpm.domain.dynamic.container.sheet.DynamicSheetGeneratorFactory;
import org.topcased.gpm.domain.product.Product;

/**
 * @see org.topcased.gpm.domain.sheet.FieldsContainer
 * @author llatil
 */
public class SheetDaoImpl
        extends
        org.topcased.gpm.domain.AbstractDao<org.topcased.gpm.domain.sheet.Sheet, java.lang.String>
        implements org.topcased.gpm.domain.sheet.SheetDao {

    /**
     * Create a DAO.
     */
    public SheetDaoImpl() {
        super(org.topcased.gpm.domain.sheet.Sheet.class);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.sheet.FieldsContainer#getSheets(org.topcased.gpm.domain.sheet.SheetType)
     */
    @SuppressWarnings("unchecked")
    public List<Sheet> getSheets(final SheetType pDefinition) {
        final Query lQuery =
                getSession(false).createQuery(
                        "FROM org.topcased.gpm.domain.sheet.Sheet AS sheet "
                                + "WHERE sheet.definition.id = :definitionId ORDER BY reference");
        lQuery.setParameter("definitionId", pDefinition.getId());

        return (List<Sheet>) lQuery.list();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.sheet.SheetDaoBase#getSheets(org.topcased.gpm.domain.product.Product)
     */
    @SuppressWarnings("unchecked")
    public List<Sheet> getSheets(final Product pProduct) {
        final Query lQuery =
                getSession(false).createQuery(
                        "FROM org.topcased.gpm.domain.sheet.Sheet AS sheet "
                                + "WHERE sheet.product.id = :productId ORDER BY reference");
        lQuery.setParameter("productId", pProduct.getId());

        return lQuery.list();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.sheet.SheetDaoBase#getSheetsCount(org.topcased.gpm.domain.sheet.SheetType)
     */
    public Integer getSheetsCount(final SheetType pDefinition) {
        final Query lQuery =
                getSession(false).createQuery(
                        "select count(*) FROM org.topcased.gpm.domain.sheet.Sheet as sheet "
                                + "where sheet.definition.id = :definitionId");
        lQuery.setParameter("definitionId", pDefinition.getId());

        return Integer.parseInt(lQuery.uniqueResult().toString());
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.sheet.SheetDaoBase#getSheetsCount(org.topcased.gpm.domain.product.Product)
     */
    public Integer getSheetsCount(final String pProductId) {
        final Query lQuery =
                getSession(false).createQuery(
                        "select count(*) FROM org.topcased.gpm.domain.sheet.Sheet as sheet "
                                + "where sheet.product.id = :productId");
        lQuery.setParameter("productId", pProductId);

        Number lResult = (Number) lQuery.uniqueResult();
        return new Integer(lResult.intValue());
    }

    private static final String ID_BY_REFERENCE =
            "select sheet.id "
                    + "FROM org.topcased.gpm.domain.sheet.Sheet as sheet "
                    + "WHERE sheet.reference = :ref and sheet.product.name = :productName "
                    + "and sheet.product.businessProcess.name = :processName";

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.sheet.SheetDaoBase#getIdByReference(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    @Override
    public String getIdByReference(String pProcessName, String pProductName,
            String pReference) {
        final String lQueryString = ID_BY_REFERENCE;
        final Query lQuery = getSession(false).createQuery(lQueryString);

        lQuery.setParameter("processName", pProcessName);
        lQuery.setParameter("productName", pProductName);
        lQuery.setParameter("ref", pReference);
        return (String) lQuery.uniqueResult();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.sheet.SheetDaoBase#sheetsIterator(org.topcased.gpm.domain.product.Product)
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Iterator sheetsIterator(final Product pProduct) {
        final String lQueryString =
                "select sheet.id from "
                        + Sheet.class.getName()
                        + " sheet where sheet.product = :product order by sheet.reference";

        Query lQuery = getSession(false).createQuery(lQueryString);
        lQuery.setParameter("productId", pProduct.getId());

        return lQuery.iterate();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.sheet.SheetDaoBase#sheetsIterator(org.topcased.gpm.domain.businessProcess.BusinessProcess)
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Iterator sheetsIterator(final BusinessProcess pProcess) {
        final String lQueryString =
                "select sheet.id from "
                        + Sheet.class.getName()
                        + " sheet where sheet.product.businessProcess = :process order by sheet.reference";

        final Query lQuery = getSession(false).createQuery(lQueryString);
        lQuery.setParameter("processId", pProcess.getId());

        return lQuery.iterate();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.sheet.SheetDaoBase#sheetsIterator(java.lang.String,
     *      java.util.Collection, java.util.Collection)
     */
    @Override
    public Iterator<String> sheetsIterator(final String pProcessName,
            final Collection<String> pProductNames,
            final Collection<String> pTypeNames) {
        final StringBuffer lQueryString =
                new StringBuffer(
                        "select sheet.id from "
                                + Sheet.class.getName()
                                + " sheet where sheet.product.businessProcess.name = :processName ");

        if (!pProductNames.isEmpty()) {
            lQueryString.append("and sheet.product.name in (:productNames) ");
        }
        if (!pTypeNames.isEmpty()) {
            lQueryString.append("and sheet.definition.name in (:typeNames) ");
        }
        lQueryString.append("order by sheet.reference");

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
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.sheet.SheetDaoBase#getProductName()
     */
    public String getProductName(final String pSheetId) {
        final String lQueryString =
                "select sheet.product.name from " + Sheet.class.getName()
                        + " sheet where sheet.id = :pSheetId";
        final Query lQuery = getSession(false).createQuery(lQueryString);
        lQuery.setParameter("pSheetId", pSheetId);
        return (String) lQuery.uniqueResult();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.sheet.SheetDao#getNewSheet(java.lang.String)
     */
    public Sheet getNewSheet(final String pTypeId) {
        return DynamicSheetGeneratorFactory.getInstance().getDynamicObjectGenerator(
                pTypeId).create();
    }

    private static final String IS_REFERENCE_EXISTS =
            "select sheet.id "
                    + "FROM org.topcased.gpm.domain.sheet.Sheet as sheet "
                    + "WHERE sheet.reference = :ref and sheet.product.name = :productName "
                    + "and sheet.definition.name =:typeName";

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.sheet.SheetDaoBase#isReferenceExists(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    @Override
    public Boolean isReferenceExists(String pTypeName, String pProductName,
            String pReference) {
        final Query lQuery = createQuery(IS_REFERENCE_EXISTS);
        lQuery.setParameter("ref", pReference);
        lQuery.setParameter("productName", pProductName);
        lQuery.setParameter("typeName", pTypeName);
        return hasResult(lQuery);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.sheet.SheetDaoBase#getStateName(java.lang.String)
     */
    @Override
    public String getStateName(String pSheetId) {
        final Query lQuery =
                createQuery("SELECT node.name FROM "
                        + Sheet.class.getName()
                        + " as sheet, org.topcased.gpm.domain.process.Node as node"
                        + " WHERE sheet.id = :pId "
                        + "AND sheet.currentNode = node.id");

        lQuery.setParameter("pId", pSheetId);

        return (String) lQuery.uniqueResult();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.sheet.Sheet#getSheetByReference(org.topcased.gpm.domain.product.Product,
     *      java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    public org.topcased.gpm.domain.sheet.Sheet getSheetByReference(
            final org.topcased.gpm.domain.product.Product pProduct,
            final java.lang.String pReference) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.sheet.Sheet as sheet where sheet.product.id = :productId and sheet.reference = :reference";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("productId", pProduct.getId());
            lQueryObject.setParameter("reference", pReference);
            final java.util.List lResults = lQueryObject.list();
            org.topcased.gpm.domain.sheet.Sheet lResult = null;
            if (lResults != null) {
                if (lResults.size() > 1) {
                    throw new org.springframework.dao.InvalidDataAccessResourceUsageException(
                            "More than one instance of 'org.topcased.gpm.domain.sheet.Sheet"
                                    + "' was found when executing query --> '"
                                    + lQueryString + "'");
                }
                else if (lResults.size() == 1) {
                    lResult =
                            (org.topcased.gpm.domain.sheet.Sheet) lResults.iterator().next();
                }
            }
            return lResult;
        }
        catch (org.hibernate.HibernateException ex) {
            throw super.convertHibernateAccessException(ex);
        }
    }

    /**
     * @see org.topcased.gpm.domain.sheet.Sheet#load(java.lang.String, boolean)
     */
    @SuppressWarnings("rawtypes")
    public org.topcased.gpm.domain.fields.ValuesContainer load(
            final java.lang.String pId, final boolean pEagerLoading) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.sheet.Sheet as sheet where sheet.id = :id and sheet.eagerLoading = :eagerLoading";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("id", pId);
            lQueryObject.setParameter("eagerLoading",
                    Boolean.valueOf(pEagerLoading));
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
     * @see org.topcased.gpm.domain.sheet.Sheet#getVersion(java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    public java.lang.Integer getVersion(final java.lang.String pId) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.sheet.Sheet as sheet where sheet.id = :id";
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
     * @see org.topcased.gpm.domain.sheet.Sheet#getCount(java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    public java.lang.Long getCount(final java.lang.String pFieldsContainerId) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.sheet.Sheet as sheet where sheet.fieldsContainerId = :fieldsContainerId";
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
     * @see org.topcased.gpm.domain.sheet.Sheet#getAll(java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    public java.util.List getAll(final java.lang.String pFieldsContainerId) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.sheet.Sheet as sheet where sheet.fieldsContainerId = :fieldsContainerId";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("fieldsContainerId", pFieldsContainerId);
            java.util.List lResults = lQueryObject.list();
            return lResults;
        }
        catch (org.hibernate.HibernateException ex) {
            throw super.convertHibernateAccessException(ex);
        }
    }

    /**
     * @see org.topcased.gpm.domain.sheet.Sheet#deleteContainers(java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    public java.lang.Boolean deleteContainers(
            final java.lang.String pFieldsContainerId) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.sheet.Sheet as sheet where sheet.fieldsContainerId = :fieldsContainerId";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("fieldsContainerId", pFieldsContainerId);
            java.util.List lResults = lQueryObject.list();
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
     * @see org.topcased.gpm.domain.sheet.Sheet#getTypeId(java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    public java.lang.String getTypeId(final java.lang.String pValuesContainerId) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.sheet.Sheet as sheet where sheet.ValuesContainerId = :ValuesContainerId";
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
     * @see org.topcased.gpm.domain.sheet.Sheet#getTypesId(java.util.Collection<
     *      String>)
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public java.util.List<String> getTypesId(
            final java.util.Collection<String> pIds) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.sheet.Sheet as sheet where sheet.Ids = :Ids";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("Ids", pIds);
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
     * @see org.topcased.gpm.domain.sheet.Sheet#getLinkedElements(java.lang.String,
     *      java.lang.String)
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public java.util.List<String> getLinkedElements(final java.lang.String pId,
            final java.lang.String pLinkTypeName) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.sheet.Sheet as sheet where sheet.id = :id and sheet.linkTypeName = :linkTypeName";
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
     * @see org.topcased.gpm.domain.sheet.Sheet#removeSubField(java.lang.Object)
     */
    @SuppressWarnings("rawtypes")
    public java.lang.Object removeSubField(final java.lang.Object pSubField) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.sheet.Sheet as sheet where sheet.subField = :subField";
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
     * @see org.topcased.gpm.domain.sheet.Sheet#getAllId(java.lang.String)
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public java.util.List<String> getAllId(final java.lang.String pTypeId) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.sheet.Sheet as sheet where sheet.typeId = :typeId";
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
     * @see org.topcased.gpm.domain.sheet.Sheet#getIdByReference(java.lang.String,
     *      java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    public java.lang.String getIdByReference(final java.lang.String pReference,
            final java.lang.String pTypeName) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.sheet.Sheet as sheet where sheet.reference = :reference and sheet.typeName = :typeName";
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
     * @see org.topcased.gpm.domain.sheet.Sheet#getTypeName(java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    public java.lang.String getTypeName(
            final java.lang.String pValuesContainerId) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.sheet.Sheet as sheet where sheet.valuesContainerId = :valuesContainerId";
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
     * @see org.topcased.gpm.domain.sheet.Sheet#getFunctionalReference(java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    public java.lang.String getFunctionalReference(
            final java.lang.String pValuesContainerId) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.sheet.Sheet as sheet where sheet.valuesContainerId = :valuesContainerId";
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
     * @see org.topcased.gpm.domain.sheet.Sheet#getAllAttrNames(org.topcased.gpm.domain.attributes.AttributesContainer)
     */
    @SuppressWarnings("rawtypes")
    public java.util.List getAllAttrNames(
            final org.topcased.gpm.domain.attributes.AttributesContainer pAttrContainer) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.sheet.Sheet as sheet where sheet.attrContainer.id = :attrContainerId";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("attrContainerId", pAttrContainer.getId());
            java.util.List lResults = lQueryObject.list();
            return lResults;
        }
        catch (org.hibernate.HibernateException ex) {
            throw super.convertHibernateAccessException(ex);
        }
    }

    /**
     * @see org.topcased.gpm.domain.sheet.Sheet#getAttributes(org.topcased.gpm.domain.attributes.AttributesContainer,
     *      java.lang.String[])
     */
    @SuppressWarnings("rawtypes")
    public java.util.List getAttributes(
            final org.topcased.gpm.domain.attributes.AttributesContainer pAttrContainer,
            final java.lang.String[] pAttrNames) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.sheet.Sheet as sheet where sheet.attrContainer.id = :attrContainerId and sheet.attrNames = :attrNames";
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
     * @see org.topcased.gpm.domain.sheet.Sheet#getAttribute(org.topcased.gpm.domain.attributes.AttributesContainer,
     *      java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    public org.topcased.gpm.domain.attributes.Attribute getAttribute(
            final org.topcased.gpm.domain.attributes.AttributesContainer pAttrContainer,
            final java.lang.String pAttrName) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.sheet.Sheet as sheet where sheet.attrContainer.id = :attrContainerId and sheet.attrName = :attrName";
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