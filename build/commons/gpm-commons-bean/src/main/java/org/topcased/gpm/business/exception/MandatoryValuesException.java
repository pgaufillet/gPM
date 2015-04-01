/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin), Thomas Szadel
 * (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.exception;

import java.io.Serializable;
import java.util.Collection;

/**
 * Exception thrown when a sheet update (or creation) does not fill all
 * mandatory fields.
 * 
 * @author llatil
 */
public class MandatoryValuesException extends ConstraintException {

    /** Automatically generated UUID. */
    private static final long serialVersionUID = 8042343176013788324L;

    /** Default user message */
    private static final String DEFAULT_MANDATORY_ERROR_MSG =
            "All mandatory fields must be filled.";

    /**
     * Constructs a new exception.
     * 
     * @param pFields
     *            List of mandatory fields.
     */
    public MandatoryValuesException(Collection<FieldRef> pFields) {
        super("Mandatory fields not valued !");
        fields = pFields;
        setUserMessage(DEFAULT_MANDATORY_ERROR_MSG);
    }

    /**
     * Get the list of mandatory fields that must be valued.
     * 
     * @return List of mandatory fields left unvalued.
     */
    public Collection<FieldRef> getFields() {
        return fields;
    }

    /** The fields. */
    private final Collection<FieldRef> fields;

    /**
     * Reference to an unfilled mandatory field. This class is used to store the
     * label key of the field, and its translation.
     * 
     * @author llatil
     */
    public final static class FieldRef implements Serializable {

        /** serialVersionUID */
        private static final long serialVersionUID = 1L;

        /**
         * Constructs a new field reference.
         * 
         * @param pLabelKey
         *            Label key for the field
         */
        public FieldRef(String pLabelKey) {
            this(pLabelKey, pLabelKey);
        }

        /**
         * Constructs a new field reference.
         * 
         * @param pLabelKey
         *            Label key for the field
         * @param pI18nName
         *            Localized name of the field
         */
        public FieldRef(String pLabelKey, String pI18nName) {
            labelKey = pLabelKey;
            i18nName = pI18nName;
        }

        /**
         * Gets the label key.
         * 
         * @return the label key
         */
        public String getLabelKey() {
            return labelKey;
        }

        /**
         * Gets the name.
         * 
         * @return the name
         */
        public String getName() {
            return i18nName;
        }

        /**
         * Label key of this field. {@inheritDoc}
         * 
         * @see java.lang.Object#toString()
         */
        public String toString() {
            return getLabelKey();
        }

        /** The label key. */
        private final String labelKey;

        /** The translated field name. */
        private final String i18nName;
    }
}
