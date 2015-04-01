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

import org.topcased.gpm.business.serialization.data.TransitionHistoryData;
import org.topcased.gpm.domain.util.FieldsUtil;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * Transition Converter
 * 
 * @author Laurent Latil
 */
public class TransitionConverter implements Converter {

    /**
     * {@inheritDoc}
     * 
     * @see com.thoughtworks.xstream.converters.Converter#marshal(java.lang.Object,
     *      com.thoughtworks.xstream.io.HierarchicalStreamWriter,
     *      com.thoughtworks.xstream.converters.MarshallingContext)
     */
    public void marshal(Object pSource, HierarchicalStreamWriter pWriter,
            MarshallingContext pContext) {
        TransitionHistoryData lTransition = (TransitionHistoryData) pSource;

        pWriter.addAttribute("login", lTransition.getLogin());
        pWriter.addAttribute("originState", lTransition.getOriginState());
        pWriter.addAttribute("destinationState",
                lTransition.getDestinationState());
        pWriter.addAttribute("transitionName",
                lTransition.getTransitionName());

        String[] lDateTimeStr =
                FieldsUtil.formatDateTime(lTransition.getTransitionDate());
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

        TransitionHistoryData lTransition = new TransitionHistoryData();
        lTransition.setLogin(pReader.getAttribute("login"));
        lTransition.setOriginState(pReader.getAttribute("originState"));
        lTransition.setDestinationState(pReader.getAttribute("destinationState"));
        lTransition.setTransitionName(pReader.getAttribute("transitionName"));
        try {
            lTransition.setTransitionDate(FieldsUtil.parseDate(lDateISO8601));
        }
        catch (ParseException e) {
            // Should not happen here.
            throw new RuntimeException("Invalid date or time format");
        }
        return lTransition;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.thoughtworks.xstream.converters.ConverterMatcher#canConvert(java.lang.Class)
     */
    @SuppressWarnings("rawtypes")
    public boolean canConvert(Class pType) {
        return TransitionHistoryData.class.isAssignableFrom(pType);
    }
}
