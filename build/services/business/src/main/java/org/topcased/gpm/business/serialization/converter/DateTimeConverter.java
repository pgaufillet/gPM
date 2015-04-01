/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available 
 * under the terms of the Lesser Gnu Public License (LGPL)which 
 * accompanies this distribution, and is available 
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.serialization.converter;

import java.text.ParseException;
import java.util.Date;

import org.topcased.gpm.domain.util.FieldsUtil;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * DateTimeConverter class
 * 
 * @author Laurent Latil
 */
public class DateTimeConverter implements Converter {

    /**
     * {@inheritDoc}
     * 
     * @see com.thoughtworks.xstream.converters.Converter#marshal(java.lang.Object,
     *      com.thoughtworks.xstream.io.HierarchicalStreamWriter,
     *      com.thoughtworks.xstream.converters.MarshallingContext)
     */
    public void marshal(Object pSource, HierarchicalStreamWriter pWriter,
            MarshallingContext pContext) {
        String[] lDateTimeStr = FieldsUtil.formatDateTime((Date) pSource);

        String lDateStr = lDateTimeStr[0];
        String lTimeStr = lDateTimeStr[1];
        if (null != lDateStr) {
            pWriter.addAttribute("date", lDateStr);
        }

        if (null != lTimeStr) {
            pWriter.addAttribute("time", lTimeStr);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.thoughtworks.xstream.converters.Converter#unmarshal(com.thoughtworks.xstream.io.HierarchicalStreamReader,
     *      com.thoughtworks.xstream.converters.UnmarshallingContext)
     */
    public Object unmarshal(HierarchicalStreamReader pReader,
            UnmarshallingContext pContext) {

        String lDateISO8601 = pReader.getAttribute("date");
        String lTimeISO8601 = pReader.getAttribute("time");

        if (null != lTimeISO8601) {
            lDateISO8601 += "T" + lTimeISO8601;
        }
        Date lResultDate = null;
        try {
            lResultDate = FieldsUtil.parseDate(lDateISO8601);
        }
        catch (ParseException e) {
            // Should not happen here.
        }
        return lResultDate;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.thoughtworks.xstream.converters.ConverterMatcher#canConvert(java.lang.Class)
     */
    @SuppressWarnings("rawtypes")
    public boolean canConvert(Class pType) {
        return java.util.Date.class.isAssignableFrom(pType);
    }
}
