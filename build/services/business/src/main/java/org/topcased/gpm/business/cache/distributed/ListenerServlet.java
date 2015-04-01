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

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import org.apache.log4j.Logger;

/**
 * EHCache listener servlet.
 * 
 * @author Olivier Juin
 */
public class ListenerServlet extends HttpServlet {

    /** The log4j logger object for this class. */
//    private static Logger staticLOGGER =
//            Logger.getLogger(ListenerServlet.class);
    
    /** Default serial ID */
    private static final long serialVersionUID = 1L;

    /** {@inheritDoc} */
    @Override
    protected void doPost(HttpServletRequest pReq, HttpServletResponse pResp)
        throws ServletException, IOException {
        
//        if (staticLOGGER.isDebugEnabled()) {
//            staticLOGGER.debug("POST from " + pReq.getRequestURL()
//                    + " with parameters :" + pReq.getParameterMap());
//        }
        
        String lCacheName = pReq.getParameter("cache");
        String[] lKeys = pReq.getParameter("keys").split(",");
        if (lKeys != null && Commons.getPeerListener() != null) {
            for (String lKey : lKeys) {
                Commons.getPeerListener().evict(lCacheName, lKey);
            }
        }
    }
}
