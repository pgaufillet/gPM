/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.shared.container.field;

import java.io.Serializable;

import org.topcased.gpm.business.util.FieldType;
import org.topcased.gpm.business.values.field.BusinessField;
import org.topcased.gpm.ui.facade.shared.exception.NotImplementedException;

/**
 * UiField
 * 
 * @author nveillet
 */
public abstract class UiField implements BusinessField, Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 3411230634571733513L;

    private String fieldDescription;

    private String fieldName;

    private FieldType fieldType;

    private boolean mandatory;

    private String translatedFieldName;

    private boolean updatable;

    /**
     * Create new UiField
     * 
     * @param pFieldType
     *            The field type
     */
    public UiField(FieldType pFieldType) {
        fieldType = pFieldType;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#clear()
     */
    @Override
    public void clear() {
        throw new NotImplementedException("Not implemented method");
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#copy(org.topcased.gpm.business.values.field.BusinessField)
     */
    @Override
    public abstract void copy(BusinessField pOther);

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#getAsString()
     */
    @Override
    public abstract String getAsString();

    /**
     * get a field clone without value
     * 
     * @return the cloned field
     */
    public abstract UiField getEmptyClone();

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#getFieldDescription()
     */
    @Override
    public String getFieldDescription() {
        return fieldDescription;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#getFieldName()
     */
    @Override
    public String getFieldName() {
        return fieldName;
    }

    /**
     * Get the field type
     * 
     * @return The field type
     */
    public FieldType getFieldType() {
        return fieldType;
    }

    /**
     * get translated field name
     * 
     * @return the translated field name
     */
    public String getTranslatedFieldName() {
        return translatedFieldName;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#hasSameValues(org.topcased.gpm.business.values.field.BusinessField)
     */
    @Override
    public abstract boolean hasSameValues(BusinessField pOther);

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#isConfidential()
     */
    @Override
    public boolean isConfidential() {
        return false;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#isEmpty()
     */
    @Override
    public boolean isEmpty() {
        throw new NotImplementedException("Not implemented method");
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#isExportable()
     */
    @Override
    public boolean isExportable() {
        throw new NotImplementedException("Not implemented method");
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#isMandatory()
     */
    @Override
    public Boolean isMandatory() {
        return mandatory;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#isUpdatable()
     */
    @Override
    public boolean isUpdatable() {
        return updatable;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#reset()
     */
    @Override
    public void reset() {
        throw new NotImplementedException("Not implemented method");
    }

    //*** Not implemented methods ***/

    /**
     * Set the field description
     * 
     * @param pFieldDescription
     *            The field description to set
     */
    public void setFieldDescription(String pFieldDescription) {
        fieldDescription = pFieldDescription;
    }

    /**
     * Set the field name
     * 
     * @param pFieldName
     *            The field name to set
     */
    public void setFieldName(String pFieldName) {
        fieldName = pFieldName;
    }

    /**
     * Set the mandatory access
     * 
     * @param pMandatory
     *            The mandatory access to set
     */
    public void setMandatory(boolean pMandatory) {
        mandatory = pMandatory;
    }

    /**
     * set translated field name
     * 
     * @param pTranslatedFieldName
     *            the translated field name to set
     */
    public void setTranslatedFieldName(String pTranslatedFieldName) {
        translatedFieldName = pTranslatedFieldName;
    }

    /**
     * Set the updatable access
     * 
     * @param pUpdatable
     *            The updatable access to set
     */
    public void setUpdatable(boolean pUpdatable) {
        updatable = pUpdatable;
    }
}
