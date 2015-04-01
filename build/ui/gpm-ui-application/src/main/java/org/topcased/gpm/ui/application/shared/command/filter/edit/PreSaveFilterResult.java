/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.shared.command.filter.edit;

import java.util.List;

import net.customware.gwt.dispatch.shared.Result;

import org.topcased.gpm.ui.facade.shared.filter.UiFilterUsage;
import org.topcased.gpm.ui.facade.shared.filter.UiFilterVisibility;

/**
 * PreSaveFilterResult
 * 
 * @author nveillet
 */
public class PreSaveFilterResult implements Result {

    /** serialVersionUID */
    private static final long serialVersionUID = -5186691865508581228L;

    private List<UiFilterVisibility> availableVisibilities;

    private String filterDescription;

    private String filterName;

    private UiFilterVisibility filterVisibility;

    private UiFilterUsage filterUsage;

    private Boolean isHidden;

    /**
     * Empty constructor for serialization.
     */
    public PreSaveFilterResult() {
    }

    /**
     * Create PreSaveFilterResult with values
     * 
     * @param pFilterName
     *            the filter name
     * @param pFilterDescription
     *            the filter description
     * @param pAvailableVisibilities
     *            the available visibilities
     * @param pFilterVisibility
     *            the filter visibility
     * @param pFilterUsage
     *            the filter usage
     * @param pIsHidden
     *            Is filter hidden (may be null if user does not have access to
     *            this attribute).
     */
    public PreSaveFilterResult(String pFilterName, String pFilterDescription,
            List<UiFilterVisibility> pAvailableVisibilities,
            UiFilterVisibility pFilterVisibility, UiFilterUsage pFilterUsage,
            Boolean pIsHidden) {
        availableVisibilities = pAvailableVisibilities;
        filterName = pFilterName;
        filterDescription = pFilterDescription;
        filterVisibility = pFilterVisibility;
        filterUsage = pFilterUsage;
        isHidden = pIsHidden;
    }

    /**
     * get available visibilities
     * 
     * @return the available visibilities
     */
    public List<UiFilterVisibility> getAvailableVisibilities() {
        return availableVisibilities;
    }

    /**
     * get filter description
     * 
     * @return the filter description
     */
    public String getFilterDescription() {
        return filterDescription;
    }

    /**
     * get filter name
     * 
     * @return the filter name
     */
    public String getFilterName() {
        return filterName;
    }

    /**
     * get filter visibility
     * 
     * @return the filter visibility
     */
    public UiFilterVisibility getFilterVisibility() {
        return filterVisibility;
    }

    /**
     * get filter usage
     * 
     * @return the filter usage
     */
    public UiFilterUsage getFilterUsage() {
        return filterUsage;
    }

    /**
     * Is filter hidden (may be null if user does not have access to this
     * attribute).
     * 
     * @return true/false/null
     */
    public Boolean isHidden() {
        return isHidden;
    }

}
