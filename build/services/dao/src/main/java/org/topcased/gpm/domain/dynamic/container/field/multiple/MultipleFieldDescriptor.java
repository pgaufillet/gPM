/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.domain.dynamic.container.field.multiple;

import java.util.LinkedList;
import java.util.List;

import net.sf.cglib.core.ClassEmitter;

import org.objectweb.asm.FieldVisitor;
import org.topcased.gpm.domain.dynamic.ColumnDescriptor;
import org.topcased.gpm.domain.dynamic.container.field.FieldDescriptor;
import org.topcased.gpm.domain.dynamic.container.field.FieldDescriptorFactory;
import org.topcased.gpm.domain.fields.Field;
import org.topcased.gpm.domain.fields.MultipleField;

/**
 * Describe dynamic mapping used to represent a multiple field
 * 
 * @author tpanuel
 */
public class MultipleFieldDescriptor extends FieldDescriptor {
    private final List<ColumnDescriptor> columnDescriptors =
            new LinkedList<ColumnDescriptor>();

    /**
     * Describe the data model of a multiple field
     * 
     * @param pField
     *            The multiple field
     * @param pIsForRevision
     *            If it's a descriptor for a revision
     */
    public MultipleFieldDescriptor(MultipleField pField, boolean pIsForRevision) {
        super(pField, null);
        for (Field lSubField : pField.getFields()) {
            columnDescriptors.add(FieldDescriptorFactory.getFieldDescriptor(
                    lSubField, pIsForRevision));
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.dynamic.ColumnDescriptor#generateColumn(net.sf.cglib.core.ClassEmitter)
     */
    public FieldVisitor generateColumn(ClassEmitter pClassEmitter) {
        for (ColumnDescriptor lColumnDescriptor : columnDescriptors) {
            lColumnDescriptor.generateColumn(pClassEmitter);
        }
        // No field for a multiple field
        return null;
    }

    /**
     * Get subfields descriptor
     * 
     * @return Subfields descriptor
     */
    public List<ColumnDescriptor> getColumnDescriptors() {
        return columnDescriptors;
    }
}
