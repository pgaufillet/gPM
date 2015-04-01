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

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.topcased.gpm.business.sheet.service.FieldGroupData;

/**
 * This class is a wrapper around the SheetData structure (returned from the
 * sheet service), and is used to simplify the access to the sheet data (groups,
 * fields, values).
 * 
 * @author llatil
 */
public class SheetDataAccess extends FieldContainerAccess implements SheetData {

    /** Reference to the sheet data */
    private org.topcased.gpm.business.sheet.service.SheetData sheetData;

    /** Group map */
    private Map<String, GroupData> groupMap;

    /**
     * Construct a new sheet data access object
     * 
     * @param pSheetData
     *            Reference to the sheet data structure.
     */
    public SheetDataAccess(
            final org.topcased.gpm.business.sheet.service.SheetData pSheetData) {
        sheetData = pSheetData;
        groupMap =
                new LinkedHashMap<String, GroupData>(
                        pSheetData.getFieldGroupDatas().length);

        for (int i = 0; i < pSheetData.getFieldGroupDatas().length; ++i) {
            FieldGroupData lGroupData = pSheetData.getFieldGroupDatas()[i];

            groupMap.put(lGroupData.getLabelKey(), new GroupDataAccess(
                    lGroupData));
            initFieldMap(lGroupData.getMultipleLineFieldDatas());
        }
    }

    /**
     * Get the list of group names
     * 
     * @return List of group names
     */
    public Collection<String> getDisplayGroupNames() {
        Collection<String> lNames = groupMap.keySet();
        List<String> lNamesList = new ArrayList<String>(lNames.size());

        lNamesList.addAll(lNames);
        return lNamesList;
    }

    /**
     * Get a group by name
     * 
     * @param pGroupName
     *            Name of the group to get
     * @return GroupData object to access the fields contained in the group
     */
    public GroupData getGroup(final String pGroupName) {
        return groupMap.get(pGroupName);
    }

    /**
     * Get the list of all groups
     * 
     * @return Groups list
     */
    public Collection<GroupData> getGroups() {
        return groupMap.values();
    }

    /**
     * Get the sheet reference
     * 
     * @return Sheet reference access
     */
    public MultipleFieldData getReference() {
        return new MultipleFieldDataAccess(sheetData.getReference());
    }

    /**
     * Get the sheet identifier This is the unique identifier of this sheet in
     * the database.
     * 
     * @return Sheet identifier.
     */
    public String getId() {
        return sheetData.getId();
    }

    /**
     * Get the name of the type of this sheet.
     * 
     * @return Sheet type name.
     */
    public String getSheetTypeName() {
        return sheetData.getSheetTypeName();
    }

    /**
     * Get the product name of the sheet
     * 
     * @return Product name
     */
    public String getProductName() {
        return sheetData.getProductName();
    }

    /**
     * Get the current version of the sheet.
     * 
     * @return Sheet type name.
     */
    public long getVersion() {
        return sheetData.getVersion();
    }
}
