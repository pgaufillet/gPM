/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.field.multivalued;

import static org.topcased.gpm.ui.component.client.resources.ComponentResources.INSTANCE;

import org.topcased.gpm.ui.component.client.button.GpmDoubleImageButton;
import org.topcased.gpm.ui.component.client.container.field.AbstractGpmField;
import org.topcased.gpm.ui.component.client.resources.ComponentResources;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Element for multivalued fields.
 * 
 * @author tpanuel
 * @param <E>
 *            The type of element.
 */
public class GpmMultivaluedElement<E extends AbstractGpmField<?>> {
    private final E element;

    private GpmDoubleImageButton removeButton;

    private GpmDoubleImageButton undoButton;

    private GpmDoubleImageButton upButton;

    private GpmDoubleImageButton downButton;

    private boolean enabled;

    private FlowPanel buildPanel;

    /**
     * Create a GpmMultivaluedElement.
     * 
     * @param pElement
     *            The element.
     * @param pRemoveClickHandler
     *            The remove click handler.
     * @param pUndoClickHandler
     *            The undo click handler.
     * @param pUpClickHandler
     *            The up click handler.
     * @param pDownClickHandler
     *            The down click handler.
     */
    public GpmMultivaluedElement(final E pElement,
            final ClickHandler pRemoveClickHandler,
            final ClickHandler pUndoClickHandler,
            final ClickHandler pUpClickHandler,
            final ClickHandler pDownClickHandler) {
        element = pElement;
        element.getWidget().getElement().getStyle().setFloat(Style.Float.LEFT);
        enabled = true;
        // Create buttons
        if (pRemoveClickHandler != null) {
            removeButton =
                    createButton(INSTANCE.images().close(),
                            INSTANCE.images().closeHover(), pRemoveClickHandler);
        }
        if (pUndoClickHandler != null) {
            undoButton =
                    createButton(INSTANCE.images().undo(),
                            INSTANCE.images().undoHover(), pUndoClickHandler);
            undoButton.setVisible(false);
        }
        if (pUpClickHandler != null) {
            upButton =
                    createButton(INSTANCE.images().arrowUp(),
                            INSTANCE.images().arrowUpHover(), pUpClickHandler);
        }
        if (pDownClickHandler != null) {
            downButton =
                    createButton(INSTANCE.images().arrowDown(),
                            INSTANCE.images().arrowDownHover(),
                            pDownClickHandler);
        }
    }

    /**
     * Get the element.
     * 
     * @return The element.
     */
    public E getElement() {
        return element;
    }

    /**
     * Build the panel with the element widget and the buttons.
     * 
     * @return The panel.
     */
    public FlowPanel buildPanel() {
        // Create a new panel
        buildPanel = new FlowPanel();
        // Add widget
        buildPanel.add(element.getWidget());
        // Add buttons
        if (upButton != null) {
            buildPanel.add(getBlanckIfNotVisible(upButton));
        }
        if (downButton != null) {
            buildPanel.add(getBlanckIfNotVisible(downButton));
        }
        if (removeButton != null) {
            buildPanel.add(removeButton);
        }
        if (undoButton != null) {
            buildPanel.add(undoButton);
        }
        // Set style
        buildPanel.addStyleName(ComponentResources.INSTANCE.css().gpmMultivaluedWidgetLine());

        return buildPanel;
    }

    private Widget getBlanckIfNotVisible(final GpmDoubleImageButton pButton) {
        if (pButton.isVisible()) {
            return pButton;
        }
        else {
            final SimplePanel lEmpty = new SimplePanel();

            lEmpty.setWidth(pButton.getWidth() + "px");
            lEmpty.getElement().setInnerHTML("&nbsp;");
            lEmpty.addStyleName(ComponentResources.INSTANCE.css().gpmMultivaluedWidgetButton());

            return lEmpty;
        }
    }

    /**
     * Test if the element is enabled.
     * 
     * @return If the element is enabled.
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Set if the element is enabled.
     * 
     * @param pEnabled
     *            If the element is enabled.
     */
    public void setEnabled(final boolean pEnabled) {
        enabled = pEnabled;
        element.setEnabled(pEnabled);
        removeButton.setVisible(pEnabled);
        undoButton.setVisible(!pEnabled);
        if (buildPanel != null) {
            if (pEnabled) {
                buildPanel.removeStyleName(ComponentResources.INSTANCE.css().gpmMultivaluedWidgetLineDeleted());
            }
            else {
                buildPanel.addStyleName(ComponentResources.INSTANCE.css().gpmMultivaluedWidgetLineDeleted());
            }
        }
    }

    private final static GpmDoubleImageButton createButton(
            final ImageResource pImage, final ImageResource pImageHover,
            final ClickHandler pClickHandler) {
        final GpmDoubleImageButton lButton =
                new GpmDoubleImageButton(pImage, pImageHover);

        lButton.addClickHandler(pClickHandler);
        lButton.addStyleName(ComponentResources.INSTANCE.css().gpmMultivaluedWidgetButton());

        return lButton;
    }

    /**
     * Set the position of the element on its multivalued panel.
     * 
     * @param pPosition
     *            The position.
     * @param pNbElement
     *            The number of element on the multivalued panel.
     */
    public void setPosition(final int pPosition, final int pNbElement) {
        if (removeButton != null) {
            removeButton.getElement().setId(String.valueOf(pPosition));
        }
        if (undoButton != null) {
            undoButton.getElement().setId(String.valueOf(pPosition));
        }
        if (upButton != null) {
            upButton.getElement().setId(String.valueOf(pPosition));
            upButton.setVisible(pPosition != 0);
        }
        if (downButton != null) {
            downButton.getElement().setId(String.valueOf(pPosition));
            downButton.setVisible(pPosition != (pNbElement - 1));
        }
    }

    public GpmDoubleImageButton getRemoveButton() {
        return removeButton;
    }

    public GpmDoubleImageButton getUndoButton() {
        return undoButton;
    }

    public GpmDoubleImageButton getUpButton() {
        return upButton;
    }

    public GpmDoubleImageButton getDownButton() {
        return downButton;
    }
}