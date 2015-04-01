/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Anne Haugommard(Atos Origin), Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.search.criterias.impl;

import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.fields.FieldType;
import org.topcased.gpm.business.search.service.UsableFieldData;
import org.topcased.gpm.business.values.field.virtual.VirtualFieldType;
import org.topcased.gpm.util.lang.CopyUtils;

/**
 * class defining data of a virtual field
 * 
 * @author ahaugomm
 */
public class VirtualFieldData extends UsableFieldData {

    /**
     * Automatically generated UID
     */
    private static final long serialVersionUID = -6738334384350704931L;

    public static final VirtualFieldData SHEET_TYPE_VIRTUAL_FIELD;

    public static final VirtualFieldData SHEET_STATE_VIRTUAL_FIELD;

    public static final VirtualFieldData SHEET_REFERENCE_VIRTUAL_FIELD;

    public static final VirtualFieldData PRODUCT_NAME_VIRTUAL_FIELD;

    public static final VirtualFieldData PRODUCT_DESCRIPTION_VIRTUAL_FIELD;

    public static final VirtualFieldData PRODUCT_HIERARCHY_VIRTUAL_FIELD;

    public static final VirtualFieldData ORIGIN_SHEET_REFERENCE_VIRTUAL_FIELD;

    public static final VirtualFieldData DEST_SHEET_REFERENCE_VIRTUAL_FIELD;

    public static final VirtualFieldData ORIGIN_PRODUCT_NAME_VIRTUAL_FIELD;

    public static final VirtualFieldData DEST_PRODUCT_NAME_VIRTUAL_FIELD;

    static {
        SHEET_TYPE_VIRTUAL_FIELD =
                CopyUtils.getImmutableCopy(new VirtualFieldData(
                        VirtualFieldType.$SHEET_TYPE.getValue(),
                        FieldType.CHOICE_FIELD, VirtualFieldType.$SHEET_TYPE));
        SHEET_STATE_VIRTUAL_FIELD =
                CopyUtils.getImmutableCopy(new VirtualFieldData(
                        VirtualFieldType.$SHEET_STATE.getValue(),
                        FieldType.CHOICE_FIELD, VirtualFieldType.$SHEET_STATE));
        SHEET_REFERENCE_VIRTUAL_FIELD =
                CopyUtils.getImmutableCopy(new VirtualFieldData(
                        VirtualFieldType.$SHEET_REFERENCE.getValue(),
                        FieldType.SIMPLE_STRING_FIELD,
                        VirtualFieldType.$SHEET_REFERENCE));
        PRODUCT_NAME_VIRTUAL_FIELD =
                CopyUtils.getImmutableCopy(new VirtualFieldData(
                        VirtualFieldType.$PRODUCT_NAME.getValue(),
                        FieldType.CHOICE_FIELD, VirtualFieldType.$PRODUCT_NAME));
        PRODUCT_DESCRIPTION_VIRTUAL_FIELD =
                CopyUtils.getImmutableCopy(new VirtualFieldData(
                        VirtualFieldType.$PRODUCT_DESCRIPTION.getValue(),
                        FieldType.SIMPLE_STRING_FIELD,
                        VirtualFieldType.$PRODUCT_DESCRIPTION));

        PRODUCT_HIERARCHY_VIRTUAL_FIELD =
                CopyUtils.getImmutableCopy(new VirtualFieldData(
                        VirtualFieldType.$PRODUCT_HIERARCHY.getValue(),
                        FieldType.CHOICE_FIELD,
                        VirtualFieldType.$PRODUCT_HIERARCHY));
        ORIGIN_SHEET_REFERENCE_VIRTUAL_FIELD =
                CopyUtils.getImmutableCopy(new VirtualFieldData(
                        VirtualFieldType.$ORIGIN_SHEET_REF.getValue(),
                        FieldType.SIMPLE_STRING_FIELD,
                        VirtualFieldType.$ORIGIN_SHEET_REF));
        DEST_SHEET_REFERENCE_VIRTUAL_FIELD =
                CopyUtils.getImmutableCopy(new VirtualFieldData(
                        VirtualFieldType.$DEST_SHEET_REF.getValue(),
                        FieldType.SIMPLE_STRING_FIELD,
                        VirtualFieldType.$DEST_SHEET_REF));
        ORIGIN_PRODUCT_NAME_VIRTUAL_FIELD =
                CopyUtils.getImmutableCopy(new VirtualFieldData(
                        VirtualFieldType.$ORIGIN_PRODUCT.getValue(),
                        FieldType.CHOICE_FIELD,
                        VirtualFieldType.$ORIGIN_PRODUCT));
        DEST_PRODUCT_NAME_VIRTUAL_FIELD =
                CopyUtils.getImmutableCopy(new VirtualFieldData(
                        VirtualFieldType.$DEST_PRODUCT.getValue(),
                        FieldType.CHOICE_FIELD, VirtualFieldType.$DEST_PRODUCT));
    }

    /**
     * The field type of the virtual field
     */
    private VirtualFieldType virtualFieldType;

    /**
     * empty constructor
     */
    public VirtualFieldData() {

    }

    /**
     * Constructor
     * 
     * @param pLabelKey
     *            Label key of the virtual field (usually value of virtual field
     *            type)
     * @param pFieldType
     *            Field type
     * @param pVirtualFieldType
     *            Virtual field type
     */
    public VirtualFieldData(final String pLabelKey, final FieldType pFieldType,
            final VirtualFieldType pVirtualFieldType) {
        super(pFieldType, StringUtils.EMPTY, StringUtils.EMPTY,
                new ArrayList<String>(0), StringUtils.EMPTY, false, -1, false,
                StringUtils.EMPTY, 0, new ArrayList<FilterFieldsContainerInfo>(
                        0));
        virtualFieldType = pVirtualFieldType;
        setFieldName(pLabelKey);
    }

    public VirtualFieldType getVirtualFieldType() {
        return virtualFieldType;
    }

    public void setVirtualFieldType(VirtualFieldType pVirtualFieldType) {
        virtualFieldType = pVirtualFieldType;
    }
}
