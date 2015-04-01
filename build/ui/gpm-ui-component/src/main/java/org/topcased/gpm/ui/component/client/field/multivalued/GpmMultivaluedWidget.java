/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Joël GIAUFER (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.field.multivalued;

import org.topcased.gpm.ui.component.client.container.field.AbstractGpmField;
import org.topcased.gpm.ui.component.client.resources.ComponentResources;

import com.google.gwt.user.client.ui.FlowPanel;

/**
 * Abstract class for the GpmMultiValuedFields. Generate the main Composite
 * widget which will received the multivalued fields. Defines the GpmButtons to
 * add or remove an element from the list of GpmComponents and implements list
 * manipulation of these GpmComponents. By default the add button is <b>not</b>
 * displayed
 * 
 * @author jgiaufer
 * @param <A>
 *            A AbstractGpmField of any type
 */
public class GpmMultivaluedWidget<A extends AbstractGpmField<?>> extends
        AbstractGpmMultivaluedWidget<A> {
    /**
     * Create a GpmMultivaluedWidget.
     * 
     * @param pTemplate
     *            the template.
     * @param pAddButtonAvailable
     *            If the add button is available.
     * @param pRemoveButtonAvailable
     *            If the remove button is available.
     * @param pMoveButtonAvailable
     *            If the move button is available.
     */
    public GpmMultivaluedWidget(final A pTemplate,
            final boolean pAddButtonAvailable,
            final boolean pRemoveButtonAvailable,
            final boolean pMoveButtonAvailable) {
        super(pTemplate, pAddButtonAvailable, pRemoveButtonAvailable,
                pMoveButtonAvailable);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.field.multivalued.AbstractGpmMultivaluedWidget#doRefreshDisplay()
     */
    protected void doRefreshDisplay() {
        for (final GpmMultivaluedElement<A> lElement : getElements()) {
            final FlowPanel lBuilPanel = lElement.buildPanel();

            // Add CSS if deleted
            if (!lElement.isEnabled()) {
                lBuilPanel.addStyleName(ComponentResources.INSTANCE.css().gpmMultivaluedWidgetLineDeleted());
            }
            add(lBuilPanel);

        }
    }
}