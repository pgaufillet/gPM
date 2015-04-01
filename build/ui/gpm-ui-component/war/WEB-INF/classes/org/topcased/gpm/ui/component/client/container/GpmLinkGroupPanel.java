/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.container;

import org.topcased.gpm.ui.component.client.button.GpmImageButton;
import org.topcased.gpm.ui.component.client.resources.ComponentResources;
import org.topcased.gpm.ui.component.client.resources.i18n.Ui18n;
import org.topcased.gpm.ui.component.client.util.GpmBasicActionHandler;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.FlowPanel;

/**
 * A link group panel.
 * 
 * @author tpanuel
 */
public class GpmLinkGroupPanel extends GpmDisclosurePanel {

    private final FlowPanel links;

    private final String linkTypeName;

    /**
     * Create a link group panel.
     */
    public GpmLinkGroupPanel(String pLinkTypeName) {
        super();

        linkTypeName = pLinkTypeName;
        getOpenCloseButton().setValueEvent(linkTypeName);

        links = new FlowPanel();
        setContent(links);
    }

    /**
     * Add a link without setting the handler. It must be affected afterwards
     * 
     * @param pLinkedElementId
     *            The linked element id.
     * @param pLinkedElementRef
     *            The linked element reference.
     * @param pVisu
     *            The visu handler.
     * @param pEdit
     *            The edit handler.
     * @return the GpmDisplayGroupPanel created with the link elements, to be
     *         able to affect a FieldCreationHandler
     */
    public GpmDisplayGroupPanel addLink(final String pLinkedElementId,
            final String pLinkedElementRef, final ClickHandler pVisu,
            final ClickHandler pEdit) {
        final GpmDisplayGroupPanel lLinkPanel = new GpmDisplayGroupPanel(false);

        lLinkPanel.setName(pLinkedElementRef);
        if (pVisu != null) {
            final GpmImageButton lVisuButton =
                    new GpmImageButton(pLinkedElementId,
                            ComponentResources.INSTANCE.images().sheetVisu());

            lVisuButton.addClickHandler(pVisu);
            lVisuButton.setTitle(Ui18n.CONSTANTS.visualize());
            lLinkPanel.addHeaderContent(lVisuButton);
        }
        if (pEdit != null) {
            final GpmImageButton lEditButton =
                    new GpmImageButton(pLinkedElementId,
                            ComponentResources.INSTANCE.images().sheetEdit());

            lEditButton.addClickHandler(pEdit);
            lEditButton.setTitle(Ui18n.CONSTANTS.edit());
            lLinkPanel.addHeaderContent(lEditButton);
        }
        DeferredCommand.addCommand(new Command() {
            @Override
            public void execute() {
                lLinkPanel.open(); // Will be opened after its handlers has been set
            }
        });
        links.add(lLinkPanel);

        return lLinkPanel;
    }

    /**
     * get link type name
     * 
     * @return the link type name
     */
    public String getLinkTypeName() {
        return linkTypeName;
    }

    /**
     * Determine if the link group is empty
     * 
     * @return if the link group is empty
     */
    public boolean isEmpty() {
        return links.getWidgetCount() == 0;
    }

    /**
     * Set the name of the group panel.
     * 
     * @param pName
     *            The group name.
     */
    public void setName(final String pName) {
        setButtonText(pName);
    }

    /**
     * @param pLoadLink
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public void setOpenHandler(GpmBasicActionHandler pLoadLink) {
        getOpenCloseButton().setOpenHandler(pLoadLink);
    }
}