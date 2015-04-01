/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Florian ROSIER (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.field;

import static org.topcased.gpm.ui.component.client.resources.ComponentResources.INSTANCE;

import org.topcased.gpm.ui.component.client.button.GpmImageButton;
import org.topcased.gpm.ui.component.client.layout.GpmFlowLayoutPanel;
import org.topcased.gpm.ui.component.client.menu.GpmRichTextToolbar;
import org.topcased.gpm.ui.component.client.resources.ComponentResources;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.HasBlurHandlers;
import com.google.gwt.event.logical.shared.InitializeEvent;
import com.google.gwt.event.logical.shared.InitializeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.RichTextArea;

/**
 * GpmRichTextBoxWidget is a useful component to edit texts with decorations.
 * <p>
 * It contains an area with decorated text and toolbar to apply text decorations
 * on selection or following texts
 * </p>
 * 
 * @author frosier
 */
public class GpmRichTextBoxWidget extends HorizontalPanel implements
        HasBlurHandlers {

    private static final String STYLE_NAME =
            ComponentResources.INSTANCE.css().gpmRichTextBoxWidget();

    private static final String RICH_TEXT_TOOLBAR_STYLE_NAME =
            ComponentResources.INSTANCE.css().gpmRichTextToolbar();

    private static final String RICH_TEXT_AREA_STYLE_NAME =
            ComponentResources.INSTANCE.css().gpmRichTextArea();

    private static final ImageResource ZOOM_BUTTON_DEFAULT_RESOURCE =
            INSTANCE.images().zoom();

    private final GpmFlowLayoutPanel panel;

    private final GpmRichTextToolbar richTextToolbar;

    private final GpmFlowLayoutPanel richTextAreaContainer;

    private final ResizableRichTextArea richTextArea;

    private final GpmImageButton zoomButton;

    private boolean enabled;

    /**
     * Creates a rich text area with its toolbar (formatting text
     * implementation).
     */
    public GpmRichTextBoxWidget() {
        super();
        panel = new GpmFlowLayoutPanel();
        richTextArea = new ResizableRichTextArea();
        richTextArea.setFocus(true);
        richTextArea.ensureDebugId("gpmRichText-area");
        richTextArea.getElement().setAttribute("frameBorder", "0");

        richTextToolbar = new GpmRichTextToolbar(richTextArea);
        panel.add(richTextToolbar);

        richTextAreaContainer = new GpmFlowLayoutPanel();
        richTextAreaContainer.add(richTextArea);
        panel.add(richTextAreaContainer);

        richTextAreaContainer.setStylePrimaryName(RICH_TEXT_AREA_STYLE_NAME);

        richTextToolbar.setStylePrimaryName(RICH_TEXT_TOOLBAR_STYLE_NAME);

        panel.setStylePrimaryName(STYLE_NAME);

        DeferredCommand.addCommand(new Command() {
            @Override
            public void execute() {
                richTextArea.onResize();
            }
        });

        zoomButton = new GpmImageButton(ZOOM_BUTTON_DEFAULT_RESOURCE);

        setHTML("");

        setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
        add(panel);
        add(zoomButton);
    }

    /**
     * A specific richTextArea with better resize behaviour
     */
    private class ResizableRichTextArea extends RichTextArea implements
            RequiresResize {

        public ResizableRichTextArea() {
            addInitializeHandler(new InitializeHandler() {

                @Override
                public void onInitialize(InitializeEvent pEvent) {
                    setFocus(true);
                }
            });
        }

        @Override
        public void onResize() {
            // Defer the resize to be sure it's done after the display
            DeferredCommand.addCommand(new Command() {
                @Override
                public void execute() {
                    setSize(richTextAreaContainer.getOffsetWidth() + "px",
                            richTextAreaContainer.getOffsetHeight() + "px");
                }
            });
        }
    }

    /**
     * Sets the richTextToolbar width to 100%
     */
    public void expandWidth() {
        richTextToolbar.getElement().getStyle().setWidth(100, Unit.PCT);
    }

    //---- RichTextArea mapping methods ----//

    /**
     * Set the content of the text area.
     * 
     * @param pHtml
     *            The text value.
     */
    public void setHTML(final String pHtml) {
        richTextArea.setHTML(pHtml);
        richTextArea.setFocus(true);
    }

    /**
     * Get the content of the rich text area.
     * 
     * @return The text area value.
     */
    public String getHTML() {
        return richTextArea.getHTML();
    }

    /**
     * Get the richTextArea.
     * 
     * @return the richTextArea.
     */
    public RichTextArea getRichTextArea() {
        return richTextArea;
    }

    /**
     * Enable disable field
     * 
     * @param pEnabled
     *            <code>true</code> to enable field, <code>false</code> to
     *            disable
     */
    public void setEnabled(boolean pEnabled) {
        // Never disable the richTextArea because it generates unidentified exceptions in IE 
        // TODO find a way to disable richTextArea properly (without IE exceptions)
        //        richTextArea.setEnabled(pEnabled);
        enabled = pEnabled;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.client.ui.UIObject#setHeight(java.lang.String)
     */
    @Override
    public void setHeight(String pHeight) {
        panel.setHeight(pHeight);
        DeferredCommand.addCommand(new Command() {
            @Override
            public void execute() {
                richTextAreaContainer.setHeight(getOffsetHeight()
                        - richTextToolbar.getOffsetHeight() + "px");
                richTextArea.onResize();
            }
        });
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.event.dom.client.HasBlurHandlers#addBlurHandler(com.google.gwt.event.dom.client.BlurHandler)
     */
    @Override
    public HandlerRegistration addBlurHandler(BlurHandler pHandler) {
        return richTextArea.addBlurHandler(pHandler);
    }

    public GpmImageButton getZoomButton() {
        return zoomButton;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public GpmRichTextToolbar getRichTextToolbar() {
        return richTextToolbar;
    }

}
