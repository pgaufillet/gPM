/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: XXX (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.component.field;

import org.topcased.gpm.business.values.field.BusinessField;
import org.topcased.gpm.business.values.field.simple.BusinessStringField;

/**
 * BusinessTextFieldTestStub : Stub for Gpm fields Junit tests.
 * 
 * @author frosier
 */
public class BusinessTextFieldTestStub implements BusinessStringField {

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessSimpleField#get()
     */
    @Override
    public String get() {
        return "One value";
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessSimpleField#set(java.lang.Object)
     */
    @Override
    public void set(String pValue) {
        // TODO Auto-generated method stub
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessSimpleField#setAsString(java.lang.String)
     */
    @Override
    public void setAsString(String pValue) {
        // TODO Auto-generated method stub
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#clear()
     */
    @Override
    public void clear() {
        // TODO Auto-generated method stub
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#copy(org.topcased.gpm.business.values.field.BusinessField)
     */
    @Override
    public void copy(BusinessField pOther) {
        // TODO Auto-generated method stub
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#getAsString()
     */
    @Override
    public String getAsString() {
        return "One value";
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#getFieldDescription()
     */
    @Override
    public String getFieldDescription() {
        return "Field description";
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#getFieldName()
     */
    @Override
    public String getFieldName() {
        return "FIELD_NAME";
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessStringField#getInternalUrlSheetReference()
     */
    @Override
    public String getInternalUrlSheetReference() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessStringField#getSize()
     */
    @Override
    public int getSize() {
        return 100;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#hasSameValues(org.topcased.gpm.business.values.field.BusinessField)
     */
    @Override
    public boolean hasSameValues(BusinessField pOther) {
        return (getAsString().equals(pOther.getAsString()));
    }

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
        return false;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#isExportable()
     */
    @Override
    public boolean isExportable() {
        return false;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#isMandatory()
     */
    @Override
    public Boolean isMandatory() {
        return false;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#isUpdatable()
     */
    @Override
    public boolean isUpdatable() {
        return false;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#reset()
     */
    @Override
    public void reset() {
        // TODO Auto-generated method stub

    }

}
