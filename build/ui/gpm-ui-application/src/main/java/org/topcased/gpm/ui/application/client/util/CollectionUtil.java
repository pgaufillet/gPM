/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Tool class for collections.
 * 
 * @author tpanuel
 */
public class CollectionUtil {
    /**
     * Get a singleton.
     * 
     * @param <T>
     *            The element type.
     * @param pElement
     *            The unique element.
     * @return The singleton.
     */
    public final static <T> List<T> singleton(final T pElement) {
        final List<T> lSingleton = new ArrayList<T>();

        lSingleton.add(pElement);

        return lSingleton;
    }

    /**
     * Create a case insensitive map.
     * <p>
     * Case of the key are all UPPER.
     * </p>
     * 
     * @param pMap
     *            Map to convert.
     * @return Map which have its key to UPPER case.
     */
    public static Map<String, String> toCaseInsensitiveMap(
            final Map<String, List<String>> pMap) {
        final Map<String, String> lInsensitive =
                new HashMap<String, String>(pMap.size());
        for (Map.Entry<String, List<String>> lEntry : pMap.entrySet()) {
            lInsensitive.put(lEntry.getKey().toUpperCase(),
                    lEntry.getValue().get(0));
        }
        return lInsensitive;
    }
}