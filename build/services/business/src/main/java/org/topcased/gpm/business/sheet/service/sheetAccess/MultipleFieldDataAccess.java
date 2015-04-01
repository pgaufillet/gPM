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

import org.topcased.gpm.business.fields.LineFieldData;
import org.topcased.gpm.business.fields.MultipleLineFieldData;

/**
 * Multiple Field Data Access
 * 
 * @author Laurent Latil
 */
public class MultipleFieldDataAccess implements MultipleFieldData {

    /**
     * Constructor 1
     * 
     * @param pLineFieldData a LineFieldData
     */
    public MultipleFieldDataAccess(final LineFieldData pLineFieldData) {
        name = "";
        labelKey = "";

        initFieldMap(pLineFieldData.getFieldDatas());
    }

    /**
     * Constructor 2
     * 
     * @param pMultiLineFieldData a MultipleLineFieldData
     */
    public MultipleFieldDataAccess(
            final MultipleLineFieldData pMultiLineFieldData) {
        this(pMultiLineFieldData, 0);
    }

    /**
     * Constructor 3
     * 
     * @param pMultiLineFieldData a MultipleLineFieldData
     * @param pNumLine a line number
     */
    public MultipleFieldDataAccess(
            final MultipleLineFieldData pMultiLineFieldData, final int pNumLine) {
        name = pMultiLineFieldData.getI18nName();
        labelKey = pMultiLineFieldData.getLabelKey();

        initFieldMap(pMultiLineFieldData.getLineFieldDatas()[pNumLine].getFieldDatas());
    }

    private void initFieldMap(
            final org.topcased.gpm.business.fields.FieldData[] pFieldData) {
        for (int i = 0; i < pFieldData.length; ++i) {
            fieldMap.put(pFieldData[i].getLabelKey(), pFieldData[i]);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.sheetAccess.FieldData#getName()
     */
    public String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.sheetAccess.FieldData#getLabelKey()
     */
    public String getLabelKey() {
        return labelKey;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.sheetAccess.FieldData#getDescription()
     */
    public String getDescription() {
        return "";
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.sheetAccess.FieldData#isMandatory()
     */
    public boolean isMandatory() {
        return false;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.sheetAccess.FieldData#isConfidential()
     */
    public boolean isConfidential() {
        return false;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.sheetAccess.FieldData#isUpdatable()
     */
    public boolean isUpdatable() {
        return true;
    }

    /**
     * Check if this field contains several values (multiple lines)
     * 
     * @return Always false
     */
    public boolean isMultiLine() {
        return false;
    }

    /**
     * Check if this field contains sub-fields.
     * 
     * @return True.
     */
    public boolean isMultiField() {
        return true;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.sheetAccess.FieldDataContainer#getFields()
     */
    public List<FieldData> getFields() {
        List<FieldData> lResult = new ArrayList<FieldData>(fieldMap.size());

        for (org.topcased.gpm.business.fields.FieldData lFieldData : fieldMap.values()) {
            lResult.add(FieldDataFactory.create(lFieldData));
        }
        return lResult;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.sheetAccess.FieldDataContainer#getFieldKeys()
     */
    public List<String> getFieldKeys() {
        Collection<String> lKeyNames = fieldMap.keySet();
        List<String> lKeyNamesList = new ArrayList<String>(lKeyNames.size());

        lKeyNamesList.addAll(lKeyNames);
        return lKeyNamesList;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.sheetAccess.FieldDataContainer#getField(java.lang.String)
     */
    public FieldData getField(final String pName) {
        return FieldDataFactory.create(fieldMap.get(pName));
    }

    private String name;

    private String labelKey;

    private LinkedHashMap<String, org.topcased.gpm.business.fields.FieldData> fieldMap =
            new LinkedHashMap<String, org.topcased.gpm.business.fields.FieldData>();

}
