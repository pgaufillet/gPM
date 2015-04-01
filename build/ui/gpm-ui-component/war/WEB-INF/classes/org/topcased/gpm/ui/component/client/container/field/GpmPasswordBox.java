/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.container.field;

import org.topcased.gpm.business.values.field.BusinessField;
import org.topcased.gpm.business.values.field.simple.BusinessSimpleField;
import org.topcased.gpm.business.values.field.simple.BusinessStringField;
import org.topcased.gpm.ui.component.client.util.GpmStringUtils;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.user.client.ui.PasswordTextBox;

/**
 * GpmPasswordBox
 * 
 * @author nveillet
 */
public class GpmPasswordBox extends AbstractGpmField<PasswordTextBox> implements
        BusinessSimpleField<String> {

    /**
     * Creates an empty gPM Password Box.
     */
    public GpmPasswordBox() {
        super(new PasswordTextBox());
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#copy(org.topcased.gpm.business.values.field.BusinessField)
     */
    @SuppressWarnings("unchecked")
    @Override
    public void copy(final BusinessField pOther) {
        set(((BusinessSimpleField<String>) pOther).get());
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessSimpleField#get()
     */
    @Override
    public String get() {
        return getAsString();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#getAsString()
     */
    @Override
    public String getAsString() {
        return getWidget().getValue();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.container.field.AbstractGpmField#getEmptyClone()
     */
    @Override
    public AbstractGpmField<PasswordTextBox> getEmptyClone() {
        final GpmPasswordBox lClone = new GpmPasswordBox();

        initEmptyClone(lClone);

        return lClone;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#hasSameValues(org.topcased.gpm.business.values.field.BusinessField)
     */
    @Override
    public boolean hasSameValues(BusinessField pOther) {
        // case for business string fields (comparison not null string values).
        if (pOther instanceof BusinessStringField) {
            return GpmStringUtils.getEmptyIfNull(pOther.getAsString()).equals(
                    get());
        }
        else {
            if (isBothNull((BusinessSimpleField<?>) pOther)) {
                return true;
            }
            else if (isOneOfBothNull((BusinessSimpleField<?>) pOther)) {
                return true;
            }
        }

        return ((BusinessSimpleField<?>) pOther).get().equals(get());
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
     * @see org.topcased.gpm.business.values.field.simple.BusinessSimpleField#set(java.lang.Object)
     */
    @Override
    public void set(String pValue) {
        setAsString(pValue);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessSimpleField#setAsString(java.lang.String)
     */
    @Override
    public void setAsString(String pValue) {
        getWidget().setValue(pValue, true);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.container.field.AbstractGpmField#setEnabled(boolean)
     */
    @Override
    public void setEnabled(boolean pEnabled) {
        getWidget().setEnabled(pEnabled);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.container.field.AbstractGpmField#isEnabled()
     */
    @Override
    public boolean isEnabled() {
        return getWidget().isEnabled();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#clear()
     */
    @Override
    public void clear() {
        getWidget().setValue("", true);
        getWidget().fireEvent(new BlurEvent() {
        });
    }
}
