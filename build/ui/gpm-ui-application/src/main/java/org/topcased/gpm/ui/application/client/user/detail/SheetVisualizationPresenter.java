/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.user.detail;

import net.customware.gwt.dispatch.client.DispatchAsync;
import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.business.values.field.BusinessField;
import org.topcased.gpm.business.values.field.simple.BusinessSimpleField;
import org.topcased.gpm.ui.application.client.command.link.LoadSheetLinkOnVisualizationCommand;
import org.topcased.gpm.ui.application.client.command.user.sheet.OpenSheetOnEditionCommand;
import org.topcased.gpm.ui.application.client.command.user.sheet.OpenSheetOnVisualizationCommand;
import org.topcased.gpm.ui.application.client.common.container.sheet.SheetDisplay;
import org.topcased.gpm.ui.application.client.common.container.sheet.SheetPresenter;
import org.topcased.gpm.ui.application.client.menu.user.SheetDetailVisualizationMenuBuilder;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.inject.Inject;

/**
 * Presenter for the SheetView on visualization mode.
 * 
 * @author tpanuel
 * @author phtsaan
 */
public class SheetVisualizationPresenter extends SheetPresenter {

    /**
     * Create a presenter for the SheetView on visualization mode.
     * 
     * @param pDisplay
     *            The display interface.
     * @param pEventBus
     *            The event bus.
     * @param pDispatchAsync
     *            the dispatcher
     * @param pMenuBuilder
     *            The menu builder.
     * @param pVisuCommand
     *            The visualization command.
     * @param pEditCommand
     *            The edition command.
     * @param pLoadLinkCommand
     *            The load link command.
     */
    @Inject
    public SheetVisualizationPresenter(final SheetDisplay pDisplay,
            final EventBus pEventBus, final DispatchAsync pDispatchAsync,
            final SheetDetailVisualizationMenuBuilder pMenuBuilder,
            final OpenSheetOnVisualizationCommand pVisuCommand,
            final OpenSheetOnEditionCommand pEditCommand,
            final LoadSheetLinkOnVisualizationCommand pLoadLinkCommand) {
        super(pDisplay, pEventBus, pMenuBuilder, pVisuCommand, pEditCommand,
                pLoadLinkCommand);
    }

    public ClickHandler getInternalUrlClickHandler() {
        return new OpenSheetOnVisualizationCommand(eventBus);
    }

    /**
     * Get a field value as String, use to get field value by name for applet
     * parameters
     * 
     * @param pFieldName
     *            field name
     * @return <B>field value</B> if the field is set, else <B>null</B> (sheet
     *         or field not found for example)
     */
    public String getFieldValueByName(String pFieldName) {
        String lValue = null;
        BusinessField lField =
                getDisplay().getContainerFieldSet().getDisplayedFields().get(
                        pFieldName);
        if (lField instanceof BusinessSimpleField<?>) {
            if (lField != null) {
                lValue = ((BusinessSimpleField<?>) lField).getAsString();
            }
        }
        return lValue;
    }
}