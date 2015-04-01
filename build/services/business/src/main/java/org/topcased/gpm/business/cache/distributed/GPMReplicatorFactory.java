/***************************************************************
 * Copyright (c) 2012 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Olivier Juin (Atos)
 ******************************************************************/
package org.topcased.gpm.business.cache.distributed;

import java.util.Properties;

import net.sf.ehcache.event.CacheEventListener;
import net.sf.ehcache.event.CacheEventListenerFactory;

/**
 * This factory provides an EHCacheReplicator object
 * 
 * @author Olivier Juin
 */
public class GPMReplicatorFactory extends CacheEventListenerFactory {

    private static CacheEventListener staticInstance;
    
    /** {@inheritDoc} */
    @Override
    public synchronized CacheEventListener createCacheEventListener(Properties pProperties) {
        if (staticInstance == null) {
            staticInstance = new EHCacheReplicator();
        }
        return staticInstance;
    }
}
