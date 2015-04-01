/***************************************************************
 * Copyright (c) 2012 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Olivier Juin (Atos)
 ***************************************************************/
package org.topcased.gpm.business.ehcache;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.topcased.gpm.business.cache.distributed.HTTPUtils;

/**
 * Test the EHCache dispatcher
 * 
 * @author ojuin
 */
public class DispatcherTest {

    
    private static final String[][] KEYS = new String[][] {
        { "key" },
        { "clé1", "key2", ",,\\&=" }
    };
    
    private static final String[] SOURCE_URL = { "http://server1", "http://server2" };
    private static final String DESTINATION_URL =
        "http://127.0.0.1:8080/gPM-tools-EHCacheDispatcher-3.1.0-SNAPSHOT/EHCacheDispatcher";
    
    /**
     * Main entry point
     * 
     * @param pArgs unused
     * @throws Exception not caught
     */
    public static void main(String[] pArgs) throws Exception {
        final int lSECOND = 1000;
        for (int i = 0; i < KEYS.length; i++) {
            Map<String, String[]> lParamMap = new HashMap<String, String[]>();
            lParamMap.put("url", new String[] { SOURCE_URL[i] });
            Collection<String> lKeys = new HashSet<String>();
            for (int j = 0; j < KEYS[i].length; j++) {
                lKeys.add(KEYS[i][j]);
            }
            lParamMap.put("keys", lKeys.toArray(new String[lKeys.size()]));
            HTTPUtils.post(DESTINATION_URL, lParamMap);
            Thread.sleep(lSECOND);
        }
    }
}
