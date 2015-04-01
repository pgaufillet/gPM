/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.util.validation;

import java.util.ArrayList;
import java.util.List;

import org.topcased.gpm.ui.component.client.container.field.AbstractGpmField;
import org.topcased.gpm.ui.component.client.container.field.GpmLabelledTextBox;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.HasBlurHandlers;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.user.client.ui.Widget;

/**
 * FieldValidator can validate a field {@link AbstractGpmField} according to
 * several rules.
 * <p>
 * A field's validator can bind the validation process with a field's event.
 * </p>
 * <h4>Note</h4> To be binding a field must implement {@link HasBlurHandlers}
 * interface.
 * 
 * @author mkargbo
 */
public class FieldValidator implements IValidator {
    private Validator validator;

    private AbstractGpmField<?> field;

    private List<IRule> rules;

    /**
     * Indicate to the validator that the validated field is only a template
     * field
     */
    private boolean isTemplate;

    /**
     * Construct a field's validator for the specified and set one rule.
     * 
     * @param pField
     *            Field to validate
     * @param pRule
     *            Rules to check.
     */
    @SuppressWarnings("rawtypes")
	public FieldValidator(AbstractGpmField pField, List<IRule> pRule) {
        field = pField;
        rules = new ArrayList<IRule>();
        rules.addAll(pRule);
    }

    /**
     * Bind the validation process with the field's event.
     * <p>
     * Launch the validation process when the <code>blur</code> event is sunk.
     * </p>
     * <p>
     * Set and show the error message for the field.
     * </p>
     */
    public void bind() {
        final Widget lFieldWidget = field.getWidget();
        if (lFieldWidget instanceof HasBlurHandlers) {
            BlurHandler lBlurHandler = new BlurHandler() {

                @Override
                public void onBlur(BlurEvent pEvent) {
                    validation();
                }
            };
            ((HasBlurHandlers) lFieldWidget).addBlurHandler(lBlurHandler);

            field.setHasBlurHandler(true);
        }
        else if (lFieldWidget instanceof HasChangeHandlers) {
            ChangeHandler lChangeHandler = new ChangeHandler() {

                @Override
                public void onChange(ChangeEvent pEvent) {
                    validation();
                }
            };
            ((HasChangeHandlers) lFieldWidget).addChangeHandler(lChangeHandler);
            field.setHasChangeHandler(true);
        }
        else if (field instanceof GpmLabelledTextBox<?>) {
            GpmLabelledTextBox<?> lBox = (GpmLabelledTextBox<?>) field;
            lBox.getTextBox().addBlurHandler(new BlurHandler() {
                @Override
                public void onBlur(BlurEvent pEvent) {
                    validation();
                }
            });
        }
    }

    /**
     * Validate the field.
     */
    private void validation() {
        String lMessage = validate();
        if (lMessage != null) {//There is an error
            field.setError(lMessage);
        }
        else {
            field.removeError();
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.client.util.validation.IValidator#validate()
     */
    @Override
    public String validate() {
        if (isTemplate) { // Do not validate if template field
            return null;
        }
        final StringBuilder lMessages = new StringBuilder();
        for (IRule lRule : rules) {
            String lError = lRule.check(field);
            if (lError != null) {
                lMessages.append(lError);
            }
        }
        final String lErrorMessage;
        if (lMessages.length() < 1) {
            lErrorMessage = null;
        }
        else {
            lErrorMessage = lMessages.toString();
        }
        return lErrorMessage;
    }

    /**
     * Add a new rules.
     * 
     * @param pRules
     *            Rule to append the validation process.
     */
    public void addRule(List<IRule> pRules) {
        rules.addAll(pRules);
    }

    public List<IRule> getRules() {
        return rules;
    }

    public Validator getValidator() {
        return validator;
    }

    public void setValidator(Validator pValidator) {
        validator = pValidator;
    }

    /**
     * Set the associated field as template field
     * 
     * @param pIsTemplate
     *            to set the current field as a template field
     */
    public void setTemplateField(boolean pIsTemplate) {
        isTemplate = pIsTemplate;
    }

}
