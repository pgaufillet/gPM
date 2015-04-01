/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.tests;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.topcased.gpm.util.session.GpmSessionFactory;

/**
 * Helper functions to be used by testing frameworks.
 * 
 * @author llatil
 */
public class TestUtils {
    /**
     * Close the current Hibernate session
     */
    public static void closeHibernateSession() {
        final Session lHibernateSession = getHibernateSession();

        if (lHibernateSession != null && lHibernateSession.isOpen()) {
            lHibernateSession.close();
        }
    }

    /**
     * Drop the current database schema.
     */
    public static void dropDatabaseSchema() {
        final GpmSessionFactory lSessionFactory =
                GpmSessionFactory.getInstance();

        if (lSessionFactory != null) {
            lSessionFactory.dropDatabaseSchema();
        }
    }

    /**
     * Recreate the database schema (drop the schema, and create it again).
     */
    public static void recreateDatabaseSchema() {
        final GpmSessionFactory lSessionFactory =
                GpmSessionFactory.getInstance();

        if (lSessionFactory != null) {
            lSessionFactory.createDatabaseSchema();
        }
    }

    /**
     * Get the current Hibernate session.
     * 
     * @return Current Hibernate session.
     */
    public static org.hibernate.Session getHibernateSession() {
        final SessionFactory lHibernateSessionFactory =
                GpmSessionFactory.getHibernateSessionFactory();

        try {
            if (lHibernateSessionFactory != null) {
                return lHibernateSessionFactory.getCurrentSession();
            }
        }
        catch (HibernateException e) {
            // When no current session exist, Hibernate like to send an
            // exception..
            // Well, let's ignore it, and simply assume no session exist yet.
        }

        return null;
    }
}
