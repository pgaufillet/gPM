/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.container.field;

import static org.topcased.gpm.ui.component.client.resources.ComponentResources.INSTANCE;

import org.topcased.gpm.business.values.field.BusinessField;
import org.topcased.gpm.business.values.field.simple.BusinessChoiceField;
import org.topcased.gpm.business.values.field.simple.BusinessSimpleField;
import org.topcased.gpm.ui.component.client.exception.NotImplementedException;
import org.topcased.gpm.ui.component.client.util.GpmComponent;
import org.topcased.gpm.ui.component.client.util.validation.FieldValidator;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Interface implemented by all gPM field widget.
 * 
 * @author tpanuel
 * @param <W>
 *            The type of Widget.
 */
public abstract class AbstractGpmField<W extends Widget> implements
        BusinessField, GpmComponent<W> {
    private final W widget;

    private boolean mandatory;

    private String fieldDescription;

    private String name;

    private String translatedName;

    private HTML fieldError = new HTML();

    private FlowPanel panel = new FlowPanel();

    private FieldValidator fieldValidator;

    private boolean hasBlurHandler;

    private boolean hasChangeHandler;

    /**
     * Create an AbstractGpmField.
     * 
     * @param pWidget
     *            The widget.
     */
    public AbstractGpmField(final W pWidget) {
        widget = pWidget;
        mandatory = false;
        panel.add(widget);
        panel.add(fieldError);
        fieldError.setVisible(false);
    }

    /**
     * get the field cloned without value
     * 
     * @return the field cloned without value
     */
    abstract public AbstractGpmField<W> getEmptyClone();

    /**
     * Initialize the empty clone.
     * 
     * @param pEmptyClone
     *            The empty clone.
     */
    protected void initEmptyClone(final AbstractGpmField<W> pEmptyClone) {
        pEmptyClone.setFieldName(name);
        pEmptyClone.setTranslatedFieldName(translatedName);
        pEmptyClone.setFieldDescription(fieldDescription);
        pEmptyClone.setMandatory(mandatory);

        if (hasBlurHandler || hasChangeHandler) {
            FieldValidator lFieldValidator =
                    new FieldValidator(pEmptyClone, fieldValidator.getRules());
            lFieldValidator.bind();
            lFieldValidator.setValidator(getFieldValidator().getValidator());
            getFieldValidator().getValidator().put(pEmptyClone, lFieldValidator);
            pEmptyClone.setFieldValidator(lFieldValidator);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.util.GpmComponent#getWidget()
     */
    @Override
    public W getWidget() {
        return widget;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#getFieldName()
     */
    @Override
    public String getFieldName() {
        return name;
    }

    /**
     * Set the field name.
     * 
     * @param pFieldName
     *            The name of the field.
     */
    public void setFieldName(final String pFieldName) {
        name = pFieldName;
    }

    /**
     * get translated field name
     * 
     * @return the translated field name
     */
    public String getTranslatedFieldName() {
        return translatedName;
    }

    /**
     * set translated field name
     * 
     * @param pTranslatedFieldName
     *            the translated field name to set
     */
    public void setTranslatedFieldName(String pTranslatedFieldName) {
        translatedName = pTranslatedFieldName;
    }

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
     * Set field description.
     * 
     * @param pFieldDescription
     *            The field description.
     */
    public void setFieldDescription(final String pFieldDescription) {
        fieldDescription = pFieldDescription;
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
     * Set if the field is mandatory.
     * 
     * @param pMandatory
     *            If the field is mandatory.
     */
    public void setMandatory(final boolean pMandatory) {
        mandatory = pMandatory;
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
        final String lValue = getAsString();
        return lValue == null || lValue.trim().isEmpty();
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

    //*** Not implemented methods ***/ 
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
     * @see org.topcased.gpm.business.values.field.BusinessField#clear()
     */
    @Override
    public void clear() {
        throw new NotImplementedException("Not implemented method");
    }

    // Commons methods to ensure not null values for hasSameValues implementations.
    /**
     * Determines if only one of both values between the other business choice
     * field and inner gPM field value is null.
     * 
     * @param pChoiceOther
     *            The other business choice field.
     * @return <code>true</code> if only one of both values is null.
     */
    protected boolean isOneOfBothNull(final BusinessChoiceField pChoiceOther) {
        if (pChoiceOther == null) {
            if (((BusinessChoiceField) this).getCategoryValue() != null) {
                return true;
            }
        }
        else {
            if (((BusinessChoiceField) this).getCategoryValue() == null) {
                return true;
            }
        }

        return false;
    }

    /**
     * Determines if only one of both values between the other business simple
     * field and inner gPM field value is null.
     * 
     * @param pSimpleOther
     *            The other business simple field.
     * @return <code>true</code> if only one of both values is null.
     */
    protected boolean isOneOfBothNull(final BusinessSimpleField<?> pSimpleOther) {
        if (pSimpleOther == null) {
            if (((BusinessSimpleField<?>) this).get() != null) {
                return true;
            }
        }
        else {
            if (((BusinessSimpleField<?>) this).get() == null) {
                return true;
            }
        }

        return false;
    }

    /**
     * Determines if both values between other business simple field and inner
     * gPM field value are null.
     * 
     * @param pSimpleOther
     *            The other simple field.
     * @return <code>true</code> if both values is null.
     */
    protected boolean isBothNull(final BusinessSimpleField<?> pSimpleOther) {
        if (pSimpleOther.get() == null
                && ((BusinessSimpleField<?>) this).get() == null) {
            return true;
        }

        return false;
    }

    /**
     * Determines if both values between other business choice field and inner
     * gPM field value are null.
     * 
     * @param pChoiceOther
     *            The other choice field.
     * @return <code>true</code> if both values is null.
     */
    protected boolean isBothNull(final BusinessChoiceField pChoiceOther) {
        if (pChoiceOther == null
                && ((BusinessChoiceField) this).getCategoryValue() == null) {
            return true;
        }
        return false;
    }

    /**
     * Indicates if the field is enabled
     * 
     * @return <CODE>true</CODE> if the field is enabled, else
     *         <CODE>false</CODE>
     */
    public abstract boolean isEnabled();

    /**
     * Enable disable field
     * 
     * @param pEnabled
     *            <code>true</code> to enable field, <code>false</code> to
     *            disable
     */
    public abstract void setEnabled(boolean pEnabled);

    public Panel getPanel() {
        return panel;
    }

    /**
     * Indicates to the user that the field is in error with the corresponding
     * message by adding it to the Widget panel
     * 
     * @param pErrorMessage
     *            the new error message
     */
    public void setError(final String pErrorMessage) {
        fieldError.setHTML(pErrorMessage);
        fieldError.setVisible(true);
        widget.addStyleName(INSTANCE.css().gpmFieldError());
        fieldError.addStyleName(INSTANCE.css().gpmFieldErrorMessage());
    }

    /**
     * Remove the error message of a field
     */
    public void removeError() {
        fieldError.setVisible(false);
        widget.removeStyleName(INSTANCE.css().gpmFieldError());
        fieldError.removeStyleName(INSTANCE.css().gpmFieldErrorMessage());
    }

    public void setHasBlurHandler(boolean pHasBlurHandler) {
        hasBlurHandler = pHasBlurHandler;
    }

    public void setHasChangeHandler(boolean pHasChangeHandler) {
        hasChangeHandler = pHasChangeHandler;
    }

    public FieldValidator getFieldValidator() {
        return fieldValidator;
    }

    public void setFieldValidator(FieldValidator pFieldValidator) {
        fieldValidator = pFieldValidator;
    }
}