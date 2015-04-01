/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 ******************************************************************/
package org.topcased.gpm.domain.dictionary;

import java.util.List;

import org.hibernate.Query;
import org.topcased.gpm.domain.businessProcess.BusinessProcess;

/**
 * @see org.topcased.gpm.domain.dictionary.Dictionary
 * @author llatil
 */
public class DictionaryDaoImpl
        extends
        org.topcased.gpm.domain.AbstractDao<org.topcased.gpm.domain.dictionary.Dictionary, java.lang.String>
        implements org.topcased.gpm.domain.dictionary.DictionaryDao {

    final static String QUERY_GETDICTIONARYCATEGORYVALUES =
            "select distinct new "
                    + CategoryValueInfo.class.getName()
                    + "(category.name,categoryValue.value) "
                    + "from "
                    + Category.class.getName()
                    + " category, "
                    + CategoryValue.class.getName()
                    + " categoryValue,"
                    + BusinessProcess.class.getName()
                    + " businessProcess "
                    + "where businessProcess.name = :processName "
                    + "AND category.name in (:categoryNames) "
                    + "AND categoryValue in elements(category.categoryValues) "
                    + "AND category.dictionary.id = businessProcess.dictionary.id "
                    + "order by category.name";

    /**
     * Constructor
     */
    public DictionaryDaoImpl() {
        super(org.topcased.gpm.domain.dictionary.Dictionary.class);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public List getCategoryValues(String pProcessName,
            List<String> pCategoryNames) {
        final Query lQuery =
                getSession(false).createQuery(QUERY_GETDICTIONARYCATEGORYVALUES);

        lQuery.setParameter("processName", pProcessName);
        lQuery.setParameterList("categoryNames", pCategoryNames);

        return lQuery.list();
    }
}