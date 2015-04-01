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

import org.hibernate.Query;
import org.topcased.gpm.domain.fields.FieldsContainer;

/**
 * @author llatil
 */
public class LinkSheetSummaryGroupDaoImpl
        extends
        org.topcased.gpm.domain.AbstractDao<org.topcased.gpm.domain.facilities.LinkSheetSummaryGroup, java.lang.String>
        implements org.topcased.gpm.domain.facilities.LinkSheetSummaryGroupDao {
    /**
     * Constructor
     */
    public LinkSheetSummaryGroupDaoImpl() {
        super(org.topcased.gpm.domain.facilities.LinkSheetSummaryGroup.class);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.facilities.LinkSheetSummaryGroupDaoBase#getLinkSheetSummaryGroup(org.topcased.gpm.domain.fields.FieldsContainer)
     */
    public LinkSheetSummaryGroup getLinkSheetSummaryGroup(
            final FieldsContainer pContainer) {
        Query lQuery =
                getSession(false).createQuery(
                        "from org.topcased.gpm.domain.facilities.LinkSheetSummaryGroup as group where group.container.id = :containerId");

        lQuery.setParameter("containerId", pContainer.getId());

        return (LinkSheetSummaryGroup) lQuery.uniqueResult();
    }

    /**
     * @see org.topcased.gpm.domain.facilities.LinkSheetSummaryGroup#deleteGroups(org.topcased.gpm.domain.fields.FieldsContainer)
     */
    @SuppressWarnings("rawtypes")
    public java.lang.Boolean deleteGroups(
            final org.topcased.gpm.domain.fields.FieldsContainer pContainer) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.facilities.LinkSheetSummaryGroup as linkSheetSummaryGroup where linkSheetSummaryGroup.container.id = :containerId";
            org.hibernate.Query lQueryObject =
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
     * @see org.topcased.gpm.domain.facilities.LinkSheetSummaryGroup#getGroups(org.topcased.gpm.domain.fields.Field)
     */
    @SuppressWarnings("rawtypes")
    public java.util.List getGroups(
            final org.topcased.gpm.domain.fields.Field pField) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.facilities.LinkSheetSummaryGroup as linkSheetSummaryGroup where linkSheetSummaryGroup.field.id = :fieldId";
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