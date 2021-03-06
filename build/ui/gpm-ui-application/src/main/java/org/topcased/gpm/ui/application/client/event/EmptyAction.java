/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.event;

import net.customware.gwt.dispatch.shared.Action;
import net.customware.gwt.dispatch.shared.Result;

/**
 * An action without server treatment.
 * 
 * @author tpanuel
 * @param <R>
 *            The type of result.
 */
@SuppressWarnings("rawtypes")
public class EmptyAction<R extends EmptyAction> implements Result, Action<R> {
    private static final long serialVersionUID = 8088874607549324389L;
}