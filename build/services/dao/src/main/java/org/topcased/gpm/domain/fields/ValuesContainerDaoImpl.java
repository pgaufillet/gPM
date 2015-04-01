/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.domain.fields;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.topcased.gpm.domain.link.Link;

/**
 * @author llatil
 */
public class ValuesContainerDaoImpl
        extends
        org.topcased.gpm.domain.AbstractDao<org.topcased.gpm.domain.fields.ValuesContainer, java.lang.String>
        implements org.topcased.gpm.domain.fields.ValuesContainerDao {

    private static final int MAX_IN_CLAUSE = 990;

    /**
     * Constructor
     */
    public ValuesContainerDaoImpl() {
        super(org.topcased.gpm.domain.fields.ValuesContainer.class);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.fields.ValuesContainerDaoBase#load(java.lang.String,
     *      boolean)
     */
    @SuppressWarnings("rawtypes")
    public ValuesContainer load(final String pId, final boolean pEagerLoading) {
        String lEagerStr = StringUtils.EMPTY;
        if (pEagerLoading) {
            lEagerStr = "left join fetch container.fieldValues ";
        }

        final java.util.List lList;
        lList =
                this.getHibernateTemplate().find(
                        "from org.topcased.gpm.domain.fields.ValuesContainer as container "
                                + lEagerStr + " where container.id = ?", pId);
        final Object lEntity =
                lList != null && !lList.isEmpty() ? lList.iterator().next()
                        : null;
        return (ValuesContainer) lEntity;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.fields.ValuesContainerDaoBase#getVersion(java.lang.String)
     */
    public Integer getVersion(final String pId) {
        Query lQuery =
                getSession(false).createQuery(
                        "select container.version "
                                + "FROM org.topcased.gpm.domain.fields.ValuesContainer as container "
                                + "WHERE container.id = :id");
        lQuery.setParameter("id", pId);
        return (Integer) lQuery.uniqueResult();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.fields.ValuesContainerDaoBase#deleteContainers(java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    public Boolean deleteContainers(final String pFieldsContainerId) {
        Session lSession = getSession(false);

        final Query lQuery =
                lSession.createQuery("FROM org.topcased.gpm.domain.fields.ValuesContainer as container "
                        + "WHERE container.definition.id = :fieldsContainerId");
        lQuery.setParameter("fieldsContainerId", pFieldsContainerId);

        Iterator lIter = lQuery.iterate();
        while (lIter.hasNext()) {
            ValuesContainer lValuesContainer = (ValuesContainer) lIter.next();
            lSession.delete(lValuesContainer);
        }

        return Boolean.TRUE;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.fields.ValuesContainerDaoBase#getCount(java.lang.String)
     */
    public Long getCount(final String pFieldsContainerId) {
        final String lQueryStr =
                "SELECT count(*) "
                        + "FROM org.topcased.gpm.domain.fields.ValuesContainer as container "
                        + "WHERE container.definition.id = :fieldsContainerId";
        final Session lSession = getSession(false);

        final Query lQuery = lSession.createQuery(lQueryStr);
        lQuery.setParameter("fieldsContainerId", pFieldsContainerId);
        return (Long) lQuery.uniqueResult();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.fields.ValuesContainerDaoBase#getAll(java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    public List getAll(final String pFieldsContainerId) {
        final Query lQuery =
                getSession(false).createQuery(
                        "from org.topcased.gpm.domain.fields.ValuesContainer as valuesContainer where "
                                + "valuesContainer.definition.id = :fieldsContainerId");
        lQuery.setParameter("fieldsContainerId", pFieldsContainerId);
        return lQuery.list();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.fields.ValuesContainerDaoBase#getTypeId(java.lang.String)
     */
    public String getTypeId(final String pId) {
        final Query lQuery =
                getSession(false).createQuery(
                        "select valuesContainer.definition.id "
                                + "from org.topcased.gpm.domain.fields.ValuesContainer as valuesContainer where "
                                + "valuesContainer.id = :id");
        lQuery.setParameter("id", pId);
        return (String) lQuery.uniqueResult();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.fields.ValuesContainerDaoBase#getTypesId(java.util.Collection)
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Set<String> getTypesId(final List<String> pIds) {

        final String lQueryBase =
                "select distinct valuesContainer.definition.id "
                        + "from org.topcased.gpm.domain.fields.ValuesContainer as valuesContainer where ";

        Set<String> lSheetTypeIds = null;
        Query lQuery = null;
        final StringBuilder lStringQuery = new StringBuilder(lQueryBase);
        if (MAX_IN_CLAUSE >= pIds.size()) {
            // The query can be handled by Oracle
            lStringQuery.append("valuesContainer.id in (:ids)");
            lQuery = getSession(false).createQuery(lStringQuery.toString());
            lQuery.setParameterList("ids", pIds);
        }
        else {
            // defined sub list
            List<List<String>> lSbubListRecord = new ArrayList<List<String>>();
            List<String> lSublist = null;
            for (int i = 0; i < pIds.size(); i++) {
                if (i % MAX_IN_CLAUSE == 0) {
                    lSublist = new ArrayList<String>(MAX_IN_CLAUSE);
                    lSbubListRecord.add(lSublist);
                }
                lSublist.add(pIds.get(i));
            }
            lQuery = getGeneratedQuery(lSbubListRecord, lStringQuery);

        }
        List lResult = lQuery.list();
        if (null != lResult) {
            lSheetTypeIds = new HashSet<String>(lQuery.list());
        }
        else {
            lSheetTypeIds = new HashSet<String>(0);
        }

        return lSheetTypeIds;
    }

    /**
     * Generated query whit dynamic where clause
     * 
     * @param pSbubListRecord
     *            subs parametrs list
     * @param pQueryBase
     *            The query comon part
     * @return Query
     */
    private Query getGeneratedQuery(List<List<String>> pSbubListRecord,
            final StringBuilder pStringBuilder) {

        pStringBuilder.append(" valuesContainer.id in ");

        // generating the where clause criterias
        for (int lIter = 0; lIter < pSbubListRecord.size(); lIter++) {
            if (lIter == 0) {
                pStringBuilder.append("(:lCriterias0)");
            }
            else {
                pStringBuilder.append(" OR ").append(" valuesContainer.id in ").append(
                        "(:lCriterias").append(lIter).append(")");
            }
        }
        // create query
        final Query lQuery =
                getSession(false).createQuery(pStringBuilder.toString());
        // set query parameters
        for (int lk = 0; lk < pSbubListRecord.size(); lk++) {
            lQuery.setParameterList("lCriterias" + lk, pSbubListRecord.get(lk));
        }
        return lQuery;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.fields.ValuesContainerDaoBase#getLinkedElements(java.lang.String,
     *      java.lang.String)
     */
    @SuppressWarnings("unchecked")
    public List<String> getLinkedElements(final String pContainerId,
            final String pLinkTypeName) {
        final String lStringQuery =
                "select linkedElement.id from "
                        + ValuesContainer.class.getName()
                        + " linkedElement, "
                        + Link.class.getName()
                        + " link where link.definition.name = :linkTypeName and "
                        + "( (linkedElement.id = link.origin.id and link.destination.id = :id) "
                        + " or ( linkedElement.id = link.destination.id and link.origin.id = :id)))";
        final Query lQuery = getSession(false).createQuery(lStringQuery);
        lQuery.setParameter("id", pContainerId);
        lQuery.setParameter("linkTypeName", pLinkTypeName);

        return (List<String>) lQuery.list();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.fields.ValuesContainerDao#removeSubField(java.lang.Object)
     */
    public Object removeSubField(final Object pSubField) {
        getSession(false).delete(pSubField);
        return null;
    }

    private static final String ALL_ID_QUERY =
            "SELECT v.id " + "FROM " + ValuesContainer.class.getName() + " v "
                    + "WHERE v.definition.id = :typeId";

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.fields.ValuesContainerDaoBase#getAllId(java.lang.String)
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<String> getAllId(String pTypeId) {
        final Query lQuery = createQuery(ALL_ID_QUERY);
        lQuery.setParameter("typeId", pTypeId);

        return lQuery.list();
    }

    private static final String REFERENCE =
            "SELECT vc.id FROM "
                    + ValuesContainer.class.getName()
                    + " vc WHERE vc.reference =:pReference AND vc.definition.name =:pTypeName";

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.fields.ValuesContainerDaoBase#getIdByReference(java.lang.String,
     *      java.lang.String)
     */
    @Override
    public String getIdByReference(String pReference, String pTypeName) {
        final Query lQuery = createQuery(REFERENCE);
        lQuery.setParameter("pReference", pReference);
        lQuery.setParameter("pTypeName", pTypeName);
        return (String) lQuery.uniqueResult();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.fields.ValuesContainerDaoBase#getTypeName(java.lang.String)
     */
    @Override
    public String getTypeName(String pValuesContainerId) {
        final Query lQuery =
                createQuery("SELECT valuesContainer.definition.name FROM "
                        + ValuesContainer.class.getName()
                        + " as valuesContainer WHERE valuesContainer.id=:pId");
        lQuery.setParameter("pId", pValuesContainerId);
        return (String) lQuery.uniqueResult();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.fields.ValuesContainerDaoBase#getFunctionalReference(java.lang.String)
     */
    @Override
    public String getFunctionalReference(String pValuesContainerId) {
        final Query lQuery =
                createQuery("SELECT valuesContainer.reference FROM "
                        + ValuesContainer.class.getName()
                        + " as valuesContainer WHERE valuesContainer.id=:pId");
        lQuery.setParameter("pId", pValuesContainerId);
        return (String) lQuery.uniqueResult();
    }

    /**
     * @see org.topcased.gpm.domain.fields.ValuesContainer#getAllAttrNames(org.topcased.gpm.domain.attributes.AttributesContainer)
     */
    @SuppressWarnings("rawtypes")
    public java.util.List getAllAttrNames(
            final org.topcased.gpm.domain.attributes.AttributesContainer pAttrContainer) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.fields.ValuesContainer as valuesContainer where valuesContainer.attrContainer.id = :attrContainerId";
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
     * @see org.topcased.gpm.domain.fields.ValuesContainer#getAttributes(org.topcased.gpm.domain.attributes.AttributesContainer,
     *      java.lang.String[])
     */
    @SuppressWarnings("rawtypes")
    public java.util.List getAttributes(
            final org.topcased.gpm.domain.attributes.AttributesContainer pAttrContainer,
            final java.lang.String[] pAttrNames) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.fields.ValuesContainer as valuesContainer where valuesContainer.attrContainer.id = :attrContainerId and valuesContainer.attrNames = :attrNames";
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
     * @see org.topcased.gpm.domain.fields.ValuesContainer#getAttribute(org.topcased.gpm.domain.attributes.AttributesContainer,
     *      java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    public org.topcased.gpm.domain.attributes.Attribute getAttribute(
            final org.topcased.gpm.domain.attributes.AttributesContainer pAttrContainer,
            final java.lang.String pAttrName) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.fields.ValuesContainer as valuesContainer where valuesContainer.attrContainer.id = :attrContainerId and valuesContainer.attrName = :attrName";
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
