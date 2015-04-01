/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien LOUISY (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.shared.command.dictionary;

import java.util.List;

import net.customware.gwt.dispatch.shared.Result;

import org.topcased.gpm.ui.facade.shared.dictionary.UiCategory;
import org.topcased.gpm.ui.facade.shared.dictionary.UiEnvironment;

/**
 * GetEnvironmentResult
 * 
 * @author jlouisy
 */
public class GetEnvironmentCategoriesResult implements Result {

    /** serialVersionUID */
    private static final long serialVersionUID = 2512283447577395071L;

    private UiEnvironment environment;

    private List<UiCategory> categories;

    /**
     * Create GetEnvironmentResult
     */
    public GetEnvironmentCategoriesResult() {
    }

    /**
     * Create GetEnvironmentResult with values
     * 
     * @param pEnvironment
     *            Environment.
     * @param pCategories
     *            Categories.
     */
    public GetEnvironmentCategoriesResult(UiEnvironment pEnvironment,
            List<UiCategory> pCategories) {
        super();
        environment = pEnvironment;
        categories = pCategories;
    }

    /**
     * Get environment.
     * 
     * @return environment.
     */
    public UiEnvironment getEnvironment() {
        return environment;
    }

    /**
     * Get categories.
     * 
     * @return categories list.
     */
    public List<UiCategory> getCategories() {
        return categories;
    }
}
