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
package org.topcased.gpm.domain.accesscontrol;

import org.hibernate.Query;

/**
 * @see org.topcased.gpm.domain.accesscontrol.FilterAccessConstraint
 * @author mkargbo
 */
public class FilterAccessConstraintDaoImpl
        extends
        org.topcased.gpm.domain.AbstractDao<org.topcased.gpm.domain.accesscontrol.FilterAccessConstraint, java.lang.String>
        implements
        org.topcased.gpm.domain.accesscontrol.FilterAccessConstraintDao {

    private final static String CONSTRAINT_EXISTS =
            "SELECT constraint.id FROM "
                    + FilterAccessConstraint.class.getName() + " constraint "
                    + "WHERE constraint.name = :name ";

    private final static String CONSTRAINT_ATTRIBUTE_NAME = "name";

    /**
     * Constructor
     */
    public FilterAccessConstraintDaoImpl() {
        super(
                org.topcased.gpm.domain.accesscontrol.FilterAccessConstraint.class);
    }

    @Override
    public Boolean isConstraintExists(String pName) {
        final Query lQuery = createQuery(CONSTRAINT_EXISTS);

        lQuery.setParameter(CONSTRAINT_ATTRIBUTE_NAME, pName);

        return hasResult(lQuery);
    }
}