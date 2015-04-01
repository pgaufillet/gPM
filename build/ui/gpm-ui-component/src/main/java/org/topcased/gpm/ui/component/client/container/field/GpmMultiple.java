/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Joël GIAUFER (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.container.field;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.topcased.gpm.business.values.field.BusinessField;
import org.topcased.gpm.business.values.field.multiple.BusinessMultipleField;
import org.topcased.gpm.business.values.field.multivalued.BusinessMultivaluedField;
import org.topcased.gpm.business.values.field.simple.BusinessAttachedField;
import org.topcased.gpm.business.values.field.simple.BusinessBooleanField;
import org.topcased.gpm.business.values.field.simple.BusinessChoiceField;
import org.topcased.gpm.business.values.field.simple.BusinessDateField;
import org.topcased.gpm.business.values.field.simple.BusinessIntegerField;
import org.topcased.gpm.business.values.field.simple.BusinessPointerField;
import org.topcased.gpm.business.values.field.simple.BusinessRealField;
import org.topcased.gpm.business.values.field.simple.BusinessStringField;
import org.topcased.gpm.ui.component.client.container.GpmFieldGridPanel;
import org.topcased.gpm.ui.component.client.resources.ComponentResources;

import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

/**
 * GpmMultiple component. Can be initialize using a list of GpmComponents
 * <u>or</u> a list of GpmHandlers that are able to create an empty GpmComponent
 * 
 * @author jgiaufer
 */
public class GpmMultiple extends AbstractGpmField<Grid> implements
        BusinessMultipleField {
    private final List<AbstractGpmField<?>> fields;

    /**
     * GpmMultiple constructor.
     * 
     * @param pFields
     *            The fields.
     */
    public GpmMultiple(final List<AbstractGpmField<?>> pFields) {
        super(new Grid(2, pFields.size()));
        fields = pFields;

        // Initialize table
        for (int i = 0; i < fields.size(); i++) {
            final AbstractGpmField<?> lSubField = fields.get(i);
            // Add the title and the content of each field
            final Widget lHeader;

            if (lSubField.isMandatory() && lSubField.isUpdatable()) {
                lHeader =
                        new HTML(lSubField.getTranslatedFieldName()
                                + GpmFieldGridPanel.MANDATORY_LABEL);
            }
            else {
                lHeader = new HTML(lSubField.getTranslatedFieldName());
            }

            lHeader.setTitle(lSubField.getFieldDescription());
            getWidget().setWidget(0, i, lHeader);
            getWidget().setWidget(1, i, lSubField.getPanel());
            getWidget().getCellFormatter().addStyleName(0, i,
                    ComponentResources.INSTANCE.css().gpmMultipleUnderline());
        }

        // Set style
        getWidget().addStyleName(
                ComponentResources.INSTANCE.css().gpmMultiple());
        getWidget().getRowFormatter().addStyleName(0,
                ComponentResources.INSTANCE.css().gpmFieldLabel());
    }

    /**
     * Get the fields.
     * 
     * @return The fields.
     */
    public List<AbstractGpmField<?>> getFields() {
        return fields;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#copy(org.topcased.gpm.business.values.field.BusinessField)
     */
    @Override
    public void copy(final BusinessField pOther) {
        for (BusinessField lSubField : fields) {
            lSubField.copy(((BusinessMultipleField) pOther).getField(lSubField.getFieldName()));
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#hasSameValues(org.topcased.gpm.business.values.field.BusinessField)
     */
    @Override
    public boolean hasSameValues(final BusinessField pOther) {
        if (!(pOther instanceof BusinessMultipleField)) {
            return false;
        }
        for (BusinessField lSubField : fields) {
            if (!lSubField.hasSameValues(((BusinessMultipleField) pOther).getField(lSubField.getFieldName()))) {
                return false;
            }
        }
        // Also check field count
        int lCounter = 0;
        for (@SuppressWarnings("unused")
        BusinessField lTmp : (BusinessMultipleField) pOther) {
            lCounter++;
        }
        if (lCounter != fields.size()) {
            return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Iterable#iterator()
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public Iterator<BusinessField> iterator() {
        return ((Collection) fields).iterator();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#getAsString()
     */
    @Override
    public String getAsString() {
        final StringBuilder lRes = new StringBuilder();

        for (AbstractGpmField<?> lSubField : fields) {
            lRes.append(lSubField.getAsString());
        }

        return lRes.toString();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessFieldSet#getAttachedField(java.lang.String)
     */
    @Override
    public BusinessAttachedField getAttachedField(String pFieldName) {
        throw new IllegalStateException("Not implemented method");
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessFieldSet#getBooleanField(java.lang.String)
     */
    @Override
    public BusinessBooleanField getBooleanField(String pFieldName) {
        throw new IllegalStateException("Not implemented method");
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessFieldSet#getChoiceField(java.lang.String)
     */
    @Override
    public BusinessChoiceField getChoiceField(String pFieldName) {
        throw new IllegalStateException("Not implemented method");
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessFieldSet#getDateField(java.lang.String)
     */
    @Override
    public BusinessDateField getDateField(String pFieldName) {
        throw new IllegalStateException("Not implemented method");
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessFieldSet#getField(java.lang.String)
     */
    @Override
    public BusinessField getField(final String pFieldName) {
        for (BusinessField lSubField : fields) {
            if (lSubField.getFieldName().equals(pFieldName)) {
                return lSubField;
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessFieldSet#getIntegerField(java.lang.String)
     */
    @Override
    public BusinessIntegerField getIntegerField(String pFieldName) {
        throw new IllegalStateException("Not implemented method");
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessFieldSet#getMultipleField(java.lang.String)
     */
    @Override
    public BusinessMultipleField getMultipleField(String pFieldName) {
        throw new IllegalStateException("Not implemented method");
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessFieldSet#getMultivaluedAttachedField(java.lang.String)
     */
    @Override
    public BusinessMultivaluedField<BusinessAttachedField> getMultivaluedAttachedField(
            String pFieldName) {
        throw new IllegalStateException("Not implemented method");
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessFieldSet#getMultivaluedBooleanField(java.lang.String)
     */
    @Override
    public BusinessMultivaluedField<BusinessBooleanField> getMultivaluedBooleanField(
            String pFieldName) {
        throw new IllegalStateException("Not implemented method");
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessFieldSet#getMultivaluedChoiceField(java.lang.String)
     */
    @Override
    public BusinessMultivaluedField<BusinessChoiceField> getMultivaluedChoiceField(
            String pFieldName) {
        throw new IllegalStateException("Not implemented method");
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessFieldSet#getMultivaluedDateField(java.lang.String)
     */
    @Override
    public BusinessMultivaluedField<BusinessDateField> getMultivaluedDateField(
            String pFieldName) {
        throw new IllegalStateException("Not implemented method");
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessFieldSet#getMultivaluedIntegerField(java.lang.String)
     */
    @Override
    public BusinessMultivaluedField<BusinessIntegerField> getMultivaluedIntegerField(
            String pFieldName) {
        throw new IllegalStateException("Not implemented method");
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessFieldSet#getMultivaluedMultipleField(java.lang.String)
     */
    @Override
    public BusinessMultivaluedField<BusinessMultipleField> getMultivaluedMultipleField(
            String pFieldName) {
        throw new IllegalStateException("Not implemented method");
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessFieldSet#getMultivaluedPointerField(java.lang.String)
     */
    @Override
    public BusinessMultivaluedField<BusinessPointerField> getMultivaluedPointerField(
            String pFieldName) {
        throw new IllegalStateException("Not implemented method");
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessFieldSet#getMultivaluedRealField(java.lang.String)
     */
    @Override
    public BusinessMultivaluedField<BusinessRealField> getMultivaluedRealField(
            String pFieldName) {
        throw new IllegalStateException("Not implemented method");
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessFieldSet#getMultivaluedStringField(java.lang.String)
     */
    @Override
    public BusinessMultivaluedField<BusinessStringField> getMultivaluedStringField(
            String pFieldName) {
        throw new IllegalStateException("Not implemented method");
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessFieldSet#getPointerField(java.lang.String)
     */
    @Override
    public BusinessPointerField getPointerField(String pFieldName) {
        throw new IllegalStateException("Not implemented method");
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessFieldSet#getRealField(java.lang.String)
     */
    @Override
    public BusinessRealField getRealField(String pFieldName) {
        throw new IllegalStateException("Not implemented method");
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessFieldSet#getStringField(java.lang.String)
     */
    @Override
    public BusinessStringField getStringField(String pFieldName) {
        throw new IllegalStateException("Not implemented method");
    }

    /**
     * {@inheritDoc} For a Multiple field, it is updatable if one of its
     * subfields is updatable.
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#isUpdatable()
     */
    @Override
    public boolean isUpdatable() {
        for (AbstractGpmField<?> lField : getFields()) {
            if (lField.isUpdatable()) {
                return true;
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.container.field.AbstractGpmField#setEnabled(boolean)
     */
    @Override
    public void setEnabled(boolean pEnabled) {
        for (AbstractGpmField<?> lField : fields) {
            lField.setEnabled(pEnabled);
            if (pEnabled) {
                lField.getWidget().removeStyleName(
                        ComponentResources.INSTANCE.css().gpmMultivaluedWidgetLineDeleted());
            }
            else {
                lField.getWidget().addStyleName(
                        ComponentResources.INSTANCE.css().gpmMultivaluedWidgetLineDeleted());
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.container.field.AbstractGpmField#getEmptyClone()
     */
    @Override
    public GpmMultiple getEmptyClone() {
        final LinkedList<AbstractGpmField<?>> lClonedFields =
                new LinkedList<AbstractGpmField<?>>();

        for (final AbstractGpmField<?> lField : fields) {
            lClonedFields.add(lField.getEmptyClone());
        }

        final GpmMultiple lClone = new GpmMultiple(lClonedFields);

        initEmptyClone(lClone);

        return lClone;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.container.field.AbstractGpmField#isEnabled()
     */
    @Override
    public boolean isEnabled() {
        boolean lResult = true;
        for (AbstractGpmField<?> lField : fields) {
            lResult = lResult && lField.isEnabled();
        }
        return lResult;
    }
}