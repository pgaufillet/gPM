/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien LOUISY (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.shared.container.field.simple;

import java.util.List;

import org.topcased.gpm.business.util.ChoiceDisplayHintType;
import org.topcased.gpm.business.util.Translation;

/**
 * UiChoiceTreeField
 * 
 * @author jlouisy
 */
public class UiChoiceTreeField extends UiChoiceField {

    /** serialVersionUID */
    private static final long serialVersionUID = 7308531794188833851L;

    /**
     * Create new UiChoiceField
     */
    public UiChoiceTreeField() {
        super();
        setChoiceDisplayHintType(ChoiceDisplayHintType.TREE);
    }

    // TODO CHECK IF METHOD IS NECESSARY :
    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.facade.shared.container.field.UiField#getEmptyClone()
     */
    @Override
    public UiChoiceTreeField getEmptyClone() {
        UiChoiceTreeField lField = new UiChoiceTreeField();
        lField.setFieldName(getFieldName());
        lField.setFieldDescription(getFieldDescription());
        lField.setMandatory(isMandatory());
        lField.setUpdatable(isUpdatable());

        for (Translation lPossibleValues : getPossibleValues()) {
            lField.addPossibleValue(lPossibleValues);
        }

        return lField;
    }

    /**
     * Set possible values.
     * 
     * @param pRootList
     *            values to set.
     */
    public void setPossibleValues(List<Translation> pRootList) {
        possibleValues = pRootList;
    }
}
