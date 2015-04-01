/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Magali Franchet (Neo-Soft on behalf of Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.attributes.impl;

import java.util.ArrayList;
import java.util.List;

import org.topcased.gpm.business.attributes.AttributeData;
import org.topcased.gpm.domain.attributes.Attribute;
import org.topcased.gpm.domain.attributes.AttributeValue;

/**
 * Helper methods to create attribute data instance
 * 
 * @author mfranche
 */
public class AttributesUtils {

    /**
     * Create an AttributeData object from a domain Attribute object.
     * 
     * @param pAttr
     *            Attribute object (domain object)
     * @return The AttributeData object
     */
    public static AttributeData createAttributeData(final Attribute pAttr) {
        if (null == pAttr) {
            return null;
        }
        AttributeData lAttrData = new AttributeData();
        lAttrData.setName(pAttr.getName());

        String[] lValues = new String[pAttr.getAttributeValues().size()];
        int i = 0;

        for (AttributeValue lValue : pAttr.getAttributeValues()) {
            lValues[i] = lValue.getValue();
            if (lValues[i] == null) {
                lValues[i] = new String();
            }
            i++;
        }
        lAttrData.setValues(lValues);
        return lAttrData;
    }

    /**
     * Transform a list of business attributes into a list of serialization
     * attributes (only modify data structure, not content)
     * 
     * @param pAttributes
     *            list of business attributes
     * @return list of serialization attributes
     */
    public static List<org.topcased.gpm.business.serialization.data.Attribute>
           transformIntoSerializationAttributes(
            AttributeData[] pAttributes) {
        if (pAttributes == null) {
            return null;
        }
        List<org.topcased.gpm.business.serialization.data.Attribute> lAttributes =
                new ArrayList<org.topcased.gpm.business.serialization.data.Attribute>();
        for (AttributeData lAttribute : pAttributes) {
            List<org.topcased.gpm.business.serialization.data.AttributeValue> lValues =
                    new ArrayList<org.topcased.gpm.business.serialization.data.AttributeValue>();
            for (String lValue : lAttribute.getValues()) {
                lValues.add(new org.topcased.gpm.business.serialization.data.AttributeValue(
                        lValue));
            }
            lAttributes.add(new org.topcased.gpm.business.serialization.data.Attribute(
                    lAttribute.getName(), lValues));
        }
        return lAttributes;
    }

    /**
     * Transform an serialization attribute to an business attribute/
     * 
     * @param pAttribute
     *            Attribute to transform
     * @return Business attribute
     */
    public static AttributeData transformIntoAttributeData(
            org.topcased.gpm.business.serialization.data.Attribute pAttribute) {
        return new AttributeData(pAttribute.getName(), pAttribute.getValues());
    }
}
