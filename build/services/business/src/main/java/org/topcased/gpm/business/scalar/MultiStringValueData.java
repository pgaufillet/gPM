/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 ******************************************************************/
package org.topcased.gpm.business.scalar;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.text.StrBuilder;

/**
 * Scalar value data for string.
 * 
 * @author tpanuel
 */
public class MultiStringValueData extends ScalarValueData {
    private static final long serialVersionUID = -7576799179897527179L;

    /**
     * Constructor taking all properties.
     * 
     * @param pValues
     *            The values.
     */
    public MultiStringValueData(final Collection<String> pValues) {
        values = pValues;
    }

    private final Collection<String> values;

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.scalar.ScalarValueData#getValue()
     */
    public Collection<String> getValue() {
        return values;
    }

    /**
     * Create the clause for SQL request.
     * 
     * @return The clause in.
     */
    public String getInClause() {
        final Set<String> lValues = new HashSet<String>();
        final StrBuilder lInClause = new StrBuilder();

        for (String lValue : values) {
            lValues.add(lValue.replaceAll("'", "''"));
        }
        lInClause.append("('");
        lInClause.appendWithSeparators(lValues, "','");
        lInClause.append("')");

        return lInClause.toString();
    }
}