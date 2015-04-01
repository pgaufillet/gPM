/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien LOUISY (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.popup.filter.edit;

import static org.topcased.gpm.ui.component.client.resources.i18n.Ui18n.CONSTANTS;

/**
 * FilterEditionStep
 * 
 * @author jeballar
 */
public enum FilterEditionStep {
    STEP_1_CHOOSE_CONTAINERS(CONSTANTS.filterEditionStep1()), STEP_2_CHOOSE_PRODUCT(
            CONSTANTS.filterEditionStep2()), STEP_3_RESULT_FIELDS(
            CONSTANTS.filterEditionStep3()), STEP_4_CHOOSE_CRITERIA(
            CONSTANTS.filterEditionStep4()), STEP_5_CHOOSE_SORTING(
            CONSTANTS.filterEditionStep5());

    private String value;

    private FilterEditionStep(String pValue) {
        value = pValue;
    }

    public String getValue() {
        return value;
    }

}
