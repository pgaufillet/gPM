/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.domain.dynamic.container.field.multivalued;

import org.topcased.gpm.domain.dynamic.DynamicObjectGenerator;
import org.topcased.gpm.domain.dynamic.IdentifierDescriptor;
import org.topcased.gpm.domain.dynamic.container.field.FieldDescriptorFactory;
import org.topcased.gpm.domain.dynamic.container.sheet.DynamicSheetGenerator;
import org.topcased.gpm.domain.dynamic.util.DynamicObjectNamesUtils;
import org.topcased.gpm.domain.fields.Field;
import org.topcased.gpm.domain.fields.MultipleField;

/**
 * Generator of dynamic multi valued fields
 * 
 * @author tpanuel
 */
public class DynamicMultivaluedFieldGenerator extends
        DynamicObjectGenerator<DynamicMultivaluedField> {
    public final static String[] COLUMN_ID_INFO = { "ID", "getId", "setId" };

    public final static String COLUMN_PARENT_ID_INFO = "PARENT_ID";

    public final static String COLUMN_NUM_LINE_INFO = "NUM_LINE";

    private static final Source SOURCE =
            new Source(DynamicSheetGenerator.class.getName());

    private final String tableName;

    /**
     * Create a dynamic multi valued fields generator
     * 
     * @param pFieldType
     *            The type of the multi valued field
     * @param pIsForRevision
     *            If it's a table for revision
     */
    public DynamicMultivaluedFieldGenerator(Field pFieldType,
            boolean pIsForRevision) {
        super(SOURCE, DynamicMultivaluedField.class, pFieldType.getId(),
                pIsForRevision);
        if (pIsForRevision) {
            tableName =
                    DynamicObjectNamesUtils.getInstance().initDynamicRevisionTableName(
                            pFieldType);
        }
        else {
            tableName =
                    DynamicObjectNamesUtils.getInstance().initDynamicTableName(
                            pFieldType);
        }
        addColumn(new IdentifierDescriptor(COLUMN_ID_INFO[0], String.class,
                COLUMN_ID_INFO[1], COLUMN_ID_INFO[2]));
        if (pFieldType instanceof MultipleField) {
            for (Field lSubField : ((MultipleField) pFieldType).getFields()) {
                addColumn(FieldDescriptorFactory.getFieldDescriptor(lSubField,
                        pIsForRevision));
            }
        }
        else {
            addColumn(FieldDescriptorFactory.getSubFieldDescriptor(pFieldType,
                    pIsForRevision));
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.dynamic.DynamicObjectGenerator#getTableName()
     */
    protected String getTableName() {
        return tableName;
    }
}