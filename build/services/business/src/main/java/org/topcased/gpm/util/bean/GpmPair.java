/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.util.bean;

/**
 * @author tpanuel
 */
public class GpmPair<F, S> {
    private F first;

    private S second;

    /**
     * 
     */
    public GpmPair() {
        this(null, null);
    }

    /**
     * @param pFirst
     *            The first element
     * @param pSecond
     *            The second element
     */
    public GpmPair(F pFirst, S pSecond) {
        first = pFirst;
        second = pSecond;
    }

    /**
     * Get the first element
     * 
     * @return The first element
     */
    public F getFirst() {
        return first;
    }

    /**
     * Set the first element
     * 
     * @param pFirst
     *            The new first element
     */
    public void setFirst(F pFirst) {
        first = pFirst;
    }

    /**
     * Get the second element
     * 
     * @return The second element
     */
    public S getSecond() {
        return second;
    }

    /**
     * Set the second element
     * 
     * @param pSecond
     *            The new second element
     */
    public void setSecond(S pSecond) {
        second = pSecond;
    }
}
