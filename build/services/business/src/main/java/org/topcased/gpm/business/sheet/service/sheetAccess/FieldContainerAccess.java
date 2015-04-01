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

import org.topcased.gpm.business.fields.MultipleLineFieldData;

/**
 * Base class for all fields containers.
 * 
 * @author llatil
 */
public abstract class FieldContainerAccess {
    /** Map field name <-> field data content */
    protected Map<String, org.topcased.gpm.business.fields.MultipleLineFieldData> fieldMap;

    /**
     * Initialize the fields map.
     * 
     * @param pFieldData
     *            Array of MultipleLineFieldData containing the fields data.
     */
    protected void initFieldMap(
            final org.topcased.gpm.business.fields.MultipleLineFieldData[] pFieldData) {
        if (null == pFieldData) {
            return;
        }
        if (null == fieldMap) {
            fieldMap =
                    new LinkedHashMap<String, org.topcased.gpm.business.fields.MultipleLineFieldData>(
                            pFieldData.length);
        }
        for (int i = 0; i < pFieldData.length; ++i) {
            fieldMap.put(pFieldData[i].getLabelKey(), pFieldData[i]);
        }
    }

    /**
     * @param pKey
     * @return
     */
    public FieldData getField(final String pKey) {
        org.topcased.gpm.business.fields.MultipleLineFieldData lMultiLineFieldData =
                fieldMap.get(pKey);

        return getFieldData(lMultiLineFieldData);
    }

    /**
     * @return
     */
    public List<FieldData> getFields() {
        List<FieldData> lResult = new ArrayList<FieldData>(fieldMap.size());

        for (org.topcased.gpm.business.fields.MultipleLineFieldData lFieldData : fieldMap.values()) {
            lResult.add(getFieldData(lFieldData));
        }
        return lResult;
    }

    /**
     * @return
     */
    public List<String> getFieldKeys() {
        Collection<String> lKeyNames = fieldMap.keySet();
        List<String> lKeyNamesList = new ArrayList<String>(lKeyNames.size());

        lKeyNamesList.addAll(lKeyNames);
        return lKeyNamesList;
    }

    private FieldData getFieldData(
            final MultipleLineFieldData pMultiLineFieldData) {
        FieldData lResult = null;
        if (pMultiLineFieldData.isMultiLined()) {
            lResult = new MultiLineFieldDataAccess(pMultiLineFieldData);
        }
        else if (pMultiLineFieldData.isMultiField()) {
            lResult = new MultipleFieldDataAccess(pMultiLineFieldData);
        }
        else {
            lResult =
                    FieldDataFactory.create(pMultiLineFieldData.getLineFieldDatas()[0].getFieldDatas()[0]);
        }
        return lResult;
    }
}
