/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available 
 * under the terms of the Lesser Gnu Public License (LGPL)which 
 * accompanies this distribution, and is available 
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin), Sophian Idjellidaine (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.serialization.converter;

import org.topcased.gpm.business.serialization.data.Message;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * Convert a Message to and from XML.
 * 
 * @author sidjelli
 */
public class MessageConverter implements Converter {

    /**
     * {@inheritDoc}
     * 
     * @see com.thoughtworks.xstream.converters.ConverterMatcher#canConvert(java.lang.Class)
     */
    @SuppressWarnings("rawtypes")
    public boolean canConvert(Class pClass) {
        return pClass.equals(Message.class);
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.thoughtworks.xstream.converters.Converter#marshal(java.lang.Object,
     *      com.thoughtworks.xstream.io.HierarchicalStreamWriter,
     *      com.thoughtworks.xstream.converters.MarshallingContext)
     */
    public void marshal(Object pObj, HierarchicalStreamWriter pWriter,
            MarshallingContext pCtx) {
        Message lMessage = (Message) pObj;

        pWriter.addAttribute("key", lMessage.getKey());
        pWriter.setValue(lMessage.getTranslatedText());
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.thoughtworks.xstream.converters.Converter#unmarshal(com.thoughtworks.xstream.io.HierarchicalStreamReader,
     *      com.thoughtworks.xstream.converters.UnmarshallingContext)
     */
    public Object unmarshal(HierarchicalStreamReader pReader,
            UnmarshallingContext pContext) {
        Message lMessage = new Message();

        lMessage.setKey(pReader.getAttribute("key"));
        lMessage.setTranslatedText(pReader.getValue());

        return lMessage;
    }
}
