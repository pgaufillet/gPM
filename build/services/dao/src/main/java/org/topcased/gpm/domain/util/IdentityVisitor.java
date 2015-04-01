/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.domain.util;

import org.topcased.gpm.domain.attributes.AttributesContainer;

/**
 * @author llatil
 */
public final class IdentityVisitor<T> implements Visitor {

    private AttributesContainer actualContainer;

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.util.Visitor#visit(java.lang.Object)
     */
    public void visit(Object pObj) {
        actualContainer = (AttributesContainer) pObj;
    }

    /**
     * Get the visited field object. This method returns the actual visited
     * class (and not the hibernate proxy)
     * 
     * @return Actual object.
     */
    @SuppressWarnings("unchecked")
    public T get() {
        return (T) actualContainer;
    }

    /**
     * Utility method used to get the 'actual' class of a given field. This
     * method uses a IdentityVisitor instance to visit the field.
     * 
     * @param pObj
     *            Field to visit (can be null).
     * @return Actual field object.
     */
    public static <T> T getIdentity(T pObj) {
        if (!(pObj instanceof AttributesContainer)) {
            return (T) pObj;
        }

        IdentityVisitor<T> lVisitor = new IdentityVisitor<T>();

        ((AttributesContainer) pObj).accept(lVisitor);
        return lVisitor.get();
    }
}
