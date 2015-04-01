/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available 
 * under the terms of the Lesser Gnu Public License (LGPL)which 
 * accompanies this distribution, and is available 
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.sheet.service.sheetAccess;

/**
 * Interface implemented by a fields group.
 * 
 * @author llatil
 */
public interface GroupData extends FieldDataContainer {

    /**
     * Get the name of the group.
     * 
     * @return Group name
     */
    public String getName();
}
