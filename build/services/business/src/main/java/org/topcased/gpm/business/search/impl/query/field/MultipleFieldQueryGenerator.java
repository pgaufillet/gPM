/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.search.impl.query.field;

import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.exception.MethodNotImplementedException;
import org.topcased.gpm.business.search.criterias.CriteriaFieldData;
import org.topcased.gpm.business.search.criterias.impl.GPMQuery;
import org.topcased.gpm.business.search.criterias.impl.GPMQuery.AliasType;
import org.topcased.gpm.business.search.criterias.impl.GPMQuery.JOIN_TYPE;
import org.topcased.gpm.business.search.impl.query.FilterQueryConfigurator;
import org.topcased.gpm.business.search.service.UsableFieldData;
import org.topcased.gpm.domain.dynamic.util.DynamicObjectNamesUtils;

/**
 * Generator for <b>Multiple</b> fields.
 * <p>
 * Base on usable field structure (id and fieldId attributes)
 * <p>
 * Handle multi-valued field.
 * <p>
 * The field criterion is setting by generateCriterion method.
 * 
 * @author mkargbo
 */
public class MultipleFieldQueryGenerator extends AbstractFieldQueryGenerator
        implements IMultiValuedHandler {

    private static final String PARENT_ID = ".parent_id";

    private static final String MV = "_mv";

    private final String multipleFieldId;

    /**
     * StringFieldQueryGenerator constructor
     * 
     * @param pUsableFieldData
     *            Usable field to analyze for generation
     */
    public MultipleFieldQueryGenerator(final UsableFieldData pUsableFieldData) {
        super(pUsableFieldData);
        multipleFieldId = pUsableFieldData.getMultipleFieldId();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.field.AbstractFieldQueryGenerator#getSelectClause()
     */
    public String getSelectClause() {
        throw new MethodNotImplementedException(
                "No SELECT clause for multiple field.");
    }

    @Override
    protected String getFromClause() {
        return DynamicObjectNamesUtils.getInstance().getDynamicTableName(
                multipleFieldId);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.field.AbstractFieldQueryGenerator#getWhereClause()
     */
    public String getWhereClause() {
        throw new MethodNotImplementedException(
                "No WHERE clause for Multiple field.");
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.field.IFieldQueryGenerator#generateCriterion(org.topcased.gpm.business.search.criterias.impl.GPMQuery,
     *      org.topcased.gpm.business.search.criterias.CriteriaFieldData)
     */
    public String generateCriterion(final GPMQuery pQuery,
            final CriteriaFieldData pCriteriaFieldData,
            final FilterQueryConfigurator pFilterQueryConfigurator) {
        criteriaFieldData = pCriteriaFieldData;
        containerAlias = pQuery.getAlias();
        if (usableFieldData.getMultipleFieldMultivalued()) {
            return handleMultiValuedCriterion(pQuery, pCriteriaFieldData,
                    pFilterQueryConfigurator);
        }
        else {
            final IFieldQueryGenerator lFieldQueryGenerator =
                    FieldQueryGeneratorFactory.getInstance().getFieldQueryGeneratorFromMultipleField(
                            usableFieldData);
            return lFieldQueryGenerator.generateCriterion(pQuery,
                    pCriteriaFieldData, pFilterQueryConfigurator);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.field.IFieldQueryGenerator#generateResult(org.topcased.gpm.business.search.criterias.impl.GPMQuery)
     */
    public void generateResult(final GPMQuery pQuery) {
        containerAlias = pQuery.getAlias();
        if (usableFieldData.getMultipleFieldMultivalued()) {
            handleMultiValuedResult(pQuery, usableFieldData);
        }
        else {
            final IFieldQueryGenerator lFieldQueryGenerator =
                    FieldQueryGeneratorFactory.getInstance().getFieldQueryGeneratorFromMultipleField(
                            usableFieldData);
            lFieldQueryGenerator.generateResult(pQuery);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.field.AbstractFieldQueryGenerator#generateUnionResult(org.topcased.gpm.business.search.criterias.impl.GPMQuery)
     */
    @Override
    public void generateUnionResult(final GPMQuery pQuery) {
        containerAlias = pQuery.getAlias();
        final IFieldQueryGenerator lFieldQueryGenerator =
                FieldQueryGeneratorFactory.getInstance().getFieldQueryGeneratorFromMultipleField(
                        usableFieldData);
        lFieldQueryGenerator.generateUnionResult(pQuery);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.field.AbstractFieldQueryGenerator#getOrderByClause()
     */
    @Override
    protected String getOrderByClause() {
        throw new MethodNotImplementedException(
                "No ORDER BY clause for multiple field.");
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.field.IFieldQueryGenerator#generatreOrder(org.topcased.gpm.business.search.criterias.impl.GPMQuery,
     *      java.lang.String)
     */
    public void generateOrder(final GPMQuery pQuery, final String pOrder) {
        containerAlias = pQuery.getAlias();
        if (usableFieldData.getMultipleFieldMultivalued()) {
            handleMultiValuedOrder(pQuery, pOrder);
        }
        else {
            final IFieldQueryGenerator lFieldQueryGenerator =
                    FieldQueryGeneratorFactory.getInstance().getFieldQueryGeneratorFromMultipleField(
                            usableFieldData);
            lFieldQueryGenerator.generateOrder(pQuery, pOrder);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.field.IMultiValuedHandler#handleMultiValued(org.topcased.gpm.business.search.criterias.impl.GPMQuery,
     *      java.lang.String,
     *      org.topcased.gpm.business.search.service.UsableFieldData)
     */
    public void handleMultiValuedResult(final GPMQuery pQuery,
            final UsableFieldData pUsableFieldData) {
        //Add to from clause multi-valued table
        String lAlias = StringUtils.EMPTY;
        String lId = multipleFieldId + "-" + containerAlias;
        if (pQuery.isAlreadyMapped(lId)) {
            final GPMQuery lQuery = pQuery.getSubQuery(lId);
            lAlias = pQuery.getMappedAlias(lId);
            final String lMultipleAlias =
                    pQuery.getMappedAlias(lId + MV);
            
            if (!pUsableFieldData.getMultivalued()) {
                lQuery.appendToSelectClause(getContainerColumnName(lMultipleAlias));
            }
            
            pQuery.updateSubQuery(lId, lQuery);
        }
        else {
            lAlias =
                    pQuery.generateAlias(lId,
                            GPMQuery.AliasType.FIELD);
            final String lMultipleAlias =
                    pQuery.generateAlias(lId + MV,
                            GPMQuery.AliasType.FIELD_MV);
            pQuery.addSubQuery(lId, JOIN_TYPE.LEFT_JOIN,
                    getMultiValuedFromClauseQuery(lMultipleAlias, false), lAlias,
                    pQuery.getAlias() + ".id = " + lAlias + PARENT_ID);
        }
        pQuery.setAlias(lAlias);
        final IFieldQueryGenerator lFieldQueryGenerator =
                FieldQueryGeneratorFactory.getInstance().getFieldQueryGeneratorFromMultipleField(
                        usableFieldData);
        lFieldQueryGenerator.generateResult(pQuery);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.field.IMultiValuedHandler#handleMultiValuedCriterion(org.topcased.gpm.business.search.criterias.impl.GPMQuery,
     *      org.topcased.gpm.business.search.service.UsableFieldData,
     *      FilterQueryConfigurator)
     */
    public String handleMultiValuedCriterion(final GPMQuery pQuery,
            final CriteriaFieldData pCriteriaFieldData,
            final FilterQueryConfigurator pFilterQueryConfigurator) {
        //Add to from clause multi-valued table
        String lAlias = StringUtils.EMPTY;
        String lId = multipleFieldId + "-" + containerAlias;
        if (pQuery.isAlreadyMapped(lId)) {

            final GPMQuery lQuery = pQuery.getSubQuery(lId);
            lAlias = pQuery.getMappedAlias(lId);
            final String lMultipleAlias =
                    pQuery.getMappedAlias(lId + MV);
            
            if (!usableFieldData.getMultivalued()) {
                lQuery.appendToSelectClause(getContainerColumnName(lMultipleAlias));
            }
            
            pQuery.updateSubQuery(lId, lQuery);
        }
        else {
            lAlias =
                    pQuery.generateAlias(lId,
                            GPMQuery.AliasType.FIELD);
            final String lMultipleAlias =
                    pQuery.generateAlias(lId + MV,
                            GPMQuery.AliasType.FIELD_MV);
            pQuery.addSubQuery(lId, JOIN_TYPE.LEFT_JOIN,
                    getMultiValuedFromClauseQuery(lMultipleAlias, usableFieldData.getMultivalued()), lAlias,
                    pQuery.getAlias() + ".id = " + lAlias + PARENT_ID);
        }
        pQuery.setAlias(lAlias);

        final IFieldQueryGenerator lFieldQueryGenerator =
                FieldQueryGeneratorFactory.getInstance().getFieldQueryGeneratorFromMultipleField(
                        usableFieldData);
        return lFieldQueryGenerator.generateCriterion(pQuery,
                pCriteriaFieldData, pFilterQueryConfigurator);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.field.IMultiValuedHandler#handleMultiValuedCriterion(GPMQuery,
     *      CriteriaFieldData, FilterQueryConfigurator)
     *      (org.topcased.gpm.business.search.criterias.impl.GPMQuery,
     *      org.topcased.gpm.business.search.service.UsableFieldData,
     *      FilterQueryConfigurator)
     */
    public void handleMultiValuedOrder(final GPMQuery pQuery,
            final String pOrder) {
        //Add to from clause multi-valued table
        String lAlias = StringUtils.EMPTY;
        if (pQuery.isAlreadyMapped(multipleFieldId)) {

            final GPMQuery lQuery = pQuery.getSubQuery(multipleFieldId);
            lAlias = pQuery.getMappedAlias(multipleFieldId);
            final String lMultipleAlias =
                    pQuery.getMappedAlias(multipleFieldId + MV);
            lQuery.appendToSelectClause(getContainerColumnName(lMultipleAlias));
            pQuery.updateSubQuery(multipleFieldId, lQuery);

        }
        else {
            lAlias =
                    pQuery.generateAlias(multipleFieldId,
                            GPMQuery.AliasType.FIELD);
            final String lMultipleAlias =
                    pQuery.generateAlias(multipleFieldId + MV,
                            GPMQuery.AliasType.FIELD_MV);
            pQuery.addSubQuery(multipleFieldId, JOIN_TYPE.LEFT_JOIN,
                    getMultiValuedFromClauseQuery(lMultipleAlias, false), lAlias,
                    pQuery.getAlias() + ".id = " + lAlias + PARENT_ID);

        }
        pQuery.setAlias(lAlias);

        final IFieldQueryGenerator lFieldQueryGenerator =
                FieldQueryGeneratorFactory.getInstance().getFieldQueryGeneratorFromMultipleField(
                        usableFieldData);
        lFieldQueryGenerator.generateOrder(pQuery, pOrder);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.field.IMultiValuedHandler#getMultiValuedSelectClause()
     */
    public String getMultiValuedSelectClause(final String pAlias) {
        throw new MethodNotImplementedException(
                "No SELECT clause for multiple field.");
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
     * @see org.topcased.gpm.business.search.impl.query.field.IMultiValuedHandler#getMultiValuedFromClause(java.lang.String)
     */
    public String getMultiValuedFromClause(final String pAlias) {
        //Get values of the multi-valued field ordering by 'num_line' attribute and 'ASC' order.
        //
        final GPMQuery lQuery = new GPMQuery();
        final String lAlias =
                lQuery.generateAlias(multipleFieldId, AliasType.FIELD_MV);
        lQuery.appendToSelectClause(getContainerColumnName(lAlias));
        lQuery.addElementInFromClause(
                DynamicObjectNamesUtils.getInstance().getDynamicTableName(
                        multipleFieldId), lAlias);
        lQuery.addOrderByElement(lAlias + ".num_line", "ASC");

        return "(" + lQuery.getCompleteQuery(false) + ")";
    }

    private GPMQuery getMultiValuedFromClauseQuery(final String pMultipleAlias, 
            final boolean pMultipleMultiValued) {
        //Get values of the multi-valued field ordering by 'num_line' attribute and 'ASC' order.
        //
        final GPMQuery lQuery = new GPMQuery();
        lQuery.appendToSelectClause(pMultipleAlias + PARENT_ID);
        
        if (!pMultipleMultiValued) {
            lQuery.appendToSelectClause(getContainerColumnName(pMultipleAlias));
        }
        
        lQuery.appendToSelectClause(pMultipleAlias + ".id");
        
        lQuery.addElementInFromClause(
                DynamicObjectNamesUtils.getInstance().getDynamicTableName(
                        multipleFieldId), pMultipleAlias);
        lQuery.addOrderByElement(pMultipleAlias + ".num_line", "ASC");

        return lQuery;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.field.IMultiValuedHandler#getMultiValuedWhereClause(java.lang.String)
     */
    public String getMultiValuedWhereClause(final String pAlias) {
        throw new MethodNotImplementedException(
                "No WHERE clause for multiple field.");
    }
}