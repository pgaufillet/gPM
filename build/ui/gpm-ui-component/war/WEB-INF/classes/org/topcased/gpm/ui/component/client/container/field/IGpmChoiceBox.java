/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.container.field;

import java.util.List;

/**
 * IGpmChoiceBox
 * 
 * @author nveillet
 */
public interface IGpmChoiceBox {

    /**
     * Set the possible values to the choice field box, using an object
     * reflecting the value/displayed value behavior of
     * <code>IGpmChoiceBox</code>
     * 
     * @param pPossibleValues
     *            The values to set in the choice field box.
     */
    public void setPossibleValues(final List<GpmChoiceBoxValue> pPossibleValues);
}
