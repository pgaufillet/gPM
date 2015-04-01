/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin)
 ******************************************************************/

package org.topcased.gpm.business.extensions.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.topcased.gpm.business.ServiceLocator;
import org.topcased.gpm.business.attributes.impl.CacheableAttributesContainer;
import org.topcased.gpm.business.extensions.service.ExtensionsService;
import org.topcased.gpm.business.serialization.data.ExtensionPoint;

/**
 * The Class CacheableExtensionsContainer.
 * 
 * @author llatil
 */
public class CacheableExtensionsContainer extends CacheableAttributesContainer {

    /** Generated UID */
    private static final long serialVersionUID = 6737305353046886932L;

    /** Map of Extension point name -> List of commands attached on this point. */
    private Map<String, List<String>> extensionsMap;

    /**
     * Empty constructor for mutable/immutable transformation
     */
    public CacheableExtensionsContainer() {
        super();
    }

    /**
     * Constructs a new cacheable extensions container.
     * 
     * @param pId
     *            the id
     */
    public CacheableExtensionsContainer(String pId) {
        super(pId);

        ExtensionsService lExtService =
                ServiceLocator.instance().getExtensionsService();
        extensionsMap = lExtService.getAllExtensions(pId);
    }

    /**
     * Get the list of command names associated with a given extension point.
     * 
     * @param pExtPointName
     *            Name of the extension point.
     * @return List of command names (or null if empty)
     */
    public List<String> getExtensionCommands(String pExtPointName) {
        if (null == extensionsMap) {
            return null;
        }
        return extensionsMap.get(pExtPointName);
    }

    /**
     * Check if a given extension point is defined on this container.
     * 
     * @param pExtPointName
     *            Extension point name
     * @return True if the extension point exists.
     */
    public boolean hasExtension(String pExtPointName) {
        return (extensionsMap != null && extensionsMap.get(pExtPointName) != null);
    }

    /**
     * Marshal this object into the given
     * org.topcased.gpm.business.serialization.data.ExtensionsContainer object.
     * 
     * @param pSerializedContent
     *            the serialized content
     */
    protected void marshal(
            org.topcased.gpm.business.serialization.data.ExtensionsContainer pSerializedContent) {
        super.marshal(pSerializedContent);

        List<ExtensionPoint> lExtPointList;
        lExtPointList = new ArrayList<ExtensionPoint>();

        if (getExtensionsMap() != null) {
            for (String lExtPointName : getExtensionsMap().keySet()) {
                lExtPointList.add(new ExtensionPoint(lExtPointName,
                        getExtensionsMap().get(lExtPointName)));
            }
        }
        pSerializedContent.setExtensionPoints(lExtPointList);
    }

    /**
     * Get the extensionsMap
     * 
     * @return the extensionsMap
     */
    public Map<String, List<String>> getExtensionsMap() {
        return extensionsMap;
    }

    /**
     * Set the extensionsMap
     * 
     * @param pExtensionsMap
     *            the new extensionsMap
     */
    public void setExtensionsMap(Map<String, List<String>> pExtensionsMap) {
        extensionsMap = pExtensionsMap;
    }
}
