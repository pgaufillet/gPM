/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Florian ROSIER (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.container;

import org.topcased.gpm.ui.component.client.button.GpmOpenCloseButton;
import org.topcased.gpm.ui.component.client.layout.GpmHorizontalPanel;
import org.topcased.gpm.ui.component.client.resources.ComponentResources;
import org.topcased.gpm.ui.component.client.util.GpmBasicActionHandler;
import org.topcased.gpm.ui.component.client.util.GpmDecoratorPanel;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * GpmDisclosurePanel is a composite that has a similar behavior as the
 * disclosure panel.
 * <p>
 * When the panel open and close, the content visible attribute is modified
 * accordingly to allow the content to be loaded at the opening using a lazy
 * behavior.
 * </p>
 * <p>
 * The panel is closed by default at the instantiation.
 * </p>
 * <p>
 * The panel must be open or close manually before be displayed.
 * </p>
 * 
 * @author frosier
 */
public class GpmDisclosurePanel extends FlowPanel {

    private final GpmDecoratorPanel decoratedHeader;

    /** Indicates if the decorator must be a light or heavy decorator */
    private final boolean fullMode;

    private FlowPanel header;

    private Widget content;

    private GpmOpenCloseButton openCloseButton;

    /**
     * Creates an empty disclosure panel.
     * <p>
     * Initialized in full mode display, with an empty header content and an
     * empty closable content.
     * </p>
     */
    public GpmDisclosurePanel() {
        this(true);
    }

    /**
     * Creates an empty disclosure panel.
     * <p>
     * Initialized with an empty header content and an empty closable content.
     * </p>
     * 
     * @param pFullMode
     *            Indicates if the decorator must be a light or heavy decorator,
     *            <CODE>true</CODE> for heavy and <CODE>false</CODE> for light.
     */
    public GpmDisclosurePanel(boolean pFullMode) {
        super();

        fullMode = pFullMode;

        openCloseButton = new GpmOpenCloseButton();
        openCloseButton.setOpenHandler(new GpmBasicActionHandler<Object>() {
            @Override
            public void execute(final Object pParam) {
                open();
            }
        });
        openCloseButton.setCloseHandler(new GpmBasicActionHandler<Object>() {
            @Override
            public void execute(final Object pParam) {
                close();
            }
        });

        header = new GpmHorizontalPanel();
        header.add(openCloseButton);
        header.setStylePrimaryName(ComponentResources.INSTANCE.css().header());

        decoratedHeader = new GpmDecoratorPanel(pFullMode);
        decoratedHeader.setWidget(header);
        decoratedHeader.setStylePrimaryName(ComponentResources.INSTANCE.css().header());

        add(decoratedHeader);
        if (pFullMode) {
            setStylePrimaryName(ComponentResources.INSTANCE.css().gpmDisclosurePanel());
        }
        else {
            setStylePrimaryName(ComponentResources.INSTANCE.css().gpmLightDisclosurePanel());
            openCloseButton.addStyleName(ComponentResources.INSTANCE.css().gpmLightDisclosurePanelHeader());
        }
    }

    /**
     * Open the panel.
     */
    public void open() {
        if (!fullMode) {
            decoratedHeader.maximize();
        }
        content.setVisible(true);
        openCloseButton.setIsOpen(true);
    }

    /**
     * Close the panel.
     */
    public void close() {
        if (!fullMode) {
            decoratedHeader.minimize();
        }
        content.setVisible(false);
        openCloseButton.setIsOpen(false);
    }

    /**
     * Set the button text.
     * 
     * @param pButtonText
     *            The button text.
     */
    public void setButtonText(final String pButtonText) {
        openCloseButton.setButtonText(pButtonText);
    }

    /**
     * Return the button text
     * 
     * @return The button text
     */
    public String getButtonText() {
        return openCloseButton.getText();
    }

    /**
     * Add element on the disclosure panel header.
     * 
     * @param pHeaderElement
     *            The element to add on the header.
     */
    public void addHeaderContent(final Widget pHeaderElement) {
        SimplePanel lPanel = new SimplePanel();
        lPanel.setStyleName(ComponentResources.INSTANCE.css().gpmLightDisclosurePanelHeader());
        lPanel.add(pHeaderElement);
        header.add(lPanel);
    }

    /**
     * Sets the content widget which can be opened and closed by this panel. If
     * there is a preexisting content widget, it will be detached.
     * 
     * @param pContent
     *            The widget to be used as the content panel.
     */
    public void setContent(final Widget pContent) {
        content = pContent;
        content.setVisible(false);
        if (fullMode) {
            content.addStyleName(ComponentResources.INSTANCE.css().content());
        }
        else {
            content.addStyleName(ComponentResources.INSTANCE.css().gpmLightDisclosurePanelOpened());
        }
        add(content);
    }

    /**
     * Indicates if the panel is disclosed or not
     * 
     * @return <code>true</code> if the panel is opened, else <code>false</code>
     */
    public boolean isOpen() {
        if (content != null) {
            return content.isVisible();
        }
        return false;
    }

    /**
     * Get the open close button
     * 
     * @return the open close button
     */
    protected GpmOpenCloseButton getOpenCloseButton() {
        return openCloseButton;
    }
}