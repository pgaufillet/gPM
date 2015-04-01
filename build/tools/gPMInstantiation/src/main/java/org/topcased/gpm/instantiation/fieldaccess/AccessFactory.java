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

import org.topcased.gpm.business.fields.LineFieldData;
import org.topcased.gpm.business.link.service.LinkData;
import org.topcased.gpm.business.product.service.ProductData;
import org.topcased.gpm.business.revision.RevisionData;
import org.topcased.gpm.business.sheet.service.SheetData;

/**
 * Construct a FieldAccess from a ProductData or a SheetData. FieldAccess is
 * used to simplify the use of field values objects.
 * 
 * @author nbousque
 */
public class AccessFactory {

    private static AccessFactory staticInstance = null;

    private AccessFactory() {

    }

    /**
     * Get the singleton instance of the factory.
     * 
     * @return Factory singleton
     */
    public static AccessFactory getInstance() {
        if (null == staticInstance) {
            staticInstance = new AccessFactory();
        }
        return staticInstance;
    }

    /**
     * Create a new FieldAccess using a SheetData.
     * 
     * @param pData
     * @return
     */
    public FieldCompositeAccess createFieldAccess(SheetData pData) {
        return new org.topcased.gpm.instantiation.fieldaccess.impl.SheetAccess(
                pData);
    }

    /**
     * Create a new FieldAccess using a RevisionData
     * 
     * @param pRevisionData
     *            The revision data
     * @return FieldCompositeAccess
     */
    public FieldCompositeAccess createFieldAccess(RevisionData pRevisionData) {
        return new org.topcased.gpm.instantiation.fieldaccess.impl.RevisionAccess(
                pRevisionData);
    }

    /**
     * Create a new FieldAccess for the sheet reference.
     * 
     * @param pData
     * @return
     */
    public FieldCompositeAccess createReferenceFieldAccess(SheetData pData) {
        LineFieldData lRefData = pData.getReference();
        return new org.topcased.gpm.instantiation.fieldaccess.impl.LineFieldAccess(
                lRefData);
    }

    /**
     * Create a new FieldAccess using a ProductData.
     * 
     * @param pData
     * @return
     */
    public FieldCompositeAccess createFieldAccess(ProductData pData) {
        return new org.topcased.gpm.instantiation.fieldaccess.impl.ProductAccess(
                pData);
    }

    /**
     * Create a new FieldAccess using a SheetLinkData.
     * 
     * @param pData
     * @return
     */
    public FieldCompositeAccess createFieldAccess(LinkData pData) {
        return new org.topcased.gpm.instantiation.fieldaccess.impl.SheetLinkAccess(
                pData);
    }
}
