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

import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.topcased.gpm.domain.fields.Field;
import org.topcased.gpm.domain.fields.FieldsContainer;

/**
 * @author llatil
 */
public class FieldGroupDaoImpl
        extends
        org.topcased.gpm.domain.AbstractDao<org.topcased.gpm.domain.facilities.FieldGroup, java.lang.String>
        implements org.topcased.gpm.domain.facilities.FieldGroupDao {

    /**
     * Constructor
     */
    public FieldGroupDaoImpl() {
        super(org.topcased.gpm.domain.facilities.FieldGroup.class);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.facilities.FieldGroupDaoBase#deleteGroups(org.topcased.gpm.domain.fields.FieldsContainer)
     */
    @SuppressWarnings("unchecked")
    public Boolean deleteGroups(final FieldsContainer pContainer) {
        final Session lSession = getSession(false);

        final Query lQuery =
                lSession.createQuery("FROM org.topcased.gpm.domain.facilities.FieldGroup as group "
                        + "WHERE group.container.id = :containerId");
        lQuery.setParameter("containerId", pContainer.getId());

        Iterator<FieldGroup> lIter = lQuery.iterate();

        while (lIter.hasNext()) {
            FieldGroup lGroup = lIter.next();
            lGroup.getFields().clear();
            lSession.delete(lGroup);
        }
        return Boolean.TRUE;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.facilities.FieldGroupDaoBase#getGroups(org.topcased.gpm.domain.fields.Field)
     */
    @SuppressWarnings("rawtypes")
    public List getGroups(final Field pField) {
        final Session lSession = getSession(false);

        final Query lQuery =
                lSession.createQuery("from org.topcased.gpm.domain.facilities.FieldGroup as grp "
                        + "where :field in elements(grp.fields)");
        lQuery.setEntity("field", pField);

        return lQuery.list();
    }
}