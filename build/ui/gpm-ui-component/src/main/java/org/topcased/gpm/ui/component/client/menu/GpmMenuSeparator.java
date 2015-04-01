/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 ******************************************************************/
package org.topcased.gpm.ui.component.client.menu;

import org.topcased.gpm.ui.component.client.resources.ComponentResources;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.MenuItem;

/**
 * A menu item separator. A picture is used instead of a simple div because on
 * IE6, the div can be hide.
 * 
 * @author tpanuel
 */
public class GpmMenuSeparator extends MenuItem {
    private final static String IMAGE =
            AbstractImagePrototype.create(
                    ComponentResources.INSTANCE.images().menuSeparator()).getHTML();

    /**
     * Create a menu item separator.
     */
    public GpmMenuSeparator() {
        super(IMAGE, true, new Command() {
            @Override
            public void execute() {
                // Nothing to do
            }
        });
        setStylePrimaryName(ComponentResources.INSTANCE.css().gpmMenuItemSeparator());
    }
}