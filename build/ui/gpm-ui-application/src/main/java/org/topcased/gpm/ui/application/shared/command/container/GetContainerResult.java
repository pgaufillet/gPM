/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.shared.command.container;

import java.util.Map;

import net.customware.gwt.dispatch.shared.Result;

import org.topcased.gpm.business.util.DisplayMode;
import org.topcased.gpm.ui.facade.shared.action.UiAction;
import org.topcased.gpm.ui.facade.shared.container.UiContainer;

/**
 * GetContainerResult
 * 
 * @param <C>
 *            the type of container
 * @author nveillet
 */
public class GetContainerResult<C extends UiContainer> implements Result {

    /** serialVersionUID */
    private static final long serialVersionUID = 4913744659753801239L;

    private Map<String, UiAction> actions;

    private C container;

    private DisplayMode displayMode;

    /**
     * Empty constructor for serialization.
     */
    protected GetContainerResult() {
    }

    /**
     * Constructor with values
     * 
     * @param pContainer
     *            the container
     * @param pDisplayMode
     *            the display mode
     * @param pActions
     *            the actions
     */
    protected GetContainerResult(C pContainer, DisplayMode pDisplayMode,
            Map<String, UiAction> pActions) {
        container = pContainer;
        displayMode = pDisplayMode;
        actions = pActions;
    }

    /**
     * get actions
     * 
     * @return the actions
     */
    public Map<String, UiAction> getActions() {
        return actions;
    }

    /**
     * get container
     * 
     * @return the container
     */
    public C getContainer() {
        return container;
    }

    /**
     * get display mode
     * 
     * @return the display mode
     */
    public DisplayMode getDisplayMode() {
        return displayMode;
    }
}
