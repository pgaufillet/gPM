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

import java.util.Arrays;
import java.util.List;

import org.topcased.gpm.business.values.field.BusinessField;
import org.topcased.gpm.business.values.field.simple.BusinessChoiceField;
import org.topcased.gpm.ui.component.client.container.field.GpmChoiceBoxValue;

/**
 * BusinessChoiceFieldTestStub : Stub for Gpm fields Junit tests.
 * 
 * @author frosier
 */
public class BusinessChoiceFieldTestStub implements BusinessChoiceField {

    public static final List<String> VALUES =
            Arrays.asList("value 1", "value 2", "value 3", "value 4",
                    "value 5", "value 6");

    public static final List<GpmChoiceBoxValue> CHOICE_VALUES =
            Arrays.asList(new GpmChoiceBoxValue("value 1", "value 1"),
                    new GpmChoiceBoxValue("value 2", "value 2"),
                    new GpmChoiceBoxValue("value 3", "value 3"),
                    new GpmChoiceBoxValue("value 4", "value 4"));

    private static final int VALUE_INDEX = 3;

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessChoiceField#getCategoryValue()
     */
    @Override
    public String getCategoryValue() {
        return VALUES.get(VALUE_INDEX);
    }

    /**
     * For tests. Give values for radio box.
     * 
     * @return Radio button values.
     */
    public static List<String> getRadioButtonValues() {
        return Arrays.asList("value 1", "value 2", "value 3", "value 4",
                "value 5", "value 6");
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessChoiceField#setCategoryValue(java.lang.String)
     */
    @Override
    public void setCategoryValue(String pValue) {
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
        return getCategoryValue();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#getFieldDescription()
     */
    @Override
    public String getFieldDescription() {
        // TODO Auto-generated method stub
        return null;
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
