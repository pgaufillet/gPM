/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.shared.container.field.simple;

import org.topcased.gpm.business.util.FieldType;
import org.topcased.gpm.business.values.field.BusinessField;
import org.topcased.gpm.business.values.field.simple.BusinessSimpleField;
import org.topcased.gpm.ui.facade.shared.container.field.UiField;
import org.topcased.gpm.ui.facade.shared.util.GpmStringUtils;

/**
 * UiSimpleField
 * 
 * @author nveillet
 * @param <T>
 *            The type of the simple field.
 */
public abstract class UiSimpleField<T> extends UiField implements
        BusinessSimpleField<T> {

    /** serialVersionUID */
    private static final long serialVersionUID = 493848581566565140L;

    protected T value;

    /**
     * Create new UiSimpleField
     * 
     * @param pFieldType
     *            The field type
     */
    public UiSimpleField(FieldType pFieldType) {
        super(pFieldType);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.facade.shared.container.field.UiField#copy(org.topcased.gpm.business.values.field.BusinessField)
     */
    @Override
    public void copy(BusinessField pOther) {
        setAsString(pOther.getAsString());
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessSimpleField#get()
     */
    @Override
    public abstract T get();

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.facade.shared.container.field.UiField#getAsString()
     */
    @Override
    public abstract String getAsString();

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.facade.shared.container.field.UiField#hasSameValues(org.topcased.gpm.business.values.field.BusinessField)
     */
    @Override
    public boolean hasSameValues(BusinessField pOther) {
        String lValue1 = GpmStringUtils.getEmptyIfNull(getAsString());
        String lValue2 = GpmStringUtils.getEmptyIfNull(pOther.getAsString());

        return lValue1.equals(lValue2);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessSimpleField#set(java.lang.Object)
     */
    @Override
    public abstract void set(T pValue);

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessSimpleField#setAsString(java.lang.String)
     */
    @Override
    public abstract void setAsString(String pValue);
}
