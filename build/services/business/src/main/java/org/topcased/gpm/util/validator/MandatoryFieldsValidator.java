/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.util.validator;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.fields.impl.CacheableFieldsContainer;
import org.topcased.gpm.business.fields.impl.CacheableValuesContainer;
import org.topcased.gpm.business.serialization.data.AttachedField;
import org.topcased.gpm.business.serialization.data.AttachedFieldValueData;
import org.topcased.gpm.business.serialization.data.ChoiceField;
import org.topcased.gpm.business.serialization.data.Field;
import org.topcased.gpm.business.serialization.data.FieldValueData;
import org.topcased.gpm.business.serialization.data.MultipleField;
import org.topcased.gpm.business.serialization.data.SimpleField;
import org.topcased.gpm.util.iterator.GpmIterator;

/**
 * Use to mandatory fields without values and validate values container
 * 
 * @author tpanuel
 */
public class MandatoryFieldsValidator {
    /**
     * Find mandatory fields without values
     * 
     * @param pFieldsContainer
     *            The field container
     * @param pValuesContainer
     *            The values container
     * @return The mandatory fields without values
     */
    public final static Set<String> getEmptyMandatoryFields(
            CacheableFieldsContainer pFieldsContainer,
            CacheableValuesContainer pValuesContainer) {
        final Set<String> lEmptyMandatoryFields = new HashSet<String>();

        // Check all fields of a container
        for (Field lField : pFieldsContainer.getFields()) {
            // Check only mandatory fields
            if (isMandatoryField(lField)) {
                checkMandatoryField(lField,
                        pValuesContainer.getValue(lField.getLabelKey()),
                        lEmptyMandatoryFields);
            }
        }

        return lEmptyMandatoryFields;
    }

    private final static boolean isMandatoryField(Field pField) {
        if (pField instanceof MultipleField) {
            final MultipleField lMultipleField = (MultipleField) pField;

            // Multiple field mandatory if one of this sun field is mandatory
            for (Field lSubField : lMultipleField.getFields()) {
                if (isMandatoryField(lSubField)) {
                    return true;
                }
            }

            return false;
        }
        else {
            return pField.isMandatory();
        }
    }

    private final static void checkMandatoryField(Field pField, Object pValues,
            Set<String> pEmptyMandatoryFields) {
        if (pField.isMultivalued()) {
            // A null object is not updated -> mandatory values are not checked
            if (pValues != null) {
                final GpmIterator<Object> lFieldValues =
                        new GpmIterator<Object>(pValues);

                // All the values of a multi valued field must not be empty
                while (lFieldValues.hasNext()) {
                    checkMandatoryMonovaluedField(pField, lFieldValues.next(),
                            pEmptyMandatoryFields);
                }
            }
        }
        else {
            // Check mono valued field
            checkMandatoryMonovaluedField(pField, pValues,
                    pEmptyMandatoryFields);
        }
    }

    @SuppressWarnings("unchecked")
    private final static void checkMandatoryMonovaluedField(Field pField,
            Object pValues, Set<String> pEmptyMandatoryFields) {
        // A null object is not updated -> mandatory values are not checked
        if (pValues != null) {
            // Pointer fields cannot be mandatory
            if (!pField.isPointerField()) {
                if (pField instanceof MultipleField) {
                    // Multiple fields cannot be mandatory, but sub-field could
                    final MultipleField lMultipleField = (MultipleField) pField;
                    final Map<String, Object> lMultipleValues =
                            (Map<String, Object>) pValues;

                    for (Field lSubField : lMultipleField.getFields()) {
                        // Check sub field only if mandatory
                        if (lSubField.isMandatory()) {
                            // Sub field can be multi valued too
                            checkMandatoryField(lSubField,
                                    lMultipleValues.get(pField.getLabelKey()),
                                    pEmptyMandatoryFields);
                        }
                    }
                }
                else if (pField instanceof SimpleField) {
                    // Simple fields are empty if the value is NULL or EMPTY
                    final FieldValueData lSimpleValue =
                            (FieldValueData) pValues;

                    if (lSimpleValue.getValue() == null
                            || lSimpleValue.getValue().equals(StringUtils.EMPTY)) {
                        // Add simple field on the empty mandatory fields list 
                        pEmptyMandatoryFields.add(pField.getLabelKey());
                    }
                }
                else if (pField instanceof ChoiceField) {
                    // Choice fields are empty if the value is NULL
                    final FieldValueData lChoiceValue =
                            (FieldValueData) pValues;

                    if (lChoiceValue.getValue() == null) {
                        // Add choice field on the empty mandatory fields list 
                        pEmptyMandatoryFields.add(pField.getLabelKey());
                    }
                }
                else if (pField instanceof AttachedField) {
                    // Attached fields are empty if the file name is NULL or EMPTY
                    final AttachedFieldValueData lAttachedValue =
                            (AttachedFieldValueData) pValues;

                    if (lAttachedValue.getFilename() == null
                            || lAttachedValue.getFilename().equals(
                                    StringUtils.EMPTY)) {
                        // Add attached field on the empty mandatory fields list 
                        pEmptyMandatoryFields.add(pField.getLabelKey());
                    }
                }
            }
        }
    }
}