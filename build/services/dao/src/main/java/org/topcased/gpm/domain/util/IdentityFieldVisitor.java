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

import org.topcased.gpm.domain.fields.Field;

/**
 * @author llatil
 */
public final class IdentityFieldVisitor implements Visitor {

    private Field actualField;

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.util.Visitor#visit(java.lang.Object)
     */
    public void visit(Object pObj) {
        actualField = (Field) pObj;
    }

    /**
     * Get the visited field object. This method returns the actual visited
     * class (and not the hibernate proxy)
     * 
     * @return Actual object.
     */
    public Field get() {
        return actualField;
    }

    /**
     * Utility method used to get the 'actual' class of a given field. This
     * method uses a IdentityFieldVisitor instance to visit the field.
     * 
     * @param pObj
     *            Field to visit (can be null).
     * @return Actual field object.
     */
    public static Field getIdentity(Field pObj) {
        if (null == pObj) {
            return null;
        }
        IdentityFieldVisitor lVisitor = new IdentityFieldVisitor();

        pObj.accept(lVisitor);
        return lVisitor.get();
    }
}
