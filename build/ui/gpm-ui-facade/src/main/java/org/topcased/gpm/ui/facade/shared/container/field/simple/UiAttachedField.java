/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien LOUISY (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.shared.container.field.simple;

import org.topcased.gpm.business.util.AttachedDisplayHintType;
import org.topcased.gpm.business.util.FieldType;
import org.topcased.gpm.business.values.field.BusinessField;
import org.topcased.gpm.business.values.field.simple.BusinessAttachedField;
import org.topcased.gpm.ui.facade.shared.container.field.UiField;
import org.topcased.gpm.ui.facade.shared.util.GpmStringUtils;

/**
 * UiAttachedField
 * 
 * @author jlouisy
 */
public class UiAttachedField extends UiField implements BusinessAttachedField {

    /** serialVersionUID */
    private static final long serialVersionUID = -2042691525881135532L;

    private AttachedDisplayHintType attachedDisplayHintType;

    private byte[] content;

    private String fileName;

    private int height;

    private String id;

    private String mimeType;

    private int width;

    /**
     * Create new UiAttachedField
     */
    public UiAttachedField() {
        super(FieldType.ATTACHED);
        attachedDisplayHintType = AttachedDisplayHintType.ATTACHED;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.facade.shared.container.field.UiField#copy(org.topcased.gpm.business.values.field.BusinessField)
     */
    @Override
    public void copy(BusinessField pOther) {
        if (pOther instanceof BusinessAttachedField) {
            final BusinessAttachedField lOtherAttachedField =
                    (BusinessAttachedField) pOther;
            if (lOtherAttachedField.getFileName() != null
                    && !lOtherAttachedField.getFileName().equals("")) {
                id = lOtherAttachedField.getId();
            }
            fileName = lOtherAttachedField.getFileName();
            mimeType = lOtherAttachedField.getMimeType();
            // Don't copy content from business
            // TODO : copy from UI
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.facade.shared.container.field.UiField#getAsString()
     */
    @Override
    public String getAsString() {
        return fileName;
    }

    public AttachedDisplayHintType getAttachedDisplayHintType() {
        return attachedDisplayHintType;
    }

    @Override
    public byte[] getContent() {
        return content;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.facade.shared.container.field.UiField#getEmptyClone()
     */
    @Override
    public UiField getEmptyClone() {
        UiAttachedField lField = new UiAttachedField();
        lField.setFieldName(getFieldName());
        lField.setFieldDescription(getFieldDescription());
        lField.setMandatory(isMandatory());
        lField.setUpdatable(isUpdatable());
        lField.setId(id);

        lField.setHeight(height);
        lField.setWidth(width);
        lField.setAttachedDisplayHintType(attachedDisplayHintType);
        return lField;
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getMimeType() {
        return mimeType;
    }

    public int getWidth() {
        return width;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.facade.shared.container.field.UiField#hasSameValues(BusinessField)
     */
    @Override
    public boolean hasSameValues(BusinessField pOther) {
        final BusinessAttachedField lOtherAttached =
                (BusinessAttachedField) pOther;
        if (GpmStringUtils.getEmptyIfNull(id).equals(
                GpmStringUtils.getEmptyIfNull(lOtherAttached.getId()))) {
            if (GpmStringUtils.getEmptyIfNull(getFileName()).equals(
                    GpmStringUtils.getEmptyIfNull(lOtherAttached.getFileName()))) {
                return true;
            }
        }
        return false;
    }

    public void setAttachedDisplayHintType(
            AttachedDisplayHintType pAttachedDisplayHintType) {
        attachedDisplayHintType = pAttachedDisplayHintType;
    }

    @Override
    public void setContent(byte[] pContent) {
        content = pContent;
    }

    @Override
    public void setFileName(String pFileName) {
        fileName = pFileName;
    }

    public void setHeight(int pHeight) {
        height = pHeight;
    }

    @Override
    public void setId(String pId) {
        id = pId;
    }

    @Override
    public void setMimeType(String pMimeType) {
        mimeType = pMimeType;
    }

    public void setWidth(int pWidth) {
        width = pWidth;
    }

}
