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

import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.business.values.field.BusinessField;
import org.topcased.gpm.business.values.field.simple.BusinessSimpleField;
import org.topcased.gpm.ui.application.client.command.link.LoadSheetLinkOnEditionCommand;
import org.topcased.gpm.ui.application.client.command.user.sheet.OpenSheetOnEditionCommand;
import org.topcased.gpm.ui.application.client.command.user.sheet.OpenSheetOnVisualizationCommand;
import org.topcased.gpm.ui.application.client.common.container.sheet.SheetDisplay;
import org.topcased.gpm.ui.application.client.common.container.sheet.SheetPresenter;
import org.topcased.gpm.ui.application.client.menu.user.SheetDetailEditionMenuBuilder;
import org.topcased.gpm.ui.component.client.container.field.GpmTextBox;

import com.google.inject.Inject;

/**
 * Presenter for the SheetView on edition mode.
 * 
 * @author tpanuel
 * @author phtsaan
 */
public class SheetEditionPresenter extends SheetPresenter {

    /**
     * Create a presenter for the SheetView on edition mode.
     * 
     * @param pDisplay
     *            The display interface.
     * @param pEventBus
     *            The event bus.
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
    public SheetEditionPresenter(final SheetDisplay pDisplay,
            final EventBus pEventBus,
            final SheetDetailEditionMenuBuilder pMenuBuilder,
            final OpenSheetOnVisualizationCommand pVisuCommand,
            final OpenSheetOnEditionCommand pEditCommand,
            final LoadSheetLinkOnEditionCommand pLoadLinkCommand) {
        super(pDisplay, pEventBus, pMenuBuilder, pVisuCommand, pEditCommand,
                pLoadLinkCommand);
    }

    /**
     * Set a field value as String, use especially for Javascript field updates
     * 
     * @param pFieldName
     *            field name
     * @param pFieldValue
     *            field value
     * @return <CODE>true</CODE> if the field is set, else <CODE>false</CODE>
     *         (sheet or field not found for example)
     */
    public boolean setFieldValue(String pFieldName, String pFieldValue) {
        boolean lRet = false;

        BusinessField lField =
                getDisplay().getContainerFieldSet().getDisplayedFields().get(
                        pFieldName);
        if (lField instanceof GpmTextBox) {
            ((BusinessSimpleField<?>) lField).setAsString(pFieldValue);
            lRet = true;
        }

        return lRet;
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
        String lFieldValue = null;
        BusinessField lField =
                getDisplay().getContainerFieldSet().getDisplayedFields().get(
                        pFieldName);

        if (lField instanceof BusinessSimpleField<?>) {
            lFieldValue = ((BusinessSimpleField<?>) lField).getAsString();
        }
        return lFieldValue;
    }

}