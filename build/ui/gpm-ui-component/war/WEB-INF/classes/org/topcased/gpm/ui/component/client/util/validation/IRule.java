/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.util.validation;

import org.topcased.gpm.ui.component.client.container.field.AbstractGpmField;

/**
 * IRule represents a rule that a field must respect.
 * 
 * @author mkargbo
 */
public interface IRule {

    /**
     * Check a field according to this rule.
     * 
     * @param pField
     *            Field to check.
     * @return <code>Null</code> if the field does not respect the rule, an
     *         error message otherwise.
     */
    public String check(AbstractGpmField<?> pField);
}