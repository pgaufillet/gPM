/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.command.popup.filter.edit;

import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.business.util.FieldsContainerType;
import org.topcased.gpm.ui.application.client.event.GlobalEvent;
import org.topcased.gpm.ui.application.shared.command.link.ExecuteLinkDeletionFilterAction;
import org.topcased.gpm.ui.facade.shared.filter.UiFilter;

import com.google.inject.Inject;

/**
 * A command to execute new filter.
 * 
 * @author nveillet
 */
public class FilterEditionExecuteProductLinkFilterDeletionCommand extends
        AbstractFilterEditionExecuteCommand {

    /**
     * Create a FilterEditionExecuteProductLinkFilterDeletionCommand.
     * 
     * @param pEventBus
     *            The bus event.
     */
    @Inject
    public FilterEditionExecuteProductLinkFilterDeletionCommand(
            EventBus pEventBus) {
        super(pEventBus);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.command.popup.filter.edit.AbstractFilterEditionExecuteCommand#executeFilter(org.topcased.gpm.ui.facade.shared.filter.UiFilter)
     */
    @Override
    protected void executeFilter(UiFilter pFilter) {
        fireEvent(
                GlobalEvent.UNLINK_PRODUCT.getType(),
                new ExecuteLinkDeletionFilterAction(
                        null,
                        FieldsContainerType.PRODUCT,
                        getCurrentProductId(),
                        getPopupManager().getFilterPopupPresenter().getTypeName(),
                        pFilter));
    }
}