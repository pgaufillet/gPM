/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.domain.dynamic.util;

import net.sf.cglib.core.ClassEmitter;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Type;

/**
 * Specific class emitter used to dynamic class generation
 * 
 * @author tpanuel
 */
public class GpmClassEmitter extends ClassEmitter {
    final private ClassVisitor classVisitor;

    /**
     * Class emitter use a class visitor for construct the class
     * 
     * @param pClassVisitor
     *            The class visitor
     */
    public GpmClassEmitter(ClassVisitor pClassVisitor) {
        super(pClassVisitor);
        classVisitor = pClassVisitor;
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.sf.cglib.core.ClassEmitter#visitField(int, java.lang.String,
     *      java.lang.String, java.lang.String, java.lang.Object)
     */
    @Override
    public FieldVisitor visitField(int pAccess, String pName, String pDesc,
            String pSignature, Object pValue) {
        return classVisitor.visitField(pAccess, pName,
                Type.getType(pDesc).getDescriptor(), null, pValue);
    }
}
