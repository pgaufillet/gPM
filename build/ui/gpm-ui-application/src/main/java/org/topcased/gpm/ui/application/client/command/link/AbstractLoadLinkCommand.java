/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.command.link;

import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.ui.application.client.command.AbstractCommand;
import org.topcased.gpm.ui.component.client.util.GpmBasicActionHandler;

import com.google.inject.Inject;

/**
 * A command to load links.
 * 
 * @author nveillet
 */
public abstract class AbstractLoadLinkCommand extends AbstractCommand implements
        GpmBasicActionHandler<String> {

    /**
     * Create an LoadLinkCommand.
     * 
     * @param pEventBus
     *            The bus event.
     */
    @Inject
    public AbstractLoadLinkCommand(EventBus pEventBus) {
        super(pEventBus);
    }
}
