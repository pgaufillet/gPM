/***************************************************************
 * Copyright (c) 2011 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thibault Landre (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.search.impl.query.field;

import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.search.criterias.CriteriaFieldData;
import org.topcased.gpm.business.search.criterias.impl.GPMQuery;
import org.topcased.gpm.business.search.criterias.impl.GPMQuery.AliasType;
import org.topcased.gpm.business.search.criterias.impl.GPMQuery.JOIN_TYPE;
import org.topcased.gpm.business.search.impl.query.FilterQueryConfigurator;
import org.topcased.gpm.business.search.service.UsableFieldData;
import org.topcased.gpm.domain.dynamic.util.DynamicObjectNamesUtils;

/**
 * Abstract generator for multi-valued field. This class is used to store common
 * code for all generators that handles multi-valued fields.
 * 
 * @author tlandre
 */
public abstract class AbstractMultiValuedFieldQueryGenerator extends
        AbstractFieldQueryGenerator implements IMultiValuedHandler {

    /**
     * Create an AbstractMultiValuedFieldQueryGenerator.
     * 
     * @param pUsableFieldData
     *            The usable field data.
     */
    protected AbstractMultiValuedFieldQueryGenerator(
            final UsableFieldData pUsableFieldData) {
        super(pUsableFieldData);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.field.IMultiValuedHandler#getMultiValuedFromClause(java.lang.String)
     */
    public String getMultiValuedFromClause(final String pAlias) {
        // Get values of the multi-valued field ordering by 'num_line' attribute and 'ASC' order.
        final GPMQuery lQuery = new GPMQuery();
        final String lAlias =
                lQuery.generateAlias(usableFieldData.getId(),
                        AliasType.FIELD_MV);

        lQuery.appendToSelectClause(getContainerColumnName(lAlias));
        lQuery.appendToSelectClause(lAlias + ".parent_id");
        lQuery.addElementInFromClause(
                DynamicObjectNamesUtils.getInstance().getDynamicTableName(
                        usableFieldData.getFieldId()), lAlias);
        lQuery.addOrderByElement(lAlias + ".num_line", "ASC");

        return "(" + lQuery.getCompleteQuery(false) + ")";
    }

    /**
     * Generates query for order multi-valued field
     * 
     * @see org.topcased.gpm.business.search.impl.query.field.IMultiValuedHandler#handleMultiValued(org.topcased.gpm.business.search.criterias.impl.GPMQuery,
     *      java.lang.String,
     *      org.topcased.gpm.business.search.service.UsableFieldData)
     */
    protected void handleMultiValuedOrder(final GPMQuery pQuery,
            final String pOrder) {
        //Add to from clause multi-valued class if the usableFieldData is not mapped
        String lAlias = StringUtils.EMPTY;
        if (pQuery.isAlreadyMapped(usableFieldData.getId())) {
            lAlias = pQuery.getMappedAlias(usableFieldData.getId());

        }
        else {
            //else get the usableFieldData alias
            lAlias =
                    pQuery.generateAlias(usableFieldData.getId(),
                            GPMQuery.AliasType.FIELD);

            pQuery.addElementInFromClause(JOIN_TYPE.LEFT_JOIN,
                    getMultiValuedFromClause(pQuery.getAlias()), lAlias,
                    pQuery.getAlias() + ".id = " + lAlias + ".parent_id");
        }
        //Set the containerAlias in order to use it to generate order clause
        setContainerAlias(lAlias);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.field.IMultiValuedHandler#getMultiValuedOrderByClause(java.lang.String)
     */
    public String getMultiValuedOrderByClause(final String pAlias) {
        return pAlias + ".num_line";
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.field.IMultiValuedHandler#getMultiValuedSelectClause()
     */
    public String getMultiValuedSelectClause(final String pAlias) {
        return getContainerColumnName(pAlias) + getAsClause();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.field.IMultiValuedHandler#handleMultiValuedCriterion(org.topcased.gpm.business.search.criterias.impl.GPMQuery,
     *      org.topcased.gpm.business.search.criterias.CriteriaFieldData,
     *      FilterQueryConfigurator)
     */
    public String handleMultiValuedCriterion(final GPMQuery pQuery,
            final CriteriaFieldData pCriteriaFieldData,
            final FilterQueryConfigurator pFilterQueryConfigurator) {
        //Add to from clause multi-valued table
        String lAlias = StringUtils.EMPTY;
        if (pQuery.isAlreadyMapped(usableFieldData.getId())) {
            lAlias = pQuery.getMappedAlias(usableFieldData.getId());
        }
        else {
            lAlias =
                    pQuery.generateAlias(usableFieldData.getId(),
                            GPMQuery.AliasType.FIELD);
            pQuery.addElementInFromClause(JOIN_TYPE.LEFT_JOIN,
                    getMultiValuedFromClause(pQuery.getAlias()), lAlias,
                    pQuery.getAlias() + ".id = " + lAlias + ".parent_id");

            pQuery.putMappedAlias(usableFieldData.getId(), lAlias);
        }
        return getMultiValuedWhereClause(lAlias);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.field.IFieldQueryGenerator#generateOrder(org.topcased.gpm.business.search.criterias.impl.GPMQuery,
     *      java.lang.String)
     */
    @Override
    public void generateOrder(GPMQuery pQuery, String pOrder) {
        if (usableFieldData.getMultivalued()) {
            handleMultiValuedOrder(pQuery, pOrder);
        }
        else {
            containerAlias = pQuery.getAlias();
            if (pQuery.isAlreadyMapped(usableFieldData.getId())) {
                containerAlias = pQuery.getMappedAlias(usableFieldData.getId());
            }
            else {
                pQuery.putMappedAlias(usableFieldData.getId(), containerAlias);
            }
        }
        final String lSelectElement = getSelectClause();
        if (!pQuery.isSelectClauseContains(lSelectElement)) {
            pQuery.appendToSelectClause(lSelectElement);
            pQuery.appendToGroupByClause(lSelectElement);
        }
        pQuery.addOrderByElement(getOrderByClause(), pOrder);

    }
}
