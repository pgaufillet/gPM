/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available 
 * under the terms of the Lesser Gnu Public License (LGPL)which 
 * accompanies this distribution, and is available 
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin)
 ******************************************************************/
// license-header java merge-point
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 */
package org.topcased.gpm.domain.extensions;

/**
 * @see org.topcased.gpm.domain.extensions.Action
 * @author nbousquet
 */
public class ActionDaoImpl
        extends
        org.topcased.gpm.domain.AbstractDao<org.topcased.gpm.domain.extensions.Action, java.lang.String>
        implements org.topcased.gpm.domain.extensions.ActionDao {
    /**
     * Constructor
     */
    public ActionDaoImpl() {
        super(org.topcased.gpm.domain.extensions.Action.class);
    }

    /**
     * @see org.topcased.gpm.domain.extensions.Action#getCommand(java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    public org.topcased.gpm.domain.extensions.Command getCommand(
            final java.lang.String pName) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.extensions.Action as action where action.name = :name";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("name", pName);
            java.util.List lResults = lQueryObject.list();
            org.topcased.gpm.domain.extensions.Command lResult = null;
            if (lResults != null) {
                if (lResults.size() > 1) {
                    throw new org.springframework.dao.InvalidDataAccessResourceUsageException(
                            "More than one instance of 'org.topcased.gpm.domain.extensions.Command"
                                    + "' was found when executing query --> '"
                                    + lQueryString + "'");
                }
                else if (lResults.size() == 1) {
                    lResult =
                            (org.topcased.gpm.domain.extensions.Command) lResults.iterator().next();
                }
            }
            return lResult;
        }
        catch (org.hibernate.HibernateException ex) {
            throw super.convertHibernateAccessException(ex);
        }
    }

    /**
     * @see org.topcased.gpm.domain.extensions.Action#getExtensionPointsWithCommand(java.lang.String)
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public java.util.List<ExtensionPoint> getExtensionPointsWithCommand(
            final java.lang.String pCommandName) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.extensions.Action as action where action.commandName = :commandName";
            final org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("commandName", pCommandName);
            java.util.List lResults = lQueryObject.list();
            java.util.List<ExtensionPoint> lResult = null;
            if (lResults != null) {
                if (lResults.size() > 1) {
                    throw new org.springframework.dao.InvalidDataAccessResourceUsageException(
                            "More than one instance of 'java.util.List<ExtensionPoint>"
                                    + "' was found when executing query --> '"
                                    + lQueryString + "'");
                }
                else if (lResults.size() == 1) {
                    lResult =
                            (java.util.List<ExtensionPoint>) lResults.iterator().next();
                }
            }
            return lResult;
        }
        catch (org.hibernate.HibernateException ex) {
            throw super.convertHibernateAccessException(ex);
        }
    }

}