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
 * Maps to a MultipleLine field. A MultipleLine field share it's name with it's
 * subFields and support adding or removing Lines.
 * 
 * @author nbousque
 */
public interface MultipleLineAccess extends FieldCompositeAccess {

    /**
     * Add a new Line to the MultipleLine. The first line is used has a model.
     * Please note that adding or removing a line will make the result of
     * iterator unpredicted.
     * 
     * @return
     */
    public FieldAccess addLine();

    /**
     * Remove the specified line. Please note that adding or removing a line
     * will make the result of iterator unpredicted.
     * 
     * @param pIndex
     *            the line to be removed.
     */
    public void removeLine(int pIndex);

}