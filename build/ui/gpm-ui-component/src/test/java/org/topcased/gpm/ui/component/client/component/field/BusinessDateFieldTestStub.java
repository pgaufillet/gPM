/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Florian ROSIER (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.component.field;

import java.util.Date;

import org.topcased.gpm.business.values.field.BusinessField;
import org.topcased.gpm.business.values.field.simple.BusinessDateField;

/**
 * BusinessDateFieldTestStub : Stub for Gpm fields Junit tests.
 * 
 * @author frosier
 */
public class BusinessDateFieldTestStub implements BusinessDateField {

    private static final Date CURRENT_DATE =
            new Date(System.currentTimeMillis());

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessSimpleField#get()
     */
    @Override
    public Date get() {
        return CURRENT_DATE;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessSimpleField#set(java.lang.Object)
     */
    @Override
    public void set(Date pValue) {
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
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#getFieldDescription()
     */
    @Override
    public String getFieldDescription() {
        return "Business date field description";
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#getFieldName()
     */
    @Override
    public String getFieldName() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#hasSameValues(org.topcased.gpm.business.values.field.BusinessField)
     */
    @Override
    public boolean hasSameValues(BusinessField pOther) {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#isConfidential()
     */
    @Override
    public boolean isConfidential() {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#isEmpty()
     */
    @Override
    public boolean isEmpty() {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#isExportable()
     */
    @Override
    public boolean isExportable() {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#isMandatory()
     */
    @Override
    public Boolean isMandatory() {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#isUpdatable()
     */
    @Override
    public boolean isUpdatable() {
        // TODO Auto-generated method stub
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
