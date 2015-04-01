/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.common.container.sheet;

import java.util.List;

import org.topcased.gpm.ui.application.client.common.container.ContainerView;
import org.topcased.gpm.ui.component.client.button.GpmToolTipImageButton;
import org.topcased.gpm.ui.component.client.menu.GpmMenuTitle;
import org.topcased.gpm.ui.component.client.popup.GpmToolTipPanel;
import org.topcased.gpm.ui.component.client.resources.ComponentResources;
import org.topcased.gpm.ui.component.client.resources.i18n.Ui18n;
import org.topcased.gpm.ui.component.client.util.IToolTipCreationHandler;
import org.topcased.gpm.ui.facade.shared.container.sheet.state.UiTransition;

import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;

/**
 * View that display a sheet.
 * 
 * @author tpanuel
 */
public class SheetView extends ContainerView implements SheetDisplay {

    private static final int HISTORY_COLUMN_COUNT = 3;

    private static final int HISTORY_CELL_SPACING = 5;
    
    private static final int S_KEYCODE = 83;

    private final GpmMenuTitle statePanel;

    /**
     * Constructor
     */
    public SheetView() {
        super();
        statePanel = new GpmMenuTitle(false);
        getMenu().addTitle(statePanel);

        // Add Key Handler on Alt+S key : Quick save
        KeyPressHandler lCtrlFFHookKeyPressHandler = new KeyPressHandler() {
            @Override
            public void onKeyPress(KeyPressEvent pEvent) {
                if (pEvent.isControlKeyDown()) {
                    if (pEvent.getCharCode() == 's') {
                        pEvent.preventDefault();
                    }
                }
            }
        };
        this.addDomHandler(lCtrlFFHookKeyPressHandler, KeyPressEvent.getType());

        KeyDownHandler lControlSKeyDownHandler = new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent pEvent) {
                if (pEvent.isControlKeyDown()
                        && (pEvent.getNativeKeyCode() == S_KEYCODE)) {
                	nativeMethod();
                	pEvent.preventDefault();
                    getMenu().getToolBar(0).executeSaveCommand();
                }
            }
        };
        this.addDomHandler(lControlSKeyDownHandler, KeyDownEvent.getType());
    }
    
    private native void nativeMethod()
    /*-{
          window.focus();
    }-*/;

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.user.detail.SheetEditionDisplay#setSheetState(java.lang.String)
     */
    @Override
    public void setSheetState(final String pSheetState) {
        statePanel.setHTML('(' + pSheetState + ')');
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.common.container.sheet.SheetDisplay#setTransitionHistory(java.util.List)
     */
    @Override
    public void setTransitionHistory(List<UiTransition> pTransitions) {
        if (!pTransitions.isEmpty()) {
            GpmToolTipImageButton lButton =
                    new GpmToolTipImageButton(
                            ComponentResources.INSTANCE.images().sheetTransitions());
            lButton.setWidgetCreationHandler(buildTransitionHistoricHandler(pTransitions));
            lButton.setTitle(Ui18n.CONSTANTS.transitionsHistoryTitle());
            getMenu().addTitleButton(lButton);
        }
    }

    /**
     * Create a handler to display a popup with transitions history. If
     * transitions is empty, <code>null</code> is returned
     * 
     * @param pTransitions
     *            the transitions to display
     * @return The handler for the popup to pop or <code>null</code> if no
     *         transitions
     */
    private IToolTipCreationHandler buildTransitionHistoricHandler(
            List<UiTransition> pTransitions) {
        final Grid lGrid = new Grid(pTransitions.size(), HISTORY_COLUMN_COUNT);
        lGrid.addStyleName(ComponentResources.INSTANCE.css().transitionHistoryContent());
        lGrid.setCellSpacing(HISTORY_CELL_SPACING);
        for (int i = 0; i < pTransitions.size(); i++) {
            int j = 0;
            UiTransition lTransition = pTransitions.get(i);
            lGrid.setWidget(i, j++, new Label(lTransition.getLogin()));
            lGrid.setWidget(i, j++, new Label(
                    DateTimeFormat.getShortDateTimeFormat().format(
                            lTransition.getDate())));
            lGrid.setWidget(i, j++, new Label(lTransition.getOriginState()
                    + " -> " + lTransition.getDestinationState()));
        }
        IToolTipCreationHandler lHandler = new IToolTipCreationHandler() {
			@Override
            public void fillPopupContent(GpmToolTipPanel pToolTip) {
                pToolTip.setHeader(new Label(
                        Ui18n.CONSTANTS.transitionsHistoryTitle()));
                pToolTip.setContent(lGrid);
            }
        };
        return lHandler;
    }
}