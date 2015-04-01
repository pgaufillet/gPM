/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 *
 ******************************************************************/
package org.topcased.gpm.business.values.field.multivalued;

import org.topcased.gpm.business.values.field.BusinessField;

/**
 * Interface used to access on a multi valued field.
 * 
 * @author tpanuel
 * @param <A>
 *            The type of access for multi valued field's elements.
 */
public interface BusinessMultivaluedField<A extends BusinessField> extends
        BusinessField, Iterable<A> {
    /**
     * Get an access to an element of the multi valued field.
     * 
     * @param pIndex
     *            The index of the element.
     * @return The access.
     */
    public A get(final int pIndex);

    /**
     * Get an access to the first element of the multi valued field. A multi
     * valued has always at less one element. This element can be used to know
     * the type of the sub elements.
     * 
     * @return The first element.
     */
    public A getFirst();

    /**
     * Add a line at the end on the multi valued field.
     * 
     * @return Access on the new line.
     */
    public A addLine();

    /**
     * Remove the last line of the multi valued field.
     * 
     * @return Access on the removed line.
     */
    public A removeLine();

    /**
     * Remove the pIndex line of the multi valued field.
     * 
     * @param pIndex
     *            Index of the element
     * @return Access on the removed line.
     */
    public A removeLine(final int pIndex);

    /**
     * Get the number of elements of the multi valued field.
     * 
     * @return The number of elements of the multi valued field.
     */
    public int size();
}