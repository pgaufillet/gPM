/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.search.impl.query;

import java.sql.Types;

/**
 * QueryParameter object.
 * <p>
 * Parameter of the query and its type (used by the PreparedStatement)
 * 
 * @see Types
 * @author mkargbo
 */
public class QueryParameter {
    private Object parameter;

    private int index;

    private int type;

    /**
     * QueryParameter constructor
     * 
     * @param pParameter Parameter of the query
     * @param pType Type of the query
     * @param pIndex the index
     */
    public QueryParameter(Object pParameter, int pType, int pIndex) {
        parameter = pParameter;
        type = pType;
        index = pIndex;
    }

    public Object getParameter() {
        return parameter;
    }

    public void setParameter(Object pParameter) {
        parameter = pParameter;
    }

    public int getType() {
        return type;
    }

    public void setType(int pType) {
        type = pType;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int pIndex) {
        index = pIndex;
    }
}
