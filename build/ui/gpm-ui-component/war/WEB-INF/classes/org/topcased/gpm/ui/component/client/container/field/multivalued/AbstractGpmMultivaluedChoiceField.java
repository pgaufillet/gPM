/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien Eballard (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.container.field.multivalued;

import java.util.ArrayList;
import java.util.List;

import org.topcased.gpm.business.values.field.BusinessField;
import org.topcased.gpm.business.values.field.simple.BusinessChoiceField;

import com.google.gwt.user.client.ui.Widget;

/**
 * AbstractGpmMultivaluedChoiceField
 * 
 * @author jeballar
 * @param <W>
 *            The type of widget.
 */
public abstract class AbstractGpmMultivaluedChoiceField<W extends Widget>
        extends AbstractGpmMultivaluedField<BusinessChoiceField, W> {
    /**
     * Constructor
     * 
     * @param pWidget
     *            The widget.
     */
    public AbstractGpmMultivaluedChoiceField(final W pWidget) {
        super(pWidget);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#isUpdatable()
     */
    @Override
    public boolean isUpdatable() {
        return true;
    }

    /**
     * Select or unselect a value.
     * 
     * @param pValue
     *            The value.
     * @param pSelected
     *            If it's a select action.
     */
    abstract public void setWidgetValueSelected(final String pValue,
            final boolean pSelected);

    /**
     * Get the selected values.
     * 
     * @return The selected values.
     */
    abstract protected List<String> getSelectedValues();

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.container.field.multivalued.AbstractGpmMultivaluedField#getFieldList()
     */
    @Override
    public List<BusinessChoiceField> getFieldList() {
        final List<BusinessChoiceField> lList =
                new ArrayList<BusinessChoiceField>();

        for (final String lValue : getSelectedValues()) {
            lList.add(new ChoiceField(lValue));
        }
        // Always at less one element
        if (lList.isEmpty()) {
            lList.add(new ChoiceField());
        }

        return lList;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.multivalued.BusinessMultivaluedField#addLine()
     */
    @Override
    public BusinessChoiceField addLine() {
        return new ChoiceField();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.multivalued.BusinessMultivaluedField#removeLine()
     */
    @Override
    public BusinessChoiceField removeLine() {
        final List<BusinessChoiceField> lValues = getFieldList();

        if (!lValues.isEmpty()) {
            setWidgetValueSelected(
                    lValues.get(lValues.size() - 1).getCategoryValue(), false);
        }

        return null;
    }

    private class ChoiceField implements BusinessChoiceField {
        private String value;

        /**
         * Create a choice field.
         */
        public ChoiceField() {
        }

        /**
         * Create a choice field.
         */
        public ChoiceField(final String pValue) {
            value = pValue;
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.topcased.gpm.business.values.field.simple.BusinessChoiceField#getCategoryValue()
         */
        @Override
        public String getCategoryValue() {
            return value;
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.topcased.gpm.business.values.field.simple.BusinessChoiceField#setCategoryValue(java.lang.String)
         */
        @Override
        public void setCategoryValue(final String pValue) {
            // Deselect old value
            if (value != null) {
                setWidgetValueSelected(value, false);
            }
            value = pValue;
            // Select new value
            if (value != null) {
                setWidgetValueSelected(value, true);
            }
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.topcased.gpm.business.values.field.BusinessField#clear()
         */
        @Override
        public void clear() {
            setCategoryValue(null);
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.topcased.gpm.business.values.field.BusinessField#copy(org.topcased.gpm.business.values.field.BusinessField)
         */
        @Override
        public void copy(final BusinessField pOther) {
            setCategoryValue(((BusinessChoiceField) pOther).getCategoryValue());
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
            return null;
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.topcased.gpm.business.values.field.BusinessField#getFieldName()
         */
        @Override
        public String getFieldName() {
            return null;
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.topcased.gpm.business.values.field.BusinessField#hasSameValues(org.topcased.gpm.business.values.field.BusinessField)
         */
        @Override
        public boolean hasSameValues(final BusinessField pOther) {
            return ((BusinessChoiceField) pOther).getCategoryValue().equals(
                    getCategoryValue());
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
            return true;
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
            return true;
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.topcased.gpm.business.values.field.BusinessField#reset()
         */
        @Override
        public void reset() {
            clear();
        }
    }
}