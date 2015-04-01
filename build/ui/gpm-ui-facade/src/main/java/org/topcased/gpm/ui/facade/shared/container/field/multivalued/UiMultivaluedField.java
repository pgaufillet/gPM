/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.shared.container.field.multivalued;

import java.util.Iterator;
import java.util.LinkedList;

import org.topcased.gpm.business.util.FieldType;
import org.topcased.gpm.business.values.field.BusinessField;
import org.topcased.gpm.business.values.field.multivalued.BusinessMultivaluedField;
import org.topcased.gpm.business.values.field.simple.BusinessPointerField;
import org.topcased.gpm.ui.facade.shared.container.field.UiField;
import org.topcased.gpm.ui.facade.shared.exception.NotImplementedException;

/**
 * UiMultivaluedField
 * 
 * @author nveillet
 */
public class UiMultivaluedField extends UiField implements
        BusinessMultivaluedField<BusinessField> {

    /** serialVersionUID */
    private static final long serialVersionUID = 737730373630926620L;

    private LinkedList<BusinessField> fields;

    private UiField templateField;

    /**
     * Empty constructor for serialization
     * 
     * @see #UiMultivaluedField(UiField)
     */
    public UiMultivaluedField() {
        super(FieldType.MULTIVALUED);
    }

    /**
     * Constructor
     * 
     * @param pTemplateField
     *            the template field
     */
    public UiMultivaluedField(UiField pTemplateField) {
        super(FieldType.MULTIVALUED);

        setFieldName(pTemplateField.getFieldName());
        setFieldDescription(pTemplateField.getFieldDescription());
        setMandatory(pTemplateField.isMandatory());
        setUpdatable(pTemplateField.isUpdatable());

        fields = new LinkedList<BusinessField>();
        templateField = pTemplateField;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.multivalued.BusinessMultivaluedField#addLine()
     */
    @Override
    public BusinessField addLine() {
        fields.addLast(templateField.getEmptyClone());
        return fields.getLast();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.facade.shared.container.field.UiField#copy(org.topcased.gpm.business.values.field.BusinessField)
     */
    @SuppressWarnings("unchecked")
    @Override
    public void copy(BusinessField pOther) {
        BusinessMultivaluedField<BusinessField> lOther =
                (BusinessMultivaluedField<BusinessField>) pOther;

        fields.clear();

        for (BusinessField lOtherField : lOther) {
            //If the field is a BusinessPointerField we must copy the pointed field not the pointer
            if (lOtherField instanceof BusinessPointerField) {
                //We are in a pointer field, so the field cannot be updated
                this.setUpdatable(false);
                //If the pointed field is multivalued
                if (((BusinessPointerField) lOtherField).getPointedField() instanceof BusinessMultivaluedField<?>) {
                    //copy all sub-fields (pointed fields)
                    for (BusinessField lBusinessField : ((BusinessMultivaluedField<?>) ((BusinessPointerField) lOtherField).getPointedField())) {
                        addLine().copy(lBusinessField);
                    }
                }
            }
            else {

                addLine().copy(lOtherField);
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.multivalued.BusinessMultivaluedField#get(int)
     */
    @Override
    public BusinessField get(int pIndex) {
        return fields.get(pIndex);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.facade.shared.container.field.UiField#getAsString()
     */
    @Override
    public String getAsString() {
        throw new NotImplementedException(
                "getAsString is not implemented, use get method.");
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.facade.shared.container.field.UiField#getEmptyClone()
     */
    @Override
    public UiMultivaluedField getEmptyClone() {
        UiMultivaluedField lField = new UiMultivaluedField(templateField);
        lField.setFieldName(getFieldName());
        lField.setFieldDescription(getFieldDescription());
        lField.setMandatory(isMandatory());
        lField.setUpdatable(isUpdatable());
        return lField;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.multivalued.BusinessMultivaluedField#getFirst()
     */
    @Override
    public BusinessField getFirst() {
        return fields.getFirst();
    }

    /**
     * Get the template field
     * 
     * @return the template field
     */
    public UiField getTemplateField() {
        return templateField;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.facade.shared.container.field.UiField#hasSameValues(org.topcased.gpm.business.values.field.BusinessField)
     */
    @SuppressWarnings("unchecked")
    @Override
    public boolean hasSameValues(BusinessField pOther) {
        final BusinessMultivaluedField<BusinessField> lOther =
                (BusinessMultivaluedField<BusinessField>) pOther;

        // The same of number of line is needed
        if (fields.size() != lOther.size()) {
            return false;
        }
        // All elements must have same values
        for (int i = 0; i < fields.size(); i++) {
            if (!fields.get(i).hasSameValues(lOther.get(i))) {
                return false;
            }
        }

        return true;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator<BusinessField> iterator() {
        return fields.iterator();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.multivalued.BusinessMultivaluedField#removeLine()
     */
    @Override
    public BusinessField removeLine() {
        return fields.removeLast();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.multivalued.BusinessMultivaluedField#removeLine(int)
     */
    @Override
    public BusinessField removeLine(int pIndex) {
        return fields.remove(pIndex);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.multivalued.BusinessMultivaluedField#size()
     */
    @Override
    public int size() {
        return fields.size();
    }
}
