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
 * Many FieldAccess are in fact Container of other FieldAccess. All of theses
 * container should implements this interface to provide a common way to
 * research or look into a FieldAccess. A FieldCompositeAccess access is of
 * course a FieldAccess but with Iterable support (read only) and the ability to
 * know the number of elements.
 * 
 * @author nbousque
 */
public interface FieldCompositeAccess extends FieldAccess,
        Iterable<FieldAccess> {

    /**
     * @return number of elements into the field container.
     */
    public long size();

}
