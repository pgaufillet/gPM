/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.exception;

/**
 * Exception threw when a field is not valid. Additional informations: 'field's
 * label key' and 'constraint that is not satisfy'
 * 
 * @author mkargbo
 */
public class FieldValidationException extends ConstraintException {

    /** serialVersionUID */
    private static final long serialVersionUID = -8072727796831435652L;

    /** Default user message */
    private static final String DEFAULT_VALIDATION_ERROR_MSG =
            "A field is not valid.";

    private String fieldLabelKey;

    /**
     * Constructs a field's validation exception
     * 
     * @param pFieldLabelKey
     *            Label key of the field
     * @param pConstraint
     *            Constraint that should be satisfy
     * @param pErrorMessage
     *            Error message
     */
    public FieldValidationException(String pFieldLabelKey, String pConstraint,
            ErrorMessage pErrorMessage) {
        super("The " + pFieldLabelKey + " field " + " can contain only "
                + pConstraint + " characters.", pConstraint);
        fieldLabelKey = pFieldLabelKey;
        setUserMessage("The " + pFieldLabelKey + " field "
                + " can contain only " + pConstraint + " characters.");
    }

    /**
     * Constructs a field's validation exception
     * 
     * @param pFieldLabelKey
     *            Label key of the field
     * @param pConstraint
     *            Constraint that should be satisfy
     * @param pErrorMessage
     *            Error message
     * @param pCause
     *            Cause
     */
    public FieldValidationException(String pFieldLabelKey,
            ErrorMessage pErrorMessage, String pConstraint, Throwable pCause) {
        super(pErrorMessage.value, pConstraint, pCause);
        fieldLabelKey = pFieldLabelKey;
        setUserMessage(DEFAULT_VALIDATION_ERROR_MSG);
    }

    public String getFieldLabelKey() {
        return fieldLabelKey;
    }

    public void setFieldLabelKey(String pFieldLabelKey) {
        fieldLabelKey = pFieldLabelKey;
    }

    /**
     * Defined error message.
     * <p>
     * Those error message use identifier for UI translation.
     * </p>
     * 
     * @author mkargbo
     */
    public enum ErrorMessage {
        MAX_SIZE("exception.field.maxsize");

        private String value;

        private ErrorMessage(String pValue) {
            value = pValue;
        }

        public String getValue() {
            return value;
        }
    }
}