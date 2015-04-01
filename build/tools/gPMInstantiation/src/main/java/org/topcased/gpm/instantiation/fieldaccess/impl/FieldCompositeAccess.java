/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available 
 * under the terms of the Lesser Gnu Public License (LGPL)which 
 * accompanies this distribution, and is available 
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin), Sophian Idjellidaine (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.instantiation.fieldaccess.impl;

import org.topcased.gpm.instantiation.fieldaccess.FieldAccess;

/**
 * @author sie
 */
public abstract class FieldCompositeAccess implements
        org.topcased.gpm.instantiation.fieldaccess.FieldCompositeAccess {

    /**
     * Retrieve the first field (possibly multipleLine) with the specified name.
     * Please note that multipleLine support is coded into MultipleLineAccess
     * not here.
     * 
     * @param pName
     *            the name of the field to retrieve.
     * @return All the lines for the specified field.
     */
    public FieldAccess getField(String pName) {

        // First search into the FieldComposite element
        for (FieldAccess lField : this) {
            // if we have found the Field with the specified name, just
            // return it.
            if (pName.equals(lField.getName())) {
                return lField;
            }

            // If the Field is a FieldComposite, then search into it.
            FieldAccess lResultaux = lField.getField(pName);

            // We stop the research only if we already have a successfull
            // result.
            if (null != lResultaux) {
                return lResultaux;
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.instantiation.fieldaccess.FieldCompositeAccess#size()
     */
    public abstract long size();
}
