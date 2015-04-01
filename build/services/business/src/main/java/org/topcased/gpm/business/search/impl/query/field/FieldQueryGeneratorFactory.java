/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.search.impl.query.field;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.search.criterias.impl.VirtualFieldData;
import org.topcased.gpm.business.search.service.UsableFieldData;

/**
 * FieldQueryGeneratorFactory can create generator instance for a specified
 * field.
 * 
 * @author mkargbo
 */
public class FieldQueryGeneratorFactory {

    private static final FieldQueryGeneratorFactory INSTANCE =
            new FieldQueryGeneratorFactory();

    /**
     * Get the FieldQueryGeneratorFactory unique instance
     * 
     * @return FieldQueryGeneratorFactory instance
     */
    public static FieldQueryGeneratorFactory getInstance() {
        return INSTANCE;
    }

    /**
     * Get the generator according to the usable field type.
     * 
     * @param pUsableFieldData
     *            Usable field to analyze for generation
     * @return Field generator
     */
    public IFieldQueryGenerator getFieldQueryGenerator(
            UsableFieldData pUsableFieldData) {

        if (pUsableFieldData instanceof VirtualFieldData) {
            return getVirtualFieldQueryGenerator(
                    (VirtualFieldData) pUsableFieldData, true);
        }
        else {
            return getBasicFieldQueryGenerator(pUsableFieldData, true);
        }
    }

    public IFieldQueryGenerator getFieldQueryGeneratorFromMultipleField(
            UsableFieldData pUsableFieldData) {

        if (pUsableFieldData instanceof VirtualFieldData) {
            return getVirtualFieldQueryGenerator(
                    (VirtualFieldData) pUsableFieldData, false);
        }
        else {
            return getBasicFieldQueryGenerator(pUsableFieldData, false);
        }
    }

    /**
     * Get generator for field.
     * 
     * @param pUsableFieldData
     *            Usable field to analyze for generation
     * @return Field generator
     * @throws NotImplementedException
     *             If the field type is not supported
     */
    private IFieldQueryGenerator getBasicFieldQueryGenerator(
            UsableFieldData pUsableFieldData, boolean pHandleMultipleField)
        throws NotImplementedException {

        if (pHandleMultipleField
                && StringUtils.isNotBlank(pUsableFieldData.getMultipleField())) {
            return new MultipleFieldQueryGenerator(pUsableFieldData);
        }
        else {
            switch (pUsableFieldData.getFieldType()) {
                case SIMPLE_STRING_FIELD:
                    return new StringFieldQueryGenerator(pUsableFieldData);
                case SIMPLE_INTEGER_FIELD:
                    return new IntegerFieldQueryGenerator(pUsableFieldData);
                case SIMPLE_REAL_FIELD:
                    return new RealFieldQueryGenerator(pUsableFieldData);
                case SIMPLE_BOOLEAN_FIELD:
                    return new BooleanFieldQueryGenerator(pUsableFieldData);
                case SIMPLE_DATE_FIELD:
                    return new DateFieldQueryGenerator(pUsableFieldData);
                case ATTACHED_FIELD:
                    return new AttachedFieldQueryGenerator(pUsableFieldData);
                case CHOICE_FIELD:
                    return new ChoiceFieldQueryGenerator(pUsableFieldData);
                default:
                    throw new NotImplementedException("Type '"
                            + pUsableFieldData.getFieldType()
                            + "' for the field '"
                            + pUsableFieldData.getFieldName()
                            + "' is not yet supported");
            }
        }
    }

    /**
     * Get generator for virtual field
     * 
     * @param pVirtualFieldData
     *            Virtual field to analyze for generation
     * @return Field generator
     * @throws NotImplementedException
     *             If the field type is not supported
     */
    private IFieldQueryGenerator getVirtualFieldQueryGenerator(
            VirtualFieldData pVirtualFieldData, boolean pHandleMultipleField)
        throws NotImplementedException {
        switch (pVirtualFieldData.getVirtualFieldType()) {
            case $LINKED_SHEET_FIELD:
                return getBasicFieldQueryGenerator(pVirtualFieldData,
                        pHandleMultipleField);
            case $LINK_FIELD:
                return getBasicFieldQueryGenerator(pVirtualFieldData,
                        pHandleMultipleField);
            case $PRODUCT_FIELD:
                return getBasicFieldQueryGenerator(pVirtualFieldData,
                        pHandleMultipleField);
            case $ORIGIN_SHEET_REF:
                return new LinkedSheetReferenceFieldQueryGenerator(
                        pVirtualFieldData);
            case $DEST_SHEET_REF:
                return new LinkedSheetReferenceFieldQueryGenerator(
                        pVirtualFieldData);
            case $ORIGIN_PRODUCT:
                return new LinkedSheetProductNameFieldQueryGenerator(
                        pVirtualFieldData);
            case $DEST_PRODUCT:
                return new LinkedSheetProductNameFieldQueryGenerator(
                        pVirtualFieldData);
            case $SHEET_TYPE:
                return new TypeFieldQueryGenerator(pVirtualFieldData);
            case $SHEET_STATE:
                return new StateFieldQueryGenerator(pVirtualFieldData);
            case $SHEET_REFERENCE:
                return new ReferenceFieldQueryGenerator(pVirtualFieldData);
            case $PRODUCT_NAME:
                return new ProductNameFieldQueryGenerator(pVirtualFieldData);
            case $PRODUCT_DESCRIPTION:
                return new ProductDescriptionFieldQueryGenerator(
                        pVirtualFieldData);
            case $PRODUCT_HIERARCHY:
                return new ProductHierarchyFieldQueryGenerator(
                        pVirtualFieldData);
            default:
                throw new NotImplementedException("Type '"
                        + pVirtualFieldData.getVirtualFieldType()
                        + "' for the field '"
                        + pVirtualFieldData.getFieldName()
                        + "' is not yet supported");
        }
    }
}