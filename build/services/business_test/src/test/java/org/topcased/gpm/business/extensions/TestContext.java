/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.extensions;

import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.extensions.service.Context;

/**
 * Unit tests on the Context interface (and ContextBase default impl). Note:
 * These tests do not depend on a specific gPM instance.
 * 
 * @author llatil
 */
public class TestContext extends AbstractBusinessServiceTestCase {

    /**
     * Test the various Context methods.
     */
    public void testContextBasic() {
        Context lRootCtx = Context.getContext();

        lRootCtx.put("rootParam1");
        lRootCtx.put("subParam3");
        lRootCtx.put("rootParam2", "value2");

        Context lSubCtx = Context.createContext(lRootCtx);
        lSubCtx.set("rootParam1", "value1");
        lSubCtx.set("subParam3", "value3");

        assertEquals("value1", lSubCtx.get("rootParam1"));
        assertEquals("value1", lRootCtx.get("rootParam1"));

        assertEquals("value2", lRootCtx.get("rootParam2"));

        String[] lNames = lSubCtx.getValueNames();

        // Note: The context may contain additional parameters, automatically
        // created for the gPM purpose. So we can only check that the context
        // contains our own parameters  (but can contain others as well).
        assertTrue(ArrayUtils.contains(lNames, "rootparam1"));
        assertTrue(ArrayUtils.contains(lNames, "rootparam2"));
        assertTrue(ArrayUtils.contains(lNames, "subparam3"));

        Set<String> lNamesSet = lSubCtx.getNames();
        assertTrue(lNamesSet.contains("rootparam1"));
        assertTrue(lNamesSet.contains("rootparam2"));
        assertTrue(lNamesSet.contains("subparam3"));
    }
}
