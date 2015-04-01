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

import java.util.Collection;

/**
 * @author llatil
 */
public interface SheetData extends FieldDataContainer {

    /**
     * Get the list of group names
     * 
     * @return List of group names
     */
    Collection<String> getDisplayGroupNames();

    /**
     * Get a group
     * 
     * @param pGroupName
     *            Name of the group to get
     * @return Group
     */
    GroupData getGroup(String pGroupName);

    /**
     * Get the list of all groups
     * 
     * @return Groups list
     */
    Collection<GroupData> getGroups();

    /**
     * Get the version of this sheet.
     * 
     * @return Sheet version number
     */
    long getVersion();

    /**
     * Get the sheet identifier (from the database)
     * 
     * @return Sheet identifier
     */
    String getId();

    /**
     * Get the sheet type name
     * 
     * @return Sheet type name
     */
    String getSheetTypeName();

    /**
     * Get the product name of the sheet
     * 
     * @return Product name
     */
    String getProductName();

    /**
     * Get the sheet reference
     * 
     * @return Sheet reference
     */
    MultipleFieldData getReference();
}
