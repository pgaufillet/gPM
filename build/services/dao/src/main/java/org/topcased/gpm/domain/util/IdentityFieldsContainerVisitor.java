/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.domain.util;

import org.topcased.gpm.domain.fields.FieldsContainer;

/**
 * @author llatil
 */
public final class IdentityFieldsContainerVisitor implements Visitor {

    private FieldsContainer actualContainer;

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.util.Visitor#visit(java.lang.Object)
     */
    public void visit(Object pObj) {
        actualContainer = (FieldsContainer) pObj;
    }

    /**
     * Get the visited field object. This method returns the actual visited
     * class (and not the hibernate proxy)
     * 
     * @return Actual object.
     */
    public FieldsContainer get() {
        return actualContainer;
    }

    /**
     * Utility method used to get the 'actual' class of a given field. This
     * method uses a IdentityFieldVisitor instance to visit the field.
     * 
     * @param pObj
     *            Field to visit (can be null).
     * @return Actual field object.
     */
    public static FieldsContainer getIdentity(FieldsContainer pObj) {
        if (null == pObj) {
            return null;
        }
        IdentityFieldsContainerVisitor lVisitor =
                new IdentityFieldsContainerVisitor();

        pObj.accept(lVisitor);
        return lVisitor.get();
    }
}
