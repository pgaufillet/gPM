/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.domain.util;

import java.io.Serializable;
import java.util.UUID;

import org.hibernate.HibernateException;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.topcased.gpm.domain.PersistentObject;

/**
 * Generate an UID
 * 
 * @author tpanuel
 */
public final class UuidGenerator implements IdentifierGenerator {
    /**
     * {@inheritDoc}
     * 
     * @see org.hibernate.id.IdentifierGenerator#generate(org.hibernate.engine.SessionImplementor,
     *      java.lang.Object)
     */
    public Serializable generate(final SessionImplementor pArg0,
            final Object pArg1) throws HibernateException {
        final PersistentObject lPersistantObject = (PersistentObject) pArg1;

        if (lPersistantObject.getId() == null) {
            return UUID.randomUUID().toString();
        }
        else {
            return lPersistantObject.getId();
        }
    }
}