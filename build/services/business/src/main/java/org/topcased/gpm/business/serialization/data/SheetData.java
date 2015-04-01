/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin), Sophian Idjellidaine (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.serialization.data;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang.StringUtils;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * Class mapping a sheet content.
 * 
 * @author sidjelli
 */
@XStreamAlias("sheet")
public class SheetData extends ValuesContainerData {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -8957017287756438031L;

    /** Name of current state of the sheet. */
    @XStreamAsAttribute
    private String state;

    /**
     * Individual values of the reference (used to define / update the
     * reference.
     */
    @XStreamAlias(value = "referenceValues", impl = LinkedList.class)
    private List<FieldValueData> referenceValues;

    /** Array list containing revisionsData on this sheet. */
    @XStreamAlias(value = "revisions", impl = LinkedList.class)
    private List<RevisionData> revisions;

    /** Transition history of the sheet. */
    @XStreamAlias(value = "transitionsHistory", impl = LinkedList.class)
    private List<TransitionHistoryData> transitionsHistory;

    /** Transition rule of the sheet. */
    @XStreamImplicit(itemFieldName = "rule")
    private List<RuleData> rules;

    /**
     * Gets the reference values.
     * 
     * @return the referenceValues
     */
    public List<FieldValueData> getReferenceValues() {
        return referenceValues;
    }

    /**
     * Sets the reference values.
     * 
     * @param pReferenceValues
     *            the referenceValues to set
     */
    public void setReferenceValues(List<FieldValueData> pReferenceValues) {
        referenceValues = pReferenceValues;
    }

    /**
     * Gets the state.
     * 
     * @return the state
     */
    public String getState() {
        return state;
    }

    /**
     * Sets the state.
     * 
     * @param pState
     *            the new state
     */
    public void setState(String pState) {
        state = pState;
    }

    /**
     * get revisions.
     * 
     * @return the revisions
     */
    public List<RevisionData> getRevisions() {
        return revisions;
    }

    /**
     * set pRevisions.
     * 
     * @param pRevisions
     *            the revisions to set
     */
    public void setRevisions(List<RevisionData> pRevisions) {
        revisions = pRevisions;
    }

    /**
     * Gets the transitions history.
     * 
     * @return the transitions history
     */
    public final List<TransitionHistoryData> getTransitionsHistory() {
        return transitionsHistory;
    }

    /**
     * Sets the transitions history.
     * 
     * @param pTransitionsHistory
     *            the new transitions history
     */
    public final void setTransitionsHistory(
            List<TransitionHistoryData> pTransitionsHistory) {
        transitionsHistory = pTransitionsHistory;
    }

    /**
     * Get the rule list
     * 
     * @return The rule list
     */
    public List<RuleData> getRules() {
        if (rules == null) {
            rules = new ArrayList<RuleData>();
        }
        return rules;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object pOther) {

        if (pOther instanceof SheetData) {
            SheetData lOther = (SheetData) pOther;

            if (!super.equals(lOther)) {
                return false;
            }
            if (!StringUtils.equals(state, lOther.state)) {
                return false;
            }
            if (!ListUtils.isEqualList(referenceValues, lOther.referenceValues)) {
                return false;
            }
            if (!ListUtils.isEqualList(transitionsHistory,
                    lOther.transitionsHistory)) {
                return false;
            }
            //TODO revision
            if (!ListUtils.isEqualList(revisions, lOther.revisions)) {
                return false;
            }

            return true;
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        // ValuesContainerData hashcode is sufficient
        return super.hashCode();
    }
}
