/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil(Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.attributes.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.topcased.gpm.business.CacheableGpmObject;
import org.topcased.gpm.business.ContextLocator;
import org.topcased.gpm.business.attributes.AttributeData;
import org.topcased.gpm.business.attributes.service.AttributesService;
import org.topcased.gpm.business.serialization.data.Attribute;
import org.topcased.gpm.business.serialization.data.AttributeValue;
import org.topcased.gpm.business.serialization.data.AttributesContainer;

/**
 * The Class CacheableAttributesContainer.
 * 
 * @author llatil
 */
public class CacheableAttributesContainer extends CacheableGpmObject {

    /** Generated UID */
    private static final long serialVersionUID = 2563318939399082241L;

    /** Map attribute name -> values. */
    private Map<String, String[]> attributesMap;

    /**
     * Empty constructor for mutable/immutable transformation
     */
    public CacheableAttributesContainer() {
        super();
    }

    /**
     * Constructs a new cacheable attributes container.
     * 
     * @param pId the id
     * @param pInitFromDb true if init required
     */
    public CacheableAttributesContainer(String pId, boolean pInitFromDb) {
        super(pId);
        if (pInitFromDb) {
            init(pId);
        }
    }

    /**
     * Constructs a new cacheable attributes container from a serialization
     * object.
     * 
     * @param pId
     *            the identifier of the container
     * @param pAttributesContainer
     *            Serialized attributes container.
     */
    public CacheableAttributesContainer(
            String pId,
            org.topcased.gpm.business.serialization.data.AttributesContainer pAttributesContainer) {
        super(pId);

        if (pAttributesContainer.hasAttributes()) {
            attributesMap = new LinkedHashMap<String, String[]>();
            for (Attribute lAttr : pAttributesContainer.getAttributes()) {
                attributesMap.put(lAttr.getName(), lAttr.getValues());
            }
        }
    }

    /**
     * Constructs a new cacheable attributes container from a list of attributes
     * object.
     * 
     * @param pAttributes
     *            List of attributes to initialize this container.
     */
    public CacheableAttributesContainer(List<Attribute> pAttributes) {
        super(null);
        addAttributes(pAttributes);
    }

    /**
     * Constructs a new cacheable attributes container.
     * 
     * @param pId
     *            the id
     */
    public CacheableAttributesContainer(String pId) {
        super(pId);
        init(pId);
    }

    /**
     * Initialize the attributes from the database content.
     * 
     * @param pId
     */
    private void init(String pId) {
        if (pId != null) {
            AttributesService lAttrService =
                    (AttributesService) ContextLocator.getContext().getBean(
                            "attributesServiceImpl",
                            AttributesServiceImpl.class);

            AttributeData[] lAttrDataArray = lAttrService.getAll(pId);
            if (lAttrDataArray != null && lAttrDataArray.length > 0) {
                attributesMap = new LinkedHashMap<String, String[]>();
                for (AttributeData lAttrData : lAttrDataArray) {
                    attributesMap.put(lAttrData.getName(),
                            lAttrData.getValues());
                }
            }
        }
    }

    /**
     * Get the attributesMap
     * 
     * @return the attributesMap
     */
    public Map<String, String[]> getAttributesMap() {
        return attributesMap;
    }

    /**
     * Set the attributesMap
     * 
     * @param pAttributesMap
     *            the new attributesMap
     */
    public void setAttributesMap(Map<String, String[]> pAttributesMap) {
        attributesMap = pAttributesMap;
    }

    /**
     * Get a list of all attributes defined in this container.
     * 
     * @return Attributes list
     */
    public List<Attribute> getAllAttributes() {
        if (null == attributesMap) {
            return Collections.emptyList();
        }
        List<Attribute> lAttrs = new ArrayList<Attribute>(attributesMap.size());
        for (Map.Entry<String, String[]> lEntry : attributesMap.entrySet()) {
            List<AttributeValue> lValues = new ArrayList<AttributeValue>();

            for (String lValue : lEntry.getValue()) {
                lValues.add(new AttributeValue(lValue));
            }
            lAttrs.add(new Attribute(lEntry.getKey(), lValues));
        }
        return lAttrs;
    }

    /**
     * Get a list of the names of all attributes defined in this container.
     * 
     * @return Collection of all attribute names.
     */
    public Collection<String> getAllAttributeNames() {
        if (null == attributesMap) {
            return Collections.emptyList();
        }
        return attributesMap.keySet();
    }

    /**
     * Gets the attribute values.
     * 
     * @param pAttrName
     *            the attr name
     * @return the attribute values (null if the attribute is not defined)
     */
    public String[] getAttributeValues(String pAttrName) {
        if (null == attributesMap) {
            return null;
        }
        return attributesMap.get(pAttrName);
    }

    /**
     * Add new extended attributes to the container
     * 
     * @param pAttributes
     *            list of attributes to add
     */
    public void addAttributes(List<Attribute> pAttributes) {

        if (attributesMap == null) {
            attributesMap = new LinkedHashMap<String, String[]>();
        }
        if (pAttributes != null) {
            // For each attribute, either create new entry in the map with new values,
            // or add new values to existing values in the map element
            for (Attribute lAttribute : pAttributes) {
                String[] lExistingValue =
                        attributesMap.get(lAttribute.getName());
                if (lExistingValue == null) {
                    attributesMap.put(lAttribute.getName(),
                            lAttribute.getValues());
                }
                else {
                    // FIXME: this value should be put in the map
                    // The following line does nothing at all.
                    lExistingValue =
                            (String[]) ArrayUtils.addAll(lExistingValue,
                                    lAttribute.getValues());
                }
            }
        }
    }

    /**
     * Marshal this object into the given AttributesContainer object.
     * 
     * @param pObject
     *            the serialized content
     */
    protected void marshal(Object pObject) {
        super.marshal(pObject);

        if (null != getAttributesMap()) {
            AttributesContainer lSerializedContent =
                    (AttributesContainer) pObject;
            List<org.topcased.gpm.business.serialization.data.Attribute> lAttributes;

            lAttributes =
                    new ArrayList<org.topcased.gpm.business.serialization.data.Attribute>();

            for (Map.Entry<String, String[]> lAttrEntry : getAttributesMap().entrySet()) {
                String lAttrName = lAttrEntry.getKey();
                String[] lValues = lAttrEntry.getValue();
                List<AttributeValue> lSerializedValues =
                        new ArrayList<AttributeValue>();

                for (String lValue : lValues) {
                    lSerializedValues.add(new AttributeValue(lValue));
                }

                lAttributes.add(new org.topcased.gpm.business.serialization.data.Attribute(
                        lAttrName, lSerializedValues));
            }
            lSerializedContent.setAttributes(lAttributes);
        }
    }
}
