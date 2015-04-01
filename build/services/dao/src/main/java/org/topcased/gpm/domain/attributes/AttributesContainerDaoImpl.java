/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.domain.attributes;

import java.util.List;

import org.hibernate.Query;

/**
 * AttributesContainer DAO implementation
 * 
 * @author llatil
 */
public class AttributesContainerDaoImpl
        extends
        org.topcased.gpm.domain.AbstractDao<org.topcased.gpm.domain.attributes.AttributesContainer, java.lang.String>
        implements org.topcased.gpm.domain.attributes.AttributesContainerDao {
    /**
     * Constructor
     */
    public AttributesContainerDaoImpl() {
        super(org.topcased.gpm.domain.attributes.AttributesContainer.class);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.attributes.AttributesContainerDaoBase#getAllAttrNames(org.topcased.gpm.domain.attributes.AttributesContainer)
     */
    @SuppressWarnings("rawtypes")
    public List getAllAttrNames(final AttributesContainer pAttrContainer) {
        final String lQueryText = "select this.name order by name";
        final Query lQuery =
                getSession(false).createFilter(pAttrContainer.getAttributes(),
                        lQueryText);
        return lQuery.list();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.attributes.AttributesContainerDaoBase#getAttribute(org.topcased.gpm.domain.attributes.AttributesContainer,
     *      java.lang.String)
     */
    public Attribute getAttribute(final AttributesContainer pAttrContainer,
            final String pAttrName) {
        final String lQueryText = "where this.name = :attributeName";
        final Query lQuery =
                getSession(false).createFilter(pAttrContainer.getAttributes(),
                        lQueryText);
        lQuery.setParameter("attributeName", pAttrName);

        return (Attribute) lQuery.uniqueResult();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.attributes.AttributesContainerDaoBase#getAttributes(org.topcased.gpm.domain.attributes.AttributesContainer,
     *      java.lang.String[])
     */
    @SuppressWarnings("unchecked")
    public List<Attribute> getAttributes(
            final AttributesContainer pAttrContainer, final String[] pAttrNames) {

        final String lQueryText = "where this.name in (:attributeNames)";
        final Query lQuery =
                getSession(false).createFilter(pAttrContainer.getAttributes(),
                        lQueryText);
        lQuery.setParameterList("attributeNames", pAttrNames);

        return lQuery.list();
    }
}