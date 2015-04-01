/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Atos Origin
 ******************************************************************/
package org.topcased.gpm.business.serialization.converter;

import org.topcased.gpm.business.serialization.data.Lock;
import org.topcased.gpm.business.serialization.data.Lock.LockScopeEnumeration;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * The Class LockConverter.
 * 
 * @author llatil
 */
public class LockConverter implements Converter {

    /**
     * {@inheritDoc}
     * 
     * @see com.thoughtworks.xstream.converters.Converter#marshal(java.lang.Object,
     *      com.thoughtworks.xstream.io.HierarchicalStreamWriter,
     *      com.thoughtworks.xstream.converters.MarshallingContext)
     */
    public void marshal(Object pValue, HierarchicalStreamWriter pWriter,
            MarshallingContext pCtx) {
        Lock lLock = (Lock) pValue;

        pWriter.startNode("lock");
        pWriter.addAttribute("owner", lLock.getOwner());
        pWriter.addAttribute("lockType", lLock.getType().toString());
        pWriter.endNode();
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.thoughtworks.xstream.converters.Converter#unmarshal(com.thoughtworks.xstream.io.HierarchicalStreamReader,
     *      com.thoughtworks.xstream.converters.UnmarshallingContext)
     */
    public Object unmarshal(HierarchicalStreamReader pReader,
            UnmarshallingContext pArg1) {
        return new Lock(pReader.getAttribute("owner"),
                pReader.getAttribute("lockType"),
                LockScopeEnumeration.PERMANENT);
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.thoughtworks.xstream.converters.ConverterMatcher#canConvert(java.lang.Class)
     */
    @SuppressWarnings("rawtypes")
    public boolean canConvert(Class pClass) {
        return pClass.equals(Lock.class);
    }
}
