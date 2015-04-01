/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.domain.dynamic.container;

import org.topcased.gpm.domain.dynamic.DynamicObjectGenerator;
import org.topcased.gpm.domain.dynamic.container.field.FieldDescriptorFactory;
import org.topcased.gpm.domain.dynamic.util.DynamicObjectNamesUtils;
import org.topcased.gpm.domain.fields.Field;
import org.topcased.gpm.domain.fields.FieldsContainer;
import org.topcased.gpm.domain.fields.ValuesContainer;

/**
 * Generator of dynamic values container
 * 
 * @author tpanuel
 * @param <MAPPED_CONTAINER>
 *            The generated object is at less a Values Container
 */
public abstract class DynamicValuesContainerGenerator<MAPPED_CONTAINER extends ValuesContainer>
        extends DynamicObjectGenerator<MAPPED_CONTAINER> {
    private final String tableName;

    /**
     * Create a generator of dynamic valuesF container
     * 
     * @param pSource
     *            The source
     * @param pContainerType
     *            The fields container
     * @param pSuperClass
     *            The super class (overriding ValuesContainer)
     * @param pIsForRevision
     *            If it's a table for revision
     */
    public DynamicValuesContainerGenerator(Source pSource,
            FieldsContainer pContainerType,
            Class<MAPPED_CONTAINER> pSuperClass, boolean pIsForRevision) {
        super(pSource, pSuperClass, pContainerType.getId(), pIsForRevision);
        if (pIsForRevision) {
            tableName =
                    DynamicObjectNamesUtils.getInstance().initDynamicRevisionTableName(
                            pContainerType);
        }
        else {
            tableName =
                    DynamicObjectNamesUtils.getInstance().initDynamicTableName(
                            pContainerType);
        }
        for (Field lField : pContainerType.getFields()) {
            // Generate only top level fields
            if (!lField.isSubfield()) {
                addColumn(FieldDescriptorFactory.getFieldDescriptor(lField,
                        pIsForRevision));
            }
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