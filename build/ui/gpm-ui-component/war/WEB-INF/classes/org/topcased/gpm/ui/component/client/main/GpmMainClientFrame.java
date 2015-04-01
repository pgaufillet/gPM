/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien Eballard (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.main;

import org.topcased.gpm.ui.component.client.resources.ComponentResources;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * GpmMainClientFrame
 * 
 * @author jeballar
 */
public class GpmMainClientFrame extends DockLayoutPanel {
    private static final double MARGE_HEIGHT = 3;

    private final GpmBanner banner;

    private final Widget workspace;

    /**
     * Constructor
     * 
     * @param pBanner
     *            the banner
     * @param pWorkspace
     *            the workspace
     */
    public GpmMainClientFrame(GpmBanner pBanner, Widget pWorkspace) {
        super(Unit.PX);
        banner = pBanner;
        workspace = pWorkspace;

        addNorth(banner, 0);
        addNorth(new LayoutPanel(), MARGE_HEIGHT);
        add(workspace);

        banner.setContentResizeHandler(new ContentResizeHandler() {
            @Override
            public void contentResized(int pNewSize) {
                ((LayoutData) getChildren().get(0).getLayoutData()).size =
                        pNewSize;
                forceLayout();
            }
        });

        setStylePrimaryName(ComponentResources.INSTANCE.css().gpmWorkspaceLayout());
    }

    /**
     * Interface for object container in the GpmMainClientFrame
     */
    public interface ContentResizeHandler {
        /**
         * Called when content has been resized
         * 
         * @param pNewSize
         *            the new size of the element
         */
        public void contentResized(int pNewSize);
    }

    /**
     * Get the banner.
     * 
     * @return The banner.
     */
    public GpmBanner getBanner() {
        return banner;
    }

    /**
     * Get the workspace.
     * 
     * @return The workspace.
     */
    public Widget getWorkspace() {
        return workspace;
    }
}
