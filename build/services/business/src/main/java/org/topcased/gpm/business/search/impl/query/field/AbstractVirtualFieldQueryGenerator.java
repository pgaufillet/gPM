/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.search.impl.query.field;

import org.topcased.gpm.business.search.criterias.impl.GPMQuery;
import org.topcased.gpm.business.search.service.UsableFieldData;

/**
 * An abstract virtual field query generator.
 * 
 * @author tpanuel
 */
public abstract class AbstractVirtualFieldQueryGenerator extends
        AbstractFieldQueryGenerator {
    /**
     * Create an AbstractVirtualFieldQueryGenerator.
     * 
     * @param pUsableFieldData
     *            The usable field data.
     */
    public AbstractVirtualFieldQueryGenerator(
            final UsableFieldData pUsableFieldData) {
        super(pUsableFieldData);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.field.IFieldQueryGenerator#generateResult(org.topcased.gpm.business.search.criterias.impl.GPMQuery)
     */
    public void generateResult(final GPMQuery pQuery) {
        containerAlias = pQuery.getAlias();
        if (!pQuery.isAlreadyMapped(usableFieldData.getId())) {
            generateJoint(pQuery);
        }
        else {
            containerAlias = pQuery.getMappedAlias(usableFieldData.getId());
        }

        final String lSelectElement = getSelectClause();

        pQuery.addElementInSelectClauseWithAlias(usableFieldData.getId(),
                lSelectElement);
        pQuery.appendToGroupByClause(lSelectElement);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.field.IFieldQueryGenerator#generatreOrder(org.topcased.gpm.business.search.criterias.impl.GPMQuery,
     *      java.lang.String)
     */
    public void generateOrder(GPMQuery pQuery, String pOrder) {
        if (!pQuery.isAlreadyMapped(usableFieldData.getId())) {
            containerAlias = pQuery.getAlias();
            generateJoint(pQuery);
        }
        else {
            containerAlias = pQuery.getMappedAlias(usableFieldData.getId());
        }

        final String lOrderByClause = getOrderByClause();

        if (!pQuery.isSelectClauseContains(lOrderByClause)) {
            final String lSelectElement = getSelectClause();

            pQuery.addElementInSelectClauseWithAlias(usableFieldData.getId(),
                    lSelectElement);
            pQuery.appendToGroupByClause(lSelectElement);
        }
        pQuery.addOrderByElement(lOrderByClause, pOrder);
    }

    /**
     * Generate the join.
     * 
     * @param pQuery
     *            The query.
     */
    abstract protected void generateJoint(final GPMQuery pQuery);
}