/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available 
 * under the terms of the Lesser Gnu Public License (LGPL)which 
 * accompanies this distribution, and is available 
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Szadel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.domain.facilities;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.topcased.gpm.domain.fields.FieldsContainer;

/**
 * @author tszadel
 */
public class FilterGroupDaoImpl
        extends
        org.topcased.gpm.domain.AbstractDao<org.topcased.gpm.domain.facilities.FilterGroup, java.lang.String>
        implements org.topcased.gpm.domain.facilities.FilterGroupDao {
    /**
     * Constructor
     */
    public FilterGroupDaoImpl() {
        super(org.topcased.gpm.domain.facilities.FilterGroup.class);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.facilities.FilterGroupDaoBase#getFilterGroup(org.topcased.gpm.domain.fields.FieldsContainer)
     */
    @SuppressWarnings("unchecked")
    public List<FilterGroup> getFilterGroup(final FieldsContainer pContainer) {
        final Query lQuery =
                getSession(false).createQuery(
                        "from org.topcased.gpm.domain.facilities.FilterGroup as filterGroup where filterGroup.container.id = :containerId ORDER BY filterGroup.name");

        lQuery.setParameter("containerId", pContainer.getId());

        return (List<FilterGroup>) (lQuery.list());
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.facilities.FilterGroupDaoBase#getFilterGroup(org.topcased.gpm.domain.fields.FieldsContainer,
     *      boolean)
     */
    @SuppressWarnings("unchecked")
    public List<FilterGroup> getFilterGroup(final FieldsContainer pContainer,
            final boolean pParameterized) {
        final Criteria lCriteria =
                getSession(false).createCriteria(FilterGroup.class);

        if (null != pContainer) {
            lCriteria.add(Restrictions.eq("container", pContainer));
        }
        lCriteria.add(Restrictions.eq("parameterized", pParameterized));

        return (List<FilterGroup>) lCriteria.list();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.facilities.FilterGroupDaoBase#getAllFilterGroups(boolean)
     */
    @SuppressWarnings("unchecked")
    public List<FilterGroup> getAllFilterGroups(final boolean pParameterized) {
        final Query lQuery =
                getSession(false).createQuery(
                        "from org.topcased.gpm.domain.facilities.FilterGroup as filterGroup where filterGroup.parameterized = :parameterized ORDER BY filterGroup.name");
        lQuery.setParameter("parameterized", pParameterized);

        return (List<FilterGroup>) (lQuery.list());
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.facilities.FilterGroupDaoBase#getFilterGroup(org.topcased.gpm.domain.fields.FieldsContainer,
     *      java.lang.String, boolean)
     */
    @SuppressWarnings("unchecked")
    public List<FilterGroup> getFilterGroup(final FieldsContainer pContainer,
            final String pFilterType, final boolean pParameterized) {
        final Criteria lCriteria =
                getSession(false).createCriteria(FilterGroup.class);

        if (null != pContainer) {
            lCriteria.add(Restrictions.eq("container", pContainer));
        }
        if (!StringUtils.isBlank(pFilterType)) {
            lCriteria.add(Restrictions.eq("filterType", pFilterType));
        }
        lCriteria.add(Restrictions.eq("parameterized", pParameterized));

        return (List<FilterGroup>) lCriteria.list();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.facilities.FilterGroupDaoBase#getFilterGroup(org.topcased.gpm.domain.fields.FieldsContainer,
     *      java.lang.String)
     */
    @SuppressWarnings("unchecked")
    public List<FilterGroup> getFilterGroup(final FieldsContainer pContainer,
            final String pFilterType) {
        final Criteria lCriteria =
                getSession(false).createCriteria(FilterGroup.class);

        if (null != pContainer) {
            lCriteria.add(Restrictions.eq("container", pContainer));
        }
        if (!StringUtils.isBlank(pFilterType)) {
            lCriteria.add(Restrictions.eq("filterType", pFilterType));
        }
        return (List<FilterGroup>) lCriteria.list();
    }

    /**
     * @see org.topcased.gpm.domain.facilities.FilterGroup#deleteGroups(org.topcased.gpm.domain.fields.FieldsContainer)
     */
    @SuppressWarnings("rawtypes")
    public java.lang.Boolean deleteGroups(
            final org.topcased.gpm.domain.fields.FieldsContainer pContainer) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.facilities.FilterGroup as filterGroup where filterGroup.container.id = :containerId";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("containerId", pContainer.getId());
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
     * @see org.topcased.gpm.domain.facilities.FilterGroup#getGroups(org.topcased.gpm.domain.fields.Field)
     */
    @SuppressWarnings("rawtypes")
    public java.util.List getGroups(
            final org.topcased.gpm.domain.fields.Field pField) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.facilities.FilterGroup as filterGroup where filterGroup.field.id = :fieldId";
            org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("fieldId", pField.getId());
            java.util.List lResults = lQueryObject.list();
            return lResults;
        }
        catch (org.hibernate.HibernateException ex) {
            throw super.convertHibernateAccessException(ex);
        }
    }
}