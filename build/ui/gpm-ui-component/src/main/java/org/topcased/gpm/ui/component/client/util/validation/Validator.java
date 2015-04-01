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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.topcased.gpm.ui.component.client.container.field.AbstractGpmField;

/**
 * Validator can process the validation of several field's validator
 * {@link FieldValidator}.
 * <p>
 * The error message has this pattern: 'fieldName|_|error message'
 * </p>
 * 
 * @author mkargbo
 */
public class Validator implements IValidator {

    private Map<Integer, FieldValidator> validators;

    /**
     * Default constructor.
     */
    public Validator() {
        validators = new HashMap<Integer, FieldValidator>();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.client.util.validation.IValidator#validate()
     */
    @Override
    public String validate() {
        final StringBuilder lMessages = new StringBuilder();
        for (Map.Entry<Integer, FieldValidator> lEntry : validators.entrySet()) {
            String lError = lEntry.getValue().validate();
            if (lError != null) {
                lMessages.append(lError.toString()).append("<br/>");
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
     * Add a new field's validation.
     * <p>
     * If the field is already set, append the rule to this field.
     * </p>
     * <p>
     * Validators have been bind with the validation process
     * </p>
     * 
     * @see FieldValidator#bind()
     * @param pField
     *            Field to validate
     * @param pRule
     *            Rule to append to checking.
     */
    @SuppressWarnings("rawtypes")
	public void addValidation(AbstractGpmField pField, List<IRule> pRule) {
        if (!pRule.isEmpty()) {
            if (validators.containsKey(generateValidatorKey(pField))) {
                FieldValidator lValidator =
                        validators.get(generateValidatorKey(pField));
                lValidator.addRule(pRule);
            }
            else {
                FieldValidator lValidator = new FieldValidator(pField, pRule);
                lValidator.bind();
                lValidator.setValidator(this);
                pField.setFieldValidator(lValidator);
                validators.put(generateValidatorKey(pField), lValidator);
            }
        }
    }

    /**
     * Generate the key for the field in argument. To avoid getting the same key
     * for fields of the same multivalued field (they have the same field name),
     * the hash code of the object is used.
     * 
     * @param pField
     *            the field to get the key
     * @return the key for the validators map
     */
    private int generateValidatorKey(AbstractGpmField<?> pField) {
        return pField.hashCode();
    }

    /**
     * Remove all validation rules.
     */
    public void clear() {
        validators.clear();
    }

    /**
     * Insert a validator for the specified field
     * 
     * @param pField
     *            the field to add a validator on
     * @param pFieldValidator
     *            the validator to add
     */
    public void put(AbstractGpmField<?> pField, FieldValidator pFieldValidator) {
        validators.put(generateValidatorKey(pField), pFieldValidator);
    }
}