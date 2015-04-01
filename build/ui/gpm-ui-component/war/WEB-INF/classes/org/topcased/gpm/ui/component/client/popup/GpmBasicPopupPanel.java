/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.popup;

import static org.topcased.gpm.ui.component.client.resources.ComponentResources.INSTANCE;

import org.topcased.gpm.ui.component.client.resources.ComponentCssResource;

import com.google.gwt.user.client.ui.PopupPanel;

/**
 * GpmPopupPanel extends {@link PopupPanel} and CSS resource using
 * {@link ComponentCssResource} <h3>CSS Style Rules</h3>
 * <dl>
 * <dt>.gpm-PopupPanel</dt>
 * <dd>the outside of the popup</dd>
 * <dt>.gpm-PopupPanel .popupContent</dt>
 * <dd>the wrapper around the content</dd>
 * </dl>
 * <h4>Note</h4>
 * <p>
 * Doesn't override glass CSS class.
 * </p>
 * 
 * @author mkargbo
 */
public class GpmBasicPopupPanel extends PopupPanel {

    /**
     * Create an empty popup panel.
     * 
     * @see PopupPanel#PopupPanel()
     */
    public GpmBasicPopupPanel() {
        super();
        initCss();
    }

    /**
     * Create an empty popup panel
     * 
     * @see PopupPanel#PopupPanel(boolean)
     * @param pAutoHide
     *            autoHide <code>true</code> if the popup should be
     *            automatically hidden when the user clicks outside of it or the
     *            history token changes.
     */
    public GpmBasicPopupPanel(boolean pAutoHide) {
        super(pAutoHide);
        initCss();
    }

    /**
     * Creates an empty popup panel, specifying its "auto-hide" and "modal"
     * properties. {@see PopupPanel#PopupPanel(boolean, boolean)}
     * 
     * @param pAutoHide
     *            <code>true</code> if the popup should be automatically hidden
     *            when the user clicks outside of it or the history token
     *            changes.
     * @param pModal
     *            <code>true</code> if keyboard or mouse events that do not
     *            target the PopupPanel or its children should be ignored
     */
    public GpmBasicPopupPanel(boolean pAutoHide, boolean pModal) {
        super(pAutoHide, pModal);
        initCss();
    }

    /**
     * Set CSS styles using CSS resource.
     * <p>
     * Set primary style for the popupPanel and its content.
     */
    private void initCss() {
        setStylePrimaryName(INSTANCE.css().gpmBasicPopupPanel());
        setStylePrimaryName(getContainerElement(),
                INSTANCE.css().gpmPopupContent());
    }
}
