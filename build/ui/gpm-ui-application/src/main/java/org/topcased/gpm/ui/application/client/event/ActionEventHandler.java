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

import net.customware.gwt.dispatch.shared.Result;

import com.google.gwt.event.shared.EventHandler;

/**
 * An event handler that can be used like an async call back.
 * 
 * @author tpanuel
 * @param <R>
 *            The result type.
 */
public abstract class ActionEventHandler<R extends Result> implements
        EventHandler {
    public abstract void execute(R pResult);
}