/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 ******************************************************************/
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 */
package org.topcased.gpm.domain.fields;

import java.util.List;

import org.hibernate.Query;

/**
 * @see org.topcased.gpm.domain.fields.PointerFieldAttributes
 * @author ahaugommard
 */
public class PointerFieldAttributesDaoImpl
        extends
        org.topcased.gpm.domain.AbstractDao<org.topcased.gpm.domain.fields.PointerFieldAttributes, java.lang.String>
        implements org.topcased.gpm.domain.fields.PointerFieldAttributesDao {
    /**
     * Constructor
     */
    public PointerFieldAttributesDaoImpl() {
        super(org.topcased.gpm.domain.fields.PointerFieldAttributes.class);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.fields.PointerFieldAttributesDaoBase#getPointerFields(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    @SuppressWarnings("unchecked")
    public List<Field> getPointerFields(final String pProcessName,
            final String pLinkTypeName, final String pContainerId) {
        final String lHqlQuery =
                "from "
                        + Field.class.getName()
                        + " field where "
                        + "field.container.businessProcess.name = :processName "
                        + "and field.pointerField = "
                        + Boolean.TRUE
                        + " and field.pointerFieldAttributes.referencedLinkType = :linkTypeName "
                        + "and field.container.id = :containerId ";
        final Query lQuery = getSession(false).createQuery(lHqlQuery);
        lQuery.setParameter("processName", pProcessName);
        lQuery.setParameter("linkTypeName", pLinkTypeName);
        lQuery.setParameter("containerId", pContainerId);
        return (List<Field>) lQuery.list();
    }

}