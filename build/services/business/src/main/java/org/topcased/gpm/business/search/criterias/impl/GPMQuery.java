/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Anne Haugommard (Atos Origin), Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.search.criterias.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrBuilder;

/**
 * GPMQuery is supposed to be used for a query construction with different
 * clauses (select, from, where, order by, group by and union).
 * <p>
 * Support alias generation and retrieving.
 * 
 * @author ahaugomm
 */
public final class GPMQuery {
    /** Text value of 'AS' for SELECT clause (surrounded by whitespace) **/
    public static final String SELECT_AS_CLAUSE = " AS ";

    private final AliasGenerator aliasGenerator;

    /** Clause containing the 'select' (without SELECT keyword) */
    private final List<String> selectClause;

    /** Clause containing the 'from' (without FROM keyword) */
    private final List<String> fromClause;

    /** Clause containing the 'where' (without WHERE keyword) */
    private final StringBuilder whereClause;

    /** Clause containing the 'orderBy' (without ORDER BY keyword) */
    private final StringBuilder orderByClause;

    /** Clause containing the 'groupBy' (without GROUP BY keyword) */
    private final StrBuilder groupByClause;

    /** Clause containing query to combine with a UNION clause) **/
    private final Collection<String> unionQuery;

    private final Collection<String> mandatoryCondition;

    /**
     * Used for sub queries to add a surrounding EXISTS() to query (ie with
     * method {@link #getAsSubQuery()}
     */
    private boolean isExistingSubQuery;

    /**
     * In certain cases, the container part of the fromClause must be added at
     * the beginning of the fromClause
     */
    private final StringBuilder containerPart;

    /**
     * Counter used for each query element in order to have unique names inside
     * query.
     */
    private int counter;

    public final static String ORDER_ASCENDING = "asc";

    public final static String ORDER_DESCENDING = "desc";

    private final Map<String, String> alias2Table;

    private final Map<String, String> alias2Field;

    private String mainAlias;

    private boolean enableGroupByClause;

    private final Map<String, GPMSubQuery> subQueries;

    /**
     * Constructor
     */
    public GPMQuery() {
        this(true);
    }

    /**
     * Creation of a gPMQuery
     * 
     * @param pEnableGroupByClause enable group by clause
     */
    public GPMQuery(boolean pEnableGroupByClause) {
        aliasGenerator = new AliasGenerator();
        selectClause = new ArrayList<String>();
        fromClause = new ArrayList<String>();
        alias2Table = new HashMap<String, String>();
        alias2Field = new HashMap<String, String>();
        whereClause = new StringBuilder();
        orderByClause = new StringBuilder();
        groupByClause = new StrBuilder();
        unionQuery = new HashSet<String>();
        mandatoryCondition = new HashSet<String>();
        counter = 0;
        containerPart = new StringBuilder();
        isExistingSubQuery = false;
        enableGroupByClause = pEnableGroupByClause;
        subQueries = new LinkedHashMap<String, GPMSubQuery>();
    }

    /**
     * Add or remove EXISTS() around sub query
     * 
     * @param pAddExistClause
     *            <ul>
     *            <li>if true, EXISTS is added</li>
     *            <li>if false, it is not present</li>
     *            </ul>
     */
    public void setExistingSubQuery(boolean pAddExistClause) {
        isExistingSubQuery = pAddExistClause;
    }

    /**
     * Append a string to the container part of the fromClause
     * 
     * @param pElementToAppend
     *            the string to append
     */
    public void appendToContainerPart(String pElementToAppend) {
        if (pElementToAppend != null) {
            containerPart.append(pElementToAppend);
        }
    }

    /**
     * Append a string to the select clause if the string to append is not empty
     * and not already present in the selectClause
     * 
     * @param pSelectToAppend
     *            the string to append
     */
    public void appendToSelectClause(String pSelectToAppend) {
        if ((StringUtils.isNotBlank(pSelectToAppend))
                && (!selectClause.contains(pSelectToAppend))) {
            selectClause.add(pSelectToAppend);
        }
    }

    /**
     * Append a string to the from clause
     * 
     * @param pFromToAppend
     *            the string to append
     */
    public void appendToFromClause(String pFromToAppend) {
        if (StringUtils.isNotBlank(pFromToAppend)) {
            fromClause.add(pFromToAppend);
        }
    }

    /**
     * Append a string to the where clause
     * 
     * @param pWhereToAppend
     *            the string to append
     */
    public void appendToWhereClause(String pWhereToAppend) {
        if (pWhereToAppend != null) {
            whereClause.append(pWhereToAppend);
        }
    }

    /**
     * Append a string to the order by clause
     * 
     * @param pOrderByToAppend
     *            string to append
     */
    public void appendToOrderByClause(String pOrderByToAppend) {
        if (pOrderByToAppend != null) {
            orderByClause.append(pOrderByToAppend);
        }
    }

    /**
     * Append an element to the group by clause.
     * <p>
     * Handle comma between elements.
     * 
     * @param pElementToAppend
     *            Element to append
     */
    public void appendToGroupByClause(String pElementToAppend) {
        if (StringUtils.isNotBlank(pElementToAppend)) {
            if (groupByClause.length() > 0) {
                groupByClause.append(", ");
            }
            groupByClause.append(pElementToAppend);
        }
    }

    /**
     * Append a query to the query to unify.
     * 
     * @param pElementToAppend
     *            Query to append.
     */
    public void appendToUnionClause(String pElementToAppend) {
        if (StringUtils.isNotBlank(pElementToAppend)) {
            unionQuery.add(pElementToAppend);
        }
    }

    /**
     * Append a where clause element which is mandatory.
     * 
     * @param pCondition
     *            Condition to append.
     */
    public void appendToMandatoryCondition(String pCondition) {
        if (StringUtils.isNotBlank(pCondition)) {
            mandatoryCondition.add(pCondition);
        }
    }

    /**
     * Append a string to the order by clause (the , separators are
     * automatically added)
     * 
     * @param pOrderByToAppend
     *            string to append (element and order)
     */
    public void addOrderByElementAndOrder(String pOrderByToAppend) {
        if (pOrderByToAppend != null) {
            if (orderByClause.length() > 0) {
                orderByClause.append(", ");
            }
            orderByClause.append(pOrderByToAppend);
        }
    }

    /**
     * Add an element and its order to the 'order by' clause
     * 
     * @param pElement
     *            element to add
     * @param pOrder
     *            order to add
     */
    public void addOrderByElement(String pElement, String pOrder) {
        if (orderByClause.length() > 0) {
            orderByClause.append(",");
        }
        orderByClause.append(pElement);
        orderByClause.append(" ");
        String lOrder = pOrder;
        orderByClause.append(lOrder);
    }

    /**
     * Add an element and its order to the 'order by' clause
     * 
     * @param pElement
     *            element to add
     */
    public void addOrderByElement(String pElement) {
        addOrderByElement(pElement, ORDER_ASCENDING);
    }

    /**
     * Add an element to the from clause
     * 
     * @param pClass
     *            the class of the element to add
     * @param pAlias
     *            the alias of the element to add
     */
    public void addElementInFromClause(String pClass, String pAlias) {

        alias2Table.put(pAlias, pClass);
        StringBuilder lFromElement = new StringBuilder();
        if (!fromClause.isEmpty()) {
            lFromElement.append(", ");
        }
        lFromElement.append(pClass);
        lFromElement.append(" ");
        lFromElement.append(pAlias);

        appendToFromClause(lFromElement.toString());
    }

    /**
     * Add element in From Clause
     * 
     * @param pJoinType the join type
     * @param pTable the table
     * @param pTableAlias the table alias
     * @param pCondition the condition
     */
    public void addElementInFromClause(JOIN_TYPE pJoinType, String pTable,
            String pTableAlias, String pCondition) {
        StringBuilder lFromElement = new StringBuilder();
        lFromElement.append(" ").append(pJoinType).append(" ").append(pTable).append(
                " ").append(pTableAlias).append(" ON ").append(pCondition);
        appendToFromClause(lFromElement.toString());
    }

    /**
     * add sub-query
     * 
     * @param pIdentifier sub-query id
     * @param pJoinType the join type
     * @param pTable the table
     * @param pTableAlias the table alias
     * @param pCondition the condition 
     */
    public void addSubQuery(String pIdentifier, JOIN_TYPE pJoinType,
            GPMQuery pTable, String pTableAlias, String pCondition) {
        GPMSubQuery lSubQuery =
                new GPMSubQuery(pJoinType, pTable, pTableAlias, pCondition);
        subQueries.put(pIdentifier, lSubQuery);
        fromClause.add("$" + pIdentifier);
    }

    /**
     * Get sub-query
     * 
     * @param pIdentifier sub-query id
     * @return sub-query
     */
    public GPMQuery getSubQuery(String pIdentifier) {
        return subQueries.get(pIdentifier).table;
    }

    /**
     * Update sub-query
     * 
     * @param pIdentifier sub-query id
     * @param pQuery the query
     */
    public void updateSubQuery(String pIdentifier, GPMQuery pQuery) {
        GPMSubQuery lSubQuery = subQueries.get(pIdentifier);
        lSubQuery.setTable(pQuery);
        subQueries.put(pIdentifier, lSubQuery);
    }


    /**
     * get Table
     * 
     * @param pAlias the alias
     * @return a table
     */
    public String getTable(String pAlias) {
        return alias2Table.get(pAlias);
    }

    /**
     * Add an element to the select clause and add an alias.
     * <p>
     * The alias is the element without '.' character.
     * 
     * @param pElementId
     *            The id element to add : for alias pas.
     * @param pElement
     *            The element to add.
     */
    public void addElementInSelectClauseWithAlias(final String pElementId,
            final String pElement) {
        final String lAlias = StringUtils.replace(pElement, ".", "");

        alias2Field.put(pElementId, lAlias);
        appendToSelectClause(pElement + " AS " + lAlias);
    }

    /**
     * Add an element to the select clause;
     * 
     * @param pElement
     *            the element to add
     * @deprecated use {@link GPMQuery#appendToSelectClause(String)}
     */
    public void addElementInSelectClause(final String pElement) {
        appendToSelectClause(pElement);
    }

    /**
     * Add an element to the where clause
     * 
     * @param pElement
     *            the element to add
     */
    public void addElementInWhereClause(String pElement) {
        if (whereClause.length() > 0) {
            whereClause.append(" AND ( ");
        }
        else {
            whereClause.append(" ( ");
        }

        whereClause.append(pElement);

        whereClause.append(" )");
    }

    /**
     * return true if select clause contains element
     * 
     * @param pElement the element to search
     * @return true if select clause contains element
     */
    public boolean isSelectClauseContains(String pElement) {
        return selectClause.indexOf(pElement) > -1;
    }

    /**
     * Get the 'select' clause
     * 
     * @return the select clause
     */
    public String getSelectClause() {
        return selectClause.toString();
    }

    /**
     * Get the 'from' clause
     * 
     * @return the from clause
     */
    public String getFromClause() {
        return new StrBuilder().appendWithSeparators(fromClause, " ").toString();
    }

    /**
     * Get the 'where' clause
     * 
     * @return the where clause
     */
    public String getWhereClause() {
        return whereClause.toString();
    }

    /**
     * Get the 'order by' clause
     * 
     * @return the order by clause
     */
    public String getOrderByClause() {
        return orderByClause.toString();
    }

    /**
     * Get next counter value (and increment the counter)
     * 
     * @return the next counter value to use.
     */
    public int getCounter() {
        int lResult = counter;
        counter++;
        return lResult;
    }

    /**
     * Get the complete query from the different parts (clauses) SELECT DISTINCT
     * (if pDistinct) selectClause FROM containerPart (if filled) , fromClause
     * (if filled) WHERE whereClause ORDER BY orderByClause
     * 
     * @param pDistinct
     *            Use a 'distinct' clause.
     * @return A string containing the complete query.
     */
    public String getCompleteQuery(boolean pDistinct) {
        StringBuilder lCompleteQuery = new StringBuilder();
        if (!selectClause.isEmpty()) {
            lCompleteQuery.append("SELECT ");
            if (pDistinct) {
                lCompleteQuery.append("DISTINCT ");
            }
            StrBuilder lBuilder = new StrBuilder();
            lBuilder.appendWithSeparators(selectClause, ",");
            lCompleteQuery.append(lBuilder.toString());
        }

        if (!subQueries.isEmpty()) {
            for (Map.Entry<String, GPMSubQuery> lEntry : subQueries.entrySet()) {
                //Replace the sub query that is not already completed.
                //The sub query position in FROM clause is identifying by the
                //string $SubQueryIdentifier
                int lIndex = fromClause.indexOf("$" + lEntry.getKey());
                if (lIndex != -1) {
                    GPMSubQuery lSubQuery = lEntry.getValue();

                    StringBuilder lFromElement = new StringBuilder();
                    lFromElement.append(" ").append(lSubQuery.getJoinType()).append(
                            " ").append(
                            "(" + lSubQuery.getTable().getCompleteQuery(false)
                                    + ")").append(" ").append(
                            lSubQuery.getTableAlias()).append(" ON ").append(
                            lSubQuery.condition);
                    fromClause.set(lIndex, lFromElement.toString());
                }
            }
        }

        boolean lIsFromClauseFilled =
                fromClause != null && !fromClause.isEmpty();
        boolean lIsContainerPartFilled =
                containerPart != null && containerPart.length() > 0;

        if (lIsFromClauseFilled || lIsContainerPartFilled) {
            lCompleteQuery.append(" FROM ");
            if (lIsContainerPartFilled) {
                lCompleteQuery.append(containerPart);
                lCompleteQuery.append(" ");
            }
            if (lIsFromClauseFilled && lIsContainerPartFilled) {
                lCompleteQuery.append(", ");
            }
            if (lIsFromClauseFilled) {
                for (String lFromElement : fromClause) {
                    lCompleteQuery.append(lFromElement);
                    lCompleteQuery.append(" ");
                }

            }
        }
        if (!mandatoryCondition.isEmpty()) {
            for (String lCondition : mandatoryCondition) {
                addElementInWhereClause(lCondition);
            }
        }
        if (whereClause != null && whereClause.length() > 0) {
            lCompleteQuery.append(" WHERE ");
            lCompleteQuery.append(whereClause);
        }
        if (groupByClause != null && !groupByClause.isEmpty()
                && enableGroupByClause) {
            lCompleteQuery.append(" GROUP BY ");
            lCompleteQuery.append(groupByClause);
        }
        if (orderByClause != null && orderByClause.length() > 0) {
            lCompleteQuery.append(" ORDER BY ");
            lCompleteQuery.append(orderByClause);
        }
        if (!unionQuery.isEmpty()) {
            StrBuilder lStrBuilder = new StrBuilder();
            lStrBuilder.appendWithSeparators(unionQuery, " UNION ");
            lCompleteQuery.append(lStrBuilder.toStringBuffer());
        }

        return lCompleteQuery.toString();
    }

    /**
     * Get the complete query from the different parts (clauses) <br>
     * (SELECT <br>
     * selectClause <br>
     * FROM <br>
     * containerPart (if filled) <br>
     * , <br>
     * fromClause (if filled) <br>
     * WHERE (if filled) <br>
     * whereClause <br>
     * ORDER BY (if filled) <br>
     * orderByClause <br>
     * )</i> <br>
     * <br>
     * or if only whereClause is filled : <br>
     * <i>(whereClause)</i> <br>
     * (without preceding WHERE)
     * 
     * @return A string containing the query (.
     */
    public String getAsSubQuery() {
        StringBuilder lSubQuery = new StringBuilder();
        boolean lIsSelectFilled =
                selectClause != null && !selectClause.isEmpty();
        boolean lIsFromClauseFilled =
                fromClause != null && !fromClause.isEmpty();
        boolean lIsContainerPartFilled =
                containerPart != null && containerPart.length() > 0;
        boolean lIsWhereClauseFilled =
                whereClause != null && whereClause.length() > 0;
        boolean lIsOrderByClauseFilled =
                orderByClause != null && orderByClause.length() > 0;

        if (isExistingSubQuery) {
            lSubQuery.append("EXISTS ");
        }
        lSubQuery.append("(");
        if (lIsSelectFilled) {
            lSubQuery.append("SELECT ");
            lSubQuery.append(selectClause);
        }

        if (lIsFromClauseFilled || lIsContainerPartFilled) {
            lSubQuery.append(" FROM ");
            if (lIsContainerPartFilled) {
                lSubQuery.append(containerPart);
                lSubQuery.append(" ");
            }
            if (lIsFromClauseFilled && lIsContainerPartFilled) {
                lSubQuery.append(", ");
            }
            if (lIsFromClauseFilled) {
                lSubQuery.append(fromClause);
                lSubQuery.append(" ");
            }
        }
        if (lIsWhereClauseFilled) {
            if (lIsSelectFilled || lIsFromClauseFilled
                    || lIsContainerPartFilled || lIsOrderByClauseFilled) {
                lSubQuery.append(" WHERE ");
                lSubQuery.append(whereClause);
            }
        }
        if (lIsOrderByClauseFilled) {
            lSubQuery.append(" ORDER BY ");
            lSubQuery.append(orderByClause);
        }
        lSubQuery.append(")");
        return lSubQuery.toString();
    }

    /**
     * Get the complete query from the different parts (clauses)
     * 
     * @return A string containing the complete query.
     */
    public String getCompleteQuery() {
        return getCompleteQuery(true);
    }

    /**
     * Test the alias existence
     * 
     * @param pUsableFieldDataId
     *            Alias identifier
     * @return True if exists, false otherwise
     */
    public boolean isAlreadyMapped(final String pUsableFieldDataId) {
        return aliasGenerator.isAlreadyMapped(pUsableFieldDataId);
    }

    /**
     * Put mapped alias in table
     * 
     * @param pUsableFieldDataId the key
     * @param pAlias the value
     */
    public void putMappedAlias(final String pUsableFieldDataId,
            final String pAlias) {
        aliasGenerator.putMappedAlias(pUsableFieldDataId, pAlias);
    }

    /**
     * Get an existing alias
     * 
     * @param pUsableFieldDataId
     *            Identifier of the alias to retrieve
     * @return Alias
     */
    public String getMappedAlias(String pUsableFieldDataId) {
        return aliasGenerator.getMappedAlias(pUsableFieldDataId);
    }

    /**
     * Get the type of an alias.
     * 
     * @param pAlias
     *            The alias.
     * @return The alias type.
     */
    public AliasType getAliasType(final String pAlias) {
        return aliasGenerator.getAliasType(pAlias);
    }

    /**
     * Get the alias
     * 
     * @return Alias
     */
    public String getAlias() {
        return aliasGenerator.getCurrent();
    }

    /**
     * Set a new alias
     * 
     * @param pCurrent
     *            Alias to set
     */
    public void setAlias(String pCurrent) {
        aliasGenerator.setCurrent(pCurrent);
    }

    /**
     * Create an alias for this identifier.
     * 
     * @param pIdentifier
     *            Identifier of the new alias
     * @param pAliasType
     *            Type of the alias (for naming ease)
     * @return New alias
     */
    public String generateAlias(String pIdentifier, AliasType pAliasType) {
        return aliasGenerator.generate(pIdentifier, pAliasType);
    }

    /**
     * Retrieve an existing alias or create it.
     * 
     * @param pIdentifier
     *            Identifier of the alias
     * @param pAliasType
     *            Type of the alias
     * @return Alias
     */
    public String getOrGenerateAlias(String pIdentifier, AliasType pAliasType) {
        return aliasGenerator.getOrGenerate(pIdentifier, pAliasType);
    }

    public String getMainAlias() {
        return mainAlias;
    }

    public void setMainAlias(String pMainAlias) {
        mainAlias = pMainAlias;
    }

    /**
     * Generator for alias.
     * <p>
     * Generates indexed alias.<br />
     * The name of alias is determine by the AliasType. It's only use for naming
     * ease.<br/>
     * The name and index are separated by a separator '_'.
     */
    private class AliasGenerator {
        private String current;

        private Map<String, StringBuilder> mapped;

        private int sheetIndex;

        private int productIndex;

        private int linkIndex;

        private int fieldIndex;

        private int fieldMvIndex;

        private int unionIndex;

        private int attachedFieldIndex;

        private int choiceFieldIndex;

        private int valuesContainerIndex;

        private int fieldsContainerIndex;

        private int jbpmProcessInstanceIndex;

        private int jbpmTokenIndex;

        private int jbpmNodeIndex;

        private int lockIndex;

        private int scalarValueIndex;

        private static final String SEPARATOR = "_";

        protected AliasGenerator() {
            current = StringUtils.EMPTY;
            mapped = new HashMap<String, StringBuilder>();
        }

        protected String getCurrent() {
            return current;
        }

        protected void setCurrent(String pCurrent) {
            current = pCurrent;
        }

        public boolean isAlreadyMapped(final String pUsableFieldDataId) {
            return mapped.containsKey(pUsableFieldDataId);
        }

        public String getMappedAlias(String pUsableFieldDataId) {
            return mapped.get(pUsableFieldDataId).toString();
        }

        public void putMappedAlias(final String pUsableFieldDataId,
                final String pAlias) {
            mapped.put(pUsableFieldDataId, new StringBuilder(pAlias));
        }

        /**
         * Get the type of an alias.
         * 
         * @param pAlias
         *            The alias.
         * @return The alias type.
         */
        public AliasType getAliasType(final String pAlias) {
            return AliasType.valueOf(pAlias.split(SEPARATOR)[0].toUpperCase());
        }

        /**
         * Two exception for identifier:
         * <ul>
         * <li>LINK and type is LINK, alias is 'link'</li>
         * <li>UNION and type is UNION, alias is 'union_x' (else type and
         * identifier are different do nothing)</li>
         * </ul>
         * 
         * @param pIdentifier
         *            Identifier of the alias
         * @param pAliasType
         *            Type of the alias
         * @return Generated alias
         */
        protected String generate(String pIdentifier, AliasType pAliasType) {
            StringBuilder lAlias = new StringBuilder(pAliasType.getName());
            switch (pAliasType) {
                case SHEET:
                    lAlias.append(SEPARATOR).append(sheetIndex);
                    sheetIndex++;
                    break;
                case PRODUCT:
                    lAlias.append(SEPARATOR).append(productIndex);
                    productIndex++;
                    break;
                case LINK:
                    lAlias.append(SEPARATOR).append(linkIndex);
                    linkIndex++;
                    break;
                case FIELD:
                    lAlias.append(SEPARATOR).append(fieldIndex);
                    fieldIndex++;
                    break;
                case FIELD_MV:
                    lAlias.append(SEPARATOR).append(fieldMvIndex);
                    fieldMvIndex++;
                    break;
                case UNION:
                    if (AliasType.UNION.name().equals(pIdentifier)) {
                        lAlias.append(SEPARATOR).append(unionIndex);
                        unionIndex++;
                    }
                    break;
                case ATTACHED_FIELD:
                    lAlias.append(SEPARATOR).append(attachedFieldIndex);
                    attachedFieldIndex++;
                    break;
                case CHOICE_FIELD:
                    lAlias.append(SEPARATOR).append(choiceFieldIndex);
                    choiceFieldIndex++;
                    break;
                case VALUES_CONTAINER:
                    lAlias.append(SEPARATOR).append(valuesContainerIndex);
                    valuesContainerIndex++;
                    break;
                case FIELDS_CONTAINER:
                    lAlias.append(SEPARATOR).append(fieldsContainerIndex);
                    fieldsContainerIndex++;
                    break;
                case JBPM_PROCESSINSTANCE:
                    lAlias.append(SEPARATOR).append(jbpmProcessInstanceIndex);
                    jbpmProcessInstanceIndex++;
                    break;
                case JBPM_TOKEN:
                    lAlias.append(SEPARATOR).append(jbpmTokenIndex);
                    jbpmTokenIndex++;
                    break;
                case JBPM_NODE:
                    lAlias.append(SEPARATOR).append(jbpmNodeIndex);
                    jbpmNodeIndex++;
                    break;
                case LOCK:
                    lAlias.append(SEPARATOR).append(lockIndex);
                    lockIndex++;
                    break;
                case SCALAR_VALUE:
                    lAlias.append(SEPARATOR).append(scalarValueIndex);
                    scalarValueIndex++;
                    break;
                default:
                    throw new NotImplementedException(
                            "Unknowed type of alias '" + pAliasType + "'");
            }
            mapped.put(pIdentifier, lAlias);
            return lAlias.toString();
        }

        protected String getOrGenerate(String pIdentifier, AliasType pAliasType) {
            if (mapped.containsKey(pIdentifier)) {
                return mapped.get(pIdentifier).toString();
            }
            else {
                return generate(pIdentifier, pAliasType);
            }
        }
    }

    /**
     * Type of alias for naming ease.
     * 
     * @author mkargbo
     */
    public enum AliasType {
        SHEET(), PRODUCT(), LINK(), FIELD(), FIELD_MV(), UNION(),
        ATTACHED_FIELD(), CHOICE_FIELD(), VALUES_CONTAINER(), FIELDS_CONTAINER(),
        JBPM_PROCESSINSTANCE(), JBPM_TOKEN(), JBPM_NODE(), LOCK(), SCALAR_VALUE();

        public String getName() {
            return name().toLowerCase();
        }
    }

    /**
     * JOIN_TYPE enum
     */
    public enum JOIN_TYPE {
        JOIN("JOIN"), LEFT_JOIN("LEFT JOIN");

        private String value;

        private JOIN_TYPE(String pValue) {
            value = pValue;
        }

        /** {@inheritDoc} */
        public String toString() {
            return value;
        }
    }

    /**
     * GPMSubQuery class
     */
    public class GPMSubQuery {
        private JOIN_TYPE joinType;

        private GPMQuery table;

        private String tableAlias;

        private String condition;

        /**
         * Constructor
         * 
         * @param pJoinType the join type
         * @param pTable the table
         * @param pTableAlias the alias table
         * @param pCondition the condition
         */
        public GPMSubQuery(JOIN_TYPE pJoinType, GPMQuery pTable,
                String pTableAlias, String pCondition) {
            joinType = pJoinType;
            table = pTable;
            tableAlias = pTableAlias;
            condition = pCondition;
        }

        public JOIN_TYPE getJoinType() {
            return joinType;
        }

        public void setJoinType(JOIN_TYPE pJoinType) {
            joinType = pJoinType;
        }

        public GPMQuery getTable() {
            return table;
        }

        public void setTable(GPMQuery pTable) {
            table = pTable;
        }

        public String getTableAlias() {
            return tableAlias;
        }

        public void setTableAlias(String pTableAlias) {
            tableAlias = pTableAlias;
        }

        public String getCondition() {
            return condition;
        }

        public void setCondition(String pCondition) {
            condition = pCondition;
        }
    }

    /**
     * Get a field alias.
     * 
     * @param pElementId
     *            The element id.
     * @return The field alias.
     */
    public String getFieldAlias(final String pElementId) {
        return alias2Field.get(pElementId);
    }

    /**
     * If an element has a field alias.
     * 
     * @param pElementId
     *            The element id.
     * @return If an element has a field alias.
     */
    public boolean hasAlias(final String pElementId) {
        return alias2Field.containsKey(pElementId);
    }

    /**
     * Get all the field alias.
     * 
     * @return All the field alias.
     */
    public Map<String, String> getAllFieldAlias() {
        return alias2Field;
    }

    /**
     * Copy all the field alias from an other query.
     * 
     * @param pQuery
     *            The other query.
     */
    public void copyFieldAlias(final GPMQuery pQuery) {
        alias2Field.putAll(pQuery.getAllFieldAlias());
    }
}
