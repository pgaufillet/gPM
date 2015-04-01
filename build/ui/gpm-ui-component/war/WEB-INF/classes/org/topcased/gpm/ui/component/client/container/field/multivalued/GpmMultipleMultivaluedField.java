/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Joël GIAUFER (Atos Origin)
 ******************************************************************/

package org.topcased.gpm.ui.component.client.container.field.multivalued;

import java.util.List;

import org.topcased.gpm.business.values.field.multivalued.BusinessMultivaluedField;
import org.topcased.gpm.ui.component.client.container.field.AbstractGpmField;
import org.topcased.gpm.ui.component.client.field.multivalued.GpmMultipleMultivaluedElement;
import org.topcased.gpm.ui.component.client.field.multivalued.GpmMultipleMultivaluedWidget;

/**
 * The GpmMultipleMultivaluedField to be instantiate to display an
 * UIMultipleMultivaluedField element
 * 
 * @author jgiaufer
 */
public class GpmMultipleMultivaluedField
        extends
        AbstractGpmMultivaluedField<GpmMultipleMultivaluedElement, GpmMultipleMultivaluedWidget>
        implements BusinessMultivaluedField<GpmMultipleMultivaluedElement> {

    /**
     * Generic constructor
     * 
     * @param pFields
     *            The fields.
     * @param pAddAvailable
     *            if the user can add a line
     * @param pEditLineAvailable
     *            if the user can edit the number of line
     * @param pMoveButtonsAvailable
     *            if the user can move lines up or down
     * @param pDisplayHeader
     *            If the header need to be displayed.
     */
    public GpmMultipleMultivaluedField(final List<AbstractGpmField<?>> pFields,
            final boolean pAddAvailable, final boolean pEditLineAvailable,
            final boolean pMoveButtonsAvailable, final boolean pDisplayHeader) {
        super(new GpmMultipleMultivaluedWidget(pFields, pAddAvailable,
                pEditLineAvailable, pMoveButtonsAvailable, pDisplayHeader));
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.multivalued.BusinessMultivaluedField#addLine()
     */
    @Override
    public GpmMultipleMultivaluedElement addLine() {
        return getWidget().addLine();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.multivalued.BusinessMultivaluedField#removeLine()
     */
    @Override
    public GpmMultipleMultivaluedElement removeLine() {
        final int lSize = size();

        if (lSize > 0) {
            final GpmMultipleMultivaluedElement lFirst = getFirst();

            getWidget().remove(size() - 1);

            return lFirst;
        }
        else {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessField#isUpdatable()
     */
    @Override
    public boolean isUpdatable() {
        return getWidget().isAddButtonAvailable()
                || getWidget().isRemoveButtonAvailable()
                || getWidget().isMoveButtonsAvailable()
                || getWidget().getTemplateField().isUpdatable();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.container.field.AbstractGpmField#getEmptyClone()
     */
    @Override
    public GpmMultipleMultivaluedField getEmptyClone() {
        final GpmMultipleMultivaluedField lClone =
                new GpmMultipleMultivaluedField(
                        getWidget().getTemplateField().getEmptyClone().getFields(),
                        getWidget().isAddButtonAvailable(),
                        getWidget().isRemoveButtonAvailable(),
                        getWidget().isMoveButtonsAvailable(),
                        getWidget().isDisplayHeader());

        initEmptyClone(lClone);

        return lClone;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.container.field.multivalued.AbstractGpmMultivaluedField#getFieldList()
     */
    @Override
    public List<GpmMultipleMultivaluedElement> getFieldList() {
        return getWidget().getAvailableLines();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.container.field.AbstractGpmField#setEnabled(boolean)
     */
    @Override
    public void setEnabled(boolean pEnabled) {
        getWidget().setEnabled(pEnabled);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.container.field.AbstractGpmField#isEnabled()
     */
    @Override
    public boolean isEnabled() {
        return getWidget().isEnabled();
    }
}