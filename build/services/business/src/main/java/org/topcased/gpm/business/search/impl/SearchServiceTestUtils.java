/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Anne Haugommard(Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.search.impl;

import java.util.Collection;

import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.exception.MethodNotImplementedException;
import org.topcased.gpm.business.scalar.BooleanValueData;
import org.topcased.gpm.business.scalar.DateValueData;
import org.topcased.gpm.business.scalar.IntegerValueData;
import org.topcased.gpm.business.scalar.RealValueData;
import org.topcased.gpm.business.scalar.ScalarValueData;
import org.topcased.gpm.business.scalar.StringValueData;
import org.topcased.gpm.business.search.criterias.CriteriaData;
import org.topcased.gpm.business.search.criterias.CriteriaFieldData;
import org.topcased.gpm.business.search.criterias.FilterData;
import org.topcased.gpm.business.search.criterias.OperationData;
import org.topcased.gpm.business.search.criterias.impl.FilterFieldsContainerInfo;
import org.topcased.gpm.business.search.result.sorter.ResultSortingData;
import org.topcased.gpm.business.search.result.sorter.SortingFieldData;
import org.topcased.gpm.business.search.result.summary.ResultSummaryData;
import org.topcased.gpm.business.search.service.ExecutableFilterData;
import org.topcased.gpm.business.search.service.FilterVisibilityConstraintData;
import org.topcased.gpm.business.search.service.UsableFieldData;

/**
 * Class used to check all the equalities between Search Service objects
 * 
 * @author ahaugomm
 */
public class SearchServiceTestUtils {

    /**
     * Check if object1 and Object2 are equal
     * 
     * @param pObject1
     *            first object
     * @param pObject2
     *            second object
     * @return true if both are null or if equal
     */
    private static boolean nullOrEquals(Object pObject1, Object pObject2) {
        if (pObject1 == null) {
            return (pObject2 == null);
        }
        // else
        return pObject1.equals(pObject2);
    }

    /**
     * Check if two CriteriaData are equal
     * 
     * @param pCriteriaData1
     *            first
     * @param pCriteriaData2
     *            second
     * @return true if equal
     */
    public static boolean areEqual(CriteriaData pCriteriaData1,
            CriteriaData pCriteriaData2) {
        boolean lBool = true;
        if (pCriteriaData1 instanceof CriteriaFieldData) {
            if (!(pCriteriaData2 instanceof CriteriaFieldData)) {
                return false;
            }
            CriteriaFieldData lCritField1 = (CriteriaFieldData) pCriteriaData1;
            CriteriaFieldData lCritField2 = (CriteriaFieldData) pCriteriaData2;
            lBool =
                    lBool
                            && (nullOrEquals(lCritField1.getOperator(),
                                    lCritField2.getOperator()));
            lBool =
                    lBool
                            && (areEqual(lCritField1.getScalarValueData(),
                                    lCritField2.getScalarValueData()));
            lBool =
                    lBool
                            && (areEqual(lCritField1.getUsableFieldData(),
                                    lCritField1.getUsableFieldData()));
        }
        else if (pCriteriaData1 instanceof OperationData) {
            if (!(pCriteriaData2 instanceof OperationData)) {
                return false;
            }
            OperationData lOp1 = (OperationData) pCriteriaData1;
            OperationData lOp2 = (OperationData) pCriteriaData2;
            lBool =
                    lBool
                            && (nullOrEquals(lOp1.getOperator(),
                                    lOp2.getOperator()));
            lBool =
                    lBool
                            && (arraysAreEqual(lOp1.getCriteriaDatas(),
                                    lOp2.getCriteriaDatas()));
        }
        else {
            throw new MethodNotImplementedException(
                    " areEqual for VirtualFieldCriteria data ");
        }
        return lBool;
    }

    /**
     * Check if the two are equal
     * 
     * @param pScalarValueData1
     *            first
     * @param pScalarValueData2
     *            second
     * @return true if equal
     */
    public static boolean areEqual(ScalarValueData pScalarValueData1,
            ScalarValueData pScalarValueData2) {
        boolean lBool = true;

        if (pScalarValueData1 == null) {
            return (pScalarValueData2 == null);
        }
        if (pScalarValueData1 instanceof StringValueData) {
            if (!(pScalarValueData2 instanceof StringValueData)) {
                return false;
            }
            StringValueData lStr1 = (StringValueData) pScalarValueData1;
            StringValueData lStr2 = (StringValueData) pScalarValueData2;
            lBool = lBool && (nullOrEquals(lStr1.getValue(), lStr2.getValue()));
        }
        else if (pScalarValueData1 instanceof RealValueData) {
            if (!(pScalarValueData2 instanceof RealValueData)) {
                return false;
            }
            RealValueData lScalar1 = (RealValueData) pScalarValueData1;
            RealValueData lScalar2 = (RealValueData) pScalarValueData2;
            lBool =
                    lBool
                            && (nullOrEquals(lScalar1.getValue(),
                                    lScalar2.getValue()));
        }
        else if (pScalarValueData1 instanceof BooleanValueData) {
            if (!(pScalarValueData2 instanceof BooleanValueData)) {
                return false;
            }
            BooleanValueData lScalar1 = (BooleanValueData) pScalarValueData1;
            BooleanValueData lScalar2 = (BooleanValueData) pScalarValueData2;
            lBool = lBool && (lScalar1.getValue() == lScalar2.getValue());
        }
        else if (pScalarValueData1 instanceof DateValueData) {
            if (!(pScalarValueData2 instanceof DateValueData)) {
                return false;
            }
            DateValueData lScalar1 = (DateValueData) pScalarValueData1;
            DateValueData lScalar2 = (DateValueData) pScalarValueData2;
            lBool =
                    lBool
                            && (nullOrEquals(lScalar1.getValue(),
                                    lScalar2.getValue()));
        }
        else if (pScalarValueData1 instanceof IntegerValueData) {
            if (!(pScalarValueData2 instanceof IntegerValueData)) {
                return false;
            }
            IntegerValueData lScalar1 = (IntegerValueData) pScalarValueData1;
            IntegerValueData lScalar2 = (IntegerValueData) pScalarValueData2;
            lBool =
                    lBool
                            && (nullOrEquals(lScalar1.getValue(),
                                    lScalar2.getValue()));
        }
        else {
            throw new GDMException("invalid ScalarValueData");
        }

        return lBool;
    }

    /**
     * Check if the two are equal
     * 
     * @param pUsableFieldData1
     *            first
     * @param pUsableFieldData2
     *            second
     * @return true if equal
     */
    public static boolean areEqual(UsableFieldData pUsableFieldData1,
            UsableFieldData pUsableFieldData2) {
        boolean lBool = true;
        if (pUsableFieldData1 == null) {
            return (pUsableFieldData2 == null);
        }
        lBool =
                lBool
                        && (nullOrEquals(pUsableFieldData1.getFieldName(),
                                pUsableFieldData2.getFieldName()));
        lBool =
                lBool
                        && (nullOrEquals(pUsableFieldData1.getFieldType(),
                                pUsableFieldData2.getFieldType()));
        return lBool;
    }

    /**
     * Check if the two are equal
     * 
     * @param pF1
     *            first filter visibility constraint
     * @param pF2
     *            second
     * @return true if equal
     */
    public static boolean areEqual(FilterVisibilityConstraintData pF1,
            FilterVisibilityConstraintData pF2) {
        boolean lBool = true;
        if (pF1 == null) {
            return (pF2 == null);
        }
        lBool =
                lBool
                        && (nullOrEquals(pF1.getBusinessProcessName(),
                                pF2.getBusinessProcessName()));
        lBool =
                lBool
                        && (nullOrEquals(pF1.getProductName(),
                                pF2.getProductName()));
        lBool = lBool && (nullOrEquals(pF1.getUserLogin(), pF2.getUserLogin()));

        return lBool;
    }

    /**
     * Check if the two are equal
     * 
     * @param pExecutableFilterData1
     *            first
     * @param pExecutableFilterData2
     *            second
     * @return true if equal
     */
    public static boolean areEqual(ExecutableFilterData pExecutableFilterData1,
            ExecutableFilterData pExecutableFilterData2) {
        boolean lBool = true;
        if (pExecutableFilterData1 == null) {
            return (pExecutableFilterData2 == null);
        }
        lBool =
                lBool
                        && (nullOrEquals(pExecutableFilterData1.getLabelKey(),
                                pExecutableFilterData2.getLabelKey()));
        lBool =
                lBool
                        && (areEqual(
                                pExecutableFilterData1.getFilterVisibilityConstraintData(),
                                pExecutableFilterData2.getFilterVisibilityConstraintData()));
        lBool =
                lBool
                        && (nullOrEquals(pExecutableFilterData1.getUsage(),
                                pExecutableFilterData2.getUsage()));
        lBool =
                lBool
                        && (areEqual(pExecutableFilterData1.getFilterData(),
                                pExecutableFilterData2.getFilterData()));
        lBool =
                lBool
                        && (areEqual(
                                pExecutableFilterData1.getResultSortingData(),
                                pExecutableFilterData2.getResultSortingData()));
        lBool =
                lBool
                        && (areEqual(
                                pExecutableFilterData1.getResultSummaryData(),
                                pExecutableFilterData2.getResultSummaryData()));
        return lBool;
    }

    /**
     * Check if the two are equal
     * 
     * @param pFilterData1
     *            first
     * @param pFilterData2
     *            second
     * @return true if equal
     */
    public static boolean areEqual(FilterData pFilterData1,
            FilterData pFilterData2) {
        boolean lBool = true;
        if (pFilterData1 == null) {
            return (pFilterData2 == null);
        }
        lBool =
                lBool
                        && (nullOrEquals(pFilterData1.getLabelKey(),
                                pFilterData2.getLabelKey()));
        lBool =
                lBool
                        && (areEqual(pFilterData1.getCriteriaData(),
                                pFilterData2.getCriteriaData()));
        lBool =
                lBool
                        && (arraysNullOrEqual(
                                pFilterData1.getFieldsContainerIds(),
                                pFilterData2.getFieldsContainerIds()));
        lBool =
                lBool
                        && (areEqual(
                                pFilterData1.getFilterVisibilityConstraintData(),
                                pFilterData2.getFilterVisibilityConstraintData()));
        return lBool;
    }

    /**
     * Check if the two are equal
     * 
     * @param pResultSortingData1
     *            first
     * @param pResultSortingData2
     *            second
     * @return true if equal
     */
    public static boolean areEqual(ResultSortingData pResultSortingData1,
            ResultSortingData pResultSortingData2) {
        boolean lBool = true;
        if (pResultSortingData1 == null) {
            return (pResultSortingData2 == null);
        }
        lBool =
                lBool
                        && (nullOrEquals(pResultSortingData1.getLabelKey(),
                                pResultSortingData2.getLabelKey()));
        lBool =
                lBool
                        && (areEqual(
                                pResultSortingData1.getFilterVisibilityConstraintData(),
                                pResultSortingData2.getFilterVisibilityConstraintData()));
        lBool =
                lBool
                        && (arraysAreEqual(
                                pResultSortingData1.getSortingFieldDatas(),
                                pResultSortingData2.getSortingFieldDatas()));
        lBool =
                lBool
                        && (arraysNullOrEqual(
                                pResultSortingData1.getFieldsContainerIds(),
                                pResultSortingData2.getFieldsContainerIds()));
        return lBool;
    }

    /**
     * Check if the two are equal
     * 
     * @param pResultSummaryData1
     *            first
     * @param pResultSummaryData2
     *            second
     * @return true if equal
     */
    public static boolean areEqual(ResultSummaryData pResultSummaryData1,
            ResultSummaryData pResultSummaryData2) {
        boolean lBool = true;
        if (pResultSummaryData1 == null) {
            return (pResultSummaryData2 == null);
        }
        lBool =
                lBool
                        && (nullOrEquals(pResultSummaryData1.getLabelKey(),
                                pResultSummaryData2.getLabelKey()));
        lBool =
                lBool
                        && (areEqual(
                                pResultSummaryData1.getFilterVisibilityConstraintData(),
                                pResultSummaryData2.getFilterVisibilityConstraintData()));
        lBool =
                lBool
                        && (arraysAreEqual(
                                pResultSummaryData1.getUsableFieldDatas(),
                                pResultSummaryData2.getUsableFieldDatas()));
        lBool =
                lBool
                        && (arraysNullOrEqual(
                                pResultSummaryData1.getFieldsContainerIds(),
                                pResultSummaryData2.getFieldsContainerIds()));
        return lBool;
    }

    /**
     * Check if the two are equal
     * 
     * @param pObject1
     *            first
     * @param pObject2
     *            second
     * @return true if equal
     */
    public static boolean areEqual(Object pObject1, Object pObject2) {

        if (pObject1 == null) {
            return (pObject2 == null);
        }
        else if (pObject1 instanceof CriteriaData) {
            return (areEqual((CriteriaData) pObject1, (CriteriaData) pObject2));
        }
        else if (pObject1 instanceof ScalarValueData) {
            return (areEqual((ScalarValueData) pObject1,
                    (ScalarValueData) pObject2));
        }
        else if (pObject1 instanceof SortingFieldData) {
            return (areEqual((SortingFieldData) pObject1,
                    (SortingFieldData) pObject2));
        }
        else if (pObject1 instanceof UsableFieldData) {
            return (areEqual((UsableFieldData) pObject1,
                    (UsableFieldData) pObject2));
        }
        else if (pObject1 instanceof FilterVisibilityConstraintData) {
            return (areEqual((FilterVisibilityConstraintData) pObject1,
                    (FilterVisibilityConstraintData) pObject2));
        }
        else if (pObject1 instanceof ExecutableFilterData) {
            return (areEqual((ExecutableFilterData) pObject1,
                    (ExecutableFilterData) pObject2));
        }
        else if (pObject1 instanceof FilterData) {
            return (areEqual((FilterData) pObject1, (FilterData) pObject2));
        }
        else if (pObject1 instanceof ResultSortingData) {
            return (areEqual((ResultSortingData) pObject1,
                    (ResultSortingData) pObject2));
        }
        else if (pObject1 instanceof ResultSummaryData) {
            return (areEqual((ResultSummaryData) pObject1,
                    (ResultSummaryData) pObject2));
        }
        else if (pObject1 instanceof FilterFieldsContainerInfo) {
            return (areEqual((FilterFieldsContainerInfo) pObject1,
                    (FilterFieldsContainerInfo) pObject2));
        }
        else {
            throw new MethodNotImplementedException("areEqual(Object, Object)");
        }
    }

    /**
     * Check if the two are equal
     * 
     * @param pSortingFieldData1
     *            first
     * @param pSortingFieldData2
     *            second
     * @return true if equal
     */
    public static boolean areEqual(SortingFieldData pSortingFieldData1,
            SortingFieldData pSortingFieldData2) {
        boolean lBool = true;
        if (pSortingFieldData1 == null) {
            return (pSortingFieldData2 == null);
        }
        lBool =
                lBool
                        && (areEqual(pSortingFieldData1.getUsableFieldData(),
                                pSortingFieldData2.getUsableFieldData()));
        lBool =
                lBool
                        && (nullOrEquals(pSortingFieldData1.getOrder(),
                                pSortingFieldData2.getOrder()));
        return lBool;
    }

    /**
     * Check if the two are equal
     * 
     * @param pFilterFieldsContainerInfo1
     *            first
     * @param pFilterFieldsContainerInfo2
     *            second
     * @return true if equal
     */
    public static boolean areEqual(
            FilterFieldsContainerInfo pFilterFieldsContainerInfo1,
            FilterFieldsContainerInfo pFilterFieldsContainerInfo2) {
        boolean lBool = true;
        if (pFilterFieldsContainerInfo1 == null) {
            return (pFilterFieldsContainerInfo2 == null);
        }
        lBool =
                lBool
                        && (nullOrEquals(pFilterFieldsContainerInfo1.getId(),
                                pFilterFieldsContainerInfo2.getId()));
        lBool =
                lBool
                        && (nullOrEquals(
                                pFilterFieldsContainerInfo1.getLabelKey(),
                                pFilterFieldsContainerInfo2.getLabelKey()));
        lBool =
                lBool
                        && (nullOrEquals(pFilterFieldsContainerInfo1.getType(),
                                pFilterFieldsContainerInfo2.getType()));
        lBool =
                lBool
                        && (nullOrEquals(
                                pFilterFieldsContainerInfo1.getLinkDirection(),
                                pFilterFieldsContainerInfo2.getLinkDirection()));
        return lBool;
    }

    /**
     * Check if the two arrays are equal
     * 
     * @param pArray1
     *            the first array
     * @param pArray2
     *            the second array
     * @return true if equal
     */
    public static boolean arraysAreEqual(Object[] pArray1, Object[] pArray2) {
        if (pArray1 == null) {
            return pArray2 == null;
        }
        if (pArray1.length != pArray2.length) {
            return false;
        }
        for (Object lObject1 : pArray1) {
            boolean lIsInArray2 = false;
            for (Object lObject2 : pArray2) {
                if (areEqual(lObject1, lObject2)) {
                    lIsInArray2 = true;
                    break;
                }
            }
            if (!lIsInArray2) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check if the two arrays are equal
     * 
     * @param pArray1
     *            the first array
     * @param pArray2
     *            the second array
     * @return true if equal
     */
    public static boolean arraysNullOrEqual(Object[] pArray1, Object[] pArray2) {
        if (pArray1 == null) {
            return pArray2 == null;
        }
        if (pArray1.length != pArray2.length) {
            return false;
        }
        for (Object lObject1 : pArray1) {
            boolean lIsInArray2 = false;
            for (Object lObject2 : pArray2) {
                if (nullOrEquals(lObject1, lObject2)) {
                    lIsInArray2 = true;
                    break;
                }
            }
            if (!lIsInArray2) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check if the object is contained in the collection
     * 
     * @param pObject
     *            the object
     * @param pList
     *            the list
     * @return true if contained
     */
    public static boolean isContained(Object pObject, Collection<?> pList) {
        for (Object lObject : pList) {
            if (areEqual(pObject, lObject)) {
                return true;
            }
        }
        return false;
    }
}
