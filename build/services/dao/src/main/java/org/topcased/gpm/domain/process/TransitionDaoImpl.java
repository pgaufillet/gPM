/***************************************************************
 * Copyright (c) 2012 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 ******************************************************************/
package org.topcased.gpm.domain.process;

public class TransitionDaoImpl
        extends
        org.topcased.gpm.domain.AbstractDao<org.topcased.gpm.domain.process.Transition, java.lang.String>
        implements org.topcased.gpm.domain.process.TransitionDao {

    public TransitionDaoImpl() {
        super(org.topcased.gpm.domain.process.Transition.class);
    }
}
