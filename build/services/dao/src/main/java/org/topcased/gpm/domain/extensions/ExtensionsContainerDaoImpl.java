/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.domain.extensions;

import java.util.List;

import org.hibernate.Query;

/**
 * @author llatil
 */
public class ExtensionsContainerDaoImpl
        extends
        org.topcased.gpm.domain.AbstractDao<org.topcased.gpm.domain.extensions.ExtensionsContainer, java.lang.String>
        implements org.topcased.gpm.domain.extensions.ExtensionsContainerDao {

    /**
     * Construtor
     */
    public ExtensionsContainerDaoImpl() {
        super(org.topcased.gpm.domain.extensions.ExtensionsContainer.class);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.extensions.ExtensionsContainerDaoBase#getExtensionPoint(org.topcased.gpm.domain.extensions.ExtensionsContainer,
     *      java.lang.String)
     */
    public ExtensionPoint getExtensionPoint(
            final ExtensionsContainer pExtContainer, final String pExtensionName) {
        if (null == pExtContainer) {
            return null;
        }

        final Query lQuery =
                getSession(false).createFilter(
                        pExtContainer.getExtensionPoints(),
                        "where this.name = :extensionName");
        lQuery.setParameter("extensionName", pExtensionName);

        return (ExtensionPoint) lQuery.uniqueResult();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.extensions.ExtensionsContainerDaoBase#getExtensionPoint(org.topcased.gpm.domain.extensions.ExtensionsContainer,
     *      java.lang.String)
     */
    public ExtensionPoint getExtensionPoint(final String pExtContainerId,
            final String pExtensionName) {
        final Query lQuery =
                getSession(false).createQuery(
                        "select extPoint from "
                                + ExtensionsContainer.class.getName()
                                + " as extContainer, "
                                + ExtensionPoint.class.getName()
                                + " as extPoint where "
                                + "extContainer.id=:containerId and extPoint.name=:extName "
                                + "and extPoint in elements(extContainer.extensionPoints)");

        lQuery.setParameter("containerId", pExtContainerId);
        lQuery.setParameter("extName", pExtensionName);

        return (ExtensionPoint) lQuery.uniqueResult();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.extensions.ExtensionsContainerDaoBase#getAllExtensionPoints(java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    public List getAllExtensionPoints(final String pExtContainerId) {
        final Query lQuery =
                getSession(false).createQuery(
                        "select extPoint from "
                                + ExtensionsContainer.class.getName()
                                + " as extContainer, "
                                + ExtensionPoint.class.getName()
                                + " as extPoint where "
                                + "extContainer.id=:containerId "
                                + "and extPoint in elements(extContainer.extensionPoints) order by extPoint.name");

        lQuery.setParameter("containerId", pExtContainerId);

        return lQuery.list();
    }

    /**
     * @see org.topcased.gpm.domain.extensions.ExtensionsContainer#getAllAttrNames(org.topcased.gpm.domain.attributes.AttributesContainer)
     */
    @SuppressWarnings("rawtypes")
    public java.util.List getAllAttrNames(
            final org.topcased.gpm.domain.attributes.AttributesContainer pAttrContainer) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.extensions.ExtensionsContainer as extensionsContainer where extensionsContainer.attrContainer.id = :attrContainerId";
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
     * @see org.topcased.gpm.domain.extensions.ExtensionsContainer#getAttributes(org.topcased.gpm.domain.attributes.AttributesContainer,
     *      java.lang.String[])
     */
    @SuppressWarnings("rawtypes")
    public java.util.List getAttributes(
            final org.topcased.gpm.domain.attributes.AttributesContainer pAttrContainer,
            final java.lang.String[] pAttrNames) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.extensions.ExtensionsContainer as extensionsContainer where extensionsContainer.attrContainer.id = :attrContainerId and extensionsContainer.attrNames = :attrNames";
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
     * @see org.topcased.gpm.domain.extensions.ExtensionsContainer#getAttribute(org.topcased.gpm.domain.attributes.AttributesContainer,
     *      java.lang.String)
     */
    @SuppressWarnings("rawtypes")
    public org.topcased.gpm.domain.attributes.Attribute getAttribute(
            final org.topcased.gpm.domain.attributes.AttributesContainer pAttrContainer,
            final java.lang.String pAttrName) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.extensions.ExtensionsContainer as extensionsContainer where extensionsContainer.attrContainer.id = :attrContainerId and extensionsContainer.attrName = :attrName";
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