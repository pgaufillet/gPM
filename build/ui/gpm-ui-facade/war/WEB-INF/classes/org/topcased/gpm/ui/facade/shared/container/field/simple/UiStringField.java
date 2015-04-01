/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.shared.container.field.simple;

import org.topcased.gpm.business.util.FieldType;
import org.topcased.gpm.business.util.StringDisplayHintType;
import org.topcased.gpm.business.values.field.BusinessField;
import org.topcased.gpm.business.values.field.simple.BusinessPointerField;
import org.topcased.gpm.business.values.field.simple.BusinessStringField;

/**
 * UiStringField
 * 
 * @author nveillet
 */
public class UiStringField extends UiSimpleField<String> implements
        BusinessStringField {
    private final static int DEFAULT_WIDTH = -1;

    private static final long serialVersionUID = 7374296455096394426L;

    private int height;

    private String internalUrlSheetReference;

    private int size;

    private StringDisplayHintType stringDisplayHintType;

    private int width;

    /**
     * Create new UiStringField
     */
    public UiStringField() {
        super(FieldType.STRING);
        stringDisplayHintType = StringDisplayHintType.SINGLE_LINE;
        width = DEFAULT_WIDTH;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.facade.shared.container.field.simple.UiSimpleField#copy(org.topcased.gpm.business.values.field.BusinessField)
     */
    @Override
    public void copy(BusinessField pOther) {
        super.copy(pOther);

        // INTERNAL_URL
        if (StringDisplayHintType.INTERNAL_URL.equals(stringDisplayHintType)
                && get() != null && !get().isEmpty()) {
            BusinessStringField lBusinessStringField =
                    getFinalBusinessStringField(pOther);
            if (lBusinessStringField != null) {
                internalUrlSheetReference =
                        lBusinessStringField.getInternalUrlSheetReference();
            }
        }
    }

    private BusinessStringField getFinalBusinessStringField(
            BusinessField pBusinessField) {
        if (pBusinessField instanceof BusinessStringField) {
            return (BusinessStringField) pBusinessField;
        }
        else if (pBusinessField instanceof BusinessPointerField) {
            BusinessField lPointedField =
                    ((BusinessPointerField) pBusinessField).getPointedField();
            return getFinalBusinessStringField(lPointedField);
        }
        else {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.shared.container.field.simple.UiSimpleField#get()
     */
    @Override
    public String get() {
        return value;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.shared.container.field.simple.UiSimpleField#getAsString()
     */
    @Override
    public String getAsString() {
        return get();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.facade.shared.container.field.UiField#getEmptyClone()
     */
    @Override
    public UiStringField getEmptyClone() {
        UiStringField lField = new UiStringField();
        lField.setFieldName(getFieldName());
        lField.setFieldDescription(getFieldDescription());
        lField.setMandatory(isMandatory());
        lField.setUpdatable(isUpdatable());

        lField.setStringDisplayHintType(stringDisplayHintType);
        lField.setSize(size);
        lField.setHeight(height);
        lField.setWidth(width);

        return lField;
    }

    /**
     * Get height
     * 
     * @return The height
     */
    public int getHeight() {
        return height;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessStringField#getInternalUrlSheetReference()
     */
    public String getInternalUrlSheetReference() {
        return internalUrlSheetReference;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.simple.BusinessStringField#getSize()
     */
    public int getSize() {
        return size;
    }

    /**
     * Get string display hint type
     * 
     * @return the string display hint type
     */
    public StringDisplayHintType getStringDisplayHintType() {
        return stringDisplayHintType;
    }

    /**
     * Get width
     * 
     * @return The width
     */
    public int getWidth() {
        return width;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.shared.container.field.simple.UiSimpleField#set(java.lang.Object)
     */
    @Override
    public void set(String pValue) {
        value = pValue;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.shared.container.field.simple.UiSimpleField#setAsString(java.lang.String)
     */
    @Override
    public void setAsString(String pValue) {
        value = pValue;
    }

    /**
     * Set the height
     * 
     * @param pHeight
     *            The height to set
     */
    public void setHeight(int pHeight) {
        height = pHeight;
    }

    /**
     * set internal sheet reference (for INTERNAL_URL display hint)
     * 
     * @param pInternalSheetReference
     *            the internal sheet reference to set
     */
    public void setInternalUrlSheetReference(String pInternalSheetReference) {
        internalUrlSheetReference = pInternalSheetReference;
    }

    /**
     * Set the the max size
     * 
     * @param pSize
     *            The size to set
     */
    public void setSize(int pSize) {
        this.size = pSize;
    }

    /**
     * Set the string display hint type
     * 
     * @param pStringDisplayHintType
     *            the string display hint type to set
     */
    public void setStringDisplayHintType(
            StringDisplayHintType pStringDisplayHintType) {
        stringDisplayHintType = pStringDisplayHintType;
    }

    /**
     * Set the width
     * 
     * @param pWidth
     *            The width to set
     */
    public void setWidth(int pWidth) {
        width = pWidth;
    }
}