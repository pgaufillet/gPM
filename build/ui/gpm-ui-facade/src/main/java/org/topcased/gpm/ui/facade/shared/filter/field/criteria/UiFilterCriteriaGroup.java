/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.shared.filter.field.criteria;

import java.io.Serializable;
import java.util.List;

/**
 * UiFilterCriteriaGroup
 * 
 * @author nveillet
 */
public class UiFilterCriteriaGroup implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = -7535523295157983751L;

    private List<UiFilterCriterion> criteria;

    /**
     * Constructor
     */
    public UiFilterCriteriaGroup() {
    }

    /**
     * Constructor with criteria
     * 
     * @param pCriteria
     *            the criteria
     */
    public UiFilterCriteriaGroup(List<UiFilterCriterion> pCriteria) {
        criteria = pCriteria;
    }

    /**
     * get criteria
     * 
     * @return the criteria
     */
    public List<UiFilterCriterion> getCriteria() {
        return criteria;
    }

    /**
     * set criteria
     * 
     * @param pCriteria
     *            the criteria to set
     */
    public void setCriteria(List<UiFilterCriterion> pCriteria) {
        criteria = pCriteria;
    }

}
