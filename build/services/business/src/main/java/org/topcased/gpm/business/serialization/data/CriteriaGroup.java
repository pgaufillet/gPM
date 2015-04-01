/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin), Sophian Idjellidaine (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.serialization.data;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * The Class CriteriaGroup.
 * 
 * @author llatil
 */
@XStreamAlias("criteriaGroup")
public class CriteriaGroup implements Serializable {

    /**
     * Default serial ID 
     */
    private static final long serialVersionUID = 1L;
    
    /** The criterion list. */
    @XStreamImplicit(itemFieldName = "criterion")
    private List<Criterion> criterionList;

    /**
     * Gets the criterion list.
     * 
     * @return the criterion list
     */
    public List<Criterion> getCriterionList() {
        return criterionList;
    }

    /**
     * set criterionList
     * 
     * @param pCriterionList
     *            the criterionList to set
     */
    public void setCriterionList(List<Criterion> pCriterionList) {
        criterionList = pCriterionList;
    }

}
