/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin), Sophian Idjellidaine (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.instantiation.fieldaccess;

/**
 * FieldAccess with FieldCompositeAccess are the main interfaces of the
 * FieldAccess API. All FieldAccess classes and interfaces extends or implement
 * FieldAccess. The getField method is intended for research use. It can find
 * another FieldAccess using it's name.
 * 
 * @author nbousque
 */
public interface FieldAccess {
    /**
     * Return the actual type of the piece of data encapsulated by the
     * FieldAccess (like Sheet or CHOICE).
     * 
     * @return the type of the encapsulated data.
     */
    String getType();

    /**
     * getName is used by getField method to research piece of data encapsulated
     * by FieldAccess. A name is supposed to be unique in a Product or Sheet.
     * But, a multipleField or MultiLineField can be considered as a new Field
     * namespace and thus this limitation don't apply. Several FieldElement in a
     * MultipleField can share the same name.
     * 
     * @return name of the encapsulated data.
     */
    String getName();

    /**
     * When several graphical representations are available for a specific type
     * of Field, this method return a preferred graphical representation.
     * 
     * @return the preferred graphical representation.
     */
    String getDisplayHint();

    /**
     * This method is used for research of Fields in FieldAccess elements.
     * Research of field allows for easier access of Fields within a sheet or a
     * product and allow to use the same API in all case. Each FieldAccess
     * implementation is responsible to implement it's research algorithm for
     * it's elements. To retrieve the name of a subField directly you should be
     * sure that it's name is really unique in your model and is not under a
     * multipleLine Field. You can always find a specific field in two time,
     * first researching for the multipleLine or multipleField object, and then
     * the subField.
     * 
     * @param pName
     * @return
     */
    public FieldAccess getField(String pName);
}
