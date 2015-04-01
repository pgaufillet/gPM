/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Florian ROSIER (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.container.field;

import java.util.List;

import org.topcased.gpm.business.util.Translation;
import org.topcased.gpm.business.values.field.BusinessField;
import org.topcased.gpm.business.values.field.simple.BusinessAttachedField;
import org.topcased.gpm.business.values.field.simple.BusinessChoiceField;
import org.topcased.gpm.business.values.field.simple.BusinessSimpleField;
import org.topcased.gpm.business.values.field.virtual.BusinessVirtualField;
import org.topcased.gpm.ui.component.client.field.formater.GpmFormatter;
import org.topcased.gpm.ui.component.client.util.GpmStringUtils;

import com.google.gwt.user.client.ui.Label;

/**
 * GpmLabel defines a label.
 * 
 * @param <T>
 *            The type of value.
 * @author frosier
 */
public class GpmLabel<T> extends AbstractGpmField<Label> implements
        BusinessSimpleField<T>, BusinessChoiceField, BusinessVirtualField {
    protected final GpmFormatter<T> formatter;

    // Store value, because space are removed on the widget text
    private String value;

    private List<Translation> possibleValues;

    /**
     * Creates an empty gPM label.
     * 
     * @param pFormatter
     *            The formatter.
     */
    public GpmLabel(final GpmFormatter<T> pFormatter) {
        super(new Label());
        formatter = pFormatter;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.container.field.AbstractGpmField#copy(org.topcased.gpm.business.values.field.BusinessField)
     */
    @SuppressWarnings("unchecked")
    @Override
    public void copy(final BusinessField pOther) {
        if (pOther instanceof BusinessChoiceField) {
            setCategoryValue(((BusinessChoiceField) pOther).getCategoryValue());
        }
        else if (pOther instanceof BusinessAttachedField) {
            setAsString(((BusinessAttachedField) pOther).getFileName());
        }
        else if (pOther instanceof BusinessVirtualField) {
            setAsString(((BusinessVirtualField) pOther).getValue());
        }
        else {
            set(((BusinessSimpleField<T>) pOther).get());
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.container.field.AbstractGpmField#hasSameValues(org.topcased.gpm.business.values.field.BusinessField)
     */
    @Override
    public boolean hasSameValues(final BusinessField pOther) {
        if (pOther == null) {
            return false;
        }
        else {
            final String lOtherValue;

            if (pOther instanceof BusinessChoiceField) {
                lOtherValue = ((BusinessChoiceField) pOther).getCategoryValue();
            }
            else if (pOther instanceof BusinessAttachedField) {
                lOtherValue = ((BusinessAttachedField) pOther).getFileName();
            }
            else if (pOther instanceof BusinessVirtualField) {
                lOtherValue = ((BusinessVirtualField) pOther).getValue();
            }
            else if (((BusinessSimpleField<?>) pOther).get() != null) {
                lOtherValue =
                        ((BusinessSimpleField<?>) pOther).get().toString();
            }
            else {
                lOtherValue = null;
            }

            if (lOtherValue == null) {
                return getAsString() == null;
            }
            else {
                return getAsString().equals(lOtherValue);
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessSimpleField#get()
     */
    @Override
    public T get() {
        return formatter.parse(GpmStringUtils.getEmptyIfNull(getAsString()));
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#getAsString()
     */
    @Override
    public String getAsString() {
        return value;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessChoiceField#getCategoryValue()
     */
    @Override
    public String getCategoryValue() {
        return getAsString();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessSimpleField#set(java.lang.Object)
     */
    @Override
    public void set(final T pValue) {
        setAsString(formatter.format(pValue));
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessSimpleField#setAsString(java.lang.String)
     */
    @Override
    public void setAsString(final String pValue) {
        value = pValue;

        String lValue = pValue;

        // Get translated values (choice field)
        if (possibleValues != null) {
            for (Translation lPossibleValue : possibleValues) {
                if (lPossibleValue.equals(lValue)) {
                    lValue = lPossibleValue.getTranslatedValue();
                    break;
                }
            }
        }

        getWidget().setText(lValue);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessChoiceField#setCategoryValue(java.lang.String)
     */
    @Override
    public void setCategoryValue(final String pValue) {
        setAsString(pValue);
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
     * @see org.topcased.gpm.ui.component.client.container.field.AbstractGpmField#setEnabled(boolean)
     */
    @Override
    public void setEnabled(boolean pEnabled) {
        // Do nothing;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.container.field.AbstractGpmField#getEmptyClone()
     */
    @Override
    public GpmLabel<T> getEmptyClone() {
        final GpmLabel<T> lClone = new GpmLabel<T>(formatter);

        initEmptyClone(lClone);

        lClone.setPossibleValues(possibleValues);

        return lClone;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.virtual.BusinessVirtualField#getValue()
     */
    @Override
    public String getValue() {
        return getAsString();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#clear()
     */
    @Override
    public void clear() {
        set(null);
    }

    /**
     * get possible values
     * 
     * @return the possible values
     */
    public List<Translation> getPossibleValues() {
        return possibleValues;
    }

    /**
     * set possible values
     * 
     * @param pPossibleValues
     *            the possible values to set
     */
    public void setPossibleValues(List<Translation> pPossibleValues) {
        possibleValues = pPossibleValues;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.container.field.AbstractGpmField#isEnabled()
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}