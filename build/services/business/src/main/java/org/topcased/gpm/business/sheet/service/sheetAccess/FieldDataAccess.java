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
 * Implementation of FieldData
 * 
 * @author llatil
 */
public class FieldDataAccess implements FieldData {

    /**
     * Constructs a new FieldDataAccess
     * 
     * @param pFieldData
     */
    public FieldDataAccess(
            final org.topcased.gpm.business.fields.FieldData pFieldData) {
        name = pFieldData.getI18nName();
        labelKey = pFieldData.getLabelKey();
        description = pFieldData.getDescription();

        confidential = pFieldData.isConfidential();
        mandatory = pFieldData.isMandatory();
        exportable = pFieldData.isExportable();

        fieldData = pFieldData;
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
        return description;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.sheetAccess.FieldData#isMandatory()
     */
    public boolean isMandatory() {
        return mandatory;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.sheetAccess.FieldData#isConfidential()
     */
    public boolean isConfidential() {
        return confidential;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.sheet.service.sheetAccess.FieldData#isUpdatable()
     */
    public boolean isUpdatable() {
        return updatable;
    }

    /**
     * Check if this field can be exported
     * 
     * @return true if the field is exportable
     */
    public boolean isExportable() {
        return exportable;
    }

    /**
     * Check if this field contains several values (multiple lines)
     * 
     * @return Always false, as this class references only a single field.
     */
    public boolean isMultiLine() {
        return false;
    }

    /**
     * Check if this field contains sub-fields.
     * 
     * @return Always false, as this class references only a single field.
     */
    public boolean isMultiField() {
        return false;
    }

    protected org.topcased.gpm.business.fields.FieldData fieldData;

    private String labelKey;

    private String name;

    private String description;

    private boolean confidential;

    private boolean mandatory;

    private boolean updatable;

    private boolean exportable;
}
