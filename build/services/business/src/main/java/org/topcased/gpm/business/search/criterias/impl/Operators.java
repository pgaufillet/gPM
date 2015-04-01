/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Anne Haugommard(Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.search.criterias.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.topcased.gpm.business.fields.FieldType;
import org.topcased.gpm.business.values.field.virtual.VirtualFieldType;

/**
 * Class used to manage the operators in criterias
 * 
 * @author ahaugomm
 */
public class Operators {

    /** ">" operator */
    public static final String GT = ">";

    /** "<" operator */
    public static final String LT = "<";

    /** ">=" operator */
    public static final String GE = ">=";

    /** "<=" operator */
    public static final String LE = "<=";

    /** "=" operator */
    public static final String EQ = "=";

    /** "like" operator */
    public static final String LIKE = "like";

    /** "<>" operator */
    public static final String NEQ = "<>";

    /** "not like" operator */
    public static final String NOT_LIKE = "not like";

    /** This operator will be converted before the query is run */
    public static final String SINCE = "since";

    /** This operator will be converted before the query is run */
    public static final String OTHER = "other";

    /**
     * Only available for product scope definition.
     */
    public static final String IN = "in";

    /*
     * Criteria operators
     * */
    /**
     *
     */
    public static final String AND = "and";

    /**
     *
     */
    public static final String OR = "or";

    /*
     * Order by parameters
     * */
    /**
     *
     */
    public static final String ASC = "asc";

    /**
     *
     */
    public static final String DESC = "desc";

    public static final String DEF_ASC = "def_asc";

    public static final String DEF_DESC = "def_desc";

    /**
     * get the list of compatible operators to use in a criteria for a given
     * field type
     * 
     * @param pFieldType
     *            the field type (from class FieldTypes : e.g.
     *            FieldTypes.CHOICE_FIELD...)
     * @return the collection of operators
     */
    public static Collection<String> getCompatibleOperators(FieldType pFieldType) {

        Collection<String> lRes = new ArrayList<String>();
        if ((FieldType.ATTACHED_FIELD.equals(pFieldType))
                || (FieldType.SIMPLE_STRING_FIELD.equals(pFieldType))) {

            lRes.add(Operators.EQ);
            lRes.add(Operators.NEQ);
            lRes.add(Operators.LIKE);
            lRes.add(Operators.NOT_LIKE);
        }
        else if (FieldType.SIMPLE_BOOLEAN_FIELD.equals(pFieldType)
                || FieldType.CHOICE_FIELD.equals(pFieldType)) {
            lRes.add(Operators.EQ);
            lRes.add(Operators.NEQ);
            lRes.add(Operators.LIKE);
            lRes.add(Operators.NOT_LIKE);
        }
        else if (FieldType.SIMPLE_INTEGER_FIELD.equals(pFieldType)
                || FieldType.SIMPLE_REAL_FIELD.equals(pFieldType)
                || FieldType.SIMPLE_DATE_FIELD.equals(pFieldType)) {
            lRes.add(Operators.EQ);
            lRes.add(Operators.NEQ);
            lRes.add(Operators.GE);
            lRes.add(Operators.GT);
            lRes.add(Operators.LE);
            lRes.add(Operators.LT);
            lRes.add(Operators.SINCE);
            lRes.add(Operators.OTHER);
        }
        return lRes;
    }

    /**
     * Get the sort operator that have been compatible with the specified field
     * type.
     * 
     * @param pFieldType
     *            Field type
     * @return Compatible sort operators.
     */
    public static Collection<String> getSort(FieldType pFieldType) {
        final Collection<String> lResult;
        switch (pFieldType) {
            case SIMPLE_STRING_FIELD:
            case SIMPLE_INTEGER_FIELD:
            case SIMPLE_REAL_FIELD:
            case SIMPLE_BOOLEAN_FIELD:
            case SIMPLE_DATE_FIELD:
            case ATTACHED_FIELD:
                lResult = new ArrayList<String>(2);
                lResult.add(ASC);
                lResult.add(DESC);
                break;
            case CHOICE_FIELD:
                lResult = new ArrayList<String>(4);
                lResult.add(ASC);
                lResult.add(DESC);
                lResult.add(DEF_ASC);
                lResult.add(DEF_DESC);
                break;
            default:
                lResult = Collections.emptyList();
        }

        return lResult;
    }

    /**
     * Get available sort operator for virtual field.
     * 
     * @param pVirtualFieldType
     *            Virtual field type.
     * @return Sort operator.
     */
    public static Collection<String> getSort(VirtualFieldType pVirtualFieldType) {
        Collection<String> lResult = new ArrayList<String>(2);
        lResult.add(ASC);
        lResult.add(DESC);
        return lResult;
    }
}
