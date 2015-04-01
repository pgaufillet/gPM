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

import org.hibernate.Query;
import org.topcased.gpm.domain.fields.FieldsContainer;

/**
 * Implementation of DisplayGroupDao
 * 
 * @author tszadel
 */
public class DisplayGroupDaoImpl
        extends
        org.topcased.gpm.domain.AbstractDao<org.topcased.gpm.domain.facilities.DisplayGroup, java.lang.String>
        implements org.topcased.gpm.domain.facilities.DisplayGroupDao {
    /**
     * Constructor
     */
    public DisplayGroupDaoImpl() {
        super(org.topcased.gpm.domain.facilities.DisplayGroup.class);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.facilities.DisplayGroupDaoBase#getDisplayGroup(org.topcased.gpm.domain.fields.FieldsContainer)
     */
    @SuppressWarnings("unchecked")
    public List<DisplayGroup> getDisplayGroup(final FieldsContainer pContainer) {
        final Query lQuery =
                getSession(false).createQuery(
                        "from org.topcased.gpm.domain.facilities.DisplayGroup as displayGroup where displayGroup.container.id = :containerId ORDER BY displayGroup.displayOrder");

        lQuery.setParameter("containerId", pContainer.getId());

        return (List<DisplayGroup>) (lQuery.list());
    }

    /**
     * @see org.topcased.gpm.domain.facilities.DisplayGroup#getDisplayGroup(org.topcased.gpm.domain.fields.FieldsContainer,
     *      java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    public org.topcased.gpm.domain.facilities.DisplayGroup getDisplayGroup(
            final org.topcased.gpm.domain.fields.FieldsContainer pContainer,
            final java.lang.String pName) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.facilities.DisplayGroup as displayGroup where displayGroup.container.id = :containerId and displayGroup.name = :name";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("containerId", pContainer.getId());
            lQueryObject.setParameter("name", pName);
            java.util.List lResults = lQueryObject.list();
            org.topcased.gpm.domain.facilities.DisplayGroup lResult = null;
            if (lResults != null) {
                if (lResults.size() > 1) {
                    throw new org.springframework.dao.InvalidDataAccessResourceUsageException(
                            "More than one instance of 'org.topcased.gpm.domain.facilities.DisplayGroup"
                                    + "' was found when executing query --> '"
                                    + lQueryString + "'");
                }
                else if (lResults.size() == 1) {
                    lResult =
                            (org.topcased.gpm.domain.facilities.DisplayGroup) lResults.iterator().next();
                }
            }
            return lResult;
        }
        catch (org.hibernate.HibernateException ex) {
            throw super.convertHibernateAccessException(ex);
        }
    }

    /**
     * @see org.topcased.gpm.domain.facilities.DisplayGroup#deleteGroups(org.topcased.gpm.domain.fields.FieldsContainer)
     */
    @SuppressWarnings("rawtypes")
    public java.lang.Boolean deleteGroups(
            final org.topcased.gpm.domain.fields.FieldsContainer pContainer) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.facilities.DisplayGroup as displayGroup where displayGroup.container.id = :containerId";
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
     * @see org.topcased.gpm.domain.facilities.DisplayGroup#getGroups(org.topcased.gpm.domain.fields.Field)
     */
    @SuppressWarnings("rawtypes")
    public java.util.List getGroups(
            final org.topcased.gpm.domain.fields.Field pField) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.facilities.DisplayGroup as displayGroup where displayGroup.field.id = :fieldId";
            final org.hibernate.Query lQueryObject =
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
