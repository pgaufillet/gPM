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

import java.util.List;

import org.hibernate.Query;

/**
 *AttachedFieldValueDaoImpl
 * 
 *@author llatil
 */
public class AttachedFieldValueDaoImpl
        extends
        org.topcased.gpm.domain.AbstractDao<org.topcased.gpm.domain.fields.AttachedFieldValue, java.lang.String>
        implements org.topcased.gpm.domain.fields.AttachedFieldValueDao {
    /**
     * Constructor
     */
    public AttachedFieldValueDaoImpl() {
        super(org.topcased.gpm.domain.fields.AttachedFieldValue.class);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.fields.AttachedFieldValueDaoBase#getAttachedFieldValues(org.topcased.gpm.domain.fields.ValuesContainer)
     */
    @SuppressWarnings("unchecked")
    public List<String> getAttachedFieldValuesId(
            final ValuesContainer pContainer) {
        final Query lQuery =
                getSession(false).createQuery(
                        "select fileValue.id from org.topcased.gpm.domain.fields.AttachedFieldValue as fileValue "
                                + "where fileValue.container.id = :containerId");
        lQuery.setParameter("containerId", pContainer.getId());

        return lQuery.list();
    }
}
