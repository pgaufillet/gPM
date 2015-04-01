/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.common.container;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.topcased.gpm.business.util.ChoiceDisplayHintType;
import org.topcased.gpm.business.values.field.BusinessField;
import org.topcased.gpm.ui.application.shared.exception.UIAttachmentException;
import org.topcased.gpm.ui.component.client.container.GpmFieldSet;
import org.topcased.gpm.ui.component.client.container.field.AbstractGpmField;
import org.topcased.gpm.ui.component.client.container.field.GpmUploadFileRegister;
import org.topcased.gpm.ui.component.client.util.validation.Validator;
import org.topcased.gpm.ui.facade.shared.container.UiContainer;
import org.topcased.gpm.ui.facade.shared.container.field.UiField;
import org.topcased.gpm.ui.facade.shared.container.field.multiple.UiMultipleField;
import org.topcased.gpm.ui.facade.shared.container.field.multivalued.UiMultivaluedField;
import org.topcased.gpm.ui.facade.shared.container.field.simple.UiChoiceField;

/**
 * A manager for a container : sheet, link, product or input data.
 * 
 * @author tpanuel
 */
public class UiFieldManager {
    private final List<UiField> editableFields;

    private final List<Validator> validators;

    private final List<GpmUploadFileRegister> uploadRegisters;

    private GpmFieldSet uiFieldSet;

    /**
     * Create a new UI field manager.
     */
    public UiFieldManager() {
        editableFields = new ArrayList<UiField>();
        validators = new ArrayList<Validator>();
        uploadRegisters = new ArrayList<GpmUploadFileRegister>();
    }

    /**
     * Initialize the UI field manager.
     * 
     * @param pUiFieldSet
     *            The UI field set.
     */
    public void init(final GpmFieldSet pUiFieldSet) {
        uiFieldSet = pUiFieldSet;
        editableFields.clear();
        validators.clear();
    }

    /**
     * Tell if the field is editable (or the subfields it contains) if any
     * 
     * @param pField
     *            the field to test
     * @return <code>true</code> if the field or at least one of its subfields
     *         is editable
     */
    private boolean isFieldOrSubFieldsEditable(UiField pField) {
        if (pField.isUpdatable()) {
            return true;
        }
        else if (pField instanceof UiMultivaluedField) {
            for (BusinessField lSubField : ((UiMultivaluedField) pField)) {
                if (isFieldOrSubFieldsEditable((UiField) lSubField)) {
                    return true;
                }
            }
        }
        else if (pField instanceof UiMultipleField) {
            for (BusinessField lSubField : (UiMultipleField) pField) {
                if (isFieldOrSubFieldsEditable((UiField) lSubField)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Create a group.
     * 
     * @param pContainer
     *            The container.
     * @param pFieldNames
     *            The field names.
     * @param pAttachedFieldsInError
     *            The Attached fields in error
     * @return The group creation handler.
     */
    public UiFieldCreationHandler buildGroup(final UiContainer pContainer,
            final List<String> pFieldNames,
            List<UIAttachmentException> pAttachedFieldsInError) {
        final UiFieldCreationHandler lHandler = new UiFieldCreationHandler();

        for (final String lFieldName : pFieldNames) {
            final UiField lField = (UiField) pContainer.getField(lFieldName);

            if (lField != null) {
                if (isFieldOrSubFieldsEditable(lField)) {
                    editableFields.add(lField);
                }
                lHandler.addUiField(lField);
            }
        }
        validators.add(lHandler.getValidator());
        uploadRegisters.add(lHandler.getUploadRegister());
        lHandler.setAttachedFieldsInError(pAttachedFieldsInError);
        return lHandler;
    }

    /**
     * Build the handler for the fields contained in the link container
     * 
     * @param pLinkContainer
     *            The container of the link fields
     * @return the UiFieldCreationHandler for the link fields
     */
    public UiFieldCreationHandler buildLink(final UiContainer pLinkContainer) {
        final UiFieldCreationHandler lHandler = new UiFieldCreationHandler();

        for (final BusinessField lBusinessField : pLinkContainer) {
            final UiField lField = (UiField) lBusinessField;
            if (lField != null) {
                if (isFieldOrSubFieldsEditable(lField)) {
                    editableFields.add(lField);
                }
                lHandler.addUiField(lField);
            }
        }
        validators.add(lHandler.getValidator());
        uploadRegisters.add(lHandler.getUploadRegister());

        return lHandler;
    }

    /**
     * Get the update fields.
     * 
     * @return The updated fields.
     */
    public List<UiField> getUpdatedFields() {
        final List<UiField> lUpdatedFields = new ArrayList<UiField>();

        // Only the editable field can be updated
        for (UiField lEditableField : editableFields) {
            final AbstractGpmField<?> lDisplayedField =
                    uiFieldSet.getDisplayedFields().get(
                            lEditableField.getFieldName());

            if (lDisplayedField == null) {
                // The field is on a closed displayGroup.
                // To be coherent with open displayGroup, non displayed mono-valued UiChoiceField
                // needs to be searched and the first category needs to be set for them.
                // Indeed, the HMI selects by itself the first category of
                // mono-valued UiChoiceField when the displayGroup is opened
                // without any user's modification.
                boolean lIsFieldModified = false;
                if (lEditableField instanceof UiChoiceField) {
                    lIsFieldModified =
                            updateChoiceFields((UiChoiceField) lEditableField);
                }
                else if (lEditableField instanceof UiMultipleField) {
                    lIsFieldModified =
                            updateChoiceFieldsInMultipleField((UiMultipleField) lEditableField);
                }
                else if (lEditableField instanceof UiMultivaluedField) {
                    lIsFieldModified =
                            updateChoiceFieldsInMultivaluedField((UiMultivaluedField) lEditableField);
                }

                // If the field has been modified because a(many) choiceField(s) were
                // modified, it is added to the updatedField list
                if (lIsFieldModified) {
                    addUpdatedField(lUpdatedFields, lEditableField,
                            lEditableField);
                }
            }
            else {
                // The field is on an opened displayGroup
                if (!lEditableField.hasSameValues(lDisplayedField)) {
                    addUpdatedField(lUpdatedFields, lEditableField,
                            lDisplayedField);
                }
            }
        }
        return lUpdatedFields;
    }

    /**
     * Update mono-valued choice fields included in a non displayed multivalued
     * field. Only multivalued field with a template field of type
     * <code>UiMultipleField</code> are taken into account. Other multivalued
     * field work correctly.
     * 
     * @param pUiMultivaluedField
     *            the multivalued field to analyze
     * @return true is the field is modified / false otherwise
     */
    private boolean updateChoiceFieldsInMultivaluedField(
            final UiMultivaluedField pUiMultivaluedField) {
        boolean lIsFieldModified = false;
        if (pUiMultivaluedField.getTemplateField() instanceof UiMultipleField) {
            final Iterator<BusinessField> lIterator =
                    pUiMultivaluedField.iterator();
            while (lIterator.hasNext()) {
                final BusinessField lBusinessField = lIterator.next();
                if (lBusinessField instanceof UiMultipleField) {
                    lIsFieldModified =
                            updateChoiceFieldsInMultipleField((UiMultipleField) lBusinessField);
                }
            }
        }
        return lIsFieldModified;
    }

    /**
     * Update mono-valued choice fields included in a non displayed multiple
     * field
     * 
     * @param pUiMultipleField
     *            the multiple field to analyze
     * @return true is the multiple field is modified / false otherwise
     */
    private boolean updateChoiceFieldsInMultipleField(
            final UiMultipleField pUiMultipleField) {
        final Iterator<BusinessField> lIterator = pUiMultipleField.iterator();
        boolean lIsFieldModified = false;
        while (lIterator.hasNext()) {
            final BusinessField lBusinessField = lIterator.next();
            if (lBusinessField instanceof UiChoiceField) {
                lIsFieldModified =
                        updateChoiceFields((UiChoiceField) lBusinessField);
            }
        }
        return lIsFieldModified;
    }

    /**
     * Update a non displayed choiceField if it has no value yet.
     * 
     * @param pUiChoiceField
     *            the non displayed choiceField to modify
     * @return true is the field is modified / false otherwise
     */
    private boolean updateChoiceFields(final UiChoiceField pUiChoiceField) {
        boolean lIsFieldModified = false;
        if (pUiChoiceField.getCategoryValue() == null
                && pUiChoiceField.getPossibleValues() != null
                && pUiChoiceField.getPossibleValues().size() > 0
                && !pUiChoiceField.getChoiceDisplayHintType().equals(
                        ChoiceDisplayHintType.NOT_LIST)) {
            final String lNewValue =
                    pUiChoiceField.getPossibleValues().get(0).getValue();
            if (lNewValue != null) {
                pUiChoiceField.setCategoryValue(lNewValue);
                lIsFieldModified = true;
            }
        }
        return lIsFieldModified;
    }

    /**
     * Add the modified <code>pDisplayedField</code> to the list
     * <code>pUdatedFields</code>
     * 
     * @param pUpdatedFields
     *            the list of UiFields updated in the sheet
     * @param pEditableField
     *            the field to copy from
     * @param pDisplayedField
     *            the modified field to copy into the list
     */
    private void addUpdatedField(final List<UiField> pUpdatedFields,
            final UiField pEditableField, final BusinessField pDisplayedField) {
        final UiField lClone = pEditableField.getEmptyClone();
        lClone.copy(pDisplayedField);
        pUpdatedFields.add(lClone);
    }

    /**
     * Validate a form.
     * 
     * @return The list of errors.
     */
    public List<String> validate() {
        final List<String> lErrors = new ArrayList<String>();

        // Test all fields of all groups
        for (Validator lValidator : validators) {
            final String lMessage = lValidator.validate();

            if (lMessage != null && lMessage.length() > 0) {
                lErrors.add(lMessage);
            }
        }

        return lErrors;
    }

    /**
     * Get the upload registers.
     * 
     * @return The upload register.
     */
    public List<GpmUploadFileRegister> getUploadRegisters() {
        return uploadRegisters;
    }
}