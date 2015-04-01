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
 * Maps SheetLinkData.
 * 
 * @author sidjelli
 */
public interface SheetLinkAccess extends FieldCompositeAccess {

    /**
     * Get the identifier of the sheet link in the DB.
     * 
     * @return the Id of the sheet link.
     */
    public String getId();

}