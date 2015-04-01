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

import org.topcased.gpm.ui.facade.shared.dictionary.UiCategory;
import org.topcased.gpm.ui.facade.shared.dictionary.UiEnvironment;

/**
 * Create Environment Result
 * 
 * @author jlouisy
 */
public class CreateEnvironmentResult extends GetEnvironmentCategoriesResult {

    /** serialVersionUID */
    private static final long serialVersionUID = 8912167514715498964L;

    private List<String> environmentList;

    /**
     * Create CreateEnvironmentResult
     */
    public CreateEnvironmentResult() {
    }

    /**
     * Create CreateEnvironmentResult with values
     * 
     * @param pEnvironment
     *            Environment.
     * @param pCategories
     *            Categories.
     * @param pEnvironmentList
     *            Environment list.
     */
    public CreateEnvironmentResult(UiEnvironment pEnvironment,
            List<UiCategory> pCategories, List<String> pEnvironmentList) {
        super(pEnvironment, pCategories);
        environmentList = pEnvironmentList;
    }

    /**
     *Get environment list.
     * 
     * @return environment list.
     */
    public List<String> getEnvironmentList() {
        return environmentList;
    }
}
